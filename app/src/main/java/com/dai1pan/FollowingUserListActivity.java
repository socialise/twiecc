package com.dai1pan;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dai1pan.Base.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class FollowingUserListActivity extends ListActivity {

    private String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_user_list);

        screenName = getIntent().getStringExtra("SCREEN_NAME");

        List<User> forrowingList = getFollowingUsers2(TwitterUtils.getTwitterInstance(),screenName);
        
    }


    /**
     * this code gets users who follow a authorized user in twitter with twitter4j.
     * @param twitter
     * @param screenName
     * @return if a error occurs, this method returns null
     */
    public static ArrayList<User> getFollowingUsers2(Twitter twitter, String screenName){
        long start = System.currentTimeMillis();
        long end = 0;
        PagableResponseList<User> rawData = null;
        ArrayList<twitter4j.User> dataToReturn = new ArrayList<twitter4j.User>();
        int apiCallCount = 0;
        int continuousErrorCount = 0;
        boolean isLastAPICallSuccess = true;
        long lastAPICallSuccessTime = 0;

        long cursor = -1;
        while(true){
            try {
                if(isLastAPICallSuccess)
                    lastAPICallSuccessTime = System.currentTimeMillis();

                rawData = twitter.getFriendsList(screenName, cursor);
                apiCallCount++;
            } catch (TwitterException e) {
                isLastAPICallSuccess = false;
                String errorCode = e.getMessage().substring(0, 3);
                if(errorCode.startsWith("5") || errorCode.startsWith("4")) {
                    continuousErrorCount++;
                    if(continuousErrorCount >= 3) {
                        System.err.println("return null because of three continuous error");
                        return null;
                    }
                    long currentTime = System.currentTimeMillis();
                    if(currentTime - lastAPICallSuccessTime > 3000){
                        System.err.println("return null because of The interval of the error is too long. " + (double)(currentTime - lastAPICallSuccessTime)/1000 + "seconds");
                        return null;
                    }

                    System.err.println(e.getMessage());
                    continue;
                }

                end = System.currentTimeMillis();
                System.err.println("error " + apiCallCount + ", " + screenName + ", " + (double)(end - start)/1000 + "seconds " + ": " + e.getMessage());
                return null;
            }
            isLastAPICallSuccess = true;
            continuousErrorCount = 0;

            if(rawData == null || rawData.isEmpty())
                break;

            dataToReturn.addAll(rawData);
            System.out.println(screenName + ", " + cursor + ", " + (double)(System.currentTimeMillis() - lastAPICallSuccessTime)/1000 + "seconds");

            if(!rawData.hasNext())
                break;

            cursor = rawData.getNextCursor();
        }
        end = System.currentTimeMillis();
        System.out.println("" + screenName + " time:" + (double)(end - start)/1000 + "seconds " + apiCallCount + "counts, " + dataToReturn.size());

        return dataToReturn;
    }



}
