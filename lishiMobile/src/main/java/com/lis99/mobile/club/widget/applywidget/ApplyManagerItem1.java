package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManagerItem1 extends MyBaseAdapter {



    public ApplyManagerItem1(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.apply_manager_list_item1, null );
            holder = new Holder();

            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_value = (TextView) view.findViewById(R.id.tv_value);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }



        return view;
    }


    class Holder
    {
        TextView tv_name, tv_value;
    }

}
