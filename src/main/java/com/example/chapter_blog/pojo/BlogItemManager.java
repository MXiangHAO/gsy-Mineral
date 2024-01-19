package com.example.chapter_blog.pojo;

public class BlogItemManager {
    private static BlogItemManager instance;
    private BlogItem currentBlogItem;

    private BlogItemManager() {
        // 私有构造函数
    }

    public static synchronized BlogItemManager getInstance() {
        if (instance == null) {
            instance = new BlogItemManager();
        }
        return instance;
    }

    public BlogItem getCurrentBlogItem() {
        return currentBlogItem;
    }

    public void setCurrentBlogItem(BlogItem blogItem) {
        currentBlogItem = blogItem;
    }
}

