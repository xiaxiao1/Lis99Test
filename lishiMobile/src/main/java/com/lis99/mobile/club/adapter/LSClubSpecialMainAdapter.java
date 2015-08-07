package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClubSpecialList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/6.
 */
public class LSClubSpecialMainAdapter extends MyBaseAdapter {


    public LSClubSpecialMainAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.ls_club_special_item, null);
            holder = new Holder();
            holder.iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        LSClubSpecialList.Taglist item = (LSClubSpecialList.Taglist) getItem(i);

        if ( item == null ) return view;

        holder.tv_title.setText("#"+item.name);

        if (!TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, holder.iv_bg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
        }
        return view;
    }


    class Holder
    {
        ImageView iv_bg, iv_load;
        TextView tv_title;
    }

}
