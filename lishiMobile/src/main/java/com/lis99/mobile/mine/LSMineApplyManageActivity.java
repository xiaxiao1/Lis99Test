package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.apply.ApplyManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.mine.adapter.LSMineApplyManageAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.view.MyListView;

import java.util.List;

public class LSMineApplyManageActivity extends LSBaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	MyListView listView;
	LSMineApplyManageAdapter adapter;

	PullToRefreshView refreshView;

	private Page page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_mine_apply_manage);
		initViews();
		setTitle("我发布的活动");

		page = new Page();

		getApplayList();
	}

	@Override
	protected void initViews() {
		super.initViews();

		refreshView = (PullToRefreshView) findViewById(R.id.refreshView);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		listView = (MyListView) findViewById(R.id.listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {


				if (adapter == null) {
					Common.log("adapter == null");
					return;
				}

				Intent intent = new Intent(LSMineApplyManageActivity.this,
						ApplyManager.class);
				intent.putExtra("topicID", Integer.valueOf(adapter.getItem(
						position).getTopic_id()));
				intent.putExtra("clubID", Integer.valueOf(adapter.getItem(
						position).getClub_id()));
				intent.putExtra("clubName", adapter.getItem(
						position).getClub_title());
				startActivity(intent);

				Common.log("adapter == onclick");

				//更新红点消失
				adapter.onClick(position);

			}
		});

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

		String url = C.LS_MINE_APPLY_MANAGE + uid + "?page=" + page.pageNo;
		Task task = new Task(null, url, C.HTTP_GET, C.LS_MINE_APPLY_MANAGE, this);
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
				if (((String) task.getParameter()).equals(C.LS_MINE_APPLY_MANAGE)) {
					parserApplyInfoList(result);
				} 
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}


	private void parserApplyInfoList(String result) {
		try {
			Common.log("rulst="+result);
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				String error = "";
				try
				{
					JsonNode data = root.get("data");
					error = data.get("error").asText();
					
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				if ( !TextUtils.isEmpty(error))
				{
					postMessage(ActivityPattern1.POPUP_TOAST, error);
				}
				return;
			}

			JsonNode data = root.get("data");
			page.setCount(data.get("totpage").asInt());
			page.setPageNo(page.getPageNo() + 1);
			final List<LSMineApplyManageInfo> temp = LSFragment.mapper.readValue(data
					.get("topiclist").traverse(),
					new TypeReference<List<LSMineApplyManageInfo>>() {
					});
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (adapter == null) {
						adapter = new LSMineApplyManageAdapter(
								LSMineApplyManageActivity.this, temp);
						listView.setAdapter(adapter);
					} else {
						adapter.appendData(temp);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
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