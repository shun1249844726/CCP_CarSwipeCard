package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.lexinsmart.xushun.ccpcarswipecard.R;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean.StsModel;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.MultipartUploadSamples;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.ProgressCallback;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.PutObjectSamples;
import com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.oss.StsTokenSamples;

import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.FAIL;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.STS_TOKEN_SUC;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_Fail;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_PROGRESS;
import static com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.utils.Constant.UPLOAD_SUC;

/**
 * Created by xushun on 2017/11/6.
 * 功能描述：
 * 心情：
 */

public class OssTestActivity extends AppCompatActivity {

    private PutObjectSamples putObjectSamples;
    private OSS oss;

    private static final String testBucket = "ccpswipcard";
    private static final String uploadObject = "logs/sampleObject.realm";

    private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String uploadFilePath = "/storage/emulated/0/Android/data/com.lexinsmart.xushun.ccpcarswipecard/files/2017-11-07swiplog.realm";


    private StsTokenSamples stsTokenSamples;
    private Button uploadBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        setContentView(com.lexinsmart.xushun.ccpcarswipecard.R.layout.activity_osstest);


        uploadBtn = (Button) findViewById(R.id.btn_uploaddddd);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNotNull(putObjectSamples)) {
                    putObjectSamples.asyncPutObjectFromLocalFile(new ProgressCallback<PutObjectRequest, PutObjectResult>() {
                        @Override
                        public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

                            Message msg = Message.obtain();
                            msg.what = UPLOAD_PROGRESS;
                            Bundle data = new Bundle();
                            data.putLong("currentSize", currentSize);
                            data.putLong("totalSize", totalSize);
                            msg.setData(data);
                            handler.sendMessage(msg);

                        }

                        @Override
                        public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                            handler.sendEmptyMessage(UPLOAD_SUC);


                        }

                        @Override
                        public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                            handler.sendEmptyMessage(UPLOAD_Fail);

                        }
                    });
                }
            }
        });


        //please init local sts server firstly. please check python/*.py for more info.
        initStsData();

    }

    private void initStsData() {
        //get sts token
        stsTokenSamples = new StsTokenSamples(handler);
        stsTokenSamples.getStsTokenAndSet();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            boolean handled = false;
            switch (msg.what) {
                case UPLOAD_SUC:
                    Toast.makeText(OssTestActivity.this, "upload_suc", Toast.LENGTH_SHORT).show();
                    handled = true;
                    break;
                case FAIL:
                    Toast.makeText(OssTestActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    handled = true;
                    break;
                case STS_TOKEN_SUC:
                    Toast.makeText(OssTestActivity.this, "sts_token_suc", Toast.LENGTH_SHORT).show();
                    StsModel response = (StsModel) msg.obj;
                    setOssClient(response.Credentials.AccessKeyId, response.Credentials.AccessKeySecret, response.Credentials.SecurityToken);
                    System.out.println("-------StsToken.Expiration-------\n" + response.Credentials.Expiration);
                    System.out.println("-------StsToken.AccessKeyId-------\n" + response.Credentials.AccessKeyId);
                    System.out.println("-------StsToken.SecretKeyId-------\n" + response.Credentials.AccessKeySecret);
                    System.out.println("-------StsToken.SecurityToken-------\n" + response.Credentials.SecurityToken);
                    handled = true;
                    break;
            }


            return handled;
        }
    });

    private boolean checkNotNull(Object obj) {
        if (obj != null) {
            return true;
        }
        Toast.makeText(OssTestActivity.this, "init Samples fail", Toast.LENGTH_SHORT).show();
        return false;
    }

    private OSSCredentialProvider mCredentialProvider;
    private MultipartUploadSamples multipartUploadSamples;


    public void setOssClient(String ak, String sk, String token) {
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

    private void initSamples() {
        multipartUploadSamples = new MultipartUploadSamples(oss, testBucket, uploadObject, uploadFilePath, handler);

        putObjectSamples = new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath);

    }
}
