package com.lis99.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lis99.mobile.dilog.ShopSearchDialog;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.entity.bean.DPListBean;
import com.lis99.mobile.entity.bean.SearchBean;
import com.lis99.mobile.entity.item.Citys;
import com.lis99.mobile.entity.item.DPListItem;
import com.lis99.mobile.entity.item.Shop;
import com.lis99.mobile.entry.adapter.DPAdapter;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;

import java.util.ArrayList;
import java.util.HashMap;

public class LocActivity extends Activity {

	// 定位相关
	LocationClient mLocClient;
	MyLocationData locData = null;
	BDLocation locInfo;
	static Double Latitude; // 纬度
	static Double Longtitude; // 经度
	String Address;
	public MyLocationListenner myListener = new MyLocationListenner();
	
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;
	private DPListItem dpListItem;
	private static View view;
	private static Context context;
	private ArrayList<Shop> shops;
	private String dis="";
	private TextView tv_dp;
	private TextView tv_px;
	private TextView tv_dq,tv_city;
	private PopupWindow popupWindow;
	private LinearLayout layoutpop;
	private LinearLayout linearLayoutdiqu;
	private LinearLayout linearLayoutjuli;
	private LinearLayout linearLayoutdianpu;
	private LinearLayout ll_diqu;
	private ImageView imageViewdianpu;
	private ImageView iv_dq;
	private ImageView iv_px;
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;
	private ImageView up, down,iv_list;
	private ImageView iv_serch;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	BitmapDescriptor bd = BitmapDescriptorFactory
	.fromResource(R.drawable.icon_gcoding);
	
	// UI相关
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	private Bitmap[] bitMaps = null;
	private String Latitude1;
	private String Longtitude1;
	private String city;
	private String cityid;
	private String shoptype;
	private String sorttype;
	
	private int limit = 10;
	private LinearLayout shuaxin;
	private ArrayList<Citys> citys;
	private HttpNetRequest httpNetRequest;
	private int offset = 0;
	int count = 0;
	private ArrayList<String> arrayList;
	private ListView list;
	private ImageView iv_home;
	private DPAdapter<String> dpAdapter;
	private TextView number;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loc);
		context = this;
		Intent intent = getIntent();
		Latitude1 = (String) intent.getStringExtra("X");
		Longtitude1 = (String) intent.getStringExtra("Y");
		city = (String) intent.getStringExtra("C");
		cityid = getIntent().getStringExtra("id");
		dis = getIntent().getStringExtra("dis");
		layoutpop = (LinearLayout) LayoutInflater.from(LocActivity.this)
				.inflate(R.layout.ls_buy_popupwindow, null);
		iv_serch = (ImageView) findViewById(R.id.iv_serch);
		ll_diqu = (LinearLayout) findViewById(R.id.ll_diqu);
		tv_dp = (TextView) findViewById(R.id.tv_dp);
		tv_px = (TextView) findViewById(R.id.tv_px);
		tv_dq = (TextView) findViewById(R.id.tv_dq);
		tv_city=(TextView)findViewById(R.id.tv_city);
		linearLayout = (LinearLayout) findViewById(R.id.ll_title);
		relativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
		imageViewdianpu = (ImageView) findViewById(R.id.iv_dianpu);
		iv_dq = (ImageView) findViewById(R.id.iv_dq);
		iv_px = (ImageView) findViewById(R.id.iv_px);
		linearLayoutdianpu = (LinearLayout) findViewById(R.id.ll_dianpu);
		linearLayoutjuli = (LinearLayout) findViewById(R.id.ll_juli);
		linearLayoutdiqu = (LinearLayout) findViewById(R.id.ll_qbdq);
		list = (ListView) layoutpop.findViewById(R.id.lv_pop);
		requestLocButton = (Button) findViewById(R.id.button1);
		number = (TextView) findViewById(R.id.number);
		shuaxin = (LinearLayout) findViewById(R.id.shuaxin);
		iv_home = (ImageView) findViewById(R.id.iv_home);
		up = (ImageView) findViewById(R.id.up);
		down = (ImageView) findViewById(R.id.down);
		iv_list=(ImageView)findViewById(R.id.iv_list);
		number.setText("第" + 1 + "-"
				+ 10 + "家");
		// iv_serch.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// onSearchRequested();
		//
		// return false;
		// }
		// });
		iv_serch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				relativeLayout.setVisibility(View.GONE);
				ShopSearchDialog dialog = new ShopSearchDialog(
						LocActivity.this, shops,
						
						new ShopSearchDialog.OnSearchClickLinsener() {
						
							public void toSearch(final String text) {
								
								new OptData<SearchBean>(LocActivity.this)
										.readData(new QueryData<SearchBean>() {
											
											@Override
											public String callData() {
											HttpNetRequest httpNetRequest=new HttpNetRequest();
											HashMap<String, Object> hashMap=new HashMap<String, Object>();
											hashMap.put("word", text);
											hashMap.put("latitude", Latitude1);
											hashMap.put("longitude", Longtitude1);
											hashMap.put("offset", offset);
											hashMap.put("limit", 20);
											//hashMap.put("cityId", ct);
												return httpNetRequest.connect(constens.URLSC, hashMap);
											}

											@Override
											public void showData(SearchBean t) {
												
											}
												
										}, SearchBean.class);

							}
						});
				dialog.show();
					}
		});
		up.setOnClickListener(clickListener);
		down.setOnClickListener(clickListener);
		iv_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		ll_diqu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LocActivity.this,
						CityListActivity.class);
				startActivity(intent);
				finish();
			}
		});
		iv_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (count) {
				case 3:
					if (arg2 == 0) {
						shoptype = null;
					} else if (arg2 == 1) {
						shoptype = "0";
					} else if (arg2 == 2) {
						shoptype = "1";
					}
					tv_dp.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow));
					offset = 0;
					viewdp();
					return;
				case 2:
					if (arg2 == 0) {
						sorttype = null;
					} else if (arg2 == 1) {
						sorttype = "distance";
					} else if (arg2 == 2) {
						sorttype = "star";
					} else if (arg2 == 3) {
						sorttype = "click";
					}
					tv_px.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					offset = 0;
					viewdp();
					return;
				case 1:
					cityid = citys.get(arg2).getId();
					tv_dq.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					offset = 0;
					viewdp();
					return;
				}
			}
		});
		linearLayoutdianpu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow));
				} else {
					int y = linearLayout.getHeight()
							+ relativeLayout.getHeight();
					int x = getWindowManager().getDefaultDisplay().getWidth();
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow2));

					showPopupWindow(x, y, 3);

				}
				count = 3;
			}
		});
		linearLayoutjuli.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow));
				} else {
					int y = linearLayout.getHeight()
							+ relativeLayout.getHeight();
					int x = getWindowManager().getDefaultDisplay().getWidth();
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow2));

					showPopupWindow(x, y, 2);

				}
				count = 2;
			}

		});
		linearLayoutdiqu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow));
				} else {
					int y = linearLayout.getHeight()
							+ relativeLayout.getHeight();
					int x = getWindowManager().getDefaultDisplay().getWidth();
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow2));

					showPopupWindow(x, y, 1);

				}
				count = 1;
			}
		});
		OnClickListener btnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 手动定位请求
				requestLocClick();
			}
		};
		shuaxin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewdp();
				requestsx();
				
			}
		});
		requestLocButton.setOnClickListener(btnClickListener);

		// RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
		// radioButtonListener = new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// // TODO Auto-generated method stub
		// //
		// if (checkedId == R.id.defaulticon){
		// //传入null则，恢复默认图标
		// modifyLocationOverlayIcon(null);
		// }
		// if (checkedId == R.id.customicon){
		// //修改为自定义marker
		// modifyLocationOverlayIcon(getResources().getDrawable(R.drawable.icon_geo));
		// }
		// }
		// };
		// group.setOnCheckedChangeListener(radioButtonListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(setZoom());
		mBaiduMap.setMapStatus(msu);
		
		mBaiduMap.setMyLocationEnabled(true);

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		addShops();
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				Bundle bundle = marker.getExtraInfo();
				if(bundle == null)
					return false;
				final Shop shop = (Shop) bundle.getSerializable("shop");
				if(shop == null)
					return false;
				OnInfoWindowClickListener listener = null;
				
				View popView = getDialogView(shop);
				
				listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
						Intent intent = new Intent(LocActivity.this,
								ShopDetailActivity.class);
						String oid = shop.getOid();
						intent.putExtra(constens.OID, oid);
						startActivity(intent);
						mBaiduMap.hideInfoWindow();
					}
				};
				LatLng ll = marker.getPosition();
				InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(popView), ll, -47, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.up:
//				if (offset > 0) {
//					offset -= limit;
//					viewdp();
//				}
//				break;
//			case R.id.down:
//
//				offset += limit;
//				viewdp();
//
//				break;
			}

		}
	};

	/**
	 * 手动触发一次定位请求
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(LocActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
	}
	
	public void requestsx() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(LocActivity.this, "正在刷新……", Toast.LENGTH_SHORT).show();
	}
	
	public void showPopupWindow(int x, int y, int i) {

		popupWindow = new PopupWindow(LocActivity.this);
		popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getWindowManager().getDefaultDisplay()
				.getHeight() - y);
		ColorDrawable dw = new ColorDrawable(getResources().getColor(
				R.color.color_touming));
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(layoutpop);
		popupWindow.showAtLocation(findViewById(R.id.loc), Gravity.LEFT
				| Gravity.TOP, x, y);
		dpAdapter = new DPAdapter<String>(LocActivity.this, arrayList);
		list.setAdapter(dpAdapter);
		switch (i) {
		case 3:
			setList();
			return;
		case 2:
			setListpx();
			return;

		case 1:
			setListdq();
			return;
		}
	}

	public void setList() {
		if (arrayList != null) {
			arrayList = null;
		}
		if (dpAdapter != null) {
			dpAdapter.clearData();
		}
		arrayList = new ArrayList<String>();
		arrayList.add(getString(R.string.dianpu));
		arrayList.add(getString(R.string.huwai));
		arrayList.add(getString(R.string.shangchang));
		arrayList.add(getString(R.string.xujudian));
		dpAdapter.setData(arrayList);
		dpAdapter.notifyDataSetChanged();

	}

	public void setListpx() {
		if (arrayList != null) {
			arrayList = null;
		}
		if (dpAdapter != null) {
			dpAdapter.clearData();
		}
		arrayList = new ArrayList<String>();
		arrayList.add(getString(R.string.moren));
		arrayList.add(getString(R.string.juli));
		arrayList.add(getString(R.string.pingjia));
		arrayList.add(getString(R.string.renqi));
		arrayList.add(getString(R.string.discount));
		dpAdapter.setData(arrayList);
		dpAdapter.notifyDataSetChanged();

	}

	public void setListdq() {
		if (arrayList != null) {
			arrayList = null;
		}
		if (dpAdapter != null) {
			dpAdapter.clearData();
		}
		arrayList = new ArrayList<String>();
		if (citys != null) {
			for (int i = 0; i < citys.size(); i++) {
				arrayList.add(citys.get(i).getName());
			}
		}
		dpAdapter.setData(arrayList);
		dpAdapter.notifyDataSetChanged();

	}


	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(location.getDirection()).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc || isRequest) {
				isRequest = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void addShops() {
		dpListItem = (DPListItem) getIntent().getSerializableExtra("data");
		shops = dpListItem.getShop();
		
		for (int a = 0; a < shops.size(); a++) {
			Shop shop = shops.get(a);
			String la = shop.getLatitude();
			String lo = shop.getLongitude();
			double latitude = Double.parseDouble(la);
			double longitude = Double.parseDouble(lo) ;
			
			LatLng llA = new LatLng(latitude, longitude);

			OverlayOptions oo = new MarkerOptions().position(llA).icon(bd)
					.zIndex(9).draggable(false);
			Marker marker = (Marker) mBaiduMap.addOverlay(oo);
			Bundle bundle = new Bundle();
			bundle.putSerializable("shop", shop);
			marker.setExtraInfo(bundle);
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	public void viewdp() {

		new OptData<DPListBean>(LocActivity.this).readData(
				new QueryData<DPListBean>() {
					@Override
					public String callData() {
						String data = "?" + constens.LATITUDE + "=" + Latitude1
								+ "&" + constens.LONGITUDE + "=" + Longtitude1
								+ "&" + constens.OFFSET + "=" + offset + "&"
								+ constens.LIMIT + "=" + limit + "&"
								+ constens.SHOPTYPE + "=" + shoptype + "&"
								+ constens.SORTTYPE + "=" + sorttype + "&"
								+ constens.NEEDCITYS + "=" + 1 + "&"
								+ constens.CITYID + "=" + cityid;
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URL,
								data);
						return str;

					}

					@Override
					public void showData(DPListBean t) {
						if (t != null) {
							DPListItem dpListItem = t.getData();
							if (dpListItem != null) {

								citys = dpListItem.getCitys();
								shops = dpListItem.getShop();
								if (mBaiduMap != null) {
									mBaiduMap.clear();
								}
								
								if (shops != null && shops.size() > 0) {
									for(Shop shop : shops){
										String la = shop.getLatitude();
										String lo = shop.getLongitude();
										double latitude = Double.parseDouble(la);
										double longitude = Double.parseDouble(lo) ;
										
										LatLng llA = new LatLng(latitude, longitude);

										OverlayOptions oo = new MarkerOptions().position(llA).icon(bd)
												.zIndex(9).draggable(false);
										Marker marker = (Marker) mBaiduMap.addOverlay(oo);
										Bundle bundle = new Bundle();
										bundle.putSerializable("shop", shop);
										marker.setExtraInfo(bundle);
									}
								} else {
									if (offset > 0) {
										offset -= 10;
									}
								}
							}
						} else {
							ShowUtil.showToast(LocActivity.this,
									getString(R.string.lj_false));
						}
					}
				}, DPListBean.class);
	}

	private static View getDialogView(Shop shop) {
		TextView title = null;
		TextView content = null;
		ImageView iv_star1 = null;
		ImageView iv_star2 = null;
		ImageView iv_star3 = null;
		ImageView iv_star4 = null;
		ImageView iv_star5 = null;
		view = LinearLayout.inflate(context, R.layout.pop_dialog, null);
		title = (TextView) view.findViewById(R.id.title);
		content = (TextView) view.findViewById(R.id.content);
		iv_star1 = (ImageView) view.findViewById(R.id.iv_star1);
		iv_star2 = (ImageView) view.findViewById(R.id.iv_star2);
		iv_star3 = (ImageView) view.findViewById(R.id.iv_star3);
		iv_star4 = (ImageView) view.findViewById(R.id.iv_star4);
		iv_star5 = (ImageView) view.findViewById(R.id.iv_star5);
		float f = Float.parseFloat(shop.getStar());
		if (f >= 3) {
			iv_star1.setImageResource(R.drawable.hwd_large_star_s);
			iv_star2.setImageResource(R.drawable.hwd_large_star_s);
			iv_star3.setImageResource(R.drawable.hwd_large_star_s);
			if (f == 3.5) {
				iv_star4.setImageResource(R.drawable.hwd_large_star_b);
			}
			if (f == 4) {
				iv_star4.setImageResource(R.drawable.hwd_large_star_s);
			}
			if (f == 4.5) {
				iv_star4.setImageResource(R.drawable.hwd_large_star_s);
				iv_star5.setImageResource(R.drawable.hwd_large_star_b);
			}
			if (f == 5) {
				iv_star4.setImageResource(R.drawable.hwd_large_star_s);
				iv_star5.setImageResource(R.drawable.hwd_large_star_s);
			}

		} else if (f >= 2) {
			iv_star1.setImageResource(R.drawable.hwd_large_star_s);
			iv_star2.setImageResource(R.drawable.hwd_large_star_s);
			if (f == 2.5) {
				iv_star3.setImageResource(R.drawable.hwd_large_star_b);
			} else if (f >= 1) {
				iv_star1.setImageResource(R.drawable.hwd_large_star_s);
				if (f == 1.5) {
					iv_star2.setImageResource(R.drawable.hwd_large_star_b);
				}
			} else if (f == 0.5) {
				iv_star1.setImageResource(R.drawable.hwd_large_star_b);
			}
		}
		title.setText(shop.getTitle());
		content.setText(shop.getAddress());
		return view;

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
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public int setZoom(){
		int zoom=14;
		int distens=(int)(Float.parseFloat(dis));
		
		if(distens>1000){
			int dist=distens/1000;
			
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
			}else if(distens<=100){
				zoom=18;
			}else if(distens>100&&distens<=300){
				zoom=17;
			}else if(distens>300&&distens<=500){
				zoom=16;
			}else if(distens>500&&distens<=800){
				zoom=15;
			}else if(distens>800&&distens<=1000){
				zoom=14;
			}
		return zoom;
	}
}

