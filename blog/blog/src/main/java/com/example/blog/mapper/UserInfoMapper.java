package com.example.blog.mapper;

import com.example.blog.pojo.User;
import com.example.blog.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper {
    @Select("SELECT * FROM userinfo WHERE userId = #{userId}")
    UserInfo getUserInfoByUserId(@Param("userId") int userId);

    @Select("select * from user where id=#{id}")
    User getUserById(@Param("id") int id);
}
