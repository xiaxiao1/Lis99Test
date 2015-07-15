package com.lis99.mobile.entry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class AutoResizeGridView extends GridView {

	public AutoResizeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoResizeGridView(Context context) {
		super(context);
	}

	public AutoResizeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
