package com.dai1pan.Base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.dai1pan.R;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtils {

    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";
	private static final String ACCOUNT_NAME = "account";

    /**
     * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
     *
     * @param context
     * @return
     */
    public static Twitter getTwitterInstance(Context context) {
        String consumerKey = context.getString(R.string.twitter_consumer_key);
        String consumerSecret = context.getString(R.string.twitter_consumer_secret);

	    SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
			    Context.MODE_PRIVATE);
	    String token = preferences.getString(TOKEN, null);
	    String tokenSecret = preferences.getString(TOKEN_SECRET, null);

	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
			    .setOAuthConsumerKey(consumerKey)
			    .setOAuthConsumerSecret(consumerSecret)
			    .setOAuthAccessToken(token)
			    .setOAuthAccessTokenSecret(tokenSecret);
	    
        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();
//        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        if (hasAccessToken(context)) {
//            twitter.setOAuthAccessToken(loadAccessToken(context));
        } else {
	        Toast.makeText(context, "アクセストークンの取得に失敗しました。", Toast.LENGTH_LONG).show();
        }
        return twitter;
    }



	public static Twitter getTwitterInstance(){
		Context context = ApplicationController.getInstance();

		String consumerKey = context.getString(R.string.twitter_consumer_key);
		String consumerSecret = context.getString(R.string.twitter_consumer_secret);

		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		String token = preferences.getString(TOKEN, null);
		String tokenSecret = preferences.getString(TOKEN_SECRET, null);

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(token)
				.setOAuthAccessTokenSecret(tokenSecret);
		TwitterFactory factory = new TwitterFactory(cb.build());
		Twitter twitter = factory.getInstance();

		if (hasAccessToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
		} else {
			Toast.makeText(context, "アクセストークンの取得に失敗しました。", Toast.LENGTH_LONG).show();
		}
		return twitter;
	}

	//管理番号ある版
	public static Twitter getTwitterInstance(int useNumber){
		Context context = ApplicationController.getInstance();

		String consumerKey = context.getString(R.string.twitter_consumer_key);
		String consumerSecret = context.getString(R.string.twitter_consumer_secret);

		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		String token = preferences.getString(TOKEN + useNumber, null);
		String tokenSecret = preferences.getString(TOKEN_SECRET + useNumber, null);

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(token)
				.setOAuthAccessTokenSecret(tokenSecret);
		TwitterFactory factory = new TwitterFactory(cb.build());
		Twitter twitter = factory.getInstance();

		if (hasAccessToken(context, useNumber)) {
			Log.v("getTwitterInstance", "hasAccessToken:true");
			twitter.setOAuthAccessToken(loadAccessToken(context, useNumber));
		} else {
			Log.v("getTwitterInstance", "hasAccessToken:false");
			Toast.makeText(context, "アクセストークンの取得に失敗しました。", Toast.LENGTH_LONG).show();
		}
		return twitter;
	}

    /**
     * アクセストークンをプリファレンスに保存します。
     *
     * @param context
     * @param accessToken
     */
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
        editor.commit();
    }

	//新生アクセストークンをプリファレンスに保存処理
	public static void storeAccessToken2(Context context, AccessToken accessToken, int useNumber) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		//プリファレンスへ保存する際、管理番号を付加する
		editor.putString(TOKEN, accessToken.getToken());
		editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
		editor.putString(TOKEN + useNumber, accessToken.getToken());
		editor.putString(TOKEN_SECRET + useNumber, accessToken.getTokenSecret());
//		editor.putString(ACCOUNT_NAME + useNumber, "");
		editor.commit();
	}


    /**
     * アクセストークンをプリファレンスから読み込みます。
     *
     * @param context
     * @return
     */
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

	//アカウント管理番号を追加した処理
	public static AccessToken loadAccessToken(Context context, int useNumber) {
		SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Log.v("token確認", TOKEN + useNumber);
		String token = preferences.getString(TOKEN + useNumber, null);
		String tokenSecret = preferences.getString(TOKEN_SECRET + useNumber, null);
		Log.v("token確認2", token + ":" + tokenSecret);
		if (token != null && tokenSecret != null) {
			//Log.v("token確認2", token + ":" + tokenSecret);
			return new AccessToken(token, tokenSecret);
		} else {
			Log.v("if結果", "ぬるでした");
			return null;
		}
	}


    /**
     * アクセストークンが存在する場合はtrueを返します。
     *
     * @return
     */
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }

	//管理追加版
	public static boolean hasAccessToken(Context context, int useNumber) {

		boolean result = loadAccessToken(context, useNumber) != null;
		Log.v("hasAccessToken result：", "" + result);
		return result;
	}
}