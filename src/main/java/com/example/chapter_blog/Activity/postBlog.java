package com.example.chapter_blog.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserManager;
import com.example.chapter_blog.util.Client;
import com.example.chapter_blog.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class postBlog extends Activity implements View.OnClickListener {

    private static final int FILE_SELECTION_REQUEST_CODE =4 ;
    private EditText txTitle, txContent, dynamicEditText;
    private Button btnUploadImage, btnUploadFile,btnPost;
    private ImageView  btnBack;

    private LinearLayout blog_layout;

    private final static int CHOOSE_PICTURE = 2;
    private final static int CHOOSE_PICTURE_KITKAT = 3;
    private Map<String, String> selectedFilesMap=null;

    private List<String> listStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);
        initViews();
    }


    @SuppressLint("WrongViewCast")
    private void initViews() {
        txTitle = findViewById(R.id.tx_title);
        btnUploadImage = findViewById(R.id.btn_upLoadImage);
        btnUploadFile = findViewById(R.id.btn_upLoadFile);
        blog_layout = findViewById(R.id.blog_layout);
        btnPost=findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);
        btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);
        txContent=findViewById(R.id.tx_content);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_upLoadImage) {
            choosePicture();
        } else if (id == R.id.btn_post) {
            try {
                post();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Add cases for other buttons if needed
        }else if(id==R.id.btn_back){
            finish();
        } else if (id==R.id.btn_upLoadFile) {
            // 使用startActivityForResult启动另一个界面
//            Intent intent = new Intent(CurrentActivity.this, FileSelectionActivity.class);
//            startActivityForResult(intent, FILE_SELECTION_REQUEST_CODE);
        }
    }

    private void post() throws JSONException, ExecutionException, InterruptedException {
        JSONObject blogJson=new JSONObject();
        User currentUser= UserManager.getInstance().getCurrentUser();
        blogJson.put("userId",currentUser.getId());
        blogJson.put("title",txTitle.getText());
        int childCount = blog_layout.getChildCount();
        listStr = new ArrayList<>();
        int i;
        // 创建一个 JsonObject 用于存储 blogJson 的 body 部分
        JSONObject bodyJson = new JSONObject();
        JSONObject filesJson = new JSONObject();
        for (i = 0; i < childCount; i++) {
            View tempView = blog_layout.getChildAt(i);

            if (tempView instanceof EditText) {
                // 如果是 EditText，将文本内容放入 bodyJson
                EditText editText = (EditText) tempView;
                bodyJson.put("text" + i, String.valueOf(editText.getText()));
            } else if (tempView instanceof ImageView) {
                // 如果是 ImageView，尝试获取图像并进行处理
                ImageView imageView = (ImageView) tempView;

                // 获取图像的 Bitmap 对象
                Bitmap imageBitmap = getImageBitmapFromImageView(imageView);

                // 将 Bitmap 转为 Base64 字符串
                String imageBase64 = convertBitmapToBase64(imageBitmap);

                // 将 Base64 字符串放入 bodyJson
                bodyJson.put("image" + i, imageBase64);
            } else if (tempView instanceof TextView) {
                TextView textView = (TextView) tempView;
                String fileName = textView.getText().toString();
                if (selectedFilesMap.containsKey(fileName)) {
                    filesJson.put("files" + i, selectedFilesMap.get(fileName));
                }
            }
        }

        bodyJson.put("text" + i, txContent.getText());
        // 将 bodyJson 添加到主 JsonObject 的 body 部分
        blogJson.put("body", bodyJson);
        bodyJson.put("files",filesJson);


        blogJson.put("date", DateUtil.getNowTime());
        boolean result = Client.sendPost("/blog/add", String.valueOf(blogJson)).get();
        if(result){
            Toast.makeText(postBlog.this, "发布成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(postBlog.this, "发布失败", Toast.LENGTH_SHORT).show();
        }
    }
    private Bitmap getImageBitmapFromImageView(ImageView imageView) {
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            return bitmapDrawable.getBitmap();
        }
        return null;
    }
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void choosePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, CHOOSE_PICTURE_KITKAT);
        } else {
            startActivityForResult(intent, CHOOSE_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    if (cursor != null) {
                        int image_columns_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(image_columns_index);
                        if (Build.VERSION.SDK_INT < 14) {
                            cursor.close();
                        }
                        addTextView(txContent.getText());
                        addImageView(path);
                        addTextView(txContent.getText());
                    }
                    break;
                case CHOOSE_PICTURE_KITKAT:
                    uri = data.getData();
                    if (uri != null) {
                        String path = getPath(uri);
                        addTextView(txContent.getText());
                        addImageView(path);
                        addTextView(txContent.getText());
                    }
                    break;
                case FILE_SELECTION_REQUEST_CODE:
                    // 处理从另一个界面返回的数据
                    if (data != null) {
                        selectedFilesMap = (Map<String, String>) data.getSerializableExtra("selectedFilesMap");
                        // 处理获取到的文件Map
                        if (selectedFilesMap != null) {
                            for (Map.Entry<String, String> entry : selectedFilesMap.entrySet()) {
                                String fileName = entry.getKey();
                                String url = entry.getValue();
                                // 处理每个文件名和对应的URL
                                addTextView(fileName);
                                // ...
                            }
                        }
                    }
                    break;

                // Handle other cases if needed
            }
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void addTextView(CharSequence editable) {
        EditText txtView = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        txtView.setLayoutParams(params);
        txtView.setBackgroundDrawable(null);
        txtView.setText(editable);
        txContent.setText("");
        blog_layout.addView(txtView);
    }
    private void addTextView(String text) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(text);
        blog_layout.addView(textView);
    }


    private void addImageView(String path) {
        final ImageView imgView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        imgView.setLayoutParams(params);
        imgView.setAdjustViewBounds(true);
        imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imgView.setTag(path);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(postBlog.this, "图片地址" + imgView.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                blog_layout.removeView(imgView);
                Toast.makeText(postBlog.this, "图片已删除", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int scaledHeight = (int) ((float) originalHeight / originalWidth * screenWidth);

        params.width = screenWidth;
        params.height = scaledHeight;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, scaledHeight, true);
        imgView.setImageBitmap(scaledBitmap);

        blog_layout.addView(imgView);
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }
}