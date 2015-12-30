package com.lis99.mobile.club;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubEventApplyerAdapter;
import com.lis99.mobile.club.model.ClubApplyManagerListModel;
import com.lis99.mobile.club.model.LSClubEventApplyer;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 			管理报名人员
 * @author yy
 *
 */
public class LSClubApplyListActivity extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{
	
	int clubID;
	public static int topicID;
	List<LSClubEventApplyer> applyers = new ArrayList<LSClubEventApplyer>();
	///人数
	TextView totalView;
	//俱乐部标题
	TextView tv_title;
	int total;
	int totalPeermen;
	
	MyListView listView;
	LSClubEventApplyerAdapter adapter;
	
	PullToRefreshView refreshView;
	private String clubName;
	private Page page;
	private ClubApplyManagerListModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clubID = getIntent().getIntExtra("clubID", 0);
		topicID = getIntent().getIntExtra("topicID", 0);
		clubName = getIntent().getStringExtra("clubName");
		setContentView(R.layout.activity_lsclub_apply_list);
		initViews();
		setTitle("管理报名");
		
		model = new ClubApplyManagerListModel();
		page = new Page();
		
		getApplayList();
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		totalView = (TextView) findViewById(R.id.totalView);
		listView = (MyListView) findViewById(R.id.listView);
		
		refreshView = (PullToRefreshView) findViewById(R.id.refreshView);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(clubName);
		tv_title.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LSClubApplyListActivity.this, LSClubTopicActivity.class);
//				intent.putExtra("clubID", clubID);
//				intent.putExtra("topicID", topicID);
//				intent.putExtra("clubName", clubName);
//				startActivity(intent);
			}
		});
	}
	
	
	//通过
	public void applyPass ()
	{
		
	}
//	拒绝
	public void applyReject ()
	{
		
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		view.onFooterRefreshComplete();
		getApplayList();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		view.onHeaderRefreshComplete();
		cleanList();
		getApplayList();
	}
	
	private void cleanList ()
	{
		page = new Page();
		adapter = null;
	}
	
	private void getApplayList ()
	{
		if ( page.getPageNo() >= page.getPageSize() )
		{
			Common.toast("没有数据了");
			return;
		}
		
		model = new ClubApplyManagerListModel();
		String UserId = DataManager.getInstance().getUser().getUser_id();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("club_id", clubID);
		map.put("user_id", UserId);
		map.put("token", SharedPreferencesHelper.getLSToken());
		String url = C.CLUB_TOPIC_MANAGER_APPLY_LIST + topicID + "/" + page.pageNo;
		MyRequestManager.getInstance().requestPost(url, map, model, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				model = (ClubApplyManagerListModel) mTask.getResultModel();
				
				
				if ( model.list == null ) return;
				
				if ( adapter == null )
				{
					page.pageSize = model.totPage;
					tv_title.setText(model.title);
					totalView.setText("报名" + model.applyTotal + "人， 已确认" + model.applyPass + "人， 已拒绝" + model.applyRefuse + "人， 待确认" + model.applyAudit + "人");
					
					adapter = new LSClubEventApplyerAdapter(LSClubApplyListActivity.this, model);
					listView.setAdapter(adapter);
					
				}
				else{
					adapter.addList(model.list);
				}
				
				page.pageNo += 1;
				
				
			}
		});
	}
	
}
