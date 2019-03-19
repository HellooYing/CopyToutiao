package com.toutiao.controller;

import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.model.EntityType;
import com.toutiao.model.HostHolder;
import com.toutiao.model.News;
import com.toutiao.service.LikeService;
import com.toutiao.service.NewsService;
import com.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;
    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newId") int newsId){
        long likeCount=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                .setEntityOwnerId(newsService.getById(newsId).getUserId()));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId){
        long likeCount=likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
