package com.example.chapter_blog.Adapter;

import com.example.chapter_blog.pojo.Blog;

public class BlogManager {
    private static BlogManager instance;
    private Blog currentBlog;

    private BlogManager() {
        // 私有构造函数
    }

    public static synchronized BlogManager getInstance() {
        if (instance == null) {
            instance = new BlogManager();
        }
        return instance;
    }

    public Blog getCurrentBlog() {
        return currentBlog;
    }

    public void setCurrentBlog(Blog blog) {
        currentBlog = blog;
    }
}
