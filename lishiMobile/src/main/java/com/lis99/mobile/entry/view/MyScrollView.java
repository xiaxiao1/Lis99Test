package com.lis99.mobile.entry.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{
//	//到顶部， 不能滚动， 下拉刷新, 解决Scroll弹跳问题
//	private boolean canScroll;
//	public void setCanScroll (boolean b )
//	{
//		canScroll = b;
//	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
//		if ( !canScroll )
//		{
//			scrollTo(0, 0);
//			if(onScrollListener != null)
//				onScrollListener.onAutoScroll(0, 0, 0, 0);
//			return;
//		}
		if(onScrollListener != null)
			onScrollListener.onAutoScroll(l, t, oldl, oldt);
	}

	private static final String tag = "MyScrollView";
	private Handler handler;
	private View view;

	public MyScrollView(Context context) {
		super(context);

	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	// 这个获得总的高度
	public int computeVerticalScrollRange() {
		return super.computeHorizontalScrollRange();
	}

	public int computeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}

	private void init() {

		this.setOnTouchListener(onTouchListener);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					if (view.getMeasuredHeight() - 20 <= getScrollY()
							+ getHeight()) {
						if (onScrollListener != null) {
							onScrollListener.onBottom();
						}

					} else if (getScrollY() == 0) {
						if (onScrollListener != null) {
							onScrollListener.onTop();
						}
					} else {
						if (onScrollListener != null) {
							onScrollListener.onScroll();
						}
					}
					break;
				default:
					break;
				}
			}
		};

	}

	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				if (view != null && onScrollListener != null) {
					handler.sendMessageDelayed(handler.obtainMessage(1), 200);
				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	/**
	 * 获得参考的View，主要是为了获得它的MeasuredHeight，然后和滚动条的ScrollY+getHeight作比较。
	 */
	public void getView() {
		this.view = getChildAt(0);
		if (view != null) {
			init();
		}
	}

	/**
	 * 定义接口
	 * 
	 * @author admin
	 * 
	 */
	public interface OnScrollListener {
		void onBottom();

		void onTop();

		void onScroll();

		void onAutoScroll(int l, int t, int oldl, int oldt);
	}
	@Override
    public boolean canScrollVertically(int direction) {
        return true;
    }
	private OnScrollListener onScrollListener;

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

}
