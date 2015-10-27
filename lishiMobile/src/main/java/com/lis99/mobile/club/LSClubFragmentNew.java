package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;


public class LSClubFragmentNew extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener
{

	private WebView webView;

	private String title;

	private String url;
	//网页登陆用到的
	private boolean NeedReload;

	private PopupWindow pop;

	private String image_url;

	private int topic_id;

	private RelativeLayout layout_main;

	private ImageView titleLeftImage;

	private ImageView titleRightImage;

	private RelativeLayout titleLeft, titleRight;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		body = View.inflate(getActivity(), R.layout.my_webview_fragment, null);


		//搜索按钮
		titleRightImage = (ImageView) body.findViewById(R.id.titleRightImage);
		titleRightImage.setImageResource(R.drawable.club_search_title_left);
		titleRightImage.setOnClickListener(this);

		titleLeftImage = (ImageView) body.findViewById(R.id.titleLeftImage);
		titleLeftImage.setImageResource(R.drawable.hwd_search);
		titleLeftImage.setOnClickListener(this);

		titleLeft = (RelativeLayout) body.findViewById(R.id.titleLeft);
		titleRight = (RelativeLayout) body.findViewById(R.id.titleRight);
		titleRight.setOnClickListener(this);
		titleLeft.setOnClickListener(this);

		setTitle("砾石聚乐部");

		init();

		return body;
	}

	private void init()
	{

		layout_main = (RelativeLayout) findViewById(R.id.layout_main);

		webView = (WebView) findViewById(R.id.webView);

		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		// 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);


//		webView.setWebChromeClient(new MyWebChromeClient());
//
//		webView.setWebViewClient( new MyWebClinet());

		// 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
		// 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
		webView.addJavascriptInterface(new LSJavaScriptInterface(), "LS");

//        webView.loadUrl(url);
		webView.loadUrl("file:///android_asset/web/index.html");
	}

	// 这是他定义由 addJavascriptInterface 提供的一个Object
	final class LSJavaScriptInterface {
		LSJavaScriptInterface() {
		}

		//跳转到排行
		@JavascriptInterface
		public void ClubTabClick ( int num )
		{
			final int n = num;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					switch (n) {
						case 1:
							Intent intent = new Intent();
							intent.setClass(getActivity(), LSClubLevelActivity.class);
							intent.putExtra("CLUB_LEVEL", 0);
							startActivity(intent);
							break;
						case 2:
							startActivity(new Intent(getActivity(), ActiveAllActivity.class));
							break;
						case 3:
//							startActivity(new Intent(getActivity(), ClubHotTopicActivity.class));
							if ( !Common.isLogin(LSBaseActivity.activity))
							{
								return;
							}
							startActivity(new Intent(getActivity(), MyJoinClubActivity.class));
							break;
					}
				}
			});
		}
//		更多俱乐部
		@JavascriptInterface
		public void getMoreClubList ()
		{
			Intent intent = new Intent(getActivity(), LSClubListActivity.class);
			getActivity().startActivity(intent);
		}

//跳转到俱乐部详情
		@JavascriptInterface
		public void goClubInfo (int club_id)
		{
			final int id = club_id;
			Common.log("goClubInfo  id =" + club_id);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(getActivity(), LSClubDetailActivity.class);
					intent.putExtra("clubID", id);
					getActivity().startActivity(intent);
				}
			});
		}

//加入俱乐部
		@JavascriptInterface
		public void JoinClub ( String club_id )
		{
			LSRequestManager.getInstance().addClub(club_id, new CallBack() {
				@Override
				public void handler(MyTask mTask) {
				}
			});
		}

//赞
		@JavascriptInterface
		public void like(int topic_id)
		{
			LSRequestManager.getInstance().clubTopicLike(topic_id, null);
		}
//		关注
		@JavascriptInterface
		public void attention ( int user_id) {
			LSRequestManager.getInstance().getFriendsAddAttention(user_id, null);
		}
//		跳转铁子
		@JavascriptInterface
		public void goTopicInfo ( final int topic_id, int type )
		{
			final int id = type;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Intent intent = null;
					switch (id) {
						//            话题
						case 0:
//            线下贴
						case 1:
							intent = new Intent(getActivity(), LSClubTopicActivity.class);
							intent.putExtra("topicID", topic_id);
							startActivity(intent);
							break;
//            线上贴
						case 2:
							intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
							intent.putExtra("topicID", topic_id);
							startActivity(intent);
							break;
					}
				}
			});
		}

		/**
		 * 			跳转用户主页
		 * @param userID
		 */
		@JavascriptInterface
		public void goUserHomePage(String userID)
		{
			Common.goUserHomeActivit(getActivity(), userID);
		}

		/**
		 * This is not called on the UI thread. Post a runnable to invoke
		 * 这不是呼吁界面线程。发表一个运行调用
		 * loadUrl on the UI thread.
		 * loadUrl在UI线程。
		 */
		@JavascriptInterface
		public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// 此处调用 HTML 中的javaScript 函数
//                    webView.loadUrl("javascript:wave()");
//					webView.loadUrl("javascript:sendUserId()");
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

			String userId = DataManager.getInstance().getUser().getUser_id();
			if ( TextUtils.isEmpty(userId))
			{
				return "";
			}
			return userId;
		}


		//调用登陆页
		@JavascriptInterface
		public void goLogin ()
		{
			Common.log("goLogin");
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					NeedReload = true;
					Common.isLogin(getActivity());
				}
			});

		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
		{
			webView.goBack();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		switch ( view.getId() )
		{
			case R.id.titleRightImage:
			case R.id.titleRight:
				startActivity(new Intent(getActivity(), LSClubListActivity.class));
				break;
			case R.id.titleLeftImage:
			case R.id.titleLeft:
				startActivity( new Intent(getActivity(), SearchActivity.class));
				break;
		}
	}

	@Override
	public void onResume()
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


	@Override
	public void onFooterRefresh(PullToRefreshView view) {

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {

	}

}
