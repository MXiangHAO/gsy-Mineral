//package com.example.chapter_blog;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class NotificationActivity extends AppCompatActivity {
//    private RecyclerView infoRecyclerView;
//    private ActionAdapter actionAdapter;
//    private List<ActionItem> actionItems = new ArrayList<>(); // 初始化数据列表
//    private OkHttpClient client = new OkHttpClient();
//    private static final String TAG = "NotificationActivity"; // 日志标签
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification);
//
//        // 设置RecyclerView
//        infoRecyclerView = findViewById(R.id.infoRecyclerView);
//        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        actionAdapter = new ActionAdapter(actionItems);
//        infoRecyclerView.setAdapter(actionAdapter);
//
//        // 从后端获取数据
//        fetchAllActions();
//    }
//
//    // 获取所有动作信息
//    private void fetchAllActions() {
//        int bId = 17;
//        // 获取点赞信息
//        fetchDataFromBackend("http://10.0.2.2:8080/api/likes/"+bId, "给你点赞了");
//        // 获取收藏信息
//        fetchDataFromBackend("http://10.0.2.2:8080/api/collect/"+bId, "收藏了你的博客");
//        // 获取评论信息
//        fetchDataFromBackend("http://10.0.2.2:8080/api/comment/"+bId, "评论了你的博客");
//    }
//
//    // 从后端获取数据
//    private void fetchDataFromBackend(String url, String actionType) {
//        Log.d(TAG, "Fetching data from: " + url); // 输出日志
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 网络错误处理
//                e.printStackTrace();
//                Log.e(TAG, "Network error while fetching data", e); // 输出错误日志
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    Log.e(TAG, "onResponse: Unexpected code " + response);
//                    throw new IOException("Unexpected code " + response);
//                }
//
//                try {
//                    String responseData = response.body().string();
//                    JSONObject jsonResponse = new JSONObject(responseData);
//                    Log.d(TAG, "onResponse: Received data: " + jsonResponse.toString());
//
//                    // 检查code是否表示成功，然后从data字段获取数据数组
//                    if (jsonResponse.has("code") && jsonResponse.getInt("code") == 1 && jsonResponse.has("data")) {
//                        JSONArray array = jsonResponse.getJSONArray("data");
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            // 确保这里获取的是正确的用户ID字段
//                            int uId = object.getInt("uid");
//                            fetchUserDetails(uId, actionType);
//                        }
//                    } else {
//                        Log.e(TAG, "Unexpected JSON structure or error code.");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "Error parsing data from response", e);
//                }
//            }
//        });
//    }
//
//
//    // 根据用户ID获取用户详情并创建ActionItem
//    private void fetchUserDetails(int uId, String actionType) {
//        String url = "http://10.0.2.2:8080/api/user/details/" + uId;
//
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    Log.e(TAG, "Unexpected code " + response); // 输出错误日志
//                    throw new IOException("Unexpected code " + response);
//                }
//
//                try {
//                    JSONObject object = new JSONObject(response.body().string());
//                    String username = object.getString("username");
//                    String avatarBase64 = object.getString("avatar");
//
//                    // 创建ActionItem并更新UI
//                    ActionItem item = new ActionItem(username, actionType, avatarBase64);
//                    runOnUiThread(() -> {
//                        actionItems.add(item);
//                        actionAdapter.notifyDataSetChanged();
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "Error parsing user details", e);
//                }
//            }
//        });
//    }
//    // ActionItem类定义
//    public class ActionItem {
//        private String username; // 用户名
//        private String actionDescription; // 动作描述
//        private String avatarBase64; // 用户头像的Base64字符串
//
//        public ActionItem(String username, String actionDescription, String avatarBase64) {
//            this.username = username;
//            this.actionDescription = actionDescription;
//            this.avatarBase64 = avatarBase64;
//        }
//
//        // Getter 和 Setter 方法
//        public String getUsername() {
//            return username;
//        }
//
//        public String getActionDescription() {
//            return actionDescription;
//        }
//
//        public String getAvatarBase64() {
//            return avatarBase64;
//        }
//    }
//
//    // ActionAdapter内部类
//    public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {
//
//        private List<ActionItem> actionItems;
//
//        public ActionAdapter(List<ActionItem> actionItems) {
//            this.actionItems = actionItems;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_action_item, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            Log.d(TAG, "onBindViewHolder: Binding data at position: " + position); // 添加日志
//            ActionItem actionItem = actionItems.get(position);
//            holder.username.setText(actionItem.getUsername());
//            holder.actionDescription.setText(actionItem.getActionDescription());
//            if (actionItem.getAvatarBase64() != null && !actionItem.getAvatarBase64().isEmpty()) {
//                byte[] decodedString = Base64.decode(actionItem.getAvatarBase64(), Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                holder.userAvatar.setImageBitmap(decodedByte);
//            } else {
//                // 如果没有头像，可能需要设置一个默认头像
//                holder.userAvatar.setImageResource(R.drawable.touxiang);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return actionItems.size();
//        }
//
//        // ViewHolder内部类
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView username, actionDescription;
//            public ImageView userAvatar;
//
//            public ViewHolder(View view) {
//                super(view);
//                username = view.findViewById(R.id.username);
//                actionDescription = view.findViewById(R.id.actionDescription);
//                userAvatar = view.findViewById(R.id.userAvatar);
//            }
//        }
//    }
//}



package com.example.chapter_blog.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.util.NetworkUtil;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    // private ExecutorService executorService = Executors.newFixedThreadPool(10);  // 线程池
    private RecyclerView infoRecyclerView;
    private ActionAdapter actionAdapter;
    private List<ActionItem> actionItems = new ArrayList<>(); // 初始化数据列表
    private static final String TAG = "NotificationActivity"; // 日志标签
    private NetworkUtil networkUtil = new NetworkUtil(); // 使用NetworkUtil类
    private String blogTitle = ""; // 用于存储博客标题的字符串

    // 获取当前用户
    User currentUser = UserManager.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // 设置RecyclerView
        infoRecyclerView = findViewById(R.id.infoRecyclerView);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        actionAdapter = new ActionAdapter(actionItems);
        infoRecyclerView.setAdapter(actionAdapter);

        // 从后端获取数据
        try {
            fetchAllBIds();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Todo: 获取所有博客ID
    private void fetchAllBIds() {
        new Thread(() -> {
            try {
                int uId = currentUser.getId();  //获取用户的Id
                //int uId = 1;  // 假设用户ID为1
                String url = "/api/blog/" + uId;  // API端点
                String responseData = networkUtil.get(url);
                JSONObject jsonResponse = new JSONObject(responseData);
                Log.d(TAG,"jsonResponse"+jsonResponse);
                if (jsonResponse.getInt("code") == 1 && jsonResponse.has("data")) {
                    JSONArray blogsArray = jsonResponse.getJSONArray("data");
                    ArrayList<Integer> blogIds = new ArrayList<>();

                    for (int i = 0; i < blogsArray.length(); i++) {
                        JSONObject blogObject = blogsArray.getJSONObject(i);
                        int bId = blogObject.getInt("bid");
                        blogIds.add(bId);
                        // 添加日志输出
                        Log.d(TAG, "Fetched bId: " + bId);
                    }

                    // 处理每个bId
                    processEachBId(blogIds);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching blog IDs: ", e);
            }
        }).start();
    }

    //Todo:顺序处理每个博客的点赞，收藏，评论信息
    private void processEachBId(ArrayList<Integer> blogIds) {
        if (blogIds.isEmpty()) return;

        int bId = blogIds.remove(0); // 移除并返回列表的第一个元素

        fetchBlogTitle(bId, title -> {
            // 获取标题后进行相关操作
            fetchActionData("/api/likes/" + bId, "点赞了你的博客：" + title);
            fetchActionData("/api/collect/" + bId, "收藏了你的博客：" + title);
            fetchActionData("/api/comment/" + bId, "评论了你的博客：" + title);
            // 完成后处理下一个bId
            processEachBId(blogIds); // 递归调用处理下一个bId
        });
    }

    //Todo： 获取相关动作数据
    private void fetchActionData(String endpoint, String actionType) {
        new Thread(() -> {
            try {
                String responseData = networkUtil.get( endpoint);
                JSONObject jsonResponse = new JSONObject(responseData);
                if (jsonResponse.getInt("code") == 1 && jsonResponse.has("data")) {
                    JSONArray array = jsonResponse.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        int uId = object.getInt("uid");
                        fetchUserDetails(uId, actionType);
                    }
                } else {
                    Log.e(TAG, "Unexpected JSON structure or error code.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching data: ", e);
            }
        }).start();
    }
    // Todo：获取用户详情（用户名和用户头像）
    private void fetchUserDetails(int uId, String actionType) {
        new Thread(() -> {
            try {
                String url = "/api/user/details/" + uId;
                String responseData = networkUtil.get(url);
                JSONObject object = new JSONObject(responseData);
                String username = object.getString("username");
                String avatarBase64 = object.getString("avatar");
                ActionItem item = new ActionItem(username, actionType, avatarBase64);
                runOnUiThread(() -> {
                    actionItems.add(item);
                    actionAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user details: ", e);
            }
        }).start();
    }

    // Todo：博客标题回调接口（防止线程间的冲突）
    private interface BlogTitleCallback {
        void onTitleFetched(String title);
    }

    //Todo： 获取博客标题
    private void fetchBlogTitle(int bId, BlogTitleCallback callback) {
        new Thread(() -> {
            try {
                String url = "/api/blog/findtitle/" + bId;
                String responseData = networkUtil.get(url);
                JSONObject jsonResponse = new JSONObject(responseData);

                if (jsonResponse.getInt("code") == 1 && jsonResponse.has("data")) {
                    blogTitle = jsonResponse.getString("data");  // 获取并存储博客标题

                    Log.d(TAG, "Blog Title fetched: " + blogTitle);  // 输出获取到的博客标题

                    // 在UI线程上执行回调
                    runOnUiThread(() -> callback.onTitleFetched(blogTitle));
                } else {
                    Log.e(TAG, "Unexpected JSON structure or error code.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching blog title: ", e);
            }
        }).start();
    }

    //Todo： 动作项目类
    public class ActionItem {
        private String username;  //用户名
        private String actionDescription; //动作描述
        private String avatarBase64;//用户头像

        public ActionItem(String username, String actionDescription, String avatarBase64) {
            this.username = username;
            this.actionDescription = actionDescription;
            this.avatarBase64 = avatarBase64;
        }

        public String getUsername() { return username; }
        public String getActionDescription() { return actionDescription; }
        public String getAvatarBase64() { return avatarBase64; }
    }

    //Todo：RecyclerView的适配器
    public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {
        private List<ActionItem> actionItems;

        public ActionAdapter(List<ActionItem> actionItems) {
            this.actionItems = actionItems;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_action_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ActionItem actionItem = actionItems.get(position);
            holder.username.setText(actionItem.getUsername());
            holder.actionDescription.setText(actionItem.getActionDescription());
            if (actionItem.getAvatarBase64() != null && !actionItem.getAvatarBase64().isEmpty()) {
                byte[] decodedString = Base64.decode(actionItem.getAvatarBase64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.userAvatar.setImageBitmap(decodedByte);
            } else {
                holder.userAvatar.setImageResource(R.drawable.touxiang); // 默认头像
            }
        }

        @Override
        public int getItemCount() {
            return actionItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView username;
            public TextView actionDescription;
            public ImageView userAvatar;

            public ViewHolder(View view) {
                super(view);
                username = view.findViewById(R.id.username);
                actionDescription = view.findViewById(R.id.actionDescription);
                userAvatar = view.findViewById(R.id.userAvatar);
            }
        }
    }
}

