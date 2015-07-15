package com.lis99.mobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsActivityComment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.Tencent;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public class LsActivityDetail extends ActivityPattern1 implements
		IWXAPIEventHandler
{

	private int id;
	private String url;
	private String weiboShareUrl;
	private WebView webView;
	private String title;

	private String nurl = "http://www.lis99.com/yyyguide.php";
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	public static String mAppid;
	private Tencent tencent;
	private int weixin = 1;

	private CookieManager cookieManager;
	private String user_id;
	private String email;
	private String password;
	private String sn;
	private String nickname;
	private UserBean userBean;

	private String currentUrl;

	private void initWeibo(Bundle savedInstanceState)
	{
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null)
		{
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire))
		{
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_detail);
		id = getIntent().getIntExtra("id", 0);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		currentUrl = url;
		weiboShareUrl = url.replace("/m.", "/www.");
		if (weiboShareUrl == "-1")
		{
			weiboShareUrl = url;
		}
		initViews();

		initWeibo(savedInstanceState);

		startWebViewThings();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (webView.canGoBack())
			{
				webView.goBack();
			} else
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == 0)
			{
				String url = "http://m.lis99.com/qiangyouhui/doLogin";
				String data = "email=" + email + "&userid=" + user_id + "&sn="
						+ sn;
				webView.postUrl(url, EncodingUtils.getBytes(data, "base64"));
			} else if (msg.what == 1)
			{
				webView.loadUrl(url);
				System.out.println("没登录走这里");
			} else
			{
				super.handleMessage(msg);
			}
		};
	};

	private void initViews()
	{

		StatusUtil.setStatusBar(this);
		webView = (WebView) findViewById(R.id.webView);

		webView.loadUrl(url);

		View shareBtn = findViewById(R.id.iv_share);
		shareBtn.setOnClickListener(this);

		View backBtn = findViewById(R.id.iv_back);
		backBtn.setOnClickListener(this);

	}

	private void startWebViewThings()
	{
		// 第一次同步
		/*
		 * CookieSyncManager syncManager =
		 * CookieSyncManager.createInstance(this); syncManager.sync();
		 * 
		 * 
		 * userBean = DataManager.getInstance().getUser(); user_id =
		 * userBean.getUser_id(); email = userBean.getEmail(); sn =
		 * userBean.getSn(); password = SharedPreferencesHelper.getValue(this,
		 * C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.PASSWORD);
		 * 
		 * cookieManager = CookieManager.getInstance();
		 * 
		 * cookieManager.setCookie("http://m.lis99.com/qiangyouhui/activity",
		 * "user_id=" + user_id);
		 * 
		 * syncManager.sync();
		 * 
		 * 
		 * WebSettings setting = webView.getSettings();
		 * setting.setJavaScriptEnabled(true);
		 * 
		 * setting.setSavePassword(false); setting.setSaveFormData(false); // //
		 * 设置webview不适用缓存 setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		 * 
		 * setting.setDefaultTextEncodingName("utf-8");
		 * 
		 * // WebView.setWebChromeClient(new MyWebChromeClient()); new
		 * Thread(new Runnable() {
		 * 
		 * @Override public void run() { String url =
		 * "http://m.lis99.com/qiangyouhui/doLogin"; String data = null; data =
		 * "email=" + email + "&" + "password=" + password;
		 * 
		 * System.out.println(password +
		 * "--------------------------------------" +
		 * DataManager.getInstance().isLogin_flag());
		 * 
		 * DataCleanManager.cleanDatabaseByName(getApplicationContext(),
		 * "webview.db");
		 * DataCleanManager.cleanDatabaseByName(getApplicationContext(),
		 * "webviewCache.db"); if (DataManager.getInstance().isLogin_flag() &&
		 * password != null && !"".equals(user_id) && !"".equals(password) &&
		 * password.trim() != "") { // 判断这里的登录情况 //
		 * ,区别是app登录的还是webview返回cookie里的-----------------------------------
		 * 
		 * webView.postUrl(url, EncodingUtils.getBytes(data, "base64"));
		 * System.out.println("已经登录了"); } else if
		 * (DataManager.getInstance().isLogin_flag() &&
		 * !TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(sn)) {
		 * handler.sendEmptyMessage(0); } else { handler.sendEmptyMessage(1); }
		 * } }).start();
		 */

		webView.setWebChromeClient(new WebChromeClient()
		{
			/*
			 * 此处覆盖的是javascript中的alert方法。当网页需要弹出alert窗口时，会执行onJsAlert中的方法
			 * 网页自身的alert方法不会被调用。
			 */
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result)
			{
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result)
			{
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result)
			{
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 如果页面被强制关闭,弹窗提示：是否确定离开？ 点击确定 保存数据离开，点击取消，停留在当前页面
			 */
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result)
			{
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			public void onReceivedTitle(WebView view, String title1)
			{
				super.onReceivedTitle(view, title1);

			}
		});

		webView.setWebViewClient(new WebViewClient()
		{
			private String nicknames;

			/*
			 * 点击页面的某条链接进行跳转的话，会启动系统的默认浏览器进行加载，调出了我们本身的应用
			 * 因此，要在shouldOverrideUrlLoading方法中
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				// 使用当前的WebView加载页面
				// view.loadUrl(url);
				// currentUrl = url;
				if (url.contains("comment"))
				{
					Intent intent = new Intent(LsActivityDetail.this,
							LsActivityComment.class);
					Bundle b = new Bundle();
					b.putInt("id", id);
					intent.putExtras(b);
					startActivity(intent);
				} else
				{
					view.loadUrl(url);
					currentUrl = url;
				}
				return true;
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageFinished(WebView view, String url)
			{
				CookieManager cookieManager = CookieManager.getInstance();
				String CookieStr = cookieManager.getCookie(url);
				super.onPageFinished(view, url);
			}

			private void getsCookies()
			{
				CookieSyncManager cookieSyncManager = CookieSyncManager
						.createInstance(getApplicationContext());
				cookieSyncManager.sync();
				CookieManager cookieManager = CookieManager.getInstance();
				// cookieManager.getCookie("http://api.lis99.com/shop/signin");
				String cookie = cookieManager
						.getCookie("http://m.lis99.com/qiangyouhui/activity");
				System.out.println(cookie + "这个是登录页面传来的");
				cookie = URLDecoder.decode(cookie);

				if (cookie != null && cookie.length() > 4)
				{
					String[] cookies = cookie.split("; ");
					HashMap<String, String> cookieMap = null;
					try
					{
						cookieMap = Util.parserCookie(cookies);
					} catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}

					if (null != cookieMap)
					{
						userBean.setEmail(cookieMap.get("email"));
						userBean.setUser_id(cookieMap.get("user_id"));
						// if (!"".endsWith(cookieMap.get("headicon"))&& null !=
						// cookieMap.get("headicon")) {}
						userBean.setHeadicon(cookieMap.get("headicon"));
						userBean.setNickname(cookieMap.get("nickname"));
						nicknames = cookieMap.get("nickname");
						DataManager.getInstance().setUser(userBean);
					}

					if (nicknames == null || "".equals(nicknames))
					{
						DataManager.getInstance().setLogin_flag(false);
						DataManager.getInstance().getUser().setUser_id(null);
					} else
					{
						DataManager.getInstance().setLogin_flag(true);
					}
				}
			}

			/*
			 * 加载页面资源
			 */
			@Override
			public void onLoadResource(WebView view, String url)
			{
				super.onLoadResource(view, url);
			}

			/*
			 * 错误提示
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl)
			{
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.iv_back:
				if (webView.canGoBack())
				{
					webView.goBack();
				} else
				{
					finish();
				}
				break;
			case R.id.iv_share:
				showShareList();
				break;
			default:
				break;
		}
	}

	private void showShareList()
	{
		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener()
				{
					private Bitmap bp;

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// 新浪微博分享
								String shareText = title + "详情戳这里:"
										+ weiboShareUrl;
								bp = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.ls_activity_share_weibo);
								LsWeiboSina.getInstance(LsActivityDetail.this)
										.shares(shareText, bp, 0);
								// LsWeiboSina.getInstance(LsActivityItem.this)
								// .share(shareText);
								break;
							case 1:
								// 微信好友分享
								bp = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.ls_activity_share);
								String shareWx1Text = url;
								String title1 = title + "详情戳这里:";
								String desc1 = title + "详情戳这里:" + url;
								LsWeiboWeixin
										.getInstance(LsActivityDetail.this)
										.getApi()
										.handleIntent(getIntent(),
												LsActivityDetail.this);
								LsWeiboWeixin
										.getInstance(LsActivityDetail.this)
										.share(shareWx1Text,
												title1,
												desc1,
												bp,
												SendMessageToWX.Req.WXSceneSession);
								weixin = 2;
								break;
							case 2:
								// 微信朋友圈分享
								bp = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.ls_activity_share);
								weixin = 3;
								String shareWx2Text = url;
								String title2 = title + "详情戳这里:";
								String desc2 = title + "详情戳这里:" + url;
								LsWeiboWeixin
										.getInstance(LsActivityDetail.this)
										.getApi()
										.handleIntent(getIntent(),
												LsActivityDetail.this);
								LsWeiboWeixin
										.getInstance(LsActivityDetail.this)
										.share(shareWx2Text,
												title2,
												desc2,
												bp,
												SendMessageToWX.Req.WXSceneTimeline);

								break;
						}
					}
				});
	}

	private Response wh = new Response()
	{
		@Override
		public void onResponse(BaseResponse arg0)
		{
			switch (arg0.errCode)
			{
				case WBConstants.ErrorCode.ERR_OK:
					Toast.makeText(LsActivityDetail.this, "分享成功",
							Toast.LENGTH_LONG).show();
					break;
				case WBConstants.ErrorCode.ERR_CANCEL:
					Toast.makeText(LsActivityDetail.this, "取消分享",
							Toast.LENGTH_LONG).show();
					break;
				case WBConstants.ErrorCode.ERR_FAIL:
					Toast.makeText(LsActivityDetail.this,
							"分享失败" + "Error Message: " + arg0.errMsg,
							Toast.LENGTH_LONG).show();
					break;
			}
		}
	};

	@Override
	public void onReq(BaseReq req)
	{
		switch (req.getType())
		{
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
	public void onResp(BaseResp resp)
	{
		System.out.println("获取到微信消息了...");
		String result = "";

		switch (resp.errCode)
		{
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
