package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 16/6/22.
 */
public class CommunityStarAdapter extends RecyclerView.Adapter<CommunityStarAdapter.MyHolder> {

    private LayoutInflater mInflater;
    private ArrayList<?> listInfo;
    private Context mContext;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }


    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private OnItemClickLitener mOnItemClickLitener;



    public CommunityStarAdapter(ArrayList<?> listInfo, Context mContext) {
        this.listInfo = listInfo;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.community_star_recycler, null);

        MyHolder holder = new MyHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder myHolder, final int i) {

        ChoicenessBannerModel.StartlistEntity item = (ChoicenessBannerModel.StartlistEntity) listInfo.get(i);

        if ( item == null ) return;

//        最后一条
        if ( i == listInfo.size() - 1 )
        {
            myHolder.tvTag.setVisibility(View.INVISIBLE);
            myHolder.iv_dot.setVisibility(View.VISIBLE);
            myHolder.forground.setVisibility(View.VISIBLE);
            myHolder.roundedImageView1.setImageResource(R.drawable.community_all_icon);
        }
        else
        {
            myHolder.tvTag.setVisibility(View.VISIBLE);
            myHolder.iv_dot.setVisibility(View.GONE);
            myHolder.forground.setVisibility(View.GONE);

            if ( !TextUtils.isEmpty(item.headicon))
            {
                ImageLoader.getInstance().displayImage(item.headicon, myHolder.roundedImageView1,
                        ImageUtil.getclub_topic_headImageOptions());
            }

        }


        myHolder.tvTag.setText(item.shortDesc);
        myHolder.tvNikeName.setText(item.nickname);




        if ( mOnItemClickLitener != null )
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

        public MyHolder(View itemView) {
            super(itemView);

            roundedImageView1 = (RoundedImageView) itemView.findViewById(R.id.roundedImageView1);
            tvNikeName = (TextView) itemView.findViewById(R.id.tv_nikeName);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            forground = (RoundedImageView) itemView.findViewById(R.id.forground);
            iv_dot = (ImageView) itemView.findViewById(R.id.iv_dot);


        }

        private RoundedImageView roundedImageView1, forground;
        private TextView tvNikeName;
        private TextView tvTag;
        private ImageView iv_dot;



    }




}
