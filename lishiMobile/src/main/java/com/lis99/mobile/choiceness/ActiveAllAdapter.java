package com.lis99.mobile.choiceness;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveAllModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAllAdapter extends MyBaseAdapter {



    public ActiveAllAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
//        if ( view == null )
//        {
//            view = View.inflate(mContext, R.layout.active_all_adapter, null);
//            holder = new Holder();
//
//            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
//            holder.tv_club_name = (TextView) view.findViewById(R.id.tv_club_name);
//            holder.tv_data = (TextView) view.findViewById(R.id.tv_data);
//            holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
//
//            holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
//            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
//            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
//            view.setTag(holder);
//
//        }
//        else
//        {
//            holder = (Holder) view.getTag();
//        }
//
//        ActiveAllModel.Clubtopiclist item = (ActiveAllModel.Clubtopiclist) getItem(i);
//
//        if ( item == null )
//            return view;
//
//        holder.tv_title.setText(item.title);
//        holder.tv_type.setText(item.catename);
//        holder.tv_data.setText(item.times);
//        holder.tv_club_name.setText(item.club_title);
//        if (!TextUtils.isEmpty(item.club_img))
//        {
//            ImageLoader.getInstance().displayImage(item.club_img, holder.roundedImageView1, ImageUtil.getImageOptionClubIcon());
//        }
//
//        if ( !TextUtils.isEmpty(item.image))
//        {
//            ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
//        }

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_all_item_new, null);
            holder = new Holder();

            holder.ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            holder.ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvTag = (TextView) view.findViewById(R.id.tv_tag);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        ActiveAllModel.Clubtopiclist item = (ActiveAllModel.Clubtopiclist) getItem(i);

        if ( item == null )
            return view;

        holder.tvTitle.setText(item.title);
        holder.tvTag.setText(item.times);

        if ( !TextUtils.isEmpty(item.image))
        {
            ImageLoader.getInstance().displayImage(item.image, holder.ivBg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder.ivBg));
        }

        return view;
    }
//获取TopicId -1 没有TopicId
    public int getTopicId ( int position )
    {
        int TopicId = -1;
        ActiveAllModel.Clubtopiclist item = (ActiveAllModel.Clubtopiclist) getItem(position);
        if ( item == null ) return TopicId;
        TopicId = item.id;
        return TopicId;
    }


    class Holder
    {
//        TextView tv_data,tv_type;
//        RoundedImageView iv_bg, roundedImageView1;
//        ImageView iv_load;
//        TextView tv_title;
//        TextView tv_club_name;

        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private TextView tvTitle;
        private TextView tvTag;

    }
}
