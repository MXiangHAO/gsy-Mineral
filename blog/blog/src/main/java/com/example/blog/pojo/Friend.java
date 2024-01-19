package com.example.blog.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Friend {
    public int userId;
    public int friendId;
    // 空构造函数
    public Friend() {
    }
    // 构造函数
    public Friend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
