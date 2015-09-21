package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSMyTopicAdapter;
import com.lis99.mobile.club.model.LSClubTopic;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;

import java.util.ArrayList;
import java.util.List;

public class LSClubMyTopicActivity extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
	
	ListView listView;
	LSMyTopicAdapter adapter;
	List<LSClubTopic> topics = new ArrayList<LSClubTopic>();
	private PullToRefreshView refreshView;
	int offset = 0;
	int totalNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsclub_my_topic);
		initViews();
		setTitle("我的发帖");
		loadTopicList();
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		listView = (ListView) findViewById(R.id.listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LSClubTopic topic = topics.get(position);
				Intent intent = new Intent(LSClubMyTopicActivity.this, LSClubTopicActivity.class);
				intent.putExtra("clubID", topic.getClub_id());
				intent.putExtra("topicID", topic.getId());
				intent.putExtra("clubName", topic.getClub_title());
				startActivity(intent);
			}
		});
		
		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
	}
	
	private void loadTopicList() {
		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.USER_GET_MYTOPICS + userID + "/" + offset;
		

		Task task = new Task(null, url, null, C.USER_GET_MYTOPICS, this);
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
				if (((String) task.getParameter()).equals(C.USER_GET_MYTOPICS)) {
					parserTopicInfo(result);
				} 
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	
	private void parserTopicInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				String msg = "没有内容";//root.get("data").asText();
				postMessage(ActivityPattern1.POPUP_TOAST, msg);
				return;
			}
			JsonNode data = root.get("data");
			totalNum = data.get("totalNum").asInt();
			List<LSClubTopic> temp = LSFragment.mapper.readValue(data.get("list").traverse(), new TypeReference<List<LSClubTopic>>() {
			});
			if (offset == 0) {
				topics.clear();
			}
			topics.addAll(temp);
			offset++; // = topics.size();
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
			if (adapter == null) {
				adapter = new LSMyTopicAdapter(this, topics);
				listView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		refreshView.onFooterRefreshComplete();
		if (topics.size() >= totalNum) {
			postMessage(POPUP_TOAST, "没有更多帖子");
		} else {
			loadTopicList();
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshView.onHeaderRefreshComplete();
		offset = 0;
		loadTopicList();
	}
	
}
