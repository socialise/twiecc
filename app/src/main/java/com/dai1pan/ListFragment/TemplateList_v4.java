package com.dai1pan.ListFragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Datebase.MyHelper;
import com.dai1pan.Function.DeleteTweet;
import com.dai1pan.MainActivity;
import com.dai1pan.R;
import com.loopj.android.image.SmartImageView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by 2140087 on 2016/12/09.
 */

public abstract class TemplateList_v4
        extends android.support.v4.app.ListFragment {

    protected TweetAdapter mAdapter;
    protected Twitter mTwitter;
    private long mUserId;
    private Cursor mCursor;
    private String mSQL;
    private android.os.Handler mHandler;

    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private Iterator mItr; //リストの削除用イテレータ


    /**
     * フラグメントを追加する。
     *
     * @return 目的のツイート(Status)のリストを返すように実装してください。
     * @throws TwitterException
     */
    protected abstract List<Status> setList() throws TwitterException;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //認証トークンを得てなかった場合は認証用のアクティビティに遷移する
        if (TwitterUtils.hasAccessToken(getActivity())) {
            //認証が済んでいる場合の処理
            mAdapter = new TweetAdapter(getActivity());
            setListAdapter(mAdapter);
            mTwitter = TwitterUtils.getTwitterInstance(getActivity());
            //loadTweets(); //左記のメソッドはActivity側で呼び出す

        }

        //カーソルのポインタ
        mCursor = MainActivity.DBCursor;
    }

    /**
     * ツイートを更新する
     */
    public void loadTweets() {
        //Toast.makeText(this.getContext(), "ろーどしました", Toast.LENGTH_LONG).show();
        AsyncTask<Void, Void, List<Status>> task = new async();
        task.execute();
    }

    //ﾂｲｰﾄをｸﾘｯｸした時に呼ばれるｺｰﾙﾊﾞｯｸﾒｿｯﾄﾞ
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO クリックした時の動作処理記述
        //Toast.makeText(getContext(), "おされた", Toast.LENGTH_SHORT).show();
        Status status = (Status) v.getTag();
        new AlertDialog.Builder(getActivity())
                .setTitle(status.getUser().getName() + "さんのツイート")
                .setMessage(status.getText())
                .show();
    }

    //Stringデータを管理するアダプター
    protected class TweetAdapter extends ArrayAdapter<Status> {

        private LayoutInflater mInflater;
//        private ArrayList<Integer> checked; //そえ
        private  ArrayList<Long> checked;
        private ArrayList<Long> favorited; //お気に入り表示調整リスト

        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            checked = new ArrayList<>();
            favorited = new ArrayList<>();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_tweet, null);
            }

            Status item = getItem(position);

            //削除ボタンの記述
            View deleteBtn = convertView.findViewById(R.id.deleteButton);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            if (item.getUser().getId() == mUserId) {
                deleteBtn.setTag(item.getId()); //削除ボタンのタグにツイートIDを格納(long型)
                checkBox.setTag(item.getId()); //チェックボックスのタグにもツイートIDを格納(long型)

                //以下はチェックボックス関連の処理
                //region CheckBox
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(false);
                //副島君
//                if (checked.contains(position)) {
//                    checkBox.setChecked(true);
//                }
                //酒井
                if(checked.contains(item.getId())){
                    checkBox.setChecked(true);
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //副島+酒井
                        //チェックしたツイートを削除リストに登録
//                        if (isChecked) {
//                            checked.add(position);
//                            MainActivity.deleteArray.add((Long) checkBox.getTag());
//                        } else {
//                            checked.remove(position);
//                        }
                        if (isChecked){
                            checked.add((Long)checkBox.getTag()); //表示調整リストへ登録
                            MainActivity.deleteArray.add((Long) checkBox.getTag()); //削除リストへ登録
                        }else{
                            mItr = checked.iterator();    //表示調整リストの削除処理 開始
                            while(mItr.hasNext()){
                                Long i = (Long)mItr.next();
                                if(i.equals(checkBox.getTag())) mItr.remove();
                            }   //表示調整リストの削除処理 終了
                            mItr = MainActivity.deleteArray.iterator();    //削除リストの削除処理 開始
                            while(mItr.hasNext()){
                                Long i = (Long)mItr.next();
                                if(i.equals(checkBox.getTag())) mItr.remove();
                            }   //削除リストの削除処理 終了
                        }
                    }
                });

                checkBox.setVisibility(View.VISIBLE);
                //endregion
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.INVISIBLE);
                checkBox.setVisibility(View.INVISIBLE);
            }


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //region ダイアログの作成→承認→削除
                    new AlertDialog.Builder(getActivity())
                            //.setMessage("ツイートを削除しますか？")
                            .setMessage(v.getTag().toString())
                            .setNegativeButton("キャンセル", null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new Thread(new DeleteTweet((long) v.getTag()))
                                            .start();

                                    //削除後、読み込みが早すぎると、TLに削除済みツイートが表示されるため
                                    //ディレイを掛ける(設定時間は要検討)
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    //loadTweets(); //TL再読込

                                }
                            })
                            .show();
                    //endregion
                }
            });


            //細かなツイート表示
            final User user = item.getUser();

            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(user.getName());

            TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
            screenName.setText("@" + user.getScreenName());

            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText(item.getText());

            //ユーザーサムネイル関連 クリックしたサムネイルのユーザーページへ遷移する
            SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SomebodyAdministrator.class);
                    intent.putExtra("userID", user.getId());
                    //TODO intentにパラメータをセットする
                    startActivity(intent);
                }
            });

            //お気に入りツイート用ボタン関連
            final Button FavoriteButton = (Button) convertView.findViewById(R.id.originalFavorite);
            FavoriteButton.setTag(item.getId());
            FavoriteButton.setTextColor(Color.BLACK); //お気に入りボタンの色を初期化
            //リストに存在するIDのStatusの場合,お気に入りボタンを赤色に変更(選択済み)
            if (favorited.contains(item.getId())) {
                FavoriteButton.setTextColor(Color.RED);
            }

            //お気に入り判定処理
            //カーソルのレコードポインタを先頭へ移動,レコード件数1以上でtrue
            if (mCursor.moveToFirst()) {
                do {
                    //StatusIDとDBの登録IDが一致すれば[★]ボタンのカラー変更
                    if (item.getId() ==
                            mCursor.getLong(mCursor.getColumnIndex("Favorite_Status_ID"))) {
                        FavoriteButton.setTextColor(Color.RED);
                        favorited.add(item.getId()); //表示調整リストへの追加
                        Log.d("登録したStatus ID:",  "" + item.getId());
                    }else{
                        mItr = favorited.iterator();    //表示調整リストの削除処理 開始
                        while(mItr.hasNext()){
                            Long i = (Long)mItr.next();
                            if(i.equals(item.getId())) mItr.remove();
                        }   //表示調整リストの削除処理 終了
                    }


                } while (mCursor.moveToNext());
            }

            //FavoriteButtonのクリック処理
            FavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //カーソルの再設定(select文再発行)
                    mHelper = new MyHelper(getContext());
                    mDb = mHelper.getReadableDatabase();
                    //mCursor = null;
                    String sql = MainActivity.SQL_SELECT;
                    try {
                        mCursor = mDb.rawQuery(sql, null);
                        MainActivity.DBCursor = mCursor;
                    } catch (Exception e) {
                        // Toast.makeText(this, "select:" + e.getMessage(), Toast.LENGTH_SHORT);
                        // Log.v("SQLModule select", e.toString());
                    }

                    //クリックしたStatusが既にリストに登録済みか確認
                    if (!favorited.contains(v.getTag())) {
                        FavoriteButton.setTextColor(Color.RED);
                        //お気に入り済みのindexを管理リストに格納
                        favorited.add((Long) v.getTag()); //表示調整リストへの追加
                        Log.d("登録したStatus ID:", "" + v.getTag());

                        //DBレコード登録処理
                        mHelper = new MyHelper(getContext());
                        mDb = mHelper.getWritableDatabase();
                        mSQL = "insert into t_id (Favorite_Status_ID) values" +
                                "('" + v.getTag().toString() + "');";
                        mDb.execSQL(mSQL);
                        mDb.close();
                    }else{
                        Log.v("ログ表示:削除:" + v.getTag(), "リストサイズ:" + favorited.size());

                        mItr = favorited.iterator();    //表示調整リストの削除処理 開始
                        while(mItr.hasNext()){
                            Long i = (Long)mItr.next();
                            if(i.equals(v.getTag())) mItr.remove();
                        }   //表示調整リストの削除処理 終了

                        //DBレコード削除処理
                        mHelper = new MyHelper(getContext());
                        mDb = mHelper.getWritableDatabase();
                        mSQL = "delete from t_id where(Favorite_Status_ID =" + v.getTag() + ");";
                        mDb.execSQL(mSQL);
                        mDb.close();

                        //カーソルの再設定(select文再発行)
                        mHelper = new MyHelper(getContext());
                        mDb = mHelper.getReadableDatabase();
                        //mCursor = null;
                        sql = MainActivity.SQL_SELECT;
                        try {
                            mCursor = mDb.rawQuery(sql, null);
                            MainActivity.DBCursor = mCursor;
                        } catch (Exception e) {
                           // Toast.makeText(this, "select:" + e.getMessage(), Toast.LENGTH_SHORT);
                           // Log.v("SQLModule select", e.toString());
                        }

                        //リストから削除したStatusのボタンカラーをリセット
                        FavoriteButton.setTextColor(Color.BLACK);

                        removeFavorite(TemplateList_v4.this, position);
                    }
                }
            });


            icon.setImageUrl(item.getUser().getProfileImageURL());

            return convertView;
        }

    }

    protected class async extends AsyncTask<Void, Void, List<Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(Void... params) {
            try {
                mUserId = mTwitter.getId();
                //オーバーライドされたsetListメソッドを実行する
                return setList();
            } catch (TwitterException e) {
                e.printStackTrace();
                if(e.getMessage().contains("limit exceeded")){

                    MainActivity.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (getContext() != null)
                                //Toast.makeText(getContext(), "リミット超えました", Toast.LENGTH_LONG).show();
                            Log.v("りみっとこえた", "limit exceeded");
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> result) {
            if (result != null) {
                mAdapter.clear();
                for (twitter4j.Status status : result) {
                    mAdapter.add(status);
                }
                try {
                    getListView().setSelection(0);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            //else { showToast("タイムラインの取得に失敗しました。。。"); }
        }
    }

    public void removeFavorite(ListFragment list, int position){

    }
}
