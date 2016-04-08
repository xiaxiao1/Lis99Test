package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.myactivty.AlbumidBean;
import com.lis99.mobile.myactivty.Comment;
import com.lis99.mobile.myactivty.Equipcate;
import com.lis99.mobile.myactivty.LineInfoBean;
import com.lis99.mobile.myactivty.ShowPic;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.view.MyGridView;
import com.lis99.mobile.view.MyListView;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LsActivityLines extends ActivityPattern1 implements
		IWXAPIEventHandler {

	private MyGridView mygv_jy, mygv_szb;
	private MyListView mlistview;
	MyGridViewAdapter myjyGridViewAdapter;
	MyGirdsViewAdapter myszbGridViewAdapter;
	MyListViewAdapter mylvadapter;

	private Button shaitu, pinglun, dianzan;
	private ImageView iv_back, iv_share, headicon, iv_header, vip, activeDiff;
	private TextView tv_title, equipDiscuss, nickname, start_date, note, man,
			travePlan, activeIntro, activeCost, comment, address, renqizhi,
			catename, comment_count, total_showPic, activeDiffs;
	private String nurl = "http://www.lis99.com/yyyguide.php";

	View leaderPanel,introductionPanel,schedulePanel,equiSuggestion,showPanel,commentPanel;

	private String status2;
	private String data2;
	private String data1;
	private String status1;
	private String status3;
	private String data3;

	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	public static String mAppid;
	private Tencent tencent;
	private int weixin = 1;
	private int id;
	private static final int SHOW_EEROR = 213;
	private static final int SHOW_LINE_INFO = 313;
	private static final int SHOW_ZAN_INFO = 314;
	private static final int SHOW_SHAITUQIAN_INFO = 315;
	private static final int SS = 316;
	private static final int ALBUM_ID_OK = 317;

	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), wh);
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
		mWeiboShareAPI.handleWeiboResponse(intent, wh);
		setIntent(intent);
		api.handleIntent(intent, this);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxl_activity_line);

		StatusUtil.setStatusBar(this);

		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);

		id = this.getIntent().getExtras().getInt("id");

		setview();
		setlistener();
		getLineList(id);
		getdianzanstatus();
	}

	private void getdianzanstatus() {
		String url = C.ACTIVITY_ZANS_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_ZANS_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private void setLike() {
		String url = C.ACTIVITY_ZAN_URL + id;
		Log.i("aa", id + "活动id");
		Task task = new Task(null, url, null, "ACTIVITY_ZAN_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getLoginParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	private void getLineList(int id) {
		String url = C.ACTIVITY_LINE_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_LINE_URL", this);
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
				if (((String) task.getParameter()).equals("ACTIVITY_LINE_URL")) {
					parserLineInfoList(result);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_ZAN_URL")) {
					parseZanList(result);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_SHAITUQIAN_URL")) {
					parserShaituqian(result);
					postMessage(ALBUM_ID_OK);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_ZANS_URL")) {
					parseZanstatus(result);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_ZANSS_URL")) {
					parseZanquxiao(result);
				}
				break;
			}
		default:
			break;
		}
	}

	private void parseZanquxiao(String params) {
		try {
			JSONObject json = new JSONObject(params);
			status3 = json.getString("status");
			data3 = json.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void parseZanstatus(String params) {
		try {
			JSONObject json = new JSONObject(params);
			status1 = json.getString("status");
			data1 = json.getString("data");

			postMessage(SS);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void parseZanList(String params) {
		try {
			JSONObject json = new JSONObject(params);
			status2 = json.getString("status");
			data2 = json.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	List<AlbumidBean> llssss = new ArrayList<AlbumidBean>();

	private void parserShaituqian(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				llssss = (List<AlbumidBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_SHAITUQIAN_INFO);
				postMessage(SHOW_SHAITUQIAN_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	List<LineInfoBean> lineInfo = new ArrayList<LineInfoBean>();

	private void parserLineInfoList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				lineInfo = (List<LineInfoBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_LINE_INFO);
				postMessage(SHOW_LINE_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SS:
			if ("ok".equalsIgnoreCase(status1)) {
				dianzan.setBackgroundResource(R.drawable.dianzan_hou);
			} else {
				dianzan.setBackgroundResource(R.drawable.dianzan_qian);
			}
			break;
		case SHOW_SHAITUQIAN_INFO:
			if (llssss != null && llssss.size() > 0)
				albumid = llssss.get(0).getAlbumid();
			break;
		case SHOW_EEROR:
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_LINE_INFO:
			if (lineInfo != null && lineInfo.size() > 0) {
				String url = lineInfo.get(0).getThumb();
				imageLoader.displayImage(url, iv_header, options);
				tv_title.setText(lineInfo.get(0).getTitle());
				start_date.setText(lineInfo.get(0).getStartTimes());
				String myaddress = lineInfo.get(0).getAddress();
				if (!TextUtils.isEmpty(myaddress)) {
					address.setText(myaddress);
				} else {
					address.setText("未定");
				}
				catename.setText(lineInfo.get(0).getCatename());
				int renqizhis = lineInfo.get(0).getRenqizhi();
				if ("".equals(renqizhis) || renqizhis == 0) {
					renqizhi.setText("人气值\n" + 0);
				} else {
					renqizhi.setText("人气值\n" + renqizhis);
				}
				int diff = lineInfo.get(0).getActiveDiff();
				switch (diff) {
				case 1:
					activeDiffs.setText("轻松");
					activeDiff.setBackgroundResource(R.drawable.easy);
					break;
				case 2:
					activeDiffs.setText("一般");
					activeDiff.setBackgroundResource(R.drawable.soso);
					break;
				case 3:
					activeDiffs.setText("艰难");
					activeDiff.setBackgroundResource(R.drawable.hard);
					break;
				default:
					break;
				}
				int payways = lineInfo.get(0).getActiveCost();
				if (payways == 1) {
					activeCost.setText("免费");
				} else if (payways == 2) {
					activeCost.setText("收费");
				}
				int man2 = lineInfo.get(0).getMan();
				man.setText(man2 + "人");
				String url2 = lineInfo.get(0).getUserInfo().getHeadicon();
				imageLoader.displayImage(url2, headicon, options);
				nickname.setText(lineInfo.get(0).getUserInfo().getNickname());
				notes = lineInfo.get(0).getUserInfo().getNote();
				if (TextUtils.isEmpty(notes)) {
					note.setText("这个人很懒,什么都木有留下 - -");
				} else {
					note.setText(notes);
				}
				travePlan.setText(lineInfo.get(0).getTravePlan());
				activeIntro.setText(lineInfo.get(0).getActiveIntro());
				equipDiscuss.setText(lineInfo.get(0).getEquipDiscuss());
				total_showPic.setText("(" + lineInfo.get(0).getTotal_showPic()
						+ ")");
				int vips = lineInfo.get(0).getVip();
				if (vips == 0) {
					vip.setVisibility(View.INVISIBLE);
				}

				//equipcateInfo = new ArrayList<Equipcate>();
				equipcateInfo = lineInfo.get(0).getEquipcate();
				// TODO
				// Log.i("aa", String.valueOf(lists.size()));
				// Log.i("aa", String.valueOf(list.get(0).getEquipcate()));

				//picInfo = new ArrayList<ShowPic>();
				picInfo = lineInfo.get(0).getShowPic();

				commentInfo = new ArrayList<Comment>();
				commentInfo = lineInfo.get(0).getComment();
				if (commentInfo.size() == 0) {
					comment_count.setTag("");
				}
				comment_count.setText("(" + commentInfo.size() + ")");
				mylvadapter.notifyDataSetChanged();
				myjyGridViewAdapter.notifyDataSetChanged();
				myszbGridViewAdapter.notifyDataSetChanged();
			}
			break;
		case ALBUM_ID_OK:
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityLoadUp1.class);
			Bundle b = new Bundle();
			b.putInt("albumid", albumid);
			b.putInt("id", id);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
		return true;
	}

	private void setlistener() {
		iv_back.setOnClickListener(this);
		//activeIntro.setOnClickListener(this);
		//travePlan.setOnClickListener(this);
		//equipDiscuss.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		//headicon.setOnClickListener(this);
		shaitu.setOnClickListener(this);
		pinglun.setOnClickListener(this);
		dianzan.setOnClickListener(this);

		leaderPanel.setOnClickListener(this);
		introductionPanel.setOnClickListener(this);
		equiSuggestion.setOnClickListener(this);
		schedulePanel.setOnClickListener(this);
		showPanel.setOnClickListener(this);
		commentPanel.setOnClickListener(this);
	}

	private void setview() {
		shaitu = (Button) findViewById(R.id.shaitu);
		pinglun = (Button) findViewById(R.id.pinglun);
		dianzan = (Button) findViewById(R.id.dianzan);
		vip = (ImageView) findViewById(R.id.vip);
		activeDiff = (ImageView) findViewById(R.id.activeDiff);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		headicon = (ImageView) findViewById(R.id.headicon);
		iv_header = (ImageView) findViewById(R.id.iv_header_pic);
		tv_title = (TextView) findViewById(R.id.tv_title);
		start_date = (TextView) findViewById(R.id.start_date);
		address = (TextView) findViewById(R.id.address);
		catename = (TextView) findViewById(R.id.catename);
		comment_count = (TextView) findViewById(R.id.comment_count);
		comment = (TextView) findViewById(R.id.comment);
		renqizhi = (TextView) findViewById(R.id.renqizhi);
		activeDiffs = (TextView) findViewById(R.id.activeDiffs);
		activeCost = (TextView) findViewById(R.id.activeCost);
		nickname = (TextView) findViewById(R.id.nickname);
		man = (TextView) findViewById(R.id.man);
		note = (TextView) findViewById(R.id.note);
		activeIntro = (TextView) findViewById(R.id.activeIntro);
		travePlan = (TextView) findViewById(R.id.travePlan);
		equipDiscuss = (TextView) findViewById(R.id.equipDiscuss);
		total_showPic = (TextView) findViewById(R.id.total_showPic);
		mlistview = (MyListView) findViewById(R.id.mlistview);
		mygv_jy = (MyGridView) findViewById(R.id.mygv_jy);
		mygv_szb = (MyGridView) findViewById(R.id.mygv_szb);

		mylvadapter = new MyListViewAdapter();
		myjyGridViewAdapter = new MyGridViewAdapter();
		myszbGridViewAdapter = new MyGirdsViewAdapter();
		mygv_jy.setAdapter(myjyGridViewAdapter);
		mygv_szb.setFocusable(false);
		mygv_szb.setAdapter(myszbGridViewAdapter);
		mlistview.setFocusable(false);
		mlistview.setAdapter(mylvadapter);

		leaderPanel = findViewById(R.id.leaderPanel);
		introductionPanel = findViewById(R.id.introductionPanel);
		equiSuggestion = findViewById(R.id.equiSuggestion);
		schedulePanel = findViewById(R.id.schedulePanel);
		showPanel = findViewById(R.id.showPanel);
		commentPanel = findViewById(R.id.commentPanel);

		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long itemId) {
				Intent intent = new Intent(LsActivityLines.this,
						LsActivityComment.class);
				Bundle b = new Bundle();
				int id = lineInfo.get(0).getId();
				b.putInt("id", id);
				intent.putExtras(b);
				startActivity(intent);

			}
		});

		mygv_szb.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsActivityLines.this,
						LsLineGalleryActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("photos", lineInfo.get(0).getShowPic());
				intent.putExtras(b);
				startActivity(intent);

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == iv_share.getId()) {
			showShareList();
		} else if (v.getId() == introductionPanel.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityInfo.class);
			Bundle b = new Bundle();
			String info = lineInfo.get(0).getActiveIntro();
			b.putString("info", info);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == equiSuggestion.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityEquip.class);
			Bundle b = new Bundle();
			String info = lineInfo.get(0).getEquipDiscuss();
			b.putString("info", info);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == schedulePanel.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityPlan.class);
			Bundle b = new Bundle();
			String plan = lineInfo.get(0).getTravePlan();
			b.putString("plan", plan);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == leaderPanel.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityElect.class);
			Bundle b = new Bundle();
			int uid = lineInfo.get(0).getUid();
			b.putInt("uid", uid);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == commentPanel.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityComment.class);
			Bundle b = new Bundle();
			int id = lineInfo.get(0).getId();
			b.putInt("id", id);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == pinglun.getId()) {
			Intent intent = new Intent(LsActivityLines.this,
					LsActivityComment.class);
			Bundle b = new Bundle();
			int id = lineInfo.get(0).getId();
			b.putInt("id", id);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == dianzan.getId()) {
			// TODO
			// Log.i("aa", status1 +data1+status2 + status3+
			// "wo qu haishi  buqu  ne");
			if (!TextUtils.isEmpty(DataManager.getInstance().getUser()
					.getUser_id())) {
				if ("OK".equalsIgnoreCase(status1)) {
					getquzan();
					status1 = "no";
					dianzan.setBackgroundResource(R.drawable.dianzan_qian);
				} else {
					setLike();
					status1 = "ok";
					dianzan.setBackgroundResource(R.drawable.dianzan_hou);
				}
			} else {
				Intent intent = new Intent(LsActivityLines.this,
						LSLoginActivity.class);
				startActivity(intent);
			}
		} else if (v.getId() == shaitu.getId()) {
			// 先发个请求
			String user_id = DataManager.getInstance().getUser().getUser_id();
			if (TextUtils.isEmpty(user_id)) {
				Intent intent = new Intent(LsActivityLines.this,
						LSLoginActivity.class);
				startActivity(intent);
			} else {
				getShaituqian();
			}
		} else if(v.getId() == showPanel.getId()){
			Intent intent = new Intent(LsActivityLines.this,
					LsLineGalleryActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("photos", lineInfo.get(0).getShowPic());
			intent.putExtras(b);
			startActivity(intent);
		}
	}

	private void getquzan() {
		String url = C.ACTIVITY_ZANSS_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_ZANSS_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private void getShaituqian() {
		String url = C.ACTIVITY_SHAITUQIAN_URL;
		Task task = new Task(null, url, null, "ACTIVITY_SHAITUQIAN_URL", this);
		task.setPostData(getSmParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getSmParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {
					private Bitmap bp;
					private String shareUrl = "http://m.lis99.com/activity/getTow";
					private String SinaWeiboShareUrl = "http://www.lis99.com/huodong/ruleDetail/";

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 新浪微博分享
							String shareText = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ SinaWeiboShareUrl;
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share_weibo);
							LsWeiboSina.getInstance(LsActivityLines.this)
									.shares(shareText, bp, 0);
							break;
						case 1:
							// 微信好友分享
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share);
							String shareWx1Text = shareUrl;
							String title1 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							String desc1 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							LsWeiboWeixin
									.getInstance(LsActivityLines.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityLines.this);
							LsWeiboWeixin.getInstance(LsActivityLines.this)
									.share(shareWx1Text, title1, desc1, bp,
											SendMessageToWX.Req.WXSceneSession);
							weixin = 2;
							break;
						case 2:
							// 微信朋友圈分享
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share);
							weixin = 3;
							String shareWx2Text = shareUrl;
							String title2 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							String desc2 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							LsWeiboWeixin
									.getInstance(LsActivityLines.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityLines.this);
							LsWeiboWeixin
									.getInstance(LsActivityLines.this)
									.share(shareWx2Text, title2, desc2, bp,
											SendMessageToWX.Req.WXSceneTimeline);

							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
									"摇一摇");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									nurl);
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							tencent.shareToQzone(LsActivityLines.this, params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													LsActivityLines.this,
													"onCancel: ");
										}

										@Override
										public void onComplete(Object response) {
											Util2.toastMessage(
													LsActivityLines.this,
													"onComplete: ");
										}

										@Override
										public void onError(UiError e) {
											Util2.toastMessage(
													LsActivityLines.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}
									});
							break;

						case 4:
							String shareWx4Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ nurl;
							LsWeiboTencent.getInstance(LsActivityLines.this)
									.share(shareWx4Text, bp);
							break;
						}
					}
				});
	}

	private Response wh = new Response() {
		@Override
		public void onResponse(BaseResponse arg0) {
			switch (arg0.errCode) {
			case WBConstants.ErrorCode.ERR_OK:
				Toast.makeText(LsActivityLines.this, "分享成功", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				Toast.makeText(LsActivityLines.this, "取消分享", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				Toast.makeText(LsActivityLines.this,
						"分享失败" + "Error Message: " + arg0.errMsg,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private List<Equipcate> equipcateInfo;
	private List<Comment> commentInfo;
	private List<ShowPic> picInfo;
	private String notes;
	private int albumid;

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

	static class ViewHolders {
		ImageView image;
	}

	class MyGirdsViewAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyGirdsViewAdapter() {
			inflater = LayoutInflater.from(LsActivityLines.this);
		}

		@Override
		public int getCount() {
			if (null != picInfo) {
				return picInfo.size() > 6 ? 6 : picInfo.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return lineInfo.get(0).getShowPic().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolders viewHolder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.my_gridsview_item, null);
				viewHolder = new ViewHolders();
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.item_iv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolders) convertView.getTag();
			}
			imageLoader.displayImage(lineInfo.get(0).getShowPic().get(position)
					.getImage_url(), viewHolder.image, options);
			return convertView;
		}
	}

	class ViewHolder3 {
		ImageView item_comment_heads;
		TextView item_comment_nickname, item_comment, item_comment_date;
	}

	class MyListViewAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyListViewAdapter() {
			inflater = LayoutInflater.from(LsActivityLines.this);
		}

		@Override
		public int getCount() {
			if (null != commentInfo) {
				return commentInfo.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return commentInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder3 holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.ls_activity_comment_item, null);
				holder = new ViewHolder3();
				holder.item_comment_heads = (ImageView) convertView
						.findViewById(R.id.item_comment_heads);
				holder.item_comment_nickname = (TextView) convertView
						.findViewById(R.id.item_comment_nickname);
				holder.item_comment_date = (TextView) convertView
						.findViewById(R.id.item_comment_date);
				holder.item_comment = (TextView) convertView
						.findViewById(R.id.item_comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder3) convertView.getTag();
			}
			imageLoader.displayImage(commentInfo.get(position).getHeadicon(),
					holder.item_comment_heads, options);
			holder.item_comment_nickname.setText(commentInfo.get(position)
					.getNickname());
			holder.item_comment_date.setText(commentInfo.get(position)
					.getCreatetime());
			holder.item_comment.setText(commentInfo.get(position).getComment());
			return convertView;
		}
	}

	class MyGridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyGridViewAdapter() {
			inflater = (LayoutInflater) LsActivityLines.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if (null != equipcateInfo) {
				return equipcateInfo.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return equipcateInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return new View(LsActivityLines.this);
			// ViewHolder viewHolder;
			// if (convertView == null) {
			// convertView = this.inflater.inflate(R.layout.my_gridview_item,
			// null);
			// viewHolder = new ViewHolder();
			// viewHolder.title = (TextView) convertView
			// .findViewById(R.id.item_title);
			// viewHolder.image = (ImageView) convertView
			// .findViewById(R.id.item_iv);
			// convertView.setTag(viewHolder);
			// } else {
			// viewHolder = (ViewHolder) convertView.getTag();
			// }
			// viewHolder.title.setText(lists.get(position).getCatname());
			// imageLoader.displayImage(lists.get(position).getThumb(),
			// viewHolder.image, options);
			// return convertView;
		}
	}

	class ViewHolder {
		public ImageView image;
		public TextView title;
	}
}
