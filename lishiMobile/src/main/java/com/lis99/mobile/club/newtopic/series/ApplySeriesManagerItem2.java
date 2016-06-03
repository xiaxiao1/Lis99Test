package com.lis99.mobile.club.newtopic.series;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.applywidget.ApplyManagerItem1;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplySeriesManagerItem2 extends MyBaseAdapter {

    private int TITLE = 0;

    private int INFO = 1;

    public ApplySeriesManagerItem2(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);

        if ( o instanceof String )
        {
            return TITLE;
        }
        else
        {
            return INFO;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;

        int state = getItemViewType(i);
        if ( state == TITLE )
        {
            if ( view == null )
            {
                view = View.inflate(mContext, R.layout.apply_manager_list_item3, null);
                holder = new Holder();

                holder.tv_name = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(holder);
            }
            else
            {
                holder = (Holder) view.getTag();
            }
            String title = (String) getItem(i);
            holder.tv_name.setText(title);

        }
        else
        {
            if ( view == null )
            {
                view = View.inflate(mContext, R.layout.apply_manager_list_item2, null );
                holder = new Holder();

                holder.list = (ListView) view.findViewById(R.id.list);

                view.setTag(holder);
            }
            else
            {
                holder = (Holder) view.getTag();
            }

            ArrayList<HashMap<String, String>> ilist = (ArrayList<HashMap<String, String>>) getItem(i);

            ApplySeriesManagerItem1 adapter = new ApplySeriesManagerItem1(mContext, ilist);

            holder.list.setAdapter(adapter);

        }

        return view;
    }


    class Holder
    {
        TextView tv_name, tv_value;
        ListView list;
    }

}
