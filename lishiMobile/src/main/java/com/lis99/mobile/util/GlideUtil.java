package com.lis99.mobile.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

    private AnimationDrawable animationDrawable;
    /**
     *      有默认加载动画的
     * @param imgDot
     * @param img
     * @return
     */
    public void getWithWhiteBG (Activity activity, String url, ImageView img, final ImageView imgDot )
    {

        Glide.with(activity)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.club_topic_default)
                .error(R.drawable.club_topic_default)
                .fallback(R.drawable.club_topic_default)
                .into( new GlideLoadingAnimation(img, imgDot));
    }

    public static class GlideLoadingAnimation extends SimpleTarget<Bitmap> {
        //3个点， 背影（展示的图片）
        private ImageView imgDot, imgRes;
        private AnimationDrawable animationDrawable;

        public GlideLoadingAnimation(ImageView imgRes, ImageView imgDot) {
            this.imgRes = imgRes;
            this.imgDot = imgDot;

            this.imgDot.setVisibility(View.VISIBLE);
            animationDrawable = (AnimationDrawable)imgDot.getDrawable();
            animationDrawable.start();

        }

        /**
         * The method that will be called when the resource load has finished.
         *
         * @param resource       the loaded resource.
         * @param glideAnimation
         */
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            this.imgDot.setVisibility(View.GONE);
            if ( animationDrawable != null )
            {
                animationDrawable.stop();
                animationDrawable = null;
            }
            this.imgRes.setImageBitmap(resource);
        }
    }







}
