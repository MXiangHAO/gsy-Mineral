package com.example.blog.service;

import com.example.blog.mapper.LikeMapper;
import com.example.blog.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeMapper likeMapper;

    @Autowired
    public LikeService(LikeMapper likeMapper) {
        this.likeMapper = likeMapper;
    }

    // 添加点赞记录
    public void addLike(Like like) {
        likeMapper.insertLike(like);
    }

    // 根据likeId删除点赞记录
    public void deleteLikeById(int likeId) {
        likeMapper.deleteLikeById(likeId);
    }

    // 更新点赞记录
    public void updateLike(Like like) {
        likeMapper.updateLike(like);
    }

    // 根据uId和bId查询点赞记录
    public Like getLikeByUidAndBid(int userId, int blogId) {
        return likeMapper.getLikeByUidAndBid(userId, blogId);
    }

    // 根据uId和bId删除点赞记录
    public void deleteLikeByUidAndBid(int userId, int blogId) {
        likeMapper.deleteLikeByUidAndBid(userId, blogId);
    }
}

