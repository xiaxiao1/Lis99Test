package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.MyJoinClubModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/23.
 */
public class MyJoinAdapter extends MyBaseAdapter {


    public MyJoinAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.my_join_club_item, null );

            holder = new Holder();

            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.nameView = (TextView) view.findViewById(R.id.nameView);
            holder.recentView = (TextView) view.findViewById(R.id.recentView);
            holder.tv_join = (TextView) view.findViewById(R.id.tv_join);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        MyJoinClubModel.Clublist item = (MyJoinClubModel.Clublist) getItem(i);

        ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView1, ImageUtil.getImageOptionClubIcon());

        holder.nameView.setText(item.title);
        holder.recentView.setText(item.content);
        holder.tv_join.setText(""+item.joinNum +"人参与");

        return view;
    }

    public class Holder
    {
        public RoundedImageView roundedImageView1;
        public TextView nameView, recentView, tv_join;
    }



}
