package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

import android.content.SharedPreferences;
import android.view.View;

import java.io.File;

/**
 * Created by xushun on 2017/11/7.
 * 功能描述：
 * 心情：
 */

public class FileUtils {

    public  static  void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }



}
