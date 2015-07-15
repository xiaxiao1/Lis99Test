package com.lis99.mobile.mine;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.mine.adapter.LSMineApplyAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.view.MyListView;

import java.util.List;

public class LSMineApplyActivity extends LSBaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	MyListView listView;
	LSMineApplyAdapter adapter;

	PullToRefreshView refreshView;

	private Page page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_mine_apply);
		initViews();
		setTitle("报名通知");
		
		page = new Page();

		getApplayList();
	}

	@Override
	protected void initViews() {
		super.initViews();
		listView = (MyListView) findViewById(R.id.listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LSMineApplyActivity.this, LSClubTopicActivity.class);
				intent.putExtra("topicID", Integer.valueOf(adapter.getItem(position).getTopic_id()));
				startActivity(intent);
				
			}
		});

		refreshView = (PullToRefreshView) findViewById(R.id.refreshView);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

	}
	
	@Override
	protected void rightAction() {
		
		DialogManager.getInstance().startAlert(this, "提示", "确定清空消息？", true, "确定", new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO Auto-generated method stub
				postMessage(ActivityPattern1.POPUP_PROGRESS,
						getString(R.string.sending));
				
				String uid = DataManager.getInstance().getUser().getUser_id();

				String url = C.LS_MINE_APPLY_INFO_CLEAR +  uid;
				Task task = new Task(null, url, C.HTTP_GET, C.LS_MINE_APPLY_INFO_CLEAR, LSMineApplyActivity.this);
				publishTask(task, IEvent.IO);
			}
		}, true, "取消", null);
		
		
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		view.onFooterRefreshComplete();
		getApplayList();

	}

	private void getApplayList() {

		if (page.getPageNo() >= page.getCount()) {
			Common.toast("没有数据了");
			return;
		}

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		
		String uid = DataManager.getInstance().getUser().getUser_id();

		String url = C.LS_MINE_APPLY_INFO +  uid +"?page=" + page.pageNo;
		Task task = new Task(null, url, C.HTTP_GET, C.LS_MINE_APPLY_INFO, this);
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
				if (((String) task.getParameter()).equals(C.LS_MINE_APPLY_INFO)) {
					parserApplyInfoList(result);
				} else if (((String) task.getParameter()).equals(C.LS_MINE_APPLY_INFO_CLEAR)){
					parserClearInfot(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	
	private void parserClearInfot(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				errCode = "没有内容";
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					postMessage(ActivityPattern1.POPUP_TOAST, "已清空");
					adapter.clear();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}

	private void parserApplyInfoList(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				errCode = "没有内容";
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			
			JsonNode data = root.get("data");
			page.setCount(data.get("totpage").asInt());
			page.setPageNo(page.getPageNo()+1);
			final List<LSMineApplyInfo> temp = LSFragment.mapper.readValue(data.get("baominglist").traverse(), new TypeReference<List<LSMineApplyInfo>>() {
			});
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (adapter == null) {
						adapter = new LSMineApplyAdapter(LSMineApplyActivity.this, temp);
						listView.setAdapter(adapter);
					} else {
						adapter.appendData(temp);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
//			数据为空时， 会走这里
			Common.toast("没有内容");
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		view.onHeaderRefreshComplete();
		cleanList();
		getApplayList();

	}

	private void cleanList() {
		page = new Page();
		adapter.clear();
	}

}
