package com.dai1pan;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by 2140261 on 2017/01/21.
 */
public class MakeIntervalTweetActivity extends AppCompatActivity {

    private static final String SAVE_TIME = "saveTime";
    private static final String SAVE_STATUS_INTERVAL = "saveStatusInterval";
    private static final String SAVE_TIME_HOUR = "saveTimeHour";
    private static final String SAVE_TIME_MINUTE = "saveTimeMinute";
    public static  final String SAVE_FLG = "saveTimeBoolean";

    private Button mStartButton;
    private Button mEndButton;
    private EditText mEditText;
    private SharedPreferences mPref;
    private int mNum;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_interval);

        //インテントから設定番号を取得
        Intent intent = getIntent();
        mNum = intent.getIntExtra("useNumber", 1);

        mStartButton = (Button)findViewById(R.id.setIntervalButton);
        mEndButton = (Button)findViewById(R.id.endIntervalButton);
        mEditText = (EditText)findViewById(R.id.intervalEdit);
        mPref = MakeIntervalTweetActivity.this.getSharedPreferences(SAVE_TIME,
                Context.MODE_PRIVATE);

        //設定情報読み込み
        mPref = getSharedPreferences(SAVE_TIME, MODE_PRIVATE);
        boolean testFlg = mPref.getBoolean(SAVE_FLG+mNum,false);
        Log.d("フラグ確認", "" + testFlg);
        if(mPref.getBoolean(SAVE_FLG+mNum,false)){
            //既に設定情報がある場合
            mEditText.setText(mPref.getString(SAVE_STATUS_INTERVAL+mNum, ""), TextView.BufferType.NORMAL);
        }else{

        }


        // 現在時刻を取得
        Calendar calendar = Calendar.getInstance();
        final int hour   = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 時間選択ダイアログの生成
        final TimePickerDialog timepick= new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                    // 設定 ボタンクリック時の処理
                        Log.d("登録時間:", "" + hourOfDay);
                        Log.d("登録分:", "" + minute);

                        //プリファレンス保存
                        SharedPreferences.Editor editor = mPref.edit();
                        //時間・分(hour, minute)を保存
                        editor.putInt(SAVE_TIME_HOUR+mNum, hour);
                        editor.putInt(SAVE_TIME_MINUTE+mNum, minute);
                        //ツイート内容を保存
                        editor.putString(SAVE_STATUS_INTERVAL+mNum, mEditText.getText().toString());
                        editor.putBoolean(SAVE_FLG+mNum, true);
                        editor.commit();

                        //(重複を避ける為にサービス停止
                        stopService(new Intent(MakeIntervalTweetActivity.this, TestService.class));

                        //サービス開始
                        startService(new Intent(MakeIntervalTweetActivity.this, TestService.class));

                        //
                        Intent intent = new Intent(MakeIntervalTweetActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                },
                hour,
                minute,
                true);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //表示
                timepick.show();

            }
        });


        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定情報のクリア処理
                SharedPreferences.Editor editor = mPref.edit();
                editor.remove(SAVE_FLG+mNum);
                editor.remove(SAVE_STATUS_INTERVAL+mNum);
                //editor.remove(SAVE_TIME);
                editor.remove(SAVE_TIME_HOUR+mNum);
                editor.remove(SAVE_TIME_MINUTE+mNum);
                editor.commit();
                //サービス停止
//                stopService(new Intent(MakeIntervalTweetActivity.this, TestService.class));
                //(重複を避ける為にサービス停止
                stopService(new Intent(MakeIntervalTweetActivity.this, TestService.class));

                //サービス開始
                startService(new Intent(MakeIntervalTweetActivity.this, TestService.class));
                Intent intent = new Intent(MakeIntervalTweetActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
