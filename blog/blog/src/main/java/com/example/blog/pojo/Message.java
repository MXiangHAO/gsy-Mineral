package com.example.blog.pojo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int id;
    private int friendId;
    private int ownId;
    private String content;
    private String timestamp;
    //0是自己发送 1是对方发送
    private int send;
    private boolean isRead;
    public Message(int friendId,int ownId,String content,String timestamp,int send,boolean isRead){
        this.friendId=friendId;
        this.ownId=ownId;
        this.content=content;
        this.timestamp=timestamp;
        this.send=send;
        this.isRead=isRead;
    }
}