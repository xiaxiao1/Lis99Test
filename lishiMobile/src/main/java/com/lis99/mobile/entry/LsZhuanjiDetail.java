package com.lis99.mobile.entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huewu.pla.lib.MultiColumnListView;
import com.lis99.mobile.R;
import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuanjiBean;
import com.lis99.mobile.application.data.ZhuanjiCate;
import com.lis99.mobile.application.data.ZhuanjiCateChild;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.entry.view.MyLinerLayout;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class LsZhuanjiDetail extends ActivityPattern implements
		IWXAPIEventHandler {

	String id;
	ImageView iv_back, iv_home, iv_share;
	AsyncLoadImageView iv_zhuanji_pic;
	TextView tv_zhuanji_detail, tv_title;
	private static final int SHOW_ZHUANGBEI_CONTENT = 200;
	AutoResizeListView ls_zhuanjicate_lv;
	ScrollView sv_zhuanji;
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	public static String mAppid;
	private Tencent tencent;
	private View ll_text;
	ImageView arrowView;
	

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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, this);
		setIntent(intent);
		api.handleIntent(intent, this);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_zhuanji_detail);
		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);
		id = getIntent().getStringExtra("id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getZhuanjiDetail(id);
	}

	private void getZhuanjiDetail(String id2) {
		String url = C.ZHUANGBEI_ALBUM_DETAIL_URL + id;
		Task task = new Task(null, url, null, "ZHUANJI_DETAIL_URL", this);
		publishTask(task, IEvent.IO);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANJI_DETAIL_URL")) {
					parserZhuanjiDetailList(result);
				}
			}
			break;
		default:
			break;
		}

	}

	ZhuanjiBean zj;
	List<ZhuanjiCate> listCates;

	private void parserZhuanjiDetailList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				zj = (ZhuanjiBean) DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_ZHUANJI_DETAIL);
				listCates = zj.getListCates();
				postMessage(SHOW_ZHUANGBEI_CONTENT);

			} else {
				postMessage(DISMISS_PROGRESS);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	CateListAdapter listAdapter;

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_CONTENT:
			iv_zhuanji_pic.setImage(zj.getImage(), null, null);
			tv_zhuanji_detail.setText(zj.getDesc());
			String title = zj.getTitle();
			if (title != null && title.length() > 7) {
				title = zj.getTitle().substring(0, 7) + "...";
			}
			tv_title.setText(title);
			listAdapter = new CateListAdapter();
			ls_zhuanjicate_lv.setAdapter(listAdapter);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// sv.scrollTo(0, 0);
					sv_zhuanji.scrollTo(0, 0);
					sv_zhuanji.setVisibility(View.VISIBLE);
					postMessage(DISMISS_PROGRESS);
				}
			}, 100);
			break;

		}
		return true;
	}

	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_zhuanji_pic = (AsyncLoadImageView) findViewById(R.id.iv_zhuanji_pic);
		tv_zhuanji_detail = (TextView) findViewById(R.id.tv_zhuanji_detail);
		tv_title = (TextView) findViewById(R.id.tv_title);
		ls_zhuanjicate_lv = (AutoResizeListView) findViewById(R.id.ls_zhuanjicate_lv);
		sv_zhuanji = (ScrollView) findViewById(R.id.sv_zhuanji);
		sv_zhuanji.setVisibility(View.INVISIBLE);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		ll_text = findViewById(R.id.ll_text);
		arrowView = (ImageView) findViewById(R.id.iv_iv);
	}

	private void setListener() {
		ll_text.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_home.setOnClickListener(this);
		iv_share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == iv_home.getId()) {
			Intent intent = new Intent(this, NewHomeActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == iv_share.getId()) {
			showShareList();
		} else if (v.getId() == ll_text.getId()) {
			if(tv_zhuanji_detail.getMaxLines() == 3){
				tv_zhuanji_detail.setMaxLines(Integer.MAX_VALUE);
				arrowView.setImageResource(R.drawable.detail_arrow_up);
			}else{
				tv_zhuanji_detail.setMaxLines(3);
				arrowView.setImageResource(R.drawable.detail_arrow_down);
			}
			
		}

	}

	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "推荐个好东西给大家［" + zj.getTitle()
									+ "］" + zj.getShareUrl()
									+ "－分享自@砾石 Android版";
							LsWeiboSina.getInstance(LsZhuanjiDetail.this)
									.share(shareText);
							break;
						case 1:
							Bitmap bmp1 = ImageCacheManager.getInstance()
									.getBitmapFromCache(zj.getImage());
							String shareWx1Text = zj.getShareUrl();
							String title1 = zj.getTitle();
							String desc1 = "分享个好东西给你【" + zj.getTitle() + "】";
							LsWeiboWeixin
									.getInstance(LsZhuanjiDetail.this)
									.getApi()
									.handleIntent(getIntent(),
											LsZhuanjiDetail.this);
							LsWeiboWeixin.getInstance(LsZhuanjiDetail.this)
									.share(shareWx1Text, title1, desc1, bmp1,
											SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Bitmap bmp2 = ImageCacheManager.getInstance()
									.getBitmapFromCache(zj.getImage());
							String shareWx2Text = zj.getShareUrl();
							String title2 = zj.getTitle();
							String desc2 = "分享＃装备专辑＃【" + zj.getTitle() + "】";
							LsWeiboWeixin
									.getInstance(LsZhuanjiDetail.this)
									.getApi()
									.handleIntent(getIntent(),
											LsZhuanjiDetail.this);
							LsWeiboWeixin
									.getInstance(LsZhuanjiDetail.this)
									.share(shareWx2Text, title2, desc2, bmp2,
											SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
									zj.getTitle());
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"砾石上看到［" + zj.getTitle() + "］不错，推荐给大家。");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									zj.getShareUrl());
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(LsZhuanjiDetail.this, params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													LsZhuanjiDetail.this,
													"onCancel: ");
										}

										@Override
										public void onComplete(Object response) {
											// TODO Auto-generated method stub
											Util2.toastMessage(
													LsZhuanjiDetail.this,
													"onComplete: ");
										}

										@Override
										public void onError(UiError e) {
											// TODO Auto-generated method stub
											Util2.toastMessage(
													LsZhuanjiDetail.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}

									});
							break;
						case 4:
							String shareWx4Text = "推荐个好东西给大家［" + zj.getTitle()
									+ "］" + zj.getShareUrl()
									+ "－分享自@砾石 Android版";
							LsWeiboTencent.getInstance(LsZhuanjiDetail.this)
									.share(shareWx4Text);
						}
					}
				});
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	private static class CateHolder {
		LinearLayout ll_title;
		TextView tv_title, tv_desc;
		MyLinerLayout ls_cate_layout;
		MultiColumnListView mlv_list;
	}

	private class CateListAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public CateListAdapter() {
			inflater = LayoutInflater.from(LsZhuanjiDetail.this);
		}

		@Override
		public int getCount() {
			return listCates.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listCates.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CateHolder holder;
			ZhuanjiCate zc = listCates.get(position);
			final int pos = position;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.ls_zhuanjicate_list_item, null);
				holder = new CateHolder();
				holder.ll_title = (LinearLayout) convertView
						.findViewById(R.id.ll_title);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tv_desc = (TextView) convertView
						.findViewById(R.id.tv_desc);
				// holder.ls_cate_layout = (MyLinerLayout)
				// convertView.findViewById(R.id.ls_cate_layout);
				holder.mlv_list = (MultiColumnListView) convertView
						.findViewById(R.id.mlv_list);
				convertView.setTag(holder);
			} else {
				holder = (CateHolder) convertView.getTag();
			}
			if (zc.getTitle() != null && !"".equals(zc.getTitle())) {
				holder.ll_title.setVisibility(View.VISIBLE);
				holder.tv_title.setText(zc.getTitle());
				holder.tv_desc.setText(zc.getDesc());
			} else {
				holder.ll_title.setVisibility(View.GONE);
			}
			CateAdapter adapter = new CateAdapter(zc.getChilds());
			holder.mlv_list.setAdapter(adapter);
			return convertView;
		}
	}

	private static class ViewHolder {
		AsyncLoadImageView ls_zhuangbei_item_pic;
		TextView ls_zhuangbei_item_title, ls_zhuangbei_item_score;
		LinearLayout ls_zhuangbei_item_star;
	}

	private class CateAdapter extends ArrayAdapter {

		LayoutInflater inflater;
		List<ZhuanjiCateChild> catelist;

		public CateAdapter(List<ZhuanjiCateChild> list) {
			super(LsZhuanjiDetail.this, R.layout.ls_xuan_zhuangbei_item, list);
			catelist = list;
			inflater = LayoutInflater.from(LsZhuanjiDetail.this);
		}

		@Override
		public int getCount() {
			return catelist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return catelist.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int pos = position;
			ZhuanjiCateChild zb = catelist.get(position);
			final String id = zb.getId();
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.ls_xuan_zhuangbei_item1, null);
				holder = new ViewHolder();
				holder.ls_zhuangbei_item_pic = (AsyncLoadImageView) convertView
						.findViewById(R.id.ls_zhuangbei_item_pic);
				holder.ls_zhuangbei_item_title = (TextView) convertView
						.findViewById(R.id.ls_zhuangbei_item_title);
				holder.ls_zhuangbei_item_score = (TextView) convertView
						.findViewById(R.id.ls_zhuangbei_item_score);
				holder.ls_zhuangbei_item_star = (LinearLayout) convertView
						.findViewById(R.id.ls_zhuangbei_item_star);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuangbei_item_pic
						.getLayoutParams();
				ll.width = StringUtil.getXY(LsZhuanjiDetail.this)[0] / 2
						- StringUtil.dip2px(LsZhuanjiDetail.this, 20);
				ll.height = ll.width;
				holder.ls_zhuangbei_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ls_zhuangbei_item_pic.setImage(zb.getThumb(), null, null,
					true);
			holder.ls_zhuangbei_item_title.setText(zb.getTitle());
			holder.ls_zhuangbei_item_score.setText(zb.getRecommend_score());
			holder.ls_zhuangbei_item_star.removeAllViews();
			int score = (int) (Float.parseFloat(zb.getRecommend_score()));
			for (int i = 0; i < score; i++) {
				ImageView iv = new ImageView(LsZhuanjiDetail.this);
				iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				iv.setLayoutParams(ll);
				holder.ls_zhuangbei_item_star.addView(iv);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsZhuanjiDetail.this,
							LsZhuangbeiDetail.class);
					intent.putExtra("id", id);
					LsZhuanjiDetail.this.startActivity(intent);
				}
			});
			return convertView;
		}

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

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			System.out.println("COMMAND_GETMESSAGE_FROM_WX");
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			System.out.println("COMMAND_SHOWMESSAGE_FROM_WX");
			break;
		default:
			break;
		}
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

}
