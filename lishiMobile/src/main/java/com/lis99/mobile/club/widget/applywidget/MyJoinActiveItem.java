package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.apply.MyJoinActiveInfoActivity;
import com.lis99.mobile.club.model.MyJoinActiveModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveItem extends MyBaseAdapter {

    public MyJoinActiveItem(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;

        if ( view == null  )
        {
            view = View.inflate(mContext, R.layout.my_join_active_item, null);

            holder = new Holder();

            holder.roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_data = (TextView) view.findViewById(R.id.tv_data);
            holder.tv_pay_state = (TextView) view.findViewById(R.id.tv_pay_state);
            holder.iv_status = (ImageView) view.findViewById(R.id.iv_status);
            holder.iv_pay_state = (ImageView) view.findViewById(R.id.iv_pay_state);
            holder.view_line = view.findViewById(R.id.view_line);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        if (i == 0 )
        {
            holder.view_line.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.view_line.setVisibility(View.GONE);
        }

        MyJoinActiveModel.Lists item = (MyJoinActiveModel.Lists) getItem(i);

        if ( item == null ) return view;

        holder.tv_title.setText(item.topic_title);
        holder.tv_data.setText(item.createTime+" 报名");
        String pay_state = "";
        if ( item.pay_status >= 0 && item.pay_status < MyJoinActiveInfoActivity.PAY_TYPE.length )
        {
            pay_state = MyJoinActiveInfoActivity.PAY_TYPE[item.pay_status];
        }
        //    支付状态（0待支付1已支付 2退款已完成 3退款申请中 4 线下支付 5免费活动 6 退款中 7逾期未支付 8 无法付款 9 无法退款）
//        switch (item.pay_status )
//        {
//            case 0:
//                pay_state = "待支付";
//                break;
//            case 1:
//                pay_state = "已支付";
//                break;
//            case 2:
//                pay_state = "退款已完成";
//                break;
//            case 3:
//                pay_state = "退款申请中";
//                break;
//            case 4:
//                pay_state = "线下支付";
//                break;
//            case 5:
//                pay_state = "免费活动";
//                break;
//            case 6:
//                pay_state = "退款中";
//                break;
//            case 7:
//                pay_state = "逾期未支付";
//                break;
//            case 8:
//                pay_state = "无法付款";
//                break;
//            case 9:
//                pay_state = "无法退款";
//                break;
//        }

        holder.tv_pay_state.setText(pay_state);

        switch (item.pay_type )
        {
            case 0:
                holder.iv_pay_state.setImageResource(R.drawable.pay_free);
                break;
            case 1:
                holder.iv_pay_state.setImageResource(R.drawable.pay_off_line);
                break;
            case 2:
                holder.iv_pay_state.setImageResource(R.drawable.pay_weixin);
                break;
            case 3:
                holder.iv_pay_state.setImageResource(R.drawable.pay_zhifubao);
                break;
        }

        switch (item.flag )
        {
            case 1:
                holder.iv_status.setImageResource(R.drawable.pay_status_enter);
                break;
            case -1:
                holder.iv_status.setImageResource(R.drawable.pay_status_refuse);
                break;
            case 2:
                holder.iv_status.setImageResource(R.drawable.pay_status_examine);
                break;
        }

        ImageLoader.getInstance().displayImage(item.image, holder.roundedImageView, ImageUtil.getDefultImageOptions());

        return view;
    }

    class Holder
    {
        RoundedImageView roundedImageView;
        TextView tv_title, tv_data, tv_pay_state;
        ImageView iv_status, iv_pay_state;
        View view_line;
    }
}
