package com.lis99.mobile.newhome;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.DynamicListModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

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
            holder.dynamic_item_title = (TextView) view.findViewById(R.id.dynamic_item_title);
            holder.dynamic_item_title1 = (TextView) view.findViewById(R.id.dynamic_item_title1);

            holder.tv_actve = (TextView) view.findViewById(R.id.tv_actve);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        DynamicListModel.Topicslist item = (DynamicListModel.Topicslist) getItem(i);

        if ( item == null ) return view;

        if ( item.category == 0 )
        {
            holder.iv_active.setVisibility(View.GONE);
            holder.dynamic_item_title.setVisibility(View.VISIBLE);
            holder.dynamic_item_title1.setVisibility(View.INVISIBLE);
            holder.dynamic_item_title.setText(item.replycontent);
            holder.dynamic_item_title1.setText(item.replycontent);
            holder.tv_actve.setText("发布了一个话题");
        }
        else
        {
            holder.iv_active.setVisibility(View.VISIBLE);
            holder.dynamic_item_title.setVisibility(View.GONE);
            holder.dynamic_item_title1.setVisibility(View.GONE);
            holder.tv_actve.setText("发布了一个活动");
        }

        if ( item.is_vip == 0 )
        {
            holder.vipStar.setVisibility(View.GONE);
        }
        else
        {
            holder.vipStar.setVisibility(View.VISIBLE);
        }

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView, ImageUtil.getclub_topic_headImageOptions());

        if ( TextUtils.isEmpty(item.image) )
        {
            holder.layout_info_img.setVisibility(View.GONE);
            holder.iv_active.setVisibility(View.GONE);
        }
        else
        {
            holder.layout_info_img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.iv_info, ImageUtil.getclub_topic_imageOptions());
        }

        holder.tv_name.setText(item.nickname);
        holder.tv_time.setText(item.createtime);
        holder.tv_info.setText(item.topic_title);

        holder.tv_club.setText(item.club_title);
        holder.tv_reply.setText(""+item.replytot);

        if ( !TextUtils.isEmpty(item.catename))
        {
            holder.tv_style.setText(item.catename);
            holder.tv_style.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_style.setVisibility(View.INVISIBLE);
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
        TextView tv_club, tv_reply, tv_style, dynamic_item_title, dynamic_item_title1, tv_actve;
    }
}
