package com.lis99.mobile.newhome.equip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.EquipInfoModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/23.
 */
public class ReplayAdapter extends MyBaseAdapter {

    private boolean haveMore;

    public ReplayAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }



    public void setHaveMore(boolean haveMore) {
        this.haveMore = haveMore;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.ls_equip_reply_item, null);
            holder = new Holder();
            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_reply_info = (TextView) view.findViewById(R.id.tv_reply_info);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            holder.btn = (Button) view.findViewById(R.id.btn);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }


        EquipInfoModel.Commenlist item = (EquipInfoModel.Commenlist) getItem(i);

        if ( item == null ) return view;

        if ( i == getCount() - 1 && haveMore )
        {
            holder.btn.setVisibility(View.VISIBLE);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转评论列表
                }
            });
        }
        else
        {
            holder.btn.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getclub_topic_headImageOptions());

        holder.tv_name.setText(item.nickname);

        holder.tv_reply_info.setText(item.comment);
        int star = Common.string2int(item.star);
        holder.ratingBar.setRating(star == -1 ? 0 : star );



        return view;
    }

    private class Holder
    {
        RoundedImageView roundedImageView1;
        ImageView vipStar;
        TextView tv_name, tv_reply_info;
        RatingBar ratingBar;
        Button btn;
    }

}
