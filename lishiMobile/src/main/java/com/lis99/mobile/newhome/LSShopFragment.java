package com.lis99.mobile.newhome;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.CityListActivity;
import com.lis99.mobile.LocActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.ShopDetailActivity;
import com.lis99.mobile.dilog.ShopSearchDialog;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.bean.DPListBean;
import com.lis99.mobile.entity.item.Citys;
import com.lis99.mobile.entity.item.DPListItem;
import com.lis99.mobile.entity.item.Shop;
import com.lis99.mobile.entry.adapter.DPAdapter;
import com.lis99.mobile.entry.adapter.LsBuyAdapter;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class LSShopFragment extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView listView;
	private ListView list;
	private TextView tv_dp;
	private TextView tv_px;
	private TextView tv_dq;
	private LsBuyAdapter<Shop> lsBuyAdapter;
	private PopupWindow popupWindow;
	private LinearLayout layoutpop;
	private LinearLayout linearLayoutdiqu;
	private LinearLayout linearLayoutjuli;
	private LinearLayout linearLayoutdianpu;
	private LinearLayout ll_diqu;
	private ImageView imageViewdianpu;
	private ImageView iv_dq;
	private ImageView iv_px;
	private ArrayList<Shop> shops;
	private ArrayList<String> arrayList;
	private DPAdapter<String> dpAdapter;
	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;
	private ImageView iv_list;
	private MyReciever myReciever;
	private String Latitude1; // 纬度
	private String Longtitude1; // 经度
	private String city;
	private String cityid = "";
	private String ct = "";
	private TextView tv_city;
	//private TextView dian;
	private View iv_home;
	private HttpNetRequest httpNetRequest;
	private ArrayList<Citys> citys;
	private String shoptype = "";
	private String sorttype = "";
	private LinearLayout ls_buy;
	Dialog dialogView = null;
	private DPListBean dpListBean;
	private int offset = 0;
	private SharedPreferences preferences;
	private String gpsCity = null;
	private String gpsCityid;
	int count = 0;

	int discountShopCount = 0;
	boolean discountOnly = false;
	boolean discountEnd = false;
	boolean allEnd = false;
	int discountTail = 0;

	final static int LIMIT = 20;

	private ImageView iv_serch;
	private PullToRefreshView refreshView;
	IntentFilter intentFilter;
	
	boolean shopTypeChange;
	
	private final static int CITY_CHANGE = 1001;
	private final static int ASK_CHANGE = 1002;
	
	public void setShopType(String shopType){
		if(!shopType.equals(this.shoptype)){
			this.shoptype = shopType;
			shopTypeChange = true;
		}
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		intentFilter = new IntentFilter();
		intentFilter.addAction("com.lis99.mobile.loc");
		myReciever = new MyReciever();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		offset = 0;
		refreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		layoutpop = (LinearLayout) LayoutInflater.from(getActivity())
				.inflate(R.layout.ls_buy_popupwindow, null);
		
		list = (ListView) layoutpop.findViewById(R.id.lv_pop);

		
		initOptions();
		lsBuyAdapter = new LsBuyAdapter<Shop>(getActivity(), null);
		listView.setAdapter(lsBuyAdapter);
		listenLL();
		preferences = getActivity().getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE);
		
//		getActivity().startService(new Intent("com.lis99.mobile.service.LocService"));
		getActivity().startService(new Intent(getActivity(), com.lis99.mobile.service.LocService.class));

		registerReceiver(myReciever, intentFilter);
		
		
		if(shopTypeChange){
			shopTypeChange = false;
			if("0".equals(shoptype)){
				tv_dp.setText(getString(R.string.huwai));
			}else if("1".equals(shoptype)){
				tv_dp.setText(getString(R.string.shangchang));
			}else if("2".equals(shoptype)){
				tv_dp.setText(getString(R.string.xujudian));
			}
		}
		
		
		cityid = preferences.getString("cityid", "");
		city = preferences.getString("city", "");
		
		
		if (!emptyString(city)) {
			tv_city.setText(city);
		}
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden){
			if(myReciever != null){
				//getActivity().stopService(new Intent("com.lis99.mobile.service.LocService"));
				//unregisterReceiver(myReciever);
				//myReciever = null;
			}
		}else{
//			getActivity().startService(new Intent("com.lis99.mobile.service.LocService"));
//			registerReceiver(myReciever, intentFilter);
			
			if(shopTypeChange){
				shopTypeChange = false;
				if("0".equals(shoptype)){
					tv_dp.setText(getString(R.string.huwai));
				}else if("1".equals(shoptype)){
					tv_dp.setText(getString(R.string.shangchang));
				}else if("2".equals(shoptype)){
					tv_dp.setText(getString(R.string.xujudian));
				}
				resetState();
			}
			
		}
		
	}
	
	
	@Override
	protected void initViews(ViewGroup container) {
		
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.activity_ls_buy, container, false);
		
		ls_buy = (LinearLayout) findViewById(R.id.ls_buy);
		iv_serch = (ImageView) findViewById(R.id.iv_serch);
		ll_diqu = (LinearLayout) findViewById(R.id.ll_diqu);
		tv_dp = (TextView) findViewById(R.id.tv_dp);
		tv_px = (TextView) findViewById(R.id.tv_px);
		tv_dq = (TextView) findViewById(R.id.tv_dq);
		iv_list = (ImageView) findViewById(R.id.iv_list);
		linearLayout = (LinearLayout) findViewById(R.id.ll_title);
		relativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
		imageViewdianpu = (ImageView) findViewById(R.id.iv_dianpu);
		iv_dq = (ImageView) findViewById(R.id.iv_dq);
		iv_px = (ImageView) findViewById(R.id.iv_px);
		listView = (ListView) findViewById(R.id.listView1);
		linearLayoutdianpu = (LinearLayout) findViewById(R.id.ll_dianpu);
		linearLayoutjuli = (LinearLayout) findViewById(R.id.ll_juli);
		tv_city = (TextView) findViewById(R.id.tv_city);
		linearLayoutdiqu = (LinearLayout) findViewById(R.id.ll_qbdq);
		
		iv_home = findViewById(R.id.iv_home);
		
		
		

	}

	public void listenLL() {

		iv_serch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				relativeLayout.setVisibility(View.GONE);
				ShopSearchDialog dialog = new ShopSearchDialog(
						getActivity(), shops, Latitude1, Longtitude1,
						offset, cityid);
				dialog.show();

			}
		});
		ll_diqu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						CityListActivity.class);
				intent.putExtra("city", gpsCity);
				startActivity(intent);
				//finish();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (count) {
				case 3:
					if (arg2 == 0) {
						shoptype = "";
					} else if (arg2 == 1) {
						shoptype = "0";
					} else if (arg2 == 2) {
						shoptype = "1";
					} else if (arg2 == 2) {
						shoptype = "2";
					}
					tv_dp.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					imageViewdianpu.setImageDrawable(getResources()
							.getDrawable(R.drawable.hwd_arrow));
					resetState();
					return;
				case 2:
					discountOnly = false;
					if (arg2 == 0) {
						sorttype = "";
					} else if (arg2 == 1) {
						sorttype = "distance";
					} else if (arg2 == 2) {
						sorttype = "star";
					} else if (arg2 == 3) {
						sorttype = "click";
					} else if (arg2 == 4) {
						sorttype = "discount";
						discountOnly = true;
					}
					tv_px.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					iv_px.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					resetState();
					return;
				case 1:
					cityid = citys.get(arg2).getId();
					tv_dq.setText(arrayList.get(arg2));
					popupWindow.dismiss();
					popupWindow = null;
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow));
					resetState();
					return;
				}
			}
		});
		iv_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getLSActivity().toggleMenu();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Shop shop = (Shop) lsBuyAdapter.getItem(arg2);
				Intent intent = new Intent(getActivity(),
						ShopDetailActivity.class);
				String oid = shop.getOid();
				intent.putExtra(constens.OID, oid);
				intent.putExtra("fav", "ls");
				intent.putExtra("dis", shop.getDistance());
				startActivity(intent);
			}
		});
		iv_list.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dpListBean != null) {
					Intent intent = new Intent(getActivity(),
							LocActivity.class);
					double dis = 0;
					for (int i = 0; i < shops.size(); i++) {
						dis += Double.parseDouble((shops.get(i).getDistance()));
					}
					dis = dis / shops.size();
					String Dis = dis + "";
					DPListItem dpListItem = dpListBean.getData();

					intent.putExtra("data", dpListItem);
					intent.putExtra("X", Latitude1);
					intent.putExtra("Y", Longtitude1);
					intent.putExtra("C", city);
					intent.putExtra("id", cityid);
					intent.putExtra("dis", Dis);
					startActivity(intent);
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
					int y = getActivity().getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight() - LSTab.sTabHeight; // linearLayout.getHeight()+
													// relativeLayout.getHeight();

					int x = getActivity().getWindowManager().getDefaultDisplay().getWidth();
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
					int y = getActivity().getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight() - LSTab.sTabHeight;
					int x = getActivity().getWindowManager().getDefaultDisplay().getWidth();
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
					int y = getActivity().getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight() - LSTab.sTabHeight;
					int x = getActivity().getWindowManager().getDefaultDisplay().getWidth();
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow2));

					showPopupWindow(x, y, 1);

				}
				count = 1;
			}
		});

	}

	public void showPopupWindow(int x, int y, int i) {
		popupWindow = new PopupWindow(getActivity());
		popupWindow.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getActivity().getWindowManager().getDefaultDisplay()
				.getHeight() - y);
		ColorDrawable dw = new ColorDrawable(getResources().getColor(
				R.color.color_touming));
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(layoutpop);
		popupWindow.showAtLocation(findViewById(R.id.ls_buy), Gravity.LEFT
				| Gravity.TOP, x, y);

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow = null;
				iv_dq.setImageDrawable(getResources().getDrawable(
						R.drawable.hwd_arrow));
				iv_px.setImageDrawable(getResources().getDrawable(
						R.drawable.hwd_arrow));
				imageViewdianpu.setImageDrawable(getResources().getDrawable(
						R.drawable.hwd_arrow));
			}

		});

		dpAdapter = new DPAdapter<String>(getActivity(), arrayList);
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
		dpAdapter.selectTitle = tv_dp.getText().toString();
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
		dpAdapter.selectTitle = tv_px.getText().toString();
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
		dpAdapter.selectTitle = tv_dq.getText().toString();
		dpAdapter.setData(arrayList);
		dpAdapter.notifyDataSetChanged();

	}

	public void viewdp() {
		showWaiting(getActivity());
		if (!discountEnd) {
			new OptData<DPListBean>(getActivity()).readData(
					new QueryData<DPListBean>() {

						@Override
						public String callData() {
							String data = "?" + constens.LATITUDE + "="
									+ Latitude1 + "&" + constens.LONGITUDE
									+ "=" + Longtitude1 + "&" + constens.OFFSET
									+ "=" + offset + "&" + constens.LIMIT + "="
									+ LIMIT + "&" + constens.SHOPTYPE + "="
									+ shoptype + "&" + constens.SORTTYPE + "="
									+ sorttype + "&" + constens.CITYID + "="
									+ cityid;
							httpNetRequest = new HttpNetRequest();
							String str = httpNetRequest.connect(
									constens.promotionShopListURL, data);
							return str;

						}

						@Override
						public void showData(DPListBean t) {
							hideDialog();
							if (t != null && t.getData() != null) {
								DPListItem dpListItem = t.getData();
								shops = dpListItem.getShop();
								if (offset == 0) {
									lsBuyAdapter.clearData();
								}
								if (shops != null && shops.size() > 0) {
									offset += shops.size();
									dpListBean = t;
									for (Shop s : shops) {
										s.setDiscount(true);
									}
									lsBuyAdapter.setData(shops);
									if (shops.size() < 20) {
										offset = 0;
										discountEnd = true;
										discountTail = shops.size();
										viewdp();
									}
								} else {
									offset = 0;
									discountEnd = true;
									discountTail = 0;
									viewdp();
								}
							} else {
								if (offset == 0) {
									lsBuyAdapter.clearData();
								}
								offset = 0;
								discountEnd = true;
								discountTail = 0;
								viewdp();
							}
						}
					}, DPListBean.class);
		} else {
			if (discountOnly) {
				hideDialog();
				allEnd = true;
				dpListBean = null;
				return;
			}
			new OptData<DPListBean>(getActivity()).readData(
					new QueryData<DPListBean>() {
						@Override
						public String callData() {
							String data = "?" + constens.LATITUDE + "="
									+ Latitude1 + "&" + constens.LONGITUDE
									+ "=" + Longtitude1 + "&" + constens.OFFSET
									+ "=" + offset + "&" + constens.LIMIT + "="
									+ (LIMIT - discountTail) + "&"
									+ constens.SHOPTYPE + "=" + shoptype + "&"
									+ constens.SORTTYPE + "=" + sorttype + "&"
									+ constens.CITYID + "=" + cityid;
							discountTail = 0;
							httpNetRequest = new HttpNetRequest();
							String str = httpNetRequest.connect(constens.URL,
									data);
							return str;

						}

						@Override
						public void showData(DPListBean t) {
							hideDialog();
							if (t != null && t.getData() != null) {
								DPListItem dpListItem = t.getData();
								shops = dpListItem.getShop();
								if (shops != null && shops.size() > 0) {
									offset += shops.size();
									dpListBean = t;
									lsBuyAdapter.setData(shops);
								} else {
									allEnd = true;
									dpListBean = null;
								}
							} else {
								allEnd = true;
								dpListBean = null;
								// ShowUtil.showToast(LsBuyActivity.this,
								// getString(R.string.lj_false));
							}
						}
					}, DPListBean.class);
		}
	}

	public void viewdp1() {
		showWaiting(getActivity());
		new OptData<DPListBean>(getActivity()).readData(
				new QueryData<DPListBean>() {

					@Override
					public String callData() {
						String data = "?" + constens.LATITUDE + "=" + Latitude1
								+ "&" + constens.LONGITUDE + "=" + Longtitude1
								+ "&" + constens.OFFSET + "=" + 0 + "&"
								+ constens.LIMIT + "=" + 1 + "&"
								+ constens.SHOPTYPE + "=" + shoptype + "&"
								+ constens.SORTTYPE + "=" + sorttype + "&"
								+ constens.NEEDCITYS + "=" + 1 + "&"
								+ constens.CITYID + "=" + cityid;
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URL, data);
						return str;
					}

					@Override
					public void showData(DPListBean t) {
						hideDialog();
						if (t != null) {
							DPListItem dpListItem = t.getData();
							if (dpListItem != null) {
								citys = dpListItem.getCitys();
								ct = citys.get(0).getReid();
								if ("".equals(cityid)) {
									cityid = ct;
									Editor editor = preferences.edit();
									editor.putString("cityid", cityid);
									editor.commit();
								}
							}
						}
						resetState();
					}
				}, DPListBean.class);
	}
	
	private void getLocationCity() {
		//postMessage(ActivityPattern1.POPUP_PROGRESS,getString(R.string.sending));
		String url = constens.getLocationByBaidu + "?latitude="+ Latitude1 +"&longitude=" + Longtitude1;
		Task task = new Task(null, url, null, constens.getLocationByBaidu, this);
		publishTask(task, IEvent.IO);
	}
	
	boolean emptyString(String str) {
		return str == null || "".equals(str);
	}
	
	@Override
	public void handleHttpResponseData(JsonNode data, String param) {
		gpsCityid = data.get("id").asText("");
		gpsCity = data.get("name").asText("");
		if (!emptyString(city)) {
			if (!emptyString(gpsCity) && !city.equals(gpsCity)) {
				postMessage(ASK_CHANGE);
			}
		} else {
			if (emptyString(gpsCity)) {
				cityid = "1";
				city = "北京";
			} else {
				cityid = gpsCityid;
				city = gpsCity;
			}
			postMessage(CITY_CHANGE);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == CITY_CHANGE) {
			tv_dq.setText("全部地区");
			tv_city.setText(city);
			viewdp1();
			return true;
		} else if (msg.what == ASK_CHANGE) {
			dialog();
			return true;
		}
		return super.handleMessage(msg);
	}

	class MyReciever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String Latitude = (String) intent.getStringExtra("X");
			String Longtitude = (String) intent.getStringExtra("Y");
			
			if (Latitude != null && !"".equals(Latitude)) {
				Latitude1 = Latitude;
				Longtitude1 = Longtitude;
//				getActivity().stopService(new Intent("com.lis99.mobile.service.LocService"));
				getActivity().stopService(new Intent(getActivity(), com.lis99.mobile.service.LocService.class));
				if (!emptyString(city)) {
					postMessage(CITY_CHANGE);
				}
				getLocationCity();
			} else {
				cityid = preferences.getString("cityid", "");
				city = preferences.getString("city", "");
				if (city == null || "".equals(city)) {
					cityid = "1";
					city = "北京";
				}
				postMessage(CITY_CHANGE);
			}
		}
	}

	protected void hideDialog() {

		if (dialogView != null) {
			dialogView.dismiss();
			dialogView = null;
		}
	}

	protected void showWaiting(Context context) {
		dialogView = new Dialog(context, R.style.theme_dialog_alert);
		dialogView.setContentView(R.layout.window_layout);
		dialogView.setCancelable(true);
		dialogView
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						hideDialog();
					}
				});
		dialogView.show();
	}

	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
//		DemoApplication.initImageLoader(getActivity(), opt);
	}

	public void onFooterRefresh(PullToRefreshView view) {
		if (allEnd) {
			ShowUtil.showToast(getActivity(), R.string.isLast);
		} else {
			viewdp();
		}
		refreshView.onFooterRefreshComplete();
	}

	void resetState() {
		offset = 0;
		discountEnd = false;
		discountTail = 0;
		allEnd = false;
		viewdp();
	}

	public void onHeaderRefresh(PullToRefreshView view) {
		resetState();
		refreshView.onHeaderRefreshComplete();
	}

	protected void dialog() {

		AlertDialog.Builder builder = new Builder(getActivity());

		builder.setMessage("你当前位置是" + gpsCity + ",是否切换？");
		builder.setTitle("提示");

		builder.setPositiveButton("切换", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				
				cityid = gpsCityid;
				city = gpsCity;
				
				postMessage(CITY_CHANGE);
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