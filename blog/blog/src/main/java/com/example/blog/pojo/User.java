package com.example.blog.pojo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import com.example.blog.util.*;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String phone;
    private String email;

    // Constructors, getters, and setters

    public User() {
        // Default constructor for JPA
        username=random.generateRandomString();

    }

    @JsonCreator
    public User(
            @JsonProperty("id") String id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("phone") String phone,
            @JsonProperty("email") String email) {
        this.id = Long.valueOf(id);
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"username\":\"" + username + '\"' +
                ", \"password\":\"" + password + '\"' +
                ", \"phone\":\"" + phone + '\"' +
                ", \"email\":\"" + email + '\"' +
                '}';
    }
}
