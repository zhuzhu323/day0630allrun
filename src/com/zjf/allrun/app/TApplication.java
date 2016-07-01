package com.zjf.allrun.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.os.Process;

import com.baidu.mapapi.SDKInitializer;
import com.zjf.allrun.util.CrashHandler;

/**
 *Created by zjf 2016-6-30����9:34:54
 */
public class TApplication extends Application{
	public ArrayList<Activity> al=new ArrayList<Activity>();
	public void finishActivity() {
	for(Activity activity:al){
		try {
			activity.finish();
		} catch (Exception e) {
			// TODO: handle exception
		} 
	}
	Process.killProcess(Process.myPid());
	}
@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	//��ʼ����ͼ
	SDKInitializer.initialize(this);
//	CrashHandler crashHandler=new CrashHandler(this);
//	//�����쳣û�м�catch,��ܵ���crashHandler
//	Thread.setDefaultUncaughtExceptionHandler(crashHandler);
}
}
