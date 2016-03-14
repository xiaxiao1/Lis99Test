package com.lis99.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.SearchInfoActiveModel;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoActiveAdapter extends MyBaseAdapter{


    public SearchInfoActiveAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.search_info_item_active, null);
            holder = new Holder();
            holder.iv_icon = (RoundedImageView) view.findViewById(R.id.iv_icon);
            holder.tv_active_title = (TextView) view.findViewById(R.id.tv_active_title);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            holder.layout = (LinearLayout) view.findViewById(R.id.layout);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);

            view.setTag(holder);


        }
        else
        {
            holder = (Holder) view.getTag();
        }

        final SearchInfoActiveModel.Clubtopiclist item = (SearchInfoActiveModel.Clubtopiclist) getItem(i);

        if ( item == null )
        {
            return  view;
        }

        if ( i == 0 )
        {
            holder.tv_active_title.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_active_title.setVisibility(View.GONE);
        }

        ImageLoader.getInstance().displayImage(item.image, holder.iv_icon, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_icon));

        holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchInfoActive.searchText, item.title)));

        holder.tv_location.setText(item.dtime);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !TextUtils.isEmpty(item.activity_code))
                {
                    Intent intent = new Intent(mContext, LSClubTopicActiveOffLine.class);
                    intent.putExtra("topicID", item.id);
                    mContext.startActivity(intent);
                    return;
                }
                Intent i = new Intent(mContext, LSClubTopicActivity.class);
                i.putExtra("topicID", item.id);
                mContext.startActivity(i);
            }
        });


        return view;
    }

    class Holder
    {
        TextView tv_active_title, tv_title, tv_location;
        RoundedImageView iv_icon;
        LinearLayout layout;
        ImageView iv_load;
    }
}
