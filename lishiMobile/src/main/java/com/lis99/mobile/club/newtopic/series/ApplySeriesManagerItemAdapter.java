package com.lis99.mobile.club.newtopic.series;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xiaxaio on 2016.10.31.
 */
public class ApplySeriesManagerItemAdapter extends MyBaseAdapter {



    public ApplySeriesManagerItemAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.apply_manager_list_item_orderitem, null );
            holder = new Holder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

//        HashMap<String, String> map = (HashMap<String, String>) getItem(i);

        holder.applyInfoTitle_tv.setText("hello");
        holder.realName_tv.setText("hello");
        holder.sex_tv.setText("hello");
        holder.phoneNumber_tv.setText("hello");
        holder.IDNumber_tv.setText("hello");


        return view;
    }


    class Holder
    {
        TextView applyInfoTitle_tv;
        TextView realName_tv;
        TextView sex_tv;
        TextView phoneNumber_tv;
        TextView IDNumber_tv;

        public  Holder(View view) {
            applyInfoTitle_tv = (TextView) view.findViewById(R.id.order_info_applynumber_tv);
            realName_tv = (TextView) view.findViewById(R.id.order_info_realname_tv);
            sex_tv = (TextView) view.findViewById(R.id.order_info_sex_tv);
            phoneNumber_tv = (TextView) view.findViewById(R.id.order_info_phone_tv);
            IDNumber_tv = (TextView) view.findViewById(R.id.order_info_idnumber_tv);
        }
    }

}
