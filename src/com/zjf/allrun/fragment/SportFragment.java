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
 * Created by zjf 2016-6-30����11:25:05
 */
public class SportFragment extends Fragment {
	View view;
	MapView mapView;
	// ��λ
	LocationClient locationClient;
	// �����ͼ
	BaiduMap baiduMap;
	AlertDialog dialog;
	TextView tvAction;
	int count = 3;
	// �˶�����
	ArrayList<LatLng> positionList = new ArrayList();
	// ��ʾ�˶�ͳ�ƽ���
	Handler handler = new Handler();
	int sleepTime = 2000;
	Runnable runnable;
	LinearLayout linearLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			// ��ʼ��
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
		// ��ͼ�����¼��������꣬��ʾ��ͼ
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng position) {
				Log.i("����λ�õ�γ�Ⱥ;���", position.latitude + ","
						+ position.longitude);
				moveMapCenter(position);
				baiduMap.clear();
				showImage(position);
				String tv = tvAction.getText().toString();
				if ("����".equals(tv)) {
					// ģ���û��˶�
					positionList.add(position);
					if (positionList.size() >= 2) {
						PolylineOptions polylineOptions = new PolylineOptions();
						// �����õ��ĵ�
						polylineOptions.points(positionList);
						baiduMap.clear();
						baiduMap.addOverlay(polylineOptions);
					}

				}
			}
		});
		locationClient = new LocationClient(getActivity());
		// ���ö�λ����
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		// ÿ2s��һ������ֵ
		// ʱ��ֵ���õ���1000ms��С�����ֵ��ÿ1000msҲֻ���һ������ֵ
		option.setScanSpan(1);
		// �ٴ����¶�λ
		// locationClient.requestLocation();
		locationClient.setLocOption(option);
		// �ðٶȿ���еĽӿ�ָ��ʵ����
		MyBdLocationListener listener = new MyBdLocationListener();
		locationClient.registerLocationListener(listener);
		locationClient.start();
		return view;
	}

	// Ӧ����ͣʱ����ͼ����λ����ֹͣ
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

	// Ӧ�ô��¼���ʱ����ͼ����λ���ܿ���
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
					// �жϵ�����ʱ���ǿ�ʼ���ǽ���
					if ("����".equals(text)) {
						linearLayout.setVisibility(View.GONE);
						baiduMap.clear();
						positionList.clear();
						handler.removeCallbacks(runnable);
						tvAction.setText("��ʼ");
						dialog.dismiss();

					} else {
					}
					// ��xml���view
					View view = View.inflate(getActivity(),
							R.layout.activity_show_counter, null);
					// ����dialog
					dialog = new AlertDialog.Builder(getActivity()).create();
					// Ϊdialog����view
					dialog.setView(view);
					// ��ʾdialog
					dialog.show();
					final TextView tv = (TextView) view
							.findViewById(R.id.tv_show_count);
					// ÿ��1s��count-1����ʾ����
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

								// ��ʾͳ�ƽ���
								showRecorder();
							}
						}

					}, 0);
					Handler handler2 = new Handler();
					handler2.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							tvAction.setText("����");
						}
					}, 3000);

				} catch (Exception e) {
				}

			}
		});
	}

	/**
	 * �ƶ����ĵ�λ��
	 * 
	 * @param currentPosition
	 */
	private void moveMapCenter(LatLng currentPosition) {
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(
				currentPosition, 17);// ��һ����������� �ڶ��������Ŵ󼶱�(4-17)
		baiduMap.animateMapStatus(update);
	}

	/**
	 * ��ʾͼƬ
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

	// ʵ����
	class MyBdLocationListener implements BDLocationListener {
		// ��λ�ɹ���ִ��
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			try {
				// ����
				double longitude = arg0.getLongitude();
				// γ��
				double latitude = arg0.getLatitude();
				Log.i("zjf", "����=" + longitude + ",γ��=" + latitude);
				// �жϳ�����û�еõ�����
				// �õ�����4.9E-324
				if (latitude == 4.9E-324) {
					// ģ��һ��λ��
					latitude = 32.541534545;
					longitude = 118.15152545;
				}

				// �ƶ���ͼ���ĵ�Ϊ��ǰλ��
				LatLng currentPosition = new LatLng(latitude, longitude);
				moveMapCenter(currentPosition);
				// �ڵ�ǰλ����ʾһ��ͼ
				showImage(currentPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void showRecorder() {
		linearLayout.setVisibility(View.VISIBLE);
		// �ҵ�ͳ�ƽ���Ŀؼ�
		// meter��ʱ��ͳ��
		final Chronometer meter = (Chronometer) view
				.findViewById(R.id.chronometer1);
		meter.start();
		meter.setBase(SystemClock.elapsedRealtime());
		final TextView tvDistance = (TextView) view
				.findViewById(R.id.tv_distance);
		final TextView tvSpeed = (TextView) view
				.findViewById(R.id.tv_recorder_speed);
		tvSpeed.setText("0.00");
		// ÿ��2s��������
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
						// ����ת���ɹ���
						distance = distance / 1000;
						String strDistance = String.valueOf(distance);
						// �ж��Ƿ���С��������Ǳ���С������λ
						if (strDistance.contains(".")) {
							int pointIndex = strDistance.indexOf(".");
							strDistance = strDistance.substring(0,
									pointIndex + 2);
						}
						tvDistance.setText(strDistance);
						// ���ʱ��01:48
						String time = meter.getText().toString();
						// �õ�����
						// [0]�ŵ��Ƿ֣�[1]�ŵ�����
						String[] array = time.split(":");
						int minute = Integer.parseInt(array[0]);
						// ����
						double second = Double.parseDouble(array[1]);
						// ����ת��Сʱ
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