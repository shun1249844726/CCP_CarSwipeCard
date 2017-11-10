package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.InfoModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.StaffInfoHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by xushun on 2017/11/1.
 * 功能描述：
 * 心情：
 */

public class ImportDataActivity extends CheckPermissionsActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_userdata);

    }
}
