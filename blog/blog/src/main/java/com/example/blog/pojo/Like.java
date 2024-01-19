package com.example.blog.pojo;

import lombok.Getter;
import lombok.Setter;

public class Like {
    @Getter @Setter
    private int lId;

    @Getter @Setter
    private int bId;

    @Getter @Setter
    private int uId;

    public Like() {
        // 默认构造函数
    }

    public Like( int userId,int blogId) {
        this.bId = blogId;
        this.uId = userId;
    }
    public Like(int likeId,int blogId, int userId){
        this.lId=likeId;
        this.bId = blogId;
        this.uId = userId;
    }
}
