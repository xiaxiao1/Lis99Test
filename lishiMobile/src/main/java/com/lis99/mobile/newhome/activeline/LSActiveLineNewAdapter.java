package com.lis99.mobile.newhome.activeline;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LiShiRecommendActiveModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/1/15.
 */
public class LSActiveLineNewAdapter extends MyBaseAdapter {

    public LSActiveLineNewAdapter(Activity c, List listItem) {
        super(c, listItem);
    }


    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(mContext, R.layout.active_line_new_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(i, (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(int i, ViewHolder holder) {
        //TODO implement
        LiShiRecommendActiveModel.ActiveEntity item = (LiShiRecommendActiveModel.ActiveEntity) getItem(i);
        if ( item == null ) return;
        if (i == 0) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.GONE);

        }

        if ( !TextUtils.isEmpty(item.getImages()))
        {
            ImageLoader.getInstance().displayImage(item.getImages(), holder.ivBg, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder.ivBg));
        }

        String space="";

        //设置活动类型
        if (item.getCatename().equals("")) {
            holder.tvStyle.setText("");
            holder.label_line.setVisibility(View.GONE);
            space="";
        } else {
            holder.label_line.setVisibility(View.VISIBLE);
            holder.tvStyle.setText(item.getCatename()+" ");
            space=" ";
        }

        //设置出发地
        holder.tvTag.setText(space+item.getCityname());
        //设置title
        holder.tvTitle.setText(item.getTitle());
        //活动批次信息
        holder.tvBatchInfo.setText(item.getMonth_day()+"  共"+item.getTotbatchs()+"期");
        //活动价格
        holder.tvPrice.setText("￥"+item.getLowprice());
        //几天行程
        holder.tvDays.setText(" 起 / "+item.getTripdays()+"天行程");

    }

    protected class ViewHolder {
        private View line;
        private View label_line;
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private TextView tvStyle;
        private TextView tvTag;
        private TextView tvTitle;
        private TextView tvBatchInfo;
        private TextView tvPrice;
        private TextView tvDays;

        public ViewHolder(View view) {
            line = view.findViewById(R.id.line);
            label_line = view.findViewById(R.id.item_label_line_v);
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            tvStyle = (TextView) view.findViewById(R.id.tv_style);
            tvTag = (TextView) view.findViewById(R.id.tv_tag);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvBatchInfo = (TextView) view.findViewById(R.id.tv_batch_info);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvDays = (TextView) view.findViewById(R.id.tv_days);
        }
    }
}
