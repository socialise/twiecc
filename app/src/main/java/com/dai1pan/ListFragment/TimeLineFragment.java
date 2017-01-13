package com.dai1pan.ListFragment;

/**
 * Created by yutakohashi on 2016/11/28.
 */


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by yutakohashi on 2016/11/18.
 */

public class TimeLineFragment extends TemplateList_v4{

	@Override
	protected List<Status> setList() throws TwitterException {
		return mTwitter.getHomeTimeline();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loadTweets();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
