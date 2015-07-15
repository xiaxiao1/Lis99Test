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
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
/**
 * 			简介管理员列表
 * @author yy
 *
 */
public class LSClubBriefListAdapter extends BaseAdapter{

	List<LSClubAdmin> clubManagers = new ArrayList<LSClubAdmin>();
	LayoutInflater inflater;
	Context context;

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	boolean editable = false;
	
	public LSClubBriefListAdapter ( List<LSClubAdmin> clubs, Context context )
	{
		this.clubManagers = clubs;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		options = ImageUtil.getclub_topic_headImageOptions();
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
		return clubManagers == null ? 1 : clubManagers.size();
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
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.club_brief_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.managerImageView);
			viewHolder.nameView = (TextView) convertView
					.findViewById(R.id.managerView);
			viewHolder.vipStar = (ImageView) convertView.findViewById(R.id.vipStar);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if ( clubManagers == null )
		{
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.vipStar.setVisibility(View.GONE);
			viewHolder.nameView.setText("该俱乐部还没有活动领队");
			return convertView;
		}
		
		imageLoader.displayImage(clubManagers.get(position).getHeadicon(),
				viewHolder.imageView, options);
		viewHolder.nameView.setText(clubManagers.get(position).getNickname());
		viewHolder.vipStar.setVisibility("1".equals(clubManagers.get(position).getIs_vip()) ? View.VISIBLE : View.GONE);

		return convertView;
	}

	
	public class ViewHolder {
		public ImageView imageView;
		TextView nameView;
		ImageView vipStar;
	}
}
