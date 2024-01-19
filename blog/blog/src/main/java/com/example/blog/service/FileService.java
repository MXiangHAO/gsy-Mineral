package com.example.blog.service;

import com.example.blog.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.blog.pojo.File;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    @Autowired
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(File file) {
        return fileMapper.addFile(file);
    }

    public int deleteFileById(int fId) {
        return fileMapper.deleteFileById(fId);
    }

    public int updateFile(File file) {
        return fileMapper.updateFile(file);
    }

    public File getFileByBUId(File file) {
        return fileMapper.getFileByBUId(file);
    }

    public List<File> getFilesByBId(int bId) {
        return fileMapper.getFilesByBId(bId);
    }

    public List<File> getFilesByUId(int uId) {
        return fileMapper.getFilesByUId(uId);
    }
}
