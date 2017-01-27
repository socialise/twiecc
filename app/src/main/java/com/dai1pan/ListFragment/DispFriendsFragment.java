package com.dai1pan.ListFragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.R;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by 2140261 on 2017/01/21.
 */
public class DispFriendsFragment extends Fragment {


    private static int ID_COUNT_PER_REQUEST = 5000;
    private List<Long> mFriendsIDs;
    private List<Long> mGoodFriendsIDs;

    private Twitter mTwitter;
    private User mUser;
    private LinearLayout mLinearLayout;

    private LayoutInflater mInflater;

    private final Handler mHandler = new Handler();
    private boolean mRunFlg = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_disp_friends, container, false);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.dispFriendsLinear);
        mInflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Log.d("チェック", "onCreateView");

        Runnable run = new Runnable() {
            @Override
            public void run() {

                while (mRunFlg){

                    try {
                        getFriends();
                        Log.d("フォロー数:", "" + mFriendsIDs.size());
                        Log.d("相互フォロー数:", "" + mGoodFriendsIDs.size());
                        setFriendsView();

                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }

                }



//                try {
//                    getFriends();
//                    Log.d("フォロー数:", "" + mFriendsIDs.size());
//                    Log.d("相互フォロー数:", "" + mGoodFriendsIDs.size());
//                    setFriendsView();
//
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }

            }
        };


        Thread otherThr = new Thread(run);
        otherThr.start();

        return v;

    }


    //LinearLayoutに各ビューを配置していく
    private void setFriendsView() {

        Log.d("チェック", "setFriendViewスタート");

        //相互フォロー数の同じ数だけ実行される
        for (int i = 0; i < mGoodFriendsIDs.size(); i++) {
            try {
                //ユーザー情報を相互フォローリストから取得(ひとりずつ)
                mUser = mTwitter.showUser(mGoodFriendsIDs.get(i));

                //UI操作はメインスレッドで行う為Handlerを通す
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            public void run() {

                                //ヌルポ対策
                                try{
                                    //リニアレイアウト水平----/水平A:Bで管理
                                    LinearLayout yokoLinear = new LinearLayout(getContext());
                                    yokoLinear.setOrientation(LinearLayout.HORIZONTAL);

                                    //リニア水平A面の準備(画像のみ)
                                    //プロフ画像用View生成
                                    SmartImageView icon = new SmartImageView(getContext());
//                                    Log.d("ImageURL", "" + mUser.getProfileImageURL());
                                    //プロフ画像URLをmUserから読み込み,リニア水平A面へ追加
                                    icon.setImageUrl(mUser.getProfileImageURL());
                                    yokoLinear.addView(icon);

                                    //リニア水平B面の準備(名前,ID,プロフ文章,これらを並べるリニア垂直)
                                    LinearLayout tateLinear = new LinearLayout(getContext());
                                    tateLinear.setOrientation(LinearLayout.VERTICAL);
                                    TextView name = new TextView(getContext()); //名前
                                    TextView screenName = new TextView(getContext()); //ID
                                    TextView description = new TextView(getContext()); //プロフ文章
                                    //各情報をmUserから読み込む
                                    name.setText(mUser.getName());
                                    screenName.setText("@" + mUser.getScreenName());
                                    description.setText(mUser.getDescription());
                                    //各TextViewをリニア垂直に追加し、リニア垂直をリニア水平B面へ追加
                                    tateLinear.addView(name);
                                    tateLinear.addView(screenName);
                                    tateLinear.addView(description);
                                    yokoLinear.addView(tateLinear);

                                    //動的ビューを全て取得した水平リニアを親元のリニアへ追加
                                    mLinearLayout.addView(yokoLinear);
                                }catch (NullPointerException e){

                                }
//                                //リニアレイアウト水平----/水平A:Bで管理
//                                LinearLayout yokoLinear = new LinearLayout(getContext());
//                                yokoLinear.setOrientation(LinearLayout.HORIZONTAL);
//
//                                //リニア水平A面の準備(画像のみ)
//                                //プロフ画像用View生成
//                                SmartImageView icon = new SmartImageView(getContext());
////                                    Log.d("ImageURL", "" + mUser.getProfileImageURL());
//                                //プロフ画像URLをmUserから読み込み,リニア水平A面へ追加
//                                icon.setImageUrl(mUser.getProfileImageURL());
//                                yokoLinear.addView(icon);
//
//                                //リニア水平B面の準備(名前,ID,プロフ文章,これらを並べるリニア垂直)
//                                LinearLayout tateLinear = new LinearLayout(getContext());
//                                tateLinear.setOrientation(LinearLayout.VERTICAL);
//                                TextView name = new TextView(getContext()); //名前
//                                TextView screenName = new TextView(getContext()); //ID
//                                TextView description = new TextView(getContext()); //プロフ文章
//                                //各情報をmUserから読み込む
//                                name.setText(mUser.getName());
//                                screenName.setText("@" + mUser.getScreenName());
//                                description.setText(mUser.getDescription());
//                                //各TextViewをリニア垂直に追加し、リニア垂直をリニア水平B面へ追加
//                                tateLinear.addView(name);
//                                tateLinear.addView(screenName);
//                                tateLinear.addView(description);
//                                yokoLinear.addView(tateLinear);
//
//                                //動的ビューを全て取得した水平リニアを親元のリニアへ追加
//                                mLinearLayout.addView(yokoLinear);


                            }
                        });
                    }
                }).start();


            } catch (TwitterException e) {
                e.printStackTrace();
            }


        }


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
        mFriendsIDs = new ArrayList<Long>();

        long page = 1L;
        do {
            // フォロワーが多いユーザだと無反応で寂しい＆不安なので状況表示
            Log.d((String.format("%dページ目取得中。。(%d <= %d)", page, ID_COUNT_PER_REQUEST * (page - 1),
                    ID_COUNT_PER_REQUEST * page++)), "");
            IDs = mTwitter.getFriendsIDs(targetScreenName, cursor);
            // 取得したIDをストックする
            for (long id : IDs.getIDs()) {
                mFriendsIDs.add(id);
            }

            // 次のページへのカーソル取得。ない場合は0のようだが、念のためループ条件はhasNextで見る
            cursor = IDs.getNextCursor();
        } while (IDs.hasNext());
//        output(mFriendsIDs);
        //相互フォロー中のIDのみをリストに登録
        Log.d("チェック", "gerFriends");
        checkFriendship(mFriendsIDs);

    }

    private void checkFriendship(List<Long> ids) {

        mGoodFriendsIDs = new ArrayList<Long>();

        for (long eachId : ids) {

            try {
                //自分のフォローしているユーザのID一覧から
                //相互フォローしている相手を抽出してリストに登録する
                if (mTwitter.showFriendship(mTwitter.getId(), eachId).isSourceFollowedByTarget()) {
                    mGoodFriendsIDs.add(eachId);
//                    Log.d("TAG/相互のID:","" + eachId);
                }
            } catch (TwitterException e) {
                e.printStackTrace();
            }

        }
        Log.d("チェック", "checkFriendShip");

    }

    @Override
    public void onDestroy() {
        mRunFlg = false;
        Log.d("テスト", "Destroy通過");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mRunFlg = false;
        Log.d("テスト", "Pause通過");
        super.onPause();
    }

    private static void output(List<Long> ids) {

        for (long eachId : ids) {
            Log.d("現在のID:", "" + eachId);
        }

    }

}
