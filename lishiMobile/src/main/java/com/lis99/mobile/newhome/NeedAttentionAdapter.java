package com.lis99.mobile.newhome;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class NeedAttentionAdapter extends MyBaseAdapter {

    private String UserId;



    public NeedAttentionAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;

        if ( view == null )
        {
            UserId = DataManager.getInstance().getUser().getUser_id();
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

        final MyFriendsRecommendModel.Lists item = (MyFriendsRecommendModel.Lists) getItem(i);

        if ( UserId.equals(""+item.user_id))
        {
            holder.btn_attention.setVisibility(View.GONE);
        }
        else
        {
            holder.btn_attention.setVisibility(View.VISIBLE);
        }

        if ( item.is_vip == 0 )
        {
            holder.vipStar.setVisibility(View.GONE);
        }
        else
        {
            holder.vipStar.setVisibility(View.VISIBLE);
        }

        if ( item.is_follow == 0 )
        {
            holder.btn_attention.setBackgroundResource(R.drawable.friends_no_attention);
        }
        else if ( item.is_follow == 1 )
        {
            holder.btn_attention.setBackgroundResource(R.drawable.friends_attention);
        }

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView, ImageUtil.getclub_topic_headImageOptions());

        holder.tv_name.setText(item.nickname);

        holder.tv_info.setText(item.topic_title);




        holder.btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addAttention
                if (item.is_follow == 0) {
                    addAttention((Button)view, item);
                }
                //CancelAttention
                else if (item.is_follow == 1) {
                    cancelAttention((Button)view, item);
                }
            }
        });


        return view;
    }

    private void addAttention ( final Button b, final MyFriendsRecommendModel.Lists item )
    {
        LSRequestManager.getInstance().getFriendsAddAttention(item.user_id, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                item.is_follow = 1;
                b.setBackgroundResource(R.drawable.friends_attention);
            }
        });
    }

    private void cancelAttention ( final Button b, final MyFriendsRecommendModel.Lists item )
    {
        LSRequestManager.getInstance().getFriendsAddAttention(item.user_id, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                item.is_follow = 0;
                b.setBackgroundResource(R.drawable.friends_no_attention);
            }
        });
    }

    class Holder
    {
        RoundedImageView roundedImageView;
        ImageView vipStar;
        Button btn_attention;
        TextView tv_name, tv_info;
    }
}
