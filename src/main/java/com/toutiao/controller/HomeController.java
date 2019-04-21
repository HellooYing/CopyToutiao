package com.toutiao.controller;

import com.toutiao.aspect.LogAspect;
import com.toutiao.model.*;
import com.toutiao.service.LikeService;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos",getNews(0,0,30));
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String user(Model model, @PathVariable("userId") int userId,
                       @RequestParam(value = "pop",defaultValue = "0") int pop) {
        model.addAttribute("vos",getNews(userId,0,10));
        model.addAttribute("pop",pop);
        return "home";
    }

    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newsList=newsService.getLatestNews(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if(hostHolder.getUser()!=null){
                int likeStatus=likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,news.getId());
                vo.set("like",likeStatus);
            }
            else vo.set("like",0);
            vos.add(vo);
        }
        return vos;
    }
}
