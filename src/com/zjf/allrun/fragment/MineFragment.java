package com.zjf.allrun.fragment;

import com.zjf.allrun.R;
import com.zjf.allrun.app.TApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

/**
 * Created by zjf 2016-6-30ÉÏÎç11:25:05
 */
public class MineFragment extends Fragment {
	View view;
	Button btnExit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_me, null);
		setViews();
		addListener();
		return view;
	}

	private void setViews() {
		btnExit = (Button) view.findViewById(R.id.btn_me_exit);

	}

	private void addListener() {
		// TODO Auto-generated method stub
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TApplication tApplication = (TApplication) getActivity()
						.getApplication();
				tApplication.finishActivity();

			}
		});
	}
}
