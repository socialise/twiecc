package com.dai1pan;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Function.DeleteTweet;
import com.dai1pan.ListFragment.TemplateList_v4;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;

/**
 * Created by 2140261 on 2017/01/12.
 */
public class MyListMenuFragment extends TemplateList_v4 {
    private static final String TAG = MyListMenuFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private List<UserList> mList;
//    private List<Status> mrList;


    @Override
    protected List<Status> setList() throws TwitterException{

        final Twitter twitter = TwitterUtils.getTwitterInstance(getContext());
        try {
            // 鍵垢のひとだとmListがnullになるので、公開垢を指定する
            mList = twitter.getUserLists("@TECHNOTAK");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        UserList list = mList.get(0);
        Log.d("MyListMenuFragment", list.getName());

        try {
            List<Status> tmp =  twitter.getUserListStatuses(list.getId(),new Paging(1, 40));
            Log.d(TAG, "" + tmp.size());
            return tmp;
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        Log.d("MyListMenuFragment", list.getName());
        return new ArrayList<>();

//        return (List<Status>)list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTweets();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}