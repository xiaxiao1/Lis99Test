package com.lis99.mobile.club;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.lis99.mobile.R;
import com.lis99.mobile.util.LocationUtil;

public class LSClubTopicInfoLocation extends LSBaseActivity
{

	BaiduMap mBaiduMap = null;
	MapView mMapView = null;

	private Double Latitude; // 纬度
	private Double Longtitude; // 经度

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Latitude = getIntent().getDoubleExtra("latitude", 0);
		Longtitude = getIntent().getDoubleExtra("longtitude", 0);

		setContentView(R.layout.club_topic_info_location);
		
		super.initViews();

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		
		LatLng latlng = new LatLng(Latitude, Longtitude);
		latlng = LocationUtil.getinstance().gaode2baidu(latlng);

		mBaiduMap.addOverlay(new MarkerOptions().position(latlng)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
		
	}

	
	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

}
