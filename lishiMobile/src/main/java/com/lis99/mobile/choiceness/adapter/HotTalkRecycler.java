package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/6/23.
 */
public class HotTalkRecycler extends RecyclerView.Adapter<HotTalkRecycler.MyHolder> {

    private LayoutInflater mInflater;
    private List<?> listInfo;
    private Context mContext;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public HotTalkRecycler(List<?> listInfo, Context mContext) {
        this.listInfo = listInfo;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.hot_talk_recycler, null);

        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder myHolder, final int i) {

        ChoicenessBannerModel.HotlistEntity.HlistEntity item = (ChoicenessBannerModel
                .HotlistEntity.HlistEntity) listInfo.get(i);

        if ( item == null ) return;

        myHolder.title.setText(item.title);
        myHolder.tvNikeName.setText(item.nickname);
        if ( !TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, myHolder.roundedImageView, ImageUtil.getDefultImageOptions());
        }
        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, myHolder.ivHead, ImageUtil.getclub_topic_headImageOptions());
        }




        if (mOnItemClickLitener != null)
        {

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(myHolder.itemView, i);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if ( listInfo == null ) return 0;
        return listInfo.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder
    {

        private RoundedImageView roundedImageView;
        private TextView title;
        private RoundedImageView ivHead;
        private TextView tvNikeName;

        public MyHolder(View itemView) {
            super(itemView);

            roundedImageView = (RoundedImageView) itemView.findViewById(R.id.roundedImageView);
            title = (TextView) itemView.findViewById(R.id.title);
            ivHead = (RoundedImageView) itemView.findViewById(R.id.iv_head);
            tvNikeName = (TextView) itemView.findViewById(R.id.tv_nikeName);

        }
    }


}
