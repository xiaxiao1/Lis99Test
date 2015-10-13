package com.lis99.mobile.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by yy on 15/10/13.
 */
public class LSClubTopicNewActive extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private LayoutInflater inflater;

    private View v;

    public LSClubTopicNewActive(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        init();

    }

    private void init ()
    {
        inflater = LayoutInflater.from(mContext);
    }


    public void setModel ()
    {

    }


    @Override
    public void onClick(View view) {

    }
}
