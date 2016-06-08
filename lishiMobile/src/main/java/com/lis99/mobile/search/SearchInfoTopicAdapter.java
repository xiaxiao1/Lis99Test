package com.lis99.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.SearchInfoTopicModel;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoTopicAdapter extends MyBaseAdapter {

    public SearchInfoTopicAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.search_info_item_topic, null);
            holder = new Holder();

            holder.tv_topic_title = (TextView) view.findViewById(R.id.tv_topic_title);
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

        final SearchInfoTopicModel.Clubtopiclist item = (SearchInfoTopicModel.Clubtopiclist) getItem(i);

        if ( item == null )
        {
            return view;
        }

        if ( i == 0 )
        {
            holder.tv_topic_title.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tv_topic_title.setVisibility(View.GONE);
        }

        holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchInfoTopic.searchText, item.title)));

        holder.tv_location.setText(item.nickname);

        holder.tv_label.setText(item.createdate + " 发布于 " + item.clubname);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( item.category == 0 || item.category == 1 )
                {
                    Intent i = new Intent(mContext, LSClubTopicActivity.class);
                    i.putExtra("topicID", item.id);
                    mContext.startActivity(i);
                }
                else if ( 2 == item.category )
                {
                    Intent intent = new Intent(mContext, LSClubTopicNewActivity.class);
                    intent.putExtra("topicID", item.id);
                    mContext.startActivity(intent);
                }
                else if ( 3 == item.category )
                {
                    Intent intent = new Intent(mContext, LSClubNewTopicListMain.class);
                    intent.putExtra("TOPICID", "" + item.id);
                    mContext.startActivity(intent);
                }
                else if ( 4 == item.category )
                {
//                    Intent intent = new Intent(mContext, LSClubTopicActiveOffLine.class);
//                    intent.putExtra("topicID", item.id);
//                    mContext.startActivity(intent);

                    Common.goTopic(mContext, 4, item.id);
                }

            }
        });


        return view;
    }




    class Holder
    {
        TextView tv_topic_title, tv_title, tv_location, tv_label;
        LinearLayout layout;
    }
}
