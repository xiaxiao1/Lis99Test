package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubLevelModel;
import com.lis99.mobile.club.model.ClubLevelModel.Hotclublist;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
/**
 * 				俱乐部排行
 * @author yy
 *
 */
public class LSClubLevelAdapter extends BaseAdapter {

	List<LSClub> data;

	private Activity a;
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private int[] icon = new int[]{R.drawable.club_level_0, R.drawable.club_level_1,
			R.drawable.club_level_2, R.drawable.club_level_3,
			R.drawable.club_level_4, R.drawable.club_level_5,
			R.drawable.club_level_6, R.drawable.club_level_7,
			R.drawable.club_level_8, R.drawable.club_level_9
			};
	
	public ClubLevelModel clubModel;
	
	private void buildOptions() {
		options = ImageUtil.getImageOptionClubIcon();
	}

	public LSClubLevelAdapter(Activity a, ClubLevelModel clubModel) {
		this.clubModel = clubModel;
		this.a = a;
		buildOptions();
	}

	@Override
	public int getCount() {
		if (clubModel == null || clubModel.hotclublist == null || clubModel.hotclublist.size() == 0) return 0;
		return clubModel.hotclublist.size();
	}

	@Override
	public Object getItem(int position) {
		if (clubModel == null || clubModel.hotclublist == null || clubModel.hotclublist.size() == 0) return null;
		return clubModel.hotclublist.get(position);
		}
	
	public String getId (int position)
	{
		if (clubModel == null || clubModel.hotclublist == null || clubModel.hotclublist.size() == 0) return null;
		return clubModel.hotclublist.get(position).clubId;
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
				convertView = View.inflate(a, R.layout.club_level_item, null);
				holder = new ViewHolder();
				holder.addButton =  (ImageView) convertView
						.findViewById(R.id.addButton);
				holder.nameView = (TextView) convertView
						.findViewById(R.id.nameView);
				holder.roundedImageView1 = (ImageView) convertView
						.findViewById(R.id.roundedImageView1);
				holder.recentView = (TextView) convertView
						.findViewById(R.id.recentView);
				convertView.setTag(holder);

				convertView.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.addButton.setImageResource(icon[position]);
			
			Hotclublist item = (Hotclublist) getItem(position);
			if ( item == null ) return convertView;
			
			holder.nameView.setText(item.title);
			holder.recentView.setText(item.topictitle);
			String url = item.image;
			ImageLoader.getInstance().displayImage(url, holder.roundedImageView1, options);
			
			
			return convertView;
		
	}

	static class ViewHolder {
		ImageView roundedImageView1;
		ImageView addButton;
		TextView nameView;
		TextView recentView;
	}

}
