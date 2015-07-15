package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSClubAdapter extends BaseAdapter {

	List<LSClub> data;

	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}

	public LSClubAdapter(Context context, List<LSClub> data) {
		this.data = data;
		inflater = LayoutInflater.from(context);
		buildOptions();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		LSClub club = data.get(position);
		return club.isLocal() ? 1 : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LSClub club = data.get(position);
		if (getItemViewType(position) == 0) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.club_list_item, null);
				holder = new ViewHolder();
				holder.addButton =  convertView
						.findViewById(R.id.addButton);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.nameView);
				holder.imageView = (RoundedImageView) convertView
						.findViewById(R.id.roundedImageView1);
				holder.recentView = (TextView) convertView
						.findViewById(R.id.recentView);
				holder.sepAll = convertView.findViewById(R.id.sepAll);
				holder.sepHalf = convertView.findViewById(R.id.sepHalf);
				convertView.setTag(holder);

				convertView.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			if (position < data.size() - 1) {
				LSClub nextClub = data.get(position+1);
				if (nextClub.isLocal()) {
					holder.sepAll.setVisibility(View.VISIBLE);
					holder.sepHalf.setVisibility(View.GONE);
				} else {
					holder.sepAll.setVisibility(View.GONE);
					holder.sepHalf.setVisibility(View.VISIBLE);
				}
			} else {
				holder.sepAll.setVisibility(View.VISIBLE);
				holder.sepHalf.setVisibility(View.GONE);
			}
			
			imageLoader.displayImage(club.getImage(), holder.imageView, options);
			holder.nameView.setText(club.getTitle());
			if (club.getTopic_title() != null) {
				holder.recentView.setText(club.getTopic_title());
			} else {
				holder.recentView.setText(null);
			}
			return convertView;
		} else {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.club_list_city_header, null);
			}
			TextView titleView = (TextView) convertView.findViewById(R.id.titleView);
			titleView.setText(club.getTitle());
			return convertView;
		}
		
	}

	static class ViewHolder {
		RoundedImageView imageView;
		TextView nameView;
		TextView recentView;
		View addButton;
		View sepAll;
		View sepHalf;
	}

}
