package com.lis99.mobile.club.widget.applywidget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.MyJoinActiveDetailModel;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveDetailItem extends MyBaseAdapter {

    public MyJoinActiveDetailItem(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;

        if ( view == null  )
        {
            view = View.inflate(mContext, R.layout.my_join_active_detail_item, null);

            holder = new Holder();

            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        MyJoinActiveDetailModel.Apply_info item = (MyJoinActiveDetailModel.Apply_info) getItem(i);

        if (item == null ) return view;

        holder.tv_name.setText(item.name);


        return view;
    }

    class Holder
    {
        TextView tv_name;
    }
}
