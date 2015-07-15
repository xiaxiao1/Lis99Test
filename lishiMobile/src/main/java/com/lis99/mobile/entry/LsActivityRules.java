package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
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

import java.util.ArrayList;

public class LsActivityRules extends ActivityPattern implements
		IWXAPIEventHandler {

	private ImageView iv_back;
	private ImageView iv_share;
	private WebView webview;
	private IWXAPI api;
	private Tencent tencent;
	IWeiboShareAPI mWeiboShareAPI;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_activity_rules);

		StatusUtil.setStatusBar(this);

		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);

		setview();
		setlistener();
	}

	private void setlistener() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		bundle = this.getIntent().getExtras();
		String url = bundle.getString("xurl");
		Log.i("aa", url);
		new Thread(new Runnable() {

			@Override
			public void run() {
				webview.loadUrl(bundle.getString("xurl"));
			}
		}).start();
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
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

	private void setview() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		webview = (WebView) findViewById(R.id.webview);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == iv_back.getId()) {
			finish();
		} else if (arg0.getId() == iv_share.getId()) {
			showShareList();
		}
	}

	private void showShareList() {

		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";

							LsWeiboSina.getInstance(LsActivityRules.this)
									.share(shareText);
							break;
						case 1:
							Resources res = getResources();
							Bitmap bmp1 = BitmapFactory.decodeResource(res,
									R.drawable.icon_ls);
							// Bitmap bmp3 =
							// ImageCacheManager.getInstance().getBitmapFromCache(getImage(String
							// path));

							String shareWx1Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							// String title1 = info.getTitle();
							// String desc1 = info.getDescribe();
							String title1 = "haha";
							String desc1 = "hehe";
							Bitmap bmp2 = BitmapFactory.decodeResource(res,
									R.drawable.icon_ls);
							LsWeiboWeixin
									.getInstance(LsActivityRules.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityRules.this);
							LsWeiboWeixin.getInstance(LsActivityRules.this)
									.share(shareWx1Text, title1, desc1, bmp2,
											SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Resources res1 = getResources();
							Bitmap bmp3 = BitmapFactory.decodeResource(res1,
									R.drawable.icon_ls);
							// Bitmap bmp4 =
							// ImageCacheManager.getInstance().getBitmapFromCache(shopDetailItem.getPic().get(0).getTh_pic());
							// String shareWx2Text = info.getShare_url();
							// String title2 = info.getTitle();
							// String desc2 = info.getDescribe();
							String shareWx2Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							String title2 = "haha";
							String desc2 = "hehe";
							LsWeiboWeixin
									.getInstance(LsActivityRules.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityRules.this);
							LsWeiboWeixin
									.getInstance(LsActivityRules.this)
									.share(shareWx2Text, title2, desc2, bmp3,
											SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "");
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(LsActivityRules.this, params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													LsActivityRules.this,
													"onCancel: ");
										}

										@Override
										public void onComplete(
												Object response) {
											Util2.toastMessage(
													LsActivityRules.this,
													"onComplete: ");
										}

										@Override
										public void onError(UiError e) {
											Util2.toastMessage(
													LsActivityRules.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}

									});
							break;
						case 4:
							String shareWx4Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							LsWeiboTencent.getInstance(LsActivityRules.this)
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
}
