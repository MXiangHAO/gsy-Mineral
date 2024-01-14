package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.Adapter.CommentAdapter;
import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.Comment;
import com.example.chapter_blog.pojo.UserDetail;
import com.example.chapter_blog.pojo.UserManager;
import com.example.chapter_blog.util.NetworkUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;


public class commentActivity extends Activity {
    private NetworkUtil networkUtil = new NetworkUtil();
    private RecyclerView commentRecyclerView;
    private EditText commentInput;
    private ImageButton sendButton;
    private CommentAdapter commentAdapter;
    private final OkHttpClient client = new OkHttpClient();
    private List<Comment> commentList = new ArrayList<>();
    private static final String TAG = "commentActivity";
    // 获取当前用户

    private  int uId,bId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        commentInput = findViewById(R.id.commentInput);
        sendButton = findViewById(R.id.sendButton);

        // 设置RecyclerView
        commentAdapter = new CommentAdapter(commentList);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 获取返回按钮
        ImageButton backbutton = findViewById(R.id.backButton);

        // 设置点击监听器
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


         Intent intent = getIntent();
         bId = intent.getIntExtra("bId", 1); // 注意默认值
         uId= UserManager.getInstance().getCurrentUser().getId();

        loadComments(bId); // 加载评论数据

        //Todo:设置发表评论的点击事件
       sendButton.setOnClickListener(v -> {
           postComment(uId,bId);
       });
    }

    // Todo:加载评论数据
    private void loadComments(int bId) {
        new Thread(() -> {
            String url = "/api/comment/" + bId;
            try {
                String responseData = networkUtil.get(url);
                Log.d(TAG, "onResponse - Load Comments: " + responseData);
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(dateFormat);
                ApiResponse apiResponse = objectMapper.readValue(responseData, ApiResponse.class);

                List<Comment> comments = apiResponse.getData();
                runOnUiThread(() -> {
                    commentList.clear();
                    if (comments != null) {
                        commentList.addAll(comments);
                        commentAdapter.notifyDataSetChanged();
                        TextView commentCountTextView = findViewById(R.id.commentCount);
                        commentCountTextView.setText(String.valueOf(comments.size()));
                        for (Comment comment : comments) {
                            loadUserDetails(comment);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(commentActivity.this, "获取评论失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    //Todo:载入用户名和头像
    private void loadUserDetails(Comment comment) {
        new Thread(() -> {
            String url = "/api/user/details/" + comment.getuId();
            try {
                String responseData = networkUtil.get(url);
                ObjectMapper objectMapper = new ObjectMapper();
                UserDetail userDetail = objectMapper.readValue(responseData, UserDetail.class);
                runOnUiThread(() -> {
                    comment.setUsername(userDetail.getUsername());
                    comment.setAvatar(userDetail.getAvatar());
                    commentAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e("loadUserDetails", "请求用户详情失败", e);
            }
        }).start();
    }

    //Todo:定义一个响应类,用于加载评论数据
    public static class ApiResponse {
        private int code;
        private String msg;
        private List<Comment> data;

        // getter和setter方法
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<Comment> getData() {
            return data;
        }

        public void setData(List<Comment> data) {
            this.data = data;
        }
    }

    //Todo: 发表新评论
    private void postComment(int uId,int bId) {
        new Thread(() -> {
            String commentText = commentInput.getText().toString();
            Comment newComment = new Comment();
            newComment.setContent(commentText);
            newComment.setuId(uId);
            newComment.setbId(bId);
            newComment.setDate(new Date());
            // 使用Android Log输出日志
            Log.d("Comment Data", newComment.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(dateFormat);
            try {
                String json = objectMapper.writeValueAsString(newComment);
                String url = "/api/comment/";
                String response = networkUtil.post(url, json);
                runOnUiThread(() -> {
                    Toast.makeText(commentActivity.this, "评论提交成功", Toast.LENGTH_SHORT).show();
                    commentInput.setText("");
                    loadComments(bId);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(commentActivity.this, "评论提交失败", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

//    // 方法来加载用户详细信息并保存行为
//    private void loadUser(Comment comment, int userId) {
//        // 假设这是获取用户详细信息的URL
//        String url = "http://10.0.2.2:8080/api/user/details/" + userId;
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                // 处理失败
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    // 处理错误
//                } else {
//                    String responseData = response.body().string();
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    UserDetail userDetail = objectMapper.readValue(responseData, UserDetail.class);
//                    // 现在用实际的用户详情保存评论行为
//                    saveActionInfo("comments", userDetail.getUsername(), "给你的博客评论了", userDetail.getAvatar());
//                }
//            }
//        });
//    }
//    private void saveActionInfo(String actionType, String username, String action, String avatar) {
//        SharedPreferences sharedPref = getSharedPreferences("AppData", Context.MODE_PRIVATE);
//        JSONArray actionArray = getActionArray(sharedPref, actionType);
//
//        try {
//            JSONObject newAction = new JSONObject();
//            newAction.put("username", username);
//            newAction.put("action", action);
//            newAction.put("avatar", avatar);
//
//            actionArray.put(newAction);
//
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(actionType, actionArray.toString());
//            editor.apply();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    private JSONArray getActionArray(SharedPreferences sharedPref, String actionType) {
//        String actionData = sharedPref.getString(actionType, "[]");
//        try {
//            return new JSONArray(actionData);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return new JSONArray();
//        }
//    }
}