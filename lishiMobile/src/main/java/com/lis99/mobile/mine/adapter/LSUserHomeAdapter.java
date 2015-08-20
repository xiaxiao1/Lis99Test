package com.lis99.mobile.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BaseListAdapter;
import com.lis99.mobile.mine.LSBaseTopicModel;
import com.lis99.mobile.mine.LSMineApplyInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhangjie on 8/19/15.
 */
public class LSUserHomeAdapter extends BaseListAdapter<LSBaseTopicModel> {

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private void buildOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.club_icon_header_default)
                .showImageForEmptyUri(R.drawable.club_icon_header_default)
                .showImageOnFail(R.drawable.club_icon_header_default)
                .cacheInMemory(false).cacheOnDisk(true).build();
    }

    public LSUserHomeAdapter(Context context) {
        super(context);
    }

    public LSUserHomeAdapter(Context context, List<LSBaseTopicModel> data) {
        super(context, data);
        buildOptions();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ls_user_home_topic_item, null);
            holder = new ViewHolder();
            holder.roundedImageView1 = (ImageView) convertView.findViewById(R.id.imageVew);
            holder.categoryView = (ImageView) convertView.findViewById(R.id.categoryView);
            holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
            holder.infoView = (TextView) convertView.findViewById(R.id.clubView);
            holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LSBaseTopicModel info = getItem(position);
        imageLoader.displayImage(info.getImage(), holder.roundedImageView1);
        holder.timeView.setText(info.getCreatedate());
        holder.nameView.setText(info.getTopic_title());
        holder.infoView.setText("发布于 " + info.getClub_title());

        if (info.getCategory() == 0) {
            holder.categoryView.setImageResource(R.drawable.icon_user_home_topic);
        } else {
            holder.categoryView.setImageResource(R.drawable.icon_user_home_activity);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView roundedImageView1;
        ImageView categoryView;
        TextView timeView;
        TextView infoView;
        TextView nameView;
    }
}
