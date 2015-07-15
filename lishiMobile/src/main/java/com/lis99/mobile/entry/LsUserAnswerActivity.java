package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserAnswerBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;
import java.util.List;

import java.util.HashMap;
import java.util.List;

public class LsUserAnswerActivity extends ActivityPattern {

	ImageView iv_back;
	ListView lv_answer;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_answer);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserShaiList();
	}
	private void getUserShaiList() {
		String url = C.USER_ANSWER_URL+"0";
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
		task.setPostData(getSendParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getSendParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}
			}
			break;
		default:
			break;
		}
	}
	List<UserAnswerBean> asblist;
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				asblist = (List<UserAnswerBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_ANSWER);
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	private static class DayiHolder{
		TextView item_time;
		TextView item_title;
		TextView item_zan,item_content;
	}
	DayiListAdapter adapter;
private class DayiListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DayiListAdapter() {
			inflater = LayoutInflater.from(LsUserAnswerActivity.this);
		}

		@Override
		public int getCount() {
			return asblist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return asblist.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DayiHolder holder;
			UserAnswerBean cb = asblist.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_user_answer_item, null);
					holder=new DayiHolder();
					holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
					holder.item_zan = (TextView) convertView.findViewById(R.id.item_zan);
					holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
					holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
					convertView.setTag(holder);
			}else{
				holder=(DayiHolder) convertView.getTag();
			}
			holder.item_title.setText(cb.getTitle());
			holder.item_zan.setText(cb.getLikenum());
			holder.item_content.setText(cb.getContent());
			holder.item_time.setText(cb.getCreate_time());
			return convertView;
		}
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			adapter = new DayiListAdapter();
			lv_answer.setAdapter(adapter);
			lv_answer.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsUserAnswerActivity.this, LsAnswerDetailActivity.class);
					intent.putExtra("ask_id", asblist.get(arg2).getWenda_id());
					intent.putExtra("answer_id", asblist.get(arg2).getAnswer_id());
					LsUserAnswerActivity.this.startActivity(intent);
				}
			});
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_answer = (ListView) findViewById(R.id.lv_answer);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}
	}}
