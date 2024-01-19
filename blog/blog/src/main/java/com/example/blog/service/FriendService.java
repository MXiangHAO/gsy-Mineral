package com.example.blog.service;

import com.example.blog.mapper.FriendMapper;
import com.example.blog.pojo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    private final FriendMapper friendMapper;

    @Autowired
    public FriendService(FriendMapper friendMapper) {
        this.friendMapper = friendMapper;
    }

    // 添加好友
    public void addFriend(int userId, int friendId) {
        friendMapper.addFriend(userId, friendId);
        friendMapper.addFriend(friendId, userId);
    }

    // 根据 userId 和 friendId 查询好友
    public List<Friend> findFriendByUserId(int userId) {
        return friendMapper.findFriendByUserAndFriendId(userId);
    }
}
