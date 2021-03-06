package com.lis99.mobile.club.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicReplyActivity;
import com.lis99.mobile.club.model.ClubTopicReplyList.Topiclist;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class LSClubTopicCommentAdapter extends BaseAdapter
{

	// List<LSClubTopicComment> data;
	public ArrayList<Topiclist> topiclist;

	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	DisplayImageOptions headerOptions;
	Drawable drawable;
	private int clubId, topicId;

	private Activity main;

	public LSClubTopicImageListener lsClubTopicCommentListener;

	class CommentOnClickListener implements OnClickListener
	{
		Topiclist comment;
		ImageView img;
		TextView tvreply;
		private int position;

		public void setReply(TextView tv)
		{
			tvreply = tv;
		}

		CommentOnClickListener(Topiclist comment, int position )
		{
			this.comment = comment;
			this.position = position;
		}

		public void setImageView(ImageView img)
		{
			this.img = img;
		}

		@Override
		public void onClick(View v)
		{
			// 删除
			if (v.getId() == R.id.tv_floor_delete)
			{
				deleteNow(comment, position);
			}
			// 回复
			else if (v.getId() == R.id.layout_club_detail_reply)
			{
				replyNow(comment);
			}
			// 点赞
			else if (v.getId() == R.id.layout_club_detail_like)
			{
				likeNow(comment, img, tvreply);
			}

			else if (v.getId() == R.id.roundedImageView1)
			{
				Intent intent = new Intent(main, LSUserHomeActivity.class);
				intent.putExtra("userID", comment.user_id);
				main.startActivity(intent);
			} else if (v.getId() == R.id.equiPanel) {
				Intent intent = new Intent(main,LSEquipInfoActivity.class);
				intent.putExtra("id", comment.zhuangbei_id);
				main.startActivity(intent);
			}
		}

	}

	private void buildOptions()
	{
		options = ImageUtil.getclub_topic_imageOptions();

		headerOptions = ImageUtil.getclub_topic_headImageOptions();
	}

	public LSClubTopicCommentAdapter(Activity main,
			ArrayList<Topiclist> topiclist, int clubId, int topicId)
	{
		this.main = main;
		this.clubId = clubId;
		this.topicId = topicId;
		this.topiclist = topiclist;
		inflater = LayoutInflater.from(main);
		buildOptions();
		drawable = LSBaseActivity.activity.getResources().getDrawable(
				R.drawable.club_tier_master);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
	}

	public void addList(ArrayList<Topiclist> topiclist)
	{
		this.topiclist.addAll(topiclist);
		notifyDataSetChanged();
	}

	public void addListUp(ArrayList<Topiclist> topiclist)
	{
		this.topiclist.addAll(0, topiclist);
		notifyDataSetChanged();
	}

	private void remove(int position )
	{
		if ( topiclist == null || topiclist.size() == 0 || topiclist.size() <= position )
		{
			return;
		}
		topiclist.remove(position);
		notifyDataSetChanged();

	}

	@Override
	public int getCount()
	{
		return topiclist == null ? 0 : topiclist.size();
	}

	@Override
	public Object getItem(int position)
	{
		return topiclist == null ? null : topiclist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.club_topic_comment_item,
					null);
			holder = new ViewHolder();
			holder.nameView = (TextView) convertView
					.findViewById(R.id.nameView);

			holder.iv_moderator = convertView.findViewById(R.id.iv_moderator);
			holder.ivFloor = convertView.findViewById(R.id.iv_tag_floor);

			//		4.1.1
			holder.tv_user_tag3 = (TextView) convertView.findViewById(R.id.tv_user_tag3);

			holder.tv_user_tag4 = (TextView) convertView.findViewById(R.id.tv_user_tag4);


			holder.imageView = (ImageView) convertView
					.findViewById(R.id.roundedImageView1);
			holder.contentImageView = (ImageView) convertView
					.findViewById(R.id.contentImageView);
			holder.dateView = (TextView) convertView
					.findViewById(R.id.dateView);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.contentView);
			holder.vipStar = convertView.findViewById(R.id.vipStar);
			// 2.3
			holder.layout_club_detail_like = (LinearLayout) convertView
					.findViewById(R.id.layout_club_detail_like);
			holder.layout_club_detail_reply = (LinearLayout) convertView
					.findViewById(R.id.layout_club_detail_reply);
			holder.tv_like = (TextView) convertView.findViewById(R.id.tv_like);
			holder.tv_floor_delete = (TextView) convertView
					.findViewById(R.id.tv_floor_delete);
			holder.tv_floor = (TextView) convertView
					.findViewById(R.id.tv_floor);
			holder.reply_view = convertView.findViewById(R.id.reply_view);
			holder.tv_reply_body = (TextView) convertView
					.findViewById(R.id.tv_reply_body);
			holder.tv_reply_floor = (TextView) convertView
					.findViewById(R.id.tv_reply_floor);
			holder.tv_reply_content = (TextView) convertView
					.findViewById(R.id.tv_reply_content);
			holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_like);

			holder.iv_load = (ImageView) convertView.findViewById(R.id.iv_load);

			holder.layout_tag = (LinearLayout) convertView.findViewById(R.id.layout_tag);


			holder.equiPanel =  convertView.findViewById(R.id.equiPanel);
			holder.equiImageView = (ImageView)  convertView.findViewById(R.id.equiImageView);
			holder.equiPriceView = (TextView)  convertView.findViewById(R.id.equiPriceView);
			holder.equiNameView = (TextView)  convertView.findViewById(R.id.equiNameView);
			holder.equiRatingBar = (RatingBar)  convertView.findViewById(R.id.equiRatingBar);


			convertView.setTag(holder);

			convertView.setLayoutParams(new ListView.LayoutParams(
					ListView.LayoutParams.MATCH_PARENT,
					ListView.LayoutParams.WRAP_CONTENT));


		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.layout_tag.setVisibility(View.INVISIBLE);

		// LSClubTopicComment item = data.get(position);
		final Topiclist item = (Topiclist) getItem(position);
		if (item == null)
			return convertView;

		//4.0.2===== 撰稿人

		holder.tv_user_tag3.setVisibility(View.GONE);
		holder.tv_user_tag4.setVisibility(View.GONE);

		if ( item.tags_name != null && item.tags_name.size() != 0 )
		{
			holder.tv_user_tag3.setVisibility(View.VISIBLE);
			holder.tv_user_tag3.setText(Common.getTagString(item.tags_name.get(0).title));
			if ( item.tags_name.size() > 1 )
			{
				holder.tv_user_tag4.setVisibility(View.VISIBLE);
				holder.tv_user_tag4.setText(Common.getTagString(item.tags_name.get(1).title));
			}
		}

//		if (Common.isWriter(item.tags))
//		{
//			holder.iv_writer.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			holder.iv_writer.setVisibility(View.GONE);
//		}

		if ( Common.isModerator(item.moderator))
		{
			holder.iv_moderator.setVisibility(View.VISIBLE);
		}
		else {
			holder.iv_moderator.setVisibility(View.GONE);
		}

		holder.nameView.setText(item.nickname);
		holder.dateView.setText(item.createdate);
		//===emotion=====
		holder.contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion(main, item.content));
		imageLoader
				.displayImage(item.headicon, holder.imageView, headerOptions);

		if ("1".equals(item.is_vip))
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
//隐藏回复前面的赞
		holder.layout_club_detail_like.setVisibility(View.GONE);

		CommentOnClickListener l = new CommentOnClickListener(item, position);
//		holder.layout_club_detail_like.setOnClickListener(l);
//		l.setReply(holder.tv_like);
//		l.setImageView(holder.iv_like);
		holder.layout_club_detail_reply.setOnClickListener(l);
		holder.tv_floor_delete.setOnClickListener(l);
		holder.imageView.setOnClickListener(l);

		if (item.topic_image != null && item.topic_image.size() > 0)
		{

			holder.contentImageView.setVisibility(View.VISIBLE);
			holder.contentImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (lsClubTopicCommentListener != null) {
						lsClubTopicCommentListener.onClickImage(item.topic_image.get(0).image);
					}
				}
			});
			holder.iv_load.setVisibility(View.VISIBLE);
			// imageLoader.displayImage(item.topic_image.get(0).image,
			// holder.contentImageView, options);
			getWidth(holder.contentImageView, holder.iv_load, item);
//			ImageLoader.getInstance().displayImage(item.topic_image.get(0).image, holder.contentImageView, options, ImageUtil.getImageLoading(holder.iv_load, holder.contentImageView));
		} else
		{
			holder.contentImageView.setVisibility(View.GONE);
//			holder.contentImageView.setImageBitmap(null);
			holder.iv_load.setVisibility(View.GONE);
		}
		if ("0".equals(item.reply_id))
		{
			holder.reply_view.setVisibility(View.GONE);
		} else
		{
			holder.reply_view.setVisibility(View.VISIBLE);
			// 回复内容＝＝＝＝＝
			holder.tv_reply_body.setText("回复@ " + item.reply_nickname);
			holder.tv_reply_floor.setText(item.reply_floor + "楼");
			//====emotion====
			holder.tv_reply_content.setText(MyEmotionsUtil.getInstance().getTextWithEmotion(main, item.reply_content));
		}
		// 1为楼主， 其他不是
		if ("1".equals(item.is_lander))
		{
			holder.ivFloor.setVisibility(View.VISIBLE);
		} else
		{
			holder.ivFloor.setVisibility(View.GONE);
		}

		holder.tv_floor.setText(item.floor + "楼");
		if (Common.replyDelete(item.is_jion, item.user_id))
		{
			holder.tv_floor_delete.setVisibility(View.VISIBLE);
		} else
		{
			holder.tv_floor_delete.setVisibility(View.GONE);
		}

		if ("1".equals(item.LikeStatus))
		{
			holder.iv_like.setImageResource(R.drawable.like_button_press);
			holder.tv_like.setTextColor(main.getResources().getColor(
					R.color.color_like_press_red));
		} else
		{
			holder.iv_like.setImageResource(R.drawable.like_button);
			holder.tv_like.setTextColor(main.getResources().getColor(
					R.color.pull_text_color));
		}

		holder.tv_like.setText(Common.getLikeNum(item.likeNum));



		if (item.zhuangbei_id != 0) {
			holder.equiPanel.setVisibility(View.VISIBLE);
			holder.equiRatingBar.setRating(item.zhuangbei_star);
			ImageLoader.getInstance().displayImage(item.zhuangbei_image, holder.equiImageView);
			holder.equiNameView.setText(item.zhuangbei_title);
			holder.equiPriceView.setText("市场价："+item.zhuangbei_price+"元");


			holder.equiPanel.setOnClickListener(l);

		} else {
			holder.equiPanel.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder
	{
		View iv_moderator;
		View ivFloor;
		ImageView imageView;
		ImageView contentImageView;
		TextView nameView;
		TextView dateView;
		TextView contentView;
		View vipStar;
		// 点赞， 回复
		LinearLayout layout_club_detail_like, layout_club_detail_reply;
		// 点赞的图片
		ImageView iv_like;
		// 点赞的数量
		TextView tv_like;
		// 删除
		TextView tv_floor_delete;
		// 楼层
		TextView tv_floor;
		// 回复的
		View reply_view;
		// 回复里面的内容, 回复的谁， 回复的楼层， 回复的内容
		TextView tv_reply_body, tv_reply_floor, tv_reply_content;

		ImageView iv_load;
		//===3.5===
		LinearLayout layout_tag;

		View equiPanel;
		ImageView equiImageView;
		TextView equiPriceView;
		TextView equiNameView;
		RatingBar equiRatingBar;

		//	4.1.1
		private TextView tv_user_tag3, tv_user_tag4;

	}

	private void replyNow(Topiclist comment)
	{
		String replyName = comment.nickname;
		String replyID = comment.replytopic_id;
		if (!Common.isLogin(LSBaseActivity.activity))
		{
			return;
		}
		Intent intent = new Intent(main, LSClubTopicReplyActivity.class);
		intent.putExtra("replyedName", comment.nickname);
		intent.putExtra("replyedcontent", comment.content);
		intent.putExtra("replyedfloor", comment.floor);
		intent.putExtra("replyedId", comment.replytopic_id);
		intent.putExtra("clubId", "" + clubId);
		intent.putExtra("topicId", "" + topicId);
		// LSBaseActivity.activity.startActivity(intent);
		main.startActivityForResult(intent, 999);
	}

	private void likeNow(final Topiclist comment, final ImageView v,
			final TextView tv)
	{
		if ("1".equals(comment.LikeStatus))
		{
			return;
		}
		LSRequestManager.getInstance().mClubTopicInfoLike(
				comment.replytopic_id, new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						if (v != null)
						{
							v.setImageResource(R.drawable.like_button_press);
							comment.LikeStatus = "1";
							int num = Common.string2int(comment.likeNum);
							num += 1;
							comment.likeNum = "" + num;
							tv.setText(comment.likeNum);
							tv.setTextColor(main.getResources().getColor(
									R.color.color_like_press_red));
						}
					}

				});
	}

	private void deleteNow(final Topiclist comment, final int position )
	{
		LSRequestManager.getInstance().mClubTopicReplyDelete("" + clubId,
				"" + comment.replytopic_id, new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
//						main.refrenshReply();
						remove(position);

						if ( comment.topic_image != null && comment.topic_image.size() > 0 )
						{
							LSScoreManager.getInstance().sendScore(comment.user_id, LSScoreManager.delreplytopicbyimg, ""+clubId);
						}
						else
						{
							LSScoreManager.getInstance().sendScore(comment.user_id, LSScoreManager.delreplytopicbynoimg, ""+clubId);
						}

					}

				});
	}

//	int ImageWidth = -1;
	int ImageWidth = Common.WIDTH;// - Common.dip2px(10);
	private AnimationDrawable animationDrawable;

	private void getWidth(final ImageView v,final ImageView load, final Topiclist item)
	{
		animationDrawable = (AnimationDrawable) load.getDrawable();
		if ( !animationDrawable.isRunning() )
		animationDrawable.start();

		if (ImageWidth == -1)
		{
			ViewTreeObserver vto = v.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{
				@SuppressLint("NewApi")
				@Override
				public void onGlobalLayout()
				{
					ImageWidth = v.getWidth();
					ViewTreeObserver obs = v.getViewTreeObserver();
					obs.removeOnGlobalLayoutListener(this);

					ImageLoader.getInstance().displayImage(
							item.topic_image.get(0).image, v, options,
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
									load.setVisibility(View.GONE);
									if ( animationDrawable != null )
									{
										animationDrawable.stop();
										animationDrawable = null;
									}
									int w = loadedImage.getWidth();
									int h = loadedImage.getHeight();
									int imgh = ImageWidth * h / w;
									android.view.ViewGroup.LayoutParams l = v
											.getLayoutParams();
									l.height = imgh;
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
			ImageLoader.getInstance().displayImage(
					item.topic_image.get(0).image, v, options,
					new ImageLoadingListener()
					{

						@Override
						public void onLoadingStarted(String imageUri, View view)
						{
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason)
						{
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage)
						{
							// TODO Auto-generated method stub
							load.setVisibility(View.GONE);
							if ( animationDrawable != null )
							{
								animationDrawable.stop();
								animationDrawable = null;
							}
							int w = loadedImage.getWidth();
							int h = loadedImage.getHeight();
							int imgh = ImageWidth * h / w;
							android.view.ViewGroup.LayoutParams l = v
									.getLayoutParams();
							l.height = imgh;
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view)
						{
							// TODO Auto-generated method stub

						}
					});
		}

	}

}
