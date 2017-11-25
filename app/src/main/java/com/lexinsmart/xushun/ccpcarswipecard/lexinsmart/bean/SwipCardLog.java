package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

import java.sql.Timestamp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xushun on 2017/10/26.
 * 功能描述：
 * 心情：
 */

public class SwipCardLog extends RealmObject{
    private String name;
    @PrimaryKey
    private String cardnum;
    private String staffnum;
    private String company;
    private String getOnTime;
    private String getOffTime;
    private boolean getOnFlag;
    private int swipCount;
    private boolean getUpOrOff;

    public boolean isGetUpOrOff() {
        return getUpOrOff;
    }

    public void setGetUpOrOff(boolean getUpOrOff) {
        this.getUpOrOff = getUpOrOff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getStaffnum() {
        return staffnum;
    }

    public void setStaffnum(String staffnum) {
        this.staffnum = staffnum;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public String getGetOnTime() {
        return getOnTime;
    }

    public void setGetOnTime(String getOnTime) {
        this.getOnTime = getOnTime;
    }

    public String getGetOffTime() {
        return getOffTime;
    }

    public void setGetOffTime(String getOffTime) {
        this.getOffTime = getOffTime;
    }

    public boolean isGetOnFlag() {
        return getOnFlag;
    }

    public void setGetOnFlag(boolean getOnFlag) {
        this.getOnFlag = getOnFlag;
    }

    public int getSwipCount() {
        return swipCount;
    }

    public void setSwipCount(int swipCount) {
        this.swipCount = swipCount;
    }
}
