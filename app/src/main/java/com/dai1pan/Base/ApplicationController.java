package com.dai1pan.Base;

import android.app.Application;
import android.util.Log;

/**
 * 作成者 : 副島 祐希
 * 作成日 : 2016-12-17.
 *
 * Contextを管理するクラス
 */
public class ApplicationController extends Application {
	private static ApplicationController sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;

		Log.v("ApplicationController", "稼働中");
	}

	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}
}