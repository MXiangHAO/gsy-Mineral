package com.example.blog.mapper;

import com.example.blog.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface BlogMapper {
    int addBlog(Blog blog);

    List<Blog> getAllBlogs();
    int getBlogIdByUD( int uId, String date);

    Blog getBlogById(int bId);

    void deleteBlog(int bId);
}
