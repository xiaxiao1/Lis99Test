package com.lis99.mobile.club.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lis99.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class ImagePageAdapter extends PagerAdapter {
	private List<View> banners;
	private ImagePageAdapterListener mListener;
	
	public static interface ImagePageClickListener{
		public void onClick(int index);
	}
	
	private ImagePageClickListener imagePageClickListener;
	
	

	public ImagePageClickListener getImagePageClickListener() {
		return imagePageClickListener;
	}

	public void setImagePageClickListener(
			ImagePageClickListener imagePageClickListener) {
		this.imagePageClickListener = imagePageClickListener;
	}

	public ImagePageAdapter(Context context, int pageCount) {
		banners = new ArrayList<View>(pageCount);
		initBanners(context, pageCount);
	}

	public void addImagePageAdapterListener(
			ImagePageAdapterListener imagePageAdapterListener) {
		mListener = imagePageAdapterListener;
	}

	private void initBanners(Context context, int pageCount) {
		pageCount = pageCount + 2;
		for (int index = 0; index < pageCount; index++) {
//			ImageView imageView = new ImageView(context);
//			imageView.setScaleType(ScaleType.FIT_XY);
			View v = View.inflate(context, R.layout.club_banner, null);
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

		ImageView iv_load = (ImageView) banner.findViewById(R.id.iv_load);
		ImageView iv_banner = (ImageView) banner.findViewById(R.id.iv_banner);

		if (mListener != null) {
			int index = position;
			if (position == 0) {
				mListener.dispalyImage(iv_banner, iv_load, getCount() - 2 - 1);
				index = getCount() - 2 - 1;
			} else if (position == getCount() - 1) {
				mListener.dispalyImage(iv_banner, iv_load, 0);
				index = 0;
			} else {
				mListener.dispalyImage(iv_banner, iv_load, position - 1);
				index = position - 1;
			}
			final int realIndex = index;
			banner.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (imagePageClickListener != null) {
						imagePageClickListener.onClick(realIndex);
					}
				}
			});
		}
		((ViewPager) container).addView(banner);
		return banner;
	}

	public interface ImagePageAdapterListener {
		void dispalyImage(ImageView banner, ImageView iv_load, int position);
	}

}
