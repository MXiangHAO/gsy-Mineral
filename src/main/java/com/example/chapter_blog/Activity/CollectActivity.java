package com.example.chapter_blog.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.R;
import com.example.chapter_blog.pojo.CollectItem;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectActivity extends Activity {
    // 声明OkHttpClient对象用于网络请求
    private final OkHttpClient httpClient = new OkHttpClient();
    // 声明一些UI控件和数据列表
    private ImageView SearchButton;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        /*********绑定控件***********/
        mRecyclerView = findViewById(R.id.collect_list);

        // 设置RecyclerView的布局管理器和分割线
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //分割线
        DividerItemDecoration mDivider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(mDivider);

        User currentUser= UserManager.getInstance().getCurrentUser();
        int cur_uId=currentUser.getId();
        /********读取数据***********/
        GetData(cur_uId);
    }

    private void GetData(int id){
        new Thread(() -> {
            try {
                fetchUsercollectItemAll(id);
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    private void fetchUsercollectItemAll(int id){
        String url = "http://81.70.43.2:8100/api/CollectItem/user?"+"cur_uId="+id;

        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理错误情况
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String jsonData = response.body().string();
                List<CollectItem> result = parseSearchResult(jsonData);
                // 在UI线程更新RecyclerView
                runOnUiThread(() -> {
                    // 创建适配器并设置给RecyclerView
                    CollectListAdapter adapter = new CollectListAdapter(result);
                    mRecyclerView.setAdapter(adapter);
                });
            }
        });
    }
    private List<CollectItem> parseSearchResult(String jsonData) {
        List<CollectItem> searchResult = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonData);
            JSONArray dataArray = jsonResponse.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                // 解析数据并创建 BlogItem 对象
                int cid = jsonObject.getInt("cid");
                int collectCount = jsonObject.getInt("collectCount");
                String collectName=jsonObject.getString("collectName");
                Log.d("CollectCountLog", "Parsed Collect Count: " + collectCount);

                CollectItem collectItem = new CollectItem(cid,collectName,collectCount);
                searchResult.add(collectItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchResult;
    }
    public class CollectListAdapter extends RecyclerView.Adapter<CollectListAdapter.ViewHolder> {
        private List<CollectItem> mData;

        // 构造方法，接收数据列表
        public CollectListAdapter(List<CollectItem> data) {
            this.mData = data;
        }

        // 创建ViewHolder
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item, parent, false);
            return new ViewHolder(view);
        }

        // 绑定ViewHolder，设置数据
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CollectItem collectItem = mData.get(position);
            // 在这里设置ViewHolder中的控件数据，例如：
            holder.collectName.setText(collectItem.getCollectName());
            int count = collectItem.getCollectCount();
            String displayText = count + "个博客";
            holder.collectCount.setText(displayText);

            // 设置点击事件
            holder.itemView.setOnClickListener(view -> {
                // 创建一个 Intent  跳转这里要改一下
                Intent intent = new Intent(view.getContext(), BoardActivity.class);
                // 将博客信息放入 Intent 中
                intent.putExtra("cid", collectItem.getcId());
                // 启动新的 Activity，并传递 Intent
                view.getContext().startActivity(intent);
            });
        }

        // 获取数据列表的大小
        @Override
        public int getItemCount() {
            return mData.size();
        }

        // ViewHolder类，用于保存每个列表项的控件引用
        public  class ViewHolder extends RecyclerView.ViewHolder {
            // 在这里声明你的控件，例如：
            TextView collectName;
            TextView collectCount;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // 在这里初始化控件，例如：
                collectName = itemView.findViewById(R.id.collectName);
                collectCount = itemView.findViewById(R.id.collectCount);
            }
        }
    }
}
