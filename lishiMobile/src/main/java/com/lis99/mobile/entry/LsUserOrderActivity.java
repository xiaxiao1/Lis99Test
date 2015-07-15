package com.lis99.mobile.entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.StatusUtil;

public class LsUserOrderActivity extends ActivityPattern {
	public static WebView webview;
	private ImageView iv_back;
	private TextView tv_title;
	private Intent intent;
	private String murl = "http://m.lis99.com/qiangyouhui/orderList";
	private String cookieStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ls_my_order);
		StatusUtil.setStatusBar(this);

		setListener();

		// CookieSyncManager.createInstance(this);
		// CookieSyncManager.getInstance().sync();
		// CookieManager cookieManager = CookieManager.getInstance();
		// String cookie = cookieManager
		// .getCookie(url);
		// System.out.println(cookie + "111111111111111111111111111111");

		initData();
	}

	private void initData() {
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (tv_title.getText().equals("我的订单")) {
					// if (arg0.getId() == iv_back.getId() )) {
					// Intent intent = new Intent(getApplicationContext(),
					// MainActivity.class);
					// startActivity(intent);
					finish();
				} else if (murl.equals("http://m.lis99.com/qiangyouhui/")
						| murl.equals("http://m.lis99.com/qiangyouhui")) {
					intent = new Intent(getApplicationContext(),
							NewHomeActivity.class);
					startActivity(intent);
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
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
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
					String defaultValue, JsPromptResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 如果页面被强制关闭,弹窗提示：是否确定离开？ 点击确定 保存数据离开，点击取消，停留在当前页面
			 */
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tv_title.setText(title);
			}

		});
		webview.setWebViewClient(new WebViewClient() {
			

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用当前的WebView加载页面
				view.loadUrl(url);
				murl = url;
				if (!murl.equals("http://m.lis99.com/qiangyouhui/")
						&& !murl.equals("http://m.lis99.com/qiangyouhui")) {
					iv_back.setBackgroundResource(R.drawable.ls_page_back_icon);

				} else {
					iv_back.setBackgroundResource(R.drawable.ls_page_home_icon);
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
				cookieStr = cookieManager.getCookie("http://m.lis99.com/qiangyouhui");
				CookieSyncManager.createInstance(getApplicationContext())
						.sync();
				System.out.println(cookieStr + "3333333333333333333333");
				super.onPageFinished(view, url);
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

	@SuppressLint("JavascriptInterface")
	private void setListener() {
		webview = (WebView) findViewById(R.id.webview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);

		
		webview.addJavascriptInterface(new WebAppInterface(
				LsUserOrderActivity.this), "share");

		// 首先得同步cookie
		 String url = "http://api.lis99.com/shop/signin";
//		String url = "http://m.lis99.com/qiangyouhui/activity";
		 String id = DataManager.getInstance().getUser().getUser_id();
		 

		String cookies =  cookieStr+"; user_id="+id;


            if (null != id){
                  webview.loadUrl("http://m.lis99.com/qiangyouhui/orderList?user_id=" + id);
            } else {
                  webview.loadUrl("http://m.lis99.com/qiangyouhui/orderList");
            }

		initData();

	}

	public class WebAppInterface {
		private Activity activity;

		public WebAppInterface(Activity activity) {
			this.activity = activity;
		}

	}
}
