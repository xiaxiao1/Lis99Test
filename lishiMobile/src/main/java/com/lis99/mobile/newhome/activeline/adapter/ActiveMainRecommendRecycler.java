package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.DestinationMainActivity;
import com.lis99.mobile.club.filter.FilterMainActivity;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/7/8.
 */
public class ActiveMainRecommendRecycler extends MyBaseRecycler<ActiveMainRecommendRecycler.VHolder> {


    static final int TYPE_MORE=1;//末端显示的更多活动
    static final int TYPE_NORMAL=0;//正常item
    @Override
    public int getItemViewType(int position) {
        ActiveMainHeadModel.HotlistEntity.ActlistEntity item = (ActiveMainHeadModel
                .HotlistEntity
                .ActlistEntity) list.get(position);
        //通过设置id来判断item类型，正常的item是正常的id,添加的用来标记末端是否有更多活动项的item的id设置为-1了
        if (item.topicId==-1) {
            return TYPE_MORE;
        } else {
            return TYPE_NORMAL;
        }
    }

    public ActiveMainRecommendRecycler(List<?> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public boolean getInfo(VHolder holder, final int i) {

        ActiveMainHeadModel.HotlistEntity.ActlistEntity item = (ActiveMainHeadModel
                .HotlistEntity
                .ActlistEntity) list.get(i);
        //正常item,没有显示末端更多项item
        if (getItemViewType(i) == TYPE_NORMAL) {
            if (item == null) return false;
            VHolder1 vHolder = (VHolder1) holder;
            vHolder.title.setText(item.topicTitle);
            vHolder.content.setText(item.harddesc + " " + item.cate_name);
            vHolder.price.setText("" + Common.getIntInString(item.price));

            if (!TextUtils.isEmpty(item.images)) {
                ImageLoader.getInstance().displayImage(item.images, vHolder.roundedImageView,
                        ImageUtil.getclub_topic_imageOptions());
            }
        } else {
            //当末端有更多项item时 分情况：
            VHolder2 vHolder = (VHolder2) holder;
            //全部目的地
            if (item.topicTitle.equals("mudidi")) {
                vHolder.bg_roundiv.setImageResource(R.drawable.all_destination_active);
                vHolder.title_tv.setText("全部目的地");
                vHolder.root_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.toast("i 我是第二个布局" + i);
                        Intent intent = new Intent(mContext, DestinationMainActivity.class);
                        mContext.startActivity(intent);

                    }
                });
            //全部附近的活动
            } else if (item.topicTitle.equals("fujin")) {
                vHolder.bg_roundiv.setImageResource(R.drawable.all_nearby_active);
                vHolder.title_tv.setText("全部附近的活动");
                vHolder.root_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.toast("i 我是第二个布局" + i);
                        Intent intent = new Intent(mContext, FilterMainActivity.class);
                        mContext.startActivity(intent);

                    }
                });
            //全部本地活动
            } else if (item.topicTitle.equals("bendi")) {
                vHolder.bg_roundiv.setImageResource(R.drawable.all_local_active);
                vHolder.title_tv.setText("全部本地活动");
                vHolder.root_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.toast("i 我是第二个布局 本地的还没做出来" + i);
                        /*Intent intent = new Intent(mContext, FilterMainActivity.class);
                        mContext.startActivity(intent);*/

                    }
                });
            }

        }
        return false;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        VHolder holder;
        if (viewType == TYPE_NORMAL) {
            View v = View.inflate(mContext, R.layout.active_recommend_recycler, null);
            holder = new VHolder1(v);
        } else {
            View v = View.inflate(mContext, R.layout.active_recommend_recycler_more, null);
            holder = new VHolder2(v);
        }

        return holder;
    }

    //多个Holder的父类，用于实现item多布局
    class VHolder extends RecyclerView.ViewHolder
    {
        public VHolder(View view) {
            super(view);
        }
    }

    /**
     * 正常item的布局
     */
    class VHolder1 extends ActiveMainRecommendRecycler.VHolder
    {

        private RoundedImageView roundedImageView;
        private TextView title;
        private TextView content;
        private TextView price;
        private TextView tvNikeName;

        public VHolder1(View view) {
            super(view);
            roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            price = (TextView) view.findViewById(R.id.price);
            tvNikeName = (TextView) view.findViewById(R.id.tv_nikeName);
        }
    }

    /**
     * 末端item的布局
     */
    class VHolder2 extends ActiveMainRecommendRecycler.VHolder
    {
        private LinearLayout root_ll;
        private TextView title_tv;
        private RoundedImageView bg_roundiv;
        public VHolder2(View view) {
            super(view);
            root_ll = (LinearLayout) view.findViewById(R.id.root_ll);
            title_tv = (TextView) view.findViewById(R.id.title_tv);
            bg_roundiv = (RoundedImageView) view.findViewById(R.id.bg_roundiv);

        }
    }

}

