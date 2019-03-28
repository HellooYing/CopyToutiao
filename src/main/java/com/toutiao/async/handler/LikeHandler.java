package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.model.Message;
import com.toutiao.model.User;
import com.toutiao.service.MessageService;
import com.toutiao.service.NewsService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message=new Message();
        User user=userService.getUser(eventModel.getActorId());
        message.setContent("用户"+user.getName()+"赞了您的资讯："+
                newsService.getById(eventModel.getEntityId()).getTitle()+
                ",网址为："+ ToutiaoUtil.TOUTIAO_DOMAIN+"news/"+eventModel.getEntityId());
        int fromId=13;
        message.setFromId(eventModel.getActorId());
        //message.setToId(eventModel.getEntityOwnerId());
        //int toId=eventModel.getActorId();
        int toId=eventModel.getEntityOwnerId();
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
