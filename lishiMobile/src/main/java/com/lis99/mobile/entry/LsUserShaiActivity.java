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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ShaiDateBean;
import com.lis99.mobile.application.data.ShaiYearBean;
import com.lis99.mobile.application.data.ShaituDateBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeGridView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.HashMap;
import java.util.List;

public class LsUserShaiActivity extends ActivityPattern {

	ImageView iv_back;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	ListView lv_year;
	String userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_shai);
		StatusUtil.setStatusBar(this);
		userid = getIntent().getStringExtra("id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserShaiList();
	}
	private void getUserShaiList() {
		String url;
		if(userid!=null && !"".equals(userid)){
			url = C.DAREN_SHAITU_URL+userid+"/0";
		}else{
			url = C.USER_SHAITU_URL+"0";
		}
		
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
	List<ShaiYearBean> shaiyears;
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				if(userid!=null && !"".equals(userid)){
					shaiyears = (List<ShaiYearBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_DAREN_SHAITU_LIST);
				}else{
					shaiyears = (List<ShaiYearBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_SHAITU_LIST);
				}
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
			YearAdapter ya =new YearAdapter();
			lv_year.setAdapter(ya);
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	
	private static class YearHolder{
		TextView tv_year;
		AutoResizeListView lv_date;
	}
	
	private class YearAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public YearAdapter() {
			inflater = LayoutInflater.from(LsUserShaiActivity.this);
		}

		@Override
		public int getCount() {
			return shaiyears.size();
		}

		@Override
		public Object getItem(int arg0) {
			return shaiyears.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			YearHolder holder;
			final int pos = position;
			ShaiYearBean sy = shaiyears.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_shaitu_year_item, null);
				holder = new YearHolder();
				holder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
				holder.lv_date = (AutoResizeListView) convertView.findViewById(R.id.lv_date);
				convertView.setTag(holder);
			} else {
				holder = (YearHolder) convertView.getTag();
			}
			holder.tv_year.setText(sy.getYear()+"年");
			DateAdapter da = new DateAdapter(sy.getDateBean());
			holder.lv_date.setAdapter(da);
			return convertView;
		}
		
	}
	
	private static class DateHolder{
		AutoResizeGridView gv_date;
	}
	
	private class DateAdapter extends BaseAdapter {
		
		LayoutInflater inflater;
		List<ShaiDateBean> lists;

		public DateAdapter(List<ShaiDateBean> ls) {
			inflater = LayoutInflater.from(LsUserShaiActivity.this);
			lists = ls;
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
			DateHolder holder;
			final int pos = position;
			ShaiDateBean sdb = lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_shaitu_date_item, null);
				holder = new DateHolder();
				holder.gv_date =  (AutoResizeGridView) convertView.findViewById(R.id.gv_date);
				convertView.setTag(holder);
			} else {
				holder = (DateHolder) convertView.getTag();
			}
			String mm = sdb.getDate().substring(4, 6);
			String dd = sdb.getDate().substring(6, 8);
			DateItemAdapter dia = new DateItemAdapter(sdb.getDateBean(), mm, dd);
			holder.gv_date.setAdapter(dia);
			//LsUserShaiActivity.this.setAdapterHeightBasedOnChildren2(holder.gv_date);
			return convertView;
		}
		
	}
	
	
	private static class DateItemHolder{
		LinearLayout ll_date,ll_image;
		TextView tv_month,tv_date;
		AsyncLoadImageView iv_image;
	}
	
	private class DateItemAdapter extends BaseAdapter {
		
		LayoutInflater inflater;
		List<ShaituDateBean> listItems;
		String month;
		String date;

		public DateItemAdapter(List<ShaituDateBean> ls,String m,String d) {
			inflater = LayoutInflater.from(LsUserShaiActivity.this);
			listItems = ls;
			month =m;
			date = d;
		}

		@Override
		public int getCount() {
			return listItems.size()+1;
		}

		@Override
		public Object getItem(int arg0) {
			return listItems.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DateItemHolder holder;
			final int pos = position;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_shaitu_item, null);
				holder = new DateItemHolder();
				holder.ll_date = (LinearLayout) convertView.findViewById(R.id.ll_date);
				holder.ll_image = (LinearLayout) convertView.findViewById(R.id.ll_image);
				holder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.iv_image = (AsyncLoadImageView) convertView.findViewById(R.id.iv_image);
				convertView.setTag(holder);
			} else {
				holder = (DateItemHolder) convertView.getTag();
			}
			if(position ==0){
				holder.ll_date.setVisibility(View.VISIBLE);
				holder.ll_image.setVisibility(View.GONE);
				holder.tv_month.setText(Integer.parseInt(month)+"月");
				holder.tv_date.setText(String.valueOf(Integer.parseInt(date)));
			}else{
				holder.ll_image.setVisibility(View.VISIBLE);
				holder.ll_date.setVisibility(View.GONE);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.iv_image.getLayoutParams();
				ll.width = StringUtil.getXY(LsUserShaiActivity.this)[0] / 5 ;
				ll.height = ll.width + 10;
				holder.iv_image.setLayoutParams(ll);
				holder.iv_image.setImage(listItems.get(position-1).getImage_url(), null, null);
				holder.iv_image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(LsUserShaiActivity.this, LsShaiDetailActivity.class);
						intent.putExtra("id", listItems.get(pos-1).getId());
						LsUserShaiActivity.this.startActivity(intent);
					}
				});
				
			}
			
			return convertView;
		}
		
	}
	
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		lv_year = (ListView) findViewById(R.id.lv_year);
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
