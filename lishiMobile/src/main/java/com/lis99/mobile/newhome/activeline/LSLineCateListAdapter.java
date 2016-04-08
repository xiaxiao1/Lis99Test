package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhangjie on 3/24/16.
 */
public class LSLineCateListAdapter extends BaseAdapter {

    LayoutInflater inflater;

    public ArrayList<LSLineCateListModel.ActivityItem> topiclist;


    DisplayImageOptions options;

    private DisplayImageOptions optionshead, optionsclub, optionsBg;

    private Animation animation;
    private Context context;

    public int ui_level = 1;

    public LSLineCateListAdapter(Context context, ArrayList<LSLineCateListModel.ActivityItem> topiclist ){
        this.topiclist = topiclist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        options = ImageUtil.getclub_topic_imageOptions();

        optionshead = ImageUtil.getclub_topic_headImageOptions();
        optionsclub = ImageUtil.getImageOptionClubIcon();
        optionsBg = ImageUtil.getDynamicImageOptions();
        animation = AnimationUtils.loadAnimation(context, R.anim.like_anim_rotate);

    }

    public void addList ( ArrayList<LSLineCateListModel.ActivityItem> topiclist )
    {
        this.topiclist.addAll(topiclist);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topiclist == null ? 0 : topiclist.size();
    }

    @Override
    public Object getItem(int position) {
        if ( topiclist.size() <= position || position < 0 ) return null;
        return topiclist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return conifgTopicActivityItemNewView(position, convertView, parent);
    }



    private View conifgTopicActivityItemNewView (int position, View view, ViewGroup parent) {
        ActivityHolder holder = null;
        if ( view == null )
        {
            view = View.inflate(context, R.layout.club_topic_activity_item_new, null);
            holder = new ActivityHolder();
            holder.imageView = (RoundedImageView) view.findViewById(R.id.iv_bg);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);


            holder.titleTextView = (TextView)view.findViewById(R.id.titleTextView);
            holder.timeTextView = (TextView)view.findViewById(R.id.timeTextView);
            holder.topGapView = view.findViewById(R.id.topGapView);

            view.setTag(holder);
        }
        else
        {
            holder = (ActivityHolder) view.getTag();
        }

        final LSLineCateListModel.ActivityItem item = topiclist.get(position);
        if ( item == null ) return view;


        if (position == 0) {
            holder.topGapView.setVisibility(View.VISIBLE);
        } else {
            holder.topGapView.setVisibility(View.GONE);
        }


        ImageLoader.getInstance().displayImage(item.images, holder.imageView, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.imageView));



        holder.titleTextView.setText(item.title);

        holder.timeTextView.setText(item.startdate);

        return view;
    }



    static class ActivityHolder
    {

        ImageView imageView, iv_load;
        TextView titleTextView, timeTextView;
        View topGapView;
    }


}
