package com.toutiao.controller;

import com.toutiao.aspect.LogAspect;
import com.toutiao.model.*;
import com.toutiao.service.CommentService;
import com.toutiao.service.LikeService;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/del"},method = {RequestMethod.GET})
    public String delComment(@RequestParam(value = "commentId") int commentId,
                             @RequestParam(value ="newsId") int newsId){
        try {
            commentService.deleteComment(commentId);
            int count=commentService.getCommentCount(newsId,EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId,count);
        }
        catch (Exception e) {
            logger.error("删除评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("content") String content,
                             @RequestParam("newsId") int newsId){
        try {
        Comment comment=new Comment();
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setEntityId(newsId);
        comment.setEntityType(EntityType.ENTITY_NEWS);
        comment.setStatus(0);
        comment.setUserId(hostHolder.getUser().getId());
        commentService.addComment(comment);
        int count=commentService.getCommentCount(comment.getEntityId(),EntityType.ENTITY_NEWS);
        newsService.updateCommentCount(comment.getEntityId(),count);
        }
        catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(Model model, @PathVariable("newsId") int newsId) {
        try{
            News news=newsService.getById(newsId);
            model.addAttribute("news",news);
            model.addAttribute("owner",userService.getUser(news.getUserId()));//发表news的人
            int likeStatus=likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,news.getId());
            model.addAttribute("like",likeStatus);
            List<Comment> comments=commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for(Comment comment:comments){
                ViewObject commentVO=new ViewObject();
                commentVO.set("comment",comment);//评论
                commentVO.set("user",userService.getUser(comment.getUserId()));//发表评论的人
                commentVOs.add(commentVO);
            }
            model.addAttribute("comments",commentVOs);
        }
        catch(Exception e){
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path = {"/uploadImage"},method = {RequestMethod.POST})
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
    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        }
        catch (Exception e){
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }
    @RequestMapping(path = {"/user/addNews"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                        @RequestParam("title") String title,
                        @RequestParam("link") String link) {
        try{
            News news=new News();
            news.setCreatedDate(new Date());
            news.setLink(link);
            news.setImage(image);
            news.setTitle(title);
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }
            else{
                news.setUserId(1);//匿名用户
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }
        catch (Exception e){
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }
}
