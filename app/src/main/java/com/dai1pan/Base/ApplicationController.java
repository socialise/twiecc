package com.dai1pan.Base;

import android.app.Application;

/**
 * 作成者 : 副島 祐希
 * 作成日 : 2016-12-17.
 */
public class ApplicationController extends Application {
	private static ApplicationController sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

	public static synchronized ApplicationController getInstance() {
		return sInstance;
	}
}