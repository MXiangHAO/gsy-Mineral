package com.example.blog.pojo;

import lombok.Getter;
import lombok.Setter;

public class Follow {

    private int id;
    private int uId;

    private int FUId;

    public void setFUId(int FUId){
        this.FUId=FUId;
    }

    public int getFUId(int FUId){
        return  this.FUId;
    }
    // 无参构造方法
    public Follow() {
    }

    // 带参构造方法
    public Follow(int uId, int fUId) {
        this.uId = uId;
        this.FUId = fUId;
    }

    public Follow(int id,int uId, int fUId) {
        this.uId = uId;
        this.FUId = fUId;
        this.id=id;
    }
    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }



    @Override
    public String toString() {
        return "YourEntity{" +
                "id=" + id +
                ", uId=" + uId +
                ", fUId=" + FUId +
                '}';
    }
}

