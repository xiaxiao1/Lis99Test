package com.lis99.mobile.entry;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ShaiTuBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.cptslide.ChapterAdapter;
import com.lis99.mobile.entry.view.ChapterToggleView;
import com.lis99.mobile.entry.view.SlidingMenuView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;
import com.lis99.mobile.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CopyOfLsShaiActivity extends ActivityPattern1 implements GestureDetector.OnGestureListener, OnItemClickListener{
	private ChapterToggleView mToggleView;
	ImageView iv_back,iv_shuaxin,iv_paizhao;
	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;// 左边布局
	private LinearLayout layout_right;// 右边布局
	private RelativeLayout ls_shaitu_content,rl_shai_title;
	LayoutInflater mInflater;
	/*AsyncLoadImageView ls_shaitu_iv1,ls_shaitu_iv2,ls_shaitu_iv3,ls_shaitu_iv4,ls_shaitu_iv5,
			ls_shaitu_iv6,ls_shaitu_iv7,ls_shaitu_iv8,ls_shaitu_iv9,ls_shaitu_iv11,ls_shaitu_iv110,ls_shaitu_iv120,ls_shaitu_iv130,ls_shaitu_iv140,ls_shaitu_iv150,
			ls_shaitu_iv160,ls_shaitu_iv170,ls_shaitu_iv180,ls_shaitu_iv190;*/
	LinearLayout ls_shai_bg_ll;
	int offset = 0;
	private static final int SHOW_SHAITU_LIST = 200;
	
	private GestureDetector mGestureDetector;// 手势
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private int window_width;// 屏幕的宽度
	
	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 */
	private final static int SPEED = 10;
	private SlidingMenuView menu_view;
	private final static int sleep_time = 5;
	
	private static final int SHOW_MAIN_BANNER = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_shai2);
		StatusUtil.setStatusBar(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_shuaxin = (ImageView) findViewById(R.id.iv_shuaxin);
		iv_paizhao = (ImageView) findViewById(R.id.iv_paizhao);
		ls_shaitu_content = (RelativeLayout) findViewById(R.id.ls_shaitu_content);
		iv_back.setOnClickListener(this);
		iv_shuaxin.setOnClickListener(this);
		iv_paizhao.setOnClickListener(this);
		mInflater = getLayoutInflater();
		init();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getShaituList(offset);
	}
	
	private void getShaituList(int offset2) {
		String url = C.SHAITU_LISTS_URL + offset2 ;
		Task task = new Task(null, url, null, "SHAITU_LISTS_URL", this);
		task.setPostData(getShaituListParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getShaituListParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		String userid = DataManager.getInstance().getUser().getUser_id();
		if(userid==null || "".equals(userid)){
			userid ="0";
		}
		params.put("user_id", userid);
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
				if (((String) task.getParameter()).equals("SHAITU_LISTS_URL")) {
					parserShaituList(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	List<ShaiTuBean> sblists = new ArrayList<ShaiTuBean>();
	private void parserShaituList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				sblists.addAll((List<ShaiTuBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_SHAITU_LIST));
				postMessage(SHOW_SHAITU_LIST);
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
		case SHOW_SHAITU_LIST:
			if(offset <40){
				offset = offset + 10;
				getShaituList(offset);
			}else{
				sblists = sblists.subList(0, sblists.size() / 9 * 9);
				//String[] names = getResources().getStringArray(R.array.array_cpt_slide_name);
				ViewPager vp = (ViewPager) findViewById(R.id.cpt_view_pager);
				final LinearLayout indexContainer = (LinearLayout) findViewById(R.id.cpt_index_container);
				// Get names list, and Alloc for page.
				int len = sblists.size();
				final int pn = ChapterAdapter.CHAPTER_PAGE_NUM;
				final int loop = len / pn + (len % pn == 0 ? 0 : 1);
				Utils.logh("ChapterSlideActivity", "name length: " + len + " loop: " + loop);
				ArrayList<ArrayList<ShaiTuBean>> arrayLists = new ArrayList<ArrayList<ShaiTuBean>>();
				for(int i=0; i<loop; i++) {
					ArrayList<ShaiTuBean> list = new ArrayList<ShaiTuBean>();
					int base = i * pn;
					int rang = base + pn > len ? len : base + pn;
					for(int j=base+0; j<rang; j++) {
						list.add(sblists.get(j));
					}
					arrayLists.add(list);
				}
				// Set adapter for ViewPager, in order to slide.
				vp.setAdapter(new ChapterAdapter(getSupportFragmentManager(), arrayLists));
				vp.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageScrollStateChanged(int arg0) { }
			
					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
					@Override
					public void onPageSelected(int position) {
						//mToggleView.hiddenBars();
						/*for(int i=0; i<loop; i++) {
							indexContainer.getChildAt(i).setSelected(i == position?true:false);
						}*/
					}
				});
				// Bottom page select navigation
				/*if(loop > 1) {
					Utils.setVisible(indexContainer);
					for(int i=0; i<loop; i++) {
						ImageView focus;
						focus = new ImageView(this);
						focus.setBackgroundResource(R.drawable.shelf_circle_selector);
						indexContainer.addView(focus);
						focus.setSelected(i == 0?true:false);
					}
				} else {
					Utils.setInvisible(indexContainer);
				}*/
				//getFlowView();
				postMessage(DISMISS_PROGRESS);
			}
			
			/*FrameLayout fl = (FrameLayout) findViewById(R.id.ls_shai_fl1);
			ViewGroup.LayoutParams ll = fl.getLayoutParams();
			ll.width = StringUtil.getXY(this)[0];
			ll.height = StringUtil.getXY(this)[0];
			fl.setLayoutParams(ll);*/
			/*ls_shaitu_iv1.setImage(sblists.get(0).getImage_url(), null, null);
			ls_shaitu_iv2.setImage(sblists.get(1).getImage_url(), null, null);
			ls_shaitu_iv3.setImage(sblists.get(2).getImage_url(), null, null);
			ls_shaitu_iv4.setImage(sblists.get(3).getImage_url(), null, null);
			ls_shaitu_iv5.setImage(sblists.get(4).getImage_url(), null, null);
			ls_shaitu_iv6.setImage(sblists.get(5).getImage_url(), null, null);
			ls_shaitu_iv7.setImage(sblists.get(6).getImage_url(), null, null);
			ls_shaitu_iv8.setImage(sblists.get(7).getImage_url(), null, null);
			ls_shaitu_iv9.setImage(sblists.get(8).getImage_url(), null, null);
			ls_shaitu_iv11.setImage(sblists.get(9).getImage_url(), null, null);*/
			/*ls_shaitu_iv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
					intent.putExtra("id", sblists.get(0).getId());
					LsShaiActivity.this.startActivity(intent);
				}
			});
ls_shaitu_iv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
					intent.putExtra("id", sblists.get(0).getId());
					LsShaiActivity.this.startActivity(intent);
				}
			});
ls_shaitu_iv2.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(1).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(2).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv4.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(3).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv5.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(4).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv6.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(5).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv7.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(6).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv8.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(7).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv9.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(8).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});
ls_shaitu_iv11.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LsShaiActivity.this, LsShaiDetailActivity.class);
		intent.putExtra("id", sblists.get(9).getId());
		LsShaiActivity.this.startActivity(intent);
	}
});*/
			/*ls_shaitu_iv110.setImage(strs.get(0), null, null);
			ls_shaitu_iv120.setImage(strs.get(1), null, null);
			ls_shaitu_iv130.setImage(strs.get(2), null, null);
			ls_shaitu_iv140.setImage(strs.get(3), null, null);
			ls_shaitu_iv150.setImage(strs.get(4), null, null);
			ls_shaitu_iv160.setImage(strs.get(5), null, null);
			ls_shaitu_iv170.setImage(strs.get(6), null, null);
			ls_shaitu_iv180.setImage(strs.get(7), null, null);
			ls_shaitu_iv190.setImage(strs.get(8), null, null);*/
			break;
		}
		return true;
	}
	int alpha = 0;
	View ls_shai_ap_bg;
	int lx =0;
	private void init()
    {
		//左右移动
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		rl_shai_title = (RelativeLayout) findViewById(R.id.rl_shai_title);
		ls_shai_ap_bg = findViewById(R.id.ls_shai_ap_bg);
		lx = StringUtil.getXY(this)[0];
		
		/*layout_right.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		layout_left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});*/
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
					if(alpha>=0 && alpha<=180){
						if(values>0){
							alpha = alpha -5;
						}else if(values<0){
							alpha = alpha +5;
						}
					}
					if(alpha<0) alpha = 0;
					ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_shai_ap_bg.invalidate();
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
					if(alpha>=0 && alpha<=180){
						if(values>0){
							alpha = alpha -5;
						}else if(values<0){
							alpha = alpha +5;
						}
					}
					if(alpha<0) alpha = 0;
					ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_shai_ap_bg.invalidate();
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
		/*menu_view.setOnScrollListener(new OnScrollListener() {
			@Override
			public void doScroll(float distanceX) {
				doScrolling(distanceX);
			}

			@Override
			public void doLoosen() {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
						.getLayoutParams();
				// 缩回去
				if (layoutParams.rightMargin < -window_width / 2) {
					//new AsynMove().execute(-SPEED);
				} else {
					//new AsynMove().execute(SPEED);
				}
			}
		});*/
		/*ls_shaitu_scrollLayout = (MyScrollLayout) findViewById(R.id.ls_shaitu_scrollLayout);
		RelativeLayout.LayoutParams ll = (android.widget.RelativeLayout.LayoutParams) ls_shaitu_scrollLayout.getLayoutParams();
		ll.width = StringUtil.getXY(this)[0];
		ll.height = StringUtil.getXY(this)[0];
		ls_shaitu_scrollLayout.setLayoutParams(ll);*/
		getMAX_WIDTH();
    	//mScrollLayout = (MyScrollLayout) findViewById(R.id.scrollLayout); 	
    	/*ls_shaitu_iv1 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv1);
    	ls_shaitu_iv11 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv11);
		ls_shaitu_iv2 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv2);
		ls_shaitu_iv3 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv3);
		ls_shaitu_iv4 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv4);
		ls_shaitu_iv5 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv5);
		ls_shaitu_iv6 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv6);
		ls_shaitu_iv7 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv7);
		ls_shaitu_iv8 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv8);
		ls_shaitu_iv9 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv9);*/
		/*ls_shaitu_iv110 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv110);
		ls_shaitu_iv120 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv120);
		ls_shaitu_iv130 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv130);
		ls_shaitu_iv140 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv140);
		ls_shaitu_iv150 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv150);
		ls_shaitu_iv160 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv160);
		ls_shaitu_iv170 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv170);
		ls_shaitu_iv180 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv180);
		ls_shaitu_iv190 = (AsyncLoadImageView) findViewById(R.id.ls_shaitu_iv190);*/
		//ls_page_num = (TextView) findViewById(R.id.ls_page_num);
		ls_shai_bg_ll = (LinearLayout) findViewById(R.id.ls_shai_bg_ll);
    	/*RelativeLayout.LayoutParams rl = (android.widget.RelativeLayout.LayoutParams) ls_shaitu_scrollLayout.getLayoutParams();
		rl.width = StringUtil.getXY(this)[0];
		rl.height = StringUtil.getXY(this)[0];
		ls_shaitu_scrollLayout.setLayoutParams(rl);*/
		/*RelativeLayout.LayoutParams rl1 = (android.widget.RelativeLayout.LayoutParams) ls_shai_bg_ll.getLayoutParams();
		rl1.width = StringUtil.getXY(this)[0];
		rl1.height = StringUtil.getXY(this)[0];
		rl1.rightMargin =  - StringUtil.getXY(this)[0] + StringUtil.dip2px(this, 30);
		ls_shai_bg_ll.setLayoutParams(rl1);*/
    	//ls_shaitu_scrollLayout.SetOnViewChangeListener(this);
    	/*LayoutParams lp1 = (LayoutParams) ls_shaitu_iv1.getLayoutParams();
		lp1.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp1.height = lp1.width;
		ls_shaitu_iv1.setLayoutParams(lp1);
		LayoutParams lp2 = (LayoutParams) ls_shaitu_iv2.getLayoutParams();
		lp2.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp2.height = lp2.width;
		ls_shaitu_iv2.setLayoutParams(lp2);
		LayoutParams lp3 = (LayoutParams) ls_shaitu_iv3.getLayoutParams();
		lp3.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp3.height = lp3.width;
		ls_shaitu_iv3.setLayoutParams(lp3);
		LayoutParams lp4 = (LayoutParams) ls_shaitu_iv4.getLayoutParams();
		lp4.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp4.height = lp4.width;
		ls_shaitu_iv4.setLayoutParams(lp4);
		LayoutParams lp5 = (LayoutParams) ls_shaitu_iv5.getLayoutParams();
		lp5.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp5.height = lp5.width;
		ls_shaitu_iv5.setLayoutParams(lp5);
		LayoutParams lp6 = (LayoutParams) ls_shaitu_iv6.getLayoutParams();
		lp6.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp6.height = lp6.width;
		ls_shaitu_iv6.setLayoutParams(lp6);
		LayoutParams lp7 = (LayoutParams) ls_shaitu_iv7.getLayoutParams();
		lp7.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp7.height = lp7.width;
		ls_shaitu_iv7.setLayoutParams(lp7);
		LayoutParams lp8 = (LayoutParams) ls_shaitu_iv8.getLayoutParams();
		lp8.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp8.height = lp8.width;
		ls_shaitu_iv8.setLayoutParams(lp8);
		LayoutParams lp9 = (LayoutParams) ls_shaitu_iv9.getLayoutParams();
		lp9.width = StringUtil.getXY(this)[0] -StringUtil.dip2px(this, 20);
		lp9.height = lp9.width;
		ls_shaitu_iv9.setLayoutParams(lp9);*/
		
		
		
		
    }
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mToggleView.toggleBarView();
		return super.onPrepareOptionsMenu(menu);
	}
	private void getFlowView() {
		// Add flow buttons layout, and buttons click listener
		View buttons = View.inflate(this, R.layout.chapter_toggle_buttons, null);
		addContentView(buttons, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		View more = buttons.findViewById(R.id.cpt_right_more);
		mToggleView = new ChapterToggleView(buttons, more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mToggleView.hiddenBars();
				Toast.makeText(CopyOfLsShaiActivity.this, R.string.str_toast_click_anzai, Toast.LENGTH_SHORT).show();
			}
		});
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
				if(alpha>=0 && alpha<=180){
					if(values[0]>0){
						alpha = alpha -5;
					}else if(values[0]<0){
						alpha = alpha +5;
					}
				}
				if(alpha<0) alpha = 0;
				System.out.println("alpha="+alpha);
				ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
				ls_shai_ap_bg.invalidate();
				if(layoutParams.rightMargin == -MAX_WIDTH){
					alpha=0;
					ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_shai_ap_bg.invalidate();
					rl_shai_title.setVisibility(View.INVISIBLE);
					//ls_shaitu_scrollLayout.setTouchFlag(false);
				}
			} else {
				// 左移动
				layoutParams.rightMargin = Math.min(layoutParams.rightMargin
						- values[0], 0);
				if(alpha>=0 && alpha<=180){
					if(values[0]>0){
						alpha = alpha -5;
					}else if(values[0]<0){
						alpha = alpha +5;
					}
				}
				if(alpha<0) alpha = 0;
				System.out.println("alpha="+alpha);
				ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
				ls_shai_ap_bg.invalidate();
				if(layoutParams.rightMargin == 0){
					alpha=180;
					ls_shai_ap_bg.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					ls_shai_ap_bg.invalidate();
					rl_shai_title.setVisibility(View.VISIBLE);
					//ls_shaitu_scrollLayout.setTouchFlag(true);
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
					MAX_WIDTH = layout_left.getWidth()-(StringUtil.getXY(CopyOfLsShaiActivity.this)[0]/8);
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
		}else if(v.getId() == iv_shuaxin.getId()){
			offset = 0;
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			getShaituList(offset);
		}else if(v.getId() == iv_paizhao.getId()){
			if(DataManager.getInstance().getUser().getUser_id()!=null&&!"".equals(DataManager.getInstance().getUser().getUser_id())){
				setUserHead();
			}else{
				postMessage(POPUP_TOAST,"请先登录");
				Intent intent = new Intent(this, LSLoginActivity.class);
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
			}
			
		}
	}
	private void setUserHead() {
		postMessage(POPUP_DIALOG_LIST, "选择图片",
				R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(CopyOfLsShaiActivity.this);
							break;
						case 1:
							// 相册
							BitmapUtil.doPickPhotoFromGallery(CopyOfLsShaiActivity.this); 
							break;
						}
					}
				});
	}
	Bitmap bitmap;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode+"==="+resultCode);
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				Uri photo_uri = data.getData();
				bitmap = BitmapUtil.getThumbnail(photo_uri,CopyOfLsShaiActivity.this);
				PublicApp myApp = (PublicApp) getApplication();
				myApp.setBitmap(bitmap);
				Intent intent = new Intent(this,LsShaiUploadActivity.class);
				startActivity(intent);
				//BitmapUtil.doCropPhoto(bitmap, CopyOfLsShaiActivity.this);
				break;
			case C.PICKED_WITH_DATA:
				/*Bitmap bt = data.getParcelableExtra("data");
				PublicApp myApp = (PublicApp) getApplication();
				myApp.setBitmap(bt);
				Intent intent = new Intent(this,LsShaiUploadActivity.class);
				startActivity(intent);*/
				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bitmap =  BitmapUtil.getThumbnail(file,CopyOfLsShaiActivity.this);
				//BitmapUtil.doCropPhoto(bitmap, CopyOfLsShaiActivity.this);
				PublicApp myApp1 = (PublicApp) getApplication();
				myApp1.setBitmap(bitmap);
				Intent intent1 = new Intent(this,LsShaiUploadActivity.class);
				startActivity(intent1);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		if(view == 0){
			ls_page_num.setText("1 / 2");
		}else if(view == 1){
			ls_page_num.setText("2 / 2");
		}
	}*/

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	private int verticalMinDistance = 20;   
	private int minVelocity         = 0;
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {   
			  
	    } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {   
	  
	    }   
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		System.out.println(distanceX);
		System.out.println(distanceY);
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}
	
	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		layoutParams.rightMargin -= mScrollX;
		layoutParams_1.rightMargin = window_width + layoutParams.rightMargin;
		if (layoutParams.rightMargin >= 0) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.rightMargin = 0;
			layoutParams_1.rightMargin = window_width;

		} else if (layoutParams.rightMargin <= -MAX_WIDTH) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.rightMargin = -MAX_WIDTH;
			layoutParams_1.rightMargin = window_width - MAX_WIDTH;
		}
		layout_right.setLayoutParams(layoutParams);
		layout_left.setLayoutParams(layoutParams_1);
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
	
	int down = 0;
	
	
	@Override
	public void onChapterItemClick(int position, ShaiTuBean name) {
		//Toast.makeText(this, "Click " + (position + 1) + ": " + name, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, LsShaiDetailActivity.class);
		intent.putExtra("id", name.getId());
		startActivity(intent);
	}

	@Override
	public void onChapterSpaceClick() {
		//mToggleView.toggleBarView();
	}
	
}
