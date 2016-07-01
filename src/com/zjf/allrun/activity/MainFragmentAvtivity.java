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
 * Created by zjf 2016-6-30����11:36:26
 */
public class MainFragmentAvtivity extends BaseActivity{
	SportFragment sportFragment;
	ArrayList<Fragment> fragmentList = new ArrayList();
	ArrayList<Button> btnList = new ArrayList();
	// ��ǰ��ʾ��һ��fragment
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
			//��ʾ��һ��fragment
			sportFragment=new SportFragment();
			FragmentManager manager=
					getSupportFragmentManager();
			//Transaction ����
			FragmentTransaction transaction
			=manager.beginTransaction();
			//��sprotFragment��ʾ��linearLayout��
			transaction.add(R.id.fragment_container, 
					sportFragment);
			transaction.show(sportFragment);
			//�ύ��ִ��add,show
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
						//�жϵ��������Ǹ�button
						switch (v.getId()) {
						case R.id.btn_main_fragment_sport:
							clickBtn=0;
							break;
							//ctrl+alt+���¼�ͷ
						case R.id.btn_main_fragment_discover:
							clickBtn=1;
							break;
						case R.id.btn_main_fragment_me:
							clickBtn=2;
							break;
						}
						//�ж�Ҫ��Ҫ��ʾ���fragment
						if (clickBtn!=currentFragment)
						{
							Fragment fragment=
									fragmentList.get(clickBtn);
						FragmentTransaction transaction=
								getSupportFragmentManager()
								.beginTransaction();
							//�ж�fragment�Ƿ���ӹ�
							if (!fragment.isAdded()){
								transaction.add
								(R.id.fragment_container, fragment);
							}
						//������ǰ��fragment
							transaction.hide(
									fragmentList.get
									(currentFragment));
						//��ʾ�µ�fragment
							transaction.show(fragment);
							transaction.commit();
						//currentFragmentֵҪ��,�������弰ͼ����ɫ
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
