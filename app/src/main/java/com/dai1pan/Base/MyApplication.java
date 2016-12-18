package com.dai1pan.Base;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	// Application#onCreateは、ActivityやServiceが生成される前に呼ばれる。
	// だから、ここでシングルトンを生成すれば問題ない
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(getClass().getName(), "テストoncreate");
		MyContext.onCreateApplication(getApplicationContext());
	}
}
