package com.lis99.mobile.mine.adapter;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicReplyActivity;
import com.lis99.mobile.club.model.MineReplyModel.Replylist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.mine.ActivityReplyMine;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AdapterMineReplyItem extends BaseAdapter
{

	private ArrayList<Replylist> replylist;
	private ActivityReplyMine main;
	private DisplayImageOptions options;
	
	public AdapterMineReplyItem ( ActivityReplyMine main, ArrayList<Replylist> replylist )
	{
		this.main = main;
		this.replylist = replylist;
		options = ImageUtil.getclub_topic_headImageOptions();
	}
	
	public void addList ( ArrayList<Replylist> replylist )
	{
		this.replylist.addAll(replylist);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return replylist == null ? 0 : replylist.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		if ( replylist == null ) return null;
		return (position >= replylist.size() ) ? null : replylist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Holder holder = null;
		if ( convertView == null )
		{
			convertView = View.inflate(main, R.layout.activity_mine_reply_item, null);
			holder = new Holder();
			holder.roundedImageView1 = (RoundedImageView) convertView.findViewById(R.id.roundedImageView1);
			holder.vipStar = (ImageView) convertView.findViewById(R.id.vipStar);
			holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
			holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
			holder.tv_floor = (TextView) convertView.findViewById(R.id.tv_floor);
			holder.layout1 = (LinearLayout) convertView.findViewById(R.id.layout1);
			holder.layoutmore = (LinearLayout) convertView.findViewById(R.id.layoutmore);
			holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.tv_reply_body = (TextView) convertView.findViewById(R.id.tv_reply_body);
			holder.tv_reply_content = (TextView) convertView.findViewById(R.id.tv_reply_content);
			holder.tv_reply_floor = (TextView) convertView.findViewById(R.id.tv_reply_floor);
			holder.contentView = (TextView) convertView.findViewById(R.id.contentView);
			holder.contentImageView = (ImageView) convertView.findViewById(R.id.contentImageView);
			holder.layout_club_detail_like = (LinearLayout) convertView.findViewById(R.id.layout_club_detail_like);
			holder.layout_club_detail_reply = (LinearLayout) convertView.findViewById(R.id.layout_club_detail_reply);
			holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);
			holder.tv_like = (TextView) convertView.findViewById(R.id.tv_like);
			holder.reply_view = convertView.findViewById(R.id.reply_view);
			holder.view_line = convertView.findViewById(R.id.view_line);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		Replylist item = (Replylist) getItem(position);
		if ( item == null ) return convertView;
		ItemOnclick itemOnclick = new ItemOnclick(item, holder);
		//点击进入帖子详情
		holder.nameView.setOnClickListener(itemOnclick);
		holder.layoutmore.setOnClickListener(itemOnclick);
//		holder.layout_club_detail_like.setOnClickListener(itemOnclick);

		holder.layout_club_detail_like.setVisibility(View.GONE);

		holder.layout_club_detail_reply.setOnClickListener(itemOnclick);
		holder.roundedImageView1.setOnClickListener(itemOnclick);

		ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, options);
		String nikename = item.nickname + "在 ";
		String titleEnd = " 中回复了我";
		SpannableString title = new SpannableString(nikename + item.topic_title + titleEnd);
		title.setSpan(new ForegroundColorSpan(main.getResources().getColor(R.color.text_color_blue)), nikename.length(), title.length() - titleEnd.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.nameView.setText(title);
		holder.dateView.setText(item.createtime);
		holder.tv_floor.setText(item.floor + "楼");
		if ( Common.isVip(item.is_vip))
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
		//楼层判断， 是否现实引用， 0. 1 都不显示
		if (item.reply_id == 0 || TextUtils.isEmpty(item.reply_content) || "0".equals(item.reply_floor) || "1".equals(item.reply_floor) || TextUtils.isEmpty(item.reply_floor) )
		{
			holder.reply_view.setVisibility(View.GONE);
		}
		else
		{
			holder.reply_view.setVisibility(View.VISIBLE);
			//回复内容＝＝＝＝＝
			holder.tv_reply_body.setText("回复@ " + item.reply_nickname);
			holder.tv_reply_floor.setText(item.reply_floor + "层");
			holder.tv_reply_content.setText(item.reply_content);
		}
		
		holder.contentView.setText(item.topic_content);
		if ( "1".equals(item.is_image) && item.topic_image != null && item.topic_image.size() > 0 )
		{
			holder.contentImageView.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(item.topic_image.get(0).image, holder.contentImageView);
		}
		else
		{
			holder.contentImageView.setVisibility(View.GONE);
		}
		
		if ( "0".equals(item.LikeStatus))
		{
			holder.iv_like.setImageResource(R.drawable.like_button);
			holder.tv_like.setTextColor(main.getResources().getColor(R.color.pull_text_color));
		}
		else
		{
			holder.iv_like.setImageResource(R.drawable.like_button_press);
			holder.tv_like.setTextColor(main.getResources().getColor(R.color.color_like_press_red));
		}
			
		holder.tv_like.setText(Common.getLikeNum(""+item.likeNum));


		
		return convertView;
	}
	
	class ItemOnclick implements OnClickListener
	{
		Replylist item;
		Holder holder;
		public ItemOnclick (Replylist item, Holder holder)
		{
			this.item = item;
			this.holder = holder;
		}
		/**holder.nameView.setOnClickListener(itemOnclick);
		holder.layoutmore.setOnClickListener(itemOnclick);
		holder.layout_club_detail_like.setOnClickListener(itemOnclick);
		holder.layout_club_detail_reply.setOnClickListener(itemOnclick);*/
		@Override
		public void onClick(View v)
		{
			Intent intent = null;
			// TODO Auto-generated method stub
			switch ( v.getId() )
			{
				case R.id.nameView:
					 intent = new Intent(main, LSClubTopicActivity.class);
					intent.putExtra("topicID", item.topic_id);
					main.startActivity(intent);
					break;
				case R.id.layoutmore:
					holder.layout1.setVisibility(View.VISIBLE);
					holder.layoutmore.setVisibility(View.GONE);
					holder.view_line.setVisibility(View.GONE);
					break;
				case R.id.layout_club_detail_like:
					if ( "1".equals(item.LikeStatus) )
					{
						return;
					}
					LSRequestManager.getInstance().mClubTopicInfoLike(""+item.replytopicid, new CallBack()
					{
						
						@Override
						public void handler(MyTask mTask)
						{
							// TODO Auto-generated method stub
							item.LikeStatus = "1";
							holder.iv_like.setImageResource(R.drawable.like_button_press);
							holder.tv_like.setTextColor(main.getResources().getColor(R.color.color_like_press_red));
//							AdapterMineReplyItem.this.notifyDataSetChanged();
						}
					});
					break;
				case R.id.layout_club_detail_reply:
					 intent = new Intent(main, LSClubTopicReplyActivity.class);
					intent.putExtra("replyedName", item.nickname);
					intent.putExtra("replyedcontent", item.topic_content);
					intent.putExtra("replyedfloor", item.floor);
					intent.putExtra("replyedId", ""+item.replytopicid);
					intent.putExtra("clubId", ""+item.club_id);
					intent.putExtra("topicId", ""+item.topic_id);
//					startActivityForResult(intent, 999);
					main.startActivity(intent);
					break;
				case R.id.roundedImageView1:
					intent = new Intent(main, LSUserHomeActivity.class);
					intent.putExtra("userID", ""+item.fromuser_id);
					main.startActivity(intent);
					break;
			}
		}
		
	}
	
	class Holder
	{
		RoundedImageView roundedImageView1;
		ImageView vipStar;
		TextView nameView;
		TextView dateView;
		TextView tv_floor;
		LinearLayout layoutmore;
		TextView tv_info;
		LinearLayout layout1;
		TextView tv_reply_body;
		TextView tv_reply_floor;
		TextView tv_reply_content;
		TextView contentView;
		ImageView contentImageView;
		LinearLayout layout_club_detail_like;
		ImageView iv_like;
		TextView tv_like;
		LinearLayout layout_club_detail_reply;
		View reply_view;
		View view_line;
	}
	
}
