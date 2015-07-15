package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClub;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class LSClubGridViewAdapter extends BaseAdapter {

	List<LSClub> clubs = new ArrayList<LSClub>();
	LayoutInflater inflater;
	Context context;
	
	

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	boolean editable = false;

	public LSClubGridViewAdapter(List<LSClub> clubs, Context context) {
		this.clubs = clubs;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(false).cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		if (this.editable ^ editable) {
			this.editable = editable;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return clubs == null ? 1 : clubs.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (outRange(position))
			return null;
		return clubs.get(position);
	}
	
	private boolean outRange(int position){
		if (clubs == null)
			return true;
		return !(position >= 0 && position < clubs.size());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.club_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.roundedImageView1);
			viewHolder.nameView = (TextView) convertView
					.findViewById(R.id.nameView);
			viewHolder.recentView = (TextView) convertView.findViewById(R.id.recentView);
			viewHolder.tv_all = (TextView) convertView.findViewById(R.id.tv_all);
			viewHolder.sepAll = convertView.findViewById(R.id.sepAll);
			viewHolder.sepHalf = convertView.findViewById(R.id.sepHalf);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (outRange(position)) {
//			viewHolder.imageView.setImageBitmap(null);
//			viewHolder.imageView.setImageResource(R.drawable.club_icon_grid_add);
//			imageLoader.displayImage("drawable://" + R.drawable.club_icon_grid_add,
//					viewHolder.imageView, options);
//			viewHolder.nameView.setText("添加俱乐部");
			viewHolder.imageView.setVisibility(View.INVISIBLE);
			viewHolder.nameView.setVisibility(View.INVISIBLE);
			viewHolder.recentView.setVisibility(View.INVISIBLE);
			viewHolder.tv_all.setVisibility(View.VISIBLE);
			viewHolder.sepHalf.setVisibility(View.GONE);
			viewHolder.sepAll.setVisibility(View.VISIBLE);
		} else {
			//线的显示
			if ( position == clubs.size() - 1 )
			{
				viewHolder.sepHalf.setVisibility(View.GONE);
				viewHolder.sepAll.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.sepHalf.setVisibility(View.VISIBLE);
				viewHolder.sepAll.setVisibility(View.GONE);
			}
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.nameView.setVisibility(View.VISIBLE);
			viewHolder.recentView.setVisibility(View.VISIBLE);
			viewHolder.tv_all.setVisibility(View.GONE);
			imageLoader.displayImage(clubs.get(position).getImage(),
					viewHolder.imageView, options);
			viewHolder.nameView.setText(clubs.get(position).getTitle());
			//描述
			viewHolder.recentView.setText(clubs.get(position).getTopic_title());
		}
		
		
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView imageView;
		public TextView nameView, recentView, tv_all;
		public View sepAll, sepHalf;
		
	}
}