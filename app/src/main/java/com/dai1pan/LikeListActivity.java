package com.dai1pan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class LikeListActivity extends AppCompatActivity {

    //private TweetAdapter mAdapter;
    private Twitter mTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_list);

        //認証トークスを得てなかった場合は認証用のアクティビティに遷移する
        if (!TwitterUtils.hasAccessToken(this))
        {
            mAdapter = new TweetAdapter(this);
            setListAdapter(mAdapter);
            mTwitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();
        }

//        Twitter twtins = TwitterUtils.getTwitterInstance(this);
//
//        try {
//             ResponseList twtres = twtins.getFavorites();
//
//        } catch (TwitterException e) {
//            e.printStackTrace();
        }
    }

