package com.lis99.mobile.entry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.myactivty.ItemInfoBean;
import com.lis99.mobile.myactivty.PhaseItem;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class LsActivityResult extends ActivityPattern1 {

	private View iv_back, iv_filter;
	private Button bt_bj, bt_xa, bt_sy, bt_cc;
	private ListView mlistview;
	private ItemInfoAdapter adapter;
	private ListView phaseListview;
	private PhaseInfoAdapter phaseAdapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	List<ItemInfoBean> list2 = new ArrayList<ItemInfoBean>();
	List<PhaseItem> phaseList = new ArrayList<PhaseItem>();
	int currentBtn = -1;
	Handler handler;
	SparseArray<Button> array = new SparseArray<Button>();
	private String[] areas = new String[] { "52", "311", "244", "211" };
	private static final int SHOW_EEROR = 213;
	private static final int SHOW_ITEM_INFO = 215;
	private static final int SHOW_PHASE_INFO = 216;
	private static final int LOAD_ITEM_INFO = 217;

	TextView phaseTitleView;

	int offset = 0;

	View filterPanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.xxl_activity_result);
		setContentView(R.layout.xxl_activity_result_ls);

		StatusUtil.setStatusBar(this);

		handler = new Handler();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		setview();

		this.onClick(bt_bj);
	}

	private void setview() {
		iv_back = findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		iv_filter = findViewById(R.id.iv_filter);
		iv_filter.setOnClickListener(this);

		filterPanel = findViewById(R.id.filterPanel);

		bt_bj = (Button) findViewById(R.id.bt_bj);
		bt_xa = (Button) findViewById(R.id.bt_xa);
		bt_sy = (Button) findViewById(R.id.bt_sy);
		bt_cc = (Button) findViewById(R.id.bt_cc);

		array.put(0, bt_bj);
		array.put(1, bt_xa);
		array.put(2, bt_sy);
		array.put(3, bt_cc);

		bt_bj.setOnClickListener(this);
		bt_xa.setOnClickListener(this);
		bt_sy.setOnClickListener(this);
		bt_cc.setOnClickListener(this);

		phaseTitleView = (TextView) findViewById(R.id.resultDate);

		mlistview = (ListView) findViewById(R.id.mlistview);

		mlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsActivityResult.this,
						LsActivityLines.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", list2.get(position).getId());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		adapter = new ItemInfoAdapter();
		mlistview.setAdapter(adapter);
		mlistview.setFocusable(false);

		phaseListview = (ListView) findViewById(R.id.phaseListview);
		phaseAdapter = new PhaseInfoAdapter();
		phaseListview.setAdapter(phaseAdapter);

		phaseListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PhaseItem item = phaseList.get(position);
				changePhase(item);
				phaseAdapter.notifyDataSetChanged();
				filterPanel.setVisibility(View.GONE);
			}
		});

	}

	private void changePhase(final PhaseItem item) {
		list2.clear();
		offset = item.getOffset();
		handler.post(new Runnable() {
			@Override
			public void run() {
				phaseTitleView.setText(item.getStage());
			}
		});

		getActivityLists(currentBtn);
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
				if ("ACTIVITY_RESULT_URL".equals(param)) {
					parserItemInfoList(result);
				} else if ("ACTIVITY_PHASE_URL".equals(param)) {
					parserPhaseItems(result);
					if (phaseList.size() > 0) {
						changePhase(phaseList.get(phaseList.size() - 1));
					} else {
						postMessage(DISMISS_PROGRESS);
					}
					// getActivityLists(currentBtn);
				}
			}
			break;
		default:
			break;
		}
	}

	private void parserItemInfoList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				List<ItemInfoBean> temp = ((List<ItemInfoBean>) DataManager
						.getInstance().jsonParse(params,
								DataManager.TYPE_ITEM_INFO));

				list2.addAll(temp);
				postMessage(SHOW_ITEM_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		}
		postMessage(DISMISS_PROGRESS);

	}

	private void parserPhaseItems(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				List<PhaseItem> temp = ((List<PhaseItem>) DataManager
						.getInstance().jsonParse(params,
								DataManager.TYPE_PHASE_INFO));

				phaseList.addAll(temp);
				postMessage(SHOW_PHASE_INFO);
			} else {
				postMessage(DISMISS_PROGRESS);
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	private void getPhaseList(int i) {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		String url = C.ACTIVITY_PHASE_URL + areas[i];
		Task task = new Task(null, url, null, "ACTIVITY_PHASE_URL", this);
		publishTask(task, IEvent.IO);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ITEM_INFO:
			adapter.notifyDataSetChanged();
			mlistview.post(new Runnable() {
				@Override
				public void run() {
					Utility.setListViewHeightBasedOnChildren(mlistview);
				}
			});
			break;
		case SHOW_PHASE_INFO:
			phaseAdapter.notifyDataSetChanged();
			break;
		case SHOW_EEROR:
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	private void changeColor(int position) {
		for (int i = 0; i < array.size(); i++) {
			if (i == position) {
				array.get(i).setBackgroundResource(R.drawable.button_press);
				array.get(i).setTextColor(
						getResources().getColor(R.color.white));
			} else {
				array.get(i).setBackgroundColor(
						getResources().getColor(R.color.white));
				array.get(i).setTextColor(
						getResources().getColor(R.color.common_black));
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == iv_filter.getId()) {
			if (filterPanel.getVisibility() == View.GONE) {
				filterPanel.setVisibility(View.VISIBLE);
			} else {
				filterPanel.setVisibility(View.GONE);
			}
		} else if (v.getId() == bt_bj.getId()) {
			if (currentBtn != 0) {
				changeColor(0);
				phaseList.clear();
				// list2.clear();
				currentBtn = 0;
				offset = 0;
				getPhaseList(0);
			}
		} else if (v.getId() == bt_xa.getId()) {
			if (currentBtn != 1) {
				changeColor(1);
				phaseList.clear();
				// list2.clear();
				currentBtn = 1;
				offset = 0;
				getPhaseList(1);
			}
		} else if (v.getId() == bt_sy.getId()) {
			if (currentBtn != 2) {
				changeColor(2);
				phaseList.clear();
				// list2.clear();
				currentBtn = 2;
				offset = 0;
				getPhaseList(2);
			}

		} else if (v.getId() == bt_cc.getId()) {
			if (currentBtn != 3) {
				changeColor(3);
				phaseList.clear();
				// list2.clear();
				currentBtn = 3;
				offset = 0;
				getPhaseList(3);
			}

		}
	}

	private void getActivityLists(int i) {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		String url = C.ACTIVITY_RESULT_URL + "?areaid=" + areas[i] + "&offset="
				+ offset;
		Task task = new Task(null, url, null, "ACTIVITY_RESULT_URL", this);
		publishTask(task, IEvent.IO);

	}

	private static class ViewHolder {
		ImageView iv_toux;
		TextView tv_zan, tv_number, tv_detail, tv_name;
	}

	private class PhaseInfoAdapter extends BaseAdapter {
		LayoutInflater inflater;

		public PhaseInfoAdapter() {
			inflater = LayoutInflater.from(LsActivityResult.this);
		}

		@Override
		public int getCount() {
			return phaseList.size();
		}

		@Override
		public Object getItem(int position) {
			return phaseList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.xxl_result_phase_item,
						null);

			}
			PhaseItem item = (PhaseItem) getItem(position);
			TextView t = (TextView) convertView.findViewById(R.id.phase);
			t.setText(item.getStage());

			View v = convertView.findViewById(R.id.check);
			if (offset == item.getOffset()) {
				v.setVisibility(View.VISIBLE);
				convertView.setBackgroundColor(Color.rgb(0xf7, 0xf7, 0xf7));
			} else {
				v.setVisibility(View.GONE);
				convertView.setBackgroundColor(Color.WHITE);
			}

			return convertView;
		}
	}

	private class ItemInfoAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public ItemInfoAdapter() {
			inflater = LayoutInflater.from(LsActivityResult.this);
		}

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return list2.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.xxl_activity, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView
						.findViewById(R.id.tv_number);
				holder.tv_detail = (TextView) convertView
						.findViewById(R.id.tv_detail);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_zan = (TextView) convertView
						.findViewById(R.id.tv_zan);
				holder.iv_toux = (ImageView) convertView
						.findViewById(R.id.iv_toux);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			imageLoader.displayImage(list2.get(position).getUserinfo()
					.getHeadicon(), holder.iv_toux, options);
			holder.tv_number.setText("第" + (position + 1) + "位 ");
			holder.tv_detail.setText(list2.get(position).getTitle());
			holder.tv_name.setText(list2.get(position).getUserinfo()
					.getNickname());
			holder.tv_zan.setText("人气值  " + list2.get(position).getRenqizhi());
			return convertView;
		}
	}
}
