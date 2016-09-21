//package com.lis99.mobile;
//
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.lis99.mobile.application.data.DataManager;
//import com.lis99.mobile.engine.base.BaseActivity;
//import com.lis99.mobile.entity.bean.IFTRUEBean;
//import com.lis99.mobile.entity.item.Info;
//import com.lis99.mobile.util.HttpNetRequest;
//import com.lis99.mobile.util.OptData;
//import com.lis99.mobile.util.QueryData;
//import com.lis99.mobile.util.constens;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ShopDetailTYPEActivity extends BaseActivity implements OnGetGeoCoderResultListener{
//	MapView mMapView;
//	static TextView tv_pj;
//	TextView name;
//	TextView addressView;
//	TextView tv_bc;
//	ImageView iv_home;
//	public MyLocationListenner myListener = new MyLocationListenner();
//
//	private BaiduMap mBaiduMap;
//	GeoCoder mSearch = null;
//
//
//	// 定位相关
//	LocationClient mLocClient;
//	MyLocationData locData = null;
//	BDLocation locInfo;
//	static Double Latitude; // 纬度
//	static Double Longtitude; // 经度
//
//
//	boolean isFirstLoc = true;
//
//	Marker mMarker;
//	BitmapDescriptor bd = BitmapDescriptorFactory
//			.fromResource(R.drawable.icon_gcoding);
//
//	private Info shop;
//	private int dis;
//
//	LatLng point;
//
//	@Override
//	public int getLayoutId() {
//		return R.layout.activity_shop_detail_type;
//	}
//
//	@Override
//	public void setListener() {
//		shop = (Info) getIntent().getSerializableExtra("data");
//		dis = getIntent().getIntExtra("dis", 0);
//
////		MyIcon mi = new MyIcon(this);
////		getWindow().addContentView(
////				mi,
////				new LayoutParams(LayoutParams.MATCH_PARENT,
////						LayoutParams.MATCH_PARENT));
//
//		mMapView = (MapView) findViewById(R.id.mMapView);
//
//		tv_pj = (TextView) findViewById(R.id.tv_pj);
//		tv_bc = (TextView) findViewById(R.id.tv_bc);
//		iv_home = (ImageView) findViewById(R.id.iv_home);
//		addressView = (TextView) findViewById(R.id.address);
//		name = (TextView) findViewById(R.id.name);
//		name.setText(shop.getTitle());
//		addressView.setText(shop.getAddress());
//		iv_home.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//		tv_bc.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new OptData<IFTRUEBean>(ShopDetailTYPEActivity.this).readData(
//						new QueryData<IFTRUEBean>() {
//							@Override
//							public String callData() {
//								HttpNetRequest httpNetRequest = new HttpNetRequest();
//								Map<String, Object> data = new HashMap<String, Object>();
//								data.put("user_id", DataManager.getInstance().getUser().getUser_id());
//								data.put("shop_id", shop.getOid());
//								data.put("e_type",2);
//								data.put("content", shop.getCategory());
//								data.put("area", shop.getCity());
//								data.put("name", shop.getTitle());
//								data.put("address", tv_pj.getText().toString());
//								data.put("tel", shop.getPhone());
//								data.put("position", point.latitude + "," + point.longitude);
//								String str=httpNetRequest.connect(constens.URLBC,data);
//								return str;
//							}
//
//							@Override
//							public void showData(IFTRUEBean t) {
//								if (t != null) {
//									if (t.getStatus() != null
//											&& t.getStatus().equals("OK")) {
//                                     Toast.makeText(ShopDetailTYPEActivity.this, "提交成功", Toast.LENGTH_LONG).show();
//                                     finish();
//									}else{
//										  Toast.makeText(ShopDetailTYPEActivity.this, "提交失败", Toast.LENGTH_LONG).show();
//									}
//								}
//
//							}
//						}, IFTRUEBean.class);
//			}
//		});
//
//		mBaiduMap = mMapView.getMap();
//		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(setZoom());
//		mBaiduMap.setMapStatus(msu);
//
//		mMapView.setLongClickable(true);
//
//		initOverlay();
//
//		mBaiduMap.setMyLocationEnabled(true);
//		// 定位初始化
//		mLocClient = new LocationClient(this);
//		mLocClient.registerLocationListener(myListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);// 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setScanSpan(1000);
//		option.setAddrType("all");
//		mLocClient.setLocOption(option);
//		mLocClient.start();
//
//		// 初始化搜索模块，注册事件监听
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(this);
//	}
//
//	public void initOverlay() {
//		String la = shop.getLatitude();
//		String lo = shop.getLongitude();
//		double latitude = Double.parseDouble(la);
//		double longitude = Double.parseDouble(lo) ;
//
//		LatLng llA = new LatLng(latitude, longitude);
//
//		OverlayOptions oo = new MarkerOptions().position(llA).icon(bd)
//				.zIndex(9).draggable(true);
//		mMarker = (Marker) (mBaiduMap.addOverlay(oo));
//
//		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
//		    public void onMarkerDrag(Marker marker) {
//		        //拖拽中
//		    }
//		    public void onMarkerDragEnd(Marker marker) {
//		    	point = marker.getPosition();
//		    	getPosition(point);
//		    }
//		    public void onMarkerDragStart(Marker marker) {
//		        //开始拖拽
//		    }
//		});
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
//	@Override
//	protected View getView() {
//		return null;
//	}
//
//	private void getPosition(LatLng ptCenter) {
//		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//		.location(ptCenter));
//		tv_pj.setText("获取位置中...");
//	}
//
//	@Override
//	protected void onPause() {
//		mMapView.onPause();
//		super.onPause();
//	}
//	@Override
//	protected void onResume() {
//		mMapView.onResume();
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		// 退出时销毁定位
//		if (mLocClient != null)
//			mLocClient.stop();
//		mBaiduMap.setMyLocationEnabled(false);
//		mMapView.onDestroy();
//		mMapView = null;
//		super.onDestroy();
//	}
//
//
//
//	/**
//	 * 定位SDK监听函数
//	 */
//	public class MyLocationListenner implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			// map view 销毁后不在处理新接收的位置
//						if (location == null || mMapView == null)
//							return;
//						locData = new MyLocationData.Builder()
//								.accuracy(location.getRadius())
//								// 此处设置开发者获取到的方向信息，顺时针0-360
//								.direction(location.getDirection()).latitude(location.getLatitude())
//								.longitude(location.getLongitude()).build();
//						mBaiduMap.setMyLocationData(locData);
//						if (isFirstLoc) {
//							isFirstLoc = false;
//							LatLng ll = new LatLng(location.getLatitude(),
//									location.getLongitude());
//							MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//							mBaiduMap.animateMapStatus(u);
//						}
//		}
//
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null) {
//				return;
//			}
//		}
//	}
//
//	public int setZoom() {
//		int zoom = 14;
//		if (dis > 1000) {
//			int dist = dis / 1000;
//
//			if (dist >= 1 && dist < 10) {
//				zoom = 13;
//			} else if (dist >= 10 && dist < 25) {
//				zoom = 12;
//			} else if (dist >= 25 && dist < 50) {
//				zoom = 11;
//			} else if (dist >= 50 && dist < 100) {
//				zoom = 10;
//			} else if (dist >= 100 && dist < 125) {
//				zoom = 9;
//			} else if (dist >= 125 && dist < 250) {
//				zoom = 8;
//			} else if (dist >= 250 && dist < 500) {
//				zoom = 7;
//			} else if (dist >= 500 && dist < 1000) {
//				zoom = 6;
//			} else if (dist >= 1000 && dist < 2000) {
//				zoom = 5;
//			} else if (dist >= 2000) {
//				zoom = 4;
//			}
//		} else if (dis <= 100) {
//			zoom = 18;
//		} else if (dis > 100 && dis <= 300) {
//			zoom = 17;
//		} else if (dis > 300 && dis <= 500) {
//			zoom = 16;
//		} else if (dis > 500 && dis <= 800) {
//			zoom = 15;
//		} else if (dis > 800 && dis <= 1000) {
//			zoom = 14;
//		}
//		return zoom;
//	}
//
//
//	@Override
//	public void onGetGeoCodeResult(GeoCodeResult result) {
//
//	}
//
//	@Override
//	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			return;
//		}
//		tv_pj.setText(result.getAddress());
//	}
//
//}
