package com.toutiao.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();
    //线程本地变量
    public User getUser(){
        return users.get();
    }
    public void setUsers(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
