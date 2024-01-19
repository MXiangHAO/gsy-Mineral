package com.example.chapter_blog.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.chapter_blog.pojo.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatRecordManager {

    private static final String PREFERENCES_NAME_PREFIX = "chat_records_";
    private static final String LAST_ACCESS_TIME_PREFIX = "last_access_time_";

    public static void saveChatRecord(Context context, int friendId, int ownId, String content, String timestamp, int send, boolean isRead) {
        if (context == null || TextUtils.isEmpty(content) || TextUtils.isEmpty(timestamp)) {
            return;
        }

        // 获取对应 friendId 的 SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_PREFIX + friendId, Context.MODE_PRIVATE);

        // 获取已有的聊天记录
        List<String> chatRecords = getChatRecords(preferences);

        // 添加新的聊天记录
        chatRecords.add(ownId + "," + content + "," + timestamp + "," + send + "," + isRead);

        // 保存更新后的聊天记录
        saveChatRecords(preferences, chatRecords);

        // 更新最近获取时间
        updateLastAccessTime(context, friendId);
    }

    public static void saveChatRecordsFromMessages(Context context, List<Message> messages) {
        if (context == null || messages == null || messages.isEmpty()) {
            return;
        }

        for (Message message : messages) {
            int friendId = message.getFriendId();
            int ownId = message.getOwnId();
            String content = message.getContent();
            String timestamp = message.getTimestamp();
            int send = message.getSend();
            boolean isRead = message.isRead();

            // 保存聊天记录
            saveChatRecord(context, friendId, ownId, content, timestamp, send, isRead);
        }
    }

    public static List<String> getChatRecords(Context context, int friendId) {
        if (context == null) {
            return new ArrayList<>();
        }

        // 获取对应 friendId 的 SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_PREFIX + friendId, Context.MODE_PRIVATE);

        // 更新最近获取时间
        updateLastAccessTime(context, friendId);

        // 获取聊天记录
        return getChatRecords(preferences);
    }

    public static String getLastAccessTime(Context context, int friendId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_PREFIX + friendId, Context.MODE_PRIVATE);
        return preferences.getString(LAST_ACCESS_TIME_PREFIX + friendId, "");
    }

    private static List<String> getChatRecords(SharedPreferences preferences) {
        List<String> chatRecords = new ArrayList<>();
        int recordCount = preferences.getInt("record_count", 0);

        // 从SharedPreferences中读取聊天记录
        for (int i = 0; i < recordCount; i++) {
            String record = preferences.getString("record_" + i, "");
            if (!TextUtils.isEmpty(record)) {
                chatRecords.add(record);
            }
        }
        return chatRecords;
    }

    private static void saveChatRecords(SharedPreferences preferences, List<String> chatRecords) {
        if (preferences == null || chatRecords == null) {
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();

        // 保存聊天记录数量
        editor.putInt("record_count", chatRecords.size());

        // 保存每条聊天记录
        for (int i = 0; i < chatRecords.size(); i++) {
            editor.putString("record_" + i, chatRecords.get(i));
        }

        // 提交保存
        editor.apply();
    }

    private static void updateLastAccessTime(Context context, int friendId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME_PREFIX + friendId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // 保存最近获取时间
        String currentTime = getCurrentTime();
        editor.putString(LAST_ACCESS_TIME_PREFIX + friendId, currentTime);

        // 提交保存
        editor.apply();
    }

    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

