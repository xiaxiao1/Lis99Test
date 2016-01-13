package com.lis99.mobile.club.newtopic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/12.
 */
public class LSClubTopicDetailAdapter extends MyBaseAdapter {

    public LSClubTopicDetailAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.adapter_club_info_detail_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tvInfo = (TextView) view.findViewById(R.id.tv_info);
            viewHolder.iv = (ImageView) view.findViewById(R.id.iv);
            viewHolder.tvDescrible = (TextView) view.findViewById(R.id.tv_describle);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }




        return view;
    }


    protected class ViewHolder {
        private TextView tvTitle;
        private TextView tvInfo;
        private ImageView iv;
        private TextView tvDescrible;
    }


}
