package com.dai1pan;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dai1pan.ListFragment.SearchFragment;

/**
 * IE3A11　下村怜大
 */

public class SearchActivity extends AppCompatActivity {

    private InputMethodManager inputMethodManager;
    private EditText keyword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seach_layout);
        //キーボード表示を制御するためのオブジェクト
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        keyword = (EditText) findViewById(R.id.keyword);
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        //EditTextにリスナーをセット
        keyword.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

	            FragmentManager fm = getFragmentManager(); //フラグメントマネージャを取得
	            SearchFragment searchFragment =
			            (SearchFragment) fm.findFragmentById(R.id.listFragment);    //目的のフラグメントを取得

	            searchFragment.setSearchWord(keyword.getText().toString());         //検索ワードをフラグメントにセット
	            searchFragment.loadTweets();                                            //リストをリロード

//                Runnable run = new Runnable(){
//                    @Override
//                    public void run(){
//                        search(keyword.getText().toString());
//                    }
//                };
//	            search(keyword.getText().toString());

//                Thread otherThr = new Thread(run); //スレッドのインスタンスに処理内容を格納
//                otherThr.start();

                final String text = keyword.getText().toString();
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(keyword.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    return true;
                }

                return false;
            }

        });



    }

    public void search(String text){

//        try {

//	        fragmentTransaction.add(R.id.frame, new TestFragment());

//            //自動的に認証してくれる
//            //（バージョン2.2.4以降はgetInstance()ではなくgetSingleton()を推奨）
//            Twitter twitter = TwitterUtils.getTwitterInstance(this);
//            Query query = new Query();
//
//            query.setQuery(text);// キーワードが含まれているツイート取得
//            query.setLang("ja");// ユーザが設定した使用言語
//
//            //検索結果を取得
//            QueryResult result = twitter.search(query);
//            List<Status> tresult = result.getTweets();
//
//            //ツイートの表示
//            for (Status tweet : tresult) {
//                Log.v("Search", "@" + tweet.getUser().getScreenName() + ":" + tweet.getText());
//            }
//
//            } catch(TwitterException te){
//                te.printStackTrace();
//            } catch(Exception e){
//                e.printStackTrace();
//            }

    }

}
