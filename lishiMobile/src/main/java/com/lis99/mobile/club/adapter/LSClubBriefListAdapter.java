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
		int num = clubManagers.size();
		if ( clubManagers == null || num == 0 )
		{
			return 1;
		}
		return num % 2 == 1 ? num / 2 + 1 : num / 2;
	}

	@Override
	public Object getItem(int position) {
		return clubManagers.get(position * 2);
	}

	public Object get1Item (int position)
	{
		int num = position * 2 + 1;
		if ( num >= clubManagers.size() ) return null;
		return clubManagers.get(num);
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

			viewHolder.imageView1 = (ImageView) convertView
					.findViewById(R.id.managerImageView1);
			viewHolder.nameView1 = (TextView) convertView
					.findViewById(R.id.managerView1);
			viewHolder.vipStar1 = (ImageView) convertView.findViewById(R.id.vipStar1);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if ( clubManagers == null || clubManagers.size() == 0 )
		{
			viewHolder.imageView.setVisibility(View.GONE);
			viewHolder.vipStar.setVisibility(View.GONE);
			viewHolder.nameView.setText("该俱乐部还没有活动领队");
			viewHolder.nameView.setText("该俱乐部还没有版主");
			viewHolder.imageView1.setVisibility(View.GONE);
			viewHolder.vipStar1.setVisibility(View.GONE);
			viewHolder.nameView.setVisibility(View.GONE);
			return convertView;
		}

		LSClubAdmin item = (LSClubAdmin) getItem(position);
		
		imageLoader.displayImage(item.getHeadicon(),
				viewHolder.imageView, options);
		viewHolder.nameView.setText(item.getNickname());
		viewHolder.vipStar.setVisibility("1".equals(item.getIs_vip()) ? View.VISIBLE : View.GONE);

		LSClubAdmin item1 = (LSClubAdmin) get1Item(position);

		if ( item1 == null )
		{
			viewHolder.imageView1.setVisibility(View.GONE);
			viewHolder.nameView1.setVisibility(View.GONE);
			viewHolder.vipStar1.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.imageView1.setVisibility(View.VISIBLE);
			viewHolder.nameView1.setVisibility(View.VISIBLE);
			viewHolder.vipStar1.setVisibility(View.VISIBLE);
			imageLoader.displayImage(item1.getHeadicon(),
					viewHolder.imageView1, options);
			viewHolder.nameView1.setText(item1.getNickname());
			viewHolder.vipStar1.setVisibility("1".equals(item1.getIs_vip()) ? View.VISIBLE : View.GONE);
		}



		return convertView;
	}

	
	public class ViewHolder {
		public ImageView imageView, imageView1;
		TextView nameView, nameView1;
		ImageView vipStar, vipStar1;
	}
}
