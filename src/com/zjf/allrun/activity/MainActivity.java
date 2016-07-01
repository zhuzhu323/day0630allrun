package com.zjf.allrun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;

import com.zjf.allrun.R;
import com.zjf.allrun.app.TApplication;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(MainActivity.this,MainFragmentAvtivity.class));
				// TODO Auto-generated method stub
				
			}
		}, 2000);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		String s = null;
		s.toCharArray();
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
