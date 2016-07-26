package com.lis99.mobile.newhome.activeline;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveLineNewModel;
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
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        ActiveLineNewModel.ActivitylistEntity item = (ActiveLineNewModel.ActivitylistEntity) object;
        if ( object == null ) return;

        if ( !TextUtils.isEmpty(item.getImages()))
        {
            ImageLoader.getInstance().displayImage(item.getImages(), holder.ivBg, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder.ivBg));
        }

        holder.tvTag.setText(item.getHarddesc());

        holder.tvStyle.setText(item.getCatename());

        holder.tvTitle.setText(item.getTitle());
    }

    protected class ViewHolder {
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private LinearLayout layouttag;
        private TextView tvTag;
        private TextView tvStyle;
        private TextView tvTitle;

        public ViewHolder(View view) {
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            layouttag = (LinearLayout) view.findViewById(R.id.layouttag);
            tvTag = (TextView) view.findViewById(R.id.tv_tag);
            tvStyle = (TextView) view.findViewById(R.id.tv_style);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
