package com.lis99.mobile.util;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lis99.mobile.R;

/**
 * Created by yy on 16/7/26.
 */
public class GlideUtil {

    private static GlideUtil instance;

    public static GlideUtil getInstance ()
    {
        if ( instance == null ) instance = new GlideUtil();
        return instance;
    }

    /**
     *          用户头像
     * @param activity
     * @param url
     * @param img
     */
    public void getHeadImageView (Activity activity, String url, ImageView img )
    {
        Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.ls_nologin_header_icon)
                .error(R.drawable.ls_nologin_header_icon)
                .fallback(R.drawable.ls_nologin_header_icon)
                .crossFade()
                .into(img);
    }







}
