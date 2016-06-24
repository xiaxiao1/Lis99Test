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
 * Created by yy on 16/6/22.
 */
public class CommunityStarAdapter extends RecyclerView.Adapter<CommunityStarAdapter.MyHolder> {

    private LayoutInflater mInflater;
    private List<Object> listInfo;
    private Context mContext;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }


    private OnItemClickLitener mOnItemClickLitener;

    public CommunityStarAdapter(List<Object> listInfo, Context mContext) {
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

        }

        private RoundedImageView roundedImageView1;
        private TextView tvNikeName;
        private TextView tvTag;


    }




}
