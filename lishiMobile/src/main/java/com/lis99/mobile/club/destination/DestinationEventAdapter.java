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
            view = View.inflate(mContext, R.layout.active_line_new_item, null);
            viewHolder = new ViewHolder(view);
//            viewHolder.ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
//            viewHolder.ivLoad = (ImageView) view.findViewById(R.id.iv_load);
//            viewHolder.tvTag = (TextView) view.findViewById(R.id.tv_tag);
//            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
//            viewHolder.tv_style = (TextView) view.findViewById(R.id.tv_style);
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

//        viewHolder.tvTag.setText(item.harddesc);

//        viewHolder.tv_style.setText(item.cate_name);


        viewHolder.tvTitle.setText(item.topic_title);


//        viewHolder.tvTitle.setText(item.title);

        if ( TextUtils.isEmpty(item.cate_name))
        {
            viewHolder.item_label_line_v.setVisibility(View.GONE);
            viewHolder.tvStyle.setText("");
            viewHolder.tvTag.setText(item.setcityname);
        }
        else
        {
            viewHolder.tvStyle.setText(item.cate_name+" ");
            viewHolder.tvTag.setText(" "+item.setcityname);
        }




        viewHolder.tvBatchInfo.setText(item.starttime_intval + " " + item.batch_count);
        viewHolder.tvPrice.setText(item.min_price);
        viewHolder.tvDays.setText(" 起 / "+item.trip_days);


        return view;
    }

//    protected class ViewHolder {
//        private RoundedImageView ivBg;
//        private ImageView ivLoad;
//        private TextView tvTag;
//        private TextView tvTitle;
//        private TextView tv_style;
//    }

    protected class ViewHolder {
        private View line;
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private TextView tvStyle;
        private TextView tvTag;
        private TextView tvTitle;
        private TextView tvBatchInfo;
        private TextView tvPrice;
        private TextView tvDays;
        private View item_label_line_v;

        public ViewHolder(View view) {
            line = view.findViewById(R.id.line);
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            tvStyle = (TextView) view.findViewById(R.id.tv_style);
            tvTag = (TextView) view.findViewById(R.id.tv_tag);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvBatchInfo = (TextView) view.findViewById(R.id.tv_batch_info);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvDays = (TextView) view.findViewById(R.id.tv_days);
            item_label_line_v = view.findViewById(R.id.item_label_line_v);
        }
    }

}
