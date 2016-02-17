package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ClubDetailList.Topiclist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ta.utdid2.android.utils.StringUtils;

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

	private DisplayImageOptions optionshead, optionsclub, optionsBg;

	private Animation animation;
	private Context context;

	public int ui_level = 1;
	
	public LSClubDitalAdapter(Context context, ArrayList<Topiclist> topiclist, boolean active ){
		this.topiclist = topiclist;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.activeList = active;
		options = ImageUtil.getclub_topic_imageOptions();

		optionshead = ImageUtil.getclub_topic_headImageOptions();
		optionsclub = ImageUtil.getImageOptionClubIcon();
		optionsBg = ImageUtil.getDynamicImageOptions();
		animation = AnimationUtils.loadAnimation(context, R.anim.like_anim_rotate);

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
		if ( topiclist.size() <= position || position < 0 ) return null;
		return topiclist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 5;
	}
	
	@Override
	public int getItemViewType(int position) {
		Topiclist topic = topiclist.get(position);
		//线路活动
		if ( activeList )
		{
			return  4;
		}
		
//		0 | 1 为置顶的 , 2为线路活动
		if ("0".equals(topic.stick) || "1".equals(topic.stick)) {
			return 0;
		}
		
		return ui_level != 3 ? 1 : 3;
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
				holder.gapBottomLine = convertView.findViewById(R.id.gapBottomLine);
				holder.topGapView = convertView.findViewById(R.id.topGapView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Topiclist item = topiclist.get(position);
			if ( item == null ) return convertView;
			holder.titleView.setText(item.title);

			if (position == 0) {
				holder.topGapView.setVisibility(View.VISIBLE);
			} else {
				holder.topGapView.setVisibility(View.GONE);
			}

			if (getCount() > position + 1 ) {
				int nextViewType = getItemViewType(position+1);
				if (nextViewType != 0) {
					if (getItemViewType(position + 1) < 3) {
						holder.gapView.setVisibility(View.VISIBLE);
						holder.gapBottomLine.setVisibility(View.VISIBLE);
						holder.view_line_top.setVisibility(View.GONE);
					} else {
						holder.gapView.setVisibility(View.VISIBLE);
						holder.gapBottomLine.setVisibility(View.GONE);
						holder.view_line_top.setVisibility(View.GONE);
					}
				}
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
			String url = item.image;
			if ( !TextUtils.isEmpty(url))
			{
				ImageLoader.getInstance().displayImage(url, holder.newImageView, options, ImageUtil.getImageLoading(holder.iv_load, holder.newImageView));
			}
			//
			holder.titleView.setText(item.title);
			holder.infoView.setText(item.times );//+ "至" + item.);
			holder.replyView.setText(item.catename);
			return convertView;
			
		} else if (type == 3) {
			return conifgTopicItemNewView(position, convertView, parent);
		} else if (type == 4) {
			return conifgTopicActivityItemNewView(position, convertView, parent);
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
//			//热贴回复数> 10
//			int hot = Integer.parseInt(item.replytot);
//			if ( hot > 10 ) {
//				holder.newImageView.setVisibility(View.VISIBLE);
//			} else {
//				holder.newImageView.setVisibility(View.GONE);
//			}
			if ( item.is_hot == 0 )
			{
				holder.newImageView.setVisibility(View.GONE);
			}
			else {
				holder.newImageView.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}
		
	}

	private View conifgTopicItemNewView (int position, View view, ViewGroup parent) {
		ClubHolder holder = null;
		if ( view == null )
		{
			view = View.inflate(context, R.layout.club_topic_list_item_new, null);
			holder = new ClubHolder();
			holder.iv_bg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_like = (TextView) view.findViewById(R.id.tv_like);
			holder.tv_reply = (TextView) view.findViewById(R.id.tv_reply);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);

			holder.layout_like =  view.findViewById(R.id.layout_like);
			holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);

			holder.iv_like = (ImageView) view.findViewById(R.id.iv_like);

			holder.btn_concern = (Button) view.findViewById(R.id.btn_concern);
			holder.topGapView = view.findViewById(R.id.topGapView);

			view.setTag(holder);
		}
		else
		{
			holder = (ClubHolder) view.getTag();
		}

		final Topiclist item = topiclist.get(position);
		if ( item == null ) return view;

		holder.topGapView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

		if ( item.is_vip == 1 )
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(item.image))
		ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
		if (!TextUtils.isEmpty(item.headicon))
		ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, optionshead);


		holder.tv_title.setText(item.title);
		holder.tv_like.setText(""+item.likeNum);
		holder.tv_name.setText(item.nickname);
//		holder.tv_reply.setText(item.replytot + "则回复" );

		if ( item.LikeStatus == 1 )
		{
			holder.iv_like.setImageResource(R.drawable.like_button_press);
		}
		else
		{
			holder.iv_like.setImageResource(R.drawable.like_button);
		}

		if ( item.is_follow == 1 )
		{
			holder.btn_concern.setVisibility(View.GONE);
		}
		else
		{
			holder.btn_concern.setVisibility(View.VISIBLE);
		}

		final ClubHolder finalHolder = holder;

		holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Common.goUserHomeActivit((Activity)context, item.user_id);
			}
		});

		holder.btn_concern.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				int id = Common.string2int(item.user_id);

				if ( id == -1 ) return;

				LSRequestManager.getInstance().getFriendsAddAttention(id, new CallBack() {
					@Override
					public void handler(MyTask mTask) {
						finalHolder.btn_concern.setVisibility(View.GONE);
						item.is_follow = 1;
					}
				});

			}
		});


		holder.layout_like.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ( item.LikeStatus == 1 ) return;

				if ( !Common.isLogin(LSBaseActivity.activity))
				{
					return;
				}

				item.LikeStatus = 1;

				item.likeNum += 1;

				finalHolder.tv_like.setText(""+item.likeNum);

				finalHolder.iv_like.setImageResource(R.drawable.like_button_press);

				finalHolder.iv_like.startAnimation(animation);

				LSRequestManager.getInstance().clubTopicLike(item.id,null);
			}
		});

		return view;
	}

	private View conifgTopicActivityItemNewView (int position, View view, ViewGroup parent) {
		ActivityHolder holder = null;
		if ( view == null )
		{
			view = View.inflate(context, R.layout.club_topic_activity_item_new, null);
			holder = new ActivityHolder();
			holder.imageView = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);


			holder.titleTextView = (TextView)view.findViewById(R.id.titleTextView);
			holder.labelTextView = (TextView)view.findViewById(R.id.labelTextView);
			holder.locTextView = (TextView)view.findViewById(R.id.locTextView);
			holder.timeTextView = (TextView)view.findViewById(R.id.timeTextView);
			holder.topGapView = view.findViewById(R.id.topGapView);

			view.setTag(holder);
		}
		else
		{
			holder = (ActivityHolder) view.getTag();
		}

		final Topiclist item = topiclist.get(position);
		if ( item == null ) return view;


		if (ui_level == 1) {
			holder.topGapView.setVisibility(View.GONE);
		} else {
			holder.topGapView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
		}


		ImageLoader.getInstance().displayImage(item.image, holder.imageView, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.imageView));



		holder.titleTextView.setText(item.title);

		if (!StringUtils.isEmpty(item.catename))
		{
			holder.labelTextView.setVisibility(View.VISIBLE);
			holder.labelTextView.setText(item.catename);
		}
		else
		{
			holder.labelTextView.setVisibility(View.GONE);
		}

		if (StringUtils.isEmpty(item.setaddress))
		{
			holder.locTextView.setVisibility(View.GONE);
		}
		else
		{
			holder.locTextView.setText(item.setaddress);
			holder.locTextView.setVisibility(View.VISIBLE);
		}

		holder.timeTextView.setText(item.starttime);

		return view;
	}
	
	static class ViewHolder{
		ImageView eventImageView, iv_load;
		ImageView newImageView;
		ImageView climbImageView;

		TextView nameView;
		View view_line_top;
		LinearLayout gapView;
		TextView titleView;
		TextView infoView;
		TextView replyView;
		TextView tv_time;

		View gapBottomLine;
		View topGapView;

	}


	static class ClubHolder
	{

		RoundedImageView roundedImageView1;
		ImageView vipStar, iv_bg, iv_like, iv_load;
		TextView tv_name, tv_like, tv_title, tv_reply;
		Button btn_concern;
		View layout_like;
		View topGapView;
	}

	static class ActivityHolder
	{

		ImageView imageView, iv_load;
		TextView titleTextView, labelTextView, locTextView, timeTextView;
		View topGapView;
	}


}
