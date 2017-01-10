package com.dai1pan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dai1pan.Base.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

//アカウント切り替えの際、MainActivityを呼び直す為のActivity

public class AccountChangeDummy extends AppCompatActivity {

	private static final String TOKEN = "token";
	private static final String TOKEN_SECRET = "token_secret";
	private static final String PREF_NAME = "twitter_access_token";
	private static final String SELECT = "number";
	private static final String ACCOUNT_NAME = "account";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Runnable run = new Runnable() {
			public void run() {

				try{
					SharedPreferences preferences = AccountChangeDummy.this.getSharedPreferences(PREF_NAME,
							Context.MODE_PRIVATE);

					int use = preferences.getInt(SELECT, 1);
					final Twitter twitter = TwitterUtils.getTwitterInstance(use);
					final User user;
					long a = twitter.getId();
					user = twitter.showUser(a);

					//読み込み処理
					String token = preferences.getString(TOKEN + use, null);
					String tokenSecret = preferences.getString(TOKEN_SECRET + use, null);

					//書き込み
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString(TOKEN, token);
					editor.putString(TOKEN_SECRET, tokenSecret);
					editor.putString(ACCOUNT_NAME + use, user.getScreenName());
					editor.commit();

					Intent intent = new Intent(AccountChangeDummy.this, MainActivity.class);
					startActivity(intent);

				}catch (TwitterException e) {
				}

			}
		};
		Thread otherThr = new Thread(run);
		otherThr.start();

//		try{
//			SharedPreferences preferences = AccountChangeDummy.this.getSharedPreferences(PREF_NAME,
//					Context.MODE_PRIVATE);
//
//			int use = preferences.getInt(SELECT, 1);
//			final Twitter twitter = TwitterUtils.getTwitterInstance(use);
//			final User user;
//			long a = twitter.getId();
//			user = twitter.showUser(a);
//
//			//読み込み処理
//			String token = preferences.getString(TOKEN + use, null);
//			String tokenSecret = preferences.getString(TOKEN_SECRET + use, null);
//
//			//書き込み
//			SharedPreferences.Editor editor = preferences.edit();
//			editor.putString(TOKEN, token);
//			editor.putString(TOKEN_SECRET, tokenSecret);
//			//editor.putString(ACCOUNT_NAME + use, user.getScreenName());
//			editor.commit();
//
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//
//		}catch (TwitterException e) {
//		}




	}



	//使用する認証をプリファレンスから読み込む
//	public static AccessToken loadAccessToken(Context context, int useNumber) {
//		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
//				Context.MODE_PRIVATE);
//
//		String token = preferences.getString(TOKEN + useNumber, null);
//		String tokenSecret = preferences.getString(TOKEN_SECRET + useNumber, null);
//
//		if (token != null && tokenSecret != null) {
//			return new AccessToken(token, tokenSecret);
//		} else {
//			return null;
//		}
//	}
}
