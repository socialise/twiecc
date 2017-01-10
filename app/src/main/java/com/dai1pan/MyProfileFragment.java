package com.dai1pan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Function.DeleteTweet;
import com.loopj.android.image.SmartImageView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	long a = 0;
	private TextView mTextProfile;
	private TextView mTextId;
	private TextView mTextName;
	private SmartImageView mImageProfile;
	private TextView mTextFollw;
	private TextView mTextFollower;
	private Button mMultiDeleteButton;

	private OnFragmentInteractionListener mListener;

	public MyProfileFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment MyProfileFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MyProfileFragment newInstance(String param1, String param2) {
		MyProfileFragment fragment = new MyProfileFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this
		View v = inflater.inflate(R.layout.fragment_myprofile_fragment, container, false);

		//各Viewの取得
		mTextName = (TextView) v.findViewById(R.id.nameText);
		mTextId = (TextView) v.findViewById(R.id.idText);
		mImageProfile = (SmartImageView) v.findViewById(R.id.profileImage);
		mTextProfile = (TextView) v.findViewById(R.id.profileText);
		mTextFollw = (TextView) v.findViewById(R.id.followText);
		mTextFollower = (TextView) v.findViewById(R.id.followerText);

		mMultiDeleteButton = (Button)v.findViewById(R.id.multiDeleteButton);

		//ツイートの複数削除用ボタン処理
		mMultiDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//アラートダイアログ部分の処理
				new AlertDialog.Builder(getActivity())
						.setMessage("選択したツイートを削除しますか？")
						.setNegativeButton("キャンセル", null)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							//ダイアログ[OK]ボタンクリック時の処理(一括削除)
							@Override
							public void onClick(DialogInterface dialog, int which) {

								Thread thread = new Thread(new Runnable() {
									@Override
									public void run() {
										for(int i = 0;i < MainActivity.deleteArray.size();i++){
											new DeleteTweet(MainActivity.deleteArray.get(i)).run();
										}

									}
								});
								thread.start();
								try {
									Thread.sleep(200);

									Intent intent = new Intent(getContext(), MainActivity.class);
									startActivity(intent);


								} catch (InterruptedException e) {
									e.printStackTrace();
								}


							}
						})
						.show();




//				Thread thread = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						for(int i = 0;i < MainActivity.deleteArray.size();i++){
//							new DeleteTweet(MainActivity.deleteArray.get(i)).run();
//						}
//
//					}
//				});
//				thread.start();
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}



			}
		});

				//自分のユーザIDを取得して引数に渡す
		final Handler handler = new Handler();
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					Log.v("TEST","run");
					final Twitter twitter = TwitterUtils.getTwitterInstance(getContext());
					final User user;
//					final User userYotti = twitter.showUser("@yothio317"); //よっちid取得用
					//twitter.showUser(twitter.getId());
					long a = twitter.getId();
					user = twitter.showUser(a);
					Log.v("tag", user.getDescription());
					//mtextProfile.setText(user.getDescription());
					handler.post(new Runnable() {
						@Override
						public void run() {
							//以下でプロフ表示
							mTextName.setText(user.getName()); //名前表示,ゆっぺ
							mTextId.setText(user.getScreenName()); //id表示,sakai_ie
							mTextProfile.setText(user.getDescription()); //プロフ文章表示
							mImageProfile.setImageUrl(user.getProfileImageURL()); //画像表示
							mTextFollw.setText(Integer.toString(user.getFriendsCount()) + "フォロー"); //フォロー数
							mTextFollower.setText(Integer.toString(user.getFollowersCount()) + "フォロワー"); //フォロワー数
							//mTextId.setText(Long.toString(userYotti.getId())); //よっちのid取得

						}
					});




				} catch (TwitterException e) {
				}
			}
		};
		Thread otherThr = new Thread(run);
		otherThr.start();


		return v;
//		return inflater.inflate(R.layout.fragment_my_fragment, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}