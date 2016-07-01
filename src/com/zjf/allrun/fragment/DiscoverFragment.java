package com.zjf.allrun.fragment;

import com.zjf.allrun.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zjf 2016-6-30ионГ11:25:05
 */
public class DiscoverFragment extends Fragment {
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view	=inflater.inflate(R.layout.fragment_discover, null);
				return view;
	}
}
