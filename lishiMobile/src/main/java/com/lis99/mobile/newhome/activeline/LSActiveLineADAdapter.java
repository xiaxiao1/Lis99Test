package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;

import java.util.List;

/**
 * Created by yy on 16/1/18.
 */
public class LSActiveLineADAdapter extends RecyclerView.Adapter<LSActiveLineADAdapter.ViewHolder> {



    private LayoutInflater mInflater;
    private List<Object> listInfo;

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public LSActiveLineADAdapter(Context context, List<Object> listInfo) {
        mInflater = LayoutInflater.from(context);
        this.listInfo = listInfo;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private TextView tvName;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.active_line_ad_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
        viewHolder.ivLoad = (ImageView) view.findViewById(R.id.iv_load);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {




        if (mOnItemClickLitener != null)
        {

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if ( listInfo == null ) return 0;
        return listInfo.size();
    }
}
