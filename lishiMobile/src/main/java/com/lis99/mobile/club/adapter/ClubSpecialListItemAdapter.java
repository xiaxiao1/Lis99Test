package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubSpecialListModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class ClubSpecialListItemAdapter extends MyBaseAdapter {

    public ClubSpecialListItemAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.club_special_list_item, null);
            holder = new Holder();
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);

            holder.iv_head = (RoundedImageView) view.findViewById(R.id.iv_head);
            holder.iv_img1 = (RoundedImageView) view.findViewById(R.id.iv_img1);
            holder.iv_img2 = (RoundedImageView) view.findViewById(R.id.iv_img2);
            holder.iv_img3 = (RoundedImageView) view.findViewById(R.id.iv_img3);

            holder.iv_hot = (ImageView) view.findViewById(R.id.iv_hot);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        ClubSpecialListModel.Topiclist item = (ClubSpecialListModel.Topiclist) getItem(i);

        if ( item == null ) return view;

        if ( item.is_hot == 0 )
        {
            holder.iv_hot.setVisibility(View.GONE);
        }
        else {
            holder.iv_hot.setVisibility(View.VISIBLE);
        }

        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, holder.iv_head, ImageUtil.getclub_topic_headImageOptions());
        }

        holder.tv_title.setText(item.topic_title);
        holder.tv_name.setText(item.nickname);
        holder.tv_reply.setText(item.replytotal == 0 ? "1" : ""+item.replytotal);

        holder.iv_img1.setVisibility(View.INVISIBLE);
        holder.iv_img2.setVisibility(View.INVISIBLE);
        holder.iv_img3.setVisibility(View.INVISIBLE);

        if ( item.topicimagelist == null || item.topicimagelist.size() == 0 )
        {

        }
        else {
            int num = item.topicimagelist.size();
            if (num > 0 && !TextUtils.isEmpty(item.topicimagelist.get(0).images))
            {
                holder.iv_img1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.topicimagelist.get(0).images, holder.iv_img1, ImageUtil.getDefultImageOptions());
            }
            if (num > 1 && !TextUtils.isEmpty(item.topicimagelist.get(1).images))
            {
                holder.iv_img2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.topicimagelist.get(1).images, holder.iv_img2, ImageUtil.getDefultImageOptions());
            }
            if (num > 2 && !TextUtils.isEmpty(item.topicimagelist.get(2).images))
            {
                holder.iv_img3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.topicimagelist.get(2).images, holder.iv_img3, ImageUtil.getDefultImageOptions());
            }
        }


        return view;
    }

    class Holder
    {
        TextView tv_title, tv_name, tv_reply;
        RoundedImageView iv_img1, iv_img2, iv_img3, iv_head;
        ImageView iv_hot;
    }
}
