package com.lis99.mobile.club.newtopic;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.model.ClubTopicNewReplyList;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by yy on 16/3/14.
 */
public class LSClubNewTopicReplyAdapter extends MyBaseAdapter {

    Drawable drawable;

    private int topicId, clubId;

    public LSClubTopicImageListener lsClubTopicCommentListener;

    class CommentOnClickListener implements View.OnClickListener
    {
        ClubTopicNewReplyList.TopicsreplylistEntity comment;
        ImageView img;
        TextView tvreply;

        public void setReply(TextView tv)
        {
            tvreply = tv;
        }

        CommentOnClickListener(ClubTopicNewReplyList.TopicsreplylistEntity comment, int position )
        {
            this.comment = comment;
//            this.position = position;
        }

        public void setImageView(ImageView img)
        {
            this.img = img;
        }

        @Override
        public void onClick(View v)
        {
//            // 删除
//            if (v.getId() == R.id.tv_floor_delete)
//            {
//                deleteNow(comment, position);
//            }
            // 回复
            if (v.getId() == R.id.layout_club_detail_reply)
            {
//                replyNow(comment);
                Intent intent = new Intent(mContext, LSClubTopicNewReply.class);
                intent.putExtra("replyedName", comment.nickname);
                intent.putExtra("replyedcontent", comment.content);
                intent.putExtra("replyedfloor", ""+comment.floor);
                intent.putExtra("replyedId", comment.id);
                intent.putExtra("clubId", "" + clubId);
                intent.putExtra("topicId", "" + topicId);
//                startActivityForResult(intent, 999);
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.activity_open_down_up, 0);
            }
//            // 点赞
//            else if (v.getId() == R.id.layout_club_detail_like)
//            {
//                likeNow(comment, img, tvreply);
//            }

            else if (v.getId() == R.id.roundedImageView1)
            {
                Intent intent = new Intent(mContext, LSUserHomeActivity.class);
                intent.putExtra("userID", comment.userId);
                mContext.startActivity(intent);
            }
//            else if (v.getId() == R.id.equiPanel) {
//                Intent intent = new Intent(mContext,LSEquipInfoActivity.class);
//                intent.putExtra("id", comment.zhuangbei_id);
//                mContext.startActivity(intent);
//            }
        }

    }


    public LSClubNewTopicReplyAdapter(Context c, List listItem) {
        super(c, listItem);

        drawable = LSBaseActivity.activity.getResources().getDrawable(
                R.drawable.club_tier_master);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

    }

    public void setId ( int topicId, int clubId )
    {
        this.topicId = topicId;
        this.clubId = clubId;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
    
        ViewHolder holder = null;
        if (view == null)
        {
            view = View.inflate(mContext, R.layout.club_topic_comment_item_reply,
                    null);
            holder = new ViewHolder();
            holder.nameView = (TextView) view
                    .findViewById(R.id.nameView);

            holder.iv_moderator = view.findViewById(R.id.iv_moderator);

            //		4.1.1
            holder.tv_user_tag3 = (TextView) view.findViewById(R.id.tv_user_tag3);

            holder.tv_user_tag4 = (TextView) view.findViewById(R.id.tv_user_tag4);

            // 去掉楼主
            holder.nameView
                    .setCompoundDrawablesRelative(null, null, null, null);
            holder.imageView = (ImageView) view
                    .findViewById(R.id.roundedImageView1);
            holder.contentImageView = (ImageView) view
                    .findViewById(R.id.contentImageView);
            holder.dateView = (TextView) view
                    .findViewById(R.id.dateView);
            holder.contentView = (TextView) view
                    .findViewById(R.id.contentView);
            holder.vipStar = view.findViewById(R.id.vipStar);
            // 2.3
            holder.layout_club_detail_like = (LinearLayout) view
                    .findViewById(R.id.layout_club_detail_like);
            holder.layout_club_detail_reply = (LinearLayout) view
                    .findViewById(R.id.layout_club_detail_reply);
            holder.tv_like = (TextView) view.findViewById(R.id.tv_like);
            holder.tv_floor_delete = (TextView) view
                    .findViewById(R.id.tv_floor_delete);
            holder.tv_floor = (TextView) view
                    .findViewById(R.id.tv_floor);
            holder.reply_view = view.findViewById(R.id.reply_view);
            holder.tv_reply_body = (TextView) view
                    .findViewById(R.id.tv_reply_body);
            holder.tv_reply_floor = (TextView) view
                    .findViewById(R.id.tv_reply_floor);
            holder.tv_reply_content = (TextView) view
                    .findViewById(R.id.tv_reply_content);
            holder.iv_like = (ImageView) view.findViewById(R.id.iv_like);

            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);

            holder.layout_tag = (LinearLayout) view.findViewById(R.id.layout_tag);


            view.setTag(holder);

            view.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT,
                    ListView.LayoutParams.WRAP_CONTENT));


        } else
        {
            holder = (ViewHolder) view.getTag();
        }

//        隐藏下面的标签, 赞， vip， 删除
        holder.layout_tag.setVisibility(View.INVISIBLE);
        holder.layout_club_detail_like.setVisibility(View.GONE);
        holder.vipStar.setVisibility(View.GONE);
        holder.tv_floor_delete.setVisibility(View.GONE);

        // LSClubTopicComment item = data.get(i);
        final ClubTopicNewReplyList.TopicsreplylistEntity item = (ClubTopicNewReplyList
                .TopicsreplylistEntity) getItem(i);
        if (item == null) {
            return view;
        }
//      达人标签
        holder.tv_user_tag3.setVisibility(View.GONE);
        holder.tv_user_tag4.setVisibility(View.GONE);

        if ( item.usercatelist != null && item.usercatelist.size() != 0 )
        {
            holder.tv_user_tag3.setVisibility(View.VISIBLE);
            holder.tv_user_tag3.setText(Common.getTagString(item.usercatelist.get(0).title));
            if ( item.usercatelist.size() > 1 )
            {
                holder.tv_user_tag4.setVisibility(View.VISIBLE);
                holder.tv_user_tag4.setText(Common.getTagString(item.usercatelist.get(1).title));
            }
        }

        if ( item.moderator == 1 )
        {
            holder.iv_moderator.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_moderator.setVisibility(View.GONE);
        }

        holder.nameView.setText(item.nickname);
        holder.dateView.setText(item.createtime);
        //===emotion=====
        holder.contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)
                mContext, item.content));

        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance()
                    .displayImage(item.headicon, holder.imageView, ImageUtil.getclub_topic_headImageOptions());
        }

        CommentOnClickListener l = new CommentOnClickListener(item, i);
//		holder.layout_club_detail_like.setOnClickListener(l);
//		l.setReply(holder.tv_like);
//		l.setImageView(holder.iv_like);
        holder.layout_club_detail_reply.setOnClickListener(l);
        holder.tv_floor_delete.setOnClickListener(l);
        holder.imageView.setOnClickListener(l);

        if ( !TextUtils.isEmpty(item.images) )
        {

            holder.contentImageView.setVisibility(View.VISIBLE);
            holder.contentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (lsClubTopicCommentListener != null) {
                        lsClubTopicCommentListener.onClickImage(item.images);
                    }
                }
            });
            holder.iv_load.setVisibility(View.VISIBLE);
            getWidth(holder.contentImageView, holder.iv_load, item);
        } else
        {
            holder.contentImageView.setVisibility(View.GONE);
//			holder.contentImageView.setImageBitmap(null);
            holder.iv_load.setVisibility(View.GONE);
        }
        if ("0".equals(item.replyId))
        {
            holder.reply_view.setVisibility(View.GONE);
        } else
        {
            holder.reply_view.setVisibility(View.VISIBLE);
            // 回复内容＝＝＝＝＝
            holder.tv_reply_body.setText("回复@ " + item.replyNickname);
            holder.tv_reply_floor.setText(item.replyFloor + "楼");
            //====emotion====
            holder.tv_reply_content.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.replyContent));
        }
        // 1为楼主， 其他不是
        if ( 1 == item.isFloor )
        {
            holder.nameView.setCompoundDrawables(null, null, null, null);
        } else
        {
            holder.nameView.setCompoundDrawables(null, null, drawable, null);
        }

        holder.tv_floor.setText(item.floor + "楼");
//        if (Common.replyDelete(item.is_jion, item.user_id))
//        {
//            holder.tv_floor_delete.setVisibility(View.VISIBLE);
//        } else
//        {
//            holder.tv_floor_delete.setVisibility(View.GONE);
//        }

//        if ("1".equals(item.LikeStatus))
//        {
//            holder.iv_like.setImageResource(R.drawable.like_button_press);
//            holder.tv_like.setTextColor(main.getResources().getColor(
//                    R.color.color_like_press_red));
//        } else
//        {
//            holder.iv_like.setImageResource(R.drawable.like_button);
//            holder.tv_like.setTextColor(main.getResources().getColor(
//                    R.color.pull_text_color));
//        }

//        holder.tv_like.setText(Common.getLikeNum(item.likeNum));

        return view;
    }

    static class ViewHolder
    {
        View iv_moderator;
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

        //	4.1.1
        private TextView tv_user_tag3, tv_user_tag4;

    }

    int ImageWidth = Common.WIDTH;// - Common.dip2px(10);
    private AnimationDrawable animationDrawable;

    private void getWidth(final ImageView v,final ImageView load, final ClubTopicNewReplyList.TopicsreplylistEntity item)
    {
        animationDrawable = (AnimationDrawable) load.getDrawable();
        if ( !animationDrawable.isRunning() )
            animationDrawable.start();

        if (ImageWidth == -1)
        {
            ViewTreeObserver vto = v.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout()
                {
                    ImageWidth = v.getWidth();
                    ViewTreeObserver obs = v.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);

                    ImageLoader.getInstance().displayImage(
                            item.images, v, ImageUtil.getclub_topic_imageOptions(),
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
                    item.images, v, ImageUtil.getclub_topic_imageOptions(),
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
