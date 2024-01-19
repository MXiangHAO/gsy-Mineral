package com.example.blog.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Collect {
    private int id;
    private int cId;
    private int uId;
    private int bId;
    private String collectName;

    // 无参构造方法
    public Collect() {
        // 可以在这里进行一些初始化操作，或者留空
    }
    public Collect(int uId, int bId) {
        this(uId, bId, "默认收藏夹");
    }

    public Collect(int uId, int bId, String collectName) {
        this.uId = uId;
        this.bId = bId;
        this.collectName = collectName;
    }

    // 带参数的构造方法

    // toString 方法
    @Override
    public String toString() {
        return "Collect{" +
                "id=" + id +
                ", cId=" + cId +
                ", uId=" + uId +
                ", bId=" + bId +
                ", collectName='" + collectName + '\'' +
                '}';
    }
}
