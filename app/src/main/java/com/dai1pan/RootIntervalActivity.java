package com.dai1pan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dai1pan.Base.TwitterOAuthActivity;

import java.util.ArrayList;

/**
 * Created by 2140261 on 2017/01/26.
 */

public class RootIntervalActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private TextView mText1;
    private TextView mText2;
    private Button mButton;
    private static final String SAVE_TIME = "saveTime";
    private static final String SAVE_STATUS_INTERVAL = "saveStatusInterval";
    private static final String SAVE_FLG = "saveTimeBoolean";
    private static final String SAVE_TIME_HOUR = "saveTimeHour";
    private static final String SAVE_TIME_MINUTE = "saveTimeMinute";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_interval_activity);

        mPref = RootIntervalActivity.this.getSharedPreferences(SAVE_TIME,
                Context.MODE_PRIVATE);


        //ビュー関連(テキスト:設定情報を反映, ボタン:クリック処理)
        for(int i=1;i<=3;i++){
            if(checkSaveData(i)){
                switch (i){
                    case 1:
                        mText1 = (TextView)findViewById(R.id.dispIntervalText1);
                        mText1.setText(mPref.getString(SAVE_STATUS_INTERVAL+i,"設定情報なし"));
                        mText2 = (TextView)findViewById(R.id.dispTimeText1);
                        mText2.setText(""+mPref.getInt(SAVE_TIME_HOUR+i, 0) +
                                "時" + mPref.getInt(SAVE_TIME_MINUTE+i, 0) + "分");
                        break;
                    case 2:
                        mText1 = (TextView)findViewById(R.id.dispIntervalText2);
                        mText1.setText(mPref.getString(SAVE_STATUS_INTERVAL+i,"設定情報なし"));
                        mText2 = (TextView)findViewById(R.id.dispTimeText2);
                        mText2.setText(""+mPref.getInt(SAVE_TIME_HOUR+i, 0) +
                                "時" + mPref.getInt(SAVE_TIME_MINUTE+i, 0) + "分");
                        break;
                    case 3:
                        mText1 = (TextView)findViewById(R.id.dispIntervalText3);
                        mText1.setText(mPref.getString(SAVE_STATUS_INTERVAL+i,"設定情報なし"));
                        mText2 = (TextView)findViewById(R.id.dispTimeText3);
                        mText2.setText(""+mPref.getInt(SAVE_TIME_HOUR+i, 0) +
                                "時" + mPref.getInt(SAVE_TIME_MINUTE+i, 0) + "分");
                        break;
                }
            }

            //各ボタンのクリック処理を設定
            switch (i){
                case 1:
                    mButton = (Button)findViewById(R.id.intervalEditButton1);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RootIntervalActivity.this, MakeIntervalTweetActivity.class);
                            intent.putExtra("useNumber", 1);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    mButton = (Button)findViewById(R.id.intervalEditButton2);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RootIntervalActivity.this, MakeIntervalTweetActivity.class);
                            intent.putExtra("useNumber", 2);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    mButton = (Button)findViewById(R.id.intervalEditButton3);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RootIntervalActivity.this, MakeIntervalTweetActivity.class);
                            intent.putExtra("useNumber", 3);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }

    }

    //プリファレンス情報が存在すればtrueを返す
    private boolean checkSaveData(int num) {

        //プリファレンス情報読み込み(numで設定番号を指定)
//        mPref = RootIntervalActivity.this.getSharedPreferences(SAVE_TIME,
//                Context.MODE_PRIVATE);

        return mPref.getBoolean(SAVE_FLG+num, false);
    }

}
