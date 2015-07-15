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
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.WendaNewBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;

import java.util.List;

import java.util.List;

public class LsCateListActivity extends ActivityPattern {

	
	ImageView iv_back;
	AutoResizeListView ls_cate_lv;
	TextView tv_cate_title,tv_title;
	String cateType = "";
	private static final int SHOW_NEW_LIST = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_cate_lst);

		StatusUtil.setStatusBar(this);
		cateType = getIntent().getStringExtra("type");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getList();
	}
	private void getList() {
		String url = C.WENDA_CATES_LIST_URL+cateType+"/0";
		Task task = new Task(null, url, null, "WENDA_CATES_LIST_URL", this);
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
				if(((String) task.getParameter()).equals("WENDA_CATES_LIST_URL")){
					parserList(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	List<WendaNewBean> list;
	private void parserList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				list = (List<WendaNewBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_CATES_LIST);
				postMessage(SHOW_NEW_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
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
		case SHOW_NEW_LIST:
			adapter = new WenListAdapter();
			ls_cate_lv.setAdapter(adapter);
			ls_cate_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsCateListActivity.this, LsAskDetailActivity.class);
					intent.putExtra("ask_id", list.get(arg2).getId());
					LsCateListActivity.this.startActivity(intent);
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
			inflater = LayoutInflater.from(LsCateListActivity.this);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			WendaNewBean cb = list.get(position);
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
			holder.tv_newwen_cate.setText(cb.getCate());
			holder.tv_newwen_answernum.setText(cb.getAnswernum());
			holder.tv_newwen_date.setText(cb.getDate());
			return convertView;
		}
		
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_cate_title = (TextView) findViewById(R.id.tv_cate_title);
		tv_title = (TextView) findViewById(R.id.tv_title);
		if("3".equals(cateType)){
			tv_cate_title.setText("与\"跑步装备\"相关的问题");
			tv_title.setText("\"跑步装备\"相关问题");
		}else if("5".equals(cateType)){
			tv_cate_title.setText("与\"徒步装备\"相关的问题");
			tv_title.setText("\"徒步装备\"相关问题");
		}else if("2".equals(cateType)){
			tv_cate_title.setText("与\"登山装备\"相关的问题");
			tv_title.setText("\"登山装备\"相关问题");
		}else if("4".equals(cateType)){
			tv_cate_title.setText("与\"滑雪装备\"相关的问题");
			tv_title.setText("\"滑雪装备\"相关问题");
		}else if("6".equals(cateType)){
			tv_cate_title.setText("与\"攀岩装备\"相关的问题");
			tv_title.setText("\"攀岩装备\"相关问题");
		}else if("7".equals(cateType)){
			tv_cate_title.setText("与\"骑行装备\"相关的问题");
			tv_title.setText("\"骑行装备\"相关问题");
		}else if("9".equals(cateType)){
			tv_cate_title.setText("与\"自驾露营装备\"相关的问题");
			tv_title.setText("\"自驾露营装备\"相关问题");
		}else if("1".equals(cateType)){
			tv_cate_title.setText("与\"其他装备\"相关的问题");
			tv_title.setText("\"其他装备\"相关问题");
		}
		ls_cate_lv = (AutoResizeListView) findViewById(R.id.ls_cate_lv);
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
