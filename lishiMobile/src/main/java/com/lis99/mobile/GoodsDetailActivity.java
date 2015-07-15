package com.lis99.mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.DisBean;
import com.lis99.mobile.entity.bean.GetdisBean;
import com.lis99.mobile.entity.bean.GoodsDetailBean;
import com.lis99.mobile.entity.item.DisItem;
import com.lis99.mobile.entity.item.GoodsDetailItem;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.FileInputStream;

public class GoodsDetailActivity extends BaseActivity {

	private String id;
	private GoodsDetailItem gooddetailItem;
	private String content;
	private LinearLayout ll_home;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView iv_pic;
	private TextView tv_gdname;
	private TextView tv_pr;
	private TextView tv_pr1;
	private TextView tv_listdz;
	private TextView tv_pj;
	private HttpNetRequest httpNetRequest;
	private TextView tv_pinglun;
	private ImageView iv_user;
	private TextView data_pinglun;
	private TextView tv_user;
	private TextView tv_time;
	private WebView textView1;
	private String oid;
	private LinearLayout ll_pinglun;
	private String module;
	private String title;
	private WebSettings mWebSettings;

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_goods_detail;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		id = this.getIntent().getStringExtra("id");
		ll_home = (LinearLayout) findViewById(R.id.ll_home);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		tv_gdname = (TextView) findViewById(R.id.tv_gdname);
		tv_pr = (TextView) findViewById(R.id.tv_pr);
		tv_listdz = (TextView) findViewById(R.id.tv_listdz);
		tv_pr1 = (TextView) findViewById(R.id.tv_pr1);
		tv_pinglun = (TextView) findViewById(R.id.tv_pinglun);
		data_pinglun = (TextView) findViewById(R.id.data_pinglun);
		tv_user = (TextView) findViewById(R.id.tv_user);
		tv_pj = (TextView) findViewById(R.id.tv_pj);
		iv_user = (ImageView) findViewById(R.id.iv_user);
		tv_time = (TextView) findViewById(R.id.tv_time);
		textView1 = (WebView) findViewById(R.id.textView1);
		ll_pinglun = (LinearLayout) findViewById(R.id.ll_pinglun);
		initOptions();
		getData();

	}

	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		DemoApplication.initImageLoader(GoodsDetailActivity.this, opt);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		ll_pinglun.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GoodsDetailActivity.this,
						DisActivity.class);
				intent.putExtra(constens.ID, oid);
				intent.putExtra("module", "store_goods");
				intent.putExtra("title", title);
				startActivity(intent);

			}
		});
		tv_pj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GoodsDetailActivity.this,
						PJActivity.class);
				intent.putExtra("module", "store_goods");
				intent.putExtra(constens.ID, gooddetailItem.getShop_id());
				startActivity(intent);
			}
		});
		ll_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getData() {
		new OptData<GoodsDetailBean>(GoodsDetailActivity.this).readData(
				new QueryData<GoodsDetailBean>() {

					private FileInputStream fis;

					@Override
					public void showData(GoodsDetailBean t) {
						// TODO Auto-generated method stub
						if (t != null) {
							gooddetailItem = t.getData();
							if (gooddetailItem != null) {
								viewdis();
								getdis();
								title = gooddetailItem.getTitle();
								content = gooddetailItem.getContent();
								oid = gooddetailItem.getId();
								imageLoader.displayImage(
										gooddetailItem.getImages(), iv_pic);
								tv_gdname.setText(gooddetailItem.getTitle()
										.toString());
								tv_pr.setText("￥"
										+ gooddetailItem.getSalePrice()
												.toString());
								tv_pr1.getPaint().setFlags(
										Paint.STRIKE_THRU_TEXT_FLAG);
								tv_pr1.setText("￥"
										+ gooddetailItem.getMarketPrice()
												.toString());
								tv_listdz.setText(gooddetailItem.getDiscount()
										.toString() + "折");
								// Spanned s = Html.fromHtml(content);
								mWebSettings = textView1.getSettings();
								mWebSettings.setJavaScriptEnabled(true);
								mWebSettings.setBuiltInZoomControls(true);
								mWebSettings.setLightTouchEnabled(true);
								mWebSettings.setSupportZoom(true);

								textView1.setHapticFeedbackEnabled(false);

								textView1.loadDataWithBaseURL(null, content,
										"text/html", "utf-8", null);
							}
						}
					}

					@Override
					public String callData() {
						// TODO Auto-generated method stub
						String url = "http://api.lis99.com/shop/storegoodsinfo?id=";
						HttpNetRequest httpNetRequest = new HttpNetRequest();

						return httpNetRequest.connect(url, id);
					}
				}, GoodsDetailBean.class);
	}

	public void viewdis() {

		new OptData<DisBean>(GoodsDetailActivity.this).readData(
				new QueryData<DisBean>() {

					public String callData() {

						String data = "?" + constens.ID + "="
								+ gooddetailItem.getId()
								+ "&module=store_goods";
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLdis,
								data);
						return str;

					}

					public void showData(DisBean t) {

						// TODO Auto-generated method stub
						if (t != null) {
							DisItem disItem = t.getData();
							if (disItem != null) {

								data_pinglun.setText(disItem.getComment());
								tv_user.setText(disItem.getNickname());
								tv_time.setText(disItem.getCreatedate());
								if (disItem.getHeadicon() != "") {
									imageLoader.displayImage(
											disItem.getHeadicon(), iv_user);
								}

							} else {
								ll_pinglun.setVisibility(View.GONE);
							}
						}
					}

				}, DisBean.class);
	}

	public void getdis() {

		new OptData<GetdisBean>(GoodsDetailActivity.this).readData(
				new QueryData<GetdisBean>() {

					public String callData() {

						String data = "?" + constens.ID + "="
								+ gooddetailItem.getId()
								+ "&module=store_goods";
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(
								"http://api.lis99.com/shop/gettotalcommnum",
								data);
						return str;

					}

					public void showData(GetdisBean t) {
						hideDialog();
						// TODO Auto-generated method stub
						if (t != null) {
							tv_pinglun.setText("评价（共" + t.getData() + "条）");

						}
					}

				}, GetdisBean.class);
	}
}
