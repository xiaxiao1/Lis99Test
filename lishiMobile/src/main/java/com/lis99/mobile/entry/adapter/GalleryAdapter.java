package com.lis99.mobile.entry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.myactivty.ShowPic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {
	
	List<ShowPic> photos = new ArrayList<ShowPic>();
	LayoutInflater inflater;
	Context context;
	
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	boolean editable=false;
	
	//float height = 0;
	
	
	
	public GalleryAdapter(List<ShowPic> photos,Context context){
		this.photos = photos;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//height = context.getResources().getDimension(R.dimen.photo_flow_height);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.shop_list_default)
		.showImageForEmptyUri(R.drawable.shop_list_default)
		.showImageOnFail(R.drawable.shop_list_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
	}

	public void setPhotos(List<ShowPic> list)
	{
		photos = list;
	}
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		if(this.editable ^ editable){
			this.editable = editable;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return photos == null ? 0 : photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.my_gridsview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_iv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(photos.get(position)
				.getImage_url(), viewHolder.imageView, options);
		return convertView;
	}
	public class ViewHolder{
		public ImageView imageView;
	}
}
