package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db;

import android.content.Context;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.InfoModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by xushun on 2017/11/1.
 * 功能描述：
 * 心情：
 */

public class StaffInfoHelper {
    private Realm mRealm;
    public static final String STAFF_DB_NAME = "staffinfo.realm";

    public StaffInfoHelper(Context context) {


        File directory = context.getExternalFilesDir(null);
        directory.mkdirs();


        RealmConfiguration configuration=new RealmConfiguration.Builder()
                .name(STAFF_DB_NAME)
                .directory(directory)
                .deleteRealmIfMigrationNeeded()
                .build();

        mRealm = Realm.getInstance(configuration);
    }

    public void addInfos(ArrayList<InfoModel> infosList) {
        mRealm.beginTransaction();
        for (int i = 0; i < infosList.size(); i++) {
            mRealm.copyToRealmOrUpdate(infosList.get(i));
        }
        mRealm.commitTransaction();
    }
    public void addinfo(InfoModel infoModel) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(infoModel);
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

    public void clearAll() {
        mRealm.beginTransaction();
        RealmResults realmResults = mRealm.where(InfoModel.class).findAll();
        realmResults.deleteAllFromRealm();
        mRealm.commitTransaction();
    }
}
