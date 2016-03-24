package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by zhangjie on 3/24/16.
 */
public class LSWrapGridView extends GridView {

    public LSWrapGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LSWrapGridView(Context context) {
        super(context);
    }

    public LSWrapGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
