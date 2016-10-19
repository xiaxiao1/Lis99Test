package com.lis99.mobile.club.filter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.filter.model.NearbyListMainModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/7/13.
 */
public class FilterAdapter extends MyBaseAdapter {



    public FilterAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.active_line_new_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag(), i);
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder, int i) {
        //TODO implement

        if ( i == 0 )
        {
            holder.line.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.line.setVisibility(View.GONE);
        }

        NearbyListMainModel.ListsEntity item = (NearbyListMainModel.ListsEntity) object;
        if ( item == null ) return;



//        holder.tvTitle.setText(item.title);
//        holder.tvTag.setText(item.harddesc);
//        holder.tvStyle.setText(item.cateName);
        if ( !TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, holder.ivBg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder.ivBg));
        }

        if ( TextUtils.isEmpty(item.cateName))
        {
            holder.item_label_line_v.setVisibility(View.GONE);
            holder.tvStyle.setText("");
        }
        else
        {
            holder.tvStyle.setText(item.cateName+" ");
        }
        holder.tvTitle.setText(item.title);

        holder.tvTag.setText(" "+item.setcityname);


        holder.tvBatchInfo.setText(item.starttime_intval + " " + item.batch_count);
        holder.tvPrice.setText(item.min_price);
        holder.tvDays.setText(" 起 / "+item.trip_days);


    }

    protected class ViewHolder {
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private LinearLayout layouttag;
        private TextView tvTag;
        private TextView tvStyle;
        private TextView tvTitle;
        private View line;
        private TextView tvBatchInfo;
        private TextView tvPrice;
        private TextView tvDays;
        private View item_label_line_v;

        public ViewHolder(View view) {
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            layouttag = (LinearLayout) view.findViewById(R.id.layouttag);
            tvTag = (TextView) view.findViewById(R.id.tv_tag);
            tvStyle = (TextView) view.findViewById(R.id.tv_style);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            line = view.findViewById(R.id.line);
            tvBatchInfo = (TextView) view.findViewById(R.id.tv_batch_info);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvDays = (TextView) view.findViewById(R.id.tv_days);
            item_label_line_v = view.findViewById(R.id.item_label_line_v);

        }
    }
}
