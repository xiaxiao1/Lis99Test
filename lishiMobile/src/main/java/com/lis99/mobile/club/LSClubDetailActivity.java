package com.lis99.mobile.club;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubDitalAdapter;
import com.lis99.mobile.club.model.ClubDetailHead;
import com.lis99.mobile.club.model.ClubDetailList;
import com.lis99.mobile.club.model.ClubDetailList.Topiclist;
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
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

public class LSClubDetailActivity extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{

	ListView listView;
	LSClubDitalAdapter adapter;

	int clubID;
	View headerView, view_line_head;
	RoundedImageView headerImageView;
//	Button addButton;
	TextView nameView;
	TextView locView;
	/**类型， （登山）*/
	TextView labelView;
	TextView memberNumView;

	boolean needRefresh = false;
	boolean loginBeforePause = true;

	View allPanel;
	TextView allView;
	View allLine;

	View eventPanel;
	TextView eventView;
	View eventLine;
	
	View topPanel;
	
	int lastScrollY = 0;
	
	int type = -1;
	int offset = 0;
	
	private final static int SHOW_CLUB = 1001;
	private final static int SHOW_MORE_TOPIC = 1003;
	private final static int NO_MORE_TOPIC = 1004;
	private final static int SHOW_ADDBUTTON = 1002;
	
	public final static String CLUB_TOPIC_CHANGE = "com.lis99.topicChange";
	
	private LocalBroadcastManager lbm;

	DisplayImageOptions options;
	
	int topPanelHeight;
	boolean topPanelVisible = true;
	
	//=============2.3版本=============
	private int[] clubIcon = new int[]
			{
			R.drawable.club_0, R.drawable.club_1,
			R.drawable.club_2, R.drawable.club_3,
			R.drawable.club_4, R.drawable.club_5,
			R.drawable.club_6, R.drawable.club_7,
			R.drawable.club_8, R.drawable.club_9,
			};

//	private MyScrollView scrollview;
	
	//标题栏
//	private ImageView iv_title_bg;
//	private ImageView titleRightImage;
	//默认给一个值， 防止初始化为0的时候， 会显示出来Tab
	private float HeadAdHeight = 1;
//	private TextView title;
	//TAB置顶
	private LinearLayout topPanel1;
	private RelativeLayout allPanel1, eventPanel1;
	private TextView allView1, eventView1;
	private View allLine1, eventLine1, view_line;
//	========
	
	private ClubDetailList clubAll;
	private Page page;
	
	private View headViewMain;
	//ListView 第一个可见item
	private int visibleFirst;
	
	
	
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
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(CLUB_TOPIC_CHANGE);
		lbm = LocalBroadcastManager.getInstance(this);
		lbm.registerReceiver(localReceiver, filter);
		clubHead = new ClubDetailHead();
		
		clubAll = new ClubDetailList();
//		clubActive = new ClubDetailList();
		page = new Page();
//		pageactive = new Page();
////		加载俱乐部head
//		loadClubInfo();

		setBack(true);

		getAllList();
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		topPanel1 = (LinearLayout) findViewById(R.id.topPanel1);
		allPanel1 = (RelativeLayout) findViewById(R.id.allPanel1);
		eventPanel1 = (RelativeLayout) findViewById(R.id.eventPanel1);
		allView1 = (TextView) findViewById(R.id.allView1);
		eventView1 = (TextView) findViewById(R.id.eventView1);
		allLine1 = findViewById(R.id.allLine1);
		eventLine1 = findViewById(R.id.eventLine1);
		view_line = findViewById(R.id.view_line);
		
		topPanel1.setVisibility(View.GONE);
		view_line.setVisibility(View.GONE);
		
		
		
//		iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
		setTitleBarAlpha(0f);
//		title = (TextView) findViewById(R.id.title);
//		title.setAlpha(0f);
//		titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
		setRightView(R.drawable.club_join);
		setLeftView(R.drawable.ls_club_back_icon_bg);
		titleRightImage.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listView);

		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
		headViewMain = View.inflate(activity, R.layout.club_header_view, null);
		
		headerView = headViewMain.findViewById(R.id.headView);
		
		view_line_head = headViewMain.findViewById(R.id.view_line_head);
		
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
		
		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LSClubDetailActivity.this, LSClubBriefActivity.class);
				intent.putExtra("clubID", clubID);
//				startActivity(intent);
				startActivityForResult(intent, 999);
			}
		});

//		adapter = new LSTopicAdapter(this, null);
//		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				LSClubTopic topic = club.getTopiclist().get(position);
				Topiclist item = null;
				item = (Topiclist) adapter.getItem(position - 1);
				if ( item == null ) return;

				if ( "2".equals(item.category))
				{
					Intent intent = new Intent(LSClubDetailActivity.this, LSClubTopicNewActivity.class);
//				intent.putExtra("clubID", clubID);
					intent.putExtra("topicID", item.id);
//				intent.putExtra("clubName", item.title);
					startActivityForResult(intent, 998);
					return;
				}

				Intent intent = new Intent(LSClubDetailActivity.this, LSClubTopicActivity.class);
//				intent.putExtra("clubID", clubID);
				intent.putExtra("topicID", item.id);
//				intent.putExtra("clubName", item.title);
				startActivityForResult(intent, 998);
			}
		});

		headerImageView = (RoundedImageView)headViewMain.findViewById(R.id.roundedImageView1);
//		addButton = (Button) findViewById(R.id.addButton);
//		addButton.setOnClickListener(this);
		
		nameView = (TextView) headViewMain.findViewById(R.id.nameView);
		locView = (TextView) headViewMain.findViewById(R.id.locView);
		labelView = (TextView) headViewMain.findViewById(R.id.labelView);
		memberNumView = (TextView) headViewMain.findViewById(R.id.memberNumView);
		
		
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
		
		listView.setOnScrollListener( new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				visibleFirst = firstVisibleItem;
				View v = listView.getChildAt(0);
				if ( v == null ) return;
				float alpha = v.getTop();
				if ( visibleFirst > 0 )
				{
					setTitleAlpha(HeadAdHeight);
					setTabTopVisible(HeadAdHeight);
				}
				else
				{
					setTitleAlpha(-alpha);
					setTabTopVisible(-alpha);
				}
			}
		});
		
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
	
	
	private void loadClubInfo() {
//		offset = 0;
//		String userID = DataManager.getInstance().getUser().getUser_id();
//
//		postMessage(ActivityPattern1.POPUP_PROGRESS,
//				getString(R.string.sending));
//		String url = C.CLUB_GET_INFO + "?club_id=" + clubID + "&category=" + type + "&offset=" + offset;
//		if (userID != null && !"".equals(userID)) {
//			url += "&user_id=" + userID;
//		}
//
//		Task task = new Task(null, url, null, C.CLUB_GET_INFO, this);
//		publishTask(task, IEvent.IO);
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
				// TODO Auto-generated method stub
				clubHead = (ClubDetailHead) mTask.getResultModel();
				initClubHead();
				
			}
		});
	}
	
	private void initClubHead ()
	{
		headerView.setBackgroundResource(clubIcon[Common.getClubId(clubHead.club_id)]);
		nameView.setText(clubHead.title);
		memberNumView.setText(clubHead.topicnum + "个帖");
		locView.setText(clubHead.cityname);
		
		if (clubHead.is_jion.equals("-1")) {
			setRightView(R.drawable.club_join);
		} else{
			setRightView(R.drawable.club_joined);
		}
		labelView.setText(clubHead.catename);
		
		if ( !TextUtils.isEmpty(clubHead.images))
		ImageLoader.getInstance().displayImage(clubHead.images,
				headerImageView, options);

		
		
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

//	private void parserClubInfo(String result) {
//		try {
//			JsonNode root = LSFragment.mapper.readTree(result);
//			String errCode = root.get("status").asText("");
//			if (!"OK".equals(errCode)) {
//				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
//				return;
//			}
//			JsonNode data = root.get("data");
//			club = LSFragment.mapper.readValue(data.traverse(), LSClub.class);
//			if (club.getTopiclist() != null) {
//				offset++;
//			}
//			postMessage(SHOW_CLUB);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			postMessage(ActivityPattern1.DISMISS_PROGRESS);
//		}
//	}
	
	
	
	
//	private void parserMoreClubInfo(String result) {
//		try {
//			JsonNode root = LSFragment.mapper.readTree(result);
//			String errCode = root.get("status").asText("");
//			if (!"OK".equals(errCode)) {
//				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
//				return;
//			}
//			JsonNode data = root.get("data");
//			List<LSClubTopic> topics = LSFragment.mapper.readValue(data.get("topiclist").traverse(), new TypeReference<List<LSClubTopic>>() {
//			});
//			if (topics != null && topics.size() > 0) {
//				offset++;
//				club.addTopics(topics);
//				postMessage(SHOW_MORE_TOPIC);
//			} else {
//				postMessage(POPUP_TOAST, "没有更多帖子");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			postMessage(ActivityPattern1.DISMISS_PROGRESS);
//		}
//	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_CLUB) {
//			headerView.setBackgroundResource(clubIcon[Common.getClubId(""+club.getId())]);
//			nameView.setText(club.getTitle());
//			memberNumView.setText(club.getMembers() + "位成员");
//			locView.setText(club.getProvince() + " " + club.getCity());
//			
//			if (club.getIs_jion() == -1) {
//				setRightView(R.drawable.club_join);
//			} else{
//				setRightView(R.drawable.club_joined);
//			}
//
//			String tagString = "";
//			List<LSClubTag> tags = club.getClubsport();
//			if (tags != null && tags.size() > 0) {
//				for (int i = 0; i < tags.size(); i++) {
//					tagString += tags.get(i).getTitle() + " ";
//				}
//				labelView.setVisibility(View.VISIBLE);
//				labelView.setText(tagString);
//			} else {
//				labelView.setVisibility(View.INVISIBLE);
//			}
//			if ( !TextUtils.isEmpty(club.getImage()))
//			ImageLoader.getInstance().displayImage(club.getImage(),
//					headerImageView, options);

//			adapter = new LSTopicAdapter(this, club.getTopiclist());
//			listView.setAdapter(adapter);

			return true;
		} else if (msg.what == SHOW_MORE_TOPIC) { 
			adapter.notifyDataSetChanged();
			return true;
		} else if (msg.what == NO_MORE_TOPIC){
			
		} else if (msg.what == SHOW_ADDBUTTON) {
			setTitleRight(isBg);
//			if ("-1".equals(clubHead.is_jion)) {
//				setRightView(R.drawable.club_join);
//			} else{
//				setRightView(R.drawable.club_joined);
//			}
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
		if(view.getId() == allPanel.getId() || view.getId() == allPanel1.getId()){
			if (type == -1) {
				return;
			}
			type = -1;
			//这跟线在线路活动 里是没有的
			view_line_head.setVisibility(View.VISIBLE);
			allView1.setTextColor(getResources().getColor(R.color.text_color_blue));
			allView.setTextColor(getResources().getColor(R.color.text_color_blue));
			allLine1.setVisibility(View.VISIBLE);
			allLine.setVisibility(View.VISIBLE);
			eventLine1.setVisibility(View.GONE);
			eventLine.setVisibility(View.GONE);
			eventView1.setTextColor(getResources().getColor(R.color.bantouming));
			eventView.setTextColor(getResources().getColor(R.color.bantouming));
//			loadClubInfo();
			cleanList();
			getAllList();
		}else if(view.getId() == eventPanel.getId() || view.getId() == eventPanel1.getId() ){
			if (type == 1) {
				return;
			}
			type = 1;
			//这跟线消失
			view_line_head.setVisibility(View.GONE);
			eventView.setTextColor(getResources().getColor(R.color.text_color_blue));
			eventView1.setTextColor(getResources().getColor(R.color.text_color_blue));
			eventLine.setVisibility(View.VISIBLE);
			eventLine1.setVisibility(View.VISIBLE);
			allLine.setVisibility(View.GONE);
			allLine1.setVisibility(View.GONE);
			allView.setTextColor(getResources().getColor(R.color.bantouming));
			allView1.setTextColor(getResources().getColor(R.color.bantouming));
//			loadClubInfo();
			cleanList();
			getActiveList();
		} else if (view.getId() == R.id.publishButton) {
			String userID = DataManager.getInstance().getUser().getUser_id();
			if (TextUtils.isEmpty(userID)) {
				Intent intent = new Intent(this, LSLoginActivity.class);
				startActivity(intent);
				return;
			}
			Intent intent = new Intent(LSClubDetailActivity.this, LSClubPublish2Activity.class);
			intent.putExtra("clubID", clubID);
//			startActivity(intent);
			startActivityForResult(intent, 998);
			return;
		} else if (view.getId() == R.id.addButton || view.getId() == R.id.titleRightImage ) {
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
			getAllList();
		}
		else
		{
			getActiveList();
		}
	}
	
	private void getHeadAdHeight ()
	{
		int titleHeight = iv_title_bg.getHeight();
		HeadAdHeight = headerView.getHeight() - titleHeight;
	}
	private boolean isBg = false;
	/**
	 * 设置标题栏透明度
	 * @param num
	 */
	private void setTitleAlpha ( float num )
	{
		if ( num >= HeadAdHeight )
		{
			num = HeadAdHeight;
			isBg = false;
			setTitleRight(false);
			setBack(false);
		}
		else if ( num <= 0 )
		{
			num = 0;
		}
		if ( num < HeadAdHeight && num >= 0 )
		{
			isBg = true;
			setTitleRight(true);
			setBack(true);
		}
		float alpha = num / HeadAdHeight;
//			iv_title_bg.setAlpha(alpha);
//			title.setAlpha(alpha);
		setTitleBarAlpha(alpha);
	}
	/**
	 * 隐藏置顶标题栏
	 * @param num
	 */
	private void setTabTopVisible ( float num )
	{
		if ( num >= HeadAdHeight )
		{
			topPanel1.setVisibility(View.VISIBLE);
			view_line.setVisibility(View.VISIBLE);
		}
		else
		{
			topPanel1.setVisibility(View.GONE);
			view_line.setVisibility(View.GONE);
		}
	}
	//设置title右边按钮
	private void setTitleRight ( boolean isBg )
	{
		if (clubHead == null ) return;
		if ( "-1".equals(clubHead.is_jion))
		{
			if ( isBg )
			{
				setRightView(R.drawable.club_join);
			}
			else
			{
				setRightView(R.drawable.join_null);
			}
		}
		else
		{
			if ( isBg )
			{
				setRightView(R.drawable.club_joined);
			}
			else
			{
				setRightView(R.drawable.joined_null);
			}
		}
	}
//	设置返回按钮
	private void setBack ( boolean isBg)
	{
		if ( isBg )
		{
			setLeftView(R.drawable.ls_club_back_icon_bg);
		}
		else
		{
			setLeftView(R.drawable.ls_page_back_icon);
		}
	}
	
//=========
	//获取所有列表
	public void getAllList ()
	{
		//当前页数>总页数 return
		if ( page.pageNo >= page.pageSize )
		{
			Common.toast("没有更多帖子");
			 return;
		}
		
		MyRequestManager.getInstance().requestGet(C.CLUB_DETAIL_LIST + clubID + "/" + "0" + "?page=" + page.getPageNo(), clubAll, new CallBack() {
			
			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				page.pageNo += 1;
				clubAll = (ClubDetailList) mTask.getResultModel();
				if ( clubAll.topiclist == null )
				{
					return;
				}
				if ( adapter == null )
				{
					page.pageSize = clubAll.totpage;
					adapter = new LSClubDitalAdapter(activity, clubAll.topiclist, false);
					listView.setAdapter(adapter);
//					//隐藏标题懒
//					setTabTopVisible(0);
//					setTitleAlpha(0);
//					listView.setSelection(0);
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
		adapter = null;
		listView.setAdapter(null);
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
		MyRequestManager.getInstance().requestGet(C.CLUB_DETAIL_LIST + clubID + "/" + "1" + "?page=" + page.getPageNo(), clubAll, new CallBack() {
			
			@Override
			public void handler(MyTask mTask) {
				// TODO Auto-generated method stub
				page.pageNo += 1;
				clubAll = (ClubDetailList) mTask.getResultModel();
				if ( clubAll.topiclist == null )
				{
					return;
				}
				if ( adapter == null )
				{
					page.pageSize = clubAll.totpage;
					adapter = new LSClubDitalAdapter(activity, clubAll.topiclist, true);
					listView.setAdapter(adapter);
//					//隐藏标题懒
//					setTabTopVisible(0);
//					setTitleAlpha(0);
//					listView.setSelection(0);
					return;
				}
				adapter.addList(clubAll.topiclist);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
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
			loadClubInfo();
			if ( type == -1 )
			{
				getAllList();
			}
			else
			{
				getActiveList();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
