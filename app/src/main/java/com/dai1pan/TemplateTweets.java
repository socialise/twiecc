package com.dai1pan;

import android.os.AsyncTask;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by 2140087 on 2016/12/09.
 */

public abstract class TemplateTweets extends MainListFragment {

    @Override
    protected void reloadTimeLine() {
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return setList();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    mAdapter.clear();
                    for (twitter4j.Status status : result) {
                        mAdapter.add(status);
                    }
                    getListView().setSelection(0);
                } else {
//                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    abstract public List<twitter4j.Status> setList() throws TwitterException;
}
