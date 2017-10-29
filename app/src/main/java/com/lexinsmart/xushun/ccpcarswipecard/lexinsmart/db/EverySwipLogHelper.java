package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db;

import android.content.Context;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.EverySwipLogEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;

import io.realm.Realm;

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
     * @param everySwipLogEntity
     */
    public void addSwipLog(EverySwipLogEntity everySwipLogEntity){
        mRealm.beginTransaction();
        mRealm.copyToRealm(everySwipLogEntity);
        mRealm.commitTransaction();
    }

    public void close() {
        if (mRealm != null){
            mRealm.close();
        }
    }
}
