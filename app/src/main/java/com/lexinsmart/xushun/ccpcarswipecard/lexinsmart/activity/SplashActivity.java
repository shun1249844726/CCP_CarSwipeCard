package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.MainActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.AppUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.CheckIfNeedUpdate;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.OnGetVersionListener;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.UpdateChecker;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        mAvi.show();
        mTvAppversion.setText(AppUtils.getVersionName(mContext));

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
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
}
