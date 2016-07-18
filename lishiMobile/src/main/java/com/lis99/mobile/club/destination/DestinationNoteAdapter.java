package com.lis99.mobile.club.destination;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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

public class DestinationNoteAdapter extends BaseListAdapter<DestinationNote> {

    public DestinationNoteAdapter(Context context, List<DestinationNote> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_note_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivBg = (RoundedImageView) view.findViewById(R.id.coverView);
            viewHolder.avatarView = (RoundedImageView) view.findViewById(R.id.avatarView);
            viewHolder.nameView = (TextView) view.findViewById(R.id.nameView);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.titleView);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        DestinationNote item = getItem(position);

        if ( !TextUtils.isEmpty(item.topic_image))
        {
            ImageLoader.getInstance().displayImage(item.topic_image, viewHolder.ivBg, ImageUtil.getclub_topic_imageOptions());
        }

        ImageLoader.getInstance().displayImage(item.headicon, viewHolder.avatarView, ImageUtil.getclub_topic_headImageOptions());

        viewHolder.tvTitle.setText(item.topic_title);
        viewHolder.nameView.setText(item.nickname);

        return view;
    }

    protected class ViewHolder {
        private RoundedImageView ivBg;
        private RoundedImageView avatarView;
        private TextView nameView;
        private TextView tvTitle;
    }

}
