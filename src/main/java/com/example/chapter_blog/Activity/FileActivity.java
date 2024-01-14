package com.example.chapter_blog.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter_blog.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private TextView fileNameTextView;
    private Uri fileUri;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        client = new OkHttpClient();

        Button buttonReturn = findViewById(R.id.button_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileActivity.this, TargetActivity.class);
                startActivity(intent);
            }
        });
        Button selectFileButton = findViewById(R.id.button_select_file);
        Button uploadButton = findViewById(R.id.button_upload);
        fileNameTextView = findViewById(R.id.text_file_name);

        selectFileButton.setOnClickListener(v -> showFileChooser());

        uploadButton.setOnClickListener(v -> {
            if (fileUri != null) {
                uploadFile(fileUri);
            } else {
                fileNameTextView.setText("请选择文件");
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            fileUri = data.getData();
            fileNameTextView.setText(fileUri.getPath()); // 显示文件路径或URI
        }
    }



    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf('.') > 0) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } else {
            return "";
        }
    }

    private void uploadFile(Uri fileUri) {
        try {
            // 打开InputStream从Uri
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            String fileName = getFileName(fileUri);
            String fileExtension = getFileExtension(fileName);

            // 创建临时文件
            File tempFile = createTempFile(fileName,inputStream, fileExtension);
            if (tempFile == null) {
                throw new IOException("无法创建临时文件");
            }

            // 创建RequestBody并上传文件
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), tempFile);
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", tempFile.getName(), fileBody)
                    .build();

            // 创建并发送请求
            Request request = new Request.Builder()
                    .url("http://81.70.43.2:8002/cug/file/oss")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> fileNameTextView.setText("上传失败: " + e.getMessage()));
                }
                @Override
                public void onResponse(Call call, Response response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            try {
                                // 解析响应体
                                String responseBody = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseBody);
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                JSONObject urlFilenameObject=dataObject.getJSONObject("url,Filename");
                                Iterator<String> keys =urlFilenameObject.keys();

                                if (keys.hasNext()) {
                                    String url = keys.next(); // 这里的 key 是 URL
                                    String fileName = urlFilenameObject.getString(url); // 从嵌套的 JSON 对象中获取文件名
                                    Log.d("UploadInfo", "URL: " + url + ", FileName: " + fileName);
                                    int userId = 1; // 用户ID


                                    RequestBody secondRequestBody = new FormBody.Builder()
                                            .add("uId", String.valueOf(userId))
                                            .add("url", url)
                                            .add("filename", fileName)
                                            .build();

                                    Request secondRequest = new Request.Builder()
                                            .url("http://81.70.43.2:8300/files/upload")
                                            .post(secondRequestBody)
                                            .build();

                                    client.newCall(secondRequest).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            runOnUiThread(() -> fileNameTextView.setText("第二次上传失败: " + e.getMessage()));
                                        }

                                        @Override
                                        public void onResponse(Call call, Response secondResponse) throws IOException {
                                            runOnUiThread(() -> {
                                                if (secondResponse.isSuccessful()) {
                                                    fileNameTextView.setText("全部上传成功");
                                                } else {
                                                    fileNameTextView.setText("第二次上传失败: " + secondResponse.code());
                                                }
                                            });
                                        }
                                    });


                                    fileNameTextView.setText("上传成功");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                fileNameTextView.setText("解析响应失败: " + e.getMessage());
                            }
                        } else {
                            fileNameTextView.setText("上传失败: " + response.code());
                        }
                    });
                    tempFile.delete(); // 删除临时文件
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            fileNameTextView.setText("上传失败: " + e.getMessage());
        }
    }

    private File createTempFile(String filename,InputStream inputStream, String extension) throws IOException {
        // 使用时间戳生成一个唯一的文件名，防止文件名冲突
        String tempFileName = "upload_" +filename;

        // 如果从URI获取的文件名中有后缀，使用它
        if (extension != null && !extension.isEmpty()) {
            tempFileName += "." + extension;
        }

        File tempFile = new File(getCacheDir(), tempFileName);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            // 将InputStream内容写入临时文件
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return tempFile;
        }
    }
}
