package com.lis99.mobile.mine.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.mine.model.LSMyActivity;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangjie on 5/21/16.
 */
public class LSMyActivityAdapter extends MyBaseAdapter {

    private boolean visibleLine = true;

    static Map<Integer, String> commentStatus = new HashMap<Integer, String>();
    static Map<Integer, String> payStatus = new HashMap<Integer, String>();

    static {
        commentStatus.put(-1, "被拒绝");
        commentStatus.put(1, "通过");
        commentStatus.put(2, "待审核");

        payStatus.put(0, "支付中");
        payStatus.put(1, "已支付");
        payStatus.put(2, "退款已完成");
        payStatus.put(3, "退款申请中");
        payStatus.put(4, "线下支付");
        payStatus.put(5, "免费活动");
        payStatus.put(6, "退款处理中");
        payStatus.put(7, "逾期未支付");
        payStatus.put(8, "订单关闭");
        payStatus.put(9, "无法退款");
        payStatus.put(10, "取消报名");
    }

    public LSMyActivityAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    public void setVisibleLine (boolean visibleLine)
    {
        this.visibleLine = visibleLine;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.mine_myactivity_item, null );

            holder = new Holder();

            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.titleView = (TextView) view.findViewById(R.id.titleView);
            holder.timeView = (TextView) view.findViewById(R.id.timeView);
            holder.commentStatusView = (TextView) view.findViewById(R.id.commentStatusView);
            holder.payStatusView = (TextView)view.findViewById(R.id.payStatusView);
            holder.sepAll = view.findViewById(R.id.sepAll);
            holder.sepHalf = view.findViewById(R.id.sepHalf);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        if ( getCount() == (i + 1) )
        {
            holder.sepAll.setVisibility(View.VISIBLE);
            holder.sepHalf.setVisibility(View.GONE);
        }
        else
        {
            holder.sepAll.setVisibility(View.GONE);
            holder.sepHalf.setVisibility(View.VISIBLE);
        }

        LSMyActivity item = (LSMyActivity) getItem(i);

        ImageLoader.getInstance().displayImage(item.image, holder.imageView, ImageUtil.getImageOptionClubIcon());

        holder.titleView.setText(item.topic_title);
        holder.timeView.setText(item.createTime + "报名");

        if (item.flag == -1 || item.flag == 2) {
            holder.commentStatusView.setTextColor(Color.rgb(0xf1, 0x5b, 0x5a));
        } else {
            holder.commentStatusView.setTextColor(Color.rgb(0x8d, 0x8c, 0x7c));
        }

        if (item.pay_status == 0) {
            holder.payStatusView.setTextColor(Color.rgb(0xf1, 0x5b, 0x5a));
        } else {
            holder.payStatusView.setTextColor(Color.rgb(0x8d, 0x8c, 0x7c));
        }

        holder.commentStatusView.setText("   " + commentStatus.get(item.flag) + "   ");
        holder.payStatusView.setText("   " + payStatus.get(item.pay_status) + "   ");

        return view;
    }

    public class Holder
    {
        public ImageView imageView;
        public TextView titleView, timeView, commentStatusView, payStatusView;
        public View sepAll, sepHalf;
    }

}
