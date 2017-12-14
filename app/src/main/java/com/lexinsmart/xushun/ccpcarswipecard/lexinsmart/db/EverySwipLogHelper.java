package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db;

import android.content.Context;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.EverySwipLogEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class EverySwipLogHelper {

    private Realm mRealm;

    public EverySwipLogHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * add
     *
     * @param everySwipLogEntity
     */
    public void addSwipLog(EverySwipLogEntity everySwipLogEntity) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(everySwipLogEntity);
        mRealm.commitTransaction();
    }

    public String getLastLogByCardNo(String cardno) {
        RealmResults<EverySwipLogEntity> logs = mRealm.where(EverySwipLogEntity.class).equalTo("cardnumber",cardno).findAll();

        System.out.println("result size:"+logs.size());
        if (logs == null || logs.size() ==0) {
            return null;
        } else {
            logs = logs.sort("swipcartime", Sort.DESCENDING);

            return logs.get(0).getSwipcartime();
        }
    }

    public  RealmResults<EverySwipLogEntity> getAllLog(){
        RealmResults<EverySwipLogEntity> logs = mRealm.where(EverySwipLogEntity.class).findAll();
        if (logs == null || logs.size() ==0) {
            return null;
        } else {
            logs = logs.sort("swipcartime", Sort.DESCENDING);
            return logs;
        }
    }
    public void clearAll(){
        mRealm.beginTransaction();
        RealmResults realmResults = mRealm.where(EverySwipLogEntity.class).findAll();
        realmResults.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
    public void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
