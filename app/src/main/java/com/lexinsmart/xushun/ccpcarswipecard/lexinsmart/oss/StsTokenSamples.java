package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss;

import android.os.Handler;
import android.os.Message;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.google.gson.Gson;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.StsModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jingdan on 2017/8/31.
 */
public class StsTokenSamples {

    private WeakReference<Handler> handler;
    /**
     * config by local ip and port
     */
    public static final String STS_SERVER_API = "http://本地ip:sts服务端口号/sts/getsts";
    //可以启动本地sts 服务来活动sts信息。详细见sts_local_server中的说明。


    public StsTokenSamples(Handler handler){
        this.handler = new WeakReference<>(handler);
    }

    //建议sts的token获取等放在服务器端进行获取对提高安全性
    public void getStsTokenAndSet(){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL stsUrl = new URL(Constant.STS_SERVER_API);
                    HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = IOUtils.readStreamAsString(inputStream, "utf-8");
                        Gson gson = new Gson();
                        StsModel stsModel = gson.fromJson(result, StsModel.class);

                        Message msg = Message.obtain();
                        msg.obj = stsModel;
                        msg.what = Constant.STS_TOKEN_SUC;
                        StsTokenSamples.this.handler.get().sendMessage(msg);
                    }else{
                        OSSLog.logDebug("stsSamples", responseCode+"");
                        StsTokenSamples.this.handler.get().sendEmptyMessage(Constant.FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StsTokenSamples.this.handler.get().sendEmptyMessage(Constant.FAIL);
                }


            }
        }.start();
    }
}
