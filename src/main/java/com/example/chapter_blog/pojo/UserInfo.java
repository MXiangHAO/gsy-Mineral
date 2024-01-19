package com.example.chapter_blog.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    private String username;
    private String fullName;
    private String birthday;
    private String address;
    private String gender;
    private String occupation;
    private int userId; // 更新为 int 类型
    private String avatar;

    // 构造方法（可以根据需要添加）
    public UserInfo() {

    }

    // 带参数的构造方法
    @JsonCreator
    public UserInfo(@JsonProperty("fullName") String fullName,
                    @JsonProperty("birthday") String birthday,
                    @JsonProperty("address") String address,
                    @JsonProperty("gender") String gender,
                    @JsonProperty("occupation") String occupation,
                    @JsonProperty("userId") int userId,
                    @JsonProperty("avatar") String avatar) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.address = address;
        this.gender = gender;
        this.occupation = occupation;
        this.userId = userId;
        this.avatar = avatar;
    }
    // Getter 和 Setter 方法
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getUserId() { // 更新为 int 类型
        return userId;
    }

    public void setUserId(int userId) { // 更新为 int 类型
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // toString 方法  是否做更改
    @Override
    public String toString() {
        return "UserInfo{" +
                "fullName='" + fullName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", occupation='" + occupation + '\'' +
                ", userId=" + userId + // 更新为 int 类型
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }
}

