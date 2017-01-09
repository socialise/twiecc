package com.dai1pan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 作成者 : 副島 祐希
 * 作成日 : 2017-01-09.
 */
public class LogOutButtonFragment extends Fragment{

	private Button mBtn;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.logout_test, container, false);

		mBtn = (Button)v.findViewById(R.id.logoutButtonTest);

		return v;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

	}




}
