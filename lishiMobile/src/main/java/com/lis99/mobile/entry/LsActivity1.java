package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.ActivityLeaderPicBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.entry.view.scroll.PullToRefreshBase.OnRefreshListener;
import com.lis99.mobile.entry.view.scroll.PullToRefreshScrollView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LsActivity1 extends ActivityPattern implements OnRefreshListener {

	private PullToRefreshScrollView refreshContainer;
	private ScrollView refreshScrollView;
	private AutoResizeListView ls_lv;
	private ImageView ivback;
	LinearLayout ls_more;
	int offset = 0;
	int limits = 20;
	ActivityListAdapter adapter;
	List<ActivityLeaderPicBean> listActivity = new ArrayList<ActivityLeaderPicBean>();

	private static final int SHOW_ACTIVITY_LIST = 201;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_activity_listview);

		StatusUtil.setStatusBar(this);

		setView();
		setListener();
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getActivityList();
	}

	private void getActivityList() {
		// + offset2 + "/" + limits2
		String url = C.ACTIVITY_LEADER_URL ;
		Task task = new Task(null, url, null, "ACTIVITY_LEADER_URL", this);
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
				if (((String) task.getParameter())
						.equals("ACTIVITY_LEADER_URL")) {
					parserActivityList(result);
					break;
				}
			}
		default:
			break;
		}

	}

	List<ActivityLeaderPicBean> list;

	private void parserActivityList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				list = (List<ActivityLeaderPicBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_ACTIVTIY);
				postMessage(SHOW_ACTIVITY_LIST);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		refreshContainer.onRefreshComplete();
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ACTIVITY_LIST:
			refreshContainer.onRefreshComplete();
			listActivity.addAll(list);
			if (adapter == null) {
				adapter = new ActivityListAdapter();
				ls_lv.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	View view;

	private void setView() {
		ivback = (ImageView) findViewById(R.id.iv_back);
		refreshContainer = (PullToRefreshScrollView) this
				.findViewById(R.id.myrefreshContainer);
		refreshContainer.setMinimumHeight(StringUtil.getXY(this)[1]);
		refreshScrollView = refreshContainer.getRefreshableView();
		refreshContainer.setBackgroundColor(0xffdedede);
		refreshScrollView.setBackgroundColor(0xffdedede);

		view = LayoutInflater.from(LsActivity1.this).inflate(
				R.layout.ls_activity_lv_item, null);
		ls_lv = (AutoResizeListView) view.findViewById(R.id.ls_lv);

		refreshScrollView.addView(view);
	}

	private void setListener() {
		ivback.setOnClickListener(this);
		refreshContainer.setOnRefreshListener(this);
		ls_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Intent intent = new Intent(LsActivity1.this,
						LsActivityItem.class);
				intent.putExtra("huodong_url", listActivity.get(position)
						.getHuodong_url());
				startActivity(intent);
			}

		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ivback.getId()) {
			finish();
		}
	}

	private static class ViewHolder {
		AsyncLoadImageView lv_item_imageview;
		TextView lv_item_text;
	}

	private class ActivityListAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public ActivityListAdapter() {
			inflater = LayoutInflater.from(LsActivity1.this);
		}

		@Override
		public int getCount() {
			return listActivity.size();
		}

		@Override
		public Object getItem(int position) {
			return listActivity.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			ActivityLeaderPicBean bean = listActivity.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lls_activity_lv_item,
						null);
				holder = new ViewHolder();
				holder.lv_item_imageview = (AsyncLoadImageView) convertView
						.findViewById(R.id.lv_item_imageview);
				holder.lv_item_text = (TextView) convertView
						.findViewById(R.id.lv_item_text);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.lv_item_imageview
						.getLayoutParams();
				ll.width = StringUtil.getXY(LsActivity1.this)[0];
				ll.height = ll.width / 2;
				holder.lv_item_imageview.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.lv_item_imageview.setImage(bean.getImage_url(), null, null);
			holder.lv_item_text.setText(bean.getTitle());
			return convertView;
		}
	}

	@Override
	public void onRefresh() {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		// offset = 0;
		// limits = 20;
		listActivity.clear();
		getActivityList();
	}

}
