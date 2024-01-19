package com.example.chapter_blog.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.Adapter.BlogAdapter;
import com.example.chapter_blog.Adapter.BlogManager;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Blog;
import com.example.chapter_blog.pojo.BlogItem;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserInfo;
import com.example.chapter_blog.pojo.UserManager;
import com.example.chapter_blog.util.NetworkUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class  firstUserInfoActivity extends Activity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final Handler handler = new Handler();
    private RecyclerView blogRecyclerView;
    private NetworkUtil networkUtil = new NetworkUtil();
    private static final String TAG = "firstUserInfoActivity";

    // 声明展示资料文本框
    private TextView displayInfoTextView;

    private int uId;


    //获取当前博客id
    Blog currentBlog = BlogManager.getInstance().getCurrentBlog();
    //  int bId = currentBlog.getBId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_user_info);
        // 初始化文本框
        displayInfoTextView = findViewById(R.id.displayInfoTextView);
        uId= UserManager.getInstance().getCurrentUser().getId();

        // 假设要获取 ID 为 1 的用户名(具体需要获取当前登录用户的信息来得到这个id)
        fetchUserName(uId);

        //假设要获取 ID 为 1 的用户头像(具体需要获取当前登录用户的信息来得到这个id)
        fetchUserImage(uId);


        // 初始化RecyclerView
        initBlogRecyclerView();

        // 从后端API加载博客数据
        fetchUserBlogs(uId);

        // Todo:获取编辑资料按钮
        ImageButton editProfileButton = findViewById(R.id.editProfileButton);
        // Todo:设置点击监听器,编辑用户资料
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建跳转到 userInfoActivity 的意图
                Intent intent = new Intent(firstUserInfoActivity.this, userInfoActivity.class);
                //  附加额外的用户ID数据
                intent.putExtra("uId", uId);
                startActivity(intent); // 启动 userInfoActivity
                finish();
            }
        });


        /*******底部导航栏*********/
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);//定位底部导航栏
        bottomNavigationView.setSelectedItemId(R.id.mine);//默认选择主页
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

        // 获取关注按钮控件
        ImageButton followButton = findViewById(R.id.followButton);

        // 设置点击事件监听器
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到FollowActivity的意图
              Intent intent = new Intent(firstUserInfoActivity.this, FollowActivity.class);
               //  附加额外的用户ID数据
                intent.putExtra("uId", uId);
               //  启动FollowActivity
                startActivity(intent);
            }
        });

        // 获取收藏按钮
        ImageButton favoritesButton = findViewById(R.id.favoriteButton);

        // 设置收藏按钮的点击监听器
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到collect的 Intent
                Intent intent = new Intent(firstUserInfoActivity.this, CollectActivity.class);


                // 启动 FavoritesActivity
                startActivity(intent);
            }
        });


    }
    //Todo：呈现当前用户名
    private void fetchUserName(Integer id) {
        new Thread(() -> {
            try {
                String response = networkUtil.get("/api/user/" + id);
                JSONObject jsonResponse = new JSONObject(response);
                handler.post(() -> updateUI(jsonResponse.toString())); // Assume updateUI takes the response JSON as a string
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user name: ", e);
            }
        }).start();
    }


    //Todo:呈现用户信息表中的字段（此处为用户头像和职业）
    private void fetchUserImage(Integer id) {
        new Thread(() -> {
            try {
                String response = networkUtil.get("/api/userprofile/" + id);
                runOnUiThread(() -> updateImageUI(response)); // Update UI on main thread
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user image: ", e);
            }
        }).start();
    }

    //Todo：更新UI信息（头像和个人资料展示)，在函数fetchUserImage中调用
    private void updateImageUI(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            UserInfo user = objectMapper.readValue(jsonData, UserInfo.class);
            // 进行更改
            // 更新头像
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                byte[] decodedString = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ((ImageView) findViewById(R.id.userImageView)).setImageBitmap(decodedByte);
            }
            // 更新展示资料文本框 - 假设展示职业
            if (user.getOccupation() != null && !user.getOccupation().isEmpty()) {
                ((TextView) findViewById(R.id.displayInfoTextView)).setText(user.getOccupation());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Todo：更新UI信息(用户名)，在函数fetchUserName中调用
    private void updateUI(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        try {
            User user1 = objectMapper.readValue(jsonData, User.class);

            // 进行更改
            ((TextView) findViewById(R.id.usernameEditText)).setText(user1.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Todo： 更新当前展示的博客,在fetchUserBlogs中调用
    private void updateBlogListUI(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Response1 response = objectMapper.readValue(jsonData, Response1.class);
            List<Blog> blogList = response.getData();
            BlogAdapter adapter = new BlogAdapter(blogList, this::deleteBlog,this::navigateToBlogDetail,true);
            blogRecyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Todo：初始化并设置RecyclerView，用于滚动查看博客
    private void initBlogRecyclerView() {
        blogRecyclerView = findViewById(R.id.blogRecyclerView);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        blogRecyclerView.setAdapter(new BlogAdapter(new ArrayList<>(), this::deleteBlog,this::navigateToBlogDetail,true));
    }

    // Todo：获取并处理博客数据
    private void fetchUserBlogs(Integer uid) {
        new Thread(() -> {
            try {
                String response = networkUtil.get("/api/blog/" + uid);
                handler.post(() -> updateBlogListUI(response));
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user blogs: ", e);
            }
        }).start();
    }


     //Todo：用于配合updateBlogListUI函数使用，辅助更新博客内容
    public static class Response1 {
        private int code;
        private String msg;
        private List<Blog> data;

        @JsonCreator
        public Response1(@JsonProperty("code") int code,
                         @JsonProperty("msg") String msg,
                         @JsonProperty("data") List<Blog> data) {
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
        public Response1() {
        }
        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public List<Blog> getData() {
            return data;
        }
    }

    // Todo：根据博客id删除博客
    private void deleteBlog(int bId) {
        new Thread(() -> {
            try {
                String response = networkUtil.delete("/api/blog/deleteblog/" + bId);
                fetchUserBlogs(uId);
                Log.e(TAG, "success deleting blog");
            } catch (Exception e) {
                Log.e(TAG, "Error deleting blog: ", e);
            }
        }).start();
    }

    //Todo：跳转到博客正文
    private void navigateToBlogDetail(int bId) {
        // 打印博客ID
        Log.d(TAG, "Navigating to blog detail for blog ID: " + bId);
        // 创建一个Intent来启动
        Intent intent1 = new Intent(firstUserInfoActivity.this, commentActivity.class);
        intent1.putExtra("bId",bId);
        startActivity(intent1);
        fetchBlogItem(bId, new otherUserInfoActivity.BlogItemCallback() {
            @Override
            public void onBlogItemFetched(BlogItem blogItem) {
                if (blogItem != null) {
                    //  intent1.putExtra("avatar", blogItem.getAvatar());
                    //   intent1.putExtra("bId", blogItem.getbId());
                    //   intent1.putExtra("uId", blogItem.getuId());
//            intent1.putExtra("url", blogItem.getUrls());
//            // intent1.putExtra("fileName", blogItem.getFileName());
//            intent1.putExtra("title", blogItem.getTitle());
//            intent1.putExtra("content", blogItem.getContent());
//            intent1.putExtra("date", blogItem.getDate());
//            intent1.putExtra("fullname", blogItem.getFullName());
                    Log.d("MyTag", "blogItem is not null!"+blogItem.getDate());
                }
            }
        });
    }

    //Todo:请求获得一个完整的BlogItem
    public void fetchBlogItem(int bId, otherUserInfoActivity.BlogItemCallback callback) {
        String url = "http://81.70.43.2:8100/api/BlogItem/user?bId=" + bId;
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseData = response.body().string();
                    BlogItem blogItem = parseSearchResultForSingleItem(responseData);
                    if (callback != null) {
                        callback.onBlogItemFetched(blogItem);
                    }
                }
            }
        });
    }


    // Todo:解析JSON数据并创建一个BlogItem对象
    private BlogItem parseSearchResultForSingleItem(String jsonData) {
        try {
            JSONObject jsonResponse = new JSONObject(jsonData);
            int code = jsonResponse.getInt("code");

            if (code == 1) {
                JSONObject dataObject = jsonResponse.getJSONObject("data");

                int bId = dataObject.getInt("bid");
                Log.d("MyTag", "Parsed bId: " + bId); // 添加这一行输出

                int uId = dataObject.getInt("uid");

                Log.d("MyTag", "Parsed uId: " + uId); // 添加这一行输出
                String urls = dataObject.isNull("urls") ? null : dataObject.getString("urls");
                Log.d("MyTag", "Parsed urls: " + urls); // 添加这一行输出
                String title = dataObject.getString("title");
                Log.d("MyTag", "Parsed title: " + title); // 添加这一行输出
                String date = dataObject.getString("date");
                Log.d("MyTag", "Parsed date: " + date); // 添加这一行输出
                // 如果需要，解析嵌套的 "content" 字符串
                String content = dataObject.getString("content");
                // 根据需要将 contentJsonString 解析为 JSON 对象
                Log.d("MyTag", "Parsed content: " + content); // 添加这一行输出
                String fullName = dataObject.getString("fullName");
                Log.d("MyTag", "Parsed fullname: " + fullName); // 添加这一行输出
                String avatar = dataObject.getString("avatar");
                //    Log.d("MyTag", "Parsed avatar: " + avatar); // 添加这一行输出
                int clicks = dataObject.getInt("clicks");
                Log.d("MyTag", "Parsed clicks: " + clicks); // 添加这一行输出
                int likes = dataObject.getInt("likes");

                int collects = dataObject.getInt("collects");
                Log.d("MyTag", "Parsed likes collects: " + likes+collects); // 添加这一行输出
                BlogItem BI= new BlogItem(bId, uId, urls, title, content, date, fullName, avatar, clicks, likes, collects);
                return BI;

            } else {
                Log.e("MyTag", "服务器返回错误: " + jsonResponse.getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MyTag", "解析 JSON 时出错: " + e.getMessage());
        }
        return null;
    }

    public interface BlogItemCallback {
        void onBlogItemFetched(BlogItem blogItem);
    }

}