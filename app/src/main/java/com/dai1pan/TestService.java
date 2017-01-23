package com.dai1pan;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dai1pan.Base.TwitterUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by 2140261 on 2017/01/21.
 */

public class TestService extends Service {

    private final int INTERVAL_BY_TIMER = (60 * 1000);
    private static final String SAVE_TIME = "saveTime";
    private static final String SAVE_STATUS_INTERVAL = "saveStatusInterval";
    private static final String SAVE_TIME_HOUR = "saveTimeHour";
    private static final String SAVE_TIME_MINUTE = "saveTimeMinute";
    private Timer mTimer = null;
    private Twitter mTwitter;
//    Handler mHandler = new Handler();
    private Random mRnd = new Random();
    private int mNum = 0;
    private SharedPreferences mPref;
    private int mHour = 0;
    private int mMinute = 0;
    private String mTime = "";
    public static  final String SAVE_FLG = "saveTimeBoolean";
    private  Boolean mFlg = false;
    private String mStatus = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("サービス開始", "しましたよ！");

        mPref = getSharedPreferences(SAVE_TIME, MODE_PRIVATE);
        mFlg = mPref.getBoolean(SAVE_FLG, false);

        //プリファレンスから設定された時間情報をintで取得
        if(mFlg){

            mHour = mPref.getInt(SAVE_TIME_HOUR, 0);
            mMinute = mPref.getInt(SAVE_TIME_MINUTE, 0);
            mTime += mHour + "" + mMinute; //保存された日時情報を4桁数字の文字列として保持
            mStatus = mPref.getString(SAVE_STATUS_INTERVAL, "");
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);

        mTwitter = TwitterUtils.getTwitterInstance(TestService.this);
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("クラスの静的定数利用テスト:", MainActivity.SERVICE_TEST_MSG);
                mNum = mRnd.nextInt(11);
                Log.d("乱数結果:", "" + mNum);


                //プリファレンスが保存されているとき
                if(mFlg){
                    Log.d("if文","とおってる");
                    Date date = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("Hm");
                    Log.d("現在時間は", "" + sdf1.format(date));
                    Log.d("保存時間は",mTime);
                    if(sdf1.format(date).toString().equals(mTime)){

                        Log.d("比較成功", "　やったぜ！");
                        try {
                            Status status = null;
                            status = mTwitter.updateStatus(mStatus + " ");
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }


                    }



                }






            }
        }, 1000, 60 * 1000);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        Log.i("TestService", "onDestroy");
        Log.d("サービス終了", "しましたよ！");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TestService", "onBind");
        return null;
    }
}
