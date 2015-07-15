package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DarenHuidaBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.HuidaBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;

import java.util.List;

import java.util.List;

public class LsDarenHuidaActivity extends ActivityPattern {

	ImageView iv_back;
	TextView tv_title;
	String userid;
	AutoResizeListView ls_huida_lv;
	private static final int SHOW_COMMENTLIST = 203;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_daren_huida);

		StatusUtil.setStatusBar(this);
		userid = getIntent().getStringExtra("id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getCommentList(userid);
	}
	private void getCommentList(String id) {
		String url = C.DAREN_HUIDA_URL +id+"/0";
		Task task = new Task(null, url, null, "MAIN_COMMENTLIST_URL", this);
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
				if(((String) task.getParameter()).equals("MAIN_COMMENTLIST_URL")){
					parserCommentList(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	DarenHuidaBean dhb;
	List<HuidaBean> lists;
	private void parserCommentList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				dhb = (DarenHuidaBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_DAREN_HUIDA);
				lists = dhb.getLists();
				postMessage(SHOW_COMMENTLIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_COMMENTLIST:
			tv_title.setText(dhb.getUser().getNickname()+"的回答");
			adapter = new DayiListAdapter();
			ls_huida_lv.setAdapter(adapter);
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	private static class DayiHolder{
		TextView time;
		TextView item_title;
		TextView item_zan,item_content;
	}
	DayiListAdapter adapter;
	private class DayiListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DayiListAdapter() {
			inflater = LayoutInflater.from(LsDarenHuidaActivity.this);
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DayiHolder holder;
			HuidaBean cb = lists.get(position);
			final int pos = position;
			final String wenid = cb.getWenda_id();
			final String aid = cb.getId();
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_daren_huida_item, null);
					holder=new DayiHolder();
					holder.time = (TextView) convertView.findViewById(R.id.time);
					holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
					holder.item_zan = (TextView) convertView.findViewById(R.id.item_zan);
					holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
					convertView.setTag(holder);
			}else{
				holder=(DayiHolder) convertView.getTag();
			}
			holder.time.setText(cb.getCreate_time());
			holder.item_title.setText(cb.getTitle());
			holder.item_zan.setText(cb.getLikenum());
			holder.item_content.setText(cb.getContent());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsDarenHuidaActivity.this, LsAnswerDetailActivity.class);
					intent.putExtra("ask_id", wenid);
					intent.putExtra("answer_id", aid);
					LsDarenHuidaActivity.this.startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
	private void setView() {
		// TODO Auto-generated method stub
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title  = (TextView) findViewById(R.id.tv_title);
		ls_huida_lv =  (AutoResizeListView) findViewById(R.id.ls_huida_lv);
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}
	}
}
