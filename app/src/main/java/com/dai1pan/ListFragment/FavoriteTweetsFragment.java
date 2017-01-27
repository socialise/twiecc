package com.dai1pan.ListFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dai1pan.Datebase.MyHelper;
import com.dai1pan.MainActivity;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * 公式の[イイネ！]と別で[お気に入りツイート]をリスト表示するフラグメント
 */
public class FavoriteTweetsFragment extends TemplateList_v4 {

    private Cursor mCursor;


    @Override
    protected List<Status> setList() throws TwitterException {

        mCursor = MainActivity.DBCursor;
        //SELECT文更新
        SQLiteOpenHelper helper = new MyHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = MainActivity.SQL_SELECT;
        try {
            mCursor = db.rawQuery(sql, null);
            MainActivity.DBCursor = mCursor;
        } catch (Exception e) {
            // Toast.makeText(this, "select:" + e.getMessage(), Toast.LENGTH_SHORT);
            // Log.v("SQLModule select", e.toString());
        }

        int i = 0;
        long[] alt = new long[mCursor.getCount()];



        //表示するツイートのStatusIDを格納
        if (mCursor.moveToFirst()) {
            do {
                alt[i] = mCursor.getLong(mCursor.getColumnIndex("Favorite_Status_ID"));
                i++;
            } while (mCursor.moveToNext());


        }
        return mTwitter.lookup(alt);
    }
        @Override
        public void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            loadTweets();
        }

    @Override
    public void removeFavorite(ListFragment list, int position) {
        super.removeFavorite(list, position);
        ArrayAdapter<Object> adapter = ((ArrayAdapter)list.getListAdapter());

        adapter.remove(adapter.getItem(position));

        //最後に触ってた
    }
}
