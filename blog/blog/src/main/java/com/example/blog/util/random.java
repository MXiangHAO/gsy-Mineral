package com.example.blog.util;

import java.security.SecureRandom;

public class random {
    public static String generateRandomString() {
        // 定义可选的字符集合
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // 使用SecureRandom生成随机数
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder();

        // 生成四个随机字符
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}