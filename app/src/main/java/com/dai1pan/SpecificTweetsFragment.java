package com.dai1pan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dai1pan.ListFragment.TemplateList_v4;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * 作成者 : 副島 祐希
 * 作成日 : 2017-01-09.
 */
public class SpecificTweetsFragment extends TemplateList_v4 {

	private static final String USER_ID = "param1";

	private long userId;

	public static SpecificTweetsFragment newInstance(long id) {
		SpecificTweetsFragment fragment = new SpecificTweetsFragment();
		Bundle args = new Bundle();
		args.putLong(USER_ID, id);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			userId = getArguments().getLong(USER_ID);

		}
	}

	@Override
	protected List<Status> setList() throws TwitterException {

		//Toast.makeText(getContext(), "" + userId, Toast.LENGTH_SHORT).show();
		return mTwitter.getUserTimeline(userId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loadTweets();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
