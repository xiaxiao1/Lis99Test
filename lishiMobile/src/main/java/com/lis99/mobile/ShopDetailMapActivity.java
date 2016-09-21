package com.lis99.mobile;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.item.Info;

public class ShopDetailMapActivity extends BaseActivity {
	private LinearLayout iv_home;
	private TextView address, name;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView tv_pj, tv_bc;
	private String Address;
	public MyLocationListenner myListener = new MyLocationListenner();
	// 定位相关
	LocationClient mLocClient;
	MyLocationData locData = null;
	BDLocation locInfo;
	static Double Latitude; // 纬度
	static Double Longtitude; // 经度
	
	private int dis;
	private Info shop;
	public static final String BAIDU_URL = "http://api.map.baidu.com/direction";
	public static final String ACTION = "android.intent.action.VIEW";
	
	boolean isFirstLoc = true;
	
	private Marker mMarker;
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	public int getLayoutId() {
		return R.layout.activity_shop_detail_map;
	}

	@Override
	public void setListener() {
		context = this;
		shop = (Info) getIntent().getSerializableExtra("data");
		dis =(int)(Float.parseFloat(getIntent().getStringExtra("dis")));
		iv_home = (LinearLayout) findViewById(R.id.iv_home);
		address = (TextView) findViewById(R.id.address);
		name = (TextView) findViewById(R.id.name);
		tv_pj = (TextView) findViewById(R.id.tv_pj);
		
		mMapView = (MapView) findViewById(R.id.mMapView);
		
		
		tv_bc = (TextView) findViewById(R.id.tv_bc);

		address.setText(shop.getAddress().substring(0,
				getString(shop.getAddress()))
				+ "...");
		name.setText(shop.getTitle());
		tv_bc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialogbc();
			}
		});
		tv_pj.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlData = "?origin=latlng:" + locData.latitude + ","
						+ locData.longitude + "|name:当前地点&destination=latlng:"
						+ shop.getBaidu_latitude() + ","
						+ shop.getBaidu_longitude() + "|name:"
						+ shop.getAddress() + "&mode=driving&region=" + Address
						+ "&output=html" + "&src=" + shop.getTitle() + "|"
						+ getString(R.string.app_name);
				String url = BAIDU_URL + urlData;

				final Intent intent = new Intent();
				intent.setAction(ACTION);
				final Uri content_url = Uri.parse(url);
				intent.setData(content_url);
				startActivity(intent);
			}
		});
		iv_home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		
		
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(setZoom());
		mBaiduMap.setMapStatus(msu);
		
		initOverlay();
		
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	public void initOverlay() {
		String la = shop.getLatitude();
		String lo = shop.getLongitude();
		double latitude = Double.parseDouble(la);
		double longitude = Double.parseDouble(lo) ;
		
		LatLng llA = new LatLng(latitude, longitude);

		OverlayOptions oo = new MarkerOptions().position(llA).icon(bd)
				.zIndex(9).draggable(false);
		mMarker = (Marker) (mBaiduMap.addOverlay(oo));
	}

	@Override
	public void initData() {
		
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected View getView() {
		return null;
	}

	private int getString(String str) {
		int a = str.length();
		if (a > 14) {
			a = 14;
		}
		return a;

	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null)
			mLocClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
						if (location == null || mMapView == null)
							return;
						locData = new MyLocationData.Builder()
								.accuracy(location.getRadius())
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(location.getDirection()).latitude(location.getLatitude())
								.longitude(location.getLongitude()).build();
						mBaiduMap.setMyLocationData(locData);
						if (isFirstLoc) {
							isFirstLoc = false;
							LatLng ll = new LatLng(location.getLatitude(),
									location.getLongitude());
							MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
							mBaiduMap.animateMapStatus(u);
						}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}



	public int setZoom(){
		int zoom=14;
		if(dis>1000){
		int dist=dis/1000;
		
		if(dist>=1&&dist<10){
			zoom=13;
		}else if(dist>=10&&dist<25){
			zoom=12;
		}else if(dist>=25&&dist<50){
			zoom=11;
		}else if(dist>=50&&dist<100){
			zoom=10;
		}else if(dist>=100&&dist<125){
			zoom=9;
		}else if(dist>=125&&dist<250){
			zoom=8;
		}else if(dist>=250&&dist<500){
			zoom=7;
		}else if(dist>=500&&dist<1000){
			zoom=6;
		}else if(dist>=1000&&dist<2000){
			zoom=5;
		}else if(dist>=2000){
			zoom=4;
		}
		}else if(dis<=100){
			zoom=18;
		}else if(dis>100&&dis<=300){
			zoom=17;
		}else if(dis>300&&dis<=500){
			zoom=16;
		}else if(dis>500&&dis<=800){
			zoom=15;
		}else if(dis>800&&dis<=1000){
			zoom=14;
		}
		return zoom;
	}
	
	
	public  void dialogbc() {
		AlertDialog.Builder builder = new Builder(ShopDetailMapActivity.this);

		builder.setMessage("我们已经收到您的报错，是否去地图上标注正确的位置？");
		builder.setTitle("提示");

		builder.setPositiveButton("标注", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(ShopDetailMapActivity.this,
//						ShopDetailTYPEActivity.class);
//				intent.putExtra("data", shop);
//				intent.putExtra("dis", dis);
//				startActivity(intent);
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.create().show();
	}
	
}
