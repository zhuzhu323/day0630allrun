package com.zjf.allrun.fragment;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.zjf.allrun.R;
import com.zjf.allrun.R.string;
import com.zjf.allrun.util.BaiduMapUtil;

import android.R.integer;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zjf 2016-6-30上午11:25:05
 */
public class SportFragment extends Fragment {
	View view;
	MapView mapView;
	// 定位
	LocationClient locationClient;
	// 管理地图
	BaiduMap baiduMap;
	AlertDialog dialog;
	TextView tvAction;
	int count = 3;
	// 运动坐标
	ArrayList<LatLng> positionList = new ArrayList();
	// 显示运动统计界面
	Handler handler = new Handler();
	int sleepTime = 2000;
	Runnable runnable;
	LinearLayout linearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			// 初始化
			view = inflater.inflate(R.layout.fragment_sport, null);
			linearLayout = (LinearLayout) view
					.findViewById(R.id.ll_sport_recorder);
			tvAction = (TextView) view
					.findViewById(R.id.tv_fragment_sport_action);
			addListener();
			mapView = (MapView) view.findViewById(R.id.mapView);

		} catch (Exception e) {
		}
		baiduMap = mapView.getMap();
		// 地图单击事件，得坐标，显示地图
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng position) {
				Log.i("单击位置的纬度和经度", position.latitude + ","
						+ position.longitude);
				moveMapCenter(position);
				baiduMap.clear();
				showImage(position);
				String tv = tvAction.getText().toString();
				if ("结束".equals(tv)) {
					// 模拟用户运动
					positionList.add(position);
					if (positionList.size() >= 2) {
						PolylineOptions polylineOptions = new PolylineOptions();
						// 划线用到的点
						polylineOptions.points(positionList);
						baiduMap.clear();
						baiduMap.addOverlay(polylineOptions);
					}

				}
			}
		});
		locationClient = new LocationClient(getActivity());
		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		// 每2s得一次坐标值
		// 时间值不得低于1000ms，小于这个值，每1000ms也只获得一次坐标值
		option.setScanSpan(1);
		// 再次重新定位
		// locationClient.requestLocation();
		locationClient.setLocOption(option);
		// 让百度框架中的接口指向实现类
		MyBdLocationListener listener = new MyBdLocationListener();
		locationClient.registerLocationListener(listener);
		locationClient.start();
		return view;
	}

	// 应用暂停时，地图及定位功能停止
	@Override
	public void onPause() {
		try {

			mapView.onPause();
			locationClient.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onPause();
	}

	// 应用从新加载时，地图及定位功能开启
	@Override
	public void onResume() {
		try {

			mapView.onResume();
			locationClient.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
	}

	private void addListener() {
		tvAction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String text = ((TextView) v).getText().toString();
					// 判断单击的时候是开始还是结束
					if ("结束".equals(text)) {
						linearLayout.setVisibility(View.GONE);
						baiduMap.clear();
						positionList.clear();
						handler.removeCallbacks(runnable);
						tvAction.setText("开始");
						dialog.dismiss();

					} else {
					}
					// 把xml变成view
					View view = View.inflate(getActivity(),
							R.layout.activity_show_counter, null);
					// 创建dialog
					dialog = new AlertDialog.Builder(getActivity()).create();
					// 为dialog设置view
					dialog.setView(view);
					// 显示dialog
					dialog.show();
					final TextView tv = (TextView) view
							.findViewById(R.id.tv_show_count);
					// 每隔1s钟count-1，显示出来
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							tv.setText(String.valueOf(count));
							count = count - 1;
							if (count > -1) {
								handler.postDelayed(this, 1000);
							} else {
								dialog.dismiss();
								dialog = null;

								// 显示统计界面
								showRecorder();
							}
						}

					}, 0);
					Handler handler2 = new Handler();
					handler2.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							tvAction.setText("结束");
						}
					}, 3000);

				} catch (Exception e) {
				}

			}
		});
	}

	/**
	 * 移动中心点位置
	 * 
	 * @param currentPosition
	 */
	private void moveMapCenter(LatLng currentPosition) {
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(
				currentPosition, 17);// 第一个参数坐标点 第二个参数放大级别(4-17)
		baiduMap.animateMapStatus(update);
	}

	/**
	 * 显示图片
	 * 
	 * @param currentPosition
	 */
	private void showImage(LatLng currentPosition) {
		MarkerOptions options = new MarkerOptions();
		options.position(currentPosition);
		options.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka));
		baiduMap.addOverlay(options);
	}

	// 实现类
	class MyBdLocationListener implements BDLocationListener {
		// 定位成功后，执行
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			try {
				// 经度
				double longitude = arg0.getLongitude();
				// 纬度
				double latitude = arg0.getLatitude();
				Log.i("zjf", "经度=" + longitude + ",纬度=" + latitude);
				// 判断程序有没有得到坐标
				// 得到的是4.9E-324
				if (latitude == 4.9E-324) {
					// 模拟一个位置
					latitude = 32.541534545;
					longitude = 118.15152545;
				}

				// 移动地图中心点为当前位置
				LatLng currentPosition = new LatLng(latitude, longitude);
				moveMapCenter(currentPosition);
				// 在当前位置显示一张图
				showImage(currentPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void showRecorder() {
		linearLayout.setVisibility(View.VISIBLE);
		// 找到统计界面的控件
		// meter是时间统计
		final Chronometer meter = (Chronometer) view
				.findViewById(R.id.chronometer1);
		meter.start();
		meter.setBase(SystemClock.elapsedRealtime());
		final TextView tvDistance = (TextView) view
				.findViewById(R.id.tv_distance);
		final TextView tvSpeed = (TextView) view
				.findViewById(R.id.tv_recorder_speed);
		tvSpeed.setText("0.00");
		// 每隔2s计算数据
		runnable = new Runnable() {

			@Override
			public void run() {
				try {
					double distance = 0;
					if (positionList.size() >= 2) {
						for (int i = 0; i < positionList.size() - 1; i++) {
							double long1 = positionList.get(i).longitude;
							double lat1 = positionList.get(i).latitude;
							double long2 = positionList.get(i + 1).longitude;
							double lat2 = positionList.get(i + 1).latitude;
							distance = distance
									+ BaiduMapUtil.GetDistance(long1, lat1,
											long2, lat2);
						}
						// 将米转换成公里
						distance = distance / 1000;
						String strDistance = String.valueOf(distance);
						// 判断是否是小数，如果是保留小数后两位
						if (strDistance.contains(".")) {
							int pointIndex = strDistance.indexOf(".");
							strDistance = strDistance.substring(0,
									pointIndex + 2);
						}
						tvDistance.setText(strDistance);
						// 获得时间01:48
						String time = meter.getText().toString();
						// 得到分钟
						// [0]放的是分，[1]放的是秒
						String[] array = time.split(":");
						int minute = Integer.parseInt(array[0]);
						// 得秒
						double second = Double.parseDouble(array[1]);
						// 把秒转成小时
						double hour = (minute * 60 + second) / 3600;
						double speed = distance / hour;
						String strSpeed = String.valueOf(speed);
						if (strSpeed.contains(".")) {
							strSpeed = strSpeed.substring(0,
									strSpeed.indexOf(".") + 3);
						}
						tvSpeed.setText(strSpeed);

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					handler.postDelayed(this, sleepTime);
				}

			}
		};
		handler.postDelayed(runnable, sleepTime);
	}

}