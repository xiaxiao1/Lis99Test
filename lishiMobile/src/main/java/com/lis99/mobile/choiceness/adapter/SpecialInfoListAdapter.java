package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.SpecialInfoModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/6/28.
 */
public class SpecialInfoListAdapter extends MyBaseAdapter {



    public SpecialInfoListAdapter(Context c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.special_info_list_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        SpecialInfoModel.TopiclistEntity item = (SpecialInfoModel.TopiclistEntity) object;

        if ( !TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, holder.ivBg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder.ivBg));
        }
        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
        }
        holder.tvNikeName.setText(item.nickname);
        holder.tvContent.setText(item.content);
        holder.tvTitle.setText(item.topicTitle);
        holder.tvLikeNum.setText(""+item.likenum);
        holder.tvReplyNum.setText(""+item.replytotal);


    }



    protected class ViewHolder {
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private RoundedImageView roundedImageView1;
        private TextView tvNikeName;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvLikeNum;
        private TextView tvReplyNum;

        public ViewHolder(View view) {
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            tvNikeName = (TextView) view.findViewById(R.id.tv_nikeName);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvLikeNum = (TextView) view.findViewById(R.id.tv_likeNum);
            tvReplyNum = (TextView) view.findViewById(R.id.tv_replyNum);
        }
    }

}
