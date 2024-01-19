package com.example.blog.service;

import com.example.blog.mapper.UserInfoMapper;
import com.example.blog.pojo.User;
import com.example.blog.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserInfoMapper userInfoMapper;

    @Autowired
    public UserInfoService(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    public User getUserById(int id){
        return userInfoMapper.getUserById(id);
    }

    public UserInfo getUserInfoByUserId(int userId) {
        try {
            // 调用 UserInfoMapper 的方法获取用户信息
            return userInfoMapper.getUserInfoByUserId(userId);
        } catch (Exception e) {
            // 如果查询失败，可以进行相应的异常处理
            e.printStackTrace();
            return null;
        }
    }
}
