package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LSSkiingParkActivity extends ActivityPattern implements
OnHeaderRefreshListener, OnFooterRefreshListener{
	
	private PullToRefreshView refreshView;
	private ListView listView;
	LSSkiingParkAdapter adapter;
	List<LSSkiingPark> loaedParks = new ArrayList<LSSkiingPark>();
	
	View iv_back;
	
	int page;
	int totalPage = 0;
	private static final int SHOW_SKIINGPARK_LIST = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsskiing_park);
		StatusUtil.setStatusBar(this);
		
		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		listView = (ListView) findViewById(R.id.listView);
		
		iv_back = findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		
		loadData();
	}
	
	private void loadData(){
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.SKIINGPARK_LIST + page;
		Task task = new Task(null, url, null, "SKIINGPARK_LIST", this);
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
				String param = (String) task.getParameter();
				if ("SKIINGPARK_LIST".equals(param)) {
					parserContents(result);
				}
			}
		default:
			break;
		}
	}

	private void parserContents(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				try {
					JSONObject json = new JSONObject(params);
					JSONObject data = json.optJSONObject("data");
					totalPage = data.optInt("totalPage");

					JSONArray list = data.optJSONArray("lists");

					if (list == null)
						return;
					int length = list.length();
					List<LSSkiingPark> contents = new ArrayList<LSSkiingPark>(
							length);

					for (int i = 0; i < length; ++i) {
						JSONObject aDict = list.getJSONObject(i);
						LSSkiingPark park = new LSSkiingPark();
						 	park.parkID = aDict.optInt("id");
			                park.title = aDict.optString("title");
			                park.mobileimg = aDict.optString("mobileimg");
			                park.level = aDict.optInt("pingji");
			                park.salePrice = aDict.optInt("salePrice");
			                park.phone = aDict.optString("tell");
			                park.openTime = aDict.optString("openTime");
			                park.address = aDict.optString("address");
			                contents.add(park);
					}
					if (page == 0) {
						loaedParks.clear();
					}
					loaedParks.addAll(contents);
					page++;
					postMessage(SHOW_SKIINGPARK_LIST);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_SKIINGPARK_LIST:
			refreshView.onHeaderRefreshComplete();
			refreshView.onFooterRefreshComplete();
			if (adapter == null) {
				adapter = new LSSkiingParkAdapter(this, loaedParks);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		loadMore();
		
	}

	private void loadMore() {
		if (page >= totalPage) {
			refreshView.onFooterRefreshComplete();
			return;
		}
		loadData();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refresh();
	}

	private void refresh() {
		page = 0;
		totalPage = 0;
		loadData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			return;

		default:
			break;
		}
		super.onClick(v);
	}
}

