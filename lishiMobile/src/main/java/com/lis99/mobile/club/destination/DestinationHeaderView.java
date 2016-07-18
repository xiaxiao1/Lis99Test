package com.lis99.mobile.club.destination;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangjie on 7/12/16.
 */

public class DestinationHeaderView extends LinearLayout {


    public static interface OnTabSelectListener{
        void onTabSelected(int tabIndex);
    }

    private OnTabSelectListener onTabSelectListener;

    public OnTabSelectListener getOnTabSelectListener() {
        return onTabSelectListener;
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.onTabSelectListener = onTabSelectListener;
    }

    @Bind(R.id.coverView)
    public ImageView coverView;
    @Bind(R.id.nameView)
    TextView nameView;
    @Bind(R.id.infoView)
    TextView infoView;
    @Bind(R.id.activityView)
    TextView activityView;
    @Bind(R.id.noteView)
    TextView noteView;

    TextView selectedView;

    private Destination destination;


    public DestinationHeaderView(Context context) {
        this(context, null);
    }

    public DestinationHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.destination_header_view, this, true);
        ButterKnife.bind(this, this);
    }

    @OnClick({R.id.moreButton, R.id.activityPanel, R.id.notePanel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moreButton:
            {
                Intent intent = new Intent(getContext(), DestinationInfoActivity.class);
                intent.putExtra("destID" , destination.id);
                getContext().startActivity(intent);
            }
            break;
            case R.id.activityPanel:
            {
                if (selectedView != activityView) {
                    selectedView = activityView;
                    Drawable drawable= getResources().getDrawable(R.drawable.icon_destination_activity_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    activityView.setCompoundDrawables(drawable,null,null,null);
                    activityView.setTextColor(Color.parseColor("#2bca63"));

                    drawable= getResources().getDrawable(R.drawable.icon_destination_note);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    noteView.setCompoundDrawables(drawable,null,null,null);
                    noteView.setTextColor(Color.parseColor("#999999"));

                    if (onTabSelectListener != null) {
                        onTabSelectListener.onTabSelected(0);
                    }
                }
            }
            break;
            case R.id.notePanel:
            {
                if (selectedView != noteView) {
                    selectedView = noteView;
                    Drawable drawable= getResources().getDrawable(R.drawable.icon_destination_note_selected);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    noteView.setCompoundDrawables(drawable,null,null,null);
                    noteView.setTextColor(Color.parseColor("#2bca63"));

                    drawable= getResources().getDrawable(R.drawable.icon_destination_activity);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    activityView.setCompoundDrawables(drawable,null,null,null);
                    activityView.setTextColor(Color.parseColor("#999999"));

                    if (onTabSelectListener != null) {
                        onTabSelectListener.onTabSelected(1);
                    }
                }
            }
            break;
        }
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
        ImageLoader.getInstance().displayImage(destination.image, coverView, ImageUtil.getclub_topic_imageOptions());
        nameView.setText(destination.title);
        infoView.setText(destination.describe);

    }
}
