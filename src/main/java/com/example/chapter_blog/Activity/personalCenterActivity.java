package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.util.NetworkUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class personalCenterActivity extends Activity {
    private EditText usernameEditText, passwordEditText, emailEditText, phoneEditText;
    private final OkHttpClient client = new OkHttpClient();
    private final Handler handler = new Handler();
    private RecyclerView blogRecyclerView;
    private NetworkUtil networkUtil = new NetworkUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        // 获取从其他Activity传来的Intent
        Intent intent1 = getIntent();
        // 读取附加的用户ID数据，默认值为-1（或您选择的任何适当默认值）
        int uId = intent1.getIntExtra("uId", -1); // 注意默认值
        //获得id为1的用户个人信息
        fetchUserData(uId);
        // 获取用户输入的 EditText 引用
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        //获得保存按钮
        ImageButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在这里创建 user 对象并获取用户输入的数据
                User userData = new User();
                userData.setUsername(usernameEditText.getText().toString());
                userData.setPassword(passwordEditText.getText().toString());
                userData.setPhone(phoneEditText.getText().toString());
                userData.setEmail(emailEditText.getText().toString());
                // 获取当前用户
              //  User currentUser = UserManager.getInstance().getCurrentUser();
               // int id = currentUser.getId();
                //这个id应该是要从登陆状态获取的,此处直接假设为1
                userData.setId(uId);
                try {
                    saveUserData(userData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // 获取返回按钮
        ImageButton backbutton = findViewById(R.id.backButton);

        // 设置点击监听器
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

    }


    //Todo:呈现当前用户安全信息
    private void fetchUserData(Integer id) {
        new Thread(() -> {
            try {
                String response = networkUtil.get("/api/user/" + id);
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
        User user1 = objectMapper.readValue(jsonData, User.class);

        // 进行更改
        ((EditText) findViewById(R.id.usernameEditText)).setText(user1.getUsername());
        ((EditText) findViewById(R.id.passwordEditText)).setText(user1.getPassword());
        ((EditText) findViewById(R.id.phoneEditText)).setText(user1.getPhone());
        ((EditText) findViewById(R.id.emailEditText)).setText(user1.getEmail());
    }

    //Todo:保存修改后的信息
    private void saveUserData(User userData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userData);
        Log.d("saveUserData", "JSON payload: " + json); // 输出查看生成的 JSON

        // 实例化 NetworkUtil
        NetworkUtil networkUtil = new NetworkUtil();
        String url = "/api/user/"; // 确保 URL 是正确的

        // 使用 NetworkUtil 发送异步 POST 请求
        networkUtil.postAsync(url, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(personalCenterActivity.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(personalCenterActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(personalCenterActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }



}