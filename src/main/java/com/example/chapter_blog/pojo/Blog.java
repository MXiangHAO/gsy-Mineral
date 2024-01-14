package com.example.chapter_blog.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Blog {
    private int bId;
    private int uId;
    private String title;
    private String content;
    private String date;

    // 无参构造方法
    public Blog() {
        // 可以在这里进行一些初始化操作，或者留空
    }

    // 带参数的构造方法
    @JsonCreator
    public Blog(@JsonProperty("bId") int bId,
                @JsonProperty("uId") int uId,
                @JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("date") String date) {
        this.bId = bId;
        this.uId = uId;
        this.title = title;
        this.content = content;
        this.date = date;
    }


    // Getter 和 Setter 方法
    public int getBId() {
        return bId;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
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

    // toString 方法
    @Override
    public String toString() {
        return "Blog{" +
                "bId=" + bId +
                ", uId=" + uId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

