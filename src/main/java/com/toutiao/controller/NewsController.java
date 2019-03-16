package com.toutiao.controller;

import com.toutiao.aspect.LogAspect;
import com.toutiao.service.NewsService;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/uploadImage"},method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try{
            String fileUrl = newsService.saveImage(file);
            if(fileUrl==null) return ToutiaoUtil.getJSONString(1,"上传失败");
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }
        catch(Exception e){
            logger.error("上传图片异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }
    }
}
