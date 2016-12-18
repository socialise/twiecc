package com.dai1pan.Base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
		Context context = MyContext.getApplicationContext();

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

    /**
     * アクセストークンが存在する場合はtrueを返します。
     *
     * @return
     */
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }
}