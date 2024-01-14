package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chapter_blog.R;
import com.example.chapter_blog.util.Client;
import com.example.chapter_blog.util.validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class login extends Activity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private Button forgetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        forgetPasswordButton = findViewById(R.id.forget);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPasswordButton.setOnClickListener(this);

    }
    // 验证邮箱的规则


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login) {// 获取输入的用户名和密码
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // 检查用户名和密码是否为空
            if (username.isEmpty() || password.isEmpty()) {
                validation.showAlertDialog(this,"提示", "用户名和密码不能为空");
                return;
            }

            // 检查手机号和邮箱格式是否正确
            if (!validation.isValidEmailOrPhone(username)) {
                validation.showAlertDialog(this,"提示", "请输入正确的手机号或邮箱");
                return;
            }

            // TODO: 发送登录请求到后端
            // 可以使用 Retrofit、Volley、OkHttp 等网络请求库
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", username);
                jsonBody.put("password", password);
                jsonBody.put("type", validation.isValidEmail(username) ? "email" : "phone");

                // TODO: 将 jsonBody 作为请求的一部分发送到后端
                // 使用 Retrofit、Volley、OkHttp 等网络请求库
                boolean result = Client.initPost("/init/login", String.valueOf(jsonBody)).get();
                if(result){
                    // 示例：Toast 提示登录成功
                    Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    // 在 UI 线程中启动新活动
                    runOnUiThread(() -> {
                        Intent intent = new Intent(login.this, BoardActivity.class);
                        startActivity(intent);
                    });
                }else {
                    Toast.makeText(login.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (id == R.id.register) {// 跳转至注册页面
            Intent registerIntent = new Intent(login.this, register.class);
            startActivity(registerIntent);
        } else if (id == R.id.forget) {// 跳转至找回密码页面
            Intent forgetIntent = new Intent(login.this, login_forget.class);
            startActivity(forgetIntent);
        }
    }
}

