package com.example.chapter_blog.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.Adapter.MessageAdapter;
import com.example.chapter_blog.MessageService;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.MessageItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class message extends Activity implements View.OnClickListener {

    private MessageAdapter messageAdapter;
    private List<MessageItem> messageList;

    private ImageView btnLike,btnFollow,btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // 初始化 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.blog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLike=findViewById(R.id.btn_like);
        btnFollow=findViewById(R.id.btn_follow);
        btnMessage=findViewById(R.id.btn_message);
        
        btnLike.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        

        // 创建示例数据

        try {
            messageList = MessageService.getMessageItem("/message/findMessageItem/1");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 初始化 RecyclerView 的适配器
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setAdapter(messageAdapter);

        // 模拟刷新数据，实际中可能是从服务器获取最新消息
        try {
            refreshData();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.botton_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.message);//默认选择主页
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.library) {
                    startActivity(new Intent(getApplicationContext(), TargetActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.message) {
                    startActivity(new Intent(getApplicationContext(), message.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId==R.id.add) {
                    startActivity(new Intent(getApplicationContext(), postBlog.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.mine) {
                    startActivity(new Intent(getApplicationContext(), firstUserInfoActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshData() throws ExecutionException, InterruptedException {
        // 模拟从服务器获取最新数据
        // 在实际应用中，你可能需要在这里调用网络请求来获取最新消息数据

        // 模拟获取到的最新数据
        List<MessageItem> updatedData = createUpdatedData();

        // 更新 Adapter 中的数据集
        messageAdapter.updateData(updatedData);

        // 刷新 RecyclerView
        messageAdapter.notifyDataSetChanged();
    }

    private List<MessageItem> createUpdatedData() throws ExecutionException, InterruptedException {
        // 模拟从服务器获取的最新消息数据
        List<MessageItem> updatedData = MessageService.getMessageItem("/message/findMessageItem/1");
        // ... （根据实际需求获取最新数据）
        return updatedData;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_message){
            Intent intent = new Intent(message.this , NotificationActivity.class);
            startActivity(intent);
        } else if (id==R.id.btn_like) {
            
        } else if (id==R.id.btn_follow) {

        }
    }
}
