package com.dai1pan.Function;

import twitter4j.*;

/**
 * Created by 2140167 on 2016/11/18.
 */

public class DeleteTweet implements Runnable{
	long id;

	//コンストラクタ
	public DeleteTweet(long id){
		this.id = id; //IDが必要なので初期化する
	}

	public static void delete(Long id){
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			twitter.destroyStatus(id);
		}catch (TwitterException e){
			//twitter削除処理でエラーが発生したとき

		}
	}

	@Override
	public void run() {

	}
}
