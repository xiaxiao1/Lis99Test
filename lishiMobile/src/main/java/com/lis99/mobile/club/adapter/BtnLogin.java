package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.lis99.mobile.R;

/**
 * Created by yy on 15/8/10.
 */
public class BtnLogin extends Button {

    private Context mContext;

    public BtnLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( event.getAction() == MotionEvent.ACTION_DOWN )
        {
            this.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
        }
        else if ( event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL )
        {
            this.setTextColor(mContext.getResources().getColor(R.color.club_login_color));
        }

        return super.onTouchEvent(event);
    }
}
