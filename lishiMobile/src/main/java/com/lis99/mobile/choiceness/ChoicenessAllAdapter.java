package com.lis99.mobile.choiceness;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ChoicenessAllModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/23.
 */
public class ChoicenessAllAdapter extends MyBaseAdapter{

    public ChoicenessAllAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        return getSubject(i, view );
    }

    private View getSubject ( int position, View view )
    {
        SubjectHolder holder = null;
        if ( view == null )
        {
            holder = new SubjectHolder();
            view = View.inflate(mContext, R.layout.choiceness_item_subject, null);

            holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.iv_subject = (ImageView) view.findViewById(R.id.iv_subject);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
            view.setTag(holder);
        }
        else
        {
            holder = (SubjectHolder) view.getTag();
        }

        ChoicenessAllModel.Omnibuslist item = (ChoicenessAllModel.Omnibuslist) getItem(position);
        if ( item == null ) return view;

        holder.iv_subject.setVisibility(View.VISIBLE);

        ImageLoader.getInstance().displayImage(item.images, holder.iv_bg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

        holder.tv_title.setText(item.title);
        holder.tv_info.setText(item.subhead);


        return view;
    }


    class SubjectHolder
    {
        ImageView iv_load;
        RoundedImageView iv_bg;
        TextView tv_title, tv_info;
        ImageView iv_subject;
    }
}
