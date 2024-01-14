package com.example.chapter_blog.pojo;

public class Message {
    private int friendId;
    private int ownId;
    private String content;
    private String timestamp;
    private int send;
    private boolean isRead;

    public Message(int friendId, int ownId, String content, String timestamp, int send, boolean isRead) {
        this.friendId = friendId;
        this.ownId = ownId;
        this.content = content;
        this.timestamp = timestamp;
        this.send = send;
        this.isRead = isRead;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public int getOwnId() {
        return ownId;
    }

    public void setOwnId(int ownId) {
        this.ownId = ownId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}


