package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.model.ClubTopicDetailHead;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
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
	TextView dateView, lookNum;
	TextView contentView;

	View vipStar;


	View equiPanel;
	ImageView equiImageView;
	TextView equiPriceView;
	TextView equiNameView;
	RatingBar equiRatingBar;

	ImageView imageView;
	ImageView contentImageView;

	// ========2.3===========
	private int ImageWidth;
	// 楼层数, 删除楼层
	private Button btn_attention;
//	private TextView tv_floor_delete;
	// 攒, 标题文字
//	private TextView tv_like;
	// 回复， 点赞
//	private LinearLayout layout_club_detail_like;

	// LSClubTopic topic;
	// 俱乐部名称,ID
	String clubName;
	int clubID;

	DisplayImageOptions options;
	DisplayImageOptions headerOptions;

	private LSClubTopicActivity lsTopic;

	private ClubTopicDetailHead clubhead;

	public LSClubTopicImageListener lsClubTopicImageListener;

//	private ImageView iv_like;
//点
	private ImageView iv_load;

	private AnimationDrawable animationDrawable;

	//======3.5=====
	private LinearLayout layout_tag;
	private TextView tv_tag1, tv_tag2, tv_tag3;

	//======3.5.3=====
	private ImageView iv_best;

//	＝＝＝＝＝＝3.5.5=＝＝＝＝＝
	private LSClubTopicHeadLike like;

	//===3.6.3====
	private View layout_club_name;
	private TextView tv_club_name, tv_click_reply;

	//    ===3.9.1====
	private View iv_moderator;
	private View iv_writer;


	public void setHead(final ClubTopicDetailHead clubhead)
	{
		this.clubhead = clubhead;
		titleView.setText(clubhead.title);


		if ( Common.isModerator(clubhead.moderator))
		{
			iv_moderator.setVisibility(VISIBLE);
		}
		else
		{
			iv_moderator.setVisibility(GONE);
		}

		//===4.0.2===撰稿人
		if ( Common.isWriter(clubhead.tags))
		{
			iv_writer.setVisibility(VISIBLE);
		}
		else
		{
			iv_writer.setVisibility(GONE);
		}

		lookNum.setText(clubhead.visits+"人读过");

		//赞
		like.setInfo(clubhead);

		if ("1".equals(clubhead.is_vip))
		{
			vipStar.setVisibility(View.VISIBLE);
		} else
		{
			vipStar.setVisibility(View.GONE);
		}

		tv_club_name.setText("来自 " + clubhead.club_title);
		//==3.6.3==
		if ( clubhead.attenStatus == 0 )
		{
			btn_attention.setVisibility(VISIBLE);
		}
		else
		{
			btn_attention.setVisibility(GONE);
		}




		nameView.setText(clubhead.nickname);
		dateView.setText(clubhead.createdate);
		//====emotion====
		contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)c, clubhead.content));
		// 头像
		ImageLoader.getInstance().displayImage(clubhead.headicon, imageView,
				headerOptions);
		// 话题帖
		if (clubhead.topic_image != null && clubhead.topic_image.size() > 0)
		{
			contentImageView.setVisibility(View.VISIBLE);

			contentImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (lsClubTopicImageListener != null) {
						lsClubTopicImageListener.onClickImage(clubhead.topic_image.get(0).image);
					}
				}
			});

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

//		// 删帖
//		if (Common.clubDelete(clubhead.is_jion))
//		{
//			tv_floor_delete.setVisibility(View.VISIBLE);
//		} else
//		{
//			tv_floor_delete.setVisibility(View.GONE);
//		}
		//

//		tv_like.setText(Common.getLikeNum(clubhead.likeNum));

//		if ( "1".equals(clubhead.LikeStatus))
//		{
//			tv_like.setTextColor(getResources().getColor(R.color.color_like_press_red));
//		}
//		else
//		{
//			tv_like.setTextColor(getResources().getColor(R.color.pull_text_color));
//		}
		
//		iv_like.setImageResource("1".equals(clubhead.LikeStatus) ? R.drawable.like_button_press
//				: R.drawable.like_button);

		//====3.5======
		if ( clubhead.taglist != null && clubhead.taglist.size() != 0 )
		{
			layout_tag.setVisibility(View.VISIBLE);
			tv_tag1.setVisibility(INVISIBLE);
			tv_tag2.setVisibility(INVISIBLE);
			tv_tag3.setVisibility(INVISIBLE);
			int num = clubhead.taglist.size();
			if ( num >0 )
			{
				tv_tag1.setText("#"+clubhead.taglist.get(0).name);
				tv_tag1.setVisibility(VISIBLE);
				tv_tag1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(c, ClubSpecialListActivity.class);
						intent.putExtra("tagid", clubhead.taglist.get(0).id);
						c.startActivity(intent);
					}
				});
			}
			if ( num > 1)
			{
				tv_tag2.setText("#"+clubhead.taglist.get(1).name);
				tv_tag2.setVisibility(VISIBLE);
				tv_tag2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(c, ClubSpecialListActivity.class);
						intent.putExtra("tagid", clubhead.taglist.get(1).id);
						c.startActivity(intent);
					}
				});
			}
			if ( num > 2 )
			{
				tv_tag3.setText("#"+clubhead.taglist.get(2).name);
				tv_tag3.setVisibility(VISIBLE);
				tv_tag3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(c, ClubSpecialListActivity.class);
						intent.putExtra("tagid", clubhead.taglist.get(2).id);
						c.startActivity(intent);
					}
				});
			}
		}
		else {
			layout_tag.setVisibility(View.INVISIBLE);
		}

		//精华
		for ( int i = 0; clubhead.topicpoints != null && i < clubhead.topicpoints.size(); i++ )
		{
			//0 精华
			if ( clubhead.topicpoints.get(i).reason == 0 )
			{
				iv_best.setVisibility(VISIBLE);
				break;
			}
		}


		if (clubhead.zhuangbei_id != 0) {
			equiPanel.setVisibility(View.VISIBLE);
			equiRatingBar.setRating(clubhead.zhuangbei_star);
			ImageLoader.getInstance().displayImage(clubhead.zhuangbei_image, equiImageView);
			equiNameView.setText(clubhead.zhuangbei_title);
			equiPriceView.setText("市场价："+clubhead.zhuangbei_price+"元");


			equiPanel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(c,LSEquipInfoActivity.class);
					intent.putExtra("id", clubhead.zhuangbei_id);
					c.startActivity(intent);
				}
			});

		} else {
			equiPanel.setVisibility(View.GONE);
		}


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

		iv_moderator = v.findViewById(R.id.iv_moderator);

//		＝＝＝4.0.2=＝＝＝
		lookNum = (TextView) v.findViewById(R.id.lookNum);
		iv_writer = v.findViewById(R.id.iv_writer);

		//		=======3.5.5=======赞
		like = new LSClubTopicHeadLike(c);
		like.InitView(v);


		vipStar = v.findViewById(R.id.vipStar);

		imageView = (ImageView) v.findViewById(R.id.roundedImageView1);
		imageView.setOnClickListener(this);
		contentImageView = (ImageView) v.findViewById(R.id.contentImageView);

		titleView = (TextView) v.findViewById(R.id.titleView);
		nameView = (TextView) v.findViewById(R.id.nameView);
		dateView = (TextView) v.findViewById(R.id.dateView);
		contentView = (TextView) v.findViewById(R.id.contentView);

		layout_club_name = v.findViewById(R.id.layout_club_name);
		tv_club_name = (TextView) v.findViewById(R.id.tv_club_name);
		layout_club_name.setOnClickListener(this);

		tv_click_reply = (TextView) v.findViewById(R.id.tv_click_reply);
		tv_click_reply.setOnClickListener(this);

		// ===========2.3====================

//		tv_floor_delete = (TextView) findViewById(R.id.tv_floor_delete);
//		tv_like = (TextView) findViewById(R.id.tv_like);
//		layout_club_detail_like = (LinearLayout) findViewById(R.id.layout_club_detail_like);

//		tv_floor_delete.setOnClickListener(this);
//		layout_club_detail_like.setVisibility(INVISIBLE);
//		layout_club_detail_like.setOnClickListener(this);

//		iv_like = (ImageView) findViewById(R.id.iv_like);

		iv_load= (ImageView) findViewById(R.id.iv_load);

//====3.5======
		layout_tag = (LinearLayout) v.findViewById(R.id.layout_tag);
		tv_tag1 = (TextView) v.findViewById(R.id.tv_tag1);
		tv_tag2 = (TextView) v.findViewById(R.id.tv_tag2);
		tv_tag3 = (TextView) v.findViewById(R.id.tv_tag3);

		//=====3.5.3======
//		精
		iv_best = (ImageView) findViewById(R.id.iv_best);
		iv_best.setVisibility(GONE);

		equiPanel =  v.findViewById(R.id.equiPanel);
		equiImageView = (ImageView)  v.findViewById(R.id.equiImageView);
		equiPriceView = (TextView)  v.findViewById(R.id.equiPriceView);
		equiNameView = (TextView)  v.findViewById(R.id.equiNameView);
		equiRatingBar = (RatingBar)  v.findViewById(R.id.equiRatingBar);

//		3.6.3===
		btn_attention = (Button) findViewById(R.id.btn_attention);
		btn_attention.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		// 赞
//			case R.id.layout_club_detail_like:
//				if ( clubhead == null ) return;
//				if ( "1".equals(clubhead.LikeStatus))
//				{
//					return;
//				}
//				LSRequestManager.getInstance().mClubTopicInfoLike(clubhead.topic_id, new CallBack()
//				{
//
//					@Override
//					public void handler(MyTask mTask)
//					{
//						// TODO Auto-generated method stub
//						iv_like.setImageResource(R.drawable.like_button_press);
//						clubhead.LikeStatus = "1";
//						int num = Common.string2int(clubhead.likeNum);
//						num += 1;
//						clubhead.likeNum = ""+num;
//						tv_like.setText(clubhead.likeNum);
//						tv_like.setTextColor(getResources().getColor(R.color.color_like_press_red));
//					}
//				});
//				// 调用赞的接口
//				break;
			// 回复
			case R.id.tv_click_reply:
				lsTopic.showReplyPanel();
				break;
//			case R.id.tv_floor_delete:
//				lsTopic.delTopic();
//				break;
			case R.id.roundedImageView1:
			{
				Intent intent = new Intent(c, LSUserHomeActivity.class);
				intent.putExtra("userID", clubhead.user_id);
				c.startActivity(intent);
			}
		break;
			case R.id.layout_club_name:
			{
				Intent intent = new Intent(c, LSClubDetailActivity.class);
				intent.putExtra("clubID", clubID);
				c.startActivity(intent);
			}
				break;
			case R.id.btn_attention:

				btn_attention.setVisibility(GONE);
				if ( clubhead == null ) return;
				LSRequestManager.getInstance().getFriendsAddAttention(Common.string2int(clubhead.user_id), null);
				break;
		}
	}

	public void setLikeHandler (HandlerList likeCall)
	{
		like.setLikeCall(likeCall);
	}

}
