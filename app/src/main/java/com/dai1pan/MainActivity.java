package com.dai1pan;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.dai1pan.Base.TwitterOAuthActivity;
import com.dai1pan.Base.TwitterUtils;
import com.dai1pan.ListFragment.LikeListFragment;
import com.dai1pan.ListFragment.TimeLineFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //認証トークンを得てなかった場合は認証用のアクティビティに遷移する
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
	        //アカウント管理テスト
	        //初回起動時(認証トークンを一つも得ていない場合はアカウント1番への登録として
	        //認証アクティビティへ飛ばす
	        intent.putExtra("useAccountNumber", 1);
            startActivity(intent);
            finish();
        }else{
	        //認証が成功したとき
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
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_white);

	    AlertDialog.Builder mListDlg;

        //NavigationDrawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//	            String itemId;
	            switch (menuItem.getItemId()){

		            case R.id.navigation_item_account :
			            final CharSequence[] items = {"新規アカウント", "新規アカウント", "新規アカウント"};
			            AlertDialog.Builder listDlg = new AlertDialog.Builder(MainActivity.this);
			            listDlg.setTitle("アカウント切替");
			            listDlg.setItems(
					            items,
					            new DialogInterface.OnClickListener() {
						            public void onClick(DialogInterface dialog, int which) {
							            // リスト選択時の処理
							            // which は、選択されたアイテムのインデックス

							            switch (which){
								            case 0:
									            //選択したアカウント番号が認証済みが確認
									            if (!TwitterUtils.hasAccessToken(MainActivity.this, 1)) {
										            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
										            //アカウント管理テスト
										            //初回起動時(認証トークンを一つも得ていない場合はアカウント1番への登録として
										            //認証アクティビティへ飛ばす
										            intent.putExtra("useAccountNumber", 1);
										            startActivity(intent);
										            finish();
									            }
									            break;

								            case 1:
									            if (!TwitterUtils.hasAccessToken(MainActivity.this, 2)) {
										            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
										            //アカウント管理テスト
										            //初回起動時(認証トークンを一つも得ていない場合はアカウント1番への登録として
										            //認証アクティビティへ飛ばす
										            intent.putExtra("useAccountNumber", 2);
										            startActivity(intent);
										            finish();
									            }
									            break;

								            case 2:
									            if (!TwitterUtils.hasAccessToken(MainActivity.this, 3)) {
										            Intent intent = new Intent(MainActivity.this, TwitterOAuthActivity.class);
										            //アカウント管理テスト
										            //初回起動時(認証トークンを一つも得ていない場合はアカウント1番への登録として
										            //認証アクティビティへ飛ばす
										            intent.putExtra("useAccountNumber", 3);
										            startActivity(intent);
										            finish();
									            }
									            break;

							            }

						            }
					            });

			            // 表示
			            listDlg.create().show();


	            }

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
	                Log.v(getClass().getName(), "フラグメント作成 Tab0");
                    fragment = new TimeLineFragment();
                    break;
                case 1:
	                Log.v(getClass().getName(), "フラグメント作成");
	                fragment = new MyInfoRootFragment();
                    break;
                case 2:
                    fragment = new MyProfileFragment();
                    break;
                default:
                    fragment = new Fragment();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }
    }
}