package com.dai1pan.ListFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by 2140148 on 2017/01/10.
 */

public class LikeListFragment  extends  TemplateList_v4{
    @Override
    protected List<Status> setList() throws TwitterException {
        return mTwitter.getFavorites();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadTweets(); //ここでﾛｰﾄﾞしないとリストが表示されない
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
