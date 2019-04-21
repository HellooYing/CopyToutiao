package com.toutiao;

import com.toutiao.controller.IndexController;
import com.toutiao.dao.LotteryDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.Activity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@WebAppConfiguration
public class ToutiaoApplicationTests {
	@Autowired
	LotteryDAO lotteryDAO;
	@Test
	public void testLotteryDAO() {
		Activity activity = new Activity();
		activity.setActDescribe("");
		activity.setAdminId(13);
		activity.setStartDate(new Date());
		Date endDate = new Date();
		endDate.setTime(endDate.getTime() + 1000 * 3600 * 72);
		activity.setEndDate(endDate);
		activity.setAwardNameOne("123");
		activity.setAwardCountOne(123);
		activity.setPrizeRateOne(123);
		activity.setPrizeNameOne("123");
		activity.setAwardNameTwo("123");
		activity.setAwardCountTwo(123);
		activity.setPrizeRateTwo(123);
		activity.setPrizeNameTwo("123");
		activity.setAwardNameThree("123");
		activity.setAwardCountThree(123);
		activity.setPrizeRateThree(123);
		activity.setPrizeNameThree("123");
		lotteryDAO.addActivity(activity);
		//Assert.assertEquals(activity.getActId(),7);


	}
}