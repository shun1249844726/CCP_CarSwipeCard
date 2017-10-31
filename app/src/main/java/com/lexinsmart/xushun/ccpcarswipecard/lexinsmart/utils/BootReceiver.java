package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.MainActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity.SplashActivity;

import java.util.logging.Logger;

/**
 * Created by wanglj on 17/3/2.
 * 设备开机，5秒后程序自启动
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {


        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.e("BootReceiver", "5秒后程序自动启动");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent bootIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()); // 要启动的Activity
                    bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(bootIntent);
                }
            }, 5000);
        }
    }
}
