package com.dai1pan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dai1pan.Base.TwitterOAuthActivity;
import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.Function.DeleteTweet;
import com.dai1pan.ListFragment.TimeLineFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //認証トークスを得てなかった場合は認証用のアクティビティに遷移する
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();


        }else{

	        //認証が成功したとき
			DeleteTweet.delete("804520682147061760");

		    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		    fab.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {

					Intent intent = new Intent(MainActivity.this, WriteTweetActivity.class);
					startActivity(intent);
			    }
		    });

	        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
			        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
			        startActivity(intent);
		        }
	        });
//            FragmentManager fragmentManager = getSupportFragmentManager();
//		    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//		    Fragment fragment = new BlankFragment();
//		    fragmentTransaction.add(R.id.fragment, fragment);
//		    fragmentTransaction.commit();




        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_white);

        //NavigationDrawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new TimeLineFragment();
                    break;
                case 1:
                    fragment = new MyTweetFragment();
                    break;
                case 2:
                    fragment = new Fragment();
                    break;
                default:
                    fragment = new Fragment();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }
    }
}