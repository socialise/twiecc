package com.dai1pan;

import android.os.Bundle;

import com.dai1pan.ListFragment.TemplateList_v4;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

public class MyTweetFragment extends TemplateList_v4{

	@Override
	protected List<Status> setList() throws TwitterException {
		return mTwitter.getUserTimeline();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadTweets();
	}
}
