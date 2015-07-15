package com.lis99.mobile.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.BannerBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.VersionBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.SildeShowWidget;
import com.lis99.mobile.entry.view.SlidingMenuView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.InternetUtil;
import com.lis99.mobile.util.L;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SelectorUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/*******************************************
 * @ClassName: MainActivity
 * @Description: 首页
 * @author xingyong cosmos250.xy@gmail.com
 * @date 2013-12-26 下午3:21:58
 * 
 ******************************************/
public class MainActivity extends ActivityPattern implements
		GestureDetector.OnGestureListener, OnItemClickListener {

	private final String TAG = "MainActivity";
	
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				postMessage(POPUP_TOAST, "百度地图 key 验证出错! ");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				postMessage(POPUP_TOAST, "网络出错 ");
			}
		}
	}

	private SDKReceiver mReceiver;

	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;// 左边布局
	private LinearLayout layout_right, ll_point;// 右边布局
	private ImageView iv_set, ls_nologin_header;// 图片
	AsyncLoadImageView ls_header;
	private ListView lv_set;// 设置菜单
	private TextView ls_login_text, ls_user_nickname, ls_user_point;// 立即登录

	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 15;

	private final static int sleep_time = 5;

	private static final int SHOW_MAIN_BANNER = 200;
	private static final int HAVE_NEW_VERSION = 201;

	private GestureDetector mGestureDetector;// 手势
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private int window_width;// 屏幕的宽度
	
	private VersionBean vb;

	private View view = null;// 点击的view
	private SlidingMenuView menu_view;

	private SildeShowWidget mViewFlipper;
	private int imageCount = 4;
	private int moveFlag = 0;
	private boolean directionFlag = true;// 锟斤拷锟斤拷锟街撅拷锟�true为锟斤拷锟斤拷锟斤拷锟斤拷flase为锟斤拷锟斤拷锟斤拷锟斤拷
	private int index = 0;
	private RelativeLayout sildeshowLayout, rl_sildeshow,
			sildeshow_title_point_rel;
	private LinearLayout ls_login_ll;
	String login = "";
	Handler mHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				changePointSelect(index);
				break;
			case 101:
				break;
			}
			super.handleMessage(msg);
		}
	};

	class News {
		int resid;
		String title;
	}

	private void changePointSelect(int index) {
		for (int i = 0; i < imageCount; i++) {
			if (i == index) {
				txtTitle[i].setSelected(true);
			} else {
				txtTitle[i].setSelected(false);
			}
		}
	}

	ImageView ls_main_banner, ls_main_wen, ls_main_xuan, ls_main_at,
			ls_main_shakes, ls_main_mai;

	private String title[] = { "待发送队列", "同步分享设置", "编辑我的资料", "找朋友", "告诉朋友",
			"节省流量", "推送设置", "版本更新", "意见反馈", "积分兑换", "精品应用", "常见问题", "退出当前帐号" };
	Bitmap bmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_main);
		StatusUtil.setStatusBar(this);
		L.turnOn();
		L.enable(TAG, L.DEBUG_LEVEL);
		login = getIntent().getStringExtra("login");
		Resources res = getResources();
		bmp = BitmapFactory.decodeResource(res,
				R.drawable.ls_nologin_header_icon);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getMainBannerTask();
		getCheckTask();
		
		// 注册 百度地图SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	private void getCheckTask() {
		Task task = new Task(null, C.MAIN_CHECKVERSION_URL, null, "USER_INFO_URL", this);
		task.setPostData(getCheckVersionParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	
	private String getCheckVersionParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		String version = C.VERSION.replaceAll("\\.", "");
		params.put("version", version);
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	

	private void getMainBannerTask() {
		Task task = new Task(null, C.MAIN_BANNER_URL, null, "MAIN_BANNER_URL",
				this);
		publishTask(task, IEvent.IO);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]
					|| task.getData() instanceof Bitmap) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("MAIN_BANNER_URL")) {
					parserMainBanner(result);
				}else if (((String) task.getParameter()).equals("USER_INFO_URL")) {
					parserVersionInfo(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserVersionInfo(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				vb = (VersionBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_MAIN_CHECKVERSION);
				postMessage(HAVE_NEW_VERSION);
			}
		}			
	}
	
	List<BannerBean> list;

	private void parserMainBanner(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				list = (List<BannerBean>) DataManager.getInstance().jsonParse(
						params, DataManager.TYPE_MAIN_BANNER);
				postMessage(SHOW_MAIN_BANNER);
			} else {

			}
		}

	}
	
	private void showNewVersionDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(vb.getChangelog());
		builder.setTitle("新版本提示");
		builder.setPositiveButton("去更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				Intent intent = new Intent();        
		        intent.setAction("android.intent.action.VIEW");    
		        Uri content_url = Uri.parse(vb.getUrl());   
		        intent.setData(content_url);  
		        startActivity(intent);
			}
		});
		builder.setNegativeButton("暂不", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 敢点暂不，就退出程序
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				android.os.Process.killProcess(Process.myPid());
			}
		});
		builder.create().show();
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case HAVE_NEW_VERSION:
			showNewVersionDialog();
			break;
		case SHOW_MAIN_BANNER:

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.alignWithParent = true;
			mViewFlipper = new SildeShowWidget(this, list);
			mViewFlipper.setLayoutParams(params);
			sildeshowLayout.addView(mViewFlipper);

			imageCount = list.size();
			/*
			 * if(DataManager.getInstance().isLogin_flag()){
			 * mViewFlipper.stopSildeShow(); }else{
			 */
			mViewFlipper.startSildeShow();
			// }
			txtTitle = new TextView[imageCount];
			LinearLayout l = (LinearLayout) findViewById(R.id.sildeshow_title_point_layout);
			sildeshow_title_point_rel = (RelativeLayout) findViewById(R.id.sildeshow_title_point_rel);

			RelativeLayout.LayoutParams ls_l_params = (RelativeLayout.LayoutParams) sildeshow_title_point_rel
					.getLayoutParams();
			ls_l_params.width = StringUtil.getXY((Activity) this)[0] - 2
					* StringUtil.dip2px(this, 10);
			sildeshow_title_point_rel.setLayoutParams(ls_l_params);
			for (int i = 0; i < imageCount; i++) {
				LayoutParams params1 = new LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				params1.setMargins(StringUtil.dip2px(this, 4), 2,
						StringUtil.dip2px(this, 4), 2);
				txtTitle[i] = new TextView(this);
				txtTitle[i].setHeight(StringUtil.dip2px(this, 8));
				txtTitle[i].setWidth(StringUtil.dip2px(this, 8));
				Drawable drawtxt = this.getResources().getDrawable(
						R.drawable.sildeshow_title_point);
				txtTitle[i].setBackgroundDrawable(drawtxt);
				txtTitle[i].setLayoutParams(params1);
				l.addView(txtTitle[i]);
			}

			mDisplayViewIDThread.start();
			break;
		}
		return true;
	}

	private void setListener() {
		ls_main_mai.setOnClickListener(this);
		ls_main_xuan.setOnClickListener(this);
		ls_main_at.setOnClickListener(this);
		ls_main_shakes.setOnClickListener(this);
		ls_nologin_header.setOnClickListener(this);
		ls_login_text.setOnClickListener(this);
		ls_header.setOnClickListener(this);
	}

	private TextView txtTitle[];
	MenuAdapter adapter;
	Thread mDisplayViewIDThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (index != mViewFlipper.getDisplayedChild()) {
					index = mViewFlipper.getDisplayedChild();
					Message msg = new Message();
					msg.what = 100;
					mHandler1.sendMessage(msg);
				}
			}
		}
	});

	protected void onResume() {
		super.onResume();
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			ls_login_text.setVisibility(View.GONE);
			ls_login_ll.setVisibility(View.VISIBLE);
			ls_user_nickname.setText(DataManager.getInstance().getUser()
					.getNickname());
			ls_user_point.setText(DataManager.getInstance().getUser()
					.getPoint());
			ll_point.setVisibility(View.GONE);
			ls_nologin_header.setVisibility(View.GONE);
			ls_header.setVisibility(View.VISIBLE);
			String headicon = DataManager.getInstance().getUser().getHeadicon();
			if (!TextUtils.isEmpty(headicon)) {
				ls_header.setImage(headicon, bmp, bmp, "zhuangbei_detail");
			}
		} else {
			ls_login_text.setVisibility(View.VISIBLE);
			ls_login_ll.setVisibility(View.GONE);
			ls_nologin_header.setVisibility(View.VISIBLE);
			ll_point.setVisibility(View.GONE);
			ls_header.setVisibility(View.GONE);
		}
		// UserBean user = DataManager.getInstance().getUser();
		// if (!TextUtils.isEmpty(user.getUser_id())){
		// Boolean hasImage = (Boolean) ls_header.getTag();
		// boolean b = hasImage.booleanValue();
		// if (!b){
		// if (!TextUtils.isEmpty(user.getHeadicon())){
		// ls_header.setImage(user.getHeadicon(),null,null);
		// }
		// }
		// }

	};

	private void setView() {
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		lv_set = (ListView) findViewById(R.id.lv_set);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		ll_point = (LinearLayout) findViewById(R.id.ll_point);
		menu_view = (SlidingMenuView) findViewById(R.id.ls_slidingmenu_view);
		ls_nologin_header = (ImageView) findViewById(R.id.ls_nologin_header);
		ls_login_text = (TextView) findViewById(R.id.ls_login_text);
		sildeshowLayout = (RelativeLayout) findViewById(R.id.sildeshow_layout);
		ls_login_ll = (LinearLayout) findViewById(R.id.ls_login_ll);
		ls_user_nickname = (TextView) findViewById(R.id.ls_user_nickname);
		ls_header = (AsyncLoadImageView) findViewById(R.id.ls_header);
		ls_user_point = (TextView) findViewById(R.id.ls_user_point);
		rl_sildeshow = (RelativeLayout) findViewById(R.id.rl_sildeshow);
		ls_main_mai = (ImageView) findViewById(R.id.ls_main_mai);
		ls_main_xuan = (ImageView) findViewById(R.id.ls_main_xuan);
		ls_main_at = (ImageView) findViewById(R.id.ls_main_at);
		ls_main_shakes = (ImageView) findViewById(R.id.ls_main_shakes);
		LinearLayout.LayoutParams rl_sildeshow_params = (LinearLayout.LayoutParams) rl_sildeshow
				.getLayoutParams();
		LinearLayout.LayoutParams ls_main_mai_params = (LinearLayout.LayoutParams) ls_main_mai
				.getLayoutParams();
		LinearLayout.LayoutParams ls_main_xuan_params = (LinearLayout.LayoutParams) ls_main_xuan
				.getLayoutParams();
		LinearLayout.LayoutParams ls_main_at_params = (LinearLayout.LayoutParams) ls_main_at
				.getLayoutParams();
		LinearLayout.LayoutParams ls_main_shakes_params = (LinearLayout.LayoutParams) ls_main_shakes
				.getLayoutParams();
		ls_main_mai_params.width = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_mai_params.height = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_xuan_params.width = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_xuan_params.height = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_at_params.width = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_at_params.height = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_shakes_params.width = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_shakes_params.height = (StringUtil.getXY(this)[0] - 3 * StringUtil
				.dip2px(this, 10)) / 2;
		ls_main_mai.setLayoutParams(ls_main_mai_params);
		ls_main_xuan.setLayoutParams(ls_main_xuan_params);
		ls_main_at.setLayoutParams(ls_main_at_params);
		ls_main_shakes.setLayoutParams(ls_main_shakes_params);
		ls_main_xuan.setBackgroundDrawable(SelectorUtil.getSelectorDrawable(
				this, R.drawable.ls_main_xuan_icon,
				R.drawable.ls_main_xuan_select_icon,
				R.drawable.ls_main_xuan_select_icon));
		ls_main_mai.setBackgroundDrawable(SelectorUtil.getSelectorDrawable(
				this, R.drawable.ls_main_mai_icon,
				R.drawable.ls_main_mai_select_icon,
				R.drawable.ls_main_mai_select_icon));
		ls_main_at.setBackgroundDrawable(SelectorUtil.getSelectorDrawable(this,
				R.drawable.ls_main_at_icon, R.drawable.ls_main_at_select_icon,
				R.drawable.ls_main_at_select_icon));
		ls_main_shakes.setBackgroundDrawable(SelectorUtil.getSelectorDrawable(
				this, R.drawable.ls_main_shakes_icon,
				R.drawable.ls_main_shakes_select_icon,
				R.drawable.ls_main_shakes_select_icon));
		rl_sildeshow_params.width = StringUtil.getXY(this)[0]
				- StringUtil.dip2px(this, 10);
		rl_sildeshow_params.height = (int) (((float) (StringUtil.getXY(this)[0] - StringUtil
				.dip2px(this, 10))) * 3 / 7);
		rl_sildeshow.setLayoutParams(rl_sildeshow_params);
		adapter = new MenuAdapter();
		lv_set.setAdapter(adapter);

		// 点击监听
		lv_set.setOnItemClickListener(this);
		iv_set.setOnClickListener(this);

		mGestureDetector = new GestureDetector(this);
		// 禁用长按监听
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
	}

	int down = 0;

	int alpha = 0;
	int lx = 0;

	/***
	 * 获取移动距离 移动的距离其实就是layout_Right的宽度
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_right.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_left.getWidth()
							- (StringUtil.getXY(MainActivity.this)[0] / 8);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					// RelativeLayout.LayoutParams layoutParams_1 =
					// (RelativeLayout.LayoutParams) layout_left
					// .getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = menu_view
							.getLayoutParams();
					// 注意： 设置layout_right的宽度。防止被在移动的时候控件被挤压
					layoutParams.width = window_width;
					layout_right.setLayoutParams(layoutParams);

					// 设置layout_left的初始位置.
					if ("loginin".equals(login) || "loginup".equals(login)) {
						layoutParams.rightMargin = -MAX_WIDTH;
						layout_right.setLayoutParams(layoutParams);
						layout_left.setVisibility(View.VISIBLE);
						// layoutParams_1.leftMargin = 0;
						// layout_left.setLayoutParams(layoutParams_1);
					} else {
						// layoutParams_1.leftMargin =
						// -window_width+(StringUtil.getXY(MainActivity.this)[0]/8);
						// layout_left.setLayoutParams(layoutParams_1);
						layout_left.setVisibility(View.INVISIBLE);
					}

					// 注意：设置lv_set的宽度防止被在移动的时候控件被挤压
					layoutParams_2.width = MAX_WIDTH;
					menu_view.setLayoutParams(layoutParams_2);

					Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
							+ window_width);
					hasMeasured = true;

				}
				return true;
			}
		});

	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values[0], -MAX_WIDTH);
				// layoutParams_1.leftMargin =
				// Math.min(layoutParams_1.leftMargin
				// + values[0], 0);
				Log.v(TAG, "layout_right右" + layoutParams.rightMargin
						+ ",layout_left右" + layoutParams_1.leftMargin);
			} else {
				// 左移动
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						- values[0], 0);
				// layoutParams_1.leftMargin =
				// Math.max(layoutParams_1.leftMargin
				// + values[0],
				// -window_width+(StringUtil.getXY(MainActivity.this)[0]/8));
				Log.v(TAG, "layout_right左" + layoutParams.rightMargin
						+ ",layout_left左" + layoutParams_1.leftMargin);
				if (layoutParams.rightMargin == 0) {
					mViewFlipper.startSildeShow();
					layout_left.setVisibility(View.INVISIBLE);
				}
			}
			// layout_left.setLayoutParams(layoutParams_1);
			layout_right.setLayoutParams(layoutParams);

		}

	}

	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		layoutParams.rightMargin -= mScrollX;
		layoutParams_1.rightMargin = window_width + layoutParams.rightMargin;
		if (layoutParams.rightMargin >= 0) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.rightMargin = 0;
			layoutParams_1.rightMargin = window_width;

		} else if (layoutParams.rightMargin <= -MAX_WIDTH) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.rightMargin = -MAX_WIDTH;
			layoutParams_1.rightMargin = window_width - MAX_WIDTH;
		}
		Log.v(TAG, "layoutParams.leftMargin=" + layoutParams.leftMargin
				+ ",layoutParams_1.leftMargin =" + layoutParams_1.leftMargin);

		layout_right.setLayoutParams(layoutParams);
		layout_left.setLayoutParams(layoutParams_1);
	}

	// TODO
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			if (position == 0) {
				Intent intent = new Intent(this, LsUserLikeActivity.class);
				startActivity(intent);
			} else if (position == 1) {
				Intent intent = new Intent(this, LsUserShaiActivity.class);
				startActivity(intent);
			} else if (position == 2) {
				Intent intent = new Intent(this, LsUserAskActivity.class);
				startActivity(intent);
			} else if (position == 3) {
				Intent intent = new Intent(this, LsUserAnswerActivity.class);
				startActivity(intent);
			} else if (position == 4) {
				Intent intent = new Intent(this, LsUserAttentionActivity.class);
				startActivity(intent);
				// TODO
			} else if (position == 5) {
				Intent intent = new Intent(this, LsUserOrderActivity.class);
				startActivity(intent);
			} else if (position == 6) {
				Intent intent = new Intent(this, LsUserMsgActivity.class);
				startActivity(intent);
			} else if (position == 7) {
				Intent intent = new Intent(this, LsUserDraftActivity.class);
				startActivity(intent);
			}/*
			 * else if(position==7){ Intent intent = new Intent(this,
			 * LsSettingActivity.class); startActivity(intent); }
			 */
		} else {
			Intent intent = new Intent(this, LSLoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_set.getId()) {
			if (!InternetUtil.checkNetWorkStatus(this)) {
				Toast.makeText(this, "网络好像有问题", 0).show();
			} else {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
						.getLayoutParams();
				layout_left.setVisibility(View.VISIBLE);
				// 缩回去
				if (layoutParams.rightMargin < -window_width / 2) {
					new AsynMove().execute(-SPEED);
				} else {
					if (mViewFlipper != null) {
						mViewFlipper.stopSildeShow();
					}
					new AsynMove().execute(SPEED);
				}
			}

		} else if (v.getId() == ls_main_mai.getId()) {
			Intent intent = new Intent(this, LsBuyActivity.class);
			startActivity(intent);
		} else if (v.getId() == ls_main_xuan.getId()) {
			Intent intent = new Intent(this, LsXuanActivity1.class);
			startActivity(intent);
		} else if (v.getId() == ls_main_at.getId()) {
			// Intent intent = new Intent(this, CopyOfLsShaiActivity.class);
			// Intent intent = new Intent(this, LsActivity1.class);
			// TODO ------------------------------------------------
			// TODO ------------------------------------------------
			// TODO ------------------------------------------------
			// TODO ------------------------------------------------
			Intent intent = new Intent(this, LsActivity.class);
			// Intent intent = new Intent(this, LsActivityLines.class);
			startActivity(intent);
		} else if (v.getId() == ls_main_shakes.getId()) {
			Intent intent = new Intent(this, LsShakesActivity.class);
			startActivity(intent);
		} else if (v.getId() == ls_nologin_header.getId()) {
			if (DataManager.getInstance().getUser().getUser_id() != null
					&& !"".equals(DataManager.getInstance().getUser()
							.getUser_id())) {
				setUserHead();
			} else {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == ls_header.getId()) {
			if (DataManager.getInstance().getUser().getUser_id() != null
					&& !"".equals(DataManager.getInstance().getUser()
							.getUser_id())) {
				setUserHead();
			} else {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == ls_login_text.getId()) {
			Intent intent = new Intent(this, LSLoginActivity.class);
			startActivity(intent);
		}

	}

	private void setUserHead() {
		postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(MainActivity.this);
							break;
						case 1:
							// 相册
							BitmapUtil
									.doPickPhotoFromGallery(MainActivity.this);
							break;
						}
					}
				});
	}

	Bitmap bitmap;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				Uri photo_uri = data.getData();
				bitmap = BitmapUtil.getThumbnail(photo_uri, MainActivity.this);
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				new GetDataTask(bitmap).execute();
				// ls_nologin_header.setImageBitmap(BitmapUtil.getRCB(bitmap,
				// bitmap.getWidth() / 2));
				// postMessage(POPUP_PROGRESS,getString(R.string.sending));
				// new GetDataTask().execute();
				// new GetDataTask().execute();
				// uploadFile();
				// uploadPicTask(bitmap);
				// BitmapUtil.doCropPhoto(bitmap, MainActivity.this);
				break;
			case C.PICKED_WITH_DATA:
				/*
				 * Bitmap bt = data.getParcelableExtra("data");
				 * postMessage(POPUP_PROGRESS,getString(R.string.sending)); new
				 * GetDataTask(bt).execute();
				 */
				/*
				 * postMessage(POPUP_PROGRESS,
				 * getString(ResourceUtil.getStringId(this,"sending")));
				 * uploadHeadTask(bt);
				 */
				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bitmap = BitmapUtil.getThumbnail(file, MainActivity.this);
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				new GetDataTask(bitmap).execute();
				// ls_nologin_header.setImageBitmap(BitmapUtil.getRCB(bitmap,
				// bitmap.getWidth() / 2));
				// postMessage(POPUP_PROGRESS,getString(R.string.sending));
				// uploadFile();
				// uploadPicTask(bitmap);
				// BitmapUtil.doCropPhoto(bitmap, MainActivity.this);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		Bitmap btt;

		public GetDataTask(Bitmap bt) {
			// TODO Auto-generated constructor stub
			btt = bt;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			String[] netResult = new String[10];
			// photo_data2 = ImageUtil.resizeBitmap(bitmap, 100, 100);
			// String url = FConstant.TASKID_WORK_ADDBOLG_URL;
			// String img_source = String.valueOf(photo_source);
			// String savePath2 = Environment.getExternalStorageDirectory()
			// + "/tmp_icon.jpg";
			String savePath = Environment.getExternalStorageDirectory()
					+ "/temp.jpg";
			ImageUtil.savePic(savePath, btt, 100);
			PostMethod filePost = new PostMethod(
					"http://api.lis99.com/user/savePhoto/");
			try {
				// 组拼post数据的实体
				// image参数
				File file = new File(savePath);
				FilePart f_part = new FilePart("photo", file);
				f_part.setCharSet("utf-8");
				f_part.setContentType("image/jpeg");

				// user_id参数
				StringPart s_part = new StringPart("user_id", DataManager
						.getInstance().getUser().getUser_id(), "utf-8");

				Part[] parts = { s_part, f_part };

				filePost.setRequestEntity(new MultipartRequestEntity(parts,
						filePost.getParams()));
				HttpClient client = new HttpClient();
				client.getHttpConnectionManager().getParams()
						.setConnectionTimeout(60000);

				// 完成文件上的post请求
				client.executeMethod(filePost);
				String receiveMsg = filePost.getResponseBodyAsString();
				netResult[0] = receiveMsg;
				System.out.println("服务器端返回结果:" + receiveMsg);
			} catch (Exception e) {
			} finally {
				filePost.releaseConnection();
			}

			return netResult;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mWaittingDialog.dismiss();

			String result1 = DataManager.getInstance()
					.validateResult(result[0]);
			String img;
			if (result1 != null) {
				if ("OK".equals(result1)) {
					try {
						JSONObject json = new JSONObject(result[0]);
						JSONObject ibjob = json.optJSONObject("data");
						img = ibjob.optString("img");
						ls_nologin_header.setVisibility(View.GONE);
						ls_header.setVisibility(View.VISIBLE);
						// DataManager.getInstance().getUser().setHeadicon(img);
						String headicon = DataManager.getInstance().getUser()
								.getHeadicon();
						if (!TextUtils.isEmpty(headicon)) {
							img = headicon;
						}
						ls_header.setImage(img, null, null, "zhuangbei_detail");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					postMessage(POPUP_TOAST, result);
				}
			}
			postMessage(DISMISS_PROGRESS);
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {

		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/***
	 * 点击松开执行
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 点击的不是layout_left
		if (view != null && view == iv_set) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// 左移动
			if (layoutParams.rightMargin >= 0) {
				new AsynMove().execute(-SPEED);
				lv_set.setSelection(0);// 设置为首位.
			} else {
				// 右移动
				new AsynMove().execute(SPEED);
			}
		} else if (view != null && view == layout_right) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			if (layoutParams.rightMargin < 0) {
				// 说明layout_left处于移动最左端状态，这个时候如果点击layout_left应该直接所以原有状态.(更人性化)
				// 右移动
				new AsynMove().execute(SPEED);
			}
		}

		return true;
	}

	/***
	 * 滑动监听 就是一个点移动到另外一个点. distanceX=后面点x-前面点x，如果大于0，说明后面点在前面点的右边及向右滑动
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			layout_left.setVisibility(View.VISIBLE);
			if (layoutParams.rightMargin == 0) {
				new AsynMove().execute(SPEED);
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private static class ViewHolder {
		ImageView iv_menu_item;
		TextView tv_menu_item;
		Button ls_setting_bt;
		LinearLayout ls_menu_item;
	}

	private class MenuAdapter extends BaseAdapter {

		public MenuAdapter() {
			inflater = LayoutInflater.from(MainActivity.this);
		}

		@Override
		public int getCount() {
			return 9;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		LayoutInflater inflater;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_main_item, null);
				holder = new ViewHolder();
				holder.iv_menu_item = (ImageView) convertView
						.findViewById(R.id.ls_menu_item_iv);
				holder.tv_menu_item = (TextView) convertView
						.findViewById(R.id.ls_menu_item_tv);
				holder.ls_setting_bt = (Button) convertView
						.findViewById(R.id.ls_setting_bt);
				holder.ls_menu_item = (LinearLayout) convertView
						.findViewById(R.id.ls_menu_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 8) { // TODO
				holder.ls_menu_item.setVisibility(View.GONE);
				holder.ls_setting_bt.setVisibility(View.VISIBLE);
				holder.ls_setting_bt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								LsSettingActivity.class);
						startActivity(intent);
					}
				});

			} else {
				holder.ls_menu_item.setVisibility(View.VISIBLE);
				holder.ls_setting_bt.setVisibility(View.GONE);
			}
			if (position == 0) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_xihuan_icon);
				holder.tv_menu_item.setText("喜欢的装备");
			} else if (position == 1) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_shaiguo_icon);
				holder.tv_menu_item.setText("晒过的装备");
			} else if (position == 2) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_tiwen_icon);
				holder.tv_menu_item.setText("我的提问");
			} else if (position == 3) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_huida_icon);
				holder.tv_menu_item.setText("我的回答");
			} else if (position == 4) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_guanzhu_icon);
				holder.tv_menu_item.setText("关注的达人");
			} else if (position == 5) {
				// TODO
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_order_icon);
				holder.tv_menu_item.setText("我的订单");
			} else if (position == 6) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_xiaoxi_icon);
				holder.tv_menu_item.setText("消息提醒");
			} else if (position == 7) {
				holder.iv_menu_item
						.setBackgroundResource(R.drawable.ls_caogao_icon);
				holder.tv_menu_item.setText("草稿箱");
			}
			/*
			 * <Button android:layout_width="80dip"
			 * android:layout_height="25dip"
			 * android:layout_gravity="center_horizontal"
			 * android:layout_marginTop="20dip" android:clickable="true"
			 * android:text="设置" android:textColor="@color/white"
			 * android:background="@drawable/ls_main_setting_bg_selector">
			 * </Button>
			 */
			return convertView;
		}

	}

}
