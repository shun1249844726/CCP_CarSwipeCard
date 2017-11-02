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
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}