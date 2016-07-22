package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/7/8.
 */
public class ActiveMainRecommendRecycler extends MyBaseRecycler<ActiveMainRecommendRecycler.VHolder> {



    public ActiveMainRecommendRecycler(List<?> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public boolean getInfo(VHolder vHolder, int i) {

        ActiveMainHeadModel.HotlistEntity.ActlistEntity item = (ActiveMainHeadModel.HotlistEntity
                .ActlistEntity) list.get(i);
        if ( item == null ) return false;
        vHolder.title.setText(item.topicTitle);
        vHolder.content.setText(item.harddesc+" "+item.cate_name);
        vHolder.price.setText(""+Common.getIntInString(item.price));
        if ( !TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, vHolder.roundedImageView, ImageUtil.getclub_topic_imageOptions());
        }



        return false;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = View.inflate(mContext, R.layout.active_recommend_recycler, null);

        VHolder holder = new VHolder(v);

        return holder;
    }


    class VHolder extends RecyclerView.ViewHolder
    {

        private RoundedImageView roundedImageView;
        private TextView title;
        private TextView content;
        private TextView price;
        private TextView tvNikeName;

        public VHolder(View view) {
            super(view);
            roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            price = (TextView) view.findViewById(R.id.price);
            tvNikeName = (TextView) view.findViewById(R.id.tv_nikeName);
        }
    }

}
