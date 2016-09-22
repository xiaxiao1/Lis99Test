package com.lis99.mobile.newhome;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.WelfareModel;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xiaxiao on 2016/9/13.
 * 福利社页 横向滑动的的 积分换购  部分
 */

class JiFenhuangouAdapter extends RecyclerView.Adapter<JiFenhuangouAdapter.MyViewHolder>
{
    Context context;
    List<WelfareModel.JfgoodsBean> datas;

    //普通item
    private static final int ITEM_TYPE_NORMAL=0;
    //footer
    private static final int ITEM_TYPE_FOOTER=1;

    public JiFenhuangouAdapter(Context context, List<WelfareModel.JfgoodsBean> datas) {
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
        //footer :加载更多
        if (position==datas.size()&&holder.viewType==ITEM_TYPE_FOOTER) {
            holder.goodsLoadMore_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = 0;
                    String Userid = DataManager.getInstance().getUser().getUser_id();
                    if (!TextUtils.isEmpty(Userid)) {
                        id = Integer.parseInt(Userid);
                    }
                    Intent intent = new Intent(context, MyActivityWebView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodList/" + id);
                    bundle.putString("TITLE", "积分商城");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else {
            //正常item 显示各种信息
            final WelfareModel.JfgoodsBean jfGood=datas.get(position);
            holder.goodsNameTv.setText("  " +jfGood.getTitle());
            ImageLoader.getInstance().displayImage(jfGood.getImages(),holder.goodsImg, ImageUtil.getDefultImageOptions());
            holder.goodsJifenNumTv.setText(""+jfGood.getMarket_price()+"积分");
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = 0;
                    String Userid = DataManager.getInstance().getUser().getUser_id();
                    if (!TextUtils.isEmpty(Userid))
                    {
                        id = Integer.parseInt(Userid);
                    }

                    Intent intent = new Intent(context, MyActivityWebView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodDetail/"+jfGood.getId()+"/"+id);
                    bundle.putString("TITLE", "积分商城");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

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