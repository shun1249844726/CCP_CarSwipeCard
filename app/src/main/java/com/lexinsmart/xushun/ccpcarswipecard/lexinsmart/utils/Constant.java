package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils;

/**
 * Created by xushun on 2017/10/26.
 * 功能描述：
 * 心情：
 */

public class Constant {
    public static int TYPE_BEON_GETON=1;
    public static int TYPE_BEON_GETOFF=2;
    public static int TYPE_BEOFF_GETON=3;
    public static int TYPE_BEOFF_GETOFF=4;

    public static boolean GETUP = true;
    public static boolean GETOFF = false;

    public static boolean GOOFF = false;
    public static boolean GOTO = true;


    public static final String MQTT_ADDRESS = "120.92.84.64";
    public static final String MQTT_PORT = "1883";

    public static String IMEI = "";

    public static final String STS_SERVER_API = "http://ccp-oss-sts.glrsmart.com/sts/getsts";
    public static final int DOWNLOAD_SUC = 1;
    public static final int DOWNLOAD_Fail = 2;
    public static final int UPLOAD_SUC = 3;
    public static final int UPLOAD_Fail = 4;
    public static final int UPLOAD_PROGRESS = 5;
    public static final int LIST_SUC = 6;
    public static final int HEAD_SUC = 7;
    public static final int RESUMABLE_SUC = 8;
    public static final int SIGN_SUC= 9;
    public static final int BUCKET_SUC= 10;
    public static final int GET_STS_SUC= 11;
    public static final int MULTIPART_SUC= 12;
    public static final int STS_TOKEN_SUC= 13;
    public static final int FAIL= 9999;

}
