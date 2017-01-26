package com.dai1pan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dai1pan.Base.TwitterUtils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class WriteTweetActivity extends AppCompatActivity {

    private Twitter twitter;
    private Button button ;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tweet);

        editText = (EditText)findViewById(R.id.edittext);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                twitter =  TwitterUtils.getTwitterInstance(WriteTweetActivity.this);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newThread", new Throwable().getStackTrace()[0].getFileName() + ":" + new Throwable().getStackTrace()[0].getLineNumber());
                        try {
                            Status status = null;
                            status = twitter.updateStatus(editText.getText().toString());
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                //Toast.makeText(WriteTweetActivity.this,"TWEETしました。",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
