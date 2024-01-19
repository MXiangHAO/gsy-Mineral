package com.example.chapter_blog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private TextView fileNameTextView;
    private Uri fileUri;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();

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

    private void uploadFile(Uri fileUri) {
        try {
            // 打开InputStream从Uri
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            // 创建临时文件
            File tempFile = createTempFile(inputStream);
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
                    .url("http://81.70.43.2:8002/cug/file/oss") // 替换为你的上传URL
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
                            fileNameTextView.setText("上传成功");
                        } else {
                            fileNameTextView.setText("上传失败: " + response.code());
                        }
                    });
                    tempFile.delete(); // 删除临时文件
                }
            });

// TODO: 2024/1/3 :下面是传递到另一个页面： 
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String responseData = response.body().string();
//                        fileNameTextView.setText("上传成功");
//                        try {
//                            JSONObject jsonObject = new JSONObject(responseData);
//                            JSONObject dataObject = jsonObject.getJSONObject("data");
//
//                            
//                            Iterator<String> keys = dataObject.keys();
//                            if (keys.hasNext()) {
//                                String key = keys.next();  // 获取键，即URL
//                                String filename = dataObject.getString(key); // 获取值，即文件名
//
//                                Log.d("UploadResponse", "URL: " + key);
//                                Log.d("UploadResponse", "Filename: " + filename);
//                                //传递到另一个页面：
//
////                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
////
////                                intent.putExtra("url", key);
////                                intent.putExtra("filename", filename);
////                                startActivity(intent);
////                                startActivity(intent);
//
//                            }
//                        } catch (JSONException e) {
//                            Log.e("JSONError", "Failed to parse JSON", e);
//                        }
//                    } else {
//                        Log.e("UploadResponse", "Server returned error: " + response.code());
//                    }
//                }
//
//            });

        } catch (Exception e) {
            e.printStackTrace();
            fileNameTextView.setText("上传失败: " + e.getMessage());
        }
    }

    private File createTempFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("upload", null, getCacheDir());
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
