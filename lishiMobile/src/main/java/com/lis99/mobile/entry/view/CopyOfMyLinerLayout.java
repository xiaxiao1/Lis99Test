package com.lis99.mobile.entry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class CopyOfMyLinerLayout extends LinearLayout {

	public CopyOfMyLinerLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public CopyOfMyLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
        	return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE)
        {
            return false;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP)
        {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
	
	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("111==="+heightMeasureSpec);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		System.out.println("222==="+heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}*/

}
