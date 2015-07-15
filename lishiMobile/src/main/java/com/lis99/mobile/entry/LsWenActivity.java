package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.WenDayiBean;
import com.lis99.mobile.application.data.WendaNewBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.entry.view.CopyOfMyLinerLayout;
import com.lis99.mobile.entry.view.scroll.PullToRefreshBase.OnRefreshListener;
import com.lis99.mobile.entry.view.scroll.PullToRefreshScrollView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LsWenActivity extends ActivityPattern implements OnRefreshListener{

	ImageView iv_home,iv_list;
	private PullToRefreshScrollView refreshContainer;
	private ScrollView refreshScrollView;
	private AutoResizeListView ls_wen_lv;
	Button bt_new,bt_hot,bt_dayi;
	RelativeLayout bt_tiwen;
	WenListAdapter adapter;
	WenHotListAdapter adapter_hot;
	DayiListAdapter adapter_dayi;
	int hot_pos,dayi_pos,new_pos;
	LinearLayout ls_more;
	ProgressBar pb_load_progress;
	TextView tv_load_more;
	private static final int SHOW_NEW_LIST = 200;
	private static final int SHOW_HOTEST_LIST = 201;
	private static final int SHOW_DARENDAYI_LIST = 202;
	private static final int SHOW_EEROR = 203;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_wen);
		StatusUtil.setStatusBar(this);
		iv_home = (ImageView) findViewById(R.id.iv_home);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getWenNewList(new_pos);
	}
	
	private void getWenNewList(int offerset) {
		String url = C.WENDA_LATEST_URL+offerset;
		Task task = new Task(null, url, null, "WENDA_LATEST_URL", this);
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
				if(((String) task.getParameter()).equals("WENDA_LATEST_URL")){
					parserWenNewList(result);
				}else if(((String) task.getParameter()).equals("WENDA_HOTEST_URL")){
					parserWenHotList(result);
				}else if(((String) task.getParameter()).equals("WENDA_DARENDAYI_URL")){
					parserDayiList(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	private void parserDayiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				listDayi.addAll((List<WenDayiBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_DAYI_LIST));
				postMessage(SHOW_DARENDAYI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}		
	}

	private void parserWenHotList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				listHot.addAll((List<WendaNewBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_LATEST));
				postMessage(SHOW_HOTEST_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	List<WendaNewBean> listNew = new ArrayList<WendaNewBean>();
	List<WendaNewBean> listHot = new ArrayList<WendaNewBean>();
	List<WenDayiBean> listDayi  = new ArrayList<WenDayiBean>();
	private void parserWenNewList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				listNew.addAll((List<WendaNewBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_LATEST));
				postMessage(SHOW_NEW_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		refreshContainer.onRefreshComplete();
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_NEW_LIST:
			ls_wen_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsWenActivity.this, LsAskDetailActivity.class);
					intent.putExtra("ask_id", listNew.get(arg2).getId());
					LsWenActivity.this.startActivity(intent);
				}
			});
			if(listNew !=null && listNew.size()>0){
			//	if(adapter == null){
					adapter = new WenListAdapter();
					ls_wen_lv.setAdapter(adapter);
			//	}else{
			//		adapter.notifyDataSetChanged();
			//	}
			}else{
				
			}
			ls_more.setVisibility(View.VISIBLE);
			pb_load_progress.setVisibility(View.GONE);
			tv_load_more.setText("加载更多");
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_HOTEST_LIST:
			ls_wen_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsWenActivity.this, LsAskDetailActivity.class);
					intent.putExtra("ask_id", listHot.get(arg2).getId());
					LsWenActivity.this.startActivity(intent);
				}
			});
			if(listHot !=null && listHot.size()>0){
				//if(adapter_hot == null){
					adapter_hot = new WenHotListAdapter();
					ls_wen_lv.setAdapter(adapter_hot);
				//}else{
				//	adapter_hot.notifyDataSetChanged();
				//}
			}else{
				
			}
			ls_more.setVisibility(View.VISIBLE);
			pb_load_progress.setVisibility(View.GONE);
			tv_load_more.setText("加载更多");
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_DARENDAYI_LIST:
			ls_wen_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsWenActivity.this, LsAnswerDetailActivity.class);
					intent.putExtra("ask_id", listDayi.get(arg2).getWenda_id());
					intent.putExtra("answer_id", listDayi.get(arg2).getId());
					LsWenActivity.this.startActivity(intent);
				}
			});
			if(listDayi !=null && listDayi.size()>0){
				//if(adapter_dayi == null){
					adapter_dayi = new DayiListAdapter();
					ls_wen_lv.setAdapter(adapter_dayi);
				//}else{
				//	adapter_dayi.notifyDataSetChanged();
				//}
			}else{
				
			}
			ls_more.setVisibility(View.VISIBLE);
			pb_load_progress.setVisibility(View.GONE);
			tv_load_more.setText("加载更多");
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_EEROR:
			ls_more.setVisibility(View.VISIBLE);
			pb_load_progress.setVisibility(View.GONE);
			tv_load_more.setText("加载更多");
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	
	private void setListener() {
		refreshContainer.setOnRefreshListener(this);
		iv_home.setOnClickListener(this);
		bt_new.setOnClickListener(this);
		bt_hot.setOnClickListener(this);
		bt_dayi.setOnClickListener(this);
		bt_tiwen.setOnClickListener(this);
		iv_list.setOnClickListener(this);
		ls_more.setOnClickListener(this);
	}
	protected void loadMoreData() {
		if("1".equals(type)){
			new_pos += 20;
			getWenNewList(new_pos);
		}else if("2".equals(type)){
			hot_pos += 20;
			getWenHotList(hot_pos);
		}else if("3".equals(type)){
			dayi_pos += 20;
			getDayiList(dayi_pos);
		}
	}
	private int lastItem;
	View view;
	private void setView() {
		bt_new = (Button) findViewById(R.id.bt_new);
		bt_hot = (Button) findViewById(R.id.bt_hot);
		bt_dayi = (Button) findViewById(R.id.bt_dayi);
		bt_tiwen =  (RelativeLayout) findViewById(R.id.bt_tiwen);
		iv_list = (ImageView) findViewById(R.id.iv_list);
		refreshContainer = (PullToRefreshScrollView) this.findViewById(R.id.refreshContainer);
		refreshContainer.setMinimumHeight(StringUtil.getXY(this)[1]);
		refreshScrollView = refreshContainer.getRefreshableView();
		refreshContainer.setBackgroundColor(0xffdedede);
		refreshScrollView.setBackgroundColor(0xffdedede);
		view = LayoutInflater.from(this).inflate(R.layout.ls_wen_main, null);
		ls_wen_lv = (AutoResizeListView) view.findViewById(R.id.ls_wen_lv);
		ls_more = (LinearLayout) view.findViewById(R.id.ls_more);
		pb_load_progress = (ProgressBar) view.findViewById(R.id.pb_load_progress);
		tv_load_more = (TextView) view.findViewById(R.id.tv_load_more);
		refreshScrollView.addView(view);
		
	}
	String type = "1";
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_home.getId()){
			finish();
		}else if(v.getId() == bt_new.getId()){
			type = "1";
			bt_new.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_selected);
			bt_new.setTextColor(0xffffffff);
			bt_hot.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_middle_unselected);
			bt_hot.setTextColor(0xff2acbc2);
			bt_dayi.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_unselected);
			bt_dayi.setTextColor(0xff2acbc2);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			listNew.clear();
			new_pos = 0;
			getWenNewList(new_pos);
		}else if(v.getId() == bt_hot.getId()){
			type = "2";
			bt_new.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_unselected);
			bt_new.setTextColor(0xff2acbc2);
			bt_hot.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_middle_selected);
			bt_hot.setTextColor(0xffffffff);
			bt_dayi.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_unselected);
			bt_dayi.setTextColor(0xff2acbc2);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			listHot.clear();
			hot_pos = 0;
			getWenHotList(hot_pos);
		}else if(v.getId() == bt_dayi.getId()){
			type = "3";
			bt_new.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_unselected);
			bt_new.setTextColor(0xff2acbc2);
			bt_hot.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_middle_unselected);
			bt_hot.setTextColor(0xff2acbc2);
			bt_dayi.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_selected);
			bt_dayi.setTextColor(0xffffffff);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			listDayi.clear();
			dayi_pos = 0;
			getDayiList(dayi_pos);
		}else if(v.getId() == bt_tiwen.getId()){
			
			if(DataManager.getInstance().getUser().getUser_id()!=null&&!"".equals(DataManager.getInstance().getUser().getUser_id())){
				Intent intent = new Intent(this, LsTiwenActivity.class);
				startActivity(intent);
			}else{
				postMessage(POPUP_TOAST,"请先登录");
				Intent intent = new Intent(this, LSLoginActivity.class);
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
			}
		}else if(v.getId() == iv_list.getId()){
			Intent intent = new Intent(this, LsWenCateActivity.class);
			startActivity(intent);
			
		} else if(v.getId() == ls_more.getId()){
			pb_load_progress.setVisibility(View.VISIBLE);
			tv_load_more.setText("正在加载...");
			loadMoreData();
		}
	}
	
	private void getDayiList(int offerset) {
		String url = C.WENDA_DARENDAYI_URL+offerset;
		Task task = new Task(null, url, null, "WENDA_DARENDAYI_URL", this);
		publishTask(task, IEvent.IO);		
	}

	private void getWenHotList(int offerset) {
		String url = C.WENDA_HOTEST_URL+offerset;
		Task task = new Task(null, url, null, "WENDA_HOTEST_URL", this);
		publishTask(task, IEvent.IO);		
	}

	private static class ViewHolder{
		TextView tv_newwen_title,tv_newwen_cate,tv_newwen_answernum,tv_newwen_date;
		CopyOfMyLinerLayout ll_images;
		GridView gv_pic;
	}
	private static class ViewPicHolder{
		AsyncLoadImageView iv_pic;
	}
	private static class DayiHolder{
		AsyncLoadImageView item_head;
		TextView item_name;
		ImageView item_vip;
		TextView item_title;
		TextView item_zan,item_content;
	}
	
	private class WenListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public WenListAdapter() {
			inflater = LayoutInflater.from(LsWenActivity.this);
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
					holder.gv_pic = (GridView) convertView.findViewById(R.id.gv_pic);
					holder.ll_images =  (CopyOfMyLinerLayout) convertView.findViewById(R.id.ll_images);
					if(cb.getImages() !=null && cb.getImages().size()>0){
						holder.gv_pic.setAdapter(new WenPicAdapter(listNew,pos));
					}
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			final View view = convertView;
			final int id = convertView.getId();
			holder.tv_newwen_title.setText(cb.getTitle());
			holder.tv_newwen_cate.setText(cb.getCate());
			holder.tv_newwen_answernum.setText(cb.getAnswernum());
			holder.tv_newwen_date.setText(cb.getDate());
			holder.ll_images.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_UP)
			        {
						ls_wen_lv.performItemClick(view, pos, id);
			        }
					return false;
				}
			});
			return convertView;
		}
		
	}
	
	private class WenPicAdapter extends BaseAdapter {
		
		LayoutInflater inflater;
		List<String>   strList;

		public WenPicAdapter(List<WendaNewBean> list, int   po) {
			inflater = LayoutInflater.from(LsWenActivity.this);
			strList = list.get(po).getImages() ;
		}

		@Override
		public int getCount() {
			return strList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return strList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewPicHolder holder;
			String cb = strList.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_wen_pic_item, null);
					holder=new ViewPicHolder();
					holder.iv_pic = (AsyncLoadImageView) convertView.findViewById(R.id.iv_pic);
					convertView.setTag(holder);
			}else{
				holder=(ViewPicHolder) convertView.getTag();
			}
				holder.iv_pic.setImage(cb, null, null);
				
			return convertView;
		}
		
	}
	
	private class WenHotListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public WenHotListAdapter() {
			inflater = LayoutInflater.from(LsWenActivity.this);
		}

		@Override
		public int getCount() {
			return listHot.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listHot.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			WendaNewBean cb = listHot.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_wen_list_item, null);
					holder=new ViewHolder();
					holder.tv_newwen_title = (TextView) convertView.findViewById(R.id.tv_newwen_title);
					holder.tv_newwen_cate = (TextView) convertView.findViewById(R.id.tv_newwen_cate);
					holder.tv_newwen_answernum = (TextView) convertView.findViewById(R.id.tv_newwen_answernum);
					holder.tv_newwen_date = (TextView) convertView.findViewById(R.id.tv_newwen_date);
					holder.gv_pic = (GridView) convertView.findViewById(R.id.gv_pic);
					holder.ll_images =  (CopyOfMyLinerLayout) convertView.findViewById(R.id.ll_images);
					if(cb.getImages() !=null && cb.getImages().size()>0){
						holder.gv_pic.setAdapter(new WenPicAdapter(listHot,pos));
					}
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			final View view = convertView;
			final int id = convertView.getId();
			holder.tv_newwen_title.setText(cb.getTitle());
			holder.tv_newwen_cate.setText(cb.getCate());
			holder.tv_newwen_answernum.setText(cb.getAnswernum());
			holder.tv_newwen_date.setText(cb.getDate());
			holder.ll_images.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_UP)
			        {
						ls_wen_lv.performItemClick(view, pos, id);
			        }
					return false;
				}
			});
			return convertView;
		}
		
	}
	
	private class DayiListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DayiListAdapter() {
			inflater = LayoutInflater.from(LsWenActivity.this);
		}

		@Override
		public int getCount() {
			return listDayi.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listDayi.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DayiHolder holder;
			WenDayiBean cb = listDayi.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_wen_dayi_item, null);
					holder=new DayiHolder();
					holder.item_head = (AsyncLoadImageView) convertView.findViewById(R.id.item_head);
					holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
					holder.item_vip = (ImageView) convertView.findViewById(R.id.item_vip);
					holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
					holder.item_zan = (TextView) convertView.findViewById(R.id.item_zan);
					holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
					if("1".equals(cb.getIs_vip())){
						holder.item_vip.setVisibility(View.VISIBLE);
					}else{
						holder.item_vip.setVisibility(View.GONE);
					}
					convertView.setTag(holder);
			}else{
				holder=(DayiHolder) convertView.getTag();
			}
			holder.item_head.setImage(cb.getHeadicon(), null, null,"zhuangbei_detail");
			holder.item_name.setText(cb.getNickname());
			holder.item_title.setText(cb.getTitle());
			holder.item_zan.setText(cb.getLikenum());
			holder.item_content.setText(cb.getContent());
			return convertView;
		}
		
	}


	@Override
	public void onRefresh() {
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		if("1".equals(type)){
			listNew.clear();
			new_pos = 0;
			getWenNewList(new_pos);
		}else if("2".equals(type)){
			listHot.clear();
			hot_pos = 0;
			getWenHotList(hot_pos);
		}else if("3".equals(type)){
			listDayi.clear();
			dayi_pos = 0;
			getDayiList(dayi_pos);
		}else{
			refreshContainer.onRefreshComplete();
		}
	}
}
