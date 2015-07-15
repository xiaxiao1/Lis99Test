package com.lis99.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	// TODO Auto-generated method stub
    	super.onScrollChanged(l, t, oldl, oldt);
    	if ( osc != null ) osc.onScrollChanged(l, t, oldl, oldt);
    }
    
    private onScrollChang osc;
    public void setonScrollChang (onScrollChang osc)
    {
    	this.osc = osc;
    }
    public interface onScrollChang
    {
    	public void onScrollChanged (int l, int t, int oldl, int oldt);
    }

}