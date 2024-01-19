package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.UserInfo;
import com.example.chapter_blog.util.NetworkUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class userInfoActivity extends AppCompatActivity {
    // 定义ActivityResultLauncher
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final Handler handler = new Handler();
    private RecyclerView blogRecyclerView;
    private NetworkUtil networkUtil = new NetworkUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        // 获取从其他Activity传来的Intent
        Intent intent1 = getIntent();
        // 读取附加的用户ID数据，默认值为-1（或您选择的任何适当默认值）
        int uId = intent1.getIntExtra("uId", -1); // 注意默认值

        // 假设要获取 ID 为 1 的用户信息(这个要根据实际情况调整)
        fetchUserData(uId);
        //Todo:保存按钮的功能
        ImageButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            UserInfo userInfo = new UserInfo();

            userInfo.setFullName(((EditText) findViewById(R.id.nameEditText)).getText().toString());

            String birthdayStr = ((EditText) findViewById(R.id.birthdayEditText)).getText().toString();
            userInfo.setBirthday(birthdayStr);

            userInfo.setGender(((EditText) findViewById(R.id.genderEditText)).getText().toString());
            userInfo.setAddress(((EditText) findViewById(R.id.addressEditText)).getText().toString());
            userInfo.setOccupation(((EditText) findViewById(R.id.occupationEditText)).getText().toString());

            try {
                long userId = Long.parseLong(((EditText) findViewById(R.id.userIDEditText)).getText().toString());
                userInfo.setUserId((int) userId);
            } catch (NumberFormatException e) {
                userInfo.setUserId(0); // 如果转换失败，设置为 null 或默认值
            }

            // 获取 ImageView 中的图像并转换为 Base64 编码
            ImageView imageView = findViewById(R.id.userImageView);
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // 设置用户头像的 Base64 编码
            userInfo.setAvatar(encodedImage);

            try {
                saveUserData(userInfo);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        // 获取返回按钮
        ImageButton backfileButton = findViewById(R.id.backButton);

        //Todo: 设置返回按钮的点击监听器
        backfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建跳转的意图
                Intent intent = new Intent(userInfoActivity.this, firstUserInfoActivity.class);
                startActivity(intent);
                // 结束当前Activity
                finish();
            }
        });

        // 获取个人中心按钮
        ImageButton personancenterButton = findViewById(R.id.personalCenterButton);

        // Todo：设置个人中心按钮的点击监听器
        personancenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建跳转的意图
                Intent intent = new Intent(userInfoActivity.this, personalCenterActivity.class);
                intent.putExtra("uId", uId);
                startActivity(intent);

            }
        });

       // 初始化ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ImageView userImageView = findViewById(R.id.userImageView);
                        userImageView.setImageURI(selectedImage); // 设置新头像
                        saveImageToInternalStorage(selectedImage); // 保存新头像
                    }
                });


        // 找到头像ImageView
        ImageView userImageView = findViewById(R.id.userImageView);

        // 为ImageView设置点击监听器
        userImageView.setOnClickListener(view -> {
            // 创建Intent以打开图库
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        loadImageFromInternalStorage(); // 加载保存的头像


    }
    //Todo:呈现当前用户信息
    private void fetchUserData(long userId) {
                new Thread(() -> {
            try {
                String response = networkUtil.get("/api/userprofile/" + userId);
                JSONObject jsonResponse = new JSONObject(response);
                handler.post(() -> {
                    try {
                        updateUI(jsonResponse.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
               // Log.e(TAG, "Error fetching user name: ", e);
            }
        }).start();
    }

    //Todo:更新UI
    private void updateUI(String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = objectMapper.readValue(jsonData, UserInfo.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        // 进行更改
        ((EditText) findViewById(R.id.nameEditText)).setText(userInfo.getFullName());
        ((EditText) findViewById(R.id.birthdayEditText)).setText(userInfo.getBirthday());
        ((EditText) findViewById(R.id.genderEditText)).setText(userInfo.getGender());
        ((EditText) findViewById(R.id.addressEditText)).setText(userInfo.getAddress());
        ((EditText) findViewById(R.id.occupationEditText)).setText(userInfo.getOccupation());
        ((EditText) findViewById(R.id.userIDEditText)).setText(userInfo.getUserId() != 0 ? String.valueOf(userInfo.getUserId()) : "");

        // 更新头像
        if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty()) {
            byte[] decodedString = Base64.decode(userInfo.getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ((ImageView) findViewById(R.id.userImageView)).setImageBitmap(decodedByte);
        }

    }

    //Todo:保存按钮
    private void saveUserData(UserInfo user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        Log.d("saveUserData", "JSON payload: " + json);

        NetworkUtil networkUtil = new NetworkUtil();
        String url = "/api/userprofile/";

        networkUtil.postAsync(url, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(userInfoActivity.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(userInfoActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(userInfoActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    //Todo:保存选择的头像到内部存储
    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            // 使用内部存储保存位图
            FileOutputStream fos = openFileOutput("userProfileImage", MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Todo:从内部存储加载头像
    private void loadImageFromInternalStorage() {
        try {
            File file = getFileStreamPath("userProfileImage");
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                ImageView userImageView = findViewById(R.id.userImageView);
                userImageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


