package com.lis99.mobile.newhome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class DynamicAdapter extends MyBaseAdapter {
    public DynamicAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.dynamic_item, null);
            holder = new Holder();

            holder.roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.layout_info_img = (RelativeLayout) view.findViewById(R.id.layout_info_img);
            holder.iv_info = (RoundedImageView) view.findViewById(R.id.iv_info);
            holder.iv_active = (ImageView) view.findViewById(R.id.iv_active);
            holder.tv_club = (TextView) view.findViewById(R.id.tv_club);
            holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);
            holder.tv_style = (TextView) view.findViewById(R.id.tv_style);

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
        RoundedImageView roundedImageView;
        ImageView vipStar;
        TextView tv_name, tv_time, tv_info;
        RelativeLayout layout_info_img;
        RoundedImageView iv_info;
        ImageView iv_active;
        TextView tv_club, tv_reply, tv_style;
    }
}
