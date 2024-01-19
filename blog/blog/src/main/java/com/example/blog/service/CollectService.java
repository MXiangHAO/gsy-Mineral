package com.example.blog.service;

import com.example.blog.mapper.CollectMapper;
import com.example.blog.pojo.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;

    public int addCollect(Collect collect) {
        return collectMapper.insertCollect(collect);
    }

    public int deleteCollectByUidAndBid(int uId, int bId) {
        return collectMapper.deleteCollectByUidAndBid(uId, bId);
    }

    public int updateCollect(Collect collect) {
        return collectMapper.updateCollect(collect);
    }

    public Collect getCollectByUidAndBid(int uId, int bId) {
        return collectMapper.selectCollectByUidAndBid(uId, bId);
    }
}

