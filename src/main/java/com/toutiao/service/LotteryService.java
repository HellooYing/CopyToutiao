package com.toutiao.service;

import com.toutiao.dao.LotteryDAO;
import com.toutiao.model.Activity;
import com.toutiao.model.JoinIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LotteryService {
    @Autowired
    LotteryDAO lotteryDAO;

    public List<Activity> getActivityByUserId(int userId){
        //用户参加了哪些活动
        return lotteryDAO.selectActivityByUserId(userId);
    }

    public List<Activity> getActivityByAdminId(int adminId){
        //管理员发布了哪些活动
        return lotteryDAO.selectActivityByAdminId(adminId);
    }

    public int getResultByActivityIdAndUserId(int userId,int actId){
        //根据活动id和用户id获取中奖结果
        return lotteryDAO.selectResultByActivityIdAndUserId(userId,actId);
    }

    public List<JoinIn> getResultByUserId(int userId){
        //根据活动id和用户id获取中奖结果
        return lotteryDAO.selectResultByUserId(userId);
    }

    public List<JoinIn> getResultByActivityId(int actId){
        //根据活动id获取全部中奖结果
        return lotteryDAO.selectResultByActivityId(actId);
    }

    public Activity getActivityById(int actId){
        //根据活动id获取活动
        return lotteryDAO.selectActivityById(actId);
    }

    public int addActivity(Activity activity){
        //增加活动(恐怖的字段)
        return lotteryDAO.addActivity(activity);
    }

    public int addJoinIn(JoinIn joinIn){
        //记录参与情况(导入人员信息时)
        return lotteryDAO.addJoinIn(joinIn);
    }

    public int setResult(int userId,int actId, int result){
        //更新中奖结果(根据用户ID和活动ID)
        return lotteryDAO.updateResult(userId, actId, result);
    }
    public int setAwardCount(int actId, int countOne,int countTwo,int countThree){
        return lotteryDAO.updateAwardCount(actId, countOne,countTwo,countThree);
    }

}
