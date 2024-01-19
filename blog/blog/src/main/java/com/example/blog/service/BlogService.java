package com.example.blog.service;

import com.example.blog.mapper.BlogMapper;
import com.example.blog.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private final BlogMapper blogMapper;

    @Autowired
    public BlogService(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    public boolean addBlog(Blog blog) {
        if(blogMapper.addBlog(blog)>0) {
            return  true;
        } else {
            return  false;
        }
    }
    public int getBlogIdByUD(int uId, String date) {
        return blogMapper.getBlogIdByUD(uId, date);
    }
    public List<Blog> getAllBlogs() {
        return blogMapper.getAllBlogs();
    }

    public Blog getBlogById(int bId) {
        return blogMapper.getBlogById(bId);
    }

    public void deleteBlog(int bId) {
        blogMapper.deleteBlog(bId);
    }

    // 可以添加其他业务逻辑方法...

}
