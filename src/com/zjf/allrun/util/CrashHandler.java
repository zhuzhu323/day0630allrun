package com.zjf.allrun.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.zjf.allrun.activity.MainActivity;
import com.zjf.allrun.app.TApplication;
/**
 *Created by zjf 2016-6-30����9:30:20
 */

public class CrashHandler implements UncaughtExceptionHandler {
	TApplication tApplication;

	public CrashHandler(TApplication tAppliction) {
		this.tApplication = tAppliction;
	}

	// �����κεط��������쳣��û��catch,
	// �ͻ�ִ��uncaughtException
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		// �ռ��쳣��Ϣ
		String info = ex.getMessage();
		Log.i("uncaughtException", "info=" + info);

		StringWriter stringWriter = new StringWriter();

		PrintWriter printWriter = new PrintWriter(stringWriter);
		// ��ϸ�쳣��Ϣ��ӡ��printWriter
		// printWriter�ְ���Ϣ��StringWirter
		// ͨ��stringWriterת��string
		ex.printStackTrace(printWriter);
		info = stringWriter.toString();
		Log.i("uncaughtException", "info=" + info);

		// �������͸���������
		// ������Աͨ����������ʱ���쳣��Ϣ

		// toast�����û�����
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(tApplication, "��Ǹ����������", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			};
		}.start();

		Log.i("uncaughtException", "toastִ������");
		try {
			Thread.currentThread().sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// ʵ����������

		Intent intent = new Intent(tApplication, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(tApplication,
				100, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		// ��ʱ��
		AlarmManager manager = (AlarmManager) tApplication
				.getSystemService(Context.ALARM_SERVICE);

		manager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
				pendingIntent);
		tApplication.finishActivity();

	}

}

