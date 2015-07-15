package com.lis99.mobile.entry.view.scroll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lis99.mobile.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadingLayout extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ImageView headerImage;
	private final ProgressBar headerProgress;
	private final TextView headerText;
	private final TextView headerTimeText;

	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	
	private SimpleDateFormat format;
	
	private Date date;
	private Context context;

	private final Animation rotateAnimation, resetRotateAnimation;

	public LoadingLayout(Context context, final int mode, String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);
		this.context = context;
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.ls_common_refresh_header, this);
		headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		headerTimeText = (TextView) header.findViewById(R.id.pull_to_refresh_time);
		headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
		headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);

		final Interpolator interpolator = new LinearInterpolator();
		rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		        0.5f);
		rotateAnimation.setInterpolator(interpolator);
		rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		rotateAnimation.setFillAfter(true);

		resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
		        Animation.RELATIVE_TO_SELF, 0.5f);
		resetRotateAnimation.setInterpolator(interpolator);
		resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		resetRotateAnimation.setFillAfter(true);

		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;

		switch (mode) {
			case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
				headerImage.setImageResource(R.drawable.common_arrow_up);
				break;
			case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
			default:
				headerImage.setImageResource(R.drawable.common_arrow_down);
				break;
		}
	}

	public void reset() {
		headerText.setText(pullLabel);
		date = new Date();
		headerTimeText.setText(context.getString(R.string.lasttime_refresh) + getFormatDate());
		headerImage.setVisibility(View.VISIBLE);
		headerProgress.setVisibility(View.GONE);
	}

	public void releaseToRefresh() {
		headerText.setText(releaseLabel);
		headerTimeText.setText(context.getString(R.string.lasttime_refresh) + getFormatDate());
		headerImage.clearAnimation();
		headerImage.startAnimation(rotateAnimation);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	public void refreshing() {
		headerText.setText(refreshingLabel);
		headerTimeText.setText(context.getString(R.string.lasttime_refresh) + getFormatDate());
		headerImage.clearAnimation();
		headerImage.setVisibility(View.INVISIBLE);
		headerProgress.setVisibility(View.VISIBLE);
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		headerText.setText(pullLabel);
		headerTimeText.setText(context.getString(R.string.lasttime_refresh) + getFormatDate());
		headerImage.clearAnimation();
		headerImage.startAnimation(resetRotateAnimation);
	}

	public void setTextColor(int color) {
		headerText.setTextColor(color);
	}
	
	private String getFormatDate(){
		if (format == null) {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		if(date == null){
			date = new Date();
		}
		return format.format(date);
	}

}
