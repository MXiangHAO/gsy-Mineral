package com.example.chapter_blog.pojo;

public class MyFile {
    private int fId;
    private int uId;
    private int bId;
    private String url;

    // 构造方法
    public MyFile(int fId, int uId, int bId, String url) {
        this.fId = fId;
        this.uId = uId;
        this.bId = bId;
        this.url = url;
    }

    // Getter和Setter方法
    public int getFId() {
        return fId;
    }

    public void setFId(int fId) {
        this.fId = fId;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public int getBId() {
        return bId;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // toString方法
    @Override
    public String toString() {
        return "MyFile{" +
                "fId=" + fId +
                ", uId=" + uId +
                ", bId=" + bId +
                ", url='" + url + '\'' +
                '}';
    }
}

