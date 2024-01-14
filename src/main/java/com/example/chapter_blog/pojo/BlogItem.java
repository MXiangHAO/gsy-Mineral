package com.example.chapter_blog.pojo;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogItem {
    private int bId;
    private int uId;
    private String urls;
    private String title;
    private String content;
    private String date;
    private String fullName;
    private String avatar;

    private int clicks;
    private int likes;
    private int collects;

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrl(String urls) {
        this.urls = urls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCollects() {
        return collects;
    }

    public void setCollects(int collects) {
        this.collects = collects;
    }

    // 将 BlogItem 对象转换为 JSON 字符串
    public String convertToJsonString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bId", this.bId);
            jsonObject.put("uId", this.uId);
            jsonObject.put("urls", this.urls);
            jsonObject.put("title", this.title);
            jsonObject.put("content", this.content);
            jsonObject.put("date", this.date);
            jsonObject.put("fullName", this.fullName);
            jsonObject.put("avatar", this.avatar);
            jsonObject.put("clicks", this.clicks);
            jsonObject.put("likes", this.likes);
            jsonObject.put("collects", this.collects);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public BlogItem(int bId, int uId, String urls, String title, String content, String date, String fullName, String avatar, int clicks, int likes, int collects) {
        this.bId = bId;
        this.uId = uId;
        this.urls = urls;
        this.title = title;
        this.content = content;
        this.date = date;
        this.fullName = fullName;
        this.avatar = avatar;
        this.clicks = clicks;
        this.likes = likes;
        this.collects = collects;
    }
}
