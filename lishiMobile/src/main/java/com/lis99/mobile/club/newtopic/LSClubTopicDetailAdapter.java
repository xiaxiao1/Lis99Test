package com.lis99.mobile.club.newtopic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubTopicActiveLineMainModel;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/12.
 */
public class LSClubTopicDetailAdapter extends MyBaseAdapter {

    public LSClubTopicDetailAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.adapter_club_info_detail_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tvInfo = (TextView) view.findViewById(R.id.tv_info);
            viewHolder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
            viewHolder.tvDescrible = (TextView) view.findViewById(R.id.tv_describle);
            viewHolder.layout_iv = view.findViewById(R.id.layout_iv);
            viewHolder.contentImageView = (ImageView) view.findViewById(R.id.contentImageView);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ClubTopicActiveLineMainModel.ActivitydetailEntity item = (ClubTopicActiveLineMainModel.ActivitydetailEntity) getItem(i);

        if ( item == null ) return view;

        viewHolder.tvTitle.setVisibility(View.GONE);
        viewHolder.tvInfo.setVisibility(View.GONE);
        viewHolder.layout_iv.setVisibility(View.GONE);
        viewHolder.tvDescrible.setVisibility(View.GONE);

        if ( item.getContent() != null )
        {
            viewHolder.tvInfo.setVisibility(View.VISIBLE);
            viewHolder.tvInfo.setText(item.getContent());
        }

        if (!TextUtils.isEmpty(item.getImages()))
        {
            viewHolder.layout_iv.setVisibility(View.VISIBLE);
            getWidth(viewHolder.contentImageView, viewHolder.iv_load, item.getImages());
        }




        return view;
    }


    protected class ViewHolder {
        private View layout_iv;
        private TextView tvTitle;
        private TextView tvInfo;
        private ImageView contentImageView, iv_load;
        private TextView tvDescrible;
    }



    int ImageWidth = Common.WIDTH;// - Common.dip2px(10);
    private AnimationDrawable animationDrawable;

    private void getWidth(final ImageView v,final ImageView load, final String item)
    {
        animationDrawable = (AnimationDrawable) load.getDrawable();
        if ( !animationDrawable.isRunning() )
            animationDrawable.start();

        if (ImageWidth == -1)
        {
            ViewTreeObserver vto = v.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout()
                {
                    ImageWidth = v.getWidth();
                    ViewTreeObserver obs = v.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);

                    ImageLoader.getInstance().displayImage(
                            item, v, ImageUtil.getclub_topic_imageOptions(),
                            new ImageLoadingListener()
                            {

                                @Override
                                public void onLoadingStarted(String imageUri,
                                                             View view)
                                {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onLoadingFailed(String imageUri,
                                                            View view, FailReason failReason)
                                {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view, Bitmap loadedImage)
                                {
                                    // TODO Auto-generated method stub
                                    load.setVisibility(View.GONE);
                                    if ( animationDrawable != null )
                                    {
                                        animationDrawable.stop();
                                        animationDrawable = null;
                                    }
                                    int w = loadedImage.getWidth();
                                    int h = loadedImage.getHeight();
                                    int imgh = ImageWidth * h / w;
                                    android.view.ViewGroup.LayoutParams l = v
                                            .getLayoutParams();
                                    l.height = imgh;
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri,
                                                               View view)
                                {
                                    // TODO Auto-generated method stub

                                }
                            });

                }
            });
        } else
        {
            ImageLoader.getInstance().displayImage(
                    item, v, ImageUtil.getclub_topic_imageOptions(),
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            load.setVisibility(View.GONE);
                            if (animationDrawable != null) {
                                animationDrawable.stop();
                                animationDrawable = null;
                            }
                            int w = loadedImage.getWidth();
                            int h = loadedImage.getHeight();
                            int imgh = ImageWidth * h / w;
                            android.view.ViewGroup.LayoutParams l = v
                                    .getLayoutParams();
                            l.height = imgh;
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                                       View view) {
                            // TODO Auto-generated method stub

                        }
                    });
        }

    }


}
