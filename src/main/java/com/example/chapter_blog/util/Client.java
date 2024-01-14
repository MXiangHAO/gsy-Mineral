package com.example.chapter_blog.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import com.example.chapter_blog.pojo.Message;
import com.example.chapter_blog.pojo.MessageItem;
import com.example.chapter_blog.pojo.User;
import com.example.chapter_blog.pojo.UserManager;
import com.example.chapter_blog.pojo.VCodeManger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Client {

    private static final String BASE_URL = "http://192.168.43.41:8080"; // 修改为你的实际服务器地址

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    public static CompletableFuture<Boolean> initPost(String endpoint, String jsonBody) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        String url = BASE_URL + endpoint;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            private boolean parseStatus(String result) {
                return result.contains("\"status\":\"true\"");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(result);

                String action=jsonNode.get("action").asText();
                if("login".equals(action)) {
                    int id = jsonNode.get("id").asInt();
                    String username = jsonNode.get("username").asText();
                    String password = jsonNode.get("password").asText();
                    String phone =jsonNode.get("phone").asText();
                    String email = jsonNode.get("email").asText();
                    // 使用获取的属性值构造 User 对象
                    User user = new User(id, username, password, phone, email);
                    UserManager.getInstance().setCurrentUser(user);
                }else if("sendCode".equals(action)){
                    VCodeManger.getInstance().setCurrentCode(jsonNode.get("code").asText());
                }
                System.out.println(result);
                if (response.isSuccessful()) {
                    boolean success = parseStatus(result);
                    future.complete(success);
                } else {
                    future.complete(false);
                }
            }
        });

        return future;
    }

    public static CompletableFuture<Boolean> sendPost(String endpoint, String jsonBody) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        String url = BASE_URL + endpoint;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            private boolean parseStatus(String result) {
                return result.contains("\"status\":\"true\"");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                /*JsonNode jsonNode = objectMapper.readTree(result);
//                JsonNode userNode = jsonNode.get("user");
//                // 从 JsonNode 中获取属性值
//                String idStr = userNode.get("id").asText();
//                int id = Integer.parseInt(idStr); // 转换为整数
                System.out.println(result);*/
                if (response.isSuccessful()) {
                    boolean success = parseStatus(result);
                    future.complete(success);
                } else {
                    future.complete(false);
                }
            }
        });

        return future;
    }
    public static CompletableFuture<JsonNode> GetMapping(String endpoint) {
        CompletableFuture<JsonNode> future = new CompletableFuture<>();

        String url = BASE_URL + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()  // 将此处修改为get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(result);

                System.out.println(result);
                if (response.isSuccessful()) {
                    future.complete(jsonNode);
                } else {
                    future.completeExceptionally(new RuntimeException("Request failed with status code: " + response.code()));
                }
            }
        });

        return future;
    }
    public static CompletableFuture<String> GetUsername(String endpoint) {
        CompletableFuture<String> future = new CompletableFuture<>();

        String url = BASE_URL + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()  // 将此处修改为get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    future.complete(result);
                } else {
                    future.completeExceptionally(new RuntimeException("Request failed with status code: " + response.code()));
                }
            }
        });

        return future;
    }
    public static CompletableFuture<Boolean> judgeIs(String endpoint) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        String url = BASE_URL + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String result = response.body().string();

                if (response.isSuccessful()) {
                    // 处理响应体为 "true" 或 "false" 的情况
                    boolean isSuccess = Boolean.parseBoolean(result.trim());
                    future.complete(isSuccess);
                } else {
                    future.completeExceptionally(new RuntimeException("Request failed with status code: " + response.code()));
                }
            }
        });

        return future;
    }


    public static List<Message> parseJsonToMessages(JsonNode jsonNode) {
        List<Message> messages = new ArrayList<>();

        if (jsonNode.isArray()) {
            for (JsonNode messageNode : jsonNode) {
                int friendId = messageNode.get("friendId").asInt();
                int ownId = messageNode.get("ownId").asInt();
                String content = messageNode.get("content").asText();
                String timestamp = messageNode.get("timestamp").asText();
                int send = messageNode.get("send").asInt();
                boolean isRead = messageNode.get("read").asBoolean();

                Message message = new Message(friendId, ownId, content, timestamp, send, isRead);
                messages.add(message);
            }
        }

        return messages;
    }

    public static List<MessageItem> parseJsonToMessagesItem(JsonNode jsonNode) {
        List<MessageItem> messageItemList = new ArrayList<>();

        if (jsonNode.isArray()) {
            for (JsonNode messageNode : jsonNode) {
                Bitmap userAvatar = decodeBase64ToBitmap(messageNode.get("userAvatar").asText()); // 解析 userAvatar，这里需要根据实际 JSON 结构进行处理
                String username = messageNode.get("username").asText();
                int senderId = messageNode.get("senderId").asInt();
                int receiverId = messageNode.get("receiverId").asInt();
                String timestamp = messageNode.get("timestamp").asText();
                String recentMessage = messageNode.get("recentMessage").asText();

                MessageItem messageItem = new MessageItem(userAvatar, username, senderId, receiverId, timestamp, recentMessage);
                messageItemList.add(messageItem);
            }
        }
        return messageItemList;
    }
    public static Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
