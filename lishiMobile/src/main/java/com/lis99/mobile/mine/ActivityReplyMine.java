package com.lis99.mobile.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.MineReplyModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.mine.adapter.AdapterMineReplyItem;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.SharedPreferencesHelper;

import java.util.HashMap;

public class ActivityReplyMine extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener
{
	private PullToRefreshView pull_refresh_view;
	
	private ListView listView;
	
	private AdapterMineReplyItem adapter;
	
	private MineReplyModel model;

	private Page page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_reply_list);
		initViews();
		setTitle("回复我的");
		page = new Page();
		model = new MineReplyModel();
		getReplyList();
	}
	
	@Override
	protected void initViews()
	{
		// TODO Auto-generated method stub
		super.initViews();
		pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		listView = (ListView) findViewById(R.id.listView);
		
		pull_refresh_view.setOnHeaderRefreshListener(this);
		pull_refresh_view.setOnFooterRefreshListener(this);
		
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view)
	{
		// TODO Auto-generated method stub
		pull_refresh_view.onFooterRefreshComplete();
		getReplyList();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view)
	{
		// TODO Auto-generated method stub
		pull_refresh_view.onHeaderRefreshComplete();
		cleanList();
		getReplyList();
	}
	
	private void cleanList ()
	{
		adapter = null;
		page = new Page();
	}
	
	private void getReplyList ()
	{
		if ( page.pageNo >= page.pageSize )
		{
			Common.toast("没有数据了");
			return;
		}
		String UserId = DataManager.getInstance().getUser().getUser_id();
		String token = SharedPreferencesHelper.getLSToken();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("token", token);
		
		String url = null;
		if ( TextUtils.isEmpty(UserId))
		{
			url = C.MINE_REPLY_LIST + "?page=" + page.pageNo;
		}
		else
		{
			url = C.MINE_REPLY_LIST + UserId + "?page=" + page.pageNo;
		}
		MyRequestManager.getInstance().requestPost(url, map, model, new CallBack()
		{
			@Override
			public void handlerError(MyTask myTask) {
				Common.showEmptyView(ActivityReplyMine.this,R.id.empty_view,true);
			}
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				model = (MineReplyModel) mTask.getResultModel();
				if ( model == null || model.replylist == null )
				{
					return;
				}
				if ( adapter == null )
				{
					page.pageSize = model.totpage;
					adapter = new AdapterMineReplyItem(ActivityReplyMine.this, model.replylist);
					listView.setAdapter(adapter);
				}
				else
				{
					adapter.addList(model.replylist);
				}
				page.pageNo += 1;
				Common.showEmptyView(ActivityReplyMine.this,R.id.empty_view,model.replylist);
			}
		});
		
		
	}
	
	
}
