package com.lis99.mobile.util.emotion;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import com.lis99.mobile.R;

import java.util.ArrayList;

public class EmoticonsPagerAdapter extends PagerAdapter {

    ArrayList<ArrayList<Object>> emoticons;
    private static final int NO_OF_EMOTICONS_PER_PAGE = 13;
    Context mActivity;
    EmoticonsGridAdapter.KeyClickListener mListener;

    public EmoticonsPagerAdapter(Context activity,
                                 ArrayList<ArrayList<Object>> emoticons, EmoticonsGridAdapter.KeyClickListener listener) {
        this.emoticons = emoticons;
        this.mActivity = activity;
        this.mListener = listener;
    }



    @Override
    public int getCount() {
        return (int) Math.ceil((double) emoticons.size()
                / (double) NO_OF_EMOTICONS_PER_PAGE);
    }

    @Override
    public Object instantiateItem(View collection, int position) {

        View layout = View.inflate(mActivity,
                R.layout.emoticons_grid, null);

        int initialPosition = position * NO_OF_EMOTICONS_PER_PAGE;
        ArrayList<ArrayList<Object>> emoticonsInAPage = new ArrayList<ArrayList<Object>>();

        for (int i = initialPosition; i < initialPosition
                + NO_OF_EMOTICONS_PER_PAGE
                && i < emoticons.size(); i++) {
            emoticonsInAPage.add(emoticons.get(i));
        }

        //添加到最后一个
        if ( emoticonsInAPage.size() < (NO_OF_EMOTICONS_PER_PAGE + 1) )
        {
            int size = emoticonsInAPage.size();
            for ( int i = size; i < (NO_OF_EMOTICONS_PER_PAGE + 1); i++ )
            {
                if ( i == NO_OF_EMOTICONS_PER_PAGE )
                {
                    ArrayList<Object> a = new ArrayList<Object>();
                    a.add("back");
                    emoticonsInAPage.add(a);
                    break;
                }
                ArrayList<Object> a = new ArrayList<Object>();
                a.add("null");
                emoticonsInAPage.add(a);
            }
        }

        GridView grid = (GridView) layout.findViewById(R.id.emoticons_grid);
        EmoticonsGridAdapter adapter = new EmoticonsGridAdapter(
                mActivity, emoticonsInAPage, position,
                mListener);
        grid.setAdapter(adapter);

        ((ViewPager) collection).addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}