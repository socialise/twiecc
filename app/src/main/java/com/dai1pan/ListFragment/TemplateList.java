package com.dai1pan.ListFragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by 2140087 on 2016/12/09.
 */

public abstract class TemplateList extends ListFragment {

	protected TweetAdapter mAdapter;
	protected Twitter mTwitter;

	/**
	 * フラグメントを追加する。
	 * @return 目的のツイート(Status)のリストを返すように実装してください。
	 * @throws TwitterException
	 */
	protected abstract List<Status> setList() throws TwitterException;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//認証トークスを得てなかった場合は認証用のアクティビティに遷移する
		if (TwitterUtils.hasAccessToken(getActivity())) {
			//認証が済んでいる場合の処理
			mAdapter = new TweetAdapter(getActivity());
			setListAdapter(mAdapter);
			mTwitter = TwitterUtils.getTwitterInstance(getActivity());
			//loadTweets(); //左記のメソッドはActivity側で呼び出す

		}
	}

	/**
	 * ツイートを更新する
	 */
	public void loadTweets() {
		AsyncTask<Void, Void, List<Status>> task = new async();
		task.execute();
	}

	//ﾂｲｰﾄをｸﾘｯｸした時に呼ばれるｺｰﾙﾊﾞｯｸﾒｿｯﾄﾞ
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//TODO クリックした時の動作処理記述

		Status status = (Status) v.getTag();
		new AlertDialog.Builder( getActivity() )
				.setTitle(status.getUser().getName() + "さんのツイート")
				.setMessage( status.getText() )
		.show();
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

			convertView.setTag(item); //タグにツイート情報(Status)を格納する

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

	protected class async extends AsyncTask<Void, Void, List<twitter4j.Status>>{

		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				//オーバーライドされたsetListメソッドを実行する
				return setList();
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
				getListView().setSelection(0);
			}
			//else { showToast("タイムラインの取得に失敗しました。。。"); }
		}
	}
}
