package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubDetailList.Topiclist;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
/***
 * 			俱乐部详情
 * @author yy
 *
 */
public class LSClubDitalAdapter extends BaseAdapter {
	
	LayoutInflater inflater;

	public ArrayList<Topiclist> topiclist;
	//线路活动样式
	private boolean activeList;
	
	DisplayImageOptions options;
	
	public LSClubDitalAdapter(Context context, ArrayList<Topiclist> topiclist, boolean active ){
		this.topiclist = topiclist;
		inflater = LayoutInflater.from(context);
		this.activeList = active;
		options = ImageUtil.getclub_topic_imageOptions();
	}
	
	public void addList ( ArrayList<Topiclist> topiclist )
	{
		this.topiclist.addAll(topiclist);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return topiclist == null ? 0 : topiclist.size();
	}

	@Override
	public Object getItem(int position) {
		return topiclist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}
	
	@Override
	public int getItemViewType(int position) {
		Topiclist topic = topiclist.get(position);
		//线路活动
		if ( activeList )
		{
			return 2;
		}
		
//		0 | 1 为置顶的 , 2为线路活动
		if ("0".equals(topic.stick) || "1".equals(topic.stick)) {
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
				holder.gapView = (LinearLayout) convertView.findViewById(R.id.gapView);
				holder.view_line_top = convertView.findViewById(R.id.view_line_top);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Topiclist item = topiclist.get(position);
			if ( item == null ) return convertView;
			holder.titleView.setText(item.title);
			
			if (getCount() > position + 1 && getItemViewType(position+1) != 0) {
				holder.gapView.setVisibility(View.VISIBLE);
				holder.view_line_top.setVisibility(View.GONE);
			} else {
				holder.view_line_top.setVisibility(View.VISIBLE);
				holder.gapView.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		//线路活动
		else if ( type == 2 )
		{
			ViewHolder holder = null;
			if ( convertView == null )
			{
				convertView = inflater.inflate(R.layout.club_topic_active_list_item, null);
				holder = new ViewHolder();
				//标题
				holder.titleView = (TextView) convertView.findViewById(R.id.tv_title);
//				背影图
				holder.newImageView = (ImageView) convertView.findViewById(R.id.iv_bg);
//				日期
				holder.infoView = (TextView) convertView.findViewById(R.id.tv_data);
//				活动类型
				holder.replyView = (TextView) convertView.findViewById(R.id.tv_type);
				holder.iv_load = (ImageView) convertView.findViewById(R.id.iv_load);


				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			Topiclist item = topiclist.get(position);
			if ( item == null ) return convertView;
			String url = item.image == null ? null : item.image.image;
			if ( !TextUtils.isEmpty(url))
			{
				ImageLoader.getInstance().displayImage(url, holder.newImageView, options, ImageUtil.getImageLoading(holder.iv_load, holder.newImageView));
			}
			//
			holder.titleView.setText(item.title);
			holder.infoView.setText(item.times );//+ "至" + item.);
			holder.replyView.setText(item.catename);
			return convertView;
			
		}
		else {
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
			
//			LSClubTopic topic = data.get(position);
			
			Topiclist item = topiclist.get(position);
			if ( item == null ) return convertView;
			
			holder.titleView.setText(item.title);
			String replynum = item.replytot;
			if ( "0".equals(replynum) )
			{
				replynum = "1";
			}
			holder.replyView.setText(replynum);
//			holder.infoView.setText( topic.getNickname() + " 发表于 " + topic.getCreatedate());
			holder.infoView.setText( item.nickname);
			holder.tv_time.setText(item.createdate);
			
			if ( "0".equals(item.category)) {
				holder.eventImageView.setVisibility(View.GONE);
			} else {
				holder.eventImageView.setVisibility(View.VISIBLE);
			}
			
			if ("1".equals(item.is_image)) {
				holder.climbImageView.setVisibility(View.VISIBLE);
			} else {
				holder.climbImageView.setVisibility(View.GONE);
			}
			//热贴回复数> 10
			int hot = Integer.parseInt(item.replytot);
			if ( hot > 10 ) {
				holder.newImageView.setVisibility(View.VISIBLE);
			} else {
				holder.newImageView.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		ImageView eventImageView, iv_load;
		ImageView newImageView;
		ImageView climbImageView;
		View view_line_top;
		LinearLayout gapView;
		TextView titleView;
		TextView infoView;
		TextView replyView;
		TextView tv_time;
	}


}
