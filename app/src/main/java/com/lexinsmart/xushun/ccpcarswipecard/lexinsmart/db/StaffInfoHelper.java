package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db;

import android.content.Context;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.InfoModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by xushun on 2017/11/1.
 * 功能描述：
 * 心情：
 */

public class StaffInfoHelper {
    private Realm mRealm;

    public StaffInfoHelper(Context context) {
        mRealm = Realm.getDefaultInstance();
    }

    public void addInfos(ArrayList<InfoModel> infosList) {
        mRealm.beginTransaction();
        for (int i = 0; i < infosList.size(); i++) {
            mRealm.copyToRealmOrUpdate(infosList.get(i));
        }
        mRealm.commitTransaction();
    }

    public void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }
    public int  getAllCount(){
        RealmResults<InfoModel> infos = mRealm.where(InfoModel.class)
                .findAll();

        if (infos == null){
            return 0;
        }else {
            return infos.size();
        }
    }
    public InfoModel getInfoByCardno(String cardno){
        InfoModel infoModel = mRealm.where(InfoModel.class).equalTo("cardnumber",cardno).findFirst();

        if(infoModel == null){
            return null;
        }else {
            return infoModel;
        }
    }
}
