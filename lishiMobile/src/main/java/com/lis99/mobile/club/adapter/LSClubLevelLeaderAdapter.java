package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.club.model.LeaderLevelModel;
import com.lis99.mobile.club.model.LeaderLevelModel.Leaderlist;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
/**
 * 				领队排行
 * @author yy
 *
 */
public class LSClubLevelLeaderAdapter extends BaseAdapter {

	List<LSClub> data;

	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private int[] icon = new int[]{R.drawable.club_level_0, R.drawable.club_level_1,
			R.drawable.club_level_2, R.drawable.club_level_3,
			R.drawable.club_level_4, R.drawable.club_level_5,
			R.drawable.club_level_6, R.drawable.club_level_7,
			R.drawable.club_level_8, R.drawable.club_level_9
			};

	private LeaderLevelModel leaderModel;
	
	private void buildOptions() {
		options = ImageUtil.getclub_topic_headImageOptions();
	}

	
	
	public LSClubLevelLeaderAdapter(Context context, LeaderLevelModel leaderModel) {
		this.leaderModel = leaderModel;
		inflater = LayoutInflater.from(context);
		buildOptions();
	}

	@Override
	public int getCount() {
		if ( leaderModel == null || leaderModel.leaderlist == null || leaderModel.leaderlist.size() == 0 ) return 0;
		return leaderModel.leaderlist.size();
	}
	
	public String getId ( int position )
	{
		if ( leaderModel == null || leaderModel.leaderlist == null || leaderModel.leaderlist.size() == 0 ) return null;
		return leaderModel.leaderlist.get(position).club_id;
	}

	@Override
	public Object getItem(int position) {
		if ( leaderModel == null || leaderModel.leaderlist == null || leaderModel.leaderlist.size() == 0 ) return null;
		return leaderModel.leaderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
//		return data.get(position).getId();
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}
	
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.club_level_leader_item, null);
				holder = new ViewHolder();
				holder.addButton =  (ImageView) convertView
						.findViewById(R.id.addButton);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.nameView);
				holder.roundedImageView1 = (ImageView) convertView
						.findViewById(R.id.roundedImageView1);
				holder.recentView = (TextView) convertView
						.findViewById(R.id.recentView);
				holder.tv_club_from = (TextView) convertView.findViewById(R.id.tv_club_from);
				convertView.setTag(holder);
				holder.vipStar = (ImageView) convertView.findViewById(R.id.vipStar);

				convertView.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.addButton.setImageResource(icon[position]);
			
			Leaderlist item = (Leaderlist) getItem(position);
			if (item == null ) return convertView;
			
			String url = item.headicon;
			ImageLoader.getInstance().displayImage(url, holder.roundedImageView1, options);
			
			holder.nameView.setText(item.nickname);
			holder.recentView.setText("发布了"+item.tottopic+"个线路活动");
			holder.tv_club_from.setText("[来自"+item.club_title+"]");
			if ( "1".equals(item.is_vip ))
			{
				holder.vipStar.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.vipStar.setVisibility(View.GONE);
			}
			
			return convertView;
		
	}

	static class ViewHolder {
		ImageView roundedImageView1;
		ImageView addButton;
		TextView nameView;
		TextView recentView;
		TextView tv_club_from;
		ImageView vipStar;
	}

}
