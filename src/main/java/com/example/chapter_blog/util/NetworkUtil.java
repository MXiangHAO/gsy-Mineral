package com.example.chapter_blog.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NetworkUtil {

    // OkHttp客户端
    private OkHttpClient client = new OkHttpClient();
    String base_url="http://81.70.43.2:8100";
    // 使用OkHttp发送GET请求
    public String get(String url) throws Exception {
        url=base_url+url;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // 使用OkHttp发送POST请求
    public String post(String url, String json) throws Exception {
        url=base_url+url;
        // 设置媒体类型为JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        // 创建请求体
        RequestBody body = RequestBody.create(json, JSON);

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public void postAsync(String url, String json, Callback callback) {
        url = base_url + url;
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    // 使用OkHttp发送DELETE请求
    public String delete(String url) throws Exception {
        url=base_url+url;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

