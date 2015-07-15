package com.lis99.mobile.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BaseListAdapter;
import com.lis99.mobile.mine.LSMineApplyInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSMineApplyAdapter extends BaseListAdapter<LSMineApplyInfo>{

	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}
	
	public LSMineApplyAdapter(Context context) {
		super(context);
	}
	
	public LSMineApplyAdapter(Context context, List<LSMineApplyInfo> data) {
		super(context, data);
		buildOptions();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ls_mine_apply_item, null);
			holder = new ViewHolder();
			holder.roundedImageView1 = (ImageView) convertView.findViewById(R.id.roundedImageView1);
			holder.infoPreView = (TextView) convertView.findViewById(R.id.infoPreView);
			holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
			holder.infoView = (TextView) convertView.findViewById(R.id.infoView);
			holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LSMineApplyInfo info = getItem(position);
		imageLoader.displayImage(info.getClub_img(), holder.roundedImageView1, options);
		holder.timeView.setText("报名时间：" + info.getCreatetime());
		holder.nameView.setText(info.getTopic_title());
		holder.infoView.setText(info.getType_text());
		
		if (info.getType() == 3) {
			holder.infoPreView.setText("活动领队");
			holder.infoView.setTextColor(Color.rgb(0xfe, 0x2a, 0));
		} else {
			holder.infoPreView.setText("您的报名");
			holder.infoView.setTextColor(Color.rgb(0x2a, 0xcb, 0xc2));
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView roundedImageView1;
		TextView infoPreView;
		TextView timeView;
		TextView infoView;
		TextView nameView;
	}

}
