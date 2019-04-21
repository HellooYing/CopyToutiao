package com.toutiao.model;

import java.util.Date;

public class Activity {
    //Entity of activity
    private int actId;
    private int adminId;
    private Date startDate;
    private Date endDate;
    private String actDescribe;
    private String prizeNameOne;
    private String prizeNameTwo;
    private String prizeNameThree;
    private String awardNameOne;
    private String awardNameTwo;
    private String awardNameThree;
    private int awardCountOne;
    private int awardCountTwo;
    private int awardCountThree;
    private double prizeRateOne;
    private double prizeRateTwo;
    private double prizeRateThree;

    public Activity() {

    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActDescribe() {
        return actDescribe;
    }

    public void setActDescribe(String actDescribe) {
        this.actDescribe = actDescribe;
    }

    public String getPrizeNameOne() {
        return prizeNameOne;
    }

    public void setPrizeNameOne(String prizeNameOne) {
        this.prizeNameOne = prizeNameOne;
    }

    public String getPrizeNameTwo() {
        return prizeNameTwo;
    }

    public void setPrizeNameTwo(String prizeNameTwo) {
        this.prizeNameTwo = prizeNameTwo;
    }

    public String getPrizeNameThree() {
        return prizeNameThree;
    }

    public void setPrizeNameThree(String prizeNameThree) {
        this.prizeNameThree = prizeNameThree;
    }

    public String getAwardNameOne() {
        return awardNameOne;
    }

    public void setAwardNameOne(String awardNameOne) {
        this.awardNameOne = awardNameOne;
    }

    public String getAwardNameTwo() {
        return awardNameTwo;
    }

    public void setAwardNameTwo(String awardNameTwo) {
        this.awardNameTwo = awardNameTwo;
    }

    public String getAwardNameThree() {
        return awardNameThree;
    }

    public void setAwardNameThree(String awardNameThree) {
        this.awardNameThree = awardNameThree;
    }

    public int getAwardCountOne() {
        return awardCountOne;
    }

    public void setAwardCountOne(int awardCountOne) {
        this.awardCountOne = awardCountOne;
    }

    public int getAwardCountTwo() {
        return awardCountTwo;
    }

    public void setAwardCountTwo(int awardCountTwo) {
        this.awardCountTwo = awardCountTwo;
    }

    public int getAwardCountThree() {
        return awardCountThree;
    }

    public void setAwardCountThree(int awardCountThree) {
        this.awardCountThree = awardCountThree;
    }

    public double getPrizeRateOne() {
        return prizeRateOne;
    }

    public void setPrizeRateOne(double prizeRateOne) {
        this.prizeRateOne = prizeRateOne;
    }

    public double getPrizeRateTwo() {
        return prizeRateTwo;
    }

    public void setPrizeRateTwo(double prizeRateTwo) {
        this.prizeRateTwo = prizeRateTwo;
    }

    public double getPrizeRateThree() {
        return prizeRateThree;
    }

    public void setPrizeRateThree(double prizeRateThree) {
        this.prizeRateThree = prizeRateThree;
    }
}
