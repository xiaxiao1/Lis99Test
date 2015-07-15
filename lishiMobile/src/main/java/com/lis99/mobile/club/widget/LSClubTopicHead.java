package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.ClubTopicDetailHead;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 帖子详情 Head
 * 
 * @author yy
 * 
 */
public class LSClubTopicHead extends LinearLayout implements
		android.view.View.OnClickListener
{

	private LayoutInflater inflater;
	private Context c;
	private View v;

	TextView titleView;
	TextView nameView;
	TextView dateView;
	TextView contentView;
	TextView clubButton;

	View vipStar;

	ImageView imageView;
	ImageView contentImageView;

	// ========2.3===========
	private int ImageWidth;
	// 帖子回复数量，
	private TextView tv_reply_number;
	// 楼层数, 删除楼层
	private TextView tv_floor, tv_floor_delete;
	// 攒, 标题文字
	private TextView tv_like;
	// 回复， 点赞
	private LinearLayout layout_club_detail_reply, layout_club_detail_like;

	// LSClubTopic topic;
	// 俱乐部名称,ID
	String clubName;
	int clubID;

	DisplayImageOptions options;
	DisplayImageOptions headerOptions;

	private LSClubTopicActivity lsTopic;

	private ClubTopicDetailHead clubhead;

	private ImageView iv_like;
//点
	private ImageView iv_load;

	private AnimationDrawable animationDrawable;

	public void setHead(final ClubTopicDetailHead clubhead)
	{
		this.clubhead = clubhead;
		titleView.setText(clubhead.title);

		if ("1".equals(clubhead.is_vip))
		{
			vipStar.setVisibility(View.VISIBLE);
		} else
		{
			vipStar.setVisibility(View.GONE);
		}

		clubButton.setText("来自【" + clubhead.club_title + "】 >");

		nameView.setText(clubhead.nickname);
		dateView.setText(clubhead.createdate);
		contentView.setText(clubhead.content);
		// 头像
		ImageLoader.getInstance().displayImage(clubhead.headicon, imageView,
				headerOptions);
		// 话题帖
		if (clubhead.topic_image != null && clubhead.topic_image.size() > 0)
		{
			contentImageView.setVisibility(View.VISIBLE);
			iv_load.setVisibility(View.VISIBLE);
			animationDrawable = (AnimationDrawable) iv_load.getDrawable();
			animationDrawable.start();

			ViewTreeObserver vto = contentImageView.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{
				@SuppressLint("NewApi")
				@Override
				public void onGlobalLayout()
				{
					ImageWidth = contentImageView.getWidth();
					ViewTreeObserver obs = contentImageView
							.getViewTreeObserver();
					obs.removeOnGlobalLayoutListener(this);

					ImageLoader.getInstance().displayImage(
							clubhead.topic_image.get(0).image,
							contentImageView, options,
							new ImageLoadingListener()
							{

								@Override
								public void onLoadingStarted(String imageUri,
										View view)
								{
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason)
								{
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage)
								{
									// TODO Auto-generated method stub
									iv_load.setVisibility(View.GONE);
									if ( animationDrawable != null )
									{
										animationDrawable.stop();
										animationDrawable = null;
									}

									int w = loadedImage.getWidth();
									int h = loadedImage.getHeight();
									int imgh = ImageWidth * h / w;
									android.view.ViewGroup.LayoutParams l = contentImageView
											.getLayoutParams();
									l.height = imgh;
									// Drawable d = new
									// BitmapDrawable(loadedImage);
									// contentImageView.setImageBitmap(null);
									// contentImageView.setBackground(d);
								}

								@Override
								public void onLoadingCancelled(String imageUri,
										View view)
								{
									// TODO Auto-generated method stub

								}
							});

				}
			});

		} else
		{
			contentImageView.setVisibility(View.GONE);
			contentImageView.setImageBitmap(null);
		}
		String replynum = clubhead.replytopic;
		if ("0".equals(replynum))
		{
			replynum = "1";
		}
		tv_reply_number.setText(replynum + "个回复");
//		// 删帖
//		if (Common.clubDelete(clubhead.is_jion))
//		{
//			tv_floor_delete.setVisibility(View.VISIBLE);
//		} else
//		{
//			tv_floor_delete.setVisibility(View.GONE);
//		}
		//

		tv_like.setText(Common.getLikeNum(clubhead.likeNum));

		if ( "1".equals(clubhead.LikeStatus))
		{
			tv_like.setTextColor(getResources().getColor(R.color.color_like_press_red));
		}
		else
		{
			tv_like.setTextColor(getResources().getColor(R.color.pull_text_color));
		}
		
		iv_like.setImageResource("1".equals(clubhead.LikeStatus) ? R.drawable.like_button_press
				: R.drawable.like_button);
	}

	public void setTopic(LSClubTopicActivity lsTopic)
	{
		this.lsTopic = lsTopic;
	}

	private void buildOptions()
	{
		options = ImageUtil.getclub_topic_imageOptions();

		headerOptions = ImageUtil.getclub_topic_headImageOptions();
	}

	public void setClubName(String name, int id)
	{
		clubName = name;
		clubID = id;
	}

	public LSClubTopicHead(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		c = context;
		init();
	}

	public LSClubTopicHead(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		c = context;
		init();
	}

	private void init()
	{
		inflater = LayoutInflater.from(c);
		v = inflater.inflate(R.layout.club_topic_header, null);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(v, lp);

		buildOptions();

		vipStar = v.findViewById(R.id.vipStar);

		imageView = (ImageView) v.findViewById(R.id.roundedImageView1);
		contentImageView = (ImageView) v.findViewById(R.id.contentImageView);

		titleView = (TextView) v.findViewById(R.id.titleView);
		nameView = (TextView) v.findViewById(R.id.nameView);
		dateView = (TextView) v.findViewById(R.id.dateView);
		contentView = (TextView) v.findViewById(R.id.contentView);

		clubButton = (TextView) v.findViewById(R.id.clubButton);
		clubButton.setOnClickListener(this);

		// ===========2.3====================
		tv_reply_number = (TextView) findViewById(R.id.tv_reply_number);
		tv_floor = (TextView) findViewById(R.id.tv_floor);
		tv_floor_delete = (TextView) findViewById(R.id.tv_floor_delete);
		tv_like = (TextView) findViewById(R.id.tv_like);
		layout_club_detail_reply = (LinearLayout) findViewById(R.id.layout_club_detail_reply);
		layout_club_detail_like = (LinearLayout) findViewById(R.id.layout_club_detail_like);

		tv_floor_delete.setOnClickListener(this);
		layout_club_detail_like.setOnClickListener(this);
		layout_club_detail_reply.setOnClickListener(this);

		iv_like = (ImageView) findViewById(R.id.iv_like);

		tv_floor.setVisibility(View.GONE);

		iv_load= (ImageView) findViewById(R.id.iv_load);



	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		// 赞
			case R.id.layout_club_detail_like:
				if ( clubhead == null ) return;
				if ( "1".equals(clubhead.LikeStatus))
				{
					return;
				}
				LSRequestManager.getInstance().mClubTopicInfoLike(clubhead.topic_id, new CallBack()
				{
					
					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						iv_like.setImageResource(R.drawable.like_button_press);
						clubhead.LikeStatus = "1";
						int num = Common.string2int(clubhead.likeNum);
						num += 1;
						clubhead.likeNum = ""+num;
						tv_like.setText(clubhead.likeNum);
						tv_like.setTextColor(getResources().getColor(R.color.color_like_press_red));
					}
				});
				// 调用赞的接口
				break;
			// 回复
			case R.id.layout_club_detail_reply:
				lsTopic.showReplyPanel();
				break;
			case R.id.tv_floor_delete:
				lsTopic.delTopic();
				break;
			case R.id.clubButton:
			{
				Intent intent = new Intent(c, LSClubDetailActivity.class);
				intent.putExtra("clubID", clubID);
				c.startActivity(intent);
			}
				break;
		}
	}

}
