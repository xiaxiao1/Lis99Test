package com.lis99.mobile.entry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyLinerLayout extends LinearLayout {

	public MyLinerLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public MyLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	
	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("111==="+heightMeasureSpec);
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		System.out.println("222==="+heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}*/

}
