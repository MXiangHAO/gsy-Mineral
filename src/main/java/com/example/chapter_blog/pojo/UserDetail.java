package com.example.chapter_blog.pojo;

//用户名和头像类
public  class UserDetail {
    private String username;
    private String avatar;

    // Constructor
    public UserDetail() {
    }
    public UserDetail(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
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

    // Optionally, override toString for easy logging or debugging
    @Override
    public String toString() {
        return "UserDetail{" +
                "username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
