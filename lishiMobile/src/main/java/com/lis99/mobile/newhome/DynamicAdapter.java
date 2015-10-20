package com.lis99.mobile.newhome;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.DynamicListModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class DynamicAdapter extends MyBaseAdapter {


    private Animation animation;

    public DynamicAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
        animation = AnimationUtils.loadAnimation(c, R.anim.like_anim_rotate);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
//            view = View.inflate(mContext, R.layout.dynamic_item, null);
            view = View.inflate(mContext, R.layout.choiceness_new_topic, null);
            holder = new Holder();

            holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_like = (TextView) view.findViewById(R.id.tv_like);
            holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);

            holder.layout_like =  view.findViewById(R.id.layout_like);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);

            holder.iv_like = (ImageView) view.findViewById(R.id.iv_like);

            holder.btn_concern = (Button) view.findViewById(R.id.btn_concern);

            holder.btn_concern.setBackgroundResource(0);
            holder.btn_concern.setTextColor(mContext.getResources().getColor(R.color.color_nine));

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        final DynamicListModel.Topicslist item = (DynamicListModel.Topicslist) getItem(i);

        if ( item == null ) return view;

        if ( item.is_vip == 0 )
        {
            holder.vipStar.setVisibility(View.GONE);
        }
        else
        {
            holder.vipStar.setVisibility(View.VISIBLE);
        }

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getclub_topic_headImageOptions());


        ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

        holder.tv_name.setText(item.nickname);
        holder.tv_reply.setText("" + item.replytot + "则评论");
        holder.tv_like.setText(""+item.likeNum);
        holder.tv_title.setText(item.topic_title);
        holder.btn_concern.setText(item.createtime);



        if ( item.likeStatus == 1 )
        {
            holder.iv_like.setImageResource(R.drawable.like_button_press);
        }
        else
        {
            holder.iv_like.setImageResource(R.drawable.topic_like_hand);
        }

        final Holder finalHolder = holder;
        holder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( item.likeStatus == 1 ) return;

                item.likeStatus = 1;

                item.likeNum += 1;

                finalHolder.tv_like.setText(""+item.likeNum);

                finalHolder.iv_like.setImageResource(R.drawable.like_button_press);

                finalHolder.iv_like.startAnimation(animation);

                LSRequestManager.getInstance().clubTopicLike(item.topic_id,null);
            }
        });


        holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.goUserHomeActivit((Activity)mContext, ""+item.user_id);
            }
        });


        return view;
    }

    class Holder
    {
        RoundedImageView roundedImageView1;
        ImageView vipStar, iv_bg, iv_like, iv_load;
        TextView tv_name, tv_like, tv_title, tv_reply;
        Button btn_concern;
        View layout_like;

    }
}
