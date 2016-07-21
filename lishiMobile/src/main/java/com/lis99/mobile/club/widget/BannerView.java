package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.util.Common;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends RelativeLayout {
	public static final int DEFAULT_INTERVAL = 5000;
	public static final int SCROLL_WHAT = 0;
	private long mInterval = DEFAULT_INTERVAL;
	private Handler mHandler;

	private Context mContext;
	public ViewPager mViewPager;
	private LinearLayout mIndicateLayout;
	private List<View> mIndicater;
	private boolean mIsBorderAnimation = false;// the border of viewppager has
												// animation
	private int[] dot;

//	设置原点
	public void setDot(int select, int nomal ) {
		dot = new int[2];
		dot[0] = select;
		dot[1] = nomal;
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView(context);
	}

	public BannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView(context);
	}

	public BannerView(Context context) {
		super(context);
		mContext = context;
		initView(context);
	}

	//	private float c_x;
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if ( event.getAction() == MotionEvent.ACTION_DOWN )
//		{
//			c_x = event.getX();
//		}
//		else if ( event.getAction() == MotionEvent.ACTION_MOVE )
//		{
//			if ( Math.abs(event.getX() - c_x) > 5 )
//			{
//				mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
//			}
//		}
//		else if ( event.getAction() == MotionEvent.ACTION_UP )
//		{
//			mViewPager.getParent().requestDisallowInterceptTouchEvent(false);
//		}
//
//		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
//	}
	
	/**
	 * set BorderAnimation
	 * 
	 * @param isBorderAnimation
	 */
	public void setBorderAnimation(Boolean isBorderAnimation) {
		mIsBorderAnimation = isBorderAnimation;
	}

	/**
	 * start auto scroll
	 */
	public void startAutoScroll() {
		startAutoScroll(DEFAULT_INTERVAL);
	}

	/**
	 * start auto scroll
	 * 
	 * @param delayTimeInMills
	 *            first scroll delay time
	 */
	public void startAutoScroll(int delayTimeInMills) {
		sendScrollMessage(delayTimeInMills);
	}

	/**
	 * onDestory：stop auto scroll
	 */
	public void stopAutoScroll() {
		mHandler.removeMessages(SCROLL_WHAT);
	}

	private void sendScrollMessage(long delayTimeInMills) {
		/** remove messages before, keeps one message is running at most **/
		mHandler.removeMessages(SCROLL_WHAT);
		mHandler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View rootView = inflater.inflate(R.layout.view_banner, this);
		mViewPager = (ViewPager) rootView.findViewById(R.id.viewPage);
		mIndicateLayout = (LinearLayout) rootView
				.findViewById(R.id.indicateLayout);
		mHandler = new PollingHandler();

		setDot(R.drawable.club_banner_dot_select, R.drawable.club_banner_dot);


	}

	public void setBannerAdapter(PagerAdapter pagerAdapter) {

		
		if (mIndicater != null) {
			for (View v : mIndicater) {
				mIndicateLayout.removeView(v);
			}
		}
		
		mViewPager.setAdapter(pagerAdapter);
		int count = pagerAdapter.getCount() - 2;
		mIndicater = new ArrayList<View>(count);
		for (int index = 0; index < count; index++) {
			// 添加Indicate
			View indicateView = new View(mContext);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					Common.dip2px(5), Common.dip2px(5));
			if (index != count - 1) {
				layoutParams.rightMargin = 10;
			}
			
			indicateView.setLayoutParams(layoutParams);
//			indicateView.setBackgroundColor(Color.WHITE);
			indicateView.setBackgroundResource(dot[1]);
			mIndicater.add(indicateView);
			mIndicateLayout.addView(indicateView);
		}
//		mIndicateLayout.getChildAt(0).setBackgroundColor(Color.rgb(0x2a, 0xcb, 0xc3));

		mIndicateLayout.getChildAt(0).setBackgroundResource(dot[0]);
		mViewPager.setOnPageChangeListener(new BannerChangeListener());
		mViewPager.setCurrentItem(1); // 设置当前pager为1，即开始时就可以向右活动
	}

	class BannerChangeListener implements OnPageChangeListener {
		private int mIndicaterOldPositon = 0;

		@Override
		public void onPageScrollStateChanged(int position) {
			Log.i("bannner", "onPageScrollStateChanged");
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
//			getParent().requestDisallowInterceptTouchEvent(true);
			Log.i("bannner", "onPageScrolled");
		}

		@Override
		public void onPageSelected(int position) {
			int indicaterPositon = -1;
			if (position == 0) {
				position = mViewPager.getAdapter().getCount() - 2;
				indicaterPositon = position - 1;
				updateIndicater(mIndicaterOldPositon, indicaterPositon);
				mIndicaterOldPositon = indicaterPositon;
				mViewPager.setCurrentItem(position, false);

			} else if (position == mViewPager.getAdapter().getCount() - 1) {
				indicaterPositon = 0;
				updateIndicater(mIndicaterOldPositon, indicaterPositon);
				mIndicaterOldPositon = indicaterPositon;
				mViewPager.setCurrentItem(1, false);

			} else {
				indicaterPositon = position - 1;
				updateIndicater(mIndicaterOldPositon, indicaterPositon);
				mIndicaterOldPositon = indicaterPositon;
			}

		}

	}

	private void updateIndicater(int indicaterOldPositon, int indicaterPositon) {
//		mIndicater.get(indicaterOldPositon).setBackgroundColor(Color.WHITE);
//		mIndicater.get(indicaterPositon).setBackgroundColor(Color.rgb(0x2a, 0xcb, 0xc3));
		mIndicater.get(indicaterOldPositon).setBackgroundResource(dot[1]);
		mIndicater.get(indicaterPositon).setBackgroundResource(dot[0]);
	}

	@SuppressLint("HandlerLeak")
	class PollingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCROLL_WHAT:
				scroll();
				sendScrollMessage(mInterval);
			default:
				break;
			}
		}
	}

	/**
	 * scroll
	 */
	private void scroll() {
		PagerAdapter adapter = mViewPager.getAdapter();
		int currentItem = mViewPager.getCurrentItem();
		int totalCount;
		if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
			return;
		}
		if (currentItem == totalCount - 1) {
			mViewPager.setCurrentItem(0, mIsBorderAnimation);

		} else {
			mViewPager.setCurrentItem(++currentItem, true);
		}

	}
}
