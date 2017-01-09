package com.dai1pan.ListFragment;

/**
 * Created by yutakohashi on 2016/11/28.
 */


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Function.DeleteTweet;
import com.dai1pan.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by yutakohashi on 2016/11/18.
 */

public class TimeLineFragment extends ListFragment{

	private final String TAG = getClass().getName();

    protected TweetAdapter mAdapter;
    protected Twitter mTwitter;
	protected long mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //認証トークスを得てなかった場合は認証用のアクティビティに遷移する
        if (TwitterUtils.hasAccessToken(getActivity())) {
            //認証が済んでいる場合の処理
            mAdapter = new TweetAdapter(getActivity());
            setListAdapter(mAdapter);
            mTwitter = TwitterUtils.getTwitterInstance(getActivity());
            reloadTimeLine();
        }
    }

    protected void reloadTimeLine() {
        AsyncTask<Void, Void, List<Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
	                mUserId = mTwitter.getId();
                    return mTwitter.getHomeTimeline();
                } catch (TwitterException e) {
                    e.printStackTrace();
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
	                try{
		                getListView().setSelection(0);
	                }catch (IllegalStateException e){
		                Log.v("",e.toString());
	                }

                } else {
//                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    //Stringデータを管理するアダプター
    protected class TweetAdapter extends ArrayAdapter<Status> {

        private LayoutInflater mInflater;

        public TweetAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_tweet, null);
            }

            Status item = getItem(position);

	        //削除ボタンの記述
	        View deleteBtn = convertView.findViewById(R.id.deleteButton);
	        if (item.getUser().getId() == mUserId) {
		        deleteBtn.setTag(item.getId()); //削除ボタンのタグにツイートIDを格納(long型)

                deleteBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {

	                    //region ダイアログの作成→承認→削除
	                    new AlertDialog.Builder(getActivity())
			                    .setMessage("ツイートを削除しますか？")
			                    .setNegativeButton("キャンセル", null)
			                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				                    @Override
				                    public void onClick(DialogInterface dialog, int which) {
					                    //TODO 削除処理
					                    new Thread( new DeleteTweet((long)v.getTag()) )
							                    .start();
				                    }
			                    })
			                    .show();
	                    //endregion
                    }
                });

		        deleteBtn.setVisibility(View.VISIBLE);
	        } else {
		        deleteBtn.setVisibility(View.INVISIBLE);
	        }

	        TextView name = (TextView) convertView.findViewById(R.id.name);
	        name.setText(item.getUser().getName());

	        TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
	        screenName.setText("@" + item.getUser().getScreenName());

	        TextView text = (TextView) convertView.findViewById(R.id.text);
	        text.setText(item.getText());

            SmartImageView icon = (SmartImageView) convertView.findViewById(R.id.icon);
            icon.setImageUrl(item.getUser().getProfileImageURL());

            return convertView;
        }

    }


}
