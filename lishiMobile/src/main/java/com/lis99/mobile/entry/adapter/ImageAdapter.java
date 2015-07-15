package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.Pic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class ImageAdapter<T> extends AdapterBase<T> {
	private ImageView imageViews;
	private Activity activity;

	protected DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageAdapter(Activity act, List<T> list) {
		super(act, list);
		this.activity = act;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.shop_detail_default)
				.showImageForEmptyUri(R.drawable.shop_detail_default)
				.showImageOnFail(R.drawable.shop_detail_default)
				.cacheInMemory(false).cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		int screenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		convertView = inflater.inflate(R.layout.img_list, null);
		imageViews = (ImageView) convertView.findViewById(R.id.imageViews);
		Pic pic = (Pic) getItem(position);
		if (pic != null) {

			imageLoader.displayImage(pic.getTh_pic(), imageViews);
			LayoutParams params = imageViews.getLayoutParams();
			params.height = screenHeight * 1 / 3;
			params.width = screenWidth;
			imageViews.setLayoutParams(params);
		}
		return convertView;
	}

}
