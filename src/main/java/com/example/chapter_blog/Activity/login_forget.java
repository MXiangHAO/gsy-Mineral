package com.example.chapter_blog.Activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.VCodeManger;
import com.example.chapter_blog.util.Client;
import com.example.chapter_blog.util.validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class login_forget extends Activity implements View.OnClickListener {

    private EditText usernameEditText;
    private EditText verificationCodeEditText;
    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private Button getVerificationCodeButton;
    private Button resetPasswordButton;
    private ImageView backButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);

        // 初始化视图
        usernameEditText = findViewById(R.id.username);
        verificationCodeEditText = findViewById(R.id.tx_verificationCode);
        newPasswordEditText = findViewById(R.id.newPassword);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPassword);
        getVerificationCodeButton = findViewById(R.id.btn_verificationCode);
        resetPasswordButton = findViewById(R.id.confirm);
        backButton = findViewById(R.id.back);

        // 设置点击事件
        getVerificationCodeButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_verificationCode) {
            // TODO: 实现获取验证码的逻辑
            // 可以通过后端接口获取验证码
            // 示例：Toast 提示验证码已发送
            String username=usernameEditText.getText().toString();
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("username", username);
                jsonBody.put("type", validation.isValidEmail(username) ? "email" : "phone");
                boolean result = Client.initPost("/init/sendCode", String.valueOf(jsonBody)).get();
                if(result){
                    // 示例：Toast 提示发送消息是否成功
                    Toast.makeText(login_forget.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    startCountDownTimer();
                }else {
                    Toast.makeText(login_forget.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (id == R.id.confirm) {
            // 获取输入的用户名、验证码和新密码
            String username = usernameEditText.getText().toString().trim();
            String verificationCode = verificationCodeEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString().trim();

            // 检查用户名、验证码和密码是否为空
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(verificationCode)
                    || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
                validation.showAlertDialog(this,"提示", "用户名、验证码和密码不能为空");
                return;
            }

            // 检查手机号和邮箱格式是否正确
            if (!validation.isValidEmailOrPhone(username)) {
                validation.showAlertDialog(this,"提示", "请输入正确的手机号或邮箱");
                return;
            }

            // 检查新密码和确认密码是否一致
            if (!TextUtils.equals(newPassword, confirmNewPassword)) {
                validation.showAlertDialog(this,"提示", "新密码和确认密码不一致");
                return;
            }

            if(!verificationCodeEditText.getText().toString().equals(VCodeManger.getInstance().getCurrentCode())){
                validation.showAlertDialog(this,"提示","验证码错误请重新输入");
            }

            // TODO: 实现重置密码的逻辑
            // 可以通过后端接口实现密码重置
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", username);
                jsonBody.put("password", newPassword);
                jsonBody.put("type", validation.isValidEmail(username) ? "email" : "phone");

                // TODO: 将 jsonBody 作为请求的一部分发送到后端
                // 使用 Retrofit、Volley、OkHttp 等网络请求库
                boolean result = Client.initPost("/init/login_forget", String.valueOf(jsonBody)).get();
                if(result){
                    // 示例：Toast 提示登录成功
                    Toast.makeText(login_forget.this, "修改成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(login_forget.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if (id == R.id.back) {
            // 返回按钮的点击事件，可以根据需求进行处理
            finish(); // 关闭当前找回密码页面
        }
    }

    private void startCountDownTimer() {
        getVerificationCodeButton.setEnabled(false);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 在倒计时过程中更新按钮上的文本
                getVerificationCodeButton.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // 倒计时结束后重新启用按钮
                getVerificationCodeButton.setEnabled(true);
                getVerificationCodeButton.setText("获取验证码");
            }
        };

        // 启动计时器
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时取消计时器，以防内存泄漏
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
