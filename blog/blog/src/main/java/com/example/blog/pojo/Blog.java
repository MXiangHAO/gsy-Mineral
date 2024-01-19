package com.example.blog.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author KGong
 */
@Getter
public class Blog {
    @Setter
    private int bId;
    @Setter
    private int uId;
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private String date;
    @Setter
    private int clicks;

    /**
     * 无参构造方法
     */
    public Blog() {
        // 可以在这里进行一些初始化操作，或者留空
    }

    /**
     * 带参数的构造方法
     */
    public Blog( int uId, String title, String content, String date) {
        this.uId = uId;
        this.title = title;
        this.content = content;
        this.date = date;
    }
    public Blog( int uId, String title, String content, String date,int clicks) {
        this.uId = uId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.clicks=clicks;
    }

    /**
     * toString 方法
     */
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
