package com.dai1pan.ListFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Datebase.MyHelper;
import com.dai1pan.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by 2140261 on 2017/01/22.
 */
public class RemoveCheckFragment extends Fragment {

    private static int ID_COUNT_PER_REQUEST = 5000;
    private List<Long> mFollowersIDs;
    private List<Long> mSaveFollowersIDs;
    private List<Long> mRemoveIDs;
    private Iterator mItr;
    private Twitter mTwitter;
    private User mUser;
    private LinearLayout mLinearLayout;
//    private LinearLayout mLinearLayout2;
    private LinearLayout mTempLinear;
    private final Handler mHandler = new Handler();
    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private String mSQL;
    public Cursor mCursor; //DBからの情報読込用カーソル
    public static final String SQL_SELECT = "select distinct Save_User_ID from t_r_id";
    private boolean mRunFlg = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_remove, container, false);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.dispFriendsLinear);
        mSaveFollowersIDs = new ArrayList<Long>();
        mTempLinear = (LinearLayout)v.findViewById(R.id.specialLinear);




        Runnable run = new Runnable() {
            @Override
            public void run() {
                while(mRunFlg){

                    try {
                        try {
                            getFriends();
                        }catch (NullPointerException e){

                        }
//                        getFriends();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }

                }

//                try {
//                    getFriends();
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }

            }
        };

        Thread otherThr = new Thread(run);
        otherThr.start();


        //以下はボタンのクリック処理
        Button saveButton = (Button) v.findViewById(R.id.listUpdateButton);
        Button checkButton = (Button) v.findViewById(R.id.listCheckButton);

        //保存ボタン
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper = new MyHelper(getContext());
                mDb = mHelper.getWritableDatabase();
                for (int i = 0; i < mFollowersIDs.size(); i++) {
                    mSQL = "insert into t_r_id (Save_User_ID) values" +
                            "('" + mFollowersIDs.get(i) + "');";
                    mDb.execSQL(mSQL);
                }
                mDb.close();
            }
        });

        //確認ボタン
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkRemove();
                Log.d("リムチェック:", "" + mSaveFollowersIDs);

            }
        });


        return v;
    }


    private void getFriends() throws TwitterException {
        mTwitter = TwitterUtils.getTwitterInstance(getContext());
        mUser = mTwitter.showUser(mTwitter.getId());

        // 取得対象のユーザ名
        String targetScreenName = mUser.getScreenName();
        // カーソル初期値。現状のt4jのjavadocは 1オリジンだが、Twitter API Documentでは -1オリジンなのでそちらに準拠
        long cursor = -1L;
        // 一時的にIDを格納するオブジェクト
        IDs IDs;
        // IDを全てストックするオブジェクト
        mFollowersIDs = new ArrayList<Long>();

        long page = 1L;
        do {
            // フォロワーが多いユーザだと無反応で寂しい＆不安なので状況表示
            Log.d((String.format("%dページ目取得中。。(%d <= %d)", page, ID_COUNT_PER_REQUEST * (page - 1),
                    ID_COUNT_PER_REQUEST * page++)), "");
            IDs = mTwitter.getFollowersIDs(targetScreenName, cursor);
            // 取得したIDをストックする
            for (long id : IDs.getIDs()) {
                mFollowersIDs.add(id);
            }

            // 次のページへのカーソル取得。ない場合は0のようだが、念のためループ条件はhasNextで見る
            cursor = IDs.getNextCursor();
        } while (IDs.hasNext());
//        output(mFriendsIDs);
        Log.d("チェック", "現フォロワのID取得完了");
        //DB関連処理
        SQLiteOpenHelper helper = new MyHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        mCursor = null;
        String sql = SQL_SELECT;
        Log.d("カーソルの中身確認", "");
        try {
            mCursor = db.rawQuery(sql, null);
            Log.d("カーソルの中身確認", "");

        } catch (Exception e) {
            Log.v("SQLModule select", e.toString());
        }

        if (mCursor.moveToFirst()) {

            do {
                mSaveFollowersIDs.add(mCursor.getLong(mCursor.getColumnIndex("Save_User_ID")));
//                Log.d("DB苦戦", ""+ mCursor.getLong(mCursor.getColumnIndex("Save_User_ID")));
            } while (mCursor.moveToNext());

        }

        Log.d("保存してたリスト",""+mSaveFollowersIDs);
        Log.d("今のリスト",""+mFollowersIDs);

        setFollowersView();

    }


    //LinearLayoutに各ビューを配置していく
    private void setFollowersView() {

        Log.d("チェック", "setFriendViewスタート");

        //フォロワ数の数だけ実行される
        for (int i = 0; i < mFollowersIDs.size(); i++) {
            try {
                //ユーザー情報をフォロワリストから取得
                mUser = mTwitter.showUser(mFollowersIDs.get(i));

                //UI操作はメインスレッドで行う為Handlerを通す
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            public void run() {

                                //エラー対策,ヌルポ用
                                try{
                                    //リニアレイアウト水平----/水平A:Bで管理
                                    LinearLayout yokoLinear = new LinearLayout(getActivity().getApplication());
                                    yokoLinear.setOrientation(LinearLayout.HORIZONTAL);

                                    //リニア水平A面(名前表示
                                    TextView name = new TextView(getContext()); //名前
                                    name.setText(mUser.getName() + " / ");
                                    yokoLinear.addView(name);

                                    //リニア水平B面の準備(ID)
                                    TextView screenName = new TextView(getContext()); //ID
                                    screenName.setText("@" + mUser.getScreenName());
                                    yokoLinear.addView(screenName);
                                    //動的ビューを全て取得した水平リニアを親元のリニアへ追加
                                    mLinearLayout.addView(yokoLinear);
                                    //mLinearLayout.removeView(yokoLinear);
                                    //mTempLinear = yokoLinear;
                                }catch (NullPointerException e){

                                }

//                                //リニアレイアウト水平----/水平A:Bで管理
//                                LinearLayout yokoLinear = new LinearLayout(getActivity().getApplication());
//                                yokoLinear.setOrientation(LinearLayout.HORIZONTAL);
//
//                                //リニア水平A面(名前表示
//                                TextView name = new TextView(getContext()); //名前
//                                name.setText(mUser.getName() + " / ");
//                                yokoLinear.addView(name);
//
//                                //リニア水平B面の準備(ID)
//                                TextView screenName = new TextView(getContext()); //ID
//                                screenName.setText("@" + mUser.getScreenName());
//                                yokoLinear.addView(screenName);
//                                //動的ビューを全て取得した水平リニアを親元のリニアへ追加
//                                mLinearLayout.addView(yokoLinear);
//                                //mLinearLayout.removeView(yokoLinear);
//                                //mTempLinear = yokoLinear;


                            }
                        });
                    }
                }).start();


            } catch (TwitterException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onPause() {
        mRunFlg = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRunFlg = false;
        super.onDestroy();
    }

    //現在のフォロワと保存されたフォロワのリストを比較する
    private void checkRemove() {
        //リムーブしたユーザのIDのみリストに残す
        mSaveFollowersIDs.removeAll(mFollowersIDs);
        Log.d("かぶりを消したあとのリスト", "" + mSaveFollowersIDs);

        //表示されていた現在のフォロワリストを非表示にする
        //ビューを削除する
        mTempLinear.removeView(mLinearLayout);

        Runnable run = new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < mSaveFollowersIDs.size(); i++) {
                    try {
                        //ユーザー情報をフォロワリストから取得
                        mUser = mTwitter.showUser(mSaveFollowersIDs.get(i));

                        //UI操作はメインスレッドで行う為Handlerを通す
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.post(new Runnable() {
                                    public void run() {

                                        //エラー処理, ヌルポ対策
                                        try{
                                            //リニアレイアウト水平----/水平A:Bで管理
                                            LinearLayout yokoLinear = new LinearLayout(getContext());
                                            yokoLinear.setOrientation(LinearLayout.HORIZONTAL);

                                            //リニア水平A面(名前表示
                                            TextView name = new TextView(getContext()); //名前
                                            name.setText(mUser.getName() + " / ");
                                            yokoLinear.addView(name);

                                            //リニア水平B面の準備(ID)
                                            TextView screenName = new TextView(getContext()); //ID
                                            screenName.setText("@" + mUser.getScreenName());
                                            yokoLinear.addView(screenName);
                                            //動的ビューを全て取得した水平リニアを親元のリニアへ追加
                                            mTempLinear.addView(yokoLinear);
                                            //mLinearLayout.removeView(yokoLinear);
                                            //mTempLinear = yokoLinear;
                                        }catch (NullPointerException e){

                                        }


//                                        //リニアレイアウト水平----/水平A:Bで管理
//                                        LinearLayout yokoLinear = new LinearLayout(getContext());
//                                        yokoLinear.setOrientation(LinearLayout.HORIZONTAL);
//
//                                        //リニア水平A面(名前表示
//                                        TextView name = new TextView(getContext()); //名前
//                                        name.setText(mUser.getName() + " / ");
//                                        yokoLinear.addView(name);
//
//                                        //リニア水平B面の準備(ID)
//                                        TextView screenName = new TextView(getContext()); //ID
//                                        screenName.setText("@" + mUser.getScreenName());
//                                        yokoLinear.addView(screenName);
//                                        //動的ビューを全て取得した水平リニアを親元のリニアへ追加
//                                        mTempLinear.addView(yokoLinear);
//                                        //mLinearLayout.removeView(yokoLinear);
//                                        //mTempLinear = yokoLinear;


                                    }
                                });
                            }
                        }).start();


                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }

                }

            }
        };

        Thread otherThr = new Thread(run);
        otherThr.start();



        //リムーブが判明したユーザー情報のみ表示する
//        for (int i = 0; i < mSaveFollowersIDs.size(); i++) {
//            try {
//                //ユーザー情報をフォロワリストから取得
//                mUser = mTwitter.showUser(mSaveFollowersIDs.get(i));
//
//                //UI操作はメインスレッドで行う為Handlerを通す
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mHandler.post(new Runnable() {
//                            public void run() {
//
//                                //リニアレイアウト水平----/水平A:Bで管理
//                                LinearLayout yokoLinear = new LinearLayout(getContext());
//                                yokoLinear.setOrientation(LinearLayout.HORIZONTAL);
//
//                                //リニア水平A面(名前表示
//                                TextView name = new TextView(getContext()); //名前
//                                name.setText(mUser.getName() + " / ");
//                                yokoLinear.addView(name);
//
//                                //リニア水平B面の準備(ID)
//                                TextView screenName = new TextView(getContext()); //ID
//                                screenName.setText("@" + mUser.getScreenName());
//                                yokoLinear.addView(screenName);
//                                //動的ビューを全て取得した水平リニアを親元のリニアへ追加
//                                mTempLinear.addView(yokoLinear);
//                                //mLinearLayout.removeView(yokoLinear);
//                                //mTempLinear = yokoLinear;
//
//
//                            }
//                        });
//                    }
//                }).start();
//
//
//            } catch (TwitterException e) {
//                e.printStackTrace();
//            }
//
//        }


    }

}
