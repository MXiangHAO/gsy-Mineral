package com.example.blog.service;

import com.example.blog.mapper.FollowMapper;
import com.example.blog.pojo.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;

    public int followUser(int uId, int fUId) {
        // 添加关注
        Follow follow = new Follow(uId,fUId);
        return followMapper.insertFollow(follow);
    }

    public int unfollowUser(int uId, int fUId) {
        // 取消关注
        return followMapper.deleteFollowByUidAndFUid(uId, fUId);
    }



    public Follow getFollowByUidAndFUid(int uId, int fUId) {
        // 根据关注者和被关注者获取关注信息
        return followMapper.selectFollowByUidAndFUid(uId, fUId);
    }
}
