package com.lis99.mobile.newhome.sysmassage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.SysMassageModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/28.
 */
public class SysMassageAdapter extends MyBaseAdapter{

    public SysMassageAdapter(Activity c, ArrayList listItem) {
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
            holder.tv_info_title = (TextView) view.findViewById(R.id.tv_info_title);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        final SysMassageModel.Lists item = (SysMassageModel.Lists) getItem(i);

        if ( item == null ) return view;

        if ( item.state == 0 )
        {
            holder.tv_info.setMaxLines(2);
        }
        else
        {
            holder.tv_info.setMaxLines(Integer.MAX_VALUE);
        }

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getImageOptionClubIcon());

        holder.tv_club_title.setText(item.nickname);
        holder.tv_info.setText(item.content);
        holder.tv_data.setText(item.createtime);
        holder.tv_info_title.setText(item.title);
        //跳转个人主页
        holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+item.uid);
                mContext.startActivity(intent);
            }
        });

        holder.tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.state = 1;
                notifyDataSetChanged();
            }
        });



        return view;
    }



    class Holder
    {
        RoundedImageView roundedImageView1;
        TextView tv_club_title,tv_data, tv_info;
        View layout;
        TextView tv_info_title;
    }
}
