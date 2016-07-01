package com.zjf.allrun.activity;

import com.zjf.allrun.app.TApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by zjf 2016-7-1ÏÂÎç4:27:09
 */
public class BaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		try {
			TApplication tApplication = (TApplication) getApplication();
			tApplication.al.add(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {
		try {
			TApplication tApplication = (TApplication) getApplication();
			tApplication.al.remove(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}
}
