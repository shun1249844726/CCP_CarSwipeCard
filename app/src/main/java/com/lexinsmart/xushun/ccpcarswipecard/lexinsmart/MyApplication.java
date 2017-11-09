package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart;

import android.app.Application;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by xushun on 2017/10/25.
 * 功能描述：
 * 心情：
 */

public class MyApplication  extends Application {
    private String TAG = "CCP_Logger";
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        File dir = new File(getExternalFilesDir(null)+"/realm");
        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder()
                .name(str+RealmHelper.DB_NAME)
                .directory(dir)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        Logger.init(TAG);
    }
}
