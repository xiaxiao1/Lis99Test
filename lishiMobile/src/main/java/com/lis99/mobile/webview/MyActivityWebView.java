package com.lis99.mobile.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ShareModel;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ShareManager;

public class MyActivityWebView extends LSBaseActivity
{
	private WebView webView;

	private String title;

	private String url;
	//网页登陆用到的
	private boolean NeedReload;

	private PopupWindow pop;

	private String image_url;

	private int topic_id;

	private View layout_main;

	private ImageView titleRightImage1;

	private String shareTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_webview);

		title = getIntent().getStringExtra("TITLE");

		url = getIntent().getStringExtra("URL");

		//帖子
		image_url = getIntent().getStringExtra("IMAGE_URL");

		topic_id = getIntent().getIntExtra("TOPIC_ID", 0);

		initViews();
		setTitle(title);



		init();
	}

	@Override
	protected void rightAction() {
		super.rightAction();

//		if ( "积分商城".equals(title))
//		{
//			webView.reload();
//			return;
//		}
//
//		//如果没有title 默认添加一个， 朋友圈没有title不能分享
//		if ( TextUtils.isEmpty(title) )
//		{
//			title = "砾石 心户外，新生活";
//		}
//		ShareModel share = new ShareModel();
//
//		share.title = title;
//		share.imageUrl = image_url;
//		share.shareUrl = url;
//
//		pop = ShareManager.getInstance().showPopWindowInShare(share, layout_main, null);

	}

	@Override
	protected void leftAction() {
		if ( webView.canGoBack() )
		{
			webView.goBack();
		}
		else
		{
			finish();
		}
	}

	private void init()
	{

		titleRightImage1 = (ImageView) findViewById(R.id.titleRightImage1);
		titleRightImage1.setOnClickListener(this);
		titleRightImage.setOnClickListener(this);

		if ( "积分商城".equals(title))
		{
//			setLeftView(-1);
			ViewGroup.LayoutParams lp = titleRightImage.getLayoutParams();// new RelativeLayout.LayoutParams(Common.px2dip(30), Common.px2dip(30));
			lp.height = Common.dip2px(16);
			lp.width = Common.dip2px(16);
			titleRightImage.setLayoutParams(lp);
//			setRightView(R.drawable.mywebview_delete);
			setRightView(R.drawable.club_main_refresh);
			titleRightImage1.setVisibility(View.GONE);
		}
		else
		{
			setRightView(R.drawable.share);
			titleRightImage1.setVisibility(View.VISIBLE);
		}

		layout_main = findViewById(R.id.layout_main);

		webView = (WebView) findViewById(R.id.webView);
		
		WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);


        webView.setWebChromeClient(new MyWebChromeClient());
        
        webView.setWebViewClient( new MyWebClinet());

        // 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
        // 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
        webView.addJavascriptInterface(new LSJavaScriptInterface(), "LS");

        webView.loadUrl(url);
//        webView.loadUrl("file:///android_asset/web/test.html");
	}
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if ( NeedReload )
		{
//			webView.reload();
			NeedReload = false;
			String userId = DataManager.getInstance().getUser().getUser_id();
			if (TextUtils.isEmpty(userId) || webView == null ) return;
			webView.loadUrl("javascript:sendUserId("+userId+")");
		}
	}
	
	// 这是他定义由 addJavascriptInterface 提供的一个Object
    final class LSJavaScriptInterface {
        LSJavaScriptInterface() {
        }

		/**
		 * 			跳转到全部活动页
		 */
		@JavascriptInterface
		public void  goActiveMain()
		{
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(activity, ActiveAllActivity.class);
					startActivity(intent);
				}
			});

		}

		/**
		 * 		调用原生分享
		 * @param title
		 * @param content
		 * @param image_url
		 * @param url
		 */
		@JavascriptInterface
		public void shareTo (final String title, final String content, final String image_url, final String url)
		{
			Common.log("title ="+title+"\n content="+content+"\n image_url="+image_url+"\n url="+url);

			mHandler.post(new Runnable() {
				@Override
				public void run() {
					ShareModel model = new ShareModel();
					model.title = title;
					model.shareTxt = content;
					model.imageUrl = image_url;
					model.shareUrl = url;
					pop = ShareManager.getInstance().showPopWindoInWeb(model, webView);
				}
			});
		}

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * 这不是呼吁界面线程。发表一个运行调用
         * loadUrl on the UI thread.
         * loadUrl在UI线程。
         */
        @JavascriptInterface
        public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
            mHandler.post(new Runnable() {
                public void run() {
                    // 此处调用 HTML 中的javaScript 函数
//                    webView.loadUrl("javascript:wave()");
					webView.loadUrl("javascript:sendUserId()");
                }
            });
        }
        @JavascriptInterface
        public void clickTest ( )
        {
//        	Toast.makeText(WebViewDemo.this, "test = ", Toast.LENGTH_LONG).show();
        }
//        精选黄崖关登录
        @JavascriptInterface
        public String getUserId ()
        {
        	if ( !Common.isLogin(activity) )
        	{
        		NeedReload = true;
        		return "";
        	}
        	String userId = DataManager.getInstance().getUser().getUser_id();
        	return userId;
        }


		//		跳转铁子
		@JavascriptInterface
		public void goTopicInfo ( final int topic_id, int type )
		{
			final int id = type;
			LSBaseActivity.activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Intent intent = null;
					switch (id) {
						//            话题
//            线下贴
						case 0:
						case 1:
							intent = new Intent(LSBaseActivity.activity, LSClubTopicActivity.class);
							intent.putExtra("topicID", topic_id);
							LSBaseActivity.activity.startActivity(intent);
							break;
//						新版活动
						case 5:
							intent = new Intent(LSBaseActivity.activity, LSClubTopicActiveOffLine.class);
							intent.putExtra("topicID", topic_id);
							LSBaseActivity.activity.startActivity(intent);
							break;
//            线上贴
						case 2:
							intent = new Intent(LSBaseActivity.activity, LSClubTopicNewActivity.class);
							intent.putExtra("topicID", topic_id);
							LSBaseActivity.activity.startActivity(intent);
							break;
//						新版话题
						case 6:
							intent = new Intent(LSBaseActivity.activity, LSClubNewTopicListMain.class);
							intent.putExtra("TOPICID", ""+topic_id);
							startActivity(intent);
							break;
					}
				}
			});
		}

        
    }
	
	
	final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            Common.toast("="+message + "===" + result.toString());
            
            return true;
        }

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			// TODO Auto-generated method stub
//			Common.toast("="+message + "===" + result.toString());
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			// TODO Auto-generated method stub
//			Common.toast("="+message + "===" + result.toString());
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
			shareTitle = title;
		}


        
        
    }
    
    class MyWebClinet extends WebViewClient
    {

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), "WebViewClient.onLoadResource", Toast.LENGTH_SHORT).show();
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), "WebViewClient.onPageFinished", Toast.LENGTH_SHORT).show(); 
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), "WebViewClient.onPageStarted", Toast.LENGTH_SHORT).show(); 
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
    	
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ( requestCode == 999 )
		{

		}


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if ( pop != null && pop.isShowing() )
		{
			pop.dismiss();
			pop = null;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
		{
			if ( "积分商城".equals(title) )
			{
				if ( !isMain(webView.getUrl()) )
				{
					webView.loadUrl(url);
					return true;
				}
				else
				{
					return super.onKeyDown(keyCode, event);
				}
			}
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isMain ( String url )
	{
		if (this.url.equals(url))
		{
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch ( arg0.getId() )
		{
			case R.id.titleRightImage1:
				webView.reload();
				break;
			case R.id.titleRightImage:
				if ( "积分商城".equals(title))
				{
					webView.reload();
					return;
				}

				//如果没有title 默认添加一个， 朋友圈没有title不能分享
				if ( TextUtils.isEmpty(shareTitle) )
				{
					shareTitle = "砾石 心户外，新生活";
				}
				ShareModel share = new ShareModel();

				share.title = shareTitle;
				share.imageUrl = image_url;
				share.shareUrl = url;

				pop = ShareManager.getInstance().showPopWindowInShare(share, layout_main, null);
				break;
		}
	}
}
