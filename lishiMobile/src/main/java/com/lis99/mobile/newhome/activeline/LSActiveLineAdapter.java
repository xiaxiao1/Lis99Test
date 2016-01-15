package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/15.
 */
public class LSActiveLineAdapter extends MyBaseAdapter {

    private final int normal = 0;

    private final int ad = 1;

    private int num = 2;

    public LSActiveLineAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return num;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_line_item, null);
        }

        return null;
    }
}
