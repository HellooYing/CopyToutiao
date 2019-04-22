package com.toutiao.dao;

import com.toutiao.model.Activity;
import com.toutiao.model.JoinIn;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LotteryDAO {
    String INSERT_FIELDS ="admin_id,start_date,end_date,act_describe,prize_name_one,prize_name_two ,prize_name_three,award_name_one,award_name_two,award_name_three,award_count_one,award_count_two,award_count_three,prize_rate_one,prize_rate_two, prize_rate_three";
    String TABLE_NAME = "activity";
    String SELECT_FIELDS = " act_id, "+INSERT_FIELDS;

    //根据userid，获取activity[]
    @Select("select a.act_id,a.admin_id,a.start_date,a.end_date,a.act_describe,a.prize_name_one,a.prize_name_two ,a.prize_name_three,a.award_name_one,a.award_name_two,a.award_name_three,a.award_count_one,a.award_count_two,a.award_count_three,a.prize_rate_one,a.prize_rate_two, a.prize_rate_three from (select act_id from join_in where user_id =#{userId})b left join activity as a on a.act_id = b.act_id order by start_date desc")
    List<Activity> selectActivityByUserId(@Param("userId")int userId);

    //根据adminid，获取activity[]
    @Select("select "+SELECT_FIELDS+" from activity where admin_id = #{adminId} order by start_date desc")
    List<Activity> selectActivityByAdminId(@Param("adminId")int adminId);

    //根据活动id和用户id获取中奖结果
    @Select("select result from join_in where user_id = #{userId} and act_id = #{actId}")
    int selectResultByActivityIdAndUserId(@Param("userId")int userId,@Param("actId")int actId);

    //根据活动id获取该活动全部中奖结果
    @Select("select * from join_in where act_id = #{actId}")
    List<JoinIn> selectResultByActivityId(@Param("actId")int actId);

    //根据用户id获取该用户全部中奖结果
    @Select("select * from join_in where user_id = #{userId} order by act_id desc")
    List<JoinIn> selectResultByUserId(@Param("userId")int userId);

    //根据活动id获取活动
    @Select("select * from activity where act_id = #{actId} order by start_date desc")
    Activity selectActivityById(@Param("actId")int actId);

    //增加活动(恐怖的字段)
    int addActivity(Activity activity);

    //记录参与情况(导入人员信息时)
    @Insert("insert into join_in(user_id, act_id, result) values(#{userId},#{actId},#{result})")
    int addJoinIn(JoinIn joinIn);

    //更新中奖结果(根据用户ID和活动ID)
    @Update("update join_in set result = #{result} where user_id = #{userId} and act_id = #{actId}")
    int updateResult(@Param("userId")int userId,@Param("actId")int actId,@Param("result")int result);

    @Update("update activity set award_count_one = #{countOne},award_count_two = #{countTwo},award_count_three = #{countThree} where act_id = #{actId}")
    int updateAwardCount(@Param("actId")int actId,@Param("countOne")int countOne,@Param("countTwo")int countTwo,@Param("countThree")int countThree);
}
