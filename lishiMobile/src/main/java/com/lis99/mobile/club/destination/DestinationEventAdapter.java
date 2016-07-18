package com.lis99.mobile.club.destination;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BaseListAdapter;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhangjie on 7/13/16.
 */

public class DestinationEventAdapter extends BaseListAdapter<DestinationEvent> {

    public DestinationEventAdapter(Context context, List<DestinationEvent> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_activiity_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            viewHolder.ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            viewHolder.tvTag = (TextView) view.findViewById(R.id.tv_tag);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        DestinationEvent item = getItem(position);

        if ( !TextUtils.isEmpty(item.topic_image))
        {
            ImageLoader.getInstance().displayImage(item.topic_image, viewHolder.ivBg, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(viewHolder.ivLoad, viewHolder.ivBg));
        }

        viewHolder.tvTag.setText(item.harddesc);


        viewHolder.tvTitle.setText(item.topic_title);

        return view;
    }

    protected class ViewHolder {
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private TextView tvTag;
        private TextView tvTitle;
    }

}
