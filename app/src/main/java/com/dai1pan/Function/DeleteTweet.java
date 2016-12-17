package com.dai1pan.Function;

import com.dai1pan.Base.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by 2140167 on 2016/11/18.
 */

public class DeleteTweet implements Runnable{

	long id;

	public DeleteTweet(Long id){
		this.id = id;
	}

    public static void delete(Long id){

        Twitter twitter = TwitterUtils.getTwitterInstance();
        try {
            twitter.destroyStatus(id);
        }catch (TwitterException e){
            //twitter削除処理でエラーが発生したとき
        }
    }

	@Override
	public void run() {
		delete(id);
	}
}
