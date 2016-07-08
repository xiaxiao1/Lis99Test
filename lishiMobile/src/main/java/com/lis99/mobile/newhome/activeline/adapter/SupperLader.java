package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yy on 16/7/8.
 */
public class SupperLader extends MyBaseRecycler<SupperLader.MyHolder> {


    public SupperLader(List<?> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public boolean getInfo(MyHolder myHolder, int i) {
        return false;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {

        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
