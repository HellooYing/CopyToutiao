package com.toutiao.service;

import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismenber(likeKey,String.valueOf(userId))){
            return 1;
        }
        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        return jedisAdapter.sismenber(dislikeKey,String.valueOf(userId))?-1:0;
    }
    public long like(int userId,int entityType,int entityId){
        System.out.println("likeService");
        String likeKey=RedisKeyUtil.getLikeKey(entityId,entityType);
        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        if(jedisAdapter.sismenber(likeKey,String.valueOf(userId))){
            //如果已经喜欢了，说明是要取消喜欢，则从喜欢集合里删去
            jedisAdapter.srem(likeKey,String.valueOf(userId));
        }
        else jedisAdapter.sadd(likeKey,String.valueOf(userId));
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
    public long dislike(int userId,int entityType,int entityId){
        String likeKey=RedisKeyUtil.getLikeKey(entityId,entityType);
        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        if(jedisAdapter.sismenber(dislikeKey,String.valueOf(userId))){
            jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        }
        else jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
