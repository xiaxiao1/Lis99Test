package com.lis99.mobile.entry;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DarenBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.SlidingMenuView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.List;

public class LsDarenActivity extends ActivityPattern implements GestureDetector.OnGestureListener, OnItemClickListener{

	ImageView iv_back;
	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;// 左边布局
	private LinearLayout layout_right;// 右边布局
	LayoutInflater mInflater;
	private GestureDetector mGestureDetector;// 手势
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private int window_width;// 屏幕的宽度
	private ListView lv_daren_list;
	private List<DarenBean>  darenlist;
	DarenListAdapter adapter;
	int offset = 0;
	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 10;
	private SlidingMenuView menu_view;
	private final static int sleep_time = 5;
	private RelativeLayout rl_shai_title;
	private static final int SHOW_DAREN_LIST = 200;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_daren);

		StatusUtil.setStatusBar(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		mInflater = getLayoutInflater();
		init();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getDarenList(offset);
	}
	
	private void getDarenList(int offset2) {
		String url = C.DAREN_LISTS_URL + offset2;
		Task task = new Task(null, url, null, "DAREN_LISTS_URL", this);
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
				if (((String) task.getParameter()).equals("DAREN_LISTS_URL")) {
					parserDarenList(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	private void parserDarenList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				darenlist = (List<DarenBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_DAREN_LISTS);
				postMessage(SHOW_DAREN_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}		
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_DAREN_LIST:
			adapter = new DarenListAdapter();
			lv_daren_list.setAdapter(adapter);
			lv_daren_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					
				}
			});
			break;
		}
		return true;
	}

	int down = 0;
	
	int alpha = 0;
	View ls_daren_ap_bg;
	int lx =0;
	private void init() {
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		rl_shai_title = (RelativeLayout) findViewById(R.id.rl_shai_title);
		lv_daren_list = (ListView) findViewById(R.id.lv_daren_list);
		ls_daren_ap_bg = findViewById(R.id.ls_daren_ap_bg);
		lx = StringUtil.getXY(this)[0];
		layout_right.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eventaction = event.getAction();
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
						.getLayoutParams();
				int x = (int) event.getRawX();
				int y = (int) event.getRawY();
				int p = (int) event.getX();
				int q = (int) event.getY();
		 
		 
				switch (eventaction) {
		 
				case MotionEvent.ACTION_DOWN: 
					down = x;
					break;
				case MotionEvent.ACTION_MOVE: 
					int values = x - down;
					down = x;
					if(alpha>=0 && alpha<=80){
						if(values>0){
							alpha = alpha -5;
						}else if(values<0){
							alpha = alpha +5;
						}
					}
					if(alpha<0) alpha = 0;
					ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_daren_ap_bg.invalidate();
			// 右移动
			if (values > 0) {
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values, -MAX_WIDTH);
			} else {
				// 左移动
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						- values, 0);
			}
			layout_right.setLayoutParams(layoutParams);
					break;
				case MotionEvent.ACTION_UP:
					// 缩回去
					if (layoutParams.rightMargin < -window_width / 2) {
						new AsynMove().execute(SPEED);
					} else {
						new AsynMove().execute(-SPEED);
					}
					down = 0;
					
					break;
				}
				return false;
			}
		});
		layout_left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eventaction = event.getAction();
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
						.getLayoutParams();
				int x = (int) event.getRawX();
				int y = (int) event.getRawY();
				int p = (int) event.getX();
				int q = (int) event.getY();
		 
		 
				switch (eventaction) {
		 
				case MotionEvent.ACTION_DOWN: 
					down = x;
					break;
				case MotionEvent.ACTION_MOVE: 
					int values = x - down;
					down = x;
					if(alpha>=0 && alpha<=80){
						if(values>0){
							alpha = alpha -5;
						}else if(values<0){
							alpha = alpha +5;
						}
					}
					if(alpha<0) alpha = 0;
					ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_daren_ap_bg.invalidate();
					
			// 右移动
			if (values > 0) {
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values, -MAX_WIDTH);
			} else {
				// 左移动
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						- values, 0);
			}
			layout_right.setLayoutParams(layoutParams);
					break;
				case MotionEvent.ACTION_UP:
					// 缩回去
					if (layoutParams.rightMargin < -window_width / 2) {
						new AsynMove().execute(SPEED);
					} else {
						new AsynMove().execute(-SPEED);
					}
					down = 0;
					
					break;
				}
		 
				return false;
			}
		});
		layout_left.setLongClickable(true);
		layout_right.setLongClickable(true);
		mGestureDetector = new GestureDetector(this);
		// 禁用长按监听
		mGestureDetector.setIsLongpressEnabled(false);
		menu_view = (SlidingMenuView) findViewById(R.id.ls_slidingmenu_view);
		getMAX_WIDTH();
	}
	
	private static class ViewHolder{
		AsyncLoadImageView iv_head;
		TextView nickname,title,bt_tiwen;
	}
	
	
	private class DarenListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DarenListAdapter() {
			inflater = LayoutInflater.from(LsDarenActivity.this);
		}

		@Override
		public int getCount() {
			return darenlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return darenlist.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			DarenBean cb = darenlist.get(position);
			final int pos = position;
			final String userid = cb.getUser_id();
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_daren_list_item, null);
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
			holder.iv_head.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsDarenActivity.this, LsDarenDetailActivity.class);
					intent.putExtra("userid", userid);
					LsDarenActivity.this.startActivity(intent);
				}
			});
			holder.nickname.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsDarenActivity.this, LsDarenDetailActivity.class);
					intent.putExtra("userid", userid);
					LsDarenActivity.this.startActivity(intent);
				}
			});
			holder.bt_tiwen.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsDarenActivity.this, LsDarenTiwenActivity.class);
					intent.putExtra("id", darenlist.get(pos).getUser_id());
					intent.putExtra("nickname", darenlist.get(pos).getNickname());
					startActivity(intent);
				}
			});
			return convertView;
		}
		
	}

	
	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values[0], -MAX_WIDTH);
				if(alpha>=0 && alpha<=80){
					if(values[0]>0){
						alpha = alpha -5;
					}else if(values[0]<0){
						alpha = alpha +5;
					}
				}
				if(alpha<0) alpha = 0;
				ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
				ls_daren_ap_bg.invalidate();
				if(layoutParams.rightMargin == -MAX_WIDTH){
					alpha=0;
					ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_daren_ap_bg.invalidate();
					rl_shai_title.setVisibility(View.INVISIBLE);
				}
				
			} else {
				// 左移动
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						- values[0], 0);
				if(alpha>=0 && alpha<=80){
					if(values[0]>0){
						alpha = alpha -5;
					}else if(values[0]<0){
						alpha = alpha +5;
					}
				}
				if(alpha<0) alpha = 0;
				ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
				ls_daren_ap_bg.invalidate();
				if(layoutParams.rightMargin == 0){
					alpha=80;
					ls_daren_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_daren_ap_bg.invalidate();
					rl_shai_title.setVisibility(View.VISIBLE);
				}
			}
			layout_right.setLayoutParams(layoutParams);

		}

	}
	/***
	 * 获取移动距离 移动的距离其实就是layout_Right的宽度
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_right.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_left.getWidth()-(StringUtil.getXY(LsDarenActivity.this)[0]/8);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
							.getLayoutParams();
					RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					
					
					// 注意： 设置layout_right的宽度。防止被在移动的时候控件被挤压
					layoutParams.width = window_width;
					layoutParams.rightMargin = -MAX_WIDTH;
					layout_right.setLayoutParams(layoutParams);

					
						layoutParams_1.leftMargin = 0;
						layout_left.setLayoutParams(layoutParams_1);
					
					hasMeasured = true;
				}
				return true;
			}
		});

	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
