package com.lis99.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.model.SearchInfoClubModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoClubAdapter extends MyBaseAdapter{


    public SearchInfoClubAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.search_info_item_club, null);
            holder = new Holder();
            holder.roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            holder.tv_club_title = (TextView) view.findViewById(R.id.tv_club_title);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            holder.tv_label = (TextView) view.findViewById(R.id.tv_label);
            holder.layout = (LinearLayout) view.findViewById(R.id.layout);

            view.setTag(holder);


        }
        else
        {
            holder = (Holder) view.getTag();
        }

        final SearchInfoClubModel.Clublist item = (SearchInfoClubModel.Clublist) getItem(i);

        if ( item == null )
        {

            return view;
        }

        if ( i == 0 )
        {
            holder.tv_club_title.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_club_title.setVisibility(View.GONE);
        }

        holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchInfoClub.searchText, item.title)));

        holder.tv_location.setText(item.cityname);
        holder.tv_label.setText(item.catename);

        ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView, ImageUtil.getImageOptionClubIcon());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, LSClubDetailActivity.class);
                i.putExtra("clubID", item.id);
                mContext.startActivity(i);
            }
        });


        return view;
    }

    class Holder
    {
        TextView tv_club_title, tv_title, tv_location, tv_label;
        RoundedImageView roundedImageView;
        LinearLayout layout;

    }

}
