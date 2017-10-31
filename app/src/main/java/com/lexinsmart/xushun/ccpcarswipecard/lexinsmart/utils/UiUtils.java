package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Created by xushun on 2017/10/31.
 * 功能描述：
 * 心情：
 */

public class UiUtils {
    // 隐藏导航栏
    public static void hideNavigate(Activity activity) {
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        if (Build.MODEL.contains("rk30sdk")) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        }

        // This work only for android 4.4+
//  if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
//  {

        activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        final View decorView = activity.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOptions);
                }
            }
        });
    }
}
