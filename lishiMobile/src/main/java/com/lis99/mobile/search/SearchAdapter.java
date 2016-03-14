package com.lis99.mobile.search;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.SearchMainListModel;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/7/2.
 */
public class SearchAdapter extends BaseAdapter{

    private int count = 3;

    private final int CLUB = 0;

    private final int TOPIC = 1;

    private final int ACTIVE = 2;

    private int state = CLUB;

    private SearchMainListModel model;

    private Context mContext;



    public SearchAdapter(Context c, SearchMainListModel listItem) {
        mContext = c;
        model = listItem;


    }

    public void clean ()
    {
        model = null;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {

        switch (i)
        {
            case 0:
                return model == null ? null : model.clublist;
            case 1:
                return model == null ? null : model.huatilist;
            case 2:
                return model == null ? null : model.huodonglist;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return setView(i, view, viewGroup);
    }

    @Override
    public int getItemViewType(int position) {
        switch ( position )
        {
            case 0:
                state = CLUB;
                break;
            case 1:
                state = TOPIC;
                break;
            case 2:
                state = ACTIVE;
                break;
        }
        return state;
    }


    public View setView(int i, View view, ViewGroup viewGroup) {

        int viewType = getItemViewType(i);
        switch ( viewType )
        {
            case CLUB:
                return getClub(i, view);
            case TOPIC:
                return getTopic(i, view);
            case ACTIVE:
                return getActive(i, view);
        }
        return view;
    }


    private View getClub ( int i, View v )
    {
        HolderClub holder = null;
        if ( v == null )
        {
            v = View.inflate(mContext, R.layout.search_item_club, null);

            holder = new HolderClub();

            holder.layout = (LinearLayout) v.findViewById(R.id.layout);
            holder.layout1 = (LinearLayout) v.findViewById(R.id.layout1);
            holder.layout2 = (LinearLayout) v.findViewById(R.id.layout2);

            holder.roundedImageView = (RoundedImageView) v.findViewById(R.id.roundedImageView);
            holder.roundedImageView1 = (RoundedImageView) v.findViewById(R.id.roundedImageView1);
            holder.roundedImageView2 = (RoundedImageView) v.findViewById(R.id.roundedImageView2);

            holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
            holder.tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
            holder.tv_title2 = (TextView) v.findViewById(R.id.tv_title2);

            holder.tv_location = (TextView) v.findViewById(R.id.tv_location);
            holder.tv_location1 = (TextView) v.findViewById(R.id.tv_location1);
            holder.tv_location2 = (TextView) v.findViewById(R.id.tv_location2);

            holder.tv_label = (TextView) v.findViewById(R.id.tv_label);
            holder.tv_label1 = (TextView) v.findViewById(R.id.tv_label1);
            holder.tv_label2 = (TextView) v.findViewById(R.id.tv_label2);

            holder.layout_search_more = (LinearLayout) v.findViewById(R.id.layout_search_more);

            holder.layout_search_nothing = (LinearLayout) v.findViewById(R.id.layout_search_nothing);

            holder.tv_nothing = (TextView) v.findViewById(R.id.tv_club_nothing);

            v.setTag(holder);

        }
        else
        {
            holder = (HolderClub) v.getTag();
        }

        holder.layout.setVisibility(View.GONE);
        holder.layout1.setVisibility(View.GONE);
        holder.layout2.setVisibility(View.GONE);
        holder.layout_search_more.setVisibility(View.GONE);
        holder.layout_search_nothing.setVisibility(View.GONE);

        ArrayList<SearchMainListModel.Clublist> list = (ArrayList<SearchMainListModel.Clublist>) getItem(i);
        if ( list == null || list.size() == 0 )
        {
            holder.tv_nothing.setText(Html.fromHtml("没有“"+"<font color='#49a34b'>"+ SearchActivity.searchText +"</font>"+"”相关的俱乐部"));
            holder.layout_search_nothing.setVisibility(View.VISIBLE);
            return v;
        }

        //更多
        if ( model.clubtot > 3 )
        {
            holder.layout_search_more.setVisibility(View.VISIBLE);
            holder.layout_search_more.setOnClickListener( new android.view.View.OnClickListener ()
            {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SearchInfoClub.class);
                    intent.putExtra("SEARCHTEXT", SearchActivity.searchText);
                    mContext.startActivity(intent);
                }
            });
        }

        if ( list.size() >= 1 )
        {
            SearchMainListModel.Clublist item = list.get(0);
            holder.layout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView, ImageUtil.getImageOptionClubIcon());
            holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location.setText(item.cityname);
            holder.tv_label.setText(item.catename);
            holder.layout.setOnClickListener( new clubClick(item));

        }
        if ( list.size() >= 2 )
        {
            SearchMainListModel.Clublist item = list.get(1);
            holder.layout1.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView1, ImageUtil.getImageOptionClubIcon());
            holder.tv_title1.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location1.setText(item.cityname);
            holder.tv_label1.setText(item.catename);
            holder.layout1.setOnClickListener(new clubClick(item));
        }
        if ( list.size() >= 3 )
        {
            SearchMainListModel.Clublist item = list.get(2);
            holder.layout2.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView2, ImageUtil.getImageOptionClubIcon());
            holder.tv_title2.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location2.setText(item.cityname);
            holder.tv_label2.setText(item.catename);
            holder.layout2.setOnClickListener(new clubClick(item));
        }





        return v;
    }

    private View getTopic ( int i, View v )
    {

        HolderClub holder = null;
        if ( v == null )
        {
            v = View.inflate(mContext, R.layout.search_item_topic, null);

            holder = new HolderClub();

            holder.layout = (LinearLayout) v.findViewById(R.id.layout);
            holder.layout1 = (LinearLayout) v.findViewById(R.id.layout1);
            holder.layout2 = (LinearLayout) v.findViewById(R.id.layout2);


            holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
            holder.tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
            holder.tv_title2 = (TextView) v.findViewById(R.id.tv_title2);

            holder.tv_location = (TextView) v.findViewById(R.id.tv_location);
            holder.tv_location1 = (TextView) v.findViewById(R.id.tv_location1);
            holder.tv_location2 = (TextView) v.findViewById(R.id.tv_location2);

            holder.tv_label = (TextView) v.findViewById(R.id.tv_label);
            holder.tv_label1 = (TextView) v.findViewById(R.id.tv_label1);
            holder.tv_label2 = (TextView) v.findViewById(R.id.tv_label2);

            holder.layout_search_more = (LinearLayout) v.findViewById(R.id.layout_search_more);

            holder.layout_search_nothing = (LinearLayout) v.findViewById(R.id.layout_search_nothing);

            holder.tv_nothing = (TextView) v.findViewById(R.id.tv_topic_nothing);

            v.setTag(holder);

        }
        else
        {
            holder = (HolderClub) v.getTag();
        }

        holder.layout.setVisibility(View.GONE);
        holder.layout1.setVisibility(View.GONE);
        holder.layout2.setVisibility(View.GONE);
        holder.layout_search_nothing.setVisibility(View.GONE);
        holder.layout_search_more.setVisibility(View.GONE);

        ArrayList<SearchMainListModel.Huatilist> list = (ArrayList<SearchMainListModel.Huatilist>) getItem(i);

        if ( list == null || list.size() == 0 )
        {
            holder.tv_nothing.setText(Html.fromHtml("没有“"+"<font color='#49a34b'>"+ SearchActivity.searchText +"</font>"+"”相关的话题"));
            holder.layout_search_nothing.setVisibility(View.VISIBLE);
            return v;
        }

        if ( model.huatitot > 3 )
        {
            holder.layout_search_more.setVisibility(View.VISIBLE);
            holder.layout_search_more.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SearchInfoTopic.class);
                    intent.putExtra("SEARCHTEXT", SearchActivity.searchText);
                    mContext.startActivity(intent);
                }
            });
        }

        if ( list.size() >= 1 )
        {
            SearchMainListModel.Huatilist item = list.get(0);
            holder.layout.setVisibility(View.VISIBLE);
            holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location.setText(item.nickname);
            holder.tv_label.setText(item.createdate + " 发布于 " + item.clubname );
            holder.layout.setOnClickListener(new topicClick(item));

        }
        if ( list.size() >= 2 )
        {
            SearchMainListModel.Huatilist item = list.get(1);
            holder.layout1.setVisibility(View.VISIBLE);
            holder.tv_title1.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location1.setText(item.nickname);
            holder.tv_label1.setText(item.createdate + " 发布于 " + item.clubname );
            holder.layout1.setOnClickListener(new topicClick(item));
        }
        if ( list.size() >= 3 )
        {
            SearchMainListModel.Huatilist item = list.get(2);
            holder.layout2.setVisibility(View.VISIBLE);
            holder.tv_title2.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location2.setText(item.nickname);
            holder.tv_label2.setText(item.createdate + " 发布于 " + item.clubname);
            holder.layout2.setOnClickListener(new topicClick(item));
        }


        return v;
    }

    private View getActive ( int i, View v )
    {

        HolderClub holder = null;
        if ( v == null )
        {
            v = View.inflate(mContext, R.layout.search_item_active, null);

            holder = new HolderClub();

            holder.layout = (LinearLayout) v.findViewById(R.id.layout);
            holder.layout1 = (LinearLayout) v.findViewById(R.id.layout1);
            holder.layout2 = (LinearLayout) v.findViewById(R.id.layout2);

            holder.roundedImageView = (RoundedImageView) v.findViewById(R.id.iv_icon);
            holder.roundedImageView1 = (RoundedImageView) v.findViewById(R.id.iv_icon1);
            holder.roundedImageView2 = (RoundedImageView) v.findViewById(R.id.iv_icon2);

            holder.tv_title = (TextView) v.findViewById(R.id.tv_title);
            holder.tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
            holder.tv_title2 = (TextView) v.findViewById(R.id.tv_title2);

            holder.tv_location = (TextView) v.findViewById(R.id.tv_location);
            holder.tv_location1 = (TextView) v.findViewById(R.id.tv_location1);
            holder.tv_location2 = (TextView) v.findViewById(R.id.tv_location2);

            holder.iv_load = (ImageView) v.findViewById(R.id.iv_load);
            holder.iv_load1 = (ImageView) v.findViewById(R.id.iv_load1);
            holder.iv_load2 = (ImageView) v.findViewById(R.id.iv_load2);


            holder.layout_search_more = (LinearLayout) v.findViewById(R.id.layout_search_more);

            holder.layout_search_nothing = (LinearLayout) v.findViewById(R.id.layout_search_nothing);

            holder.tv_nothing = (TextView) v.findViewById(R.id.tv_active_nothing);

            v.setTag(holder);

        }
        else
        {
            holder = (HolderClub) v.getTag();
        }

        holder.layout.setVisibility(View.GONE);
        holder.layout1.setVisibility(View.GONE);
        holder.layout2.setVisibility(View.GONE);
        holder.layout_search_more.setVisibility(View.GONE);
        holder.layout_search_nothing.setVisibility(View.GONE);

        ArrayList<SearchMainListModel.Huodonglist> list = (ArrayList<SearchMainListModel.Huodonglist>) getItem(i);
        if ( list == null || list.size() == 0 )
        {
            holder.tv_nothing.setText(Html.fromHtml("没有“"+"<font color='#49a34b'>"+ SearchActivity.searchText +"</font>"+"”相关的线路活动"));
            holder.layout_search_nothing.setVisibility(View.VISIBLE);
            return v;
        }

        if ( model.huodongtot > 3 )
        {
            holder.layout_search_more.setVisibility(View.VISIBLE);
            holder.layout_search_more.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SearchInfoActive.class);
                    intent.putExtra("SEARCHTEXT", SearchActivity.searchText);
                    mContext.startActivity(intent);
                }
            });
        }

        if ( list.size() >= 1 )
        {
            SearchMainListModel.Huodonglist item = list.get(0);
            holder.layout.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.roundedImageView));
            holder.tv_title.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location.setText(item.dtime);
            holder.layout.setOnClickListener(new activeClick(item));


        }
        if ( list.size() >= 2 )
        {
            SearchMainListModel.Huodonglist item = list.get(1);
            holder.layout1.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView1, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load1, holder.roundedImageView1));
            holder.tv_title1.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location1.setText(item.dtime);
            holder.layout1.setOnClickListener(new activeClick(item));
        }
        if ( list.size() >= 3 )
        {
            SearchMainListModel.Huodonglist item = list.get(2);
            holder.layout2.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView2, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load2, holder.roundedImageView2));
            holder.tv_title2.setText(Html.fromHtml(Common.getSearchText(SearchActivity.searchText, item.title)));
            holder.tv_location2.setText(item.dtime);
            holder.layout2.setOnClickListener(new activeClick(item));
        }


        return  v;
    }

    class HolderClub
    {
        LinearLayout layout, layout1, layout2, layout_search_more, layout_search_nothing;
        RoundedImageView roundedImageView, roundedImageView1, roundedImageView2;
        TextView tv_title, tv_title1, tv_title2;
        TextView tv_location, tv_location1, tv_location2;
        TextView tv_label, tv_label1, tv_label2;
        ImageView iv_load, iv_load1, iv_load2;
        TextView tv_nothing;
    }

    class clubClick implements View.OnClickListener {

        SearchMainListModel.Clublist item;

        public clubClick ( SearchMainListModel.Clublist item )
        {
            this.item = item;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, LSClubDetailActivity.class);
            i.putExtra("clubID", item.id);
            mContext.startActivity(i);
        }
    }

    class topicClick implements View.OnClickListener
    {
        SearchMainListModel.Huatilist item;

        public topicClick ( SearchMainListModel.Huatilist item )
        {
            this.item = item;
        }


        @Override
        public void onClick(View view) {
            Intent i = new Intent(mContext, LSClubTopicActivity.class);
            i.putExtra("topicID", item.id);
            mContext.startActivity(i);
        }
    }

    class activeClick implements View.OnClickListener
    {
        SearchMainListModel.Huodonglist item;

        public activeClick ( SearchMainListModel.Huodonglist item )
        {
            this.item = item;
        }


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
    }

}
