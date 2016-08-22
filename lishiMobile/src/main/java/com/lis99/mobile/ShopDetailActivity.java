package com.lis99.mobile;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.Util;
import com.lis99.mobile.entity.bean.DisBean;
import com.lis99.mobile.entity.bean.IFTRUEBean;
import com.lis99.mobile.entity.bean.PJBean;
import com.lis99.mobile.entity.bean.ShopDetailBean;
import com.lis99.mobile.entity.item.DisItem;
import com.lis99.mobile.entity.item.Goods;
import com.lis99.mobile.entity.item.Info;
import com.lis99.mobile.entity.item.Pic;
import com.lis99.mobile.entity.item.ShopDetailItem;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.entry.adapter.DPAdapter;
import com.lis99.mobile.entry.adapter.ImageAdapter;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.entry.view.scroll.PullToRefreshView;
import com.lis99.mobile.entry.view.scroll.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.util.constens;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopDetailActivity extends ActivityPattern implements
		IWXAPIEventHandler, OnHeaderRefreshListener, IWeiboHandler.Response {
	private HttpNetRequest httpNetRequest;
	private String oid;
	private LinearLayout ll_dizhi;
	private TextView tv_dianpu;
	private TextView tv_pingjia;
	private TextView tv_dizhi;
	private LinearLayout ll_dianhua;
	private TextView tv_dianhua;
	private TextView data_pingpai;
	private TextView data_chanping;
	private TextView tv_pinglun;
	private TextView data_pinglun;
	private LinearLayout ll_pingpai;
	private LinearLayout ll_pinglun;
	private LinearLayout ll_home;
	private ImageView iv_list;
	private ImageView iv_serch;
	private ImageView iv_star1;
	private ImageView iv_star2;
	private ImageView iv_star3;
	private ImageView iv_star4;
	private ImageView iv_star5;
	private ImageView iv_star11;
	private ImageView iv_star12;
	private ImageView iv_star13;
	private ImageView iv_star14;
	private ImageView iv_star15;
	private ImageView iv_user;
	private TextView tv_pj;
	Dialog dialogView = null;
	private TextView tv_user;
	private TextView tv_bc;
	private String user_id = "";
	private TextView tv_time;
	private Gallery img_gallery;
	IWeiboShareAPI mWeiboShareAPI;
	private ShopDetailItem shopDetailItem;
	private PopupWindow popupWindow;
	private Info info;
	private LinearLayout layoutpop;

	private Context context;
	private ListView list;
	private int fav;
	private String dis = "";
	private String Fav = "";
	private int wide = 0;
	private int hight = 0;
	private RelativeLayout rl_cx1;
	private RelativeLayout rl_cx2;
	private RelativeLayout rl_cx3;
	private RelativeLayout rl_cx4;
	private RelativeLayout rl_cx5;
	private RelativeLayout rl_cx6;
	private TextView tv_cx;
	private TextView tv_cx1;
	private TextView tv_cx2;
	private TextView tv_cx3;
	private TextView tv_cx4;
	private TextView tv_cx5;
	private TextView tv_dz;
	private TextView tv_dz1;
	private TextView tv_dz2;
	private TextView tv_dz3;
	private TextView tv_dz4;
	private TextView tv_dz5;
	private ImageView iv_cx;
	private ImageView iv_cx1;
	private ImageView iv_cx2;
	private ImageView iv_cx3;
	private ImageView iv_cx4;
	private ImageView iv_cx5;
	private TextView tv_tv;
	private String title;
	private LinearLayout ll_dzcx;
	private ArrayList<String> arrayList;
	private DPAdapter<String> dpAdapter;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	private PullToRefreshView refreshView;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageAdapter<Pic> imageAdapter;
	private Tencent tencent;
	Bitmap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		refreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);
		context = ShopDetailActivity.this;
		initOptions();
		setListener();
		initData();

		View ll_bottom = findViewById(R.id.ll_bottom);
		ll_bottom.setVisibility(View.GONE);

	}

	public int getLayoutId() {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		return R.layout.activity_shop_detail;
	}

	public void setListener() {
		oid = this.getIntent().getStringExtra(constens.OID);
		if (this.getIntent().getStringExtra("fav") != null) {
			Fav = this.getIntent().getStringExtra("fav");
		}
		dis = this.getIntent().getStringExtra("dis");
		layoutpop = (LinearLayout) LayoutInflater.from(ShopDetailActivity.this)
				.inflate(R.layout.ls_buy_popupwindow, null);
		iv_cx = (ImageView) findViewById(R.id.iv_cx);
		iv_cx1 = (ImageView) findViewById(R.id.iv_cx1);
		iv_cx2 = (ImageView) findViewById(R.id.iv_cx2);
		iv_cx3 = (ImageView) findViewById(R.id.iv_cx3);
		iv_cx4 = (ImageView) findViewById(R.id.iv_cx4);
		iv_cx5 = (ImageView) findViewById(R.id.iv_cx5);
		rl_cx1 = (RelativeLayout) findViewById(R.id.rl_cx1);
		rl_cx2 = (RelativeLayout) findViewById(R.id.rl_cx2);
		rl_cx3 = (RelativeLayout) findViewById(R.id.rl_cx3);
		rl_cx4 = (RelativeLayout) findViewById(R.id.rl_cx4);
		rl_cx5 = (RelativeLayout) findViewById(R.id.rl_cx5);
		rl_cx6 = (RelativeLayout) findViewById(R.id.rl_cx6);
		tv_cx = (TextView) findViewById(R.id.tv_cx);
		tv_cx1 = (TextView) findViewById(R.id.tv_cx1);
		tv_cx2 = (TextView) findViewById(R.id.tv_cx2);
		tv_cx3 = (TextView) findViewById(R.id.tv_cx3);
		tv_cx4 = (TextView) findViewById(R.id.tv_cx4);
		tv_cx5 = (TextView) findViewById(R.id.tv_cx5);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		tv_dz1 = (TextView) findViewById(R.id.tv_dz1);
		tv_dz2 = (TextView) findViewById(R.id.tv_dz2);
		tv_dz3 = (TextView) findViewById(R.id.tv_dz3);
		tv_dz4 = (TextView) findViewById(R.id.tv_dz4);
		tv_dz5 = (TextView) findViewById(R.id.tv_dz5);
		ll_dzcx = (LinearLayout) findViewById(R.id.ll_dzcx);
		list = (ListView) layoutpop.findViewById(R.id.lv_pop);
		tv_pj = (TextView) findViewById(R.id.tv_pj);
		tv_tv = (TextView) findViewById(R.id.tv_tv);
		tv_bc = (TextView) findViewById(R.id.tv_bc);
		iv_user = (ImageView) findViewById(R.id.iv_user);
		tv_user = (TextView) findViewById(R.id.tv_user);
		iv_serch = (ImageView) findViewById(R.id.iv_serch);
		tv_dianpu = (TextView) findViewById(R.id.tv_dianpu);
		tv_pingjia = (TextView) findViewById(R.id.tv_pingjia);
		tv_dizhi = (TextView) findViewById(R.id.tv_dizhi);
		tv_dianhua = (TextView) findViewById(R.id.tv_dianhua);
		data_pingpai = (TextView) findViewById(R.id.data_pingpai);
		data_chanping = (TextView) findViewById(R.id.data_chanping);
		ll_dianhua = (LinearLayout) findViewById(R.id.ll_dianhua);
		ll_dizhi = (LinearLayout) findViewById(R.id.ll_dizhi);
		tv_pinglun = (TextView) findViewById(R.id.tv_pinglun);
		data_pinglun = (TextView) findViewById(R.id.data_pinglun);
		ll_pingpai = (LinearLayout) findViewById(R.id.ll_pingpai);
		ll_pinglun = (LinearLayout) findViewById(R.id.ll_pinglun);
		img_gallery = (Gallery) findViewById(R.id.img_gallery);
		iv_star1 = (ImageView) findViewById(R.id.iv_star1);
		iv_star2 = (ImageView) findViewById(R.id.iv_star2);
		iv_star3 = (ImageView) findViewById(R.id.iv_star3);
		iv_star4 = (ImageView) findViewById(R.id.iv_star4);
		iv_star5 = (ImageView) findViewById(R.id.iv_star5);
		iv_star11 = (ImageView) findViewById(R.id.iv_star11);
		iv_star12 = (ImageView) findViewById(R.id.iv_star12);
		iv_star13 = (ImageView) findViewById(R.id.iv_star13);
		iv_star14 = (ImageView) findViewById(R.id.iv_star14);
		iv_star15 = (ImageView) findViewById(R.id.iv_star15);
		iv_list = (ImageView) findViewById(R.id.iv_list);
		tv_time = (TextView) findViewById(R.id.tv_time);
		imageAdapter = new ImageAdapter<Pic>(ShopDetailActivity.this, null);
		// img_gallery.setX(this.getWindowManager().getDefaultDisplay().getWidth());
		img_gallery.setAdapter(imageAdapter);
		ll_home = (LinearLayout) findViewById(R.id.ll_home);
		initData();
		viewdp();
		viewdis();
		wides();
	}

	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire)) {
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
	}

	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
//		DemoApplication.initImageLoader(ShopDetailActivity.this, opt);
	}

	public void initData() {
		rl_cx1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		rl_cx2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		rl_cx3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		rl_cx4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		rl_cx5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		rl_cx6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DZCXActivity.class);
				intent.putExtra("oid", oid);
				intent.putExtra("title", info.getTitle());
				startActivity(intent);
			}
		});
		iv_serch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				iflog();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					iflogback("0");
					popupWindow.dismiss();
					popupWindow = null;
				} else if (arg2 == 1) {
					Intent intent = new Intent(ShopDetailActivity.this,
							DPCWActivity.class);
					intent.putExtra("shop_id", info.getOid());
					intent.putExtra("area", info.getCt());
					intent.putExtra("name", info.getTitle());
					intent.putExtra("address", info.getAddress());
					intent.putExtra("tel", info.getPhone());
					startActivity(intent);
					popupWindow.dismiss();
					popupWindow = null;
				} else if (arg2 == 2) {
					dialogbc();

					popupWindow.dismiss();
					popupWindow = null;
				} else if (arg2 == 3) {
					Intent intent = new Intent(ShopDetailActivity.this,
							QTActivity.class);
					intent.putExtra("shop_id", info.getOid());
					startActivity(intent);
					popupWindow.dismiss();
					popupWindow = null;
				} else if (arg2 == 4) {
					popupWindow.dismiss();
					popupWindow = null;
				}
			}

		});
		tv_bc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;

				} else {
					int y = getWindowManager().getDefaultDisplay().getHeight() / 2;

					int x = getWindowManager().getDefaultDisplay().getWidth();

					showPopupWindow(x, y, 3);

				}
			}
		});
		tv_pj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						PJActivity.class);
				intent.putExtra(constens.ID, oid);
				intent.putExtra("module", "outdoor");
				startActivity(intent);

			}
		});
		ll_dizhi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (info != null) {
					Intent intent = new Intent(ShopDetailActivity.this,
							ShopDetailMapActivity.class);
					intent.putExtra("data", info);
					intent.putExtra("dis", dis);
					startActivity(intent);
				}

			}
		});
		ll_pinglun.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetailActivity.this,
						DisActivity.class);
				intent.putExtra(constens.ID, oid);
				intent.putExtra("module", "outdoor");
				intent.putExtra("title", title);
				startActivity(intent);

			}
		});
		ll_dianhua.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog();

			}
		});
		iv_list.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatas();

			}
		});
		img_gallery
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						tv_tv.setText((position + 1) + "/"
								+ shopDetailItem.getPic().size());
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// 这里不做响应
					}
				});
		ll_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Fav.equals("fav")) {

					finish();
				} else {

					finish();
				}
			}
		});
	}

	public void addfeedback(final String e_type) {

		new OptData<PJBean>(ShopDetailActivity.this).readData(
				new QueryData<PJBean>() {

					public String callData() {
						// TODO Auto-generated method stub
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("user_id", DataManager.getInstance().getUser()
								.getUser_id());
						data.put("shop_id", info.getOid());
						data.put("e_type", e_type);
						data.put("content", "");
						data.put("area", info.getCity());
						data.put("name", info.getTitle());
						data.put("address", info.getAddress());
						data.put("tel", info.getPhone());
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLBC,
								data);
						return str;
					}

					@Override
					public void showData(PJBean t) {
						// TODO Auto-generated method stub
						if (t != null && t.getData().equals("ok")) {
							ShowUtil.showToast(ShopDetailActivity.this, "提交成功");
						} else {
							ShowUtil.showToast(ShopDetailActivity.this, "提交失败");
						}
					}
				}, PJBean.class);

	}

	public void viewdp() {
		showWaiting(ShopDetailActivity.this);
		new OptData<ShopDetailBean>(ShopDetailActivity.this).readData(
				new QueryData<ShopDetailBean>() {

					public String callData() {

						String data = "?"
								+ constens.ID
								+ "="
								+ oid
								+ "&user_id="
								+ DataManager.getInstance().getUser()
										.getUser_id();
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest
								.connect(constens.URL1, data);
						return str;

					}

					public void showData(ShopDetailBean t) {
						if (t != null) {
							shopDetailItem = t.getData();
							if (shopDetailItem != null) {

								if (shopDetailItem.getGoods() != null
										&& shopDetailItem.getGoods().size() != 0) {
									ArrayList<Goods> arrayList = new ArrayList<Goods>();
									arrayList = shopDetailItem.getGoods();

									int arrayListSize = arrayList.size();
									if (arrayListSize >= 1) {
										tv_cx.setText("￥"
												+ arrayList.get(0)
														.getSalePrice()
														.toString());
										tv_dz.setText(arrayList.get(0)
												.getDiscount().toString()
												+ "折");
										imageLoader.displayImage(
												"http://i1.lis99.com"
														+ arrayList.get(0)
																.getImage()
																.toString(), iv_cx);

									} else {
										tv_cx.setText("");
										tv_dz.setVisibility(View.INVISIBLE);
									}
									if (arrayListSize >= 2) {
										tv_cx1.setText("￥"
												+ arrayList.get(1)
														.getSalePrice()
														.toString());
										tv_dz1.setText(arrayList.get(1)
												.getDiscount().toString()
												+ "折");
										imageLoader
										.displayImage("http://i1.lis99.com"
												+ arrayList.get(1)
														.getImage()
														.toString(), iv_cx1);

									} else {
										tv_cx1.setText("");
										tv_dz1.setVisibility(View.INVISIBLE);
									}
									if (arrayListSize >= 3) {
										tv_cx2.setText("￥"
												+ arrayList.get(2)
														.getSalePrice()
														.toString());
										tv_dz2.setText(arrayList.get(2)
												.getDiscount().toString()
												+ "折");
										imageLoader
										.displayImage("http://i1.lis99.com"
												+ arrayList.get(2)
														.getImage()
														.toString(), iv_cx2);
									} else {
										tv_cx2.setText("");
										tv_dz2.setVisibility(View.INVISIBLE);
									}
									if (arrayListSize >= 4) {

										tv_cx3.setText("￥"
												+ arrayList.get(3)
														.getSalePrice()
														.toString());
										tv_dz3.setText(arrayList.get(3)
												.getDiscount().toString()
												+ "折");
										imageLoader
										.displayImage("http://i1.lis99.com"
												+ arrayList.get(3)
														.getImage()
														.toString(), iv_cx3);
									} else {
										tv_cx3.setText("");
										tv_dz3.setVisibility(View.INVISIBLE);
									}
									if (arrayListSize >= 5) {
										tv_cx4.setText("￥"
												+ arrayList.get(4)
														.getSalePrice()
														.toString());
										tv_dz4.setText(arrayList.get(4)
												.getDiscount().toString()
												+ "折");
										imageLoader
										.displayImage("http://i1.lis99.com"
												+ arrayList.get(4)
														.getImage()
														.toString(), iv_cx4);

									} else {
										tv_cx4.setText("");
										tv_dz4.setVisibility(View.INVISIBLE);
									}
									if (arrayListSize >= 6) {
										tv_cx5.setText("￥"
												+ arrayList.get(5)
														.getSalePrice()
														.toString());
										tv_dz5.setText(arrayList.get(5)
												.getDiscount().toString()
												+ "折");
										imageLoader
										.displayImage("http://i1.lis99.com"
												+ arrayList.get(5)
														.getImage()
														.toString(), iv_cx5);
									} else {
										tv_cx5.setText("");
										tv_dz5.setVisibility(View.INVISIBLE);
									}


								} else {
									ll_dzcx.setVisibility(View.GONE);
								}
								map(shopDetailItem.getPic().get(0).getTh_pic());
								info = shopDetailItem.getInfo();

								if (info != null) {
									fav = info.getFav();
									title = info.getTitle();
									tv_dianpu.setText(info.getTitle());
									tv_pingjia.setText(info.getCount() + "人评价");
									if (info.getAddress() == "") {
										ll_dizhi.setVisibility(View.GONE);
									} else {
										tv_dizhi.setText(info.getAddress());
									}

									if (info.getPhone() == "") {
										ll_dianhua.setVisibility(View.GONE);
									} else {
										tv_dianhua.setText(info.getPhone());

									}
									if (shopDetailItem.getBrand() == null) {
										ll_pingpai.setVisibility(View.GONE);
									} else {
										String string = "";
										if (shopDetailItem.getBrand().size() > 1) {
											for (int i = 0; i < shopDetailItem
													.getBrand().size(); i++) {

												string += shopDetailItem
														.getBrand().get(i)
														.getTitle()
														+ "、";

											}
										} else {
											string += shopDetailItem.getBrand()
													.get(0).getTitle();

										}
										data_pingpai.setText(string);
									}

									if (info.getCount() == 0) {
										ll_pinglun.setVisibility(View.GONE);
									} else {
										tv_pinglun.setText("店铺评价  （共"
												+ info.getCount() + "条）");
									}
									ll_pinglun.setVisibility(View.GONE);
									data_chanping.setText(info.getProduct());
									imageAdapter.setData(shopDetailItem
											.getPic());
									tv_tv.setText(1 + "/"
											+ shopDetailItem.getPic().size());
									if (info.getFav() == 1) {
										iv_serch.setImageResource(R.drawable.detail_favorited);
									}
									float f = Float.parseFloat(info.getStar());
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
								}
							}
						} else {
							ShowUtil.showToast(ShopDetailActivity.this,
									getString(R.string.lj_false));
						}
					}
				}, ShopDetailBean.class);
	}

	public void viewdis() {

		new OptData<DisBean>(ShopDetailActivity.this).readData(
				new QueryData<DisBean>() {

					public String callData() {

						String data = "?" + constens.ID + "=" + oid;
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLdis,
								data);
						return str;

					}

					public void showData(DisBean t) {
						hideDialog();
						// TODO Auto-generated method stub
						if (t != null) {
							DisItem disItem = t.getData();
							if (disItem != null) {

								float f = Float.parseFloat(disItem.getStar());
								if (f >= 3) {
									iv_star11
											.setImageResource(R.drawable.hwd_large_star_s);
									iv_star12
											.setImageResource(R.drawable.hwd_large_star_s);
									iv_star13
											.setImageResource(R.drawable.hwd_large_star_s);
									if (f == 3.5) {
										iv_star14
												.setImageResource(R.drawable.hwd_large_star_b);
									}
									if (f == 4) {
										iv_star14
												.setImageResource(R.drawable.hwd_large_star_s);
									}
									if (f == 4.5) {
										iv_star14
												.setImageResource(R.drawable.hwd_large_star_s);
										iv_star15
												.setImageResource(R.drawable.hwd_large_star_b);
									}
									if (f == 5) {
										iv_star14
												.setImageResource(R.drawable.hwd_large_star_s);
										iv_star15
												.setImageResource(R.drawable.hwd_large_star_s);
									}

								} else if (f >= 2) {
									iv_star11
											.setImageResource(R.drawable.hwd_large_star_s);
									iv_star12
											.setImageResource(R.drawable.hwd_large_star_s);
									if (f == 2.5) {
										iv_star13
												.setImageResource(R.drawable.hwd_large_star_b);
									} else if (f >= 1) {
										iv_star11
												.setImageResource(R.drawable.hwd_large_star_s);
										if (f == 1.5) {
											iv_star12
													.setImageResource(R.drawable.hwd_large_star_b);
										}
									} else if (f == 0.5) {
										iv_star11
												.setImageResource(R.drawable.hwd_large_star_b);
									}
								}
								data_pinglun.setText(disItem.getComment());
								tv_user.setText(disItem.getNickname());
								tv_time.setText(disItem.getCreatedate());
								if (disItem.getHeadicon() != "") {
									imageLoader.displayImage(
											disItem.getHeadicon(), iv_user);
								}

							}
						}
					}

				}, DisBean.class);
	}

	private void showDatas() {
		if (shopDetailItem != null) {
			Info info = shopDetailItem.getInfo();
			if (info != null) {

				showShareList(info);
			}
		}
	}

	private void showShareList(final Info info) {

		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "推荐一买户外装备的地方［" + info.getTitle()
									+ "］" + info.getShare_url()
									+ "－分享自@砾石 Android版";
							LsWeiboSina.getInstance(ShopDetailActivity.this)
									.share(shareText);
							break;
						case 1:
							Resources res = getResources();
							Bitmap bmp1 = BitmapFactory.decodeResource(res,
									R.drawable.icon_ls);
							// Bitmap bmp3 =
							// ImageCacheManager.getInstance().getBitmapFromCache(getImage(String
							// path));

							String shareWx1Text = info.getShare_url();
							String title1 = info.getTitle();
							String desc1 = info.getDescribe();
							LsWeiboWeixin
									.getInstance(ShopDetailActivity.this)
									.getApi()
									.handleIntent(getIntent(),
											ShopDetailActivity.this);
							LsWeiboWeixin.getInstance(ShopDetailActivity.this)
									.share(shareWx1Text,
											title1,
											desc1,
											map(shopDetailItem.getPic().get(0)
													.getTh_pic()),
											SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Resources res1 = getResources();
							Bitmap bmp2 = BitmapFactory.decodeResource(res1,
									R.drawable.icon_ls);
							Bitmap bmp4 = ImageCacheManager.getInstance()
									.getBitmapFromCache(
											shopDetailItem.getPic().get(0)
													.getTh_pic());
							String shareWx2Text = info.getShare_url();
							String title2 = info.getTitle();
							String desc2 = info.getDescribe();
							LsWeiboWeixin
									.getInstance(ShopDetailActivity.this)
									.getApi()
									.handleIntent(getIntent(),
											ShopDetailActivity.this);
							LsWeiboWeixin
									.getInstance(ShopDetailActivity.this)
									.share(shareWx2Text,
											title2,
											desc2,
											map(shopDetailItem.getPic().get(0)
													.getTh_pic()),
											SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
									info.getTitle());
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"推荐一买户外装备的地方［" + info.getTitle() + "］ ");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									info.getShare_url());
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(ShopDetailActivity.this,
									params, new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													ShopDetailActivity.this,
													"onCancel: ");
										}

										@Override
										public void onComplete(
												Object response) {
											// TODO Auto-generated method stub
											Util2.toastMessage(
													ShopDetailActivity.this,
													"onComplete: "
															+ response
																	.toString());
										}

										@Override
										public void onError(UiError e) {
											// TODO Auto-generated method stub
											Util2.toastMessage(
													ShopDetailActivity.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}

									});
							break;
						case 4:
							String shareWx4Text = "推荐一买户外装备的地方［"
									+ info.getTitle() + "］"
									+ info.getShare_url()
									+ "－分享自@砾石 Android版";
							LsWeiboTencent.getInstance(ShopDetailActivity.this)
									.share(shareWx4Text);
							break;
						}
					}
				});
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		System.out.println("获取到微信消息了...");
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消发送";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "AUTH_DENIED";
			break;
		default:
			result = "未知错误";
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResponse(BaseResponse arg0) {
		switch (arg0.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, "分享失败" + "Error Message: " + arg0.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	protected void dialog() {

		AlertDialog.Builder builder = new Builder(ShopDetailActivity.this);

		builder.setMessage(tv_dianhua.getText());
		builder.setTitle("提示");

		builder.setPositiveButton("拨打", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ tv_dianhua.getText()));
				// mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
				context.startActivity(intent);
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

	public void showPopupWindow(int x, int y, int i) {
		popupWindow = new PopupWindow(ShopDetailActivity.this);
		popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getWindowManager().getDefaultDisplay()
				.getHeight() - y);
		ColorDrawable dw = new ColorDrawable(getResources().getColor(
				R.color.color_touming));
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(layoutpop);
		popupWindow.showAtLocation(findViewById(R.id.shopdetail), Gravity.LEFT
				| Gravity.TOP, x, y);
		dpAdapter = new DPAdapter<String>(ShopDetailActivity.this, arrayList);
		list.setAdapter(dpAdapter);
		setList();
	}

	public void setList() {
		if (arrayList != null) {
			arrayList = null;
		}
		if (dpAdapter != null) {
			dpAdapter.clearData();
		}
		arrayList = new ArrayList<String>();
		arrayList.add(getString(R.string.yiguan));
		arrayList.add(getString(R.string.xinxi));
		arrayList.add(getString(R.string.weizhi));
		arrayList.add(getString(R.string.qita));
		arrayList.add(getString(R.string.quxiao));
		dpAdapter.setData(arrayList);
		dpAdapter.notifyDataSetChanged();

	}

	public void iflog() {
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			user_id = DataManager.getInstance().getUser().getUser_id();

			if (fav == 0) {
				new OptData<IFTRUEBean>(ShopDetailActivity.this).readData(
						new QueryData<IFTRUEBean>() {

							@Override
							public void showData(IFTRUEBean t) {
								if (t != null) {
									String iftrue = t.getData();
									if (iftrue.equals("ok")) {
										Toast.makeText(ShopDetailActivity.this,
												"收藏成功", 1000).show();
										iv_serch.setImageResource(R.drawable.detail_favorited);

									} else {
										Toast.makeText(ShopDetailActivity.this,
												"收藏失败", 1000).show();
									}
									fav = 1;
								}

							}

							@Override
							public String callData() {
								httpNetRequest = new HttpNetRequest();
								String data = "?user_id=" + user_id
										+ "&shop_id=" + info.getOid();
								String str = httpNetRequest.connect(
										constens.urladdLike, data);
								return str;
							}
						}, IFTRUEBean.class);
			} else if (fav == 1) {
				new OptData<IFTRUEBean>(ShopDetailActivity.this).readData(
						new QueryData<IFTRUEBean>() {

							@Override
							public void showData(IFTRUEBean t) {
								if (t != null) {
									String iftrue = t.getData();
									if (iftrue.equals("ok")) {
										Toast.makeText(ShopDetailActivity.this,
												"取消收藏", 1000).show();
										iv_serch.setImageResource(R.drawable.detail_favorite);

									} else {
										Toast.makeText(ShopDetailActivity.this,
												"取消失败", 1000).show();
									}
									fav = 0;
								}

							}

							@Override
							public String callData() {
								httpNetRequest = new HttpNetRequest();
								String data = "?user_id=" + user_id
										+ "&shop_id=" + info.getOid();
								String str = httpNetRequest.connect(
										constens.urldellike, data);
								return str;
							}
						}, IFTRUEBean.class);
			}

		} else {
			ShowUtil.showToast(ShopDetailActivity.this, "请先登录");
			Intent intent = new Intent(this, LSLoginActivity.class);
			intent.putExtra("unlogin", "unlogin");
			startActivity(intent);
		}
	}

	protected void dialogbc() {

		AlertDialog.Builder builder = new Builder(ShopDetailActivity.this);

		builder.setMessage("我们已经收到您的报错，是否去地图上标注正确的位置？");
		builder.setTitle("提示");

		builder.setPositiveButton("标注", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ShopDetailActivity.this,
						ShopDetailTYPEActivity.class);
				int Dis = (int) Double.parseDouble(dis);
				intent.putExtra("data", info);
				intent.putExtra("dis", Dis);
				startActivity(intent);
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

	public void iflogback(String e_type) {
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			addfeedback(e_type);
		} else {
			postMessage(POPUP_TOAST, "请先登录");
			Intent intent = new Intent(this, LSLoginActivity.class);
			intent.putExtra("unlogin", "unlogin");
			startActivity(intent);
		}
	}

	public void onHeaderRefresh(PullToRefreshView view) {

		viewdp();
		viewdis();
		refreshView.onHeaderRefreshComplete();

	}

	// public static byte[] getImage(String path) throws Exception {
	// URL url = new URL(path);
	// HttpURLConnection httpURLconnection =
	// (HttpURLConnection)url.openConnection();
	// httpURLconnection.setRequestMethod("GET");
	// httpURLconnection.setReadTimeout(6*1000);
	// InputStream in = null;
	// byte[] b = new byte[1024];
	// int len = -1;
	// if (httpURLconnection.getResponseCode() == 200) {
	// in = httpURLconnection.getInputStream();
	// byte[] result = readStream(in);
	// in.close();
	// return result;
	//
	// }
	// return null;
	// }
	//
	// public static byte[] readStream(InputStream in) throws Exception{
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int len = -1;
	// while((len = in.read(buffer)) != -1) {
	// outputStream.write(buffer, 0, len);
	// }
	// outputStream.close();
	// in.close();
	// return outputStream.toByteArray();
	// }
	// public Bitmap getbit(){
	// byte[] data;
	// Bitmap bitMap = null;
	// try {
	// data = getImage(shopDetailItem.getPic().get(0).getTh_pic());
	// String d = new String(data);
	// int length = data.length;
	// bitMap = BitmapFactory.decodeByteArray(data, 0, length);
	// return bitMap;
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// return bitMap;
	// }

	// public Bitmap getBitmap(String path){
	// try{
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	// conn.setConnectTimeout(5000);
	// conn.setRequestMethod("GET");
	// if(conn.getResponseCode() == 200){
	// InputStream inputStream = conn.getInputStream();
	// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	// return bitmap;
	// }}
	// catch(IOException exception){
	// exception.printStackTrace();
	// }
	// return null;
	// }
	public Bitmap map(final String path) {

		Util.threadExecute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					if (conn.getResponseCode() == 200) {
						InputStream inputStream = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						map = bitmap;
					}
				} catch (IOException exception) {
					exception.printStackTrace();
				}

			}
		});
		return map;
	}

	public void wides() {
		Display mDisplay = getWindowManager().getDefaultDisplay();
		wide = mDisplay.getWidth();
		LayoutParams layoutParams;
		layoutParams = rl_cx1.getLayoutParams();
		layoutParams = rl_cx2.getLayoutParams();
		layoutParams = rl_cx3.getLayoutParams();
		layoutParams = rl_cx4.getLayoutParams();
		layoutParams = rl_cx5.getLayoutParams();
		layoutParams = rl_cx6.getLayoutParams();

		layoutParams.width = (wide - 20) / 3;
		layoutParams.height = (wide - 20) / 3 + 20;

		rl_cx1.setLayoutParams(layoutParams);
		rl_cx2.setLayoutParams(layoutParams);
		rl_cx3.setLayoutParams(layoutParams);
		rl_cx4.setLayoutParams(layoutParams);
		rl_cx5.setLayoutParams(layoutParams);
		rl_cx6.setLayoutParams(layoutParams);
		rl_cx1.setPadding(10, 10, 10, 10);
		rl_cx2.setPadding(10, 10, 10, 10);
		rl_cx3.setPadding(10, 10, 10, 10);
		rl_cx4.setPadding(10, 10, 10, 10);
		rl_cx5.setPadding(10, 10, 10, 10);
		rl_cx6.setPadding(10, 10, 10, 10);
	}
}
