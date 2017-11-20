package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart;

import android.app.Application;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.io.File;
import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by xushun on 2017/10/25.
 * 功能描述：
 * 心情：
 */

public class MyApplication extends Application {
    private String TAG = "CCP_Logger";
    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        String str = sdf.format(date);
        File dir = new File(getExternalFilesDir(null) + "/realm");
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(str + RealmHelper.DB_NAME)
                .directory(dir)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        //Logger 初始化
        Logger.init(TAG);

        //初始化hotfix
        initHotfix();
    }
    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            System.out.println("version:"+appVersion);
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" \nCode:").append(code)
                                .append(" \nInfo:").append(info)
                                .append(" \nHandlePatchVersion:").append(handlePatchVersion).toString();
                        if (msgDisplayListener != null) {
                            msgDisplayListener.handle(msg);
                        } else {
                            cacheMsg.append("\n").append(msg);
                        }

                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            System.out.println("补丁加载ok.!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();

    }
}
