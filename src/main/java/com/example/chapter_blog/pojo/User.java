package com.example.chapter_blog.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String email;

    // 构造函数
    @JsonCreator
    public User(@JsonProperty("id") int id,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email,
                @JsonProperty("phone") String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public User() {

    }

    // 添加 getters 和 setters 方法

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
