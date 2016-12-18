package com.dai1pan.Base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class ApplicationController extends Application {
	private static ApplicationController instance = null;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(getClass().getName(), "onCreate");
		instance = this;
	}

	public static ApplicationController getInstance() {
		return instance;
	}
}


