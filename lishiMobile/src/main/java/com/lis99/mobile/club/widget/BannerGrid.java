package com.lis99.mobile.club.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;

/**
 * Created by yy on 16/7/5.
 */
public class BannerGrid extends RelativeLayout {

    private Context mContext;
    public ViewPager mViewPager;
    private LinearLayout mIndicateLayout;


    public BannerGrid(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BannerGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init ()
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.view_banner, this);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPage);
        mIndicateLayout = (LinearLayout) rootView
                .findViewById(R.id.indicateLayout);
    }


}
