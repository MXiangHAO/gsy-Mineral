package com.example.chapter_blog.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter_blog.MainActivity;
import com.example.chapter_blog.R;
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

public class TargetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FileListAdapter fileListAdapter;
    private List<com.example.chapter_blog.Activity.FileInfo> fileList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_target);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileListAdapter = new FileListAdapter(); // 自定义适配器，稍后创建
        recyclerView.setAdapter(fileListAdapter);

        // 添加按钮点击事件
        ImageView addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建意图以启动 MainActivity
                Intent intent = new Intent(TargetActivity.this, MainActivity.class);
                // 添加标记以清除活动任务栈
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        // 获取文件名并更新RecyclerView
        fetchFileNames();

    }

    private void fetchFileNames() {
        // 使用OkHttp请求获取文件名列表
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://81.70.43.2:8300/files/file?uId="+ UserManager.getInstance().getCurrentUser().getId())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                         fileList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject fileObject = jsonArray.getJSONObject(i);
                            String fileName = fileObject.getString("filename");
                            String fileUrl = fileObject.getString("url");
                            fileList.add(new FileInfo(fileName, fileUrl));

                            // 添加日志以记录获取的文件信息
                            Log.d("FileList", "File Name: " + fileName + ", URL: " + fileUrl);
                        }

                        // 更新RecyclerView数据
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fileListAdapter.setFileInfos(fileList);
                                fileListAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 处理请求失败的情况
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TargetActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TargetActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
