package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.apply.LSApplayNew;
import com.lis99.mobile.club.model.ClubTopicDetailHead;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LSClubTopicHeadActive extends LinearLayout implements
		android.view.View.OnClickListener
{

	private LayoutInflater inflater;
	private Context c;
	private View v;

	public LSClubTopicImageListener lsClubTopicImageListener;

	private ImageView iv_head, vipStar;
	private RoundedImageView roundedImageView1;
	private TextView nameView, dateView;

	private TextView titleView, tv_active_style;
	private TextView tv_time, tv_location, tv_rmb, tv_detail, tv_end_time;

	private LinearLayout layout_detail;

	private Button actionButton;

//	private TextView tv_like;

	// LSClubTopic topic;
	// 俱乐部名称,ID
	String clubName;
	int clubID;

	DisplayImageOptions options;
	DisplayImageOptions headerOptions;

	private LSClubTopicActivity lsTopic;

	private ClubTopicDetailHead clubhead;
	
//	private ImageView iv_like;

	private ImageView iv_load;

	//======3.5=====
	private LinearLayout layout_tag;
	private TextView tv_tag1, tv_tag2, tv_tag3;

	//=====3.5.3====
//	精
	private ImageView iv_best;

	View equiPanel;
	ImageView equiImageView;
	TextView equiPriceView;
	TextView equiNameView;
	RatingBar equiRatingBar;

//	====3.5.5====
	private LSClubTopicHeadLike like;

//	====3.6.3=====
	private View layout_club_name;
	private TextView tv_club_name;
	private TextView tv_click_reply;
	private Button btn_attention;

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

	public LSClubTopicHeadActive(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		c = context;
		init();
	}

	public LSClubTopicHeadActive(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		c = context;
		init();
	}

	public void init()
	{
		buildOptions();
		inflater = LayoutInflater.from(c);
		v = inflate(c, R.layout.club_topic_header_active, null);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(v, lp);

//		=======3.5.5=======赞
		like = new LSClubTopicHeadLike(c);
		like.InitView(v);

		iv_head = (ImageView) v.findViewById(R.id.iv_head);
		vipStar = (ImageView) v.findViewById(R.id.vipStar);

		roundedImageView1 = (RoundedImageView) v
				.findViewById(R.id.roundedImageView1);
		roundedImageView1.setOnClickListener(this);

		titleView = (TextView) v.findViewById(R.id.titleView);
		nameView = (TextView) v.findViewById(R.id.nameView);
		dateView = (TextView) v.findViewById(R.id.dateView);

		tv_club_name = (TextView) v.findViewById(R.id.tv_club_name);

		actionButton = (Button) v.findViewById(R.id.actionButton);
		actionButton.setOnClickListener(this);

		// ===========2.3====================
//		tv_like = (TextView) findViewById(R.id.tv_like);

		tv_active_style = (TextView) v.findViewById(R.id.tv_active_style);

		layout_detail = (LinearLayout) findViewById(R.id.layout_detail);
		layout_detail.setOnClickListener(this);

		// tv_time, tv_location, tv_rmb, tv_detail, tv_end_time;
		tv_time = (TextView) v.findViewById(R.id.tv_time);
		tv_location = (TextView) v.findViewById(R.id.tv_location);
		tv_rmb = (TextView) v.findViewById(R.id.tv_rmb);
		tv_detail = (TextView) v.findViewById(R.id.tv_detail);
		tv_end_time = (TextView) v.findViewById(R.id.tv_end_time);

//		iv_like = (ImageView) findViewById(R.id.iv_like);

		iv_load = (ImageView) findViewById(R.id.iv_load);

		//====3.5======
		layout_tag = (LinearLayout) v.findViewById(R.id.layout_tag);
		tv_tag1 = (TextView) v.findViewById(R.id.tv_tag1);
		tv_tag2 = (TextView) v.findViewById(R.id.tv_tag2);
		tv_tag3 = (TextView) v.findViewById(R.id.tv_tag3);

		//=====3.5.3====
		iv_best = (ImageView) findViewById(R.id.iv_best);
		iv_best.setVisibility(GONE);

		equiPanel =  v.findViewById(R.id.equiPanel);
		equiImageView = (ImageView)  v.findViewById(R.id.equiImageView);
		equiPriceView = (TextView)  v.findViewById(R.id.equiPriceView);
		equiNameView = (TextView)  v.findViewById(R.id.equiNameView);
		equiRatingBar = (RatingBar)  v.findViewById(R.id.equiRatingBar);

//		3.6.3----
		layout_club_name = v.findViewById(R.id.layout_club_name);
		layout_club_name.setOnClickListener(this);
		tv_click_reply = (TextView) v.findViewById(R.id.tv_click_reply);
		tv_click_reply.setOnClickListener(this);
		btn_attention = (Button) v.findViewById(R.id.btn_attention);
		btn_attention.setOnClickListener(this);
//		btn_attention.setVisibility(GONE);

	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		// 俱乐部详情
			case R.id.layout_club_name:
				Intent intent = new Intent(c, LSClubDetailActivity.class);
				intent.putExtra("clubID", clubID);
				c.startActivity(intent);
				break;
			// 报名
			case R.id.actionButton:
				doAction();
				break;
//			// 删除
//			case R.id.tv_floor_delete:
//				lsTopic.delTopic();
//				break;
			// 点赞
			case R.id.layout_club_detail_like:
				if ( clubhead == null ) return;
				//点过赞
				if ( "1".equals(clubhead.LikeStatus) )
				{
					return;
				}
				LSRequestManager.getInstance().mClubTopicInfoLike(clubhead.topic_id, new CallBack()
				{
					
					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
//						iv_like.setImageResource(R.drawable.like_button_press);
						clubhead.LikeStatus = "1";
						int num = Common.string2int(clubhead.likeNum);
						num += 1;
						clubhead.likeNum = ""+num;
//						tv_like.setText(clubhead.likeNum);
//						tv_like.setTextColor(getResources().getColor(R.color.color_like_press_red));
					}
				});
				break;
			// 回复
			case R.id.tv_click_reply:
				lsTopic.showReplyPanel();
				break;
			// 活动详情
			case R.id.roundedImageView1:
			{
				Intent intent2 = new Intent(c, LSUserHomeActivity.class);
				intent2.putExtra("userID", clubhead.user_id);
				c.startActivity(intent2);
			}
			break;
			case R.id.layout_detail:
				Intent i = new Intent(c, LSClubActiveDetail.class);
				i.putExtra("topic_id", clubhead.topic_id);
				c.startActivity(i);
				break;
			case R.id.btn_attention:
				btn_attention.setVisibility(GONE);
				if ( clubhead == null ) return;
				LSRequestManager.getInstance().getFriendsAddAttention(Common.string2int(clubhead.user_id), null);
				break;
		}
	}

	// public void setModel (LSClubTopic topic)
	// {
	// this.topic = topic;
	//
	// titleView.setText("【活动】" + topic.getTitle());
	//
	// if (topic.getIs_vip() == 1) {
	// vipStar.setVisibility(View.VISIBLE);
	// } else {
	// vipStar.setVisibility(View.GONE);
	// }
	//
	// clubButton.setText("来自【" + clubName + "】 >");
	//
	// nameView.setText(topic.getNickname());
	// dateView.setText(topic.getCreatedate());
	// ImageLoader.getInstance().displayImage(topic.getHeadicon(),
	// roundedImageView1, headerOptions);
	// //大图
	// if (topic.getImagelist() != null && topic.getImagelist().size() > 0) {
	// ImageLoader.getInstance().displayImage(
	// topic.getImagelist().get(0).getImage(),
	// iv_head, options);
	// }
	// // 权限1创始人，2管理员，4成员,8网站编辑
	// if (topic.getIs_jion() == 4 || topic.getIs_jion() == -1) {
	// } else {
	// //
	// actionButton.setBackgroundResource(R.drawable.club_topic_checkapply_bg);
	// actionButton.setText("查看已报名用户");
	// }
	// }

	public int getHeadHeight()
	{
		return ImageWidth;
	}
	private int ImageWidth = 0;
	private void getHeight ( final ImageView v )
	{
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				ImageWidth = v.getHeight();
				
				Common.log("ImageWidth="+ImageWidth);
				ViewTreeObserver obs = v.getViewTreeObserver();
				obs.removeOnGlobalLayoutListener(this);
			}
		});
	}

	public void setModel(final ClubTopicDetailHead clubhead)
	{
		this.clubhead = clubhead;
//赞
		like.setInfo(clubhead);

		titleView.setText(clubhead.title);

		if ("1".equals(clubhead.is_vip))
		{
			vipStar.setVisibility(View.VISIBLE);
		} else
		{
			vipStar.setVisibility(View.GONE);
		}

		//3.6.3===

		tv_club_name.setText("来自 " + clubhead.club_title);


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
		ImageLoader.getInstance().displayImage(clubhead.headicon,
				roundedImageView1, headerOptions, ImageUtil.getImageLoading(iv_load, iv_head));
		// 大图
		if (clubhead.topic_image != null && clubhead.topic_image.size() > 0)
		{

			iv_head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (lsClubTopicImageListener != null) {
						lsClubTopicImageListener.onClickImage(clubhead.topic_image.get(0).image);
					}
				}
			});

			ImageLoader.getInstance().displayImage(
					clubhead.topic_image.get(0).image, iv_head, options);
		}
		//帖子属性
		tv_active_style.setText(clubhead.catename + " " + clubhead.hardDegree);
//		＝＝＝＝＝＝
//		tv_time.setText(clubhead.createdate + " 至 " + clubhead.deadline);
		tv_time.setText(clubhead.times);
		tv_location.setText(clubhead.aimaddress);
		tv_rmb.setText(clubhead.consts);
		
		tv_end_time.setText("截止时间:" + clubhead.deadline);
		
		
		String replynum = clubhead.replytopic;
		if ( "0".equals(replynum) )
		{
			replynum = "1";
		}
//		//删帖
//		if ( Common.clubDelete(clubhead.is_jion) )
//		{
//			tv_floor_delete.setVisibility(View.VISIBLE);
//		}
//		else
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
		
//		iv_like.setImageResource("1".equals(clubhead.LikeStatus) ? R.drawable.like_button_press : R.drawable.like_button);

		//报名已结束
		if ( clubhead.applyTimeStatus == 1 )
		{
			applyPast();
		}
		else
		{
			// 权限1创始人，2管理员，4成员,8网站编辑
//			if ("4".equals(clubhead.is_jion) || "-1".equals(clubhead.is_jion))
//			{
				//已报名
				if (  clubhead.applyStauts == 1 )
				{
					applyOK();
				}
//			}
		}

//		else
//		{
//			// actionButton.setBackgroundResource(R.drawable.club_topic_checkapply_bg);
//			actionButton.setText("查看已报名用户");
//		}



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

		getHeight(iv_head);

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
	
	public void doAction()
	{
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userID))
		{
			Intent intent = new Intent(getContext(), LSLoginActivity.class);
			getContext().startActivity(intent);
		} else
		{
			Intent intent = new Intent(getContext(), LSApplayNew.class);
				intent.putExtra("clubID", clubID);
				intent.putExtra("topicID", Common.string2int(clubhead.topic_id));
				intent.putExtra("clubName", clubName);
				lsTopic.startActivityForResult(intent, 997);

//			if ("4".equals(clubhead.is_jion) || "-1".equals(clubhead.is_jion))
//			{
//				Intent intent = new Intent(getContext(), LSClubApplyActivity.class);
//				intent.putExtra("clubID", clubID);
//				intent.putExtra("topicID", Common.string2int(clubhead.topic_id));
//				intent.putExtra("clubName", clubName);
////				lsTopic.startActivity(intent);
//				lsTopic.startActivityForResult(intent, 997);
//			}
//			else
//			{
//				Intent intent = new Intent(getContext(), LSClubApplyListActivity.class);
//				intent.putExtra("clubID", clubID);
//				intent.putExtra("topicID", Common.string2int(clubhead.topic_id));
//				intent.putExtra("clubName", clubName);
//				getContext().startActivity(intent);
//			}
		}
	}
	
	public void applyOK ()
	{
		clubhead.applyStauts = 1;
		actionButton.setText("已报名");
		actionButton.setEnabled(false);
		actionButton.setClickable(false);
	}
	//过期
	public void applyPast ()
	{
		actionButton.setText("报名已截止");
		actionButton.setBackgroundResource(R.drawable.club_action_past);
		actionButton.setClickable(false);
		actionButton.setEnabled(false);
	}

	public void setLikeHandler (HandlerList likeCall)
	{
		like.setLikeCall(likeCall);
	}

	
}
