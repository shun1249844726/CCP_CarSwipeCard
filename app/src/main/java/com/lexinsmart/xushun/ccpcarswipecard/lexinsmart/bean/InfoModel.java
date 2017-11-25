package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xushun on 2017/11/1.
 * 功能描述：
 * 心情：
 */

public class InfoModel extends RealmObject{
    private String name ;
    @PrimaryKey
    private String cardnumber;
    private String staffnumber;
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getStaffnumber() {
        return staffnumber;
    }

    public void setStaffnumber(String staffnumber) {
        this.staffnumber = staffnumber;
    }
}
