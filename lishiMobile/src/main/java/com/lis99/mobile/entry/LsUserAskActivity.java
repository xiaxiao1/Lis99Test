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
import com.lis99.mobile.application.data.WendaNewBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LsUserAskActivity extends ActivityPattern {

	ImageView iv_back;
	ListView lv_tiwen;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_ask);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserTiwenList();
	}
	private void getUserTiwenList() {
		String url = C.USER_QUESTION_URL+"0/0";
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
	List<WendaNewBean> listNew = new ArrayList<WendaNewBean>();
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				listNew.addAll((List<WendaNewBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_LATEST));
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			if(listNew !=null && listNew.size()>0){
					adapter = new WenListAdapter();
					lv_tiwen.setAdapter(adapter);
				}else{
					
				}
			lv_tiwen.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(LsUserAskActivity.this, LsAskDetailActivity.class);
						intent.putExtra("ask_id", listNew.get(arg2).getId());
						LsUserAskActivity.this.startActivity(intent);
					}
				});
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	WenListAdapter adapter;
	private static class ViewHolder{
		TextView tv_newwen_title,tv_newwen_cate,tv_newwen_answernum,tv_newwen_date;
	}	
	private class WenListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public WenListAdapter() {
			inflater = LayoutInflater.from(LsUserAskActivity.this);
		}

		@Override
		public int getCount() {
			return listNew.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listNew.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			WendaNewBean cb = listNew.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_wen_list_item, null);
					holder=new ViewHolder();
					holder.tv_newwen_title = (TextView) convertView.findViewById(R.id.tv_newwen_title);
					holder.tv_newwen_cate = (TextView) convertView.findViewById(R.id.tv_newwen_cate);
					holder.tv_newwen_answernum = (TextView) convertView.findViewById(R.id.tv_newwen_answernum);
					holder.tv_newwen_date = (TextView) convertView.findViewById(R.id.tv_newwen_date);
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.tv_newwen_title.setText(cb.getTitle());
			holder.tv_newwen_cate.setText(cb.getCate_title());
			holder.tv_newwen_answernum.setText(cb.getAnswernum());
			holder.tv_newwen_date.setText(cb.getDate());
			return convertView;
		}
		
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_tiwen = (ListView) findViewById(R.id.lv_tiwen);
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
