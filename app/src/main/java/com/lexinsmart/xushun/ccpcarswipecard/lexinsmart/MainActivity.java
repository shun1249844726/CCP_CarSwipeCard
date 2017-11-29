package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.smdt.SmdtManager;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.dk.bleNfc.BleManager.BleManager;
import com.dk.bleNfc.BleManager.Scanner;
import com.dk.bleNfc.BleManager.ScannerCallback;
import com.dk.bleNfc.BleNfcDeviceService;
import com.dk.bleNfc.DeviceManager.BleNfcDevice;
import com.dk.bleNfc.DeviceManager.ComByteManager;
import com.dk.bleNfc.DeviceManager.DeviceManager;
import com.dk.bleNfc.DeviceManager.DeviceManagerCallback;
import com.dk.bleNfc.Exception.DeviceNoResponseException;
import com.dk.bleNfc.Tool.StringTool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity.CheckPermissionsActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity.StaffListActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.AckDeviceInfoEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.AckRequireOk;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.AppVersionEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.DeviceInfoEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.EverySwipLogEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.InfoModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.RefreshTimeEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.RequireInfoEntity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.StsModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.EverySwipLogHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.StaffInfoHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.mqtt.MqttV3Service;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.ProgressCallback;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.PutObjectSamples;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.StsTokenSamples;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.CardUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.DateUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.ExcelUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.FileUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.JsonUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.StringUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.UiUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.toasty.Toasty;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.FAIL;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.IMEI;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.STS_TOKEN_SUC;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_Fail;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_PROGRESS;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_SUC;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.VERSIONCODE;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.VERSIONNAME;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.title0;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.title1;

/**
 * Created by xushun on 2017/10/25.
 * 功能描述：
 * 心情：
 */

public class MainActivity extends CheckPermissionsActivity implements LocationSource, AMapLocationListener {
    BleNfcDeviceService mBleNfcDeviceService;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sp_type_select)
    Spinner mSpTypeSelect;
    @BindView(R.id.tv_power)
    TextView mTvPower;
    @BindView(R.id.main_tv_name)
    TextView mMainTvName;
    @BindView(R.id.main_tv_staffNum)
    TextView mMainTvStaffNum;
    @BindView(R.id.cardview1)
    CardView mCardview1;
    @BindView(R.id.cardview2)
    CardView mCardview2;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.tv_nowCount)
    TextView mTvNowCount;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.img_bluetooth_status)
    ImageView mImgBluetoothStatus;
    @BindView(R.id.tv_bluetooth)
    TextView mTvBluetooth;
    @BindView(R.id.ll_bluetooth_status)
    LinearLayout mLlBluetoothStatus;
    @BindView(R.id.img_net_status)
    ImageView mImgNetStatus;
    @BindView(R.id.tv_net_status)
    TextView mTvNetStatus;
    @BindView(R.id.ll_net_status)
    LinearLayout mLlNetStatus;
    @BindView(R.id.ll_device_status)
    LinearLayout mLlDeviceStatus;
    @BindView(R.id.img_gps_status)
    ImageView mImgGpsStatus;
    @BindView(R.id.tv_gps_status)
    TextView mTvGpsStatus;
    @BindView(R.id.ll_gps_status)
    LinearLayout mLlGpsStatus;
    @BindView(R.id.ll_main_setting)
    LinearLayout mLlMainSetting;
    @BindView(R.id.ll_info)
    LinearLayout mLlInfo;
    @BindView(R.id.tv_location_result)
    TextView mTvLocationResult;
    private BleNfcDevice bleNfcDevice;
    private Scanner mScanner;
    private ProgressDialog readWriteDialog = null;
    private AlertDialog.Builder alertDialog = null;

    private StringBuffer msgBuffer;
    private BluetoothDevice mNearestBle = null;
    private Lock mNearestBleLock = new ReentrantLock();// 锁对象
    private int lastRssi = -100;
    private CharSequence[] items = null;
    Toolbar toolbar;
    static int NowPeopleCount = 0;
    int NowType = 1;
    Context mContext;


    //map 相关：
    private AMap mAMap;
    private MapView mMapView;
    private Polygon mPolygon;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationClientOption = null;

    private OnLocationChangedListener mListener = null;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private LatLng myLatLng = null;


    //Mqtt
    ArrayList<String> topicList = new ArrayList<String>();
    int Qos = 1;


    String cardNumberString = "";
    boolean goOnOrGooff = false; // 下班 false  上班；true

    private SmdtManager mSmdtManager;


    public static final int KEY_SOUND_A1 = 1;
    public static final int KEY_SOUND_A2 = 2;
    public static final int KEY_SOUND_A3 = 3;
    public static final int KEY_SOUND_A4 = 4;


    SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundPoolMap;


    //Oss
    private PutObjectSamples putObjectSamples;
    private OSS oss;
    private StsTokenSamples stsTokenSamples;
    private static final String testBucket = "ccpswipcard";
    private static String uploadObject = "logs/" + IMEI + "/刷卡详细记录.xls";

    private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String uploadFilePath = "/storage/emulated/0/Android/data/com.lexinsmart.xushun.ccpcarswipecard/files/Record/刷卡详细记录.xls";

    boolean sendToOss = false;
    int getDeviceFlag = 0;

    String DeviceRemarks = IMEI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  //OSS相关
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
        System.out.println("life:" + "onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏


        Date mDate = new Date();   //设置上传到OSS的文件的目录结构   logs/日期/IMEI/时间
        // String dateString = DateUtils.dateToString(mDate, "SHORT");
        String dateString = DateUtils.getStringDate(mDate);
        uploadObject = "logs/" + dateString + "/" + IMEI + "/" + DateUtils.getTimeShort() + ".xls";

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
            }
        });

        msgBuffer = new StringBuffer();
        //ble_nfc服务初始化
        Intent gattServiceIntent = new Intent(this, BleNfcDeviceService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        mMapView = (MapView) findViewById(R.id.mp_fence);
        mMapView.onCreate(savedInstanceState);
        initFence();


        //查询已经有的实时人数
        RealmHelper realmHelper = new RealmHelper(mContext);
        int number = realmHelper.getIncarCount();
        mTvNowCount.setText(number + " 人");
        Logger.d("实时人数：" + number);
        realmHelper.close();


        // MQTT


        TelephonyManager telephonemanage = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        topicList.add("ccp/remote_card/" + IMEI);  //注册刷卡的话题
        topicList.add("ccp/app_version/" + IMEI);
        topicList.add("ccp/device_info/test/" + IMEI);
//        Logger.d("topic:" + topic);

        if (MqttV3Service.mqttAndroidClient == null) {
            new Thread(new MqttProcThread()).start();//如果没有 则连接
            System.out.println("MQTT:创建MQTT连接");
        } else if (!MqttV3Service.isConnected()) {
            try {
                MqttV3Service.mqttAndroidClient.connect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            System.out.println("MQTT:MQTT连接");

        }

        mLlBluetoothStatus.setOnClickListener(llBluetoothClickListener);
        mLlNetStatus.setOnClickListener(llnetClickListener);


        //语音提示
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                System.out.println("sampleId" + sampleId + "  " + status);
            }

        });
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(KEY_SOUND_A1, mSoundPool.load(this, R.raw.checkstatus, 1));
        soundPoolMap.put(KEY_SOUND_A2, mSoundPool.load(this, R.raw.ackok, 1));
        soundPoolMap.put(KEY_SOUND_A3, mSoundPool.load(this, R.raw.ackfail, 1));
        soundPoolMap.put(KEY_SOUND_A4, mSoundPool.load(this, R.raw.donotswip, 1));



    }

    /**
     * 初始化STS
     */
    private void initStsData() {
        //get sts token
        stsTokenSamples = new StsTokenSamples(stsHandler);
        stsTokenSamples.getStsTokenAndSet();
    }

    /**
     * sts 的handler  处理。
     */
    private Handler stsHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            boolean handled = false;
            switch (msg.what) {
                case UPLOAD_SUC:
                    Toasty.success(MainActivity.this, "上传文件成功！!", Toast.LENGTH_SHORT, true).show();
                    handled = true;
                    break;
                case FAIL:
                    Toasty.error(MainActivity.this, "上传文件失败！!", Toast.LENGTH_SHORT, true).show();
                    handled = true;
                    break;
                case STS_TOKEN_SUC:
               //     Toasty.success(MainActivity.this, "获取认证成功！!", Toast.LENGTH_SHORT, true).show();
                    StsModel response = (StsModel) msg.obj;
                    setOssClient(response.Credentials.AccessKeyId, response.Credentials.AccessKeySecret, response.Credentials.SecurityToken);
                    System.out.println("STS:-------StsToken.Expiration-------" + response.Credentials.Expiration);
                    System.out.println("STS:-------StsToken.AccessKeyId-------" + response.Credentials.AccessKeyId);
                    System.out.println("STS:-------StsToken.SecretKeyId-------" + response.Credentials.AccessKeySecret);
                    System.out.println("STS:-------StsToken.SecurityToken-------" + response.Credentials.SecurityToken);
                    handled = true;


                    System.out.println("STS:获取认证成功：" + sendToOss);
                    if (sendToOss) {

                        System.out.println("STS:checkNotNull(putObjectSamples):" + checkNotNull(putObjectSamples));

                        if (checkNotNull(putObjectSamples)) {
                            putObjectSamples.asyncPutObjectFromLocalFile(new ProgressCallback<PutObjectRequest, PutObjectResult>() {
                                @Override
                                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                                    System.out.println("STS:onprogress" + 100 * currentSize / totalSize);
                                    Message msg = Message.obtain();
                                    msg.what = UPLOAD_PROGRESS;
                                    Bundle data = new Bundle();
                                    data.putLong("currentSize", currentSize);
                                    data.putLong("totalSize", totalSize);
                                    msg.setData(data);
                                    stsHandler.sendMessage(msg);

                                }

                                @Override
                                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                    System.out.println("STS:onsuccess");
                                    stsHandler.sendEmptyMessage(UPLOAD_SUC);
                                }

                                @Override
                                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                                    System.out.println("STS:onFailure");
                                    clientException.printStackTrace();
                                    serviceException.printStackTrace();
                                    stsHandler.sendEmptyMessage(UPLOAD_Fail);

                                }
                            });
                        }
                        sendToOss = false;

                    }
                    break;
            }
            return handled;
        }
    });

    public void setOssClient(String ak, String sk, String token) {
        OSSCredentialProvider mCredentialProvider = null;
        if (mCredentialProvider == null || oss == null) {
            mCredentialProvider = new OSSStsTokenCredentialProvider(ak, sk, token);
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ak, sk, token);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SD_path\OSSLog\logs.csv
            oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
            initSamples();
        } else {
            ((OSSStsTokenCredentialProvider) mCredentialProvider).setAccessKeyId(ak);
            ((OSSStsTokenCredentialProvider) mCredentialProvider).setSecretKeyId(sk);
            ((OSSStsTokenCredentialProvider) mCredentialProvider).setSecurityToken(token);
        }
    }

    private void initSamples() {//初始化OSS的上传的东西
        Date mDate = new Date();
//        String dateString = DateUtils.dateToString(mDate, "SHORT");
        String dateString = DateUtils.getStringDate(mDate);

        uploadObject = "logs/" + dateString + "/" + DeviceRemarks + "/" + DeviceRemarks + "_" + DateUtils.getStringDate_2(mDate) + "_" + DateUtils.getTimeShort() + ".xls";

        putObjectSamples = new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath);
        System.out.println("uploadObject:"+uploadObject);
    }

    View.OnClickListener llnetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO  重连MQTT
            if (!MqttV3Service.isConnected()) {
//                try {
//                    MqttV3Service.client.connect();
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
            }
        }
    };
    View.OnClickListener llBluetoothClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTED)) {
                bleNfcDevice.requestDisConnectDevice();
                System.out.println("BLE:手动断开");
                return;
            }
            searchNearestBleDevice();

        }
    };

    /**
     * 初始化地图
     */
    private void initFence() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            UiSettings settings = mAMap.getUiSettings();
            settings.setRotateGesturesEnabled(false);
            settings.setMyLocationButtonEnabled(true);

            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        // 绘制一个长方形  地里围栏的
        PolygonOptions pOption = new PolygonOptions();
        pOption.add(new LatLng(31.7424590000, 121.0241830000));
        pOption.add(new LatLng(31.7387130000, 121.0214960000));
        pOption.add(new LatLng(31.742051, 121.014358));
        pOption.add(new LatLng(31.746293, 121.016569));


        mPolygon = mAMap.addPolygon(pOption.strokeWidth(4)
                .strokeColor(Color.argb(50, 1, 1, 1))
                .fillColor(Color.argb(50, 1, 1, 1)));

    }

    @OnClick(R.id.btn_submit)
    void submit(View view) {


        File file = new File(getExternalFilesDir(null) + "/Record");
        FileUtils.makeDir(file);
        System.out.println(file.toString());
        ExcelUtils.initExcel(file.toString() + "/刷卡详细记录.xls", title0, title1);
        String fileName = getExternalFilesDir(null) + "/Record/刷卡详细记录.xls";

        System.out.println("结束行程");


        RealmHelper realmHelper = new RealmHelper(mContext);//获取
        RealmResults<SwipCardLog> incarlog = realmHelper.getAllLog();
        ArrayList<ArrayList<String>> incarlogsArray = new ArrayList<>();
        if (incarlog != null) {
            for (int incarlogindex = 0; incarlogindex < incarlog.size(); incarlogindex++) {
                SwipCardLog oneswiplog = incarlog.get(incarlogindex);
                ArrayList<String> oneswiplogbeanlist = new ArrayList<String>();
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getName()));
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getCardnum()));
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getStaffnum()));
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getCompany()));
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getGetOnTime()));
                oneswiplogbeanlist.add(StringUtils.checkIsNull(oneswiplog.getGetOffTime()));
                String inCarFlag = "";
                if (oneswiplog.isGetOnFlag()){
                    inCarFlag = "在";
                }else {
                    inCarFlag = "不在";
                }
                oneswiplogbeanlist.add(StringUtils.checkIsNull(inCarFlag));
                oneswiplogbeanlist.add(StringUtils.checkIsNull("" + oneswiplog.getSwipCount()));

                String onDutyFlag = "";
                if (oneswiplog.isGetUpOrOff()){
                    onDutyFlag = "上班";
                }else {
                    onDutyFlag = "下班";
                }
                oneswiplogbeanlist.add(StringUtils.checkIsNull(onDutyFlag));

                incarlogsArray.add(oneswiplogbeanlist);
            }
//            File file = new File(getExternalFilesDir(null) + "/Record");
//            FileUtils.makeDir(file);
//            System.out.println(file.toString());
//            ExcelUtils.initExcel(file.toString() + "/车载记录.xls", title);
//            String fileName = getExternalFilesDir(null) + "/Record/车载记录.xls";
            ExcelUtils.writeObjListToExcel(1, incarlogsArray, fileName, this);

            sendToOss = true;


            EverySwipLogHelper everySwipLogHelper = new EverySwipLogHelper(mContext);//获取所有列表，转成EXcel 发给服务器
            RealmResults<EverySwipLogEntity> logs = everySwipLogHelper.getAllLog();
            ArrayList<ArrayList<String>> logsArray = new ArrayList<>();
            if (logs != null) {
                int size = logs.size();
                if (size>200){
                    size = 200;
                }
                for (int i = 0; i < size; i++) {   //将从数据库获取的数据，转成list   写入Excel
                    EverySwipLogEntity everySwipLogEntity = logs.get(i);

                    ArrayList<String> logbeanList = new ArrayList<String>();
                    logbeanList.add(everySwipLogEntity.getCardnumber());
                    logbeanList.add(everySwipLogEntity.getSwipcartime());
                    logbeanList.add(everySwipLogEntity.getLatitude());
                    logbeanList.add(everySwipLogEntity.getLongitude());

                    logsArray.add(logbeanList);
                }
                ExcelUtils.writeObjListToExcel(0, logsArray, fileName, this);   //写入sheet0
            }
            initStsData();  //初始化 OSS Sts   获取TOKEN 等信息。

        }

        realmHelper.clearAll();
        realmHelper.close();

        mMainTvStaffNum.setText("— —");
        mMainTvName.setText("— —");
        mTvNowCount.setText("0 人");
        mTvInfo.setText("提示信息");


        if (MqttV3Service.isConnected()) {
            AppVersionEntity versionEntity = new AppVersionEntity();   //发送APPversion信息。
            versionEntity.setCmd_type("app_version");

            AppVersionEntity.CmdContentBean cmdContentBean = new AppVersionEntity.CmdContentBean();
            cmdContentBean.setVersion_name(VERSIONNAME);
            cmdContentBean.setVersion_code(VERSIONCODE);
            cmdContentBean.setDevice_iemi(IMEI);
            versionEntity.setCmd_content(cmdContentBean);

            Gson gson = new Gson();
            String s = gson.toJson(versionEntity);
            Logger.json(s);
            MqttV3Service.publishMsg(s, 0, 1);
        }

    }

    @OnClick(R.id.cardview2)
    void showStaffList() {
        RealmHelper realmHelper = new RealmHelper(mContext);
        if (realmHelper.getAllLog() != null) {

            Intent intent = new Intent(MainActivity.this, StaffListActivity.class);
            startActivity(intent);

        }

    }

    private boolean checkNotNull(Object obj) {
        if (obj != null) {
            return true;
        }
        Toasty.error(MainActivity.this, "init Samples fail!", Toast.LENGTH_SHORT, true).show();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mSmdtManager.smdtSetStatusBar(getApplicationContext(), false);


        int id = item.getItemId();

        if (id == R.id.action_bluetooth) {
            SharedPreferences sharedPreferences = getSharedPreferences("MAC", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();
            editor.commit();
//            Toast.makeText(mContext, "解除蓝牙绑定成功！", Toast.LENGTH_SHORT).show();
            Toasty.success(MainActivity.this, "解除蓝牙绑定成功！", Toast.LENGTH_SHORT, true).show();

        }
//        if (id == R.id.action_settings) {
//
//        }
        if (id == R.id.action_map_showHide) {
            if (mMapView.getVisibility() == View.GONE) {
                mMapView.setVisibility(View.VISIBLE);
                mTvLocationResult.setVisibility(View.VISIBLE);
                mCardview1.setVisibility(View.GONE);
                mCardview2.setVisibility(View.GONE);
                mBtnSubmit.setVisibility(View.GONE);

            } else if (mMapView.getVisibility() == View.VISIBLE) {
                mTvLocationResult.setVisibility(View.GONE);
                mMapView.setVisibility(View.GONE);
                mCardview1.setVisibility(View.VISIBLE);
                mCardview2.setVisibility(View.VISIBLE);
                mBtnSubmit.setVisibility(View.VISIBLE);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BleNfcDeviceService mBleNfcDeviceService = ((BleNfcDeviceService.LocalBinder) service).getService();
            bleNfcDevice = mBleNfcDeviceService.bleNfcDevice;
            mScanner = mBleNfcDeviceService.scanner;
            mBleNfcDeviceService.setDeviceManagerCallback(deviceManagerCallback);
            mBleNfcDeviceService.setScannerCallback(scannerCallback);

            //开始搜索设备
            searchNearestBleDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBleNfcDeviceService = null;
        }
    };
    //Scanner 回调
    private ScannerCallback scannerCallback = new ScannerCallback() {
        @Override
        public void onReceiveScanDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            super.onReceiveScanDevice(device, rssi, scanRecord);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //StringTool.byteHexToSting(scanRecord.getBytes())
                System.out.println("Activity搜到设备：" + device.getName()
                        + " 信号强度：" + rssi
                        + " scanRecord：" + StringTool.byteHexToSting(scanRecord));
            }
            //搜索蓝牙设备并记录信号强度最强的设备
//            if ((scanRecord != null) && (StringTool.byteHexToSting(scanRecord).contains("017f5450")) && device.getName().equals(getImeIlast5(Constant.IMEI))) {  //从广播数据中过滤掉其它蓝牙设备


            if ((scanRecord != null) && (StringTool.byteHexToSting(scanRecord).contains("017f5450")) && isLocalMac(device.getAddress())) {  //从广播数据中过滤掉其它蓝牙设备
                msgBuffer.append("搜到设备：").append(device.getName()).append(" 信号强度：").append(rssi).append("\r\n");
                System.out.println("BLE:" + "搜到设备：" + device.getName() + "  rssi:" + rssi + "   last rssi:" + lastRssi);
                handler.sendEmptyMessage(0);
                if (mNearestBle != null) {
                    System.out.println("BLE:" + "mNearestBle != null");
                    if (rssi > lastRssi) {
                        mNearestBleLock.lock();
                        try {
                            mNearestBle = device;
                        } finally {
                            mNearestBleLock.unlock();
                        }
                    }
                } else {
                    System.out.println("BLE:" + "mNearestBle == null");

                    mNearestBleLock.lock();
                    try {
                        mNearestBle = device;
                    } finally {
                        mNearestBleLock.unlock();
                    }
                    lastRssi = rssi;
                }
            }
        }

        @Override
        public void onScanDeviceStopped() {
            super.onScanDeviceStopped();
        }
    };


    //搜索最近的设备并连接
    private void searchNearestBleDevice() {
        msgBuffer.delete(0, msgBuffer.length());
        msgBuffer.append("正在搜索设备...");
        handler.sendEmptyMessage(0);
        if (!mScanner.isScanning() && (bleNfcDevice.isConnection() == BleManager.STATE_DISCONNECTED)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        mScanner.startScan(0);
                        mNearestBleLock.lock();
                        try {
                            mNearestBle = null;
                        } finally {
                            mNearestBleLock.unlock();
                        }
                        lastRssi = -100;

                        int searchCnt = 0;
                        while ((mNearestBle == null)
                                && (searchCnt < 10000)
                                && (mScanner.isScanning())
                                && (bleNfcDevice.isConnection() == BleManager.STATE_DISCONNECTED)) {
                            searchCnt++;
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (mScanner.isScanning() && (bleNfcDevice.isConnection() == BleManager.STATE_DISCONNECTED)) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mScanner.stopScan();
                            mNearestBleLock.lock();
                            try {
                                if (mNearestBle != null) {
                                    mScanner.stopScan();
                                    msgBuffer.delete(0, msgBuffer.length());
                                    msgBuffer.append("正在连接设备...");
                                    System.out.println("BLE:" + "正在连接设备...");
                                    handler.sendEmptyMessage(0);
                                    bleNfcDevice.requestConnectBleDevice(mNearestBle.getAddress());
                                } else {
                                    msgBuffer.delete(0, msgBuffer.length());
                                    msgBuffer.append("未找到设备！");
                                    System.out.println("BLE:" + "未找到设备！...");

                                    handler.sendEmptyMessage(0);
                                }
                            } finally {
                                mNearestBleLock.unlock();
                            }
                        } else {
                            mScanner.stopScan();
                        }
                    }
                }
            }).start();
        }
    }

    //设备操作类回调deviceManagerCallback
    private DeviceManagerCallback deviceManagerCallback = new DeviceManagerCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceiveConnectBtDevice(boolean blnIsConnectSuc, String mac) {
            super.onReceiveConnectBtDevice(blnIsConnectSuc, mac);
            if (blnIsConnectSuc) {
                System.out.println("Activity设备连接成功");
                msgBuffer.delete(0, msgBuffer.length());
                msgBuffer.append("设备连接成功!\r\n");
                if (mNearestBle != null) {
                    msgBuffer.append("设备名称：").append(bleNfcDevice.getDeviceName()).append("\r\n");
                }
                msgBuffer.append("信号强度：").append(lastRssi).append("dB\r\n");
                msgBuffer.append("SDK版本：" + BleNfcDevice.SDK_VERSIONS + "\r\n");

                //连接上后延时500ms后再开始发指令
                try {
                    Thread.sleep(500L);
                    handler.sendEmptyMessage(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(9);

                System.out.println("mac:" + mac);

                SharedPreferences preferences = getSharedPreferences("MAC", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString("MAC", mac);
                editor.commit();


            }
        }

        @Override
        public void onReceiveDisConnectDevice(boolean blnIsDisConnectDevice) {
            super.onReceiveDisConnectDevice(blnIsDisConnectDevice);
            System.out.println("Activity设备断开链接");
            msgBuffer.delete(0, msgBuffer.length());
            msgBuffer.append("设备断开链接!");
            handler.sendEmptyMessage(8);

        }

        @Override
        public void onReceiveConnectionStatus(boolean blnIsConnection) {
            super.onReceiveConnectionStatus(blnIsConnection);
            System.out.println("Activity设备链接状态回调");
        }

        @Override
        public void onReceiveInitCiphy(boolean blnIsInitSuc) {
            super.onReceiveInitCiphy(blnIsInitSuc);
        }

        @Override
        public void onReceiveDeviceAuth(byte[] authData) {
            super.onReceiveDeviceAuth(authData);
        }

        @Override
        //寻到卡片回调
        public void onReceiveRfnSearchCard(boolean blnIsSus, int cardType, byte[] bytCardSn, byte[] bytCarATS) {
            super.onReceiveRfnSearchCard(blnIsSus, cardType, bytCardSn, bytCarATS);
            if (!blnIsSus || cardType == BleNfcDevice.CARD_TYPE_NO_DEFINE) {
                return;
            }

            System.out.println("Activity接收到激活卡片回调：UID->" + StringTool.byteHexToSting(bytCardSn) + " ATS->" + StringTool.byteHexToSting(bytCarATS));
            long cardNumber = CardUtils.bytesToLong(bytCardSn, 0);
            cardNumberString = CardUtils.cardAddZeroLong(cardNumber);
            //获取补0后的卡号
            System.out.println("卡号为:" + cardNumberString);

            try {
                bleNfcDevice.openBeep(50, 50, 3);  //读写卡成功快响3声
            } catch (DeviceNoResponseException e) {
                e.printStackTrace();
            }

            EverySwipLogEntity everySwipLogEntity = new EverySwipLogEntity();
            everySwipLogEntity.setCardnumber(cardNumberString);
            everySwipLogEntity.setSwipcartime(new Timestamp(System.currentTimeMillis()).toString());
            everySwipLogEntity.setLatitude(myLatLng.latitude + "");
            everySwipLogEntity.setLongitude(myLatLng.longitude + "");

            EverySwipLogHelper mEverySwipLogHelper = new EverySwipLogHelper(mContext);

            String lasttime = mEverySwipLogHelper.getLastLogByCardNo(cardNumberString); //先查出来 最新的历史记录时间
            System.out.println("lasttime:" + lasttime);

            mEverySwipLogHelper.addSwipLog(everySwipLogEntity); //再添加一条历史记录
            mEverySwipLogHelper.close();

            if (lasttime != null) {
                Logger.d("最近一次刷卡时间为：" + lasttime);
                Timestamp lastTimeDate = DateUtils.StringToTimestamp(lasttime);
                Logger.d("这次刷卡时间为：" + DateUtils.timestamptostring(new Timestamp(System.currentTimeMillis())));

                if ((System.currentTimeMillis() - lastTimeDate.getTime()) < (1000 * 5)) {
//                    System.out.println("请勿连续刷卡"+(System.currentTimeMillis()-lastTimeDate.getTime()));
                    mSoundPool.play(soundPoolMap.get(KEY_SOUND_A4), 1, 1, 0, 0, 1);

                    return;
                }
            }
            ProcessSwipLog(cardNumberString, handler);
        }

        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //  System.out.println(msgBuffer);

                if ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTED) || ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTING))) {
                    System.out.println("断开连接");
                } else {
                    System.out.println("请先搜索设备");
                }

                switch (msg.what) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    byte versions = bleNfcDevice.getDeviceVersions();
                                    msgBuffer.append("设备版本:").append(String.format("%02x", versions)).append("\r\n");
                                    handler.sendEmptyMessage(0);
                                    double voltage = bleNfcDevice.getDeviceBatteryVoltage();
                                    msgBuffer.append("设备电池电压:").append(String.format("%.2f", voltage)).append("\r\n");
                                    if (voltage < 3.61) {
                                        msgBuffer.append("设备电池电量低，请及时充电！");
                                    } else {
                                        msgBuffer.append("设备电池电量充足！");
                                    }
                                    handler.sendEmptyMessage(0);
                                    boolean isSuc = bleNfcDevice.androidFastParams(true);
                                    if (isSuc) {
                                        msgBuffer.append("\r\n蓝牙快速传输参数设置成功!");
                                    } else {
                                        msgBuffer.append("\n不支持快速传输参数设置!");
                                    }
                                    handler.sendEmptyMessage(0);

                                    msgBuffer.append("\n开启自动寻卡...\r\n");
                                    handler.sendEmptyMessage(0);
                                    //开始自动寻卡
                                    startAutoSearchCard();
                                } catch (DeviceNoResponseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        break;

                    case 4:   //读写进度条
                        if ((msg.arg1 == 0) || (msg.arg1 == 100)) {
                            readWriteDialog.dismiss();
                            readWriteDialog.setProgress(0);
                        } else {
                            readWriteDialog.setMessage((String) msg.obj);
                            readWriteDialog.setProgress(msg.arg1);
                            if (!readWriteDialog.isShowing()) {
                                readWriteDialog.show();
                            }
                        }
                        break;
                    case 7:  //搜索设备列表
//                    items = mScanner.getDeviceNames();
//                    if (alertDialog == null) {
//                        alertDialog = new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("请选择设备连接")
//                                .setItems(items, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // TODO Auto-generated method stub
//                                        mScanner.stopScan();
//                                        mScanner.setOnReceiveScannerListener(null);
//                                        msgBuffer.delete(0, msgBuffer.length());
//                                        msgBuffer.append("正在连接设备...");
//                                        handler.sendEmptyMessage(0);
//                                        bleNfcDevice.requestConnectBleDevice(mScanner.getDeviceList().get(which).getAddress());
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mScanner.stopScan();
//                                        mScanner.setOnReceiveScannerListener(null);
//                                    }
//                                });
//                        alertDialog.show();
//                    }
//                    else if (!alertDialog.create().isShowing()) {
//                        alertDialog.show();
//                    }
//                    else {
//                        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                                mScanner.stopScan();
//                                mScanner.setOnReceiveScannerListener(null);
//                                msgBuffer.delete(0, msgBuffer.length());
//                                msgBuffer.append("正在连接设备...");
//                                handler.sendEmptyMessage(0);
//                                bleNfcDevice.requestConnectBleDevice(mScanner.getDeviceList().get(which).getAddress());
//                            }
//                        });
//                        alertDialog.create().show();
//                    }
                        break;
                    case 8:
//                        toolbar.getMenu().getItem(0).setIcon(R.mipmap.ic_bluetooth_no);
                        mImgBluetoothStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bluetooth_no));
                        mTvBluetooth.setText("设备未连接\n点击返回重连");
                        break;
                    case 9:
//                        toolbar.getMenu().getItem(0).setIcon(R.mipmap.ic_bluetooth);
                        mImgBluetoothStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bluetooth));
                        mTvBluetooth.setText("设备连接成功");

                        break;
                    case 10:
                        String name = msg.getData().getString("name");
                        mMainTvName.setText(name);  //姓名
                        mMainTvStaffNum.setText(msg.getData().getString("staffnum"));//工号
                        break;
                    case 11:
                        RealmHelper realmHelper = new RealmHelper(mContext);
                        int number = realmHelper.getIncarCount();

                        mTvNowCount.setText(number + " 人");
                        Logger.d("实时人数：" + number);
                        realmHelper.close();
                        break;
                    case 12:
                        mTvInfo.setText(msg.obj.toString());
                        break;

                }
            }
        };


        @Override
        public void onReceiveRfmSentApduCmd(byte[] bytApduRtnData) {
            super.onReceiveRfmSentApduCmd(bytApduRtnData);

            System.out.println("Activity接收到APDU回调：" + StringTool.byteHexToSting(bytApduRtnData));
        }

        @Override
        public void onReceiveRfmClose(boolean blnIsCloseSuc) {
            super.onReceiveRfmClose(blnIsCloseSuc);
        }

        @Override
        //按键返回回调
        public void onReceiveButtonEnter(byte keyValue) {
            if (keyValue == DeviceManager.BUTTON_VALUE_SHORT_ENTER) { //按键短按
                System.out.println("Activity接收到按键短按回调");
                msgBuffer.append("按键短按\r\n");
                handler.sendEmptyMessage(0);
            } else if (keyValue == DeviceManager.BUTTON_VALUE_LONG_ENTER) { //按键长按
                System.out.println("Activity接收到按键长按回调");
                msgBuffer.append("按键长按\r\n");
                handler.sendEmptyMessage(0);
            }
        }
    };

    /**
     * 处理刷卡记录，判断进出，记录时间，上传信息。
     *
     * @param cardNumberString
     * @param handler
     */
    private void ProcessSwipLog(String cardNumberString, Handler handler) {
        String info = "";
        RealmHelper mRealmHelper = new RealmHelper(mContext);
        boolean inFactory = mPolygon.contains(myLatLng);   //是否在围栏位置内。

        if (!mRealmHelper.isLogExist(cardNumberString)) { //没有记录 第一次刷卡
            if (inFactory) {
                info += "下班 厂内上车 —— send out";
                goOnOrGooff = Constant.GOOFF;
                sendRefreshTime(-1, cardNumberString, new Timestamp(System.currentTimeMillis()).toString());//发送 出门信息
            } else {
                info += "上班 厂外上车";
                goOnOrGooff = Constant.GOTO;
            }

            RequireInfoEntity requireInfoEntity = new RequireInfoEntity();
            requireInfoEntity.setCmd_type("require_info");
            RequireInfoEntity.ContentBean contentBean = new RequireInfoEntity.ContentBean();
            contentBean.setCard_number(cardNumberString);
            requireInfoEntity.setContent(contentBean);
            Gson gson = new Gson();
            String s = gson.toJson(requireInfoEntity);
            Logger.json(s);

            StaffInfoHelper staffInfoHelper = new StaffInfoHelper(mContext);
            InfoModel staffInfo = staffInfoHelper.getInfoByCardno(cardNumberString);
            if (staffInfo == null) {
                MqttV3Service.publishMsg(s, Qos, 0);
            } else {//先从本地缓存查询
                System.out.println("本地读取数据");
                SwipCardLog swipCardLog = new SwipCardLog();
                swipCardLog.setName(staffInfo.getName());
                swipCardLog.setCardnum(cardNumberString);
                swipCardLog.setStaffnum(staffInfo.getStaffnumber());
                swipCardLog.setCompany(staffInfo.getCompany());
                swipCardLog.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());
                swipCardLog.setGetOnFlag(true);
                swipCardLog.setSwipCount(1);
                swipCardLog.setGetUpOrOff(goOnOrGooff);

                RealmHelper realmHelper = new RealmHelper(mContext);
                realmHelper.addSwipLog(swipCardLog);
                realmHelper.close();

                //通知更新刷卡人信息。
                Message message = Message.obtain(handler);
                message.what = 10;
                //通知界面更新刷卡人信息
                Bundle b = new Bundle();
                b.putString("name", staffInfo.getName());
                b.putString("staffnum", staffInfo.getStaffnumber());
                message.setData(b);
                handler.sendMessage(message);
                handler.sendEmptyMessage(11);

                mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);

            }
        } else {//存在记录

            goOnOrGooff = mRealmHelper.qurryLogByCardNum(cardNumberString).isGetUpOrOff();
            SwipCardLog log = mRealmHelper.qurryLogByCardNum(cardNumberString);
            int count = mRealmHelper.getSwipCount(cardNumberString);
            Logger.d("count:" + count);

            SwipCardLog logNew = new SwipCardLog();
            logNew.setName(log.getName());
            logNew.setCardnum(log.getCardnum());
            logNew.setStaffnum(log.getStaffnum());
            logNew.setCompany(log.getCompany());
            logNew.setGetOnTime(log.getGetOnTime());
//            logNew.setGetOffTime((new Timestamp(System.currentTimeMillis())).toString());
            logNew.setGetOffTime(log.getGetOffTime());

            logNew.setSwipCount(count + 1);
            logNew.setGetUpOrOff(goOnOrGooff);

            if (inFactory) {
                // go on or go of  下班 false  上班；true
                if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 1 && goOnOrGooff) { //下车了 上班 ，场内
                    Logger.d("send:" + log);//TODO 在这里发送数据。
                    info += "上班-厂内-下车->send in";
                    logNew.setGetOnFlag(false);
                    logNew.setGetOffTime(new Timestamp(System.currentTimeMillis()).toString());

                    sendRefreshTime(1, cardNumberString, logNew.getGetOffTime());

                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 1 && !goOnOrGooff) {
                    info += "下班 厂内 又下车了 send in";
                    logNew.setGetOnFlag(false);
                    logNew.setGetOffTime(new Timestamp(System.currentTimeMillis()).toString());

                    sendRefreshTime(1, cardNumberString, logNew.getGetOffTime());

                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && goOnOrGooff) { //下班 又上车了
                    info += "上班 场内 刷了多次 上车";
                    logNew.setGetOnFlag(true);
                    logNew.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());


                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && !goOnOrGooff) {
                    Logger.d("send:" + log);//TODO 在这里发送数据。
                    info += "下班 厂内 又上车 ->send out";
                    logNew.setGetOnFlag(true);
                    logNew.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());
                    sendRefreshTime(-1, cardNumberString, logNew.getGetOnTime());

                }

            } else {
                // go on or go of  下班 false  上班；true
                if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 1 && goOnOrGooff) { //厂外 上班 下车
                    info += "上班-厂外-下车 ";
                    logNew.setGetOnFlag(false);
                    logNew.setGetOffTime(new Timestamp(System.currentTimeMillis()).toString());


                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 1 && !goOnOrGooff) { //下班回家到家了
                    info += "下班-厂外-下车";
                    logNew.setGetOnFlag(false);
                    logNew.setGetOffTime(new Timestamp(System.currentTimeMillis()).toString());


                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && goOnOrGooff) { //
                    info += "上班-厂外-又上车";
                    logNew.setGetOnFlag(true);
                    logNew.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());

                } else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && !goOnOrGooff) {
                    info += "下班-厂外-又上车";
                    logNew.setGetOnFlag(true);
                    logNew.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());


                }

            }
            mRealmHelper.updateSwipLog(logNew);

            //通知更新刷卡人信息。
            Message message = Message.obtain(handler);
            message.what = 10;
            //通知界面更新刷卡人信息
            Bundle b = new Bundle();
            b.putString("name", log.getName());
            b.putString("staffnum", log.getStaffnum());
            message.setData(b);
            handler.sendMessage(message);

            mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);

        }
        mRealmHelper.close();

        //通知界面更新 实时人数
        handler.sendEmptyMessage(11);

        Message msg = new Message();
        msg.what = 12;
        msg.obj = info;
        handler.sendMessage(msg);
        Logger.d(info);

    }

    /**
     * 发送更新数据；
     *
     * @param direction
     * @param cardno
     * @param time
     */
    public void sendRefreshTime(int direction, String cardno, String time) {
        RefreshTimeEntity refreshTimeEntity = new RefreshTimeEntity();
        refreshTimeEntity.setCmd_type("refresh_time");

        RefreshTimeEntity.ContentBean contentBean = new RefreshTimeEntity.ContentBean();
        contentBean.setCard_number(cardno);
        contentBean.setDirection(direction);
        contentBean.setTime(time);

        refreshTimeEntity.setContent(contentBean);


        Gson gson = new Gson();
        String s = gson.toJson(refreshTimeEntity);

        MqttV3Service.publishMsg(s, Qos, 0);
    }

    //开始自动寻卡
    private boolean startAutoSearchCard() throws DeviceNoResponseException {
        //打开自动寻卡，200ms间隔，寻M1/UL卡
        boolean isSuc = false;
        int falseCnt = 0;
        do {
            isSuc = bleNfcDevice.startAutoSearchCard((byte) 50, ComByteManager.ISO14443_P4);
        } while (!isSuc && (falseCnt++ < 10));
        if (!isSuc) {
            //msgBuffer.delete(0, msgBuffer.length());
            msgBuffer.append("不支持自动寻卡！\r\n");
            handler.sendEmptyMessage(0);
        }
        return isSuc;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msgBuffer);
            if ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTED) || ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTING))) {
                System.out.println("断开连接");
            } else {
                System.out.println("搜索设备");
            }
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                byte versions = bleNfcDevice.getDeviceVersions();
                                msgBuffer.append("设备版本:").append(String.format("%02x", versions)).append("\r\n");
                                handler.sendEmptyMessage(0);
                                double voltage = bleNfcDevice.getDeviceBatteryVoltage();
                                msgBuffer.append("设备电池电压:").append(String.format("%.2f", voltage)).append("\r\n");
                                if (voltage < 3.61) {
                                    msgBuffer.append("设备电池电量低，请及时充电！");
                                } else {
                                    msgBuffer.append("设备电池电量充足！");
                                }
                                handler.sendEmptyMessage(0);
                                boolean isSuc = bleNfcDevice.androidFastParams(true);
                                if (isSuc) {
                                    msgBuffer.append("\r\n蓝牙快速传输参数设置成功!");
                                } else {
                                    msgBuffer.append("\n不支持快速传输参数设置!");
                                }
                                handler.sendEmptyMessage(0);

                                msgBuffer.append("\n开启自动寻卡...\r\n");
                                handler.sendEmptyMessage(0);
                                //开始自动寻卡
                                startAutoSearchCard();
                            } catch (DeviceNoResponseException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;

                case 4:   //读写进度条
                    if ((msg.arg1 == 0) || (msg.arg1 == 100)) {
                        readWriteDialog.dismiss();
                        readWriteDialog.setProgress(0);
                    } else {
                        readWriteDialog.setMessage((String) msg.obj);
                        readWriteDialog.setProgress(msg.arg1);
                        if (!readWriteDialog.isShowing()) {
                            readWriteDialog.show();
                        }
                    }
                    break;
                case 7:  //搜索设备列表
//                    items = mScanner.getDeviceNames();
//                    if (alertDialog == null) {
//                        alertDialog = new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("请选择设备连接")
//                                .setItems(items, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // TODO Auto-generated method stub
//                                        mScanner.stopScan();
//                                        mScanner.setOnReceiveScannerListener(null);
//                                        msgBuffer.delete(0, msgBuffer.length());
//                                        msgBuffer.append("正在连接设备...");
//                                        handler.sendEmptyMessage(0);
//                                        bleNfcDevice.requestConnectBleDevice(mScanner.getDeviceList().get(which).getAddress());
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mScanner.stopScan();
//                                        mScanner.setOnReceiveScannerListener(null);
//                                    }
//                                });
//                        alertDialog.show();
//                    }
//                    else if (!alertDialog.create().isShowing()) {
//                        alertDialog.show();
//                    }
//                    else {
//                        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                                mScanner.stopScan();
//                                mScanner.setOnReceiveScannerListener(null);
//                                msgBuffer.delete(0, msgBuffer.length());
//                                msgBuffer.append("正在连接设备...");
//                                handler.sendEmptyMessage(0);
//                                bleNfcDevice.requestConnectBleDevice(mScanner.getDeviceList().get(which).getAddress());
//                            }
//                        });
//                        alertDialog.create().show();
//                    }
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mBleNfcDeviceService != null) {
            mBleNfcDeviceService.setScannerCallback(scannerCallback);
            mBleNfcDeviceService.setDeviceManagerCallback(deviceManagerCallback);
        }
        mMapView.onResume();

        System.out.println("life:" + "onResume");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSmdtManager = new SmdtManager(getApplicationContext());
        mSmdtManager.smdtSetStatusBar(getApplicationContext(), false);
        UiUtils.hideNavigate(this);

        System.out.println("life:" + "onStart");

    }

    @Override
    protected void onRestart() {

        super.onRestart();
        System.out.println("life:" + "onReStart");

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

        //deactivate();

        System.out.println("life:" + "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();

        mSmdtManager.smdtSetStatusBar(getApplicationContext(), true);
        System.out.println("onstop");
        System.out.println("life:" + "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if (readWriteDialog != null) {
            readWriteDialog.dismiss();
        }

        unbindService(mServiceConnection);

        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        mlocationClient = null;

        MqttV3Service.closeMqtt();
        //  MqttV3Service.client = null;

        System.out.println("life:" + "onDestroy");
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            //关闭当前界面方法二
            Process.killProcess(Process.myPid());
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        System.out.println("onLocationChanged:" + location.getLocationType());
        if (mListener != null && location != null) {
            if (location.getErrorCode() == 0) {
                mListener.onLocationChanged(location);// 显示系统小蓝点

                //经纬度：   [词典]	longitude and latitude
                myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                showLocationStatus(13);
                System.out.println("定位：" + "成功");


            } else {
                String errText = "定位失败," + location.getErrorCode() + ": "
                        + location.getErrorInfo();
                Log.e("AmapErr", errText);
                System.out.println("定位：" + "失败");

                showLocationStatus(14);
            }
        }

        if (null != location) {
            StringBuffer sb = new StringBuffer();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");

                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.getSatellites() + "\n");
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
                sb.append("定位时间: " + DateUtils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间
            sb.append("回调时间: " + DateUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

            //解析定位结果，
            String result = sb.toString();
            mTvLocationResult.setText(result);
        } else {
            mTvLocationResult.setText("定位失败，loc is null");
        }
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    public void showLocationStatus(int what) {
        if (what == 13) {
            if (!mTvGpsStatus.getText().equals("定位成功！")) {
                mImgGpsStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_gps_location));
                mTvGpsStatus.setText("定位成功！");
            }

        } else if (what == 14) {
            if (!mTvGpsStatus.getText().equals("定位失败！")) {
                mImgGpsStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_gps_location_no));
                mTvGpsStatus.setText("定位失败！");
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        System.out.println("activate:" + "activate!");

        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(false);
            mLocationOption.setGpsFirst(true);
            mLocationOption.setSensorEnable(true);
            mLocationOption.setNeedAddress(true);
            mLocationOption.setWifiScan(false);
            mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mLocationOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
            mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
            mLocationOption.setLastLocationLifeCycle(20000);

            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }

    }

    public class MqttProcThread implements Runnable {
        String clientid = "CCP" + IMEI;

        @Override
        public void run() {
            Message msg = new Message();
            MqttV3Service.connectionMqttServer(mContext, myHandler, Constant.MQTT_ADDRESS, Constant.MQTT_PORT, clientid, topicList);
        }
    }

    @SuppressWarnings("HandlerLeak")
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toasty.success(MainActivity.this, "网络连接成功!", Toast.LENGTH_SHORT, true).show();
                mTvNetStatus.setText("网络连接成功");
                mImgNetStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_net_ok));

                SharedPreferences preferences = getSharedPreferences("DEVICEINFO", MODE_PRIVATE);
                DeviceRemarks = preferences.getString("REMARKS",IMEI);
                if (DeviceRemarks.equals(IMEI) || getDeviceFlag == 0){
                    if (MqttV3Service.isConnected()) {
                        DeviceInfoEntity deviceInfoEntity = new DeviceInfoEntity();
                        deviceInfoEntity.setCmd_type("device_info");

                        DeviceInfoEntity.CmdContentBean cmdContentBean = new DeviceInfoEntity.CmdContentBean();
                        cmdContentBean.setDevice_iemi(IMEI);
                        cmdContentBean.setTime(new Timestamp(System.currentTimeMillis()).toString());
                        deviceInfoEntity.setCmd_content(cmdContentBean);


                        Gson gson = new Gson();
                        String s = gson.toJson(deviceInfoEntity);
                        Logger.json(s);
                        MqttV3Service.publishMsg(s, 1, 2);
                        getDeviceFlag = 1;
                    }
                }
            } else if (msg.what == 0) {
                Toasty.error(MainActivity.this, "网络连接失败!", Toast.LENGTH_SHORT, true).show();

                mTvNetStatus.setText("网络连接失败\n点击返回重连");
                mImgNetStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_net_disconn));
            } else if (msg.what == 2) {
                String strContent = "";
                strContent += msg.getData().getString("content");

                Logger.d("strcontent:" + strContent);

                if (strContent.contains("ack_")) {
                    if (JsonUtils.isGoodJson(strContent)) {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(strContent).getAsJsonObject();
                        String cmd_type = jsonObject.get("cmd_type").getAsString();
                        int status_code = jsonObject.get("status_code").getAsInt();
//                        Logger.d("jsonObject.get(\"cmd_type\")   "+cmd_type);
//                        Logger.d("jsonObject.get(\"status_code\")   "+status_code);

                        if (cmd_type.equals("ack_require")) { //请求个人信息 返回
                            Logger.d("ack_require");
                            if (status_code == 0) { //成功
                                Logger.d("请求个人信息成功：");
                                Gson gson = new Gson();
                                AckRequireOk ackRequireOk = gson.fromJson(strContent, AckRequireOk.class);
                                Logger.json(strContent);

                                String name = ackRequireOk.getContent().getName();
                                String staffnumber = ackRequireOk.getContent().getStaff_number();
                                String cardnumber = ackRequireOk.getContent().getCard_number();
                                String company = ackRequireOk.getContent().getCompany();

                                if (cardNumberString.equals(cardnumber)) {
                                    Logger.d("卡号一致！");

                                    SwipCardLog swipCardLog = new SwipCardLog();
                                    swipCardLog.setName(name);
                                    swipCardLog.setCardnum(cardNumberString);
                                    if (strContent.contains("company")){
                                        System.out.println("swipcardlog:"+"company:"+company);
                                        swipCardLog.setCompany(company);//TODO  获取刷卡人的公司
                                    }else {
                                        System.out.println("swipcardlog:"+"no company");
                                        swipCardLog.setCompany("无");//TODO  获取刷卡人的公司
                                    }
                                    swipCardLog.setStaffnum(staffnumber);
                                    swipCardLog.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());
                                    swipCardLog.setGetOnFlag(true);
                                    swipCardLog.setSwipCount(1);
                                    swipCardLog.setGetUpOrOff(goOnOrGooff);

                                    RealmHelper realmHelper = new RealmHelper(mContext);
                                    realmHelper.addSwipLog(swipCardLog);


                                    int number = realmHelper.getIncarCount();
                                    mMainTvName.setText(name); //  姓名
                                    mMainTvStaffNum.setText(staffnumber); // 工号
                                    mTvNowCount.setText(number + " 人");
                                    Logger.d("实时人数：" + number);
                                    realmHelper.close();

                                    mSoundPool.play(soundPoolMap.get(KEY_SOUND_A2), 1, 1, 0, 0, 1);

                                }

                            } else { //失败 失败解析
                                mSoundPool.play(soundPoolMap.get(KEY_SOUND_A3), 1, 1, 0, 0, 1);
                            }
                        } else if (cmd_type.equals("ack_refresh")) {
                            Logger.d("ack_refresh");

                            if (status_code == 0) {
                                Logger.d("更新成功：");

                            } else {

                            }
                        } else if (cmd_type.equals("ack_version")) {
                            if (status_code == 0) {
                                Logger.d("提交软件版本成功：");
                            } else {

                            }
                        } else if (cmd_type.equals("ack_device_info")) {
                            if (status_code == 0) {
                                System.out.println("获取设备信息成功：");
                                Gson gson = new Gson();
                                AckDeviceInfoEntity ackDeviceInfoEntity = gson.fromJson(strContent, AckDeviceInfoEntity.class);

                                SharedPreferences preferences = getSharedPreferences("DEVICEINFO", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                if (ackDeviceInfoEntity.getCmd_content().getRemarks() == null)
                                {
                                    editor.putString("REMARKS", IMEI);

                                }else {
                                    editor.putString("REMARKS", ackDeviceInfoEntity.getCmd_content().getRemarks().toString());
                                    DeviceRemarks = ackDeviceInfoEntity.getCmd_content().getRemarks().toString();

                                }
                                editor.commit();

                            } else {

                            }
                        }
                    }
                }
            } else if (msg.what == 3) {
                if (!MqttV3Service.mqttAndroidClient.isConnected()) {
                    System.out.println("MQTT:连接丢失");
                    Toasty.error(MainActivity.this, "断开连接!", Toast.LENGTH_SHORT, true).show();

                    mImgNetStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_net_disconn));
                    mTvNetStatus.setText("网络断开\n请按返回键");
                }
            }
        }
    };

    public boolean isLocalMac(String mac) {
        boolean islocal = false;
        SharedPreferences sharedPreferences = getSharedPreferences("MAC", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String localmac = sharedPreferences.getString("MAC", "");
        System.out.println("local：" + localmac + "  mac2: " + mac);

        if (localmac.equals("")) {
            return true;
        }

        if (localmac.equals(mac)) {
            return true;
        } else {
            return false;
        }
    }
}
