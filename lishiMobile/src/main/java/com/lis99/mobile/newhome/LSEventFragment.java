package com.lis99.mobile.newhome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.LsActivityDetail;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.ActivityLeaderPicBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsActivityItem;
import com.lis99.mobile.entry.LsActivityLines;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.myactivty.ItemInfoBean;
import com.lis99.mobile.util.C;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class LSEventFragment extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener,OnClickListener {

	private TextView leftBtn;
	private TextView rightBtn;
	private View selectedBtn;

	private PullToRefreshView refreshView;
	private ListView listView;
	private LsItemInfoAdapter lsAdapter;
	private LineItemInfoAdapter lineAdapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private int lsOffset = 0;
	private int lineOffset = 0;
	private final static int limit = 10;

	List<ActivityLeaderPicBean> lsActivitives = new ArrayList<ActivityLeaderPicBean>();
	List<ItemInfoBean> lineActivitives = new ArrayList<ItemInfoBean>();

	private static final int SHOW_LSACTIVITY_LIST = 5001;
	private static final int SHOW_LINEACTIVITY_LIST = 5002;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		getLsActivityLists();
	}
	
	@Override
	protected void initViews(ViewGroup container) {
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.activity_ls, container, false);
		
		leftBtn = (TextView) findViewById(R.id.lsBtn);
		rightBtn = (TextView) findViewById(R.id.lineBtn);

		selectedBtn = leftBtn;

		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);

		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		View backBtn = findViewById(R.id.iv_back);
		backBtn.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.listView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectedBtn == rightBtn) {
					Intent intent = new Intent(getActivity(),
							LsActivityLines.class);
					Bundle bundle = new Bundle();
					bundle.putInt("id", lineActivitives.get(position).getId());
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					ActivityLeaderPicBean activity = lsActivitives
							.get(position);
					String url = activity.getHuodong_url();
					if ("1".equals(url)) {
						Intent intent = new Intent(getActivity(),
								LsActivityItem.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(),
								LsActivityDetail.class);
						Bundle bundle = new Bundle();
						bundle.putInt("id", activity.getId());
						bundle.putString("url", activity.getHuodong_url());
						bundle.putString("title", activity.getTitle());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
		});

		
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			getLSActivity().toggleMenu();
			break;
		case R.id.lsBtn:
			if (selectedBtn != leftBtn) {
				leftBtn.setBackgroundResource(R.drawable.activity_leftbtn_sel);
				leftBtn.setTextColor(Color.WHITE);
				rightBtn.setBackgroundResource(R.drawable.activity_rightbtn);
				rightBtn.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
				selectedBtn = leftBtn;

				listView.setAdapter(lsAdapter);

				if (lsActivitives.size() == 0) {
					getLsActivityLists();
				}

			}
			break;

		case R.id.lineBtn:
			if (selectedBtn != rightBtn) {
				leftBtn.setBackgroundResource(R.drawable.activity_leftbtn);
				leftBtn.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
				rightBtn.setBackgroundResource(R.drawable.activity_rightbtn_sel);
				rightBtn.setTextColor(Color.WHITE);
				selectedBtn = rightBtn;

				listView.setAdapter(lineAdapter);

				if (lineActivitives.size() == 0) {
					getLineActivityLists();
				}

			}
			break;

		default:
			break;
		}
	}

	private void getLsActivityLists() {
		postMessage(ActivityPattern1.POPUP_PROGRESS, getString(R.string.sending));
		String url = C.ACTIVITY_LEADER_URL + "?offset=" + lsOffset;
		Task task = new Task(null, url, null, "ACTIVITY_LEADER_URL", this);
		publishTask(task, IEvent.IO);

	}

	private void parserLsActivityList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				lsActivitives = (List<ActivityLeaderPicBean>) DataManager
						.getInstance().jsonParse(params,
								DataManager.TYPE_ACTIVTIY);
				postMessage(SHOW_LSACTIVITY_LIST);
			}
		}
		postMessage(ActivityPattern1.DISMISS_PROGRESS);

	}

	private void parserLineActivitList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				List<ItemInfoBean> temp = ((List<ItemInfoBean>) DataManager
						.getInstance().jsonParse(params,
								DataManager.TYPE_ITEM_INFO));
				lineActivitives.addAll(temp);
				postMessage(SHOW_LINEACTIVITY_LIST);
			} else {
				postMessage(ActivityPattern1.POPUP_TOAST, result);
			}
		}
		postMessage(ActivityPattern1.DISMISS_PROGRESS);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		//super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				String param = (String) task.getParameter();
				if ("ACTIVITY_LEADER_URL".equals(param)) {
					parserLsActivityList(result);
				} else if ("ACTIVITY_LINE_URL".equals(param)) {
					parserLineActivitList(result);
				}
			}
		default:
			break;
		}

	}

	private void getLineActivityLists() {
		postMessage(ActivityPattern1.POPUP_PROGRESS, getString(R.string.sending));
		String url = C.ACTIVITY_ITEM_URL + "?areaid=all" + "&offset="
				+ lineOffset + "&limit=" + limit;
		Task task = new Task(null, url, null, "ACTIVITY_LINE_URL", this);
		publishTask(task, IEvent.IO);

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (selectedBtn == rightBtn) {
			getLineActivityLists();
		}
		refreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		if (selectedBtn == leftBtn) {
			lsActivitives.clear();
			lsOffset = 0;
			getLsActivityLists();
		} else {
			lineActivitives.clear();
			lineOffset = 0;
			getLineActivityLists();
		}
		refreshView.onHeaderRefreshComplete();
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_LSACTIVITY_LIST:
			if (lsAdapter == null) {
				lsAdapter = new LsItemInfoAdapter();
				listView.setAdapter(lsAdapter);
			} else {
				lsAdapter.notifyDataSetChanged();
			}
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return true;
		case SHOW_LINEACTIVITY_LIST:
			lineOffset++;
			if (lineAdapter == null) {
				lineAdapter = new LineItemInfoAdapter();
				listView.setAdapter(lineAdapter);
			} else {
				lineAdapter.notifyDataSetChanged();
				listView.post(new Runnable() {

					@Override
					public void run() {
						int first = listView.getFirstVisiblePosition();
						listView.setSelection(first + 4);
						listView.setSelected(true);

					}
				});
			}
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return true;
		}
		return false;
	}

	private static class LsViewHolder {
		ImageView imageView;
		TextView titleView, timeView;
	}

	private class LsItemInfoAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public LsItemInfoAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return lsActivitives.size();
		}

		@Override
		public Object getItem(int position) {
			return lsActivitives.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LsViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_activity_item, null);
				holder = new LsViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.image);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.title);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(holder);
			} else {
				holder = (LsViewHolder) convertView.getTag();
			}
			ActivityLeaderPicBean activity = lsActivitives.get(position);
			imageLoader.displayImage(activity.getImage_url(), holder.imageView,
					options);
			holder.titleView.setText(activity.getTitle());
			holder.timeView.setText("活动时间：" + activity.getStartDate() + "至"
					+ activity.getEndDate());
			return convertView;
		}
	}

	private class LineItemInfoAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public LineItemInfoAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return lineActivitives.size();
		}

		@Override
		public Object getItem(int position) {
			return lineActivitives.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LsViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.line_activity_item,
						null);
				holder = new LsViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.image);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.title);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(holder);
			} else {
				holder = (LsViewHolder) convertView.getTag();
			}
			ItemInfoBean activity = lineActivitives.get(position);
			imageLoader.displayImage(activity.getImageUrl(), holder.imageView,
					options);
			holder.titleView.setText(activity.getTitle());
			holder.timeView.setText("活动时间：" + activity.getStartDate() + "至"
					+ activity.getEndDate());
			return convertView;
		}
	}

}