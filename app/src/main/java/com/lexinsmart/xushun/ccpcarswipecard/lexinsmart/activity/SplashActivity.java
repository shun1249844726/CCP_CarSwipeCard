package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.Manifest;
import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.MainActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.InfoModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.StaffInfoHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.AppUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.CheckIfNeedUpdate;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.OnGetVersionListener;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update.UpdateChecker;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.UiUtils;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jxl.Sheet;
import jxl.Workbook;

import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.VERSIONCODE;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.VERSIONNAME;

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
    @BindView(R.id.tv_loding_config)
    TextView mTvLodingConfig;
//    private SmdtManager mSmdtManager;

    private String TAG = "Splash";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        mAvi.smoothToShow();
        VERSIONNAME = AppUtils.getVersionName(mContext);
        VERSIONCODE =""+AppUtils.getVersionCode(mContext);

        mTvAppversion.setText("软件版本：" + VERSIONNAME);

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
        StaffInfoHelper staffInfoHelper = new StaffInfoHelper(this);
        if (staffInfoHelper.getAllCount() == 0){
            new ExcelDataLoader().execute("info.xls");
            System.out.println("添加excel 数据");
        }else {
            System.out.println("已经有了，不用更新数据了");

        }
        staffInfoHelper.close();
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

                            mTvLodingConfig.setText("等待下载更新！");
                            mBtnGetInApp.setVisibility(View.GONE);

                        } else {
                            mAvi.hide();
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
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

        mTvLodingConfig.setText("加载完成，等待进入主页！");


    }

    @OnClick(R.id.btn_getInApp)
    public void getInApp() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

//        mSmdtManager = new SmdtManager(getApplicationContext());
//        mSmdtManager.smdtSetStatusBar(getApplicationContext(), false);
//        UiUtils.hideNavigate(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mSmdtManager.smdtSetStatusBar(getApplicationContext(), true);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    private ArrayList<InfoModel> getXlsData(String xlsName, int index) {
        ArrayList<InfoModel> infosList = new ArrayList<InfoModel>();
        AssetManager assetManager = getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 0; i < sheetRows; i++) {
                InfoModel countryModel = new InfoModel();
                countryModel.setName(sheet.getCell(0, i).getContents());
                countryModel.setCardnumber(sheet.getCell(1, i).getContents());
                countryModel.setStaffnumber(sheet.getCell(2, i).getContents());

                infosList.add(countryModel);
            }

            workbook.close();
//            Gson gson = new Gson();
//            String sss = gson.toJson(countryList);
//            Logger.json(sss);

            StaffInfoHelper staffInfoHelper = new StaffInfoHelper(this);

            staffInfoHelper.addInfos(infosList);
            staffInfoHelper.close();



        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return infosList;
    }

    private class ExcelDataLoader extends AsyncTask<String, Void, ArrayList<InfoModel>> {

        @Override
        protected void onPreExecute() {
//            progressDialog.setMessage("加载中,请稍后......");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected ArrayList<InfoModel> doInBackground(String... params) {
            return getXlsData(params[0], 0);
        }

        @Override
        protected void onPostExecute(ArrayList<InfoModel> countryModels) {

            if(countryModels != null && countryModels.size()>0){
                //存在数据
//                sortByName(countryModels);
//                setupData(countryModels);
            }else {
                //加载失败


            }

        }
    }

}
