package com.dai1pan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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

	            FragmentManager fm = getSupportFragmentManager(); //フラグメントマネージャを取得
	            SearchFragment searchFragment =
			            (SearchFragment) fm.findFragmentById(R.id.listFragment);    //目的のフラグメントを取得

	            searchFragment.setSearchWord(keyword.getText().toString());         //検索ワードをフラグメントにセット
	            searchFragment.loadTweets();                                        //リストをリロード

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

}
