package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chapter_blog.R;

public class start extends Activity {

    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 使用Handler延时跳转到登录界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 启动登录界面
                Intent intent = new Intent(start.this, login.class);
                startActivity(intent);
                finish(); // 关闭当前Activity，避免用户返回到启动封面
            }
        }, SPLASH_DELAY);
    }
}