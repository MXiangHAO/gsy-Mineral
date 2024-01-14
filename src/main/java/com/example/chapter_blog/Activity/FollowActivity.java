package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.Adapter.FollowAdapter;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.UserDetailAdduId;
import com.example.chapter_blog.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends Activity implements FollowAdapter.OnUserClickListener {
    private RecyclerView followingRecyclerView;
    private FollowAdapter followAdapter;
    private List<UserDetailAdduId> followList = new ArrayList<>(); // 初始化数据列表
    private NetworkUtil networkUtil = new NetworkUtil(); // 使用NetworkUtil类
    private static final String TAG = "FollowActivity"; // 日志标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        //Todo: 获取从其他Activity传来的Intent
        Intent intent = getIntent();
        // 读取附加的用户ID数据，默认值为-1（或您选择的任何适当默认值）
        int uId = intent.getIntExtra("uId", -1); // 注意默认值为-1

        // 输出获取的uId，用于调试
        Log.d(TAG, "Received uId: " + uId);
        // 设置RecyclerView
        followingRecyclerView = findViewById(R.id.followingRecyclerView);
        followingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        followAdapter = new FollowAdapter(followList,this);
        followingRecyclerView.setAdapter(followAdapter);

        // 从后端获取数据
        fetchAllFollows(uId);
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

    // Todo:获取所有关注信息
    private void fetchAllFollows(int uId) {
        new Thread(() -> {
            try {
               // int uId = 1; // 假设用户ID为1

                String url = "/api/follow/" + uId; // API端点
                String responseData = networkUtil.get(url);
                JSONObject jsonResponse = new JSONObject(responseData);

                if (jsonResponse.getInt("code") == 1 && jsonResponse.has("data")) {
                    JSONArray followArray = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < followArray.length(); i++) {
                        JSONObject followObject = followArray.getJSONObject(i);
                        int fUId = followObject.getInt("fuid");
                        fetchUserDetails(fUId);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching follow list: ", e);
            }
        }).start();
    }

    // Todo:根据用户ID获取用户详情
    private void fetchUserDetails(int userId) {
        new Thread(() -> {
            try {
                String url = "/api/user/details/" + userId;
                String responseData = networkUtil.get(url);
                JSONObject userObject = new JSONObject(responseData);
                String username = userObject.getString("username");
                String avatar = userObject.getString("avatar");
                UserDetailAdduId userDetailAdduId = new UserDetailAdduId(username, avatar,userId);
                // 添加日志输出
                //Log.d(TAG, "Fetched user details for userId " + userId + ": " + userDetailAdduId.toString());
                runOnUiThread(() -> {
                    followList.add(userDetailAdduId);
                    followAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user details: ", e);
            }
        }).start();
    }

    @Override
    public void onUserClick(int position) {
        // 获取被点击用户的UserDetailAdduId对象
        UserDetailAdduId clickedUser = followList.get(position);
        // 从对象中获取userId
        int userId = clickedUser.getUid();
        Intent intent = new Intent(FollowActivity.this, otherUserInfoActivity.class);
        intent.putExtra("uId", userId);
        startActivity(intent);
    }
}

