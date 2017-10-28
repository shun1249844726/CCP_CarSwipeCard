package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lexinsmart.xushun.ccpcarswipecard.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xushun on 2017/10/28.
 * 功能描述：
 * 心情：
 */

public class CheckIfNeedUpdate extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private static final String url = Constants.UPDATE_URL;
    OnGetVersionListener mOnGetVersionInterface;

    public CheckIfNeedUpdate(Context context) {
        this.mContext = context;
    }


    public void setOnGetVeersionListener (OnGetVersionListener onGetVeersionListener){
        this.mOnGetVersionInterface = onGetVeersionListener;
    }
    protected void onPreExecute() {

    }


    @Override
    protected void onPostExecute(String result) {

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONObject obj = new JSONObject(result);
            String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
            String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

            int versionCode = AppUtils.getVersionCode(mContext);

            System.out.println("远程版本："+apkCode  + " 本地版本："+versionCode);
            if (apkCode > versionCode){
                mOnGetVersionInterface.onDataSuccessfully(true);
            }else {
                mOnGetVersionInterface.onDataSuccessfully(false);
            }

        } catch (JSONException e) {
            mOnGetVersionInterface.onDataFailed();
            Log.e(Constants.TAG, "parse json error");
        }
    }




    @Override
    protected String doInBackground(Void... args) {
        return HttpUtils.get(url);
    }
}
