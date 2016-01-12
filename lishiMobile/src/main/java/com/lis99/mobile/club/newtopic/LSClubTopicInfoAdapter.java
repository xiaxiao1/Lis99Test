package com.lis99.mobile.club.newtopic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/11.
 */
public class LSClubTopicInfoAdapter extends MyBaseAdapter {


    public LSClubTopicInfoAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if ( view == null )
        {
            holder = new Holder();
            view = View.inflate(mContext, R.layout.ls_club_topic_list_adapter, null);
            holder.tv = (TextView) view.findViewById(R.id.tv);
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
        TextView tv;
    }

}
