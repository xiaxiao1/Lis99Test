package com.lis99.mobile.club.widget.applywidget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManagerItem1 extends MyBaseAdapter {



    public ApplyManagerItem1(Activity c, ArrayList listItem) {
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

        HashMap<String, String> map = (HashMap<String, String>) getItem(i);

        if ( map != null )
        {
            if ( map.containsKey("name"))
            {
                holder.tv_name.setText(map.get("name"));
            }
            if ( map.containsKey("value"))
            {
                holder.tv_value.setText(map.get("value"));
            }
        }


        return view;
    }


    class Holder
    {
        TextView tv_name, tv_value;
    }

}
