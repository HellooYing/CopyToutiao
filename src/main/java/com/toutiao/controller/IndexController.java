package com.toutiao.controller;

import com.toutiao.aspect.LogAspect;
import com.toutiao.model.User;
import com.toutiao.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private ToutiaoService toutiaoService;

//    @RequestMapping(path = {"/"})
//    @ResponseBody
//    public String index(HttpSession session) {
//        logger.info("Visit Index");
//        return "hi" + toutiaoService.say() + session.getAttribute("msg");
//    }

    @RequestMapping("/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "mcq") String key
    ) {
        return String.format("{%s},{%d},{%d},{%s}", groupId, userId, type, key);
    }

    @RequestMapping("/hello")
    public String news(Model model) {
        model.addAttribute("name", "1");
        List<String> colors = Arrays.asList(new String[]{"red", "white", "blue"});
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("colors", colors);
        model.addAttribute("map", map);
        model.addAttribute("user", new User("Marry"));
        return "index";
    }
    @RequestMapping("/header")
    public String head() {
        return "header";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headNames = request.getHeaderNames();
        while (headNames.hasMoreElements()) {
            String name = headNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        for (Cookie cookie : request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br");
        }
        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "nc", defaultValue = "a") String nc,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "mycookie:" + nc;
    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code) {
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping("/redirect")
    public String redirect1(HttpSession session) {
        session.setAttribute("msg", "jump from redirect");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("key wrong");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}
