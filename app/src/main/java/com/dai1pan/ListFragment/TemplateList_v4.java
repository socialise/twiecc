package com.dai1pan.ListFragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Function.DeleteTweet;
import com.dai1pan.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by 2140087 on 2016/12/09.
 */

public abstract class TemplateList_v4
		extends android.support.v4.app.ListFragment
{

	protected TweetAdapter mAdapter;
	protected Twitter mTwitter;
	private long mUserId;


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
		Toast.makeText(this.getContext(), "ろーどしました", Toast.LENGTH_LONG).show();
		AsyncTask<Void, Void, List<Status>> task = new async();
		task.execute();
	}

	//ﾂｲｰﾄをｸﾘｯｸした時に呼ばれるｺｰﾙﾊﾞｯｸﾒｿｯﾄﾞ
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//TODO クリックした時の動作処理記述
		Toast.makeText(getContext(),"おされた",Toast.LENGTH_SHORT).show();
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


//			parent.removeViewAt(position);

			//削除ボタンの記述
			View deleteBtn = convertView.findViewById(R.id.deleteButton);
			if (item.getUser().getId() == mUserId) {
				deleteBtn.setTag(item.getId()); //削除ボタンのタグにツイートIDを格納(long型)

				deleteBtn.setOnClickListener(new View.OnClickListener(){
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

										new Thread( new DeleteTweet((long)v.getTag()) )
												.start();

										//削除後、読み込みが早すぎると、TLに削除済みツイートが表示されるため
										//ディレイを掛ける(設定時間は要検討)
										try {
											Thread.sleep(200);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

										loadTweets(); //TL再読込
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

			//細かなツイート表示
			final User user  = item.getUser();

			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(user.getName());

			TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
			screenName.setText("@" + user.getScreenName());

			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(item.getText());

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

			icon.setImageUrl(item.getUser().getProfileImageURL());

			return convertView;
		}

	}

	protected class async extends AsyncTask<Void, Void, List<Status>>{

		@Override
		protected List<twitter4j.Status> doInBackground(Void... params) {
			try {
				mUserId = mTwitter.getId();
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
				try {
					getListView().setSelection(0);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
			//else { showToast("タイムラインの取得に失敗しました。。。"); }
		}
	}
}
