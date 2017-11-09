package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.app.Activity;
import android.app.smdt.SmdtManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.ui.StaffListAdapter;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.UiUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by xushun on 2017/11/9.
 * 功能描述：
 * 心情：
 */

public class StaffListActivity extends Activity {
    ListView mLvStaffList;
    StaffListAdapter mStaffListAdapter;
    List<SwipCardLog> mSwipCardLogs = new ArrayList<>();
    private SmdtManager mSmdtManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflist);

        mLvStaffList = (ListView) findViewById(R.id.lv_staff_list);

        RealmHelper realmHelper = new RealmHelper(this);
        RealmResults<SwipCardLog> allResults = realmHelper.getAllLog();

        if (allResults != null) {
            for (int i = 0; i < allResults.size(); i++) {
                mSwipCardLogs.add(allResults.get(i));
            }
            mStaffListAdapter = new StaffListAdapter(this, mSwipCardLogs);
            mLvStaffList.setAdapter(mStaffListAdapter);
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        mSmdtManager = new SmdtManager(getApplicationContext());
        mSmdtManager.smdtSetStatusBar(getApplicationContext(), false);
        UiUtils.hideNavigate(this);
    }
    @Override
    protected void onStop() {
        super.onStop();

        mSmdtManager.smdtSetStatusBar(getApplicationContext(), true);
        System.out.println("onstop");
    }
}
