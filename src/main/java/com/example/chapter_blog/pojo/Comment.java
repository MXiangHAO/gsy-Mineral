package com.example.chapter_blog.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Comment {
    @JsonProperty("cid")
    private transient int cId;
    @JsonProperty("uid")
    private int uId;
    @JsonProperty("bid")
    private int bId;
    private String content;
    private Date date;
    private String username; // 用户名
    private String avatar; // 头像URL

    public Comment() {
    }

    public Comment(int cId, int uid, int bId, String content, Date date) {
        this.cId = cId;
        this.uId = uid;
        this.bId = bId;
        this.content = content;
        this.date = date;
    }
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

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // ToString method for debugging
//    @Override
//    public String toString() {
//        return "Comment{" +
//                "cId=" + cId +
//                ", uId=" + uId +
//                ", bId=" + bId +
//                ", content='" + content + '\'' +
//                ", date=" + date +
//                '}';
//    }
}
