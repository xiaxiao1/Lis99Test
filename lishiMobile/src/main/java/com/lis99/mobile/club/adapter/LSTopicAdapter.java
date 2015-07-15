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

import java.util.List;

public class LSTopicAdapter extends BaseAdapter {
	
	

	
	List<LSClubTopic> data;
	
	LayoutInflater inflater;

//	ImageLoader imageLoader = ImageLoader.getInstance();
//	DisplayImageOptions options;
	
//	private void buildOptions() {
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.club_list_item_default)
//				.showImageForEmptyUri(R.drawable.club_list_item_default) 
//				.showImageOnFail(R.drawable.club_list_item_default) 
//				.cacheInMemory(false) 
//				.cacheOnDisk(true) 
//				.build();
//	}
	
	public LSTopicAdapter(Context context, List<LSClubTopic> data){
		this.data = data;
		inflater = LayoutInflater.from(context);
//		buildOptions();
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
		LSClubTopic topic = data.get(position);
//		0 | 1 为置顶的 
		if (topic.getStick() == 0 || topic.getStick() == 1) {
			return 0;
		}
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (type == 0) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.club_toparticle_item, null);
				holder = new ViewHolder();
				holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
				holder.gapView = convertView.findViewById(R.id.gapView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LSClubTopic topic = data.get(position);
			if (topic.getCategory() == 0) {
				holder.titleView.setText( topic.getTitle());
			} else {
				holder.titleView.setText( topic.getTitle());
			}
			
			if (getCount() > position + 1 && getItemViewType(position+1) != 0) {
				holder.gapView.setVisibility(View.VISIBLE);
			} else {
				holder.gapView.setVisibility(View.GONE);
			}
			
			return convertView;
		} else {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.club_article_item, null);
				holder = new ViewHolder();
				
				holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
				holder.infoView = (TextView) convertView.findViewById(R.id.infoView);
				holder.replyView = (TextView) convertView.findViewById(R.id.replyView);
				
				holder.eventImageView = (ImageView) convertView.findViewById(R.id.eventImageView);
				holder.newImageView = (ImageView) convertView.findViewById(R.id.newImageView);
				holder.climbImageView = (ImageView) convertView.findViewById(R.id.climbImageView);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			LSClubTopic topic = data.get(position);
			
			holder.titleView.setText(topic.getTitle());
			holder.replyView.setText(topic.getTotal() + "条");
//			holder.infoView.setText( topic.getNickname() + " 发表于 " + topic.getCreatedate());
			holder.infoView.setText( topic.getNickname());
			holder.tv_time.setText(topic.getCreatedate());
			
			if (topic.getCategory() == 0) {
				holder.eventImageView.setVisibility(View.GONE);
			} else {
				holder.eventImageView.setVisibility(View.VISIBLE);
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
		
	}
	
	static class ViewHolder{
		ImageView eventImageView;
		ImageView newImageView;
		ImageView climbImageView;
		View gapView;
		TextView titleView;
		TextView infoView;
		TextView replyView;
		TextView tv_time;
	}


}
