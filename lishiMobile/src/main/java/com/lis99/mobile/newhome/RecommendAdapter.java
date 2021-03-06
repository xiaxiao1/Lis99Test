package com.lis99.mobile.newhome;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
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
public class RecommendAdapter extends MyBaseAdapter {

    private String userId;

    public RecommendAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;

        if ( view == null )
        {
            userId = DataManager.getInstance().getUser().getUser_id();
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

        if ( userId.equals(""+item.user_id))
        {
            holder.btn_attention.setVisibility(View.GONE);
        }
        else
        {
            holder.btn_attention.setVisibility(View.VISIBLE);
        }

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

        String url = (String) holder.roundedImageView.getTag();
        if (!TextUtils.isEmpty(item.headicon))
        {
            if ( !item.headicon.equals(url) )
            {
                holder.roundedImageView.setTag(item.headicon);
                ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView, ImageUtil.getclub_topic_headImageOptions());
            }
        }
        else {
            holder.roundedImageView.setImageResource(R.drawable.ls_nologin_header_icon);
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
