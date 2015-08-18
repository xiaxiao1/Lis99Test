package com.lis99.mobile.newhome;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.newhome.click.FriendsItemOnClick;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class FansAdapter extends MyBaseAdapter {


    public FansAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.friends_attention_item, null);
            holder = new Holder();
            holder.roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.btn_attention = (Button) view.findViewById(R.id.btn_attention);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        MyFriendsRecommendModel.Lists item = (MyFriendsRecommendModel.Lists) getItem(i);
        if ( item == null ) return view;

        holder.tv_name.setText(item.nickname);
        holder.tv_info.setText(item.topic_title);
        if ( item.is_vip == 0 )
        {
            holder.vipStar.setVisibility(View.GONE);
        }
        else {
            holder.vipStar.setVisibility(View.VISIBLE);
        }
        if ( item.is_follow == 0 )
        {
            Common.setBtnNoAttention(holder.btn_attention);
        }
        else if ( item.is_follow == 1 )
        {
            Common.setBtnAttention(holder.btn_attention);
        }
        if (!TextUtils.isEmpty(item.headicon) && !holder.roundedImageView.getTag().equals(item.headicon))
        {
            holder.roundedImageView.setTag(item.headicon);
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView, ImageUtil.getclub_topic_headImageOptions());
        }

        holder.btn_attention.setOnClickListener( new FriendsItemOnClick(holder.btn_attention, item));


        return view;
    }

    class Holder
    {
        RoundedImageView roundedImageView;
        ImageView vipStar;
        Button btn_attention;
        TextView tv_name, tv_info;
    }
}
