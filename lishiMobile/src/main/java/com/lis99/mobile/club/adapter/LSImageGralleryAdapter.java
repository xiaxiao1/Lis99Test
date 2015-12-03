package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.TouchImageView;
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

    public static interface LSImageGralleryListner {
        void onClickPageView(View v);
    }

    public LSImageGralleryListner lsImageGralleryListner;

    // 构造方法
    public LSImageGralleryAdapter(Context mContext, List<String> photos) {
        super();
        this.mContext = mContext;
        this.photos = photos;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ls_image_gallery_def)
                .showImageForEmptyUri(R.drawable.ls_image_gallery_def)
                .showImageOnFail(R.drawable.ls_image_gallery_def).cacheInMemory(true)
                .cacheOnDisk(true).displayer(new SimpleBitmapDisplayer())
                .build();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TouchImageView iv = new TouchImageView(mContext);
//        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);

        collection.addView(iv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        imageLoader.displayImage(photos.get(position), iv,
                options);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lsImageGralleryListner != null) {
                    lsImageGralleryListner.onClickPageView(v);
                }
            }
        });
        //collection.addView(iv);
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
        return view == object;
    }

}