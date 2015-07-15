package com.lis99.mobile.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.lis99.mobile.entity.item.LocData;
import com.lis99.mobile.entry.application.DemoApplication;

public class LocService extends Service {
	// 定位相关
	LocationClient mLocClient;
	MyLocationData locData = null;
	BDLocation locInfo;
	static Double Latitude; // 纬度
	static Double Longtitude; // 经度
	Context context;
	String Address;
	private DemoApplication application;
	public MyLocationListenner myListener = new MyLocationListenner();
	public LocData loc;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// 定位初始化
		this.context = this;
		application = DemoApplication.getInstance();
		
		//locData = new MyLocationData.Builder().build();
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.requestLocation();
	}

	/**
	 * 定位SDK监听函数
	 */
	public void putIntent(BDLocation location, String str) {
		Intent intent1 = new Intent(str);
		intent1.putExtra("jingdu", location.getLongitude() + "");
		intent1.putExtra("weidu", location.getLatitude() + "");
		sendBroadcast(intent1);
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			application.setLocation(location);
			if (loc == null) {
				loc = new LocData();
			}
			
			locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				.direction(location.getDirection())
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.build();
			
			loc.setLocation(location);
			
			Address = location.getCity();

			// 更新定位数据
			Intent intent = new Intent("com.lis99.mobile.loc");
			intent.putExtra("X", location.getLatitude() + "");
			intent.putExtra("Y", location.getLongitude() + "");
			intent.putExtra("C", location.getCity());
			sendBroadcast(intent);

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

}
