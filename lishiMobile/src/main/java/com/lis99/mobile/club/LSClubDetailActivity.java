package com.lis99.mobile.club;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubDitalAdapter;
import com.lis99.mobile.club.model.ClubDetailHead;
import com.lis99.mobile.club.model.ClubDetailList;
import com.lis99.mobile.club.model.ClubDetailList.Topiclist;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.club.topicstrimg.LSTopicStringImageActivity;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSClubDetailActivity extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{

	ListView listView;
	LSClubDitalAdapter adapter;

	int clubID;
	View headerView;
	RoundedImageView headerImageView;
	TextView nameView;
	TextView memberNumView;
	TextView descView;

	boolean needRefresh = false;
	boolean loginBeforePause = true;

	View allPanel;
	TextView allView;
	View allLine;

	View eventPanel;
	TextView eventView;
	View eventLine;

	View topPanel;

	View infoPanel;

	int type = -1;
	int offset = 0;

	View cicleView;
	View joinButton;
	View topGapView;
	View titlePanel;

	TextView leaderTitleView;
	View rightArrow;



	private final static int SHOW_CLUB = 1001;
	private final static int SHOW_MORE_TOPIC = 1003;
	private final static int NO_MORE_TOPIC = 1004;
	private final static int SHOW_ADDBUTTON = 1002;

	public final static String CLUB_TOPIC_CHANGE = "com.lis99.topicChange";

	private LocalBroadcastManager lbm;

	DisplayImageOptions options;

	int topPanelHeight;
	boolean topPanelVisible = true;

	HorizontalScrollView leaderPanel;
	HorizontalScrollView tagPanel;
	View leaderAllPanel;
	View sep2;
	View sep3;

	//=============2.3版本=============
	private Map<Integer,Integer> clubIcons;

//	private MyScrollView scrollview;

	//标题栏
//	private ImageView iv_title_bg;
//	private ImageView titleRightImage;
	//默认给一个值， 防止初始化为0的时候， 会显示出来Tab
	private float HeadAdHeight = 1;
//	private TextView title;
//	========

	private ClubDetailList clubAll;
	private Page page;

	private View headViewMain;
	//ListView 第一个可见item
	private int visibleFirst;
	private View buttonPanel;


	private void buildOptions() {
		options = ImageUtil.getImageOptionClubIcon();
	}

	private PullToRefreshView refreshView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsclub_detail);
		initViews();
		clubID = getIntent().getIntExtra("clubID", 0);
		setTitle("帖子列表");

		buildOptions();


		clubIcons = new HashMap<Integer, Integer>();
		clubIcons.put(48, R.drawable.club_new_bg_48);
		clubIcons.put(284, R.drawable.club_new_bg_284);
		clubIcons.put(285, R.drawable.club_new_bg_285);
		clubIcons.put(342, R.drawable.club_new_bg_337);
		clubIcons.put(339, R.drawable.club_new_bg_339);
		clubIcons.put(340, R.drawable.club_new_bg_340);
		clubIcons.put(343, R.drawable.club_new_bg_343);
		clubIcons.put(349, R.drawable.club_new_bg_349);

		IntentFilter filter = new IntentFilter();
		filter.addAction(CLUB_TOPIC_CHANGE);
		lbm = LocalBroadcastManager.getInstance(this);
		lbm.registerReceiver(localReceiver, filter);
		clubHead = new ClubDetailHead();

		clubAll = new ClubDetailList();
		page = new Page();
		loadClubInfoFirst();

	}

	@Override
	protected void initViews() {
		super.initViews();
		buttonPanel = findViewById(R.id.buttonPanel);
		//setRightView(R.drawable.club_join);
		//titleRightImage.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listView);

		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		headViewMain = View.inflate(activity, R.layout.club_new_header_view, null);

		headerView = headViewMain;

		tagPanel = (HorizontalScrollView) headerView.findViewById(R.id.tagPanel);
		leaderPanel = (HorizontalScrollView) headerView.findViewById(R.id.leaderPanel);
		leaderAllPanel = headerView.findViewById(R.id.leaderAllPanel);
		sep2 = headerView.findViewById(R.id.sepView2);
		sep3 = headerView.findViewById(R.id.sepView3);
		cicleView = headerView.findViewById(R.id.offical_cicle);
		joinButton = headerView.findViewById(R.id.btn_join);
		infoPanel = headerView.findViewById(R.id.infoPanel);
		topGapView = headerView.findViewById(R.id.topGapView);
		titlePanel = headerView.findViewById(R.id.titlePanel);

		leaderTitleView = (TextView) headerView.findViewById(R.id.leaderTitleView);
		rightArrow = headerView.findViewById(R.id.rightArrow);

		ViewTreeObserver vto1 = headerView.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				getHeadAdHeight();

				ViewTreeObserver obs = headerView.getViewTreeObserver();
				obs.removeOnGlobalLayoutListener(this);
			}
		});

		infoPanel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LSClubDetailActivity.this, LSClubBriefActivity.class);
				intent.putExtra("clubID", clubID);
//				startActivity(intent);
				startActivityForResult(intent, 999);
			}
		});


		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				String activity_code = null, category = null;
				int topicId = -1;

//				Topiclist item = null;
//				item = (Topiclist) adapter.getItem(position - 1);
//				if ( item == null ) return;

				if ( adapter == null ) return;
				Object o = adapter.getItem(position - 1);
				if ( o == null ) return;

				if ( o instanceof ClubDetailList.Toptopiclist )
				{
					ClubDetailList.Toptopiclist item = (ClubDetailList.Toptopiclist) o;
					activity_code = item.activity_code;
					category = item.category;
					topicId = item.id;
				}
				else
				{
					ClubDetailList.Topiclist item = (Topiclist) o;
					activity_code = item.activity_code;
					category = item.category;
					topicId = item.id;
				}

				if ( topicId == -1 )
				{
					Common.toast("帖子ID没有获取到");
					return;
				}


//				新版活动帖
				if ( !TextUtils.isEmpty(activity_code) && "4".equals(category) )
				{
//					Intent intent = new Intent(activity, LSClubTopicActiveOffLine.class);
//					intent.putExtra("topicID", topicId);
//					startActivity(intent);

					Common.goTopic(activity, 4, topicId);

					return;
				}
//				线上活动帖
				else if ( "2".equals(category))
				{
					Intent intent = new Intent(LSClubDetailActivity.this, LSClubTopicNewActivity.class);
					intent.putExtra("topicID", topicId);
					startActivityForResult(intent, 998);
					return;
				}
//				新版话题贴
				else if ( "3".equals(category))
				{
					Intent intent = new Intent(activity, LSClubNewTopicListMain.class);
					intent.putExtra("TOPICID", "" + topicId);
					startActivity(intent);
					return;
				}

				else if ( "0".equals(category) || "1".equals(category) )
				{
					Intent intent = new Intent(LSClubDetailActivity.this, LSClubTopicActivity.class);
					intent.putExtra("topicID", topicId);
					startActivityForResult(intent, 998);
					return;
				}


			}
		});

		headerImageView = (RoundedImageView)headViewMain.findViewById(R.id.roundedImageView1);

		nameView = (TextView) headViewMain.findViewById(R.id.titleView);
		descView = (TextView) headViewMain.findViewById(R.id.descView);
		memberNumView = (TextView) headViewMain.findViewById(R.id.numberView);


		allPanel = headViewMain.findViewById(R.id.allPanel);
		allView = (TextView) headViewMain.findViewById(R.id.allView);
		allLine = headViewMain.findViewById(R.id.allLine);

		eventPanel = headViewMain.findViewById(R.id.eventPanel);
		eventView = (TextView) headViewMain.findViewById(R.id.eventView);
		eventLine = headViewMain.findViewById(R.id.eventLine);

		allPanel.setOnClickListener(this);
		eventPanel.setOnClickListener(this);

		topPanel = headViewMain.findViewById(R.id.topPanel);



		ViewTreeObserver vto = topPanel.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				topPanelHeight = topPanel.getHeight();
				ViewTreeObserver obs = topPanel.getViewTreeObserver();
				obs.removeOnGlobalLayoutListener(this);
			}
		});


		listView.addHeaderView(headViewMain);

	}

	@Override
	protected void onDestroy() {
		lbm.unregisterReceiver(localReceiver);
		super.onDestroy();
	}

	private BroadcastReceiver localReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			needRefresh = true;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!loginBeforePause && !TextUtils.isEmpty(userID)) {
			needRefresh = true;
		}
		if (needRefresh) {
			needRefresh = false;
			loadClubInfo();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!TextUtils.isEmpty(userID)) {
			loginBeforePause = true;
		} else {
			loginBeforePause = false;
		}
	}


	private void loadClubInfoFirst() {
		offset = 0;
		String userID = DataManager.getInstance().getUser().getUser_id();

		String url = C.CLUB_DETAIL_HEAD + clubID;
		if (userID != null && !"".equals(userID)) {
			url += "/"+userID;
		}
		MyRequestManager.getInstance().requestGet(url, clubHead, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				clubHead = (ClubDetailHead) mTask.getResultModel();
				initClubHead();
				setTitle(clubHead.title);
				if (clubHead.ui_levels == 3) {
//					adapter = new LSClubDitalAdapter(LSClubDetailActivity.this, new
//							ArrayList<Topiclist>(), false);
//					adapter.ui_level = clubHead.ui_levels;
//					listView.setAdapter(adapter);
					getAllList();
				} else {
					topPanel.setVisibility(View.GONE);
					buttonPanel.setVisibility(View.GONE);
//					adapter = new LSClubDitalAdapter(LSClubDetailActivity.this, new
//							ArrayList<Topiclist>(), true);
//					adapter.ui_level = clubHead.ui_levels;
//					listView.setAdapter(adapter);
					getActiveList();
				}
			}
		});
	}

	private void loadClubInfo() {
		MyHttp();
	}


	private ClubDetailHead clubHead;
	private void MyHttp ()
	{
		offset = 0;
		String userID = DataManager.getInstance().getUser().getUser_id();

		String url = C.CLUB_DETAIL_HEAD + clubID;
		if (userID != null && !"".equals(userID)) {
			url += "/"+userID;
		}
		MyRequestManager.getInstance().requestGet(url, clubHead, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				clubHead = (ClubDetailHead) mTask.getResultModel();
				initClubHead();

			}
		});
	}

	@Override
	protected void rightAction() {
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userID)) {
			Intent intent = new Intent(this, LSLoginActivity.class);
			startActivity(intent);
			return;
		}
//		Intent intent = new Intent(LSClubDetailActivity.this, LSClubPublish2Activity.class);
		Intent intent = new Intent(LSClubDetailActivity.this, LSTopicStringImageActivity.class);
		intent.putExtra("clubID", clubID);
		intent.putExtra("clubName", clubHead.title);
//		intent.putExtra("CURRENTCLUB", true);
//			startActivity(intent);
		startActivityForResult(intent, 998);
	}

	private void initClubHead ()
	{
		if (clubHead.ui_levels == 3) {
			int clubID = Integer.parseInt(clubHead.club_id);
			int resID = R.drawable.club_new_bg_default;
			if (clubIcons.containsKey(clubID)) {
				resID = clubIcons.get(clubID);
			}
			setRightView(R.drawable.new_topic_icon);

			infoPanel.setBackgroundResource(resID);

			ClubDetailHead.Leader leader = new ClubDetailHead.Leader();
			leader.user_id = clubHead.inituserid;
			leader.nickname = clubHead.initnickname;
			leader.headicon = clubHead.initheadicon;
			if (clubHead.moderator_list == null) {
				ArrayList<ClubDetailHead.Leader> leaders = new ArrayList<ClubDetailHead.Leader>();
				leaders.add(leader);
				clubHead.moderator_list = leaders;
			} else {
				clubHead.moderator_list.add(0, leader);
			}

			if (clubHead.moderator_list == null || clubHead.moderator_list.size() == 0) {
				leaderAllPanel.setVisibility(View.GONE);
				sep2.setVisibility(View.GONE);
			} else {
				leaderAllPanel.setVisibility(View.VISIBLE);
				sep2.setVisibility(View.VISIBLE);
				createLeaderHeaders(clubHead.moderator_list);
			}

		} else {
			topGapView.setVisibility(View.VISIBLE);
			topPanel.setVisibility(View.GONE);
			titlePanel.setVisibility(View.VISIBLE);
			sep3.setVisibility(View.GONE);
			cicleView.setVisibility(View.INVISIBLE);
			joinButton.setVisibility(View.VISIBLE);
			rightArrow.setVisibility(View.GONE);
			nameView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			descView.setTextColor(Color.rgb(0x99, 0x99, 0x99));
			memberNumView.setVisibility(View.GONE);
			setTitleRight(true);
			infoPanel.setBackgroundColor(Color.WHITE);
			leaderTitleView.setText("领队");

			ClubDetailHead.Leader leader = new ClubDetailHead.Leader();
			leader.user_id = clubHead.inituserid;
			leader.nickname = clubHead.initnickname;
			leader.headicon = clubHead.initheadicon;
			if (clubHead.adminlist == null) {
				ArrayList<ClubDetailHead.Leader> leaders = new ArrayList<ClubDetailHead.Leader>();
				leaders.add(leader);
				clubHead.adminlist = leaders;
			} else {
				clubHead.adminlist.add(0, leader);
			}

			if (clubHead.adminlist == null || clubHead.adminlist.size() == 0) {
				leaderAllPanel.setVisibility(View.GONE);
				sep2.setVisibility(View.GONE);
			} else {
				leaderAllPanel.setVisibility(View.VISIBLE);
				sep2.setVisibility(View.VISIBLE);
				createLeaderHeaders(clubHead.adminlist);
			}
		}

		nameView.setText(clubHead.title);
		memberNumView.setText("" + clubHead.members);
		descView.setText(clubHead.descript);




		if ( !TextUtils.isEmpty(clubHead.images))
			ImageLoader.getInstance().displayImage(clubHead.images,
					headerImageView, options);




		if (clubHead.tags == null || clubHead.tags.size() == 0) {
			tagPanel.setVisibility(View.GONE);
			sep3.setVisibility(View.GONE);
		} else {
			tagPanel.setVisibility(View.VISIBLE);
			sep3.setVisibility(View.VISIBLE);
			createTags();
		}


	}


	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
			case IEvent.IO:
				if (task.getData() instanceof byte[]) {
					result = new String((byte[]) task.getData());
					if (((String) task.getParameter()).equals(C.CLUB_GET_INFO)) {
//					parserClubInfo(result);
					} else if (((String) task.getParameter()).equals(C.CLUB_JOIN)) {
						parserJoinClubInfo(result);
					} else if (((String) task.getParameter()).equals(C.CLUB_QUIT)) {
						parserQuitClubInfo(result);
					} else if (((String) task.getParameter()).equals("moreTopic")) {
//					parserMoreClubInfo(result);
					}
				}
				break;
			default:
				break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserJoinClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			postMessage(ActivityPattern1.POPUP_TOAST, "加入成功");
			clubHead.is_jion = "4";
			postMessage(SHOW_ADDBUTTON);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	private void parserQuitClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			postMessage(ActivityPattern1.POPUP_TOAST, "退出成功");
			clubHead.is_jion = "-1";
			postMessage(SHOW_ADDBUTTON);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	private void loadMore() {
		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_GET_INFO + "?club_id=" + clubID + "&category=" + type + "&offset=" + offset;
		if (userID != null && !"".equals(userID)) {
			url += "&user_id=" + userID;
		}

		Task task = new Task(null, url, null, "moreTopic", this);
		publishTask(task, IEvent.IO);

	}


	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_CLUB) {
			return true;
		} else if (msg.what == SHOW_MORE_TOPIC) {
			adapter.notifyDataSetChanged();
			return true;
		} else if (msg.what == NO_MORE_TOPIC){

		} else if (msg.what == SHOW_ADDBUTTON) {
			setTitleRight(isBg);
			return true;
		}
		return super.handleMessage(msg);
	}

	private void joinClub(String userID){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("club_id", clubID);
		params.put("user_id", userID);

		Task task = new Task(null, C.CLUB_JOIN, C.HTTP_POST, C.CLUB_JOIN, this);
		task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}

	private void quitClub(final String userID) {

		postMessage(POPUP_ALERT, null, "确定要退出俱乐部吗？", true, "确定",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						doQuit(userID);
					}
				}, true, "取消", null);

	}

	private void doQuit(String userID){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("club_id", clubID);
		params.put("user_id", userID);

		Task task = new Task(null, C.CLUB_QUIT, C.HTTP_POST,
				C.CLUB_QUIT, LSClubDetailActivity.this);
		task.setPostData(RequestParamUtil.getInstance(LSClubDetailActivity.this)
				.getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == allPanel.getId() ){
			if (type == -1) {
				return;
			}
			type = -1;
			//这跟线在线路活动 里是没有的
			allView.setTextColor(getResources().getColor(R.color.text_color_green));

			allLine.setVisibility(View.VISIBLE);

			eventLine.setVisibility(View.GONE);
			eventView.setTextColor(getResources().getColor(R.color.bantouming));
//			loadClubInfo();
			cleanList();
			if (clubHead.ui_levels == 3) {
				getAllList();
			} else {
				getActiveList();
			}

		}else if(view.getId() == eventPanel.getId()  ){
			if (type == 1) {
				return;
			}
			type = 1;
			//这跟线消失
			eventView.setTextColor(getResources().getColor(R.color.text_color_green));
			eventLine.setVisibility(View.VISIBLE);
			allLine.setVisibility(View.GONE);
			allView.setTextColor(getResources().getColor(R.color.bantouming));
//			loadClubInfo();
			cleanList();
			if (clubHead.ui_levels == 3) {
				getActiveList();
			} else {
				getAllList();
			}

		} else if (view.getId() == R.id.publishButton || view.getId() == R.id.titleRightImage) {
			String userID = DataManager.getInstance().getUser().getUser_id();
			if (TextUtils.isEmpty(userID)) {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
				return;
			}
			Intent intent = new Intent(LSClubDetailActivity.this, LSTopicStringImageActivity.class);
			intent.putExtra("clubName", clubHead.title);
			intent.putExtra("clubID", clubID);
//			intent.putExtra("CURRENTCLUB", true);
//			startActivity(intent);
			startActivityForResult(intent, 998);
			return;
		} else if (view.getId() == R.id.btn_join  ) {
			if (TextUtils.isEmpty(clubHead.is_jion)) {
				return;
			}
			String userID = DataManager.getInstance().getUser().getUser_id();
			if (TextUtils.isEmpty(userID)) {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
				return;
			}

			if ("-1".equals(clubHead.is_jion)) {
				joinClub(userID);
			} else if ("4".equals(clubHead.is_jion)){
				quitClub(userID);
			} else {
				postMessage(POPUP_TOAST, "您是俱乐部管理员，不能退出俱乐部");
			}
			return;
		}
		super.onClick(view);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		refreshView.onFooterRefreshComplete();
		if ( type == -1 )
		{
			getAllList();
		}
		else
		{
			getActiveList();
		}
//		if (club.getTopiclist().size() >= club.getTotalNum()) {
//			postMessage(POPUP_TOAST, "没有更多帖子");
//		} else {
//			loadMore();
//		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshView.onHeaderRefreshComplete();
//		offset = 0;

		cleanList();
		loadClubInfo();
		if ( type == -1 )
		{
			if (clubHead.ui_levels == 3) {
				getAllList();
			} else {
				getActiveList();
			}
		}
		else
		{
			if (clubHead.ui_levels == 3) {
				getActiveList();
			} else {
				getAllList();
			}
		}
	}

	private void getHeadAdHeight ()
	{
		int titleHeight = iv_title_bg.getHeight();
		HeadAdHeight = headerView.getHeight() - titleHeight;
	}
	private boolean isBg = false;


	//设置title右边按钮
	private void setTitleRight ( boolean isBg )
	{
		if (clubHead == null ) return;
		if ( "-1".equals(clubHead.is_jion))
		{
//			joinButton.setEnabled(true);
			joinButton.setBackgroundResource(R.drawable.club_join_new);
		}
		else
		{
//			joinButton.setEnabled(false);
			joinButton.setBackgroundResource(R.drawable.club_joined_new);
		}
	}


	//=========
	//获取所有列表
	public void getAllList ()
	{
		//当前页数>总页数 returnl
		if ( page.pageNo >= page.pageSize )
		{
			Common.toast("没有更多帖子");
			return;
		}

		String url = C.CLUB_DETAIL_LIST + clubID + "/" + "0" + "?page=" + page.getPageNo();

		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!TextUtils.isEmpty(userID)) {
			url += "&user_id=" + userID;
		}

		MyRequestManager.getInstance().requestGet(url, clubAll, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				page.pageNo += 1;
				clubAll = (ClubDetailList) mTask.getResultModel();
				if (clubAll.topiclist == null) {
					return;
				}
				if (adapter == null) {
					ArrayList<Object> infoList = new ArrayList<Object>();
					if (clubAll.toptopiclist != null && clubAll.toptopiclist.size() != 0) {
						infoList.addAll(clubAll.toptopiclist);
					}

					infoList.addAll(clubAll.topiclist);

					page.pageSize = clubAll.totpage;
					adapter = new LSClubDitalAdapter(activity, infoList, false);
					if (clubHead != null) {
						adapter.ui_level = clubHead.ui_levels;
					}
					listView.setAdapter(adapter);
					loadClubInfo();
					return;
				}
				adapter.addList(clubAll.topiclist);
			}
		});
	}
	//下拉刷新用
	public void cleanList ()
	{
		page = new Page();
//		pageactive = new Page();
		listView.setAdapter(null);
		adapter = null;
//		adapterActive = null;
	}

	//	获取线路活动列表
	public void getActiveList ()
	{
		if ( page.pageNo >= page.pageSize )
		{
			Common.toast("没有更多帖子");
			return;
		}
		MyRequestManager.getInstance().requestGet(C.CLUB_DETAIL_LIST + clubID + "/" + "1" +
				"?page=" + page.getPageNo(), clubAll, new CallBack() {

			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				page.pageNo += 1;
				clubAll = (ClubDetailList) mTask.getResultModel();
				if (clubAll.topiclist == null) {
					return;
				}
				if (adapter == null) {

					ArrayList<Object> infoList = new ArrayList<Object>();
					if (clubAll.toptopiclist != null && clubAll.toptopiclist.size() != 0) {
						infoList.addAll(clubAll.toptopiclist);
					}

					infoList.addAll(clubAll.topiclist);


					page.pageSize = clubAll.totpage;
					adapter = new LSClubDitalAdapter(activity, infoList, true);
					if (clubHead != null) {
						adapter.ui_level = clubHead.ui_levels;
					}
					listView.setAdapter(adapter);
					return;
				}
				adapter.addList(clubAll.topiclist);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		//简介，操作后， 更新是否加入俱乐部界面
		// TODO Auto-generated method stub
		if ( resultCode == RESULT_OK && requestCode == 999 )
		{
			boolean b = data.getBooleanExtra("join", false);
			if (!b) {
				if ( clubHead != null )
				{
					clubHead.is_jion = "-1";
				}
				setRightView(R.drawable.club_join);
			} else{
				if ( clubHead != null )
				{
					clubHead.is_jion = "4";
				}
				setRightView(R.drawable.club_joined);
			}
		}
		//删帖、置顶， 请求刷新, 发帖
		else if ( resultCode == RESULT_OK  && requestCode == 998 )
		{
			cleanList();
//			loadClubInfo();
			if ( type == -1 )
			{
				if (clubHead.ui_levels == 3) {
					getAllList();
				} else {
					getActiveList();
				}
			}
			else
			{
				if (clubHead.ui_levels == 3) {
					getActiveList();
				} else {
					getAllList();
				}
			}
		}
	}


	private int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	private int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}


	private void  createLeaderHeaders(List<ClubDetailHead.Leader> leaders) {

		LinearLayout ll = new LinearLayout(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);

		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER_VERTICAL);


		int margin = dip2px(this, 8);
		int imageSize = dip2px(this, 27);
		int round = dip2px(this, 4);

		int size = leaders.size();
		if (size > 8) {
			size = 9;
		}
		for (int i = 0; i < size; ++i) {
			RoundedImageView ri = new RoundedImageView(this);
			ri.setCornerRadius(round);
			ri.setScaleType(ImageView.ScaleType.FIT_XY);
			if (i < 8) {
				final ClubDetailHead.Leader leader = leaders.get(i);
				ImageLoader.getInstance().displayImage(leader.headicon, ri, options);
				ri.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LSClubDetailActivity.this, LSUserHomeActivity.class);
						intent.putExtra("userID", leader.user_id);
						startActivity(intent);
					}
				});
			} else {
				ri.setImageResource(R.drawable.club_admin_more);
				ri.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LSClubDetailActivity.this, LSClubBriefActivity.class);
						intent.putExtra("clubID", clubID);
						startActivityForResult(intent, 999);
					}
				});
			}
			LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(imageSize, imageSize);
			if (i != 0) {
				lllp.setMargins(margin, 0, 0, 0);
			}
			ri.setLayoutParams(lllp);
			ll.addView(ri);
		}

		leaderPanel.removeAllViews();
		leaderPanel.addView(ll);
	}

	private void  createTags() {

		tagPanel.setVisibility(View.VISIBLE);

		LinearLayout ll = new LinearLayout(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);

		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER_VERTICAL);


		int margin = dip2px(this, 12);
		int height = dip2px(this, 20);

		for (int i = 0; i < clubHead.tags.size(); ++i) {
			final ClubDetailHead.TagItem item = clubHead.tags.get(i);
			TextView tv = new TextView(this);
			tv.setBackgroundResource(R.drawable.club_top_header_tag_bg);
			tv.setTextColor(Color.rgb(0xcd, 0xad, 0x76));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
			if (item.name.length() > 7) {
				tv.setText("    "+item.name.substring(0,7) + "..." + "    ");
			} else {
				tv.setText("    "+item.name+"    ");
			}

			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(LSClubDetailActivity.this, SpecialInfoActivity.class);
//					intent.putExtra("tagid", item.id);
//					startActivity(intent);
					ActivityUtil.goSpecialInfoActivity(activity, item.id);
				}
			});
			LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height);
			if (i == clubHead.tags.size() - 1) {
				lllp.setMargins(margin, 0, margin, 0);
			} else {
				lllp.setMargins(margin, 0, 0, 0);
			}
			tv.setGravity(Gravity.CENTER);

			tv.setLayoutParams(lllp);
			ll.addView(tv);
		}
		tagPanel.removeAllViews();
		tagPanel.addView(ll);
	}

}
