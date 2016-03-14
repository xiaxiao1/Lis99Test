package com.lis99.mobile;

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
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.dilog.ShopSearchDialog;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entity.bean.DPListBean;
import com.lis99.mobile.entity.item.Citys;
import com.lis99.mobile.entity.item.DPListItem;
import com.lis99.mobile.entity.item.Shop;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.adapter.DPAdapter;
import com.lis99.mobile.entry.adapter.LsBuyAdapter;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class LsBuyActivity extends ActivityPattern implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
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
	private String ct="";
	private TextView tv_city;
	private TextView dian;
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
	
	private final static int CITY_CHANGE = 1001;
	private final static int ASK_CHANGE = 1002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ls_buy);

		StatusUtil.setStatusBar(this);
		
		preferences = getSharedPreferences(constens.CITYINFO, MODE_PRIVATE);
		
		cityid = preferences.getString("cityid", "");
		city = preferences.getString("city", "");
		
		shoptype = getIntent().getStringExtra("shoptype");
		
//		startService(new Intent("com.lis99.mobile.service.LocService"));
		Intent intent = new Intent(this, com.lis99.mobile.service.LocService.class);
		startService(intent);
		offset = 0;
		refreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		layoutpop = (LinearLayout) LayoutInflater.from(LsBuyActivity.this)
				.inflate(R.layout.ls_buy_popupwindow, null);

		getId();
		initOptions();
		// Location location = new Location(LsBuyActivity.this);
		lsBuyAdapter = new LsBuyAdapter<Shop>(LsBuyActivity.this, null);
		listView.setAdapter(lsBuyAdapter);
		listenLL();
		
		

	}

	@Override
	protected void onDestroy() {
//		stopService(new Intent("com.lis99.mobile.service.LocService"));
		stopService(new Intent(activity, com.lis99.mobile.service.LocService.class));
		unregisterReceiver(myReciever);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ls_buy, menu);
		return true;
	}

	public void getId() {
		ls_buy = (LinearLayout) findViewById(R.id.ls_buy);
		iv_serch = (ImageView) findViewById(R.id.iv_serch);
		ll_diqu = (LinearLayout) findViewById(R.id.ll_diqu);
		tv_dp = (TextView) findViewById(R.id.tv_dp);
		tv_px = (TextView) findViewById(R.id.tv_px);
		tv_dq = (TextView) findViewById(R.id.tv_dq);
		dian =(TextView)findViewById(R.id.dian);
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
		list = (ListView) layoutpop.findViewById(R.id.lv_pop);
		iv_home = findViewById(R.id.iv_home);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.lis99.mobile.loc");
		myReciever = new MyReciever();
		registerReceiver(myReciever, intentFilter);
		
		if (!emptyString(city)) {
			tv_city.setText(city);
		}
		
		if("0".equals(shoptype)){
			tv_dp.setText(getString(R.string.huwai));
		}else if("1".equals(shoptype)){
			tv_dp.setText(getString(R.string.shangchang));
		}else if("2".equals(shoptype)){
			tv_dp.setText(getString(R.string.xujudian));
		}

	}

	public void listenLL() {
		
		iv_serch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				relativeLayout.setVisibility(View.GONE);
				ShopSearchDialog dialog = new ShopSearchDialog(
						LsBuyActivity.this, shops, Latitude1, Longtitude1,
						offset, cityid);
				dialog.show();

			}
		});
		ll_diqu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LsBuyActivity.this,
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
							shoptype = null;
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
							sorttype = null;
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
				Intent intent = new Intent(LsBuyActivity.this, NewHomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
//				stopService(new Intent("com.lis99.mobile.service.LocService"));
				stopService(new Intent(activity, com.lis99.mobile.service.LocService.class));
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				Shop shop = (Shop) lsBuyAdapter.getItem(arg2);
				Intent intent = new Intent(LsBuyActivity.this,
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
					Intent intent = new Intent(LsBuyActivity.this,
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
					int y = getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight(); // linearLayout.getHeight()+
					// relativeLayout.getHeight();

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
					int y = getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight();
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
					int y = getWindowManager().getDefaultDisplay().getHeight()
							- listView.getHeight();
					int x = getWindowManager().getDefaultDisplay().getWidth();
					iv_dq.setImageDrawable(getResources().getDrawable(
							R.drawable.hwd_arrow2));

					showPopupWindow(x, y, 1);

				}
				count = 1;
			}
		});

	}

	public void showPopupWindow(int x, int y, int i) {
		popupWindow = new PopupWindow(LsBuyActivity.this);
		popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getWindowManager().getDefaultDisplay()
				.getHeight() - y);
		ColorDrawable dw = new ColorDrawable(getResources().getColor(
				R.color.color_touming));
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(layoutpop);
		popupWindow.showAtLocation(findViewById(R.id.ls_buy), Gravity.LEFT
				| Gravity.TOP, x, y);
		
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){
			@Override
			public void onDismiss() {
				popupWindow = null;
				iv_dq.setImageDrawable(getResources().getDrawable(
						R.drawable.hwd_arrow));
				iv_px.setImageDrawable(getResources().getDrawable(
						R.drawable.hwd_arrow));
				imageViewdianpu.setImageDrawable(getResources()
						.getDrawable(R.drawable.hwd_arrow));
			}
			
		});
		
		dpAdapter = new DPAdapter<String>(LsBuyActivity.this, arrayList);
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
		showWaiting(this);
		if (!discountEnd) {
			new OptData<DPListBean>(this).readData(
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
			new OptData<DPListBean>(this).readData(
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
		showWaiting(this);
		new OptData<DPListBean>(this).readData(
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
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals(constens.getLocationByBaidu)) {
					parserCityInfo(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserCityInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
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
				stopService(new Intent(activity, com.lis99.mobile.service.LocService.class));
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
		try {
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
		}catch (Exception e)
		{

		}
	}

	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		DemoApplication.initImageLoader(this, opt);
	}

	public void onFooterRefresh(PullToRefreshView view) {
		if (allEnd) {
			ShowUtil.showToast(this, R.string.isLast);
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

		AlertDialog.Builder builder = new Builder(this);

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
