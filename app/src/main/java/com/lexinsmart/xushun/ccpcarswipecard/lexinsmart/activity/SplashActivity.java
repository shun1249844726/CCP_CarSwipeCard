package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.Manifest;
import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.MainActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.AppUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.CheckIfNeedUpdate;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.OnGetVersionListener;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.UpdateChecker;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.UiUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xushun on 2017/10/28.
 * 功能描述：
 * 心情：
 */

public class SplashActivity extends AppCompatActivity {
    Context mContext;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mAvi;
    @BindView(R.id.btn_getInApp)
    Button mBtnGetInApp;
    @BindView(R.id.tv_appversion)
    TextView mTvAppversion;


    private SmdtManager mSmdtManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        mAvi.show();
        mTvAppversion.setText(AppUtils.getVersionName(mContext));

        TelephonyManager telephonemanage = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Constant.IMEI = telephonemanage.getDeviceId();

        } catch (Exception e) {
            Log.i("error", e.getMessage());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBtnGetInApp.setVisibility(View.VISIBLE);
                CheckIfNeedUpdate checkIfNeedUpdate = new CheckIfNeedUpdate(mContext);
                checkIfNeedUpdate.setOnGetVeersionListener(new OnGetVersionListener() {
                    @Override
                    public void onDataSuccessfully(Object data) {
                        System.out.println("onDataSuccessfully :" + data);
                        if ((boolean) data) {
                            UpdateChecker.checkForDialog(SplashActivity.this);

                        } else {
//                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onDataFailed() {
                        System.out.println("onDataFailed");
                    }
                });
                checkIfNeedUpdate.execute();
            }
        }, 2000);

    }
    @OnClick(R.id.btn_getInApp)
    public void getInApp(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mSmdtManager = new SmdtManager(getApplicationContext());
        mSmdtManager.smdtSetStatusBar(getApplicationContext(),false);
        UiUtils.hideNavigate(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSmdtManager.smdtSetStatusBar(getApplicationContext(),true);
    }
}
