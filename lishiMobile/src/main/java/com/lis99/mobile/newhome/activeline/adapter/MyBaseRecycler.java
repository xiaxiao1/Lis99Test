package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by yy on 16/7/7.
 */
public abstract class MyBaseRecycler<M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<M> {

    protected List<?> list;
    protected Context mContext;

    public interface OnItemClickLitener
    {
        void onItemClick(int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public MyBaseRecycler(List<?> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public abstract boolean getInfo (M m, int i);

    @Override
    public void onBindViewHolder(final M m, final int i) {
        getInfo(m, i);
        if ( mOnItemClickLitener == null ) return;
        m.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemClick(i);
            }
        });
    }

    public Object getItem ( int i )
    {
        return ( list == null || list.size() == 0 ) ? null : list.get(i);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

}
