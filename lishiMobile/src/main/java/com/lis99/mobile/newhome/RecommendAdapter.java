package com.lis99.mobile.newhome;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class RecommendAdapter extends MyBaseAdapter {


    public RecommendAdapter(Context c, ArrayList listItem) {
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
