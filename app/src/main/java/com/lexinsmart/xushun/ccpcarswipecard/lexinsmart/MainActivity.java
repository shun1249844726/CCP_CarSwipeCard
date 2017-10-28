package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity.CheckPermissionsActivity;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.GetInfo;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.SwipCardLog;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.UserInfo;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.db.RealmHelper;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.CardUtils;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.DateUtils;
import com.orhanobut.logger.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xushun on 2017/10/25.
 * 功能描述：
 * 心情：
 */

public class MainActivity extends CheckPermissionsActivity implements LocationSource,AMapLocationListener {
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
    private AMapLocationClientOption mLocationClientOption =null;

    private LocationSource.OnLocationChangedListener mListener = null;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private LatLng myLatLng = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏


        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        msgBuffer = new StringBuffer();
        //ble_nfc服务初始化
        Intent gattServiceIntent = new Intent(this, BleNfcDeviceService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        mMapView = (MapView) findViewById(R.id.mp_fence);
        mMapView.onCreate(savedInstanceState);
        initFence();

    }

    private void initFence() {

        if(mAMap == null){
            mAMap = mMapView.getMap();
            UiSettings settings = mAMap.getUiSettings();
            mAMap.setLocationSource(this);
            settings.setMyLocationButtonEnabled(true);
            mAMap.setMyLocationEnabled(true);

            mAMap.moveCamera(CameraUpdateFactory.zoomBy(5));
            setUpMap();
        }

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);

        mLocationClientOption = new AMapLocationClientOption();
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationClientOption);
        //启动定位
        mLocationClient.startLocation();
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

        // 绘制一个长方形
        PolygonOptions pOption = new PolygonOptions();
        pOption.add(new LatLng(31.978761, 120.903709));
        pOption.add(new LatLng(31.979339, 120.905897));
        pOption.add(new LatLng(31.9774, 120.905173));

        mPolygon = mAMap.addPolygon(pOption.strokeWidth(4)
                .strokeColor(Color.argb(50, 1, 1, 1))
                .fillColor(Color.argb(50, 1, 1, 1)));

    }
    @OnClick(R.id.btn_submit)
    void submit(View view) {
        Logger.d("submit");
        RealmHelper realmHelper = new RealmHelper(mContext);

        realmHelper.clearAll();
        realmHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bluetooth) {
            if ((bleNfcDevice.isConnection() == BleManager.STATE_CONNECTED)) {
                bleNfcDevice.requestDisConnectDevice();
            } else {
                searchNearestBleDevice();
            }
        }
        if (id == R.id.action_settings) {

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
            if ((scanRecord != null) && (StringTool.byteHexToSting(scanRecord).contains("017f5450"))) {  //从广播数据中过滤掉其它蓝牙设备
                msgBuffer.append("搜到设备：").append(device.getName()).append(" 信号强度：").append(rssi).append("\r\n");
                handler.sendEmptyMessage(0);
                if (mNearestBle != null) {
                    if (rssi > lastRssi) {
                        mNearestBleLock.lock();
                        try {
                            mNearestBle = device;
                        } finally {
                            mNearestBleLock.unlock();
                        }
                    }
                } else {
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
                                    handler.sendEmptyMessage(0);
                                    bleNfcDevice.requestConnectBleDevice(mNearestBle.getAddress());
                                } else {
                                    msgBuffer.delete(0, msgBuffer.length());
                                    msgBuffer.append("未找到设备！");
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

    //设备操作类回调
    private DeviceManagerCallback deviceManagerCallback = new DeviceManagerCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceiveConnectBtDevice(boolean blnIsConnectSuc) {
            super.onReceiveConnectBtDevice(blnIsConnectSuc);
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

//            System.out.println("Activity接收到激活卡片回调：UID->" + StringTool.byteHexToSting(bytCardSn) + " ATS->" + StringTool.byteHexToSting(bytCarATS));
            int cardNumber = CardUtils.bytesToInt(bytCardSn, 0);
            String cardNumberString = CardUtils.cardAddZero(cardNumber);
            //获取补0后的卡号
            System.out.println("卡号为:" + cardNumberString);

            try {
                bleNfcDevice.openBeep(50, 50, 3);  //读写卡成功快响3声
            } catch (DeviceNoResponseException e) {
                e.printStackTrace();
            }

            //requestInfo(cardNumberString, handler);
            //处理逻辑
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
                        toolbar.getMenu().getItem(0).setIcon(R.mipmap.ic_bluetooth_no);
                        break;
                    case 9:
                        toolbar.getMenu().getItem(0).setIcon(R.mipmap.ic_bluetooth);
                        break;
                    case 10:
                        String name = msg.getData().getString("name");
                        mMainTvName.setText("姓名:" + name);
                        mMainTvStaffNum.setText("工号:" + msg.getData().getString("staffnum"));
                        break;
                    case 11:
                        RealmHelper realmHelper = new RealmHelper(mContext);
                        int number = realmHelper.getIncarCount();

                        mTvNowCount.setText(number+"人");
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
     * @param cardNumberString
     * @param handler
     */
    private void ProcessSwipLog(String cardNumberString, Handler handler) {
        String info = "";
        RealmHelper mRealmHelper = new RealmHelper(mContext);
        boolean inFactory = mPolygon.contains(myLatLng);   //是否在围栏位置内。
        boolean goOnOrGooff = false; // 下班 false  上班；true

        if (!mRealmHelper.isLogExist(cardNumberString)) { //没有记录
            if (inFactory) {
                info += "下班 厂内上车";
                goOnOrGooff = Constant.GOOFF;
            } else {
                info += "上班 厂外上车";
                goOnOrGooff = Constant.GOTO;
            }

            getUserInfo(mRealmHelper,cardNumberString,handler,1,goOnOrGooff); //第一次 刷卡

        } else {//存在记录

            goOnOrGooff = mRealmHelper.qurryLogByCardNum(cardNumberString).isGetUpOrOff();

            SwipCardLog log = mRealmHelper.qurryLogByCardNum(cardNumberString);
            int count = mRealmHelper.getSwipCount(cardNumberString);
            Logger.d("count:"+count);

            SwipCardLog logNew = new SwipCardLog();
            logNew.setName(log.getName());
            logNew.setCardnum(log.getCardnum());
            logNew.setStaffnum(log.getStaffnum());
            logNew.setGetOnTime(log.getGetOnTime());
            logNew.setGetOffTime((new Timestamp(System.currentTimeMillis())).toString());
            logNew.setSwipCount(count+1);
            logNew.setGetUpOrOff(goOnOrGooff);

            if (inFactory) {

                // go on or go of  下班 false  上班；true
                if (mRealmHelper.getSwipCount(cardNumberString)%2 == 1 && goOnOrGooff){ //下车了 上班 ，场内
                    Logger.d("send:" + log);//TODO 在这里发送数据。
                    info += "上班-厂内-下车->send";
                    logNew.setGetOnFlag(false);

                }else if (mRealmHelper.getSwipCount(cardNumberString) %2 ==1 && !goOnOrGooff){
                    info += "下班 厂内 又下车了 ";
                    logNew.setGetOnFlag(false);

                }else if (mRealmHelper.getSwipCount(cardNumberString) %2 == 0 && goOnOrGooff){ //下班 又上车了
                    info += "上班 场内 刷了多次 上车";
                    logNew.setGetOnFlag(true);

                }else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && !goOnOrGooff){
                    Logger.d("send:" + log);//TODO 在这里发送数据。
                    info += "下班 厂内 又上车 ->send";
                    logNew.setGetOnFlag(true);

                }

            } else {
                // go on or go of  下班 false  上班；true
                if (mRealmHelper.getSwipCount(cardNumberString)%2 == 1 && goOnOrGooff){ //厂外 上班 下车
                    info += "上班-厂外-下车 ";
                    logNew.setGetOnFlag(false);

                }else if (mRealmHelper.getSwipCount(cardNumberString) %2 ==1 && !goOnOrGooff){ //下班回家到家了
                    info += "下班-厂外-下车";
                    logNew.setGetOnFlag(false);

                }else if (mRealmHelper.getSwipCount(cardNumberString) %2 == 0 && goOnOrGooff){ //
                    info += "上班-厂外-又上车";
                    logNew.setGetOnFlag(true);

                }else if (mRealmHelper.getSwipCount(cardNumberString) % 2 == 0 && !goOnOrGooff){
                    info += "下班-厂外-又上车";
                    logNew.setGetOnFlag(true);

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
     * 从网络中获取到用户信息。
     * @param realmHelper
     * @param cardNumberString
     * @param mHandler
     * @param swipcount
     * @param getUpOrOff
     */
    private void getUserInfo(RealmHelper realmHelper,String cardNumberString, Handler mHandler, int swipcount, boolean getUpOrOff) {

        int max = 20;
        int min = 10;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;

        UserInfo userInfo = new UserInfo();
        userInfo.setSuccess("0");
        userInfo.setName("xushun" + s);
        userInfo.setStaffNum("C1111" + s);
        userInfo.setCardNum(cardNumberString);

        //通知更新刷卡人信息。
        Message message = Message.obtain(mHandler);
        message.what = 10;
        //通知界面更新刷卡人信息
        Bundle b = new Bundle();
        b.putString("name", userInfo.getName());
        b.putString("staffnum", userInfo.getStaffNum());
        message.setData(b);
        mHandler.sendMessage(message);


        SwipCardLog swipCardLog = new SwipCardLog();
        swipCardLog.setName(userInfo.getName());
        swipCardLog.setCardnum(userInfo.getCardNum());
        swipCardLog.setStaffnum(userInfo.getStaffNum());
        swipCardLog.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());
        swipCardLog.setGetOnFlag(true);
        swipCardLog.setSwipCount(swipcount);
        swipCardLog.setGetUpOrOff(getUpOrOff);

        realmHelper.addSwipLog(swipCardLog);
        realmHelper.close();

    }

    /**
     * 请求基础信息，姓名和工号
     *
     * @param cardNumberString
     */
    private void requestInfo(String cardNumberString, Handler mHandler) {


        int max = 20;
        int min = 10;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;

        GetInfo getInfoResult = new GetInfo();
        getInfoResult.setSuccess("0");
        getInfoResult.setName("张" + s);
        getInfoResult.setCardNum(cardNumberString);
        getInfoResult.setStaffNum("C10" + s);

        String checkintime = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 19).toString();   // intime
        String checkouttime = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 9).toString(); // outtime
        getInfoResult.setCheckouttime(checkouttime);
        getInfoResult.setCheckintime(checkintime);//模拟 九个小时

        //通知更新刷卡人信息。
        Message message = Message.obtain(mHandler);
        message.what = 10;
        //通知界面更新刷卡人信息
        Bundle b = new Bundle();
        b.putString("name", getInfoResult.getName());
        b.putString("staffnum", getInfoResult.getStaffNum());
        message.setData(b);
        mHandler.sendMessage(message);


        String info = "";
        if (getInfoResult.getSuccess().equals("0")) {//返回结果成功
            Timestamp checkouttimeDate = DateUtils.StringToTimestamp(getInfoResult.getCheckouttime());
            Timestamp checkintimeDate = DateUtils.StringToTimestamp(getInfoResult.getCheckintime());
            if (checkouttimeDate.getTime() > checkintimeDate.getTime()) {//下班时间>上班时间
                if (System.currentTimeMillis() - checkouttimeDate.getTime() > 6 * 60 * 60 * 1000) { //  >6h

                    RealmHelper mRealmHelper = new RealmHelper(mContext);

                    boolean isInFactory = false; //是否在厂区内 ，根据经纬度判断。
                    if (isInFactory) {//在厂区内 ，下班
                        if (mRealmHelper.isLogExist(cardNumberString)) { //存在记录
                            if (mRealmHelper.qurryLogByCardNum(cardNumberString).isGetUpOrOff()) {//如果是上班  则下车
                                info = "在厂区内 下车";
                                Logger.d(info);
                            } else {//下班  他为啥又刷了

                            }
                        } else {
                            info = "在厂区内，下班坐车";
                            Logger.d(info);
                        }
                    } else {  //在厂区外。
                        int count = 0;
                        Logger.d(mRealmHelper.getSwipCount(cardNumberString));

                        if (mRealmHelper.isLogExist(cardNumberString)) { //信息存在
                            count = mRealmHelper.getSwipCount(getInfoResult.getCardNum());//从数据库查询刷卡的次数。
                        } else {
                            count = 0;//初次添加
                        }

                        SwipCardLog swipCardLog = new SwipCardLog();
                        swipCardLog.setName(getInfoResult.getName());
                        swipCardLog.setCardnum(getInfoResult.getCardNum());
                        swipCardLog.setStaffnum(getInfoResult.getStaffNum());
                        if (count == 0) {//上班，上车，
                            info = "厂区外，上班，上车";
                            Logger.d(info);

                            swipCardLog.setGetOnTime(new Timestamp(System.currentTimeMillis()).toString());
                            SwipCardLog oldinfo = mRealmHelper.qurryLogByCardNum(getInfoResult.getCardNum());
                            swipCardLog.setGetOffTime(null);
                            swipCardLog.setGetOnFlag(true);
                            swipCardLog.setSwipCount(1);
                            swipCardLog.setGetUpOrOff(true);
                            mRealmHelper.addSwipLog(swipCardLog);


                        } else {//上班，下车
                            info = "厂区外，上班，下车";
                            Logger.d(info);

                            SwipCardLog oldinfo = mRealmHelper.qurryLogByCardNum(getInfoResult.getCardNum());
                            swipCardLog.setGetOnTime(oldinfo.getGetOnTime());
                            swipCardLog.setGetOffTime(new Timestamp(System.currentTimeMillis()).toString());
                            swipCardLog.setGetOnFlag(false);
                            swipCardLog.setSwipCount(2);
                            swipCardLog.setGetUpOrOff(true);

                            mRealmHelper.updateSwipLog(swipCardLog);
                        }
                    }
                    mRealmHelper.close();

                } else if (System.currentTimeMillis() - checkouttimeDate.getTime() < 6 * 60 * 60 * 1000) { // <3h
                    info = "下班，上车";
                    Logger.d(info);


                }
            } else {//下班忘刷卡
                info = "未打下班卡！";

            }
            Message msg = new Message();
            msg.what = 12;
            msg.obj = info;
            mHandler.sendMessage(msg);
        }

        //通知界面更新 实时人数
        mHandler.sendEmptyMessage(11);
    }


    //发送读写进度条显示Handler
    private void showReadWriteDialog(String msg, int rate) {
        Message message = new Message();
        message.what = 4;
        message.arg1 = rate;
        message.obj = msg;
        handler.sendMessage(message);
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
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (readWriteDialog != null) {
            readWriteDialog.dismiss();
        }

        unbindService(mServiceConnection);

        mMapView.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                //经纬度：   [词典]	longitude and latitude
                myLatLng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption.setOnceLocation(false);
            mLocationOption.setGpsFirst(true);
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
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
