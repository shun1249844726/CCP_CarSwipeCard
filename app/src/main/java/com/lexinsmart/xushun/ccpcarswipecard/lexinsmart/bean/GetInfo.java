package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

import java.sql.Timestamp;

/**
 * Created by xushun on 2017/10/26.
 * 功能描述：
 * 心情：
 */

public class GetInfo {
    private String success;
    private String name;
    private String staffNum;
    private String cardNum;
    private String checkintime;
    private String checkouttime;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkouttime) {
        this.checkouttime = checkouttime;
    }
}
