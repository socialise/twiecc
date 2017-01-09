package com.dai1pan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class test extends AppCompatActivity {
	//        2407902008L
	long id = 2407902008L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		Fragment fragment =  SpecificTweetsFragment.newInstance(2407902008L);
		android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.test, fragment, "fragment");
		transaction.commit();

	}


}
