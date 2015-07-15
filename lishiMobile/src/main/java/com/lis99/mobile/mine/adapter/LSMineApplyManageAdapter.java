package com.lis99.mobile.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BaseListAdapter;
import com.lis99.mobile.mine.LSMineApplyManageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LSMineApplyManageAdapter extends BaseListAdapter<LSMineApplyManageInfo>{

	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.club_icon_header_default)
				.showImageForEmptyUri(R.drawable.club_icon_header_default)
				.showImageOnFail(R.drawable.club_icon_header_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}
	
	public LSMineApplyManageAdapter(Context context) {
		super(context);
	}
	
	public LSMineApplyManageAdapter(Context context, List<LSMineApplyManageInfo> data) {
		super(context, data);
		buildOptions();
	}
	//点击后， 红点消失
	public void onClick ( int position )
	{
		//没有红点， return
		if ( position == 0 ) return;
		LSMineApplyManageInfo info = getItem(position);
		info.setIs_baoming(0);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ls_mine_apply_manage_item, null);
			holder = new ViewHolder();
			holder.roundedImageView1 = (ImageView) convertView.findViewById(R.id.roundedImageView1);
			holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
			holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
			holder.newsDot = convertView.findViewById(R.id.newsDot);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LSMineApplyManageInfo info = getItem(position);
		imageLoader.displayImage(info.getClub_img(), holder.roundedImageView1, options);
		holder.timeView.setText("报名时间：" + info.getCreatedate());
		holder.nameView.setText(info.getTopic_title());
		holder.newsDot.setVisibility(info.isIs_baoming() == 1 ? View.VISIBLE : View.GONE);
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView roundedImageView1;
		TextView timeView;
		TextView nameView;
		View newsDot;
	}

}
