package com.lis99.mobile.newhome;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.Common;

import java.util.List;

/**
 * Created by xiaxiao on 2016/9/13.
 * 福利社页 横向滑动的的 积分换购  部分
 */

class JiFenhuangouAdapter extends RecyclerView.Adapter<JiFenhuangouAdapter.MyViewHolder>
{
    Context context;
    List<String> datas;

    private static final int ITEM_TYPE_NORMAL=0;
    private static final int ITEM_TYPE_FOOTER=1;

    public JiFenhuangouAdapter(Context context, List<String> datas) {
        this.context=context;
        this.datas = datas;
    }

    @Override
    public int getItemCount()
    {
        return datas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == ITEM_TYPE_NORMAL) {

            View v = LayoutInflater.from(
                    context).inflate(R.layout.item_of_jifenhuangou_recyclerview, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(v, viewType);
            return holder;
        } else {
            View v = LayoutInflater.from(
                    context).inflate(R.layout.item_of_jifenhuangou_recyclerview_load_more, parent,
                    false);
            MyViewHolder holder = new MyViewHolder(v, viewType);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        if (position==datas.size()&&holder.viewType==ITEM_TYPE_FOOTER) {
            holder.goodsLoadMore_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.toast("load more");
                }
            });
        }else {
            String tagName=datas.get(position);
            holder.goodsNameTv.setText("  " + tagName + "  ");
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.toast(datas.get(position));
                }
            });
        }
        /*holder.tv.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                ActivityUtil.goSpecialInfoActivity(ListFragment.this.getActivity(), recycler_datas.get(position).id);
            }
        });*/
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {

        //正常item的
        private ImageView goodsImg;
        private TextView goodsNameTv;
        private TextView goodsJifenNumTv;
        private TextView goodsDuihuanTv;
        private int viewType;

        //footer布局的
        private ImageView goodsLoadMore_Img;

        public MyViewHolder(View view,int viewType)
        {
            super(view);
            this.viewType=viewType;
            if (viewType == ITEM_TYPE_NORMAL) {

                goodsImg = (ImageView) view.findViewById(R.id.goods_img);
                goodsNameTv = (TextView) view.findViewById(R.id.goods_name_tv);
                goodsJifenNumTv = (TextView) view.findViewById(R.id.goods_jifen_num_tv);
                goodsDuihuanTv = (TextView) view.findViewById(R.id.goods_duihuan_tv);
            } else {
                goodsLoadMore_Img = (ImageView) view.findViewById(R.id.goods_loadmore_img);
            }
        }

        public View getItemView() {
            return this.itemView;
        }
    }

}