package com.lis99.mobile.entry;

import android.content.Intent;
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

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DarenBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;
import java.util.List;

import java.util.HashMap;
import java.util.List;

public class LsUserAttentionActivity extends ActivityPattern {

	ImageView iv_back;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	ListView lv_attention;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_attention);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserShaiList();
	}
	private void getUserShaiList() {
		String url = C.USER_DAREN_URL+"0";
		Task task = new Task(null, url, null, "USER_DAREN_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getLoginParams() {
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
				if (((String) task.getParameter()).equals("USER_DAREN_URL")) {
					parserZhuangbeiList(result);
				}
			}
			break;
		default:
			break;
		}
	}
	List<DarenBean> listDaren1;
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				listDaren1 = (List<DarenBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_DAREN);
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	DarenListAdapter adapter;
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			adapter = new DarenListAdapter();
			lv_attention.setAdapter(adapter);
			lv_attention.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					adapter  = new DarenListAdapter();
					
				}
			});
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	private static class ViewHolder{
		AsyncLoadImageView iv_head;
		TextView nickname,title,bt_tiwen;
	}
private class DarenListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DarenListAdapter() {
			inflater = LayoutInflater.from(LsUserAttentionActivity.this);
		}

		@Override
		public int getCount() {
			return listDaren1.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listDaren1.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			DarenBean cb = listDaren1.get(position);
			final String userid = cb.getUser_id();
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_attention_list_item, null);
					holder=new ViewHolder();
					holder.bt_tiwen = (TextView) convertView.findViewById(R.id.bt_tiwen);
					holder.title = (TextView) convertView.findViewById(R.id.title);
					holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
					holder.iv_head = (AsyncLoadImageView) convertView.findViewById(R.id.iv_head);
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.title.setText(cb.getTitle());
			holder.nickname.setText(cb.getNickname());
			holder.iv_head.setImage(cb.getHeadicon(), null, null,"zhuangbei_detail");
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsUserAttentionActivity.this, LsDarenDetailActivity.class);
					intent.putExtra("userid", userid);
					LsUserAttentionActivity.this.startActivity(intent);					
				}
			});
			
			return convertView;
		}
		
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_attention = (ListView) findViewById(R.id.lv_attention);
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
