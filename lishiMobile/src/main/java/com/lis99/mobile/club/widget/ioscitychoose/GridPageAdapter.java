package com.lis99.mobile.club.widget.ioscitychoose;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lis99.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/7/5.
 */
public class GridPageAdapter extends PagerAdapter {

    private List<View> banners;
    private GridPageAdapterListener mListener;

    public static interface GridPageClickListener{
        public void onClick(int index);
    }

    private GridPageClickListener gridPageClickListener;



    public GridPageClickListener getImagePageClickListener() {
        return gridPageClickListener;
    }

    public void setGridPageClickListener(
            GridPageClickListener imagePageClickListener) {
        this.gridPageClickListener = imagePageClickListener;
    }

    public GridPageAdapter (Context context, int pageCount) {
        banners = new ArrayList<View>(pageCount);
        initBanners(context, pageCount);
    }

    public void addGridPageAdapterListener(
            GridPageAdapterListener imagePageAdapterListener) {
        mListener = imagePageAdapterListener;
    }

    private void initBanners(Context context, int pageCount) {
        pageCount = pageCount + 2;
        for (int index = 0; index < pageCount; index++) {
//			ImageView imageView = new ImageView(context);
//			imageView.setScaleType(ScaleType.FIT_XY);
            View v = View.inflate(context, R.layout.grid_in_active_banner, null);
            banners.add(v);
        }
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(View container,int position) {
        View banner = banners.get(position);

        GridView grid = (GridView) banner.findViewById(R.id.grid);

        if (mListener != null) {
            int index = position;
            if (position == 0) {
                mListener.dispalyImage(grid, getCount() - 2 - 1);
                index = getCount() - 2 - 1;
            } else if (position == getCount() - 1) {
                mListener.dispalyImage(grid, 0);
                index = 0;
            } else {
                mListener.dispalyImage(grid, position - 1);
                index = position - 1;
            }
            final int realIndex = index;
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gridPageClickListener != null) {
                        gridPageClickListener.onClick(realIndex);
                    }
                }
            });
        }
        ((ViewPager) container).addView(banner);
        return banner;
    }

    public interface GridPageAdapterListener {
        void dispalyImage(GridView gridView, int position);
    }
}
