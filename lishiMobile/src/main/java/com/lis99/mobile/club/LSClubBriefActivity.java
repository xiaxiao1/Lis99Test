package com.lis99.mobile.club;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubBriefListAdapter;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.club.model.LSClubAdmin;
import com.lis99.mobile.club.model.LSClubTag;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

public class LSClubBriefActivity extends LSBaseActivity {
	
	int clubID = 0;
	
	TextView nameView;
	TextView addressView;
	TextView sportView;
	TextView timeView;
	TextView founderView;
	TextView descView;
	ImageView roundedImageView1;
	ImageView founderImageView, iv_title_bg;
	
	LSClubBriefListAdapter listAdapter;
	
	LSClub club;
	
	DisplayImageOptions options;
	DisplayImageOptions headerOptions;
	//=2.3=====
	public final int JOINCLUB = 199;
	public final int QUITCLUB = 299;
	private ImageView vipStar;
	private ScrollView scroll;

	private TextView tv_leader;
	
	
	private void buildOptions() {
		options = ImageUtil.getImageOptionClubIcon();
		headerOptions = ImageUtil.getclub_topic_headImageOptions();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clubID = getIntent().getIntExtra("clubID", 0);
		setContentView(R.layout.activity_club_brief);
		initViews();
		setTitle("");

		setLeftView(R.drawable.club_ditail_title_back);

		setRightViewColor(getResources().getColor(R.color.white));
		
		buildOptions();
		loadClubBriefInfo();
	}
	
	private void loadClubBriefInfo (){
		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_GET_ONE_INFO + "?club_id=" + clubID;
		if (userID != null && !"".equals(userID)) {
			url += "&user_id=" + userID;
		}
		Common.log(url);
		Task task = new Task(null, url, null, C.CLUB_GET_ONE_INFO, this);
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
				Common.log(result);
				if (((String) task.getParameter()).equals(C.CLUB_GET_ONE_INFO)) {
					parserClubInfo(result);
				}
				else if (((String) task.getParameter()).equals(C.CLUB_JOIN))
				{
					parserJoinClub(result);
				}
				else if (((String) task.getParameter()).equals(C.CLUB_QUIT)) {
					parserQuitClubInfo(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			club = LSFragment.mapper.readValue(data.traverse(), LSClub.class);
			postMessage(SHOW_UI);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_UI) {
//			//显示加入俱乐部
//			if ( club.getIs_jion() == -1 )
//			{
//				setRightView("加入");
//			}
//			else
//			{
//				setRightView("已加入");
//			}

			if ( "1".equals(club.getIs_lishi()))
			{
				tv_leader.setText("版主");
			}
			else
			{
				tv_leader.setText("领队");
			}


			nameView.setText(club.getTitle());
			addressView.setText(club.getProvince() + " " + club.getCity());

			String tagString = "";
			List<LSClubTag> tags = club.getClubsport();
			if (tags != null && tags.size() > 0) {
				for (int i = 0; i < tags.size(); i++) {
					tagString += tags.get(i).getTitle() + " ";
				}
				sportView.setText(tagString);
			} else {
				sportView.setText("暂无");
			}
			

			ImageLoader.getInstance().displayImage(club.getImage(),
					roundedImageView1, options);
			
			ImageLoader.getInstance().displayImage(club.getInitheadicon(),
					founderImageView, headerOptions);
			
			if ( "1".equals(club.creater_is_vip) )
			{
				vipStar.setVisibility(View.VISIBLE);
			}
			else
			{
				vipStar.setVisibility(View.GONE);
			}
			
			founderView.setText(club.getInitnickname());
			
			descView.setText(club.getDescript());
			
			timeView.setText(club.getCreate_time());

			List<LSClubAdmin> admins = club.getAdminlist();

			if ( "1".equals(club.getIs_lishi()))
			{
				admins = club.getModerator_list();
			}

//			if (admins == null || admins.size() == 0) {
//				admins = new ArrayList<LSClubAdmin>();
//				LSClubAdmin admin = new LSClubAdmin();
//				admin.setHeadicon(club.getInitheadicon());
//				admin.setNickname(club.getInitnickname());
//				admins.add(admin);
//			}
			listAdapter = new LSClubBriefListAdapter(admins, this);
			listView.setAdapter(listAdapter);


			return true;
		}
//		else if (msg.what == JOINCLUB )
//		{
//			setRightView("已加入");
//			setresult(true);
//		}
//		else if ( msg.what == QUITCLUB )
//		{
//			setRightView("加入");
//			setresult(false);
//		}
		return super.handleMessage(msg);
	}
	
	ListView listView;
	
	@Override
	protected void initViews() {
		super.initViews();
		
		nameView = (TextView) findViewById(R.id.nameView);
		addressView = (TextView) findViewById(R.id.addressView);
		sportView = (TextView) findViewById(R.id.sportView);
		timeView = (TextView) findViewById(R.id.timeView);
		founderView = (TextView) findViewById(R.id.founderView);
		descView = (TextView) findViewById(R.id.descView);
		
		roundedImageView1 = (ImageView) findViewById(R.id.roundedImageView1);
		founderImageView = (ImageView) findViewById(R.id.founderImageView);
		
		listView = (ListView) findViewById(R.id.listView);
		vipStar = (ImageView) findViewById(R.id.vipStar);
		scroll = (ScrollView) findViewById(R.id.scroll);
		scroll.smoothScrollTo(0, 0);

		iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
		iv_title_bg.setBackgroundColor(Color.parseColor("#29ca62"));

		tv_leader = (TextView) findViewById(R.id.tv_leader);

	}
	
//	@Override
//	protected void rightAction() {
//		// TODO Auto-generated method stub
//		super.rightAction();
//		if (club == null) {
//			return;
//		}
//		String userID = DataManager.getInstance().getUser().getUser_id();
//		if (TextUtils.isEmpty(userID)) {
//			Intent intent = new Intent(this, LSLoginActivity.class);
//			startActivity(intent);
//			return;
//		}
//
//		if (club.getIs_jion() == -1) {
//			joinClub();
//		} else if (club.getIs_jion() == 4){
//			quitClub();
//		} else {
//			postMessage(POPUP_TOAST, "您是俱乐部管理员，不能退出俱乐部");
//		}
//	}
	
	private void quitClub ()
	{
		DialogManager.getInstance().startAlert(this, "退出", "确定要退出俱乐部吗？", true, "退出", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				doQuitClub();
			}
		}, true, "取消", null);
	}
	
	private void doQuitClub ()
	{
		String userID = DataManager.getInstance().getUser().getUser_id();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("club_id", clubID);
		params.put("user_id", userID);

		Task task = new Task(null, C.CLUB_QUIT, C.HTTP_POST,
				C.CLUB_QUIT, this);
		task.setPostData(RequestParamUtil.getInstance(this)
				.getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
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
			club.setIs_jion(-1);
			postMessage(QUITCLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	private void joinClub ()
	{
		String userID = DataManager.getInstance().getUser().getUser_id();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("club_id", clubID);
		params.put("user_id", userID);
		
		Task task = new Task(null, C.CLUB_JOIN, C.HTTP_POST, C.CLUB_JOIN, this);
		task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}
	
	private void parserJoinClub (String result )
	{
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			postMessage(ActivityPattern1.POPUP_TOAST, "加入成功");
			club.setIs_jion(4);
			postMessage(JOINCLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	//是加入还是退出
	private void setresult (boolean b)
	{
		Intent intent = new Intent();
		intent.putExtra("join", b);
		setResult(RESULT_OK, intent);
	}
	
}
