package com.toutiao.service;

import com.toutiao.dao.NewsDAO;
import com.toutiao.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;
    public List<News> getLastNews(int userId,int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }
}
