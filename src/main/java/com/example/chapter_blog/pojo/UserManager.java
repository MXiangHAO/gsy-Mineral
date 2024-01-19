package com.example.chapter_blog.pojo;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {
        // 私有构造函数
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }
}

