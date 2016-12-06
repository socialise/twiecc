package com.dai1pan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * IE3A11　下村怜大
 */

public class SearchActivity extends AppCompatActivity {

    private InputMethodManager inputMethodManager;
    EditText keyword;

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
                Runnable run = new Runnable(){
                    @Override
                    public void run(){
                        search(keyword.getText().toString());
                    }
                };
                Thread otherThr = new Thread(run); //スレッドのインスタンスに処理内容を格納
                otherThr.start();

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

        try {
            //自動的に認証してくれる
            //（バージョン2.2.4以降はgetInstance()ではなくgetSingleton()を推奨）
            Twitter twitter = TwitterUtils.getTwitterInstance(this);
            Query query = new Query();

            query.setQuery(text);// キーワードが含まれているツイート取得
            query.setLang("ja");// ユーザが設定した使用言語

            //検索結果を取得
            QueryResult result = twitter.search(query);
            List<Status> tresult = result.getTweets();

            //ツイートの表示
            for (Status tweet : tresult) {
                Log.v("Search", "@" + tweet.getUser().getScreenName() + ":" + tweet.getText());
            }

            } catch(TwitterException te){
                te.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }

    }

}
