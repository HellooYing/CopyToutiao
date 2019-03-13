package com.toutiao.controller;

import com.toutiao.model.User;
import com.toutiao.model.News;
import com.toutiao.model.ViewObject;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        List<News> newsList=newsService.getLatestNews(0,0,10);
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList){
            ViewObject vo=new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "home";
    }
}
