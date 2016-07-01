package com.zjf.allrun.activity;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zjf.allrun.R;
import com.zjf.allrun.fragment.DiscoverFragment;
import com.zjf.allrun.fragment.MineFragment;
import com.zjf.allrun.fragment.SportFragment;

/**
 * Created by zjf 2016-6-30上午11:36:26
 */
public class MainFragmentAvtivity extends BaseActivity{
	SportFragment sportFragment;
	ArrayList<Fragment> fragmentList = new ArrayList();
	ArrayList<Button> btnList = new ArrayList();
	// 当前显示第一个fragment
	int currentFragment = 0;
	int clickBtn;
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int orientation=newConfig.orientation;
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle arg0) {	
		super.onCreate(arg0);
		try {
			this.setContentView(R.layout.main_fragment);
			//显示第一个fragment
			sportFragment=new SportFragment();
			FragmentManager manager=
					getSupportFragmentManager();
			//Transaction 事务
			FragmentTransaction transaction
			=manager.beginTransaction();
			//把sprotFragment显示在linearLayout中
			transaction.add(R.id.fragment_container, 
					sportFragment);
			transaction.show(sportFragment);
			//提交，执行add,show
			transaction.commit();
			
			DiscoverFragment discoverFragment=new DiscoverFragment();
			MineFragment meFragment=new MineFragment();
			fragmentList.add(sportFragment);
			fragmentList.add(discoverFragment);
			fragmentList.add(meFragment);
			
			Button sportBtn=(Button) 
					findViewById
					(R.id.btn_main_fragment_sport);
			Button discoverBtn=(Button) 
					findViewById
					(R.id.btn_main_fragment_discover);
			Button mineBtn=(Button) 
					findViewById
					(R.id.btn_main_fragment_me);
			btnList.add(sportBtn);
			btnList.add(discoverBtn);
			btnList.add(mineBtn);
			btnList.get(currentFragment).setSelected(true);
			for(Button btn:btnList)
			{
				btn.setOnClickListener(
						new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//判断单击的是那个button
						switch (v.getId()) {
						case R.id.btn_main_fragment_sport:
							clickBtn=0;
							break;
							//ctrl+alt+向下箭头
						case R.id.btn_main_fragment_discover:
							clickBtn=1;
							break;
						case R.id.btn_main_fragment_me:
							clickBtn=2;
							break;
						}
						//判断要不要显示别的fragment
						if (clickBtn!=currentFragment)
						{
							Fragment fragment=
									fragmentList.get(clickBtn);
						FragmentTransaction transaction=
								getSupportFragmentManager()
								.beginTransaction();
							//判断fragment是否添加过
							if (!fragment.isAdded()){
								transaction.add
								(R.id.fragment_container, fragment);
							}
						//隐藏以前的fragment
							transaction.hide(
									fragmentList.get
									(currentFragment));
						//显示新的fragment
							transaction.show(fragment);
							transaction.commit();
						//currentFragment值要改,更改字体及图标颜色
							btnList.get(currentFragment).setSelected(false);
							btnList.get(clickBtn).setSelected(true);
							currentFragment=clickBtn;
						}
					}
				});
			}
			
			
		} catch (Exception e) {
			
		}
	}
	}
