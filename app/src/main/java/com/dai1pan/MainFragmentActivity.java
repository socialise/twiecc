package com.dai1pan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main2);
	    //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	    //setSupportActionBar(toolbar);

	    if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();

        }else{

	        //認証が成功したとき
		    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		    fab.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {

					Intent intent = new Intent(MainFragmentActivity.this, WriteTweetActivity.class);
					startActivity(intent);
			    }
		    });
            FragmentManager fragmentManager = getFragmentManager();
		    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		    Fragment fragment = new BlankFragment();
		    fragmentTransaction.add(R.id.fragment, fragment);
		    fragmentTransaction.commit();



        }

    }


}
