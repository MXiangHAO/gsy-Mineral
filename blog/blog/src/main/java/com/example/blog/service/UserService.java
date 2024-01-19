package com.example.blog.service;
import com.example.blog.mapper.UserMapper;
import com.example.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByType(String value, String type) {
        if ("email".equals(type)) {
            return getUserByEmail(value);
        } else if ("phone".equals(type)) {
            return getUserByPhone(value);
        } else {
            // 处理其他情况，例如抛出异常或返回空值，根据实际需求进行处理
            return null;
        }
    }
    public User getUserById(int id){
        return userMapper.getUserById(id);
    }
    // 根据 email 查询用户
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    // 根据 phone 查询用户
    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }
    //添加用户
    public boolean addUser(User user) {
        int rowsAffected = userMapper.addUser(user);
        return rowsAffected > 0;
    }
    public boolean updateUser(User updatedUser) {
        int rowsAffected = userMapper.updateUser(updatedUser);
        return rowsAffected > 0;
    }
}
