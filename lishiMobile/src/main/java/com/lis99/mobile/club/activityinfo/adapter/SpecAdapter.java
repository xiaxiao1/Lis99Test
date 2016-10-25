package com.lis99.mobile.club.activityinfo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.activityinfo.SericeCalendarActivity;
import com.lis99.mobile.club.model.BatchListEntity;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yy on 16/8/26.
 *  选择规格列表
 */
public class SpecAdapter extends MyBaseAdapter {

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getBatchId ()
    {
        BatchListEntity item = (BatchListEntity) getItem(currentPosition);

        if ( item.isEnd == 1 /*|| item.isBaoming == 1*/ )
        {
            return -1;
        }

        return item.batchId;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    private int currentPosition = -1;

    public SpecAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext,R.layout.series_spec_adapter, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag(), i);
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder, int i) {
        //TODO implement
        BatchListEntity item = (BatchListEntity) object;

        holder.tvName.setText(item.describe+",¥"+item.price);

        holder.tvTime.setText("集合时间"+item.settime);

        int state = 1;

        if ( item.isEnd == 1 )
        {
            state = 3;
        }
//        else if ( item.isBaoming == 1 )
//        {
//            state = 2;
//        }
        else if ( currentPosition == i )
        {
            state = 4;
        }
        else if ( currentPosition == -1 )
        {
            currentPosition = i;
            ((SericeCalendarActivity)mContext).setBtnClick(true);
        }

        holder.iv_checked.setImageResource(R.drawable.spec_normal);

        switch ( state )
        {
//                默认
            case 1:
                holder.tvStatus.setVisibility(View.GONE);
                holder.iv_checked.setVisibility(View.VISIBLE);
                break;
//                    已报名
            case 2:
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.iv_checked.setVisibility(View.GONE);
                holder.tvStatus.setText("已报名");
                break;
//                报名截止
            case 3:
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.iv_checked.setVisibility(View.GONE);
                holder.tvStatus.setText("已截止");
                break;
//                选中
            case 4:
                holder.tvStatus.setVisibility(View.GONE);
                holder.iv_checked.setVisibility(View.VISIBLE);
                holder.iv_checked.setImageResource(R.drawable.spec_select);
                break;
        }


    }

    static class ViewHolder {
        @Bind(R.id.fl_right)
        FrameLayout flRight;
        @Bind(R.id.iv_checked)
        ImageView iv_checked;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_name) TextView tvName;
        @Bind(R.id.tv_time) TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
