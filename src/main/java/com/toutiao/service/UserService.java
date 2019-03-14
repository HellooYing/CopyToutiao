package com.toutiao.service;

import com.toutiao.dao.UserDAO;
import com.toutiao.model.User;
import com.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by toutiao on 2016/7/2.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
        }
        if(StringUtils.isBlank(password)){
            map.put("msgname","密码不能为空");
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户名已被注册");
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);
        return map;
    }
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
