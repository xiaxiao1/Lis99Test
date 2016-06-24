package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;

import java.util.List;

/**
 * Created by yy on 16/6/23.
 */
public class HotTalkRecycler extends RecyclerView.Adapter<HotTalkRecycler.MyHolder> {

    private LayoutInflater mInflater;
    private List<Object> listInfo;
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

    public HotTalkRecycler(List<Object> listInfo, Context mContext) {
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
        return 0;
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
