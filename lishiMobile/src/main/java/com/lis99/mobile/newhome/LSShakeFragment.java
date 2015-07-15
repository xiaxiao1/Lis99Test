package com.lis99.mobile.newhome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.DataCleanManager;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.Util;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;

import org.apache.http.HttpResponse;
import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class LSShakeFragment extends LSFragment implements IWXAPIEventHandler,IWeiboHandler.Response {
	public static WebView webview;
	private View iv_back;
	private ImageView leftView;
	private TextView tv_title;
	Dialog dialogView = null;
	private Tencent tencent;
	private LinearLayout ll_gb;
	private IWXAPI api;
	private int weixin = 1;
	IWeiboShareAPI mWeiboShareAPI;
	private SoundPool sp;// 声明一个SoundPool
	private int music;// 定义一个整型用load（）；来设置suondID
	private String murl = "http://m.lis99.com/qiangyouhui";
	private String nurl = "http://www.lis99.com/yyyguide.php";
	private UserBean userBean;

	// private LsControllShake mShaker;
	private CookieManager cookieManager;
	private HttpResponse response;
	private String user_id;
	private String email;
	private String password;
	private String sn;
	private String nickname;
	protected Tencent mTencent;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				String url = "http://m.lis99.com/qiangyouhui/doLogin";
				String data = "email=" + email + "&userid=" + user_id + "&sn="
						+ sn;
				webview.postUrl(url, EncodingUtils.getBytes(data, "base64"));
			} else if (msg.what == 1) {
				webview.loadUrl(murl);
				System.out.println("没登录走这里");
			} else {
				super.handleMessage(msg);
			}
		};
	};

	protected void initViews(android.view.ViewGroup container) {
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.ls_qiang_youhui, container, false);
		
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 第一次同步
				CookieSyncManager syncManager = CookieSyncManager.createInstance(getActivity());
				syncManager.sync();

				initWeibo(savedInstanceState);
				userBean = DataManager.getInstance().getUser();
				user_id = userBean.getUser_id();
				email = userBean.getEmail();
				sn = userBean.getSn();
				password = SharedPreferencesHelper.getValue(getActivity(), C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.PASSWORD);

				cookieManager = CookieManager.getInstance();

				cookieManager.setCookie("http://m.lis99.com/qiangyouhui/activity",
						"user_id=" + user_id);

				syncManager.sync();

				String cookie = cookieManager
						.getCookie("http://m.lis99.com/qiangyouhui/activity");


				setListener();
				initData();
	}


	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getActivity(), C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getActivity().getIntent(), this);
		}
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				getActivity().getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(getActivity(),
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(getActivity(),
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire)) {
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(getActivity(), C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
	}

	@SuppressLint("JavascriptInterface")
	public void setListener() {
		webview = (WebView) findViewById(R.id.webview);
		iv_back = findViewById(R.id.iv_back);
		leftView = (ImageView) findViewById(R.id.titleLeftImage);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, getActivity());
		WebSettings setting = webview.getSettings();
		setting.setJavaScriptEnabled(true);

		setting.setSavePassword(false);
		setting.setSaveFormData(false);
		// // 设置webview不适用缓存
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);

		setting.setDefaultTextEncodingName("utf-8");

		webview.addJavascriptInterface(new WebAppInterface(
				getActivity()), "share");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = "http://m.lis99.com/qiangyouhui/doLogin";
				String data = null;
				data = "email=" + email + "&" + "password=" + password;

				System.out.println(password
						+ "--------------------------------------"
						+ DataManager.getInstance().isLogin_flag());

				DataCleanManager.cleanDatabaseByName(getActivity().getApplicationContext(),
						"webview.db");
				DataCleanManager.cleanDatabaseByName(getActivity().getApplicationContext(),
						"webviewCache.db");
				if (DataManager.getInstance().isLogin_flag()
						&& password != null && !"".equals(user_id)
						&& !"".equals(password) && password.trim() != "") {
					// 判断这里的登录情况
					// ,区别是app登录的还是webview返回cookie里的-----------------------------------

					webview.postUrl(url, EncodingUtils.getBytes(data, "base64"));
					System.out.println("已经登录了");
				} else if (DataManager.getInstance().isLogin_flag()
						&& !TextUtils.isEmpty(user_id)
						&& !TextUtils.isEmpty(sn)) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
		initData();
	}

	public void initData() {
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// 第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		music = sp.load(getActivity(), R.raw.shake_sound, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if(getLSActivity() != null){
				// getLSActivity().toggleMenu();
				// }

				if (murl.equals("http://m.lis99.com/qiangyouhui/")
						| murl.equals("http://m.lis99.com/qiangyouhui")) {
					if (getLSActivity() != null) {
						getLSActivity().toggleMenu();
					} 
				} else {
					webview.loadUrl("javascript:backUrl()");
				}
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			/*
			 * 此处覆盖的是javascript中的alert方法。当网页需要弹出alert窗口时，会执行onJsAlert中的方法
			 * 网页自身的alert方法不会被调用。
			 */
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(getActivity().getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				Toast.makeText(getActivity().getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				Toast.makeText(getActivity().getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 如果页面被强制关闭,弹窗提示：是否确定离开？ 点击确定 保存数据离开，点击取消，停留在当前页面
			 */
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				Toast.makeText(getActivity().getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			public void onReceivedTitle(WebView view, String title1) {
				super.onReceivedTitle(view, title1);
				if (title1.equals("收银台-高端版-支付宝")) {
					// ll_gb.setVisibility(View.VISIBLE);
					// iv_back.setVisibility(View.INVISIBLE);
				} else {
					iv_back.setVisibility(View.VISIBLE);
					// ll_gb.setVisibility(View.INVISIBLE);
				}
				tv_title.setText(title1);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			private String nicknames;

			/*
			 * 点击页面的某条链接进行跳转的话，会启动系统的默认浏览器进行加载，调出了我们本身的应用
			 * 因此，要在shouldOverrideUrlLoading方法中
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用当前的WebView加载页面
				view.loadUrl(url);
				murl = url;
				if (!murl.equals("http://m.lis99.com/qiangyouhui/")
						&& !murl.equals("http://m.lis99.com/qiangyouhui")) {
					leftView.setImageResource(R.drawable.ls_page_back_icon);
				} else {
					leftView.setImageDrawable(null);

					// 读取cookie
					getsCookies();
				}
				return true;
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				CookieManager cookieManager = CookieManager.getInstance();
				String CookieStr = cookieManager.getCookie(url);
				super.onPageFinished(view, url);
			}

			private void getsCookies() {
				CookieSyncManager cookieSyncManager = CookieSyncManager
						.createInstance(getActivity().getApplicationContext());
				cookieSyncManager.sync();
				CookieManager cookieManager = CookieManager.getInstance();
				// cookieManager.getCookie("http://api.lis99.com/shop/signin");
				String cookie = cookieManager
						.getCookie("http://m.lis99.com/qiangyouhui/activity");
				System.out.println(cookie + "这个是登录页面传来的");
				cookie = URLDecoder.decode(cookie);

				if (cookie != null && cookie.length() > 4) {
					String[] cookies = cookie.split("; ");
					HashMap<String, String> cookieMap = null;
					try {
						cookieMap = Util.parserCookie(cookies);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

					if (null != cookieMap) {
						userBean.setEmail(cookieMap.get("email"));
						userBean.setUser_id(cookieMap.get("user_id"));
						// if (!"".endsWith(cookieMap.get("headicon"))&& null !=
						// cookieMap.get("headicon")) {}
						userBean.setHeadicon(cookieMap.get("headicon"));
						userBean.setNickname(cookieMap.get("nickname"));
						nicknames = cookieMap.get("nickname");
						DataManager.getInstance().setUser(userBean);
					}

					if (nicknames == null || "".equals(nicknames)) {
						DataManager.getInstance().setLogin_flag(false);
						DataManager.getInstance().getUser().setUser_id(null);
					} else {
						DataManager.getInstance().setLogin_flag(true);
					}
				}
			}

			/*
			 * 加载页面资源
			 */
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			/*
			 * 错误提示
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (murl.equals("http://m.lis99.com/qiangyouhui/")
					| murl.equals("http://m.lis99.com/qiangyouhui")) {
				return false;
			} else {
				webview.loadUrl("javascript:backUrl()");
				return true;
			}
		}
		return false;
	}

	public class WebAppInterface {
		private Activity activity;

		public WebAppInterface(Activity activity) {
			this.activity = activity;
		}

		public void shareFunction() {
			showShareList();
		}

	}

	private void showShareList() {
		postMessage(ActivityPattern1.POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {

					private Bitmap bp;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 分享到微博
							String shareText = "砾石带你看ISPO - 五星酒店降价竞拍，0元入住就在掌中。 http://www.lis99.com/rdguide.php";

							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.qiang_weibo);

							LsWeiboSina.getInstance(getActivity())
									.shares(shareText, bp, 1);

							break;
						case 1:
							// 分享到微信好友
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.qiang_wechat);
							String shareWx1Text = "http://m.lis99.com/rdguide.php";
							String title1 = "砾石带你看ISPO，五星酒店降价竞拍";
							String desc1 = "砾石带你看ISPO - 五星酒店降价竞拍，0元入住就在掌中。";

							LsWeiboWeixin
									.getInstance(getActivity())
									.getApi()
									.handleIntent(getActivity().getIntent(),
											LSShakeFragment.this);
							LsWeiboWeixin.getInstance(getActivity())
									.share(shareWx1Text, title1, desc1, bp,
											SendMessageToWX.Req.WXSceneSession);
							weixin = 2;
							break;
						case 2:
							// 分享到微信朋友圈
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.qiang_wechat);
							weixin = 3;
							String shareWx2Text = "http://m.lis99.com/rdguide.php";
							String title2 = "砾石带你看ISPO，五星酒店降价竞拍";
							String desc2 = "砾石带你看ISPO - 五星酒店降价竞拍，0元入住就在掌中。";
							LsWeiboWeixin
									.getInstance(getActivity())
									.getApi()
									.handleIntent(getActivity().getIntent(),
											LSShakeFragment.this);
							LsWeiboWeixin
									.getInstance(getActivity())
									.share(shareWx2Text, title2, desc2, bp,
											SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
									"摇一摇");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									nurl);
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(getActivity(), params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													getActivity(),
													"onCancel: ");
										}

										@Override
										public void onError(UiError e) {
											Util2.toastMessage(
													getActivity(),
													"onComplete: "
															+ e.errorMessage,
													"e");
										}

										@Override
										public void onComplete(Object arg0) {
											Util2.toastMessage(
													getActivity(),
													"onComplete: ");
											webview.loadUrl("javascript:shareCallback('qzone')");
										}

									});
							break;

						case 4:

							String shareWx4Text = "＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！"
									+ nurl;

							LsWeiboTencent.getInstance(getActivity())
									.share(shareWx4Text, bp);
							break;
						}
					}
				});
		if (LsWeiboSina.ret == 1) {
			webview.loadUrl("javascript:shareCallback('weibo')");
		}
	}

	HttpCallback mCallBack = new HttpCallback() {
		@Override
		public void onResult(Object object) {
			ModelResult result = (ModelResult) object;
			if (result != null && result.isSuccess()) {
				Toast.makeText(getActivity(), "分享成功",
						Toast.LENGTH_SHORT).show();
				webview.loadUrl("javascript:shareCallback('tweibo')");
			} else {
				Toast.makeText(getActivity(), "分享失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};// 回调函数

	@Override
	public void onResponse(BaseResponse arg0) {
		switch (arg0.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG)
					.show();
			webview.loadUrl("javascript:shareCallback('weibo')");
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG)
					.show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(getActivity(),
					"分享失败" + "Error Message: " + arg0.errMsg, Toast.LENGTH_LONG)
					.show();
			break;
		}
	}

	
	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			if (weixin == 3) {
				webview.loadUrl("javascript:shareCallback('weixin')");
			}
			if (weixin == 2) {
				webview.loadUrl("javascript:shareCallback('wxfriend ')");
			}
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
		Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onReq(BaseReq req) {
	}
}