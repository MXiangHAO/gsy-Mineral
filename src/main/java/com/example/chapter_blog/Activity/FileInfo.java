package com.example.chapter_blog.Activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FileInfo implements Serializable {
    private String filename;
    private String url;
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("filename", filename);
        map.put("url", url);
        return map;
    }
    // 构造函数
    public FileInfo(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }

    // getter和setter
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
