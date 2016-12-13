package com.dai1pan.ListFragment;

import android.os.Bundle;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * 作成者 : 副島 祐希
 * 作成日 : 2016-12-10.
 */
public class SearchFragment extends TemplateTweets{

	private static final String TEXT_ARG = "text";
	private String mText;

	public void setSearchWord(String text){
		mText = text;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (getArguments() != null){
			mText = getArguments().getString(TEXT_ARG);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected List<Status> setList() {

		try {
			Query query = new Query();

			query.setQuery(mText);// キーワードが含まれているツイート取得
			query.setLang("ja");// ユーザが設定した使用言語

			//検索結果を取得
			QueryResult result = mTwitter.search(query);

			return result.getTweets();

		} catch (TwitterException e) {
			e.printStackTrace();
			return  null;
		}
	}
}
