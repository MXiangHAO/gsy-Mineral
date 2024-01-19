package com.example.blog.service;

import com.example.blog.mapper.MessageMapper;
import com.example.blog.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public int addMessage(Message message) {
        // 添加消息到数据库
        return messageMapper.addMessage(message);
    }

    public List<Message> getMessagesByOwnIdAndFriendId(int ownId,int friendId) {
        // 根据 ownId 查询消息
        return messageMapper.getMessagesByOwnIdAndFriendId(ownId,friendId);
    }

    public void updateIsReadStatus(Message message){
        messageMapper.updateIsReadStatus(message);
    }

    public List<Message> getNewMessagesByOwnIdAndFriendId(int ownId, int friendId) {
        return messageMapper.getNewMessagesByOwnIdAndFriendId( ownId, friendId);
    }
}

