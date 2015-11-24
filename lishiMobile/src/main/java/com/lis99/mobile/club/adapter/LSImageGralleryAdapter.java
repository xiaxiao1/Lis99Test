package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by zhangjie on 11/22/15.
 */
public class LSImageGralleryAdapter extends PagerAdapter {

    private Context mContext;

    // 所要显示的图片的数组
    List<String> photos;

    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    // 构造方法
    public LSImageGralleryAdapter(Context mContext, List<String> photos) {
        super();
        this.mContext = mContext;
        this.photos = photos;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bigimage_def)
                .showImageForEmptyUri(R.drawable.bigimage_def)
                .showImageOnFail(R.drawable.bigimage_def).cacheInMemory(true)
                .cacheOnDisk(true).displayer(new SimpleBitmapDisplayer())
                .build();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        imageLoader.displayImage(photos.get(position), iv,
                options);
        collection.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (photos == null) {
            return 0;
        }
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}