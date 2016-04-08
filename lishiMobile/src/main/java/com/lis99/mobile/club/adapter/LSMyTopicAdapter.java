package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClubTopic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSMyTopicAdapter extends BaseAdapter {
	List<LSClubTopic> data;

	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_list_item_default)
				.showImageForEmptyUri(R.drawable.club_list_item_default)
				.showImageOnFail(R.drawable.club_list_item_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}

	public LSMyTopicAdapter(Context context, List<LSClubTopic> data) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.club_myarticle_item, null);
			holder = new ViewHolder();

			holder.titleView = (TextView) convertView
					.findViewById(R.id.titleView);
			holder.infoView = (TextView) convertView
					.findViewById(R.id.infoView);
			holder.clubNameView = (TextView) convertView
					.findViewById(R.id.clubNameView);

			holder.eventImageView = (ImageView) convertView
					.findViewById(R.id.eventImageView);
			holder.newImageView = (ImageView) convertView
					.findViewById(R.id.newImageView);
			holder.climbImageView = (ImageView) convertView
					.findViewById(R.id.climbImageView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LSClubTopic topic = data.get(position);

		holder.titleView.setText(topic.getTitle());
		holder.clubNameView.setText(topic.getClub_title());
		holder.infoView.setText(/*topic.getNickname() + " 发布于 "
				+ */topic.getCreatedate());

		if (topic.getCategory() == 1 || topic.getCategory() == 2 || topic.getCategory() == 4 ) {
			holder.eventImageView.setVisibility(View.VISIBLE);
		} else {
			holder.eventImageView.setVisibility(View.GONE);
		}

		if (topic.getIs_image() == 1) {
			holder.climbImageView.setVisibility(View.VISIBLE);
		} else {
			holder.climbImageView.setVisibility(View.GONE);
		}

		if (topic.getIs_new() == 1) {
			holder.newImageView.setVisibility(View.VISIBLE);
		} else {
			holder.newImageView.setVisibility(View.GONE);
		}

		return convertView;

	}

	static class ViewHolder {
		ImageView eventImageView;
		ImageView newImageView;
		ImageView climbImageView;
		View gapView;
		TextView titleView;
		TextView infoView;
		TextView clubNameView;
	}
}
