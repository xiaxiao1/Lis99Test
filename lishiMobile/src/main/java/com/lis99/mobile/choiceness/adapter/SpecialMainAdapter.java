package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClubSpecialList;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/6/23.
 */
public class SpecialMainAdapter extends MyBaseAdapter {



    public SpecialMainAdapter(Context c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        SubjectHolder holder = null;
        if (view == null) {
            holder = new SubjectHolder();
            view = View.inflate(mContext, R.layout.choiceness_item_subject, null);

            holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
            holder.choiceness_subject_forground = (RoundedImageView) view.findViewById(R.id.choiceness_subject_forground);

            view.setTag(holder);
        } else {
            holder = (SubjectHolder) view.getTag();
        }

        LSClubSpecialList.Taglist item = (LSClubSpecialList.Taglist)listItem.get(i);
        if (item == null) return view;

        if (!TextUtils.isEmpty(item.name)) {
            holder.choiceness_subject_forground.setVisibility(View.VISIBLE);
        } else {
            holder.choiceness_subject_forground.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(item.images, holder.iv_bg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

        holder.tv_title.setText(item.name);
        holder.tv_info.setText(item.title);


        return view;
    }



    class SubjectHolder
    {
        ImageView iv_load;
        RoundedImageView iv_bg, choiceness_subject_forground;
        TextView tv_title, tv_info;
    }

}
