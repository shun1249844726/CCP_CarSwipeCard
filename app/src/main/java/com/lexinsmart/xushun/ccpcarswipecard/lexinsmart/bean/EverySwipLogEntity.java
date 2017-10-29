package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

import io.realm.RealmObject;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class EverySwipLogEntity extends RealmObject {
    private String cardnumber;
    private String swipcartime;
    private String latitude;
    private String longitude;

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getSwipcartime() {
        return swipcartime;
    }

    public void setSwipcartime(String swipcartime) {
        this.swipcartime = swipcartime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
