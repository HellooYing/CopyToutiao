package com.toutiao.controller;

import com.toutiao.aspect.LogAspect;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.model.News;
import com.toutiao.model.ViewObject;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int remember,
                      HttpServletResponse response) {
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                addCookie(map,remember,response);
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }
            else{
                return ToutiaoUtil.getJSONString(1,map);
            }

        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }
    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int remember,
                        HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                addCookie(map,remember,response);
                EventModel eventModel=new EventModel(EventType.LOGIN);
                eventModel.setActorId((int) map.get("userId"))
                        .setExt("username", "2669612599")
                        .setExt("to", "2213073589@qq.com");
                eventProducer.fireEvent(eventModel);
                return ToutiaoUtil.getJSONString(0,"登录成功");
            }
            else{
                return ToutiaoUtil.getJSONString(1,map);
            }

        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录异常");
        }
    }
    private void addCookie(Map<String,Object> map,int remember,HttpServletResponse response){
        Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
        cookie.setPath("/");
        if(remember>0){
            cookie.setMaxAge(3600*24*5);
        }
        response.addCookie(cookie);
    }
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
