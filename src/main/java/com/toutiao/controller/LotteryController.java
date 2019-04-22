package com.toutiao.controller;

import com.alibaba.fastjson.JSONObject;
import com.toutiao.model.Activity;
import com.toutiao.model.HostHolder;
import com.toutiao.model.JoinIn;
import com.toutiao.service.LotteryService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LotteryController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LotteryService lotteryService;
    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);

    @RequestMapping(path = {"/lotteryLogin"}, method = {RequestMethod.GET})
    public String lotteryLogin() {
        return "lotteryLogin";
    }

    @RequestMapping(path = {"/activity/{actId}"}, method = {RequestMethod.GET})
    public String activityPage(Model model, @PathVariable("actId") int actId){
        Activity activity = lotteryService.getActivityById(actId);
        model.addAttribute("activity",activity);
        return "activity";
    }

    @RequestMapping(path = {"/activity/{actId}/winning"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String winning(@PathVariable("actId") int actId,
                          @RequestParam("prize") int prize
                          ){
        //找到join_in中的该条，结果改为prize。
        //找到该活动，prize级结果-1，如果之前有别的中奖奖品归还，返回
        int user_id=hostHolder.getUser().getId();
        int lastResult=lotteryService.getResultByActivityIdAndUserId(user_id,actId);
        lotteryService.setResult(user_id,actId,prize);
        Activity activity = lotteryService.getActivityById(actId);
        if(lastResult!=0){
            if(lastResult==1) {
                activity.setAwardCountOne(activity.getAwardCountOne()+1);
            }
            if(lastResult==2) {
                activity.setAwardCountTwo(activity.getAwardCountTwo()+1);
            }
            if(lastResult==3) {
                activity.setAwardCountThree(activity.getAwardCountThree()+1);
            }
        }
        if(prize==1){
            activity.setAwardCountOne(activity.getAwardCountOne()-1);
        }
        else if(prize==2){
            activity.setAwardCountTwo(activity.getAwardCountTwo()-1);
        }
        else if(prize==3){
            activity.setAwardCountThree(activity.getAwardCountThree()-1);
        }
        lotteryService.setAwardCount(actId,activity.getAwardCountOne(),activity.getAwardCountTwo(),activity.getAwardCountThree());
        JSONObject response=new JSONObject();
        response.put("code",0);
        response.put("firstPrizeAmount",String.valueOf(activity.getAwardCountOne()));
        response.put("firstPrizeProbability",String.valueOf(activity.getPrizeRateOne()));
        response.put("secondPrizeAmount",String.valueOf(activity.getAwardCountTwo()));
        response.put("secondPrizeProbability",String.valueOf(activity.getPrizeRateTwo()));
        response.put("thirdPrizeAmount",String.valueOf(activity.getAwardCountThree()));
        response.put("thirdPrizeProbability",String.valueOf(activity.getPrizeRateThree()));
        return response.toJSONString();
    }

    @RequestMapping(path = {"/activity/{actId}/refresh"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String activityRefresh(@PathVariable("actId") int actId){
        Activity activity = lotteryService.getActivityById(actId);
        JSONObject response=new JSONObject();
        response.put("code",0);
        response.put("firstPrizeAmount",String.valueOf(activity.getAwardCountOne()));
        response.put("firstPrizeProbability",String.valueOf(activity.getPrizeRateOne()));
        response.put("secondPrizeAmount",String.valueOf(activity.getAwardCountTwo()));
        response.put("secondPrizeProbability",String.valueOf(activity.getPrizeRateTwo()));
        response.put("thirdPrizeAmount",String.valueOf(activity.getAwardCountThree()));
        response.put("thirdPrizeProbability",String.valueOf(activity.getPrizeRateThree()));
        return response.toJSONString();
    }

    @RequestMapping(path = {"/lotteryLogout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String lotteryLogout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/lottery";
    }

    @RequestMapping(path = {"/lotteryReg"}, method = {RequestMethod.GET})
    public String lotteryReg() {
        return "lotteryReg";
    }

    @RequestMapping(path = {"/lottery"}, method = {RequestMethod.GET})
    public String lotteryIndex(Model model) {
        return "lotteryIndex";
    }

    @RequestMapping(path = {"/userPage/{userId}"}, method = {RequestMethod.GET})
    public String userPage(Model model, @PathVariable("userId") int userId) {
        List<Activity> activities = lotteryService.getActivityByUserId(userId);
        model.addAttribute("activity",activities);
        List<JoinIn> joinIns=lotteryService.getResultByUserId(userId);
        List<String[]> allAwardInformation=new ArrayList<>();
        for(JoinIn joinIn:joinIns){
            int result=joinIn.getResult();
            if(result==0) continue;
            String[] awardInformation=new String[5];
            Activity activity=findActivityById(activities,joinIn.getActId());
            awardInformation[0]=activity.getActDescribe();
            if(result==1){
                awardInformation[1]="一等奖";
                awardInformation[2]=activity.getAwardNameOne();
            }
            else if(result==2){
                awardInformation[1]="二等奖";
                awardInformation[2]=activity.getAwardNameTwo();
            }
            else if(result==3){
                awardInformation[1]="三等奖";
                awardInformation[2]=activity.getAwardNameThree();
            }
            awardInformation[3]= new SimpleDateFormat("yyyy-MM-dd").format(activity.getStartDate());
            awardInformation[4]=String.valueOf(activity.getActId());
            allAwardInformation.add(awardInformation);
        }
        model.addAttribute("award",allAwardInformation);
        return "lotteryUser";
    }

    @RequestMapping(path = {"/userPage"}, method = {RequestMethod.GET})
    public String autoUserPage(Model model) {
        if(hostHolder.getUser()==null) return "lotteryIndex";
        int userId = hostHolder.getUser().getId();
        if (userId == 13) return "redirect:/adminPage/13";
        return "redirect:/userPage/" + userId;
    }

    @RequestMapping(path = {"/adminPage/{adminId}"}, method = {RequestMethod.GET})
    public String index(Model model, @PathVariable("adminId") int adminId) {
        model.addAttribute("activity", lotteryService.getActivityByAdminId(adminId));
        return "lotteryAdmin";
    }

    @RequestMapping(path = {"/addActivity"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addActivity(
            @RequestParam("ActDescribe") String ActDescribe,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("firstPrizeName") String firstPrizeName,
            @RequestParam("firstAwardName") String firstAwardName,
            @RequestParam("firstPrizeAmount") int firstPrizeAmount,
            @RequestParam("firstPrizeProbability") double firstPrizeProbability,
            @RequestParam("secondPrizeName") String secondPrizeName,
            @RequestParam("secondAwardName") String secondAwardName,
            @RequestParam("secondPrizeAmount") int secondPrizeAmount,
            @RequestParam("secondPrizeProbability") double secondPrizeProbability,
            @RequestParam("thirdPrizeName") String thirdPrizeName,
            @RequestParam("thirdAwardName") String thirdAwardName,
            @RequestParam("thirdPrizeAmount") int thirdPrizeAmount,
            @RequestParam("thirdPrizeProbability") double thirdPrizeProbability,
            @RequestParam("userList[]") ArrayList<Integer> userList
    ) {
        DateFormat dateFormat=new SimpleDateFormat ("yyyy-MM-dd hh:mm");
        Activity activity = new Activity();
        activity.setActDescribe(ActDescribe);
        activity.setAdminId(hostHolder.getUser().getId());
        try {
            activity.setStartDate(dateFormat.parse(startTime));
            activity.setEndDate(dateFormat.parse(endTime));
        }
        catch (Exception e){
            logger.error("日期格式出错"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"日期异常");
        }
        activity.setPrizeNameOne(firstPrizeName);
        activity.setPrizeNameTwo(secondPrizeName);
        activity.setPrizeNameThree(thirdPrizeName);
        activity.setAwardNameOne(firstAwardName);
        activity.setAwardCountOne(firstPrizeAmount);
        activity.setPrizeRateOne(firstPrizeProbability);
        activity.setAwardNameTwo(secondAwardName);
        activity.setAwardCountTwo(secondPrizeAmount);
        activity.setPrizeRateTwo(secondPrizeProbability);
        activity.setAwardNameThree(thirdAwardName);
        activity.setAwardCountThree(thirdPrizeAmount);
        activity.setPrizeRateThree(thirdPrizeProbability);
        lotteryService.addActivity(activity);
        int actId=activity.getActId();
        for(int i:userList){
            JoinIn joinIn=new JoinIn();
            joinIn.setActId(actId);
            joinIn.setResult(0);
            joinIn.setUserId(i);
            lotteryService.addJoinIn(joinIn);
        }
        return ToutiaoUtil.getJSONString(0);
    }

    Activity findActivityById(List<Activity> activities,int id){
        for(Activity activity:activities){
            if(activity.getActId()==id) return activity;
        }
        return null;
    }
}
