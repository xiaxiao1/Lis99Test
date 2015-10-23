package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/23.
 */
public class MyJoinAdapter extends MyBaseAdapter {


    public MyJoinAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.club_grid_item, null );



        }

        return null;
    }
}
