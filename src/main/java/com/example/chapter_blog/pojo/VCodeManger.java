package com.example.chapter_blog.pojo;

public class VCodeManger {
    private static VCodeManger instance;
    private String currentCode;

    private VCodeManger(){

    }
    public static synchronized VCodeManger getInstance(){
        if (instance==null){
            instance = new VCodeManger();
        }
        return  instance;
    }

    public String getCurrentCode(){
        return currentCode;
    }

    public void setCurrentCode(String code){currentCode=code;}

}
