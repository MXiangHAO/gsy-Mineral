package com.example.blog.pojo;

public class MessageItem {
    private String userAvatar;  // 用户头像Bitmap
    private String username;    // 用户名
    private int senderId;
    private int receiverId;
    private String timestamp;
    private String recentMessage;

    public MessageItem(String userAvatar, String username, int senderId, int receiverId, String timestamp, String recentMessage) {
        this.userAvatar = userAvatar;
        this.username = username;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.recentMessage = recentMessage;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }
}