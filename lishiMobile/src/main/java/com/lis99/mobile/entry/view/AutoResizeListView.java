package com.lis99.mobile.entry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutoResizeListView extends ListView{
	
	public AutoResizeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoResizeListView(Context context) {
		super(context);
	}

	public AutoResizeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
