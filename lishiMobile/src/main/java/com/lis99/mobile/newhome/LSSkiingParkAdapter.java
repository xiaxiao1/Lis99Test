package com.lis99.mobile.newhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSSkiingParkAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	List<LSSkiingPark> parks;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.skiingpark_default)
				.showImageForEmptyUri(R.drawable.skiingpark_default) 
				.showImageOnFail(R.drawable.skiingpark_default) 
				.cacheInMemory(false) 
				.cacheOnDisk(true) 
				.build();
	}
	
	public LSSkiingParkAdapter(Context context, List<LSSkiingPark> parks){
		this.parks = parks;
		inflater = LayoutInflater.from(context);
		buildOptions();
	}

	@Override
	public int getCount() {
		return parks == null ? 0 : parks.size();
	}

	@Override
	public Object getItem(int position) {
		return parks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.skiingpark_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			viewHolder.priceView = (TextView) convertView.findViewById(R.id.priceView);
			viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
			viewHolder.phoneView = (TextView) convertView.findViewById(R.id.phoneView);
			viewHolder.timeView = (TextView) convertView.findViewById(R.id.timeView);
			viewHolder.addressView = (TextView) convertView.findViewById(R.id.addressView);
			
			viewHolder.stars = new ImageView[5];
			
			viewHolder.stars[0] = (ImageView) convertView.findViewById(R.id.star1);
			viewHolder.stars[1] = (ImageView) convertView.findViewById(R.id.star2);
			viewHolder.stars[2] = (ImageView) convertView.findViewById(R.id.star3);
			viewHolder.stars[3] = (ImageView) convertView.findViewById(R.id.star4);
			viewHolder.stars[4] = (ImageView) convertView.findViewById(R.id.star5);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		LSSkiingPark park = parks.get(position);
		
		imageLoader.displayImage(park.mobileimg, viewHolder.imageView, options);
		viewHolder.nameView.setText(park.title);
		viewHolder.phoneView.setText(park.phone);
		viewHolder.timeView.setText(park.openTime);
		viewHolder.addressView.setText(park.address);
		viewHolder.priceView.setText("人均￥"+park.salePrice);
		
		int level = park.level;
		if(level < 0)
			level = 0;
		if(level > 5)
			level = 5;
		
		for(int i = 0; i < level; ++i){
			viewHolder.stars[i].setImageResource(R.drawable.skiingpark_star);
		}
		
		for(int i = level; i < 5; ++i){
			viewHolder.stars[i].setImageResource(R.drawable.skiingpark_star_empty);
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView imageView;
		TextView priceView;
		TextView nameView;
		TextView phoneView;
		TextView timeView;
		TextView addressView;
		ImageView[] stars;
	}
	

}
