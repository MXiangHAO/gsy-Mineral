package com.example.blog.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class StringJson {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 将 JSON 字符串转换为指定类型的对象
    public static <T> T fromJson(String jsonString, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonString, valueType);
    }

    // 将对象转换为 JSON 字符串
    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    // 将 JSON 字符串转换为 String
    public static String fromJsonToString(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, String.class);
    }

    // 将 String 转换为 JSON 字符串
    public static String toJsonFromString(String stringValue) throws JsonProcessingException {
        return objectMapper.writeValueAsString(stringValue);
    }

    // 将 String 转换为指定类型的对象
    public static <T> T fromString(String stringValue, Class<T> valueType) throws IOException {
        return objectMapper.readValue(stringValue, valueType);
    }

    // 将对象转换为 String
    public static String toString(Object object) {
        return String.valueOf(object);
    }
}
