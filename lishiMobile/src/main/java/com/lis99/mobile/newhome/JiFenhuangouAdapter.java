package com.lis99.mobile.newhome;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.ActivityUtil;

import java.util.List;

/**
 * Created by xiaxiao on 2016/9/13.
 */

class JiFenhuangouAdapter<T> extends RecyclerView.Adapter<JiFenhuangouAdapter.MyViewHolder>
{
    Context context;
    List<T> datas;

    public JiFenhuangouAdapter(Context context, List<T> datas) {
        this.context=context;
        this.datas = datas;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_of_jifenhuangou_recyclerview, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        String tagName=datas.get(position).name;
        if (tagName.length() > 7) {
            holder.tv.setText("    "+tagName.substring(0,7) + "..." + "    ");
        } else {
            holder.tv.setText("    "+tagName+"    ");
        }
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                ActivityUtil.goSpecialInfoActivity(ListFragment.this.getActivity(), recycler_datas.get(position).id);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return recycler_datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.afullinfo_recycler_item_tv);
        }
    }
}