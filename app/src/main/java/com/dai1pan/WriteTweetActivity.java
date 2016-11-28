package com.dai1pan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class WriteTweetActivity extends AppCompatActivity {

    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tweet);

        twitter =  TwitterUtils.getTwitterInstance(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Status status = null;
                    status = twitter.updateStatus("テスト3");
                } catch (TwitterException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            }
        }).start();

//        editText.setText("");

    }
}
