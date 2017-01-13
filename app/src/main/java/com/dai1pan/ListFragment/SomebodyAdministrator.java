package com.dai1pan.ListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.dai1pan.R;
import com.dai1pan.SomebodyProfileFragment;
import com.dai1pan.SomebodyTweetsFragment;

public class SomebodyAdministrator extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		Intent intent = getIntent();
		long id = intent.getLongExtra("userID", 0);


		Fragment fragment2 =  SomebodyProfileFragment.newInstance(id);
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.profile, fragment2, "fragment");
		//transaction.commit();


		Fragment fragment =  SomebodyTweetsFragment.newInstance(id);
		transaction.add(R.id.tweet, fragment, "fragment");
		transaction.commit();

	}


}
