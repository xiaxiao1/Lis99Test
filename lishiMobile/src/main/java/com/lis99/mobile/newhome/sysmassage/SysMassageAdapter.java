package com.lis99.mobile.newhome.sysmassage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/28.
 */
public class SysMassageAdapter extends MyBaseAdapter{

    public SysMassageAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.sys_massage_item, null);
            holder = new Holder();
            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.tv_club_title = (TextView) view.findViewById(R.id.tv_club_title);
            holder.tv_data = (TextView) view.findViewById(R.id.tv_data);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.layout = view.findViewById(R.id.layout);

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
        RoundedImageView roundedImageView1;
        TextView tv_club_title,tv_data, tv_info;
        View layout;

    }
}
