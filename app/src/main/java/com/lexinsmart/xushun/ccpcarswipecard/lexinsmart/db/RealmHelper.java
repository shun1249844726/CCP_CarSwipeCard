package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db;

import android.content.Context;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by xushun on 2017/10/25.
 * 功能描述：
 * 心情：
 */

public class RealmHelper {
    public static final String DB_NAME = "swiplog.realm";

    private Realm mRealm;
    public RealmHelper(Context context){
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * add
     * @param swipCardLog
     */
    public void addSwipLog(SwipCardLog swipCardLog){
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(swipCardLog);
        mRealm.commitTransaction();
    }

    /**
     * 更新
     * @param swipCardLog
     */
    public void updateSwipLog(SwipCardLog swipCardLog){
        SwipCardLog log = mRealm.where(SwipCardLog.class).equalTo("cardnum",swipCardLog.getCardnum()).findFirst();

        mRealm.beginTransaction();
        log.setName(swipCardLog.getName());
        log.setStaffnum(swipCardLog.getStaffnum());
        log.setGetOnTime(swipCardLog.getGetOnTime());
        log.setGetOffTime(swipCardLog.getGetOffTime());
        log.setGetOnFlag(swipCardLog.isGetOnFlag());
        log.setSwipCount(swipCardLog.getSwipCount());
        log.setGetUpOrOff(swipCardLog.isGetUpOrOff());
        mRealm.commitTransaction();
    }

    /**
     * 根据物理卡号查询是否存在
     * @param cardNumber
     * @return
     */
    public boolean isLogExist(String cardNumber){
        SwipCardLog log = mRealm.where(SwipCardLog.class).equalTo("cardnum",cardNumber).findFirst();
        if (log == null){
            return false;
        }else {
            return true;
        }

    }
    public SwipCardLog qurryLogByCardNum(String cardnumber){

        SwipCardLog log = mRealm.where(SwipCardLog.class).equalTo("cardnum",cardnumber).findFirst();

        return log;
    }

    public int getSwipCount (String cardnumber){
        SwipCardLog log = mRealm.where(SwipCardLog.class).equalTo("cardnum",cardnumber).findFirst();
        int count = 0;

        if (log == null){
            count = 0;
        }else {
            count =log.getSwipCount();
        }
        return count;
    }

    public void clearAll(){
        mRealm.beginTransaction();
        RealmResults realmResults = mRealm.where(SwipCardLog.class).findAll();
        realmResults.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
    public void close(){
        if (mRealm != null){
            mRealm.close();
        }
    }

    public int getIncarCount() {
        RealmResults<SwipCardLog> logs = mRealm.where(SwipCardLog.class)
                .equalTo("getOnFlag",true)
                .findAll();
        return logs.size();
    }
}
