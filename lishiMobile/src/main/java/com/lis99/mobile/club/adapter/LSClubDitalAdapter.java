package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ClubDetailList;
import com.lis99.mobile.club.model.ClubDetailList.Topiclist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/***
 * 			俱乐部详情
 * @author yy
 *
 */
public class LSClubDitalAdapter extends MyBaseAdapter {
	
	LayoutInflater inflater;

	//线路活动样式
	private boolean activeList;
	
	DisplayImageOptions options;

	private DisplayImageOptions optionshead, optionsclub, optionsBg;

	private Animation animation;

	public int ui_level = 1;
	
	public LSClubDitalAdapter(Context context, List topiclist, boolean active ){
		super(context, topiclist);
		inflater = LayoutInflater.from(context);
		this.activeList = active;
		options = ImageUtil.getclub_topic_imageOptions();

		optionshead = ImageUtil.getclub_topic_headImageOptions();
		optionsclub = ImageUtil.getImageOptionClubIcon();
		optionsBg = ImageUtil.getDynamicImageOptions();
		animation = AnimationUtils.loadAnimation(context, R.anim.like_anim_rotate);

	}
	
	@Override
	public int getViewTypeCount() {
		return 6;
	}
	
	@Override
	public int getItemViewType(int position) {
		Object o = getItem(position);

//		0 | 1 为置顶的 , 2为线路活动
//		if ("0".equals(topic.stick) || "1".equals(topic.stick)) {
//			return 0;
//		}
//		置顶帖
		if ( o instanceof ClubDetailList.Toptopiclist )
		{
			return 0;
		}

		//线路活动
		if ( activeList )
		{
			return  4;
		}
//		讨论区
		if ( !activeList )
		{
			return 5;
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
//			Topiclist item = topiclist.get(position);

			ClubDetailList.Toptopiclist item = (ClubDetailList.Toptopiclist) getItem(position);

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
//			Topiclist item = topiclist.get(position);

			ClubDetailList.Topiclist item = (Topiclist) getItem(position);

			if ( item == null ) return convertView;
//			String url = item.image;
//			if ( !TextUtils.isEmpty(url))
//			{
//				ImageLoader.getInstance().displayImage(url, holder.newImageView, options, ImageUtil.getImageLoading(holder.iv_load, holder.newImageView));
//			}
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
//		讨论区
		else if ( type == 5 )
		{
			return TopicItemNewTopic (position, convertView, parent);
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
			
			Topiclist item = (Topiclist) getItem(position);//topiclist.get(position);
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

	@Override
	public View setView(int i, View view, ViewGroup viewGroup) {
		return null;
	}
//	新的讨论区
	private View TopicItemNewTopic (int position, View view, ViewGroup parent) {

		ViewHolderTopicImages holder = null;

		if ( view == null )
		{
			view = View.inflate(mContext, R.layout.club_topic_item_images, null);

			holder = new ViewHolderTopicImages(view);

			view.setTag(holder);

		}
		else
		{
			holder = (ViewHolderTopicImages) view.getTag();
		}

		final Topiclist item = (Topiclist) getItem(position);

		if ( item == null ) return view;

		holder.topGapView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

		holder.layoutImg0.setVisibility(View.GONE);
		holder.layoutImg.setVisibility(View.GONE);
		holder.layoutImg1.setVisibility(View.GONE);
		holder.layoutIv.setVisibility(View.GONE);
		holder.vipStar.setVisibility(View.GONE);

		holder.ivBg.setVisibility(View.INVISIBLE);
		holder.ivBg1.setVisibility(View.INVISIBLE);
		holder.ivBg2.setVisibility(View.INVISIBLE);
		holder.ivBg3.setVisibility(View.INVISIBLE);
		holder.ivBg4.setVisibility(View.INVISIBLE);
		holder.ivBg5.setVisibility(View.INVISIBLE);



		if ( item.is_follow == 1 )
		{
			holder.btnConcern.setVisibility(View.GONE);
		}
		else
		{
			holder.btnConcern.setVisibility(View.VISIBLE);
		}


		final ViewHolderTopicImages finalHolder = holder;
		holder.btnConcern.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				int id = Common.string2int(item.user_id);

				if ( id == -1 ) return;

				LSRequestManager.getInstance().getFriendsAddAttention(id, new CallBack() {
					@Override
					public void handler(MyTask mTask) {
						finalHolder.btnConcern.setVisibility(View.GONE);
						item.is_follow = 1;
					}
				});

			}
		});


        if ( item.LikeStatus == 1 )
        {
            finalHolder.iv_like.setImageResource(R.drawable.like_btn_2);
        }
        else
        {
            finalHolder.iv_like.setImageResource(R.drawable.like_btn_1);
        }

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

				finalHolder.tvLike.setText(""+item.likeNum);

				finalHolder.iv_like.setImageResource(R.drawable.like_btn_2);

//				finalHolder.iv_like.startAnimation(animation);

//                新版话题用新接口，其他用老点赞接口
                if ( "3".equals(item.category))
                {
                    LSRequestManager.getInstance().clubTopicLikeNew(item.id, null);
                }
                else
                {
                    LSRequestManager.getInstance().clubTopicLike(item.id,null);
                }


			}
		});



//		if (!TextUtils.isEmpty(item.headicon))
			ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, optionshead);

		holder.tvTitle.setText(item.title);
//		holder.tvContent.setText(item.content);
		holder.tvContent.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity) mContext, item.content));
		holder.tvLike.setText("" + item.likeNum);
		holder.tvName.setText(item.nickname);
		holder.tvCreate.setText(item.createdate);

		if (TextUtils.isEmpty(item.replytot))
		{
			holder.tvReply.setText( "0" );
		}
		else
		{
			holder.tvReply.setText(item.replytot + "" );
		}

		if ( "0".equals(item.videoid) || TextUtils.isEmpty(item.videoid) )
		{
			if ( item.image != null )
			{
				int num = item.image.size();
//				只有一张图片的情况
				if ( num == 1 )
				{
					holder.layoutImg0.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(item.image.get(0).image, holder.ivbg0, ImageUtil.getDefultImageOptions());
				}
				else
				{
					if ( num >= 1 )
					{
						holder.layoutImg.setVisibility(View.VISIBLE);
						holder.ivBg.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(0).image, holder.ivBg, ImageUtil.getDefultImageOptions());
					}
					if ( num >= 2 )
					{
						holder.ivBg1.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(1).image, holder.ivBg1, ImageUtil.getDefultImageOptions());
					}
					if ( num >= 3 )
					{
						holder.ivBg2.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(2).image, holder.ivBg2, ImageUtil.getDefultImageOptions());
					}
					if ( num >= 4 )
					{
						holder.layoutImg1.setVisibility(View.VISIBLE);
						holder.ivBg3.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(3).image, holder.ivBg3, ImageUtil.getDefultImageOptions());
					}
					if ( num >= 5 )
					{
						holder.ivBg4.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(4).image, holder.ivBg4, ImageUtil.getDefultImageOptions());
					}
					if ( num >= 6 )
					{
						holder.ivBg5.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(item.image.get(5).image, holder.ivBg5, ImageUtil.getDefultImageOptions());
					}
				}
			}

		}
		else
		{
			holder.layoutIv.setVisibility(View.VISIBLE);

			if ( !TextUtils.isEmpty(item.videoimg))
			{
				ImageLoader.getInstance().displayImage(item.videoimg, holder.contentImageView,
						optionsBg, ImageUtil.getImageLoading(holder.ivLoad, holder
								.contentImageView));
			}

		}




		return view;
	}

	private View conifgTopicItemNewView (int position, View view, ViewGroup parent) {
		ClubHolder holder = null;
		if ( view == null )
		{
			view = View.inflate(mContext, R.layout.club_topic_list_item_new, null);
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

		final Topiclist item = (Topiclist) getItem(position);//topiclist.get(position);
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

		if ( item.image != null && item.image.size() != 0 && !TextUtils.isEmpty(item.image.get(0).image) )
		ImageLoader.getInstance().displayImage(item.image.get(0).image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));
//			ImageLoader.getInstance().displayImage(item.image, holder.iv_bg, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.iv_bg));

		if (!TextUtils.isEmpty(item.headicon))
		ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1, optionshead);


		holder.tv_title.setText(item.title);
		holder.tv_like.setText(""+item.likeNum);
		holder.tv_name.setText(item.nickname);
		if (TextUtils.isEmpty(item.replytot))
		{
			holder.tv_reply.setText( "0则回复" );
		}
		else
		{
			holder.tv_reply.setText(item.replytot + "则回复" );
		}

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
				Common.goUserHomeActivit((Activity)mContext, item.user_id);
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

//				LSRequestManager.getInstance().clubTopicLike(item.id,null);

                //                新版话题用新接口，其他用老点赞接口
                if ( "3".equals(item.category))
                {
                    LSRequestManager.getInstance().clubTopicLikeNew(item.id, null);
                }
                else
                {
                    LSRequestManager.getInstance().clubTopicLike(item.id,null);
                }

			}
		});

		return view;
	}

	private View conifgTopicActivityItemNewView (int position, View view, ViewGroup parent) {
		ActivityHolder holder = null;
		if ( view == null )
		{
			view = View.inflate(mContext, R.layout.club_topic_activity_item_new, null);
			holder = new ActivityHolder();
			holder.imageView = (RoundedImageView) view.findViewById(R.id.iv_bg);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);


			holder.titleTextView = (TextView)view.findViewById(R.id.titleTextView);
			holder.timeTextView = (TextView)view.findViewById(R.id.timeTextView);
			holder.topGapView = view.findViewById(R.id.topGapView);

			view.setTag(holder);
		}
		else
		{
			holder = (ActivityHolder) view.getTag();
		}

		final Topiclist item = (Topiclist) getItem(position);//topiclist.get(position);
		if ( item == null ) return view;


		if (ui_level == 3) {
			holder.topGapView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
		} else {
			holder.topGapView.setVisibility(View.GONE);
		}


		if ( item.image != null && item.image.size() != 0 )
		ImageLoader.getInstance().displayImage(item.image.get(0).image, holder.imageView, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.imageView));
//			ImageLoader.getInstance().displayImage(item.image, holder.imageView, optionsBg, ImageUtil.getImageLoading(holder.iv_load, holder.imageView));



		holder.titleTextView.setText(item.title);

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
		TextView titleTextView, timeTextView;
		View topGapView;
	}


	protected class ViewHolderTopicImages {
		private RoundedImageView roundedImageView1;
		private ImageView vipStar;
		private TextView tvName;
		private Button btnConcern;
		private TextView tvTitle;
		private TextView tvContent;
		private LinearLayout layoutImg;
		private RoundedImageView ivBg;
		private RoundedImageView ivBg1;
		private RoundedImageView ivBg2;
		private LinearLayout layoutImg1;
		private RoundedImageView ivBg3;
		private RoundedImageView ivBg4;
		private RoundedImageView ivBg5;
		private RelativeLayout layoutIv;
		private ImageView contentImageView;
		private View viewTransprant;
		private ImageView ivLoad;
		private ImageView vedio;
		private TextView tvCreate;
		private TextView tvLike;
		private TextView tvReply;
		private LinearLayout layout_like;
		private ImageView iv_like;
		private LinearLayout layoutImg0;
		private ImageView ivbg0;
		private View topGapView;

		public ViewHolderTopicImages(View view) {

			topGapView = view.findViewById(R.id.topGapView);

			roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
			vipStar = (ImageView) view.findViewById(R.id.vipStar);
			tvName = (TextView) view.findViewById(R.id.tv_name);
			btnConcern = (Button) view.findViewById(R.id.btn_concern);
			tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvContent = (TextView) view.findViewById(R.id.tv_content);
			layoutImg = (LinearLayout) view.findViewById(R.id.layout_img);
			ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
			ivBg1 = (RoundedImageView) view.findViewById(R.id.iv_bg1);
			ivBg2 = (RoundedImageView) view.findViewById(R.id.iv_bg2);
			layoutImg1 = (LinearLayout) view.findViewById(R.id.layout_img1);
			ivBg3 = (RoundedImageView) view.findViewById(R.id.iv_bg3);
			ivBg4 = (RoundedImageView) view.findViewById(R.id.iv_bg4);
			ivBg5 = (RoundedImageView) view.findViewById(R.id.iv_bg5);
			layoutIv = (RelativeLayout) view.findViewById(R.id.layout_iv);
			contentImageView = (ImageView) view.findViewById(R.id.contentImageView);
			viewTransprant = (View) view.findViewById(R.id.view_transprant);
			ivLoad = (ImageView) view.findViewById(R.id.iv_load);
			vedio = (ImageView) view.findViewById(R.id.vedio);
			tvCreate = (TextView) view.findViewById(R.id.tv_create);
			tvLike = (TextView) view.findViewById(R.id.tv_like);
			tvReply = (TextView) view.findViewById(R.id.tv_reply);
			layout_like = (LinearLayout) view.findViewById(R.id.layout_like);
			iv_like = (ImageView) view.findViewById(R.id.iv_like);

			ivbg0 = (ImageView) view.findViewById(R.id.iv_bg0);
			layoutImg0 = (LinearLayout) view.findViewById(R.id.layout_img0);
		}
	}

}
