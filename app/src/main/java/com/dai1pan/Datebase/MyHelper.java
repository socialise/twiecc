package com.dai1pan.Datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyHelper extends SQLiteOpenHelper {


	//データベースの名前
	private static String dbName = "TwieccDB";

	//id列が存在しているのを前提にしているクラスがあるので、
	//それらのクラスを使用するときには下記の文を入れる
	//_id integer primary key autoincrement
	private String createTableSQL = "CREATE TABLE t_id(" +
			"_id INTEGER primary key autoincrement, " +
			"Favorite_Status_ID TEXT" +
			");";

	public MyHelper(Context context) {
		super(context,dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
        db.execSQL(createTableSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}


}
