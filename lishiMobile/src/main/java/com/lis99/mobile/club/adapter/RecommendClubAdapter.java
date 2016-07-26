package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubMainListModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class RecommendClubAdapter extends MyBaseAdapter {


	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public RecommendClubAdapter(Activity c, ArrayList listItem) {
		super(c, listItem);

		inflater = LayoutInflater.from(mContext);
		buildOptions();

	}

	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}

	@Override
	public View setView(int i, View view, ViewGroup viewGroup) {

			ViewHolder holder = null;
			if (view == null) {
				view = inflater.inflate(R.layout.club_list_item, null);
				holder = new ViewHolder();
				holder.addButton =  view
						.findViewById(R.id.addButton);
				holder.nameView = (TextView) view
						.findViewById(R.id.nameView);
				holder.imageView = (RoundedImageView) view
						.findViewById(R.id.roundedImageView1);
				holder.recentView = (TextView) view
						.findViewById(R.id.recentView);
				holder.sepAll = view.findViewById(R.id.sepAll);
				holder.sepHalf = view.findViewById(R.id.sepHalf);
				view.setTag(holder);

				view.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

			} else {
				holder = (ViewHolder) view.getTag();
			}

		ClubMainListModel item = (ClubMainListModel) getItem(i);

			imageLoader.displayImage(item.image, holder.imageView, options);
			holder.nameView.setText(item.title);
			holder.recentView.setText(item.topic_title);
			return view;

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
