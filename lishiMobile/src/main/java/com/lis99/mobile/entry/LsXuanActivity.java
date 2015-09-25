package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_AbsListView.OnScrollListener;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.application.data.ZhuanjiBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.entry.view.LazyScrollView;
import com.lis99.mobile.entry.view.MyLinerLayout;
import com.lis99.mobile.entry.view.scroll.PullToRefreshBase.OnRefreshListener;
import com.lis99.mobile.entry.view.scroll.PullToRefreshScrollView;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LsXuanActivity extends ActivityPattern  implements OnRefreshListener{

	ImageView iv_home;
	Button bt_dangjiliuxing,bt_remenzhuanji;
	int offset = 0;
	int limits = 40;
	int zhuanji_offset = 0;
	int zhuanji_limits = 20;
	List<ZhuangbeiBean> getlist;
	List<ZhuangbeiBean> dangji_lists = new ArrayList<ZhuangbeiBean>();
	List<ZhuanjiBean> zhuanji_lists = new ArrayList<ZhuanjiBean>();
	private LazyScrollView waterfall_scroll;
	private LinearLayout waterfall_container;
	private ArrayList<LinearLayout> waterfall_items;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	private static final int SHOW_ZHUANJI_LIST = 201;
	private static final int SHOW_ZHUANGBEI_CONTENT = 202;
	
	
	//private Button ls_more;
	
	private PullToRefreshScrollView refreshContainer;
	private ScrollView refreshScrollView;
	private AutoResizeListView ls_remen_lv;
	
	private com.huewu.pla.lib.MultiColumnListView mAdapterView = null;
	ZhuangbeiAdapter adapter;
	ZhuanjiAdapter zhuanji_adapter;
	MyLinerLayout ls_dangji_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_xuan);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getZhuangbeiList(offset,limits);
	}
	
	private void getZhuangbeiList(int offset2, int limits2) {
		String url = C.ZHUANGBEI_ITEM_URL + offset2 + "/" + limits2;
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
		publishTask(task, IEvent.IO);
	}
	private void getZhuanjiList(int offset2, int limits2) {
		String url = C.ZHUANGBEI_ALBUMS_URL + offset2 + "/" + limits2;
		Task task = new Task(null, url, null, "ZHUANGBEI_ALBUMS_URL", this);
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
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}else if(((String) task.getParameter()).equals("ZHUANGBEI_ALBUMS_URL")){
					parserZhuanjiList(result);
				}else if(((String) task.getParameter()).equals("ZHUANGBEI_DETAIL_URL")){
					parserZhuangbeiDetailList(result);
				}
			}
			break;
		default:
			break;
		}
		
		//refreshContainer.onRefreshComplete();
	}
	String content ;
	private void parserZhuangbeiDetailList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				content = (String) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANGBEI_DETAIL);
				postMessage(SHOW_ZHUANGBEI_CONTENT);
			}else{
				
			}
		}		
	}

	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				getlist = (List<ZhuangbeiBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANGBEI_LIST);
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				
			}
		}
		
	}
	List<ZhuanjiBean> zhuanjilist;
	private void parserZhuanjiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				zhuanjilist = (List<ZhuanjiBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_ZHUANJI_LIST);
				postMessage(SHOW_ZHUANJI_LIST);
			}else{
				
			}
		}
		
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			refreshContainer.onRefreshComplete();
			//List<ZhuangbeiBean> list = new ArrayList<ZhuangbeiBean>();
			dangji_lists.addAll(getlist);
			//initAdapter();
			if(adapter==null){
				adapter = new ZhuangbeiAdapter();
				mAdapterView.setAdapter(adapter);
			}else{
				adapter.notifyDataSetChanged();
			}
			postMessage(DISMISS_PROGRESS);
			//setAdapterHeightBasedOnChildren(mAdapterView);
			
			//refreshScrollView.addView(view);
			break;
		case SHOW_ZHUANJI_LIST:
			refreshContainer.onRefreshComplete();
			zhuanji_lists.addAll(zhuanjilist);
			if(zhuanji_adapter==null){
				zhuanji_adapter = new ZhuanjiAdapter();
				ls_remen_lv.setAdapter(zhuanji_adapter);
			}else{
				zhuanji_adapter.notifyDataSetChanged();
			}
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_ZHUANGBEI_CONTENT:
			
			break;
		}
		return true;
	}
	private Random mRand = new Random();
	private void initAdapter() {
		/*mAdapter = new MySimpleAdapter(this, R.layout.sample_item);

		for( int i = 0; i < 30; ++i){
			//generate 30 random items.

			StringBuilder builder = new StringBuilder();
			builder.append("Hello!![");
			builder.append(i);
			builder.append("] ");

			char[] chars = new char[mRand.nextInt(500)];
			Arrays.fill(chars, '1');
			builder.append(chars);
			mAdapter.add(builder.toString());
		}*/
		
	}
	View view;
	private void setView() {
		iv_home = (ImageView) findViewById(R.id.iv_home);
		bt_dangjiliuxing = (Button) findViewById(R.id.bt_dangjiliuxing);
		bt_remenzhuanji = (Button) findViewById(R.id.bt_remenzhuanji);
		refreshContainer = (PullToRefreshScrollView) this.findViewById(R.id.refreshContainer);
		refreshContainer.setMinimumHeight(StringUtil.getXY(this)[1]);
		refreshScrollView = refreshContainer.getRefreshableView();
		refreshContainer.setBackgroundColor(0xffdedede);
		refreshScrollView.setBackgroundColor(0xffdedede);
		view = LayoutInflater.from(this).inflate(R.layout.ls_xuan_dangjiliuxing, null);
		ls_remen_lv = (AutoResizeListView) view.findViewById(R.id.ls_remen_lv);
		ls_dangji_layout = (MyLinerLayout) view.findViewById(R.id.ls_dangji_layout);
		//ls_more = (Button) view.findViewById(R.id.ls_more);
		mAdapterView = (MultiColumnListView) view.findViewById(R.id.list);
		mAdapterView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(PLA_AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		refreshScrollView.addView(view);
	}
	
	private void setListener() {
		iv_home.setOnClickListener(this);
		bt_dangjiliuxing.setOnClickListener(this);
		bt_remenzhuanji.setOnClickListener(this);
		refreshContainer.setOnRefreshListener(this);
		mAdapterView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
//				Intent intent = new Intent(LsXuanActivity.this,LsZhuangbeiDetail.class);
				Intent intent = new Intent(LsXuanActivity.this,LSEquipInfoActivity.class);
				intent.putExtra("id", dangji_lists.get(position).getId());
				startActivity(intent);
			}
			
		});
		ls_remen_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Intent intent = new Intent(LsXuanActivity.this,LsZhuanjiDetail.class);
				intent.putExtra("id", zhuanji_lists.get(position).getId());
				startActivity(intent);
			}
			
		});
		//ls_more.setOnClickListener(this);
	}
	protected void getZhuangbeiDetail(String id) {
		// TODO Auto-generated method stub
		String url = C.ZHUANGBEI_ALBUM_DETAIL_URL + id;
		Task task = new Task(null, url, null, "ZHUANGBEI_DETAIL_URL", this);
		publishTask(task, IEvent.IO);
		
	}
	boolean remen_flag = false;
	String type = "1";
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_home.getId()){
			finish();
		}/*else if(v.getId() == ls_more.getId()){
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			if("1".equals(type)){
				offset = offset + limits;
				getZhuangbeiList(offset,limits);
			}else if("2".equals(type)){
				zhuanji_offset = zhuanji_offset + zhuanji_limits;
				getZhuanjiList(zhuanji_offset,zhuanji_limits);
			}
		}*/else if(v.getId() == bt_dangjiliuxing.getId()){
			type = "1";
			ls_remen_lv.setVisibility(View.GONE);
			mAdapterView.setVisibility(View.VISIBLE);
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_selected);
			bt_dangjiliuxing.setTextColor(0xffffffff);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_unselected);
			bt_remenzhuanji.setTextColor(0xff2acbc2);
		}else if(v.getId() == bt_remenzhuanji.getId()){
			type = "2";
			if(!remen_flag){
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				getZhuanjiList(zhuanji_offset,zhuanji_limits);
				remen_flag = true;
			}
			ls_remen_lv.setVisibility(View.VISIBLE);
			mAdapterView.setVisibility(View.GONE);
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_unselected);
			bt_dangjiliuxing.setTextColor(0xff2acbc2);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_selected);
			bt_remenzhuanji.setTextColor(0xffffffff);
		}
	}
	
	private static class ViewHolder{
		AsyncLoadImageView ls_zhuangbei_item_pic;
		TextView ls_zhuangbei_item_title,ls_zhuangbei_item_score;
		LinearLayout ls_zhuangbei_item_star;
	}
	
	private class ZhuangbeiAdapter extends ArrayAdapter {
		
		LayoutInflater inflater;

		public ZhuangbeiAdapter() {
			super(LsXuanActivity.this, R.layout.ls_xuan_zhuangbei_item, dangji_lists);
			inflater = LayoutInflater.from(LsXuanActivity.this);
		}

		@Override
		public int getCount() {
			return dangji_lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dangji_lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int pos = position;
			ZhuangbeiBean zb = dangji_lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_xuan_zhuangbei_item, null);
				holder = new ViewHolder();
				holder.ls_zhuangbei_item_pic = (AsyncLoadImageView) convertView.findViewById(R.id.ls_zhuangbei_item_pic);
				holder.ls_zhuangbei_item_title = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_title);
				holder.ls_zhuangbei_item_score = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_score);
				holder.ls_zhuangbei_item_star = (LinearLayout) convertView.findViewById(R.id.ls_zhuangbei_item_star);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuangbei_item_pic.getLayoutParams();
				ll.width = StringUtil.getXY(LsXuanActivity.this)[0] / 2 - StringUtil.dip2px(LsXuanActivity.this, 20);
				ll.height = ll.width ;
				holder.ls_zhuangbei_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ls_zhuangbei_item_pic.setImage(zb.getThumb(), null, null,true);
			holder.ls_zhuangbei_item_title.setText(zb.getTitle());
			holder.ls_zhuangbei_item_score.setText(zb.getScore());
			holder.ls_zhuangbei_item_star.removeAllViews();
			int score = (int)(Float.parseFloat(zb.getScore()));
			for(int i=0;i<score;i++){
				ImageView iv = new ImageView(LsXuanActivity.this);
				iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				iv.setLayoutParams(ll);
				holder.ls_zhuangbei_item_star.addView(iv);
			}
			return convertView;
		}
		
	}
	
	private static class ZhuanjiViewHolder{
		AsyncLoadImageView ls_zhuanji_item_pic;
		TextView ls_zhuanji_item_title;
	}
	
	private class ZhuanjiAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public ZhuanjiAdapter() {
			inflater = LayoutInflater.from(LsXuanActivity.this);
		}

		@Override
		public int getCount() {
			return zhuanji_lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return zhuanji_lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ZhuanjiViewHolder holder;
			final int pos = position;
			ZhuanjiBean zj = zhuanji_lists.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_xuan_zhuanji_item, null);
				holder = new ZhuanjiViewHolder();
				holder.ls_zhuanji_item_pic = (AsyncLoadImageView) convertView.findViewById(R.id.ls_zhuanji_item_pic);
				holder.ls_zhuanji_item_title = (TextView) convertView.findViewById(R.id.ls_zhuanji_item_title);
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) holder.ls_zhuanji_item_pic.getLayoutParams();
				ll.width = StringUtil.getXY(LsXuanActivity.this)[0];
				ll.height = ll.width / 2 ;
				holder.ls_zhuanji_item_pic.setLayoutParams(ll);
				convertView.setTag(holder);
			} else {
				holder = (ZhuanjiViewHolder) convertView.getTag();
			}
			holder.ls_zhuanji_item_pic.setImage(zj.getImage(), null, null);
			holder.ls_zhuanji_item_title.setText(zj.getTitle());
			return convertView;
		}
		
	}

	@Override
	public void onRefresh() {
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		if("1".equals(type)){
			offset = 0;
			limits = 40;
			dangji_lists.clear();
			getZhuangbeiList(offset,limits);
		}else if("2".equals(type)){
			zhuanji_offset = 0;
			zhuanji_limits = 20;
			zhuanji_lists.clear();
			getZhuanjiList(zhuanji_offset,zhuanji_limits);
		}
		
	}

}
