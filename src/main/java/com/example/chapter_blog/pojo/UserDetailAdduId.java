package com.example.chapter_blog.pojo;

public class UserDetailAdduId {
    private String username;
    private String avatar;
    private int uid;
    // Constructor
    public UserDetailAdduId() {
    }
    public UserDetailAdduId(String username, String avatar,int uid) {
        this.username = username;
        this.avatar = avatar;
        this.uid=uid;
    }

    // Getter and setter methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUid(){return uid;}

    public void setUid(int uid){this.uid=uid;}

//    // Optionally, override toString for easy logging or debugging
//    @Override
//    public String toString() {
//        return "UserDetailAdduId{" +
//                "username='" + username + '\'' +
//                ", avatar='" + avatar + '\'' +
//                '}';
//    }
}
