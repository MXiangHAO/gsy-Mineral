package com.example.blog.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import com.example.blog.pojo.File;

import java.util.List;

@Mapper
public interface FileMapper {

    int addFile(File file);


    int deleteFileById(int fId);


    int updateFile(File file);


    File getFileByBUId(File file);


    List<File> getFilesByBId(int bId);


    List<File> getFilesByUId(int uId);
}
