package com.lis99.mobile.newhome.activeline.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;


/**
 * Created by yy on 16/7/7.
 */
public class ActiveMainRecommendAdapter extends MyBaseAdapter {

    private final int count = 2;

    private int info = 0;

    private  int ad = 1;


    public ActiveMainRecommendAdapter(Activity c, List listItem) {
        super(c, listItem);
    }


    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        if ( ad == getItemViewType(i) )
        {
            return getAD(i, view, viewGroup);
        }

        if (view == null) {
            view = View.inflate(mContext, R.layout.active_recommend_adapter, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private View getAD ( int i, View view, ViewGroup viewGroup )
    {
        BannerHolder bannerHolder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_line_hot_ad, null);
            view.setTag(new BannerHolder(view));
        }
        else
        {
            bannerHolder = (BannerHolder) view.getTag();
        }

        final BannerHolder finalBannerHolder = bannerHolder;
        bannerHolder.bannerAD.mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        finalBannerHolder.bannerAD.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        finalBannerHolder.bannerAD.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        finalBannerHolder.bannerAD.startAutoScroll();
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        ImagePageAdapter bannerAdapter=new ImagePageAdapter(mContext, 0);
        bannerAdapter.addImagePageAdapterListener(new ImagePageAdapter.ImagePageAdapterListener() {
            @Override
            public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

            }
        });
        bannerAdapter.setImagePageClickListener(new ImagePageAdapter.ImagePageClickListener() {
            @Override
            public void onClick(int index) {

            }
        });
        bannerHolder.bannerAD.setBannerAdapter(bannerAdapter);
        bannerHolder.bannerAD.startAutoScroll();


        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        final ActiveMainHeadModel.HotlistEntity item = (ActiveMainHeadModel.HotlistEntity) object;
        if ( item == null ) return;
        holder.title.setText(item.title);

        if ( item.actlist == null ) return;

        ActiveMainRecommendRecycler recycler = new ActiveMainRecommendRecycler(item.actlist, mContext);
        holder.recyclerView.setAdapter(recycler);
        recycler.setOnItemClickLitener(new MyBaseRecycler.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                int topicId = item.actlist.get(position).topicId;
                Common.goTopic(mContext, 4, topicId);

            }
        });

    }

    protected static class BannerHolder
    {
        private BannerView bannerAD;

        public BannerHolder( View view ) {
            this.bannerAD = (BannerView) view.findViewById(R.id.bannerAD);
        }
    }

    protected class ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
