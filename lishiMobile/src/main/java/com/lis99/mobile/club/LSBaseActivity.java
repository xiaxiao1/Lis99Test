package com.lis99.mobile.club;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.util.StatusUtil;

public abstract class LSBaseActivity extends ActivityPattern {
	
	protected final static int SHOW_UI = 2001;
	ImageView iv_title_bg;
	ImageView titleLeftImage;
	TextView titleRightImage;
	TextView title;
	RelativeLayout titleRight;

	protected void initViews(){
		StatusUtil.setStatusBar(this);
		titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
		View titleLeft = findViewById(R.id.titleLeft);
		if(titleLeft != null){
			titleLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					leftAction();
				}
			});
		}

		if ( titleLeftImage != null )
		{
			titleLeftImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					leftAction();
				}
			});
		}

		titleRight = (RelativeLayout) findViewById(R.id.titleRight);
		if ( titleRight != null )
		{
			titleRight.setOnClickListener( new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					rightAction();
				}
			});
		}
		
		titleRightImage = (TextView) findViewById(R.id.titleRightImage);
		if ( titleRightImage != null )
		titleRightImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rightAction();
			}
		});
		
		iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
		if ( this.title == null )
		this.title = (TextView)findViewById(R.id.title);
		
	}
	
	public void setTitleBarAlpha ( float alpha )
	{
		if ( iv_title_bg == null ) return;
		iv_title_bg.setAlpha(alpha);
		if (  title  == null ) return;
		title.setAlpha(alpha);
	}
	
	protected void rightAction() {
	}

	protected void leftAction() {
		finish();
	}

	protected TextView setTitle(String title){
		if ( this.title == null )
		this.title = (TextView)findViewById(R.id.title);
		if (this.title != null) {
			this.title.setText(title);
		}
		return this.title;
	}
	/**
	 * 			设置右边的按钮
	 * @param res
	 */
	protected void setRightView ( int res )
	{
		if ( titleRightImage != null )
		{
			titleRightImage.setVisibility(View.VISIBLE);
			titleRightImage.setBackgroundResource(res);
		}
	}
	//设置左边按钮图片
	protected void setLeftView( int res )
	{
		if ( titleLeftImage != null )
		{
			titleLeftImage.setImageResource(res);
		}
	}
	
	protected void setRightView ( String str )
	{
		if ( titleRightImage != null )
		{
			titleRightImage.setVisibility(View.VISIBLE);
			titleRightImage.setText(str);
		}
	}
	
	
}
