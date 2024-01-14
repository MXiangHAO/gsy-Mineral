package com.example.chapter_blog.pojo;

public class CollectItem {
    private int id;
    private int cId;
    private int uId;
    private int bId;
    private int collectCount;
    private String collectName;

    private String title;
    private String fullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public CollectItem(int id, int cId, int uId, int bId, String collectName, String title, String fullName) {
        this.id = id;
        this.cId = cId;
        this.uId = uId;
        this.bId = bId;
        this.collectName = collectName;
        this.title = title;
        this.fullName = fullName;
    }
    public CollectItem(int cId, String collectName,int collectCount) {
        this.cId = cId;
        this.collectName = collectName;
        this.collectCount=collectCount;
    }
}
