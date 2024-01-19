package com.example.blog.mapper;

import com.example.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUserByEmail(String email);
    User getUserById(int Id);

    // 根据 phone 查询用户
    User getUserByPhone(String phone);

    int addUser(User user);

    int updateUser(User updatedUser);
}
