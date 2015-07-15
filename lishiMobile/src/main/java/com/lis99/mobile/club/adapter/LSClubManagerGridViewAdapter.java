package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClubAdmin;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class LSClubManagerGridViewAdapter extends BaseAdapter {

	List<LSClubAdmin> clubManagers = new ArrayList<LSClubAdmin>();
	LayoutInflater inflater;
	Context context;

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	boolean editable = false;

	public LSClubManagerGridViewAdapter(List<LSClubAdmin> clubs, Context context) {
		this.clubManagers = clubs;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ls_nologin_header_icon)
				.showImageForEmptyUri(R.drawable.ls_nologin_header_icon)
				.showImageOnFail(R.drawable.ls_nologin_header_icon)
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
		return clubManagers == null ? 0 : clubManagers.size();
	}

	@Override
	public Object getItem(int position) {
		return clubManagers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.club_manager_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.managerImageView);
			viewHolder.nameView = (TextView) convertView
					.findViewById(R.id.managerView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(clubManagers.get(position).getHeadicon(),
				viewHolder.imageView, options);
		viewHolder.nameView.setText(clubManagers.get(position).getNickname());

		return convertView;
	}

	public class ViewHolder {
		public ImageView imageView;
		TextView nameView;
	}
}