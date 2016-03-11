package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.TopicNewListMainModel;
import com.lis99.mobile.club.model.TopicNewListMainModelEquip;
import com.lis99.mobile.club.model.TopicNewListMainModelTitle;
import com.lis99.mobile.club.widget.LSClubTopicHeadLike;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/3/8.
 */
public class ClubNewTopicListItem extends MyBaseAdapter {

    private final int TITLE = 0;

    private final int INFO = 1;

    private final int EQUIP = 2;

    private final int REPLY = 3;

    private final int count = 4;

    private LSClubTopicHeadLike like;

    private Drawable drawable;


    public ClubNewTopicListItem(Context c, List listItem) {
        super(c, listItem);

        drawable = LSBaseActivity.activity.getResources().getDrawable(
                R.drawable.club_tier_master);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);

        switch (type) {
            case TITLE:
                return getTopicTitle(i, view, viewGroup);
            case INFO:
                return getTopicInfo(i, view, viewGroup);
            case EQUIP:
                return getEquip(i, view, viewGroup);
            case REPLY:
                return getReply(i, view, viewGroup);
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        int num = 0;
        Object o = getItem(position);
        if (o instanceof TopicNewListMainModelTitle) {
            num = TITLE;
        } else if (o instanceof TopicNewListMainModel.TopicsdetaillistEntity) {
            num = INFO;
        } else if (o instanceof TopicNewListMainModelEquip) {
            num = EQUIP;
        } else if (o instanceof TopicNewListMainModel.TopicsreplylistEntity) {
            num = REPLY;
        }

        return num;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    //      标题
    private View getTopicTitle(int i, View view, ViewGroup viewGroup) {
        ViewHolderTitle holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.adapter_club_new_topic_list_title, null);
            holder = new ViewHolderTitle(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderTitle) view.getTag();
        }

        TopicNewListMainModelTitle item = (TopicNewListMainModelTitle) getItem(i);

        if (item == null) return view;

        holder.titleView.setText(item.title);
        holder.nameView.setText(item.nickname);
        holder.dateView.setText(item.createtime);
        holder.lookNum.setText(""+item.browsenums);
        if (!TextUtils.isEmpty(item.headicon))
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1,
                    ImageUtil.getclub_topic_headImageOptions());

        holder.tvUserTag3.setVisibility(View.GONE);
        holder.tvUserTag4.setVisibility(View.GONE);
        //版主
        holder.ivModerator.setVisibility(View.GONE);
        //说以后不要了， 先隐藏吧， 万一呢
        holder.vipStar.setVisibility(View.GONE);
//        版主
        if (item.moderator == 1) {
            holder.ivModerator.setVisibility(View.VISIBLE);
        } else {
            holder.ivModerator.setVisibility(View.GONE);
        }

//      关注
        if (item.attenStatus == 1) {
            holder.btnAttention.setVisibility(View.GONE);
        } else {
            holder.btnAttention.setVisibility(View.VISIBLE);
        }


        if (item.usercatelist != null && item.usercatelist.size() != 0) {
            if (item.usercatelist.size() > 0) {
                holder.tvUserTag3.setVisibility(View.VISIBLE);
                holder.tvUserTag3.setText(item.usercatelist.get(0).title);
            }
            if (item.usercatelist.size() > 1) {
                holder.tvUserTag4.setVisibility(View.VISIBLE);
                holder.tvUserTag4.setText(item.usercatelist.get(1).title);
            }
        }

        holder.ivTagFloor.setTag("1楼");


        return view;
    }

    //      图文
    private View getTopicInfo(int i, View view, ViewGroup viewGroup) {
//        adapter_club_new_topic_list_item
        ViewHolderInfo holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.adapter_club_new_topic_list_item, null);
            holder = new ViewHolderInfo(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderInfo) view.getTag();
        }

        TopicNewListMainModel.TopicsdetaillistEntity item = (TopicNewListMainModel
                .TopicsdetaillistEntity) getItem(i);

        if (item == null) return view;

        holder.layoutIv.setVisibility(View.GONE);
        holder.tvDescrible.setVisibility(View.GONE);
        holder.tvInfo.setVisibility(View.GONE);
        holder.tvTitle.setVisibility(View.GONE);
        holder.vedio.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.content)) {
            holder.tvInfo.setVisibility(View.VISIBLE);
//            holder.tvInfo.setText(item.content);
            holder.tvInfo.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.content));
        }

        if (!TextUtils.isEmpty(item.videoid) && !TextUtils.isEmpty(item.videoimg)) {
            holder.layoutIv.setVisibility(View.VISIBLE);
            holder.vedio.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.videoimg, holder.contentImageView,
                    ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder
                            .contentImageView));
        } else {
            if (!TextUtils.isEmpty(item.images)) {
                holder.layoutIv.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.images, holder.contentImageView,
                        ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder
                                .contentImageView ));
            }
        }

        holder.layoutIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转视频
            }
        });

        return view;
    }

    //  推荐装备
    private View getEquip(int i, View view, ViewGroup viewGroup) {
        ViewHolderEquip holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.adapter_club_new_topic_list_equip, null);
            holder = new ViewHolderEquip(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderEquip) view.getTag();
        }

        final TopicNewListMainModelEquip item = (TopicNewListMainModelEquip) getItem(i);

        if (item == null) return view;

        if (item.zhuangbei_id != 0) {
            holder.equiPanel.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.zhuangbei_image)) {
                ImageLoader.getInstance().displayImage(item.zhuangbei_image, holder.equiImageView);
            }
            holder.equiNameView.setText(item.zhuangbei_title);
            holder.equiPriceView.setText("市场价："+item.zhuangbei_price);
            holder.equiRatingBar.setRating(item.zhuangbei_star);

            holder.equiPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,LSEquipInfoActivity.class);
                    intent.putExtra("id", item.zhuangbei_id);
                    mContext.startActivity(intent);
                }
            });

        }
        else
        {
            holder.equiPanel.setVisibility(View.GONE);
        }

        if ( item.taglist != null && item.taglist.size() != 0 )
        {
            holder.layoutTag.setVisibility(View.VISIBLE);
            holder.tvTag1.setVisibility(View.INVISIBLE);
            holder.tvTag2.setVisibility(View.INVISIBLE);
            holder.tvTag3.setVisibility(View.INVISIBLE);
            int num = item.taglist.size();
            if ( num >0 )
            {
                holder.tvTag1.setText("#"+item.taglist.get(0).tagname);
                holder.tvTag1.setVisibility(View.VISIBLE);
                holder.tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", item.taglist.get(0).tagid);
                        mContext.startActivity(intent);
                    }
                });
            }
            if ( num > 1)
            {
                holder.tvTag2.setText("#"+item.taglist.get(1).tagname);
                holder.tvTag2.setVisibility(View.VISIBLE);
                holder.tvTag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", item.taglist.get(1).tagid);
                        mContext.startActivity(intent);
                    }
                });
            }
            if ( num > 2 )
            {
                holder.tvTag3.setText("#"+item.taglist.get(2).tagname);
                holder.tvTag3.setVisibility(View.VISIBLE);
                holder.tvTag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", item.taglist.get(2).tagid);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        else {
            holder.layoutTag.setVisibility(View.INVISIBLE);
        }

        if ( like == null )
        {
            like = new LSClubTopicHeadLike(mContext);
            like.InitView(view);
            like.setInfo(item);
        }

        holder.tvClubName.setText("精彩评论（"+ item.topictot +"）");

        return view;
    }

    //      回复
    private View getReply(int i, View view, ViewGroup viewGroup) {
//        adapter_club_new_topic_list_reply
        ViewHolderReply holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.adapter_club_new_topic_list_reply, null);
            holder = new ViewHolderReply(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderReply) view.getTag();
        }

        TopicNewListMainModel.TopicsreplylistEntity item = (TopicNewListMainModel
                .TopicsreplylistEntity) getItem(i);

        if ( item == null ) return view;

        holder.layout_more.setVisibility(View.GONE);

        if ( i == getCount() - 1 )
        {
            holder.layout_more.setVisibility(View.VISIBLE);
            holder.layout_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    跳转到评论列表

                }
            });
        }

        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1,
                    ImageUtil.getclub_topic_headImageOptions());
        }

        holder.nameView.setText(item.nickname);

        if ( item.is_floor == 0 )
        {
            holder.nameView.setCompoundDrawables(null, null, null, null);
        }
        else
        {
            holder.nameView.setCompoundDrawables(null, null, drawable, null);
        }

        holder.dateView.setText(item.createtime);
        holder.tvFloor.setText(item.floor);
        holder.contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.content));
        holder.vipStar.setVisibility(View.GONE);

        if ( !"0".equals(item.replyId))
        {
            holder.replyView.setVisibility(View.VISIBLE);
            holder.tvReplyBody.setText("回复@ " + item.replyNickname);
            holder.tvReplyContent.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.replyContent));
            holder.tvReplyFloor.setText(item.replyFloor+"楼");
        }
        else
        {
            holder.replyView.setVisibility(View.GONE);
        }






        return view;
    }


    //  回复
    protected class ViewHolderReply {
        private View include1;
        private View replyView;
        private TextView contentView;

        private RoundedImageView roundedImageView1;
        private RelativeLayout layout;
        private RelativeLayout layoutMain;
        private TextView nameView;
        private ImageView ivModerator;
        private TextView tvUserTag3;
        private TextView tvUserTag4;
        private TextView dateView;
        private TextView tvFloor;
        private TextView tvFloorDelete;
        private ImageView vipStar;

        private TextView tvReplyBody;
        private TextView tvReplyFloor;
        private TextView tvReplyContent;
        private View layout_more;

        public ViewHolderReply(View view) {
            include1 = view.findViewById(R.id.include1);
            replyView = view.findViewById(R.id.reply_view);
            contentView = (TextView) view.findViewById(R.id.contentView);

            roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            layout = (RelativeLayout) view.findViewById(R.id.layout);
            layoutMain = (RelativeLayout) view.findViewById(R.id.layout_main);
            nameView = (TextView) view.findViewById(R.id.nameView);
            ivModerator = (ImageView) view.findViewById(R.id.iv_moderator);
            tvUserTag3 = (TextView) view.findViewById(R.id.tv_user_tag3);
            tvUserTag4 = (TextView) view.findViewById(R.id.tv_user_tag4);
            dateView = (TextView) view.findViewById(R.id.dateView);
            tvFloor = (TextView) view.findViewById(R.id.tv_floor);
            tvFloorDelete = (TextView) view.findViewById(R.id.tv_floor_delete);
            vipStar = (ImageView) view.findViewById(R.id.vipStar);

            tvReplyBody = (TextView) view.findViewById(R.id.tv_reply_body);
            tvReplyFloor = (TextView) view.findViewById(R.id.tv_reply_floor);
            tvReplyContent = (TextView) view.findViewById(R.id.tv_reply_content);

            layout_more = view.findViewById(R.id.layout_more);


        }
    }


    //      装备
    protected class ViewHolderEquip {
        private RelativeLayout equiPanel;
        private RoundedImageView equiImageView;
        private TextView equiPriceView;
        private RatingBar equiRatingBar;
        private TextView equiNameView;
        private TextView tvClubName;
        private LinearLayout layoutTag;
        private TextView tvTag1;
        private TextView tvTag2;
        private TextView tvTag3;

        private LSClubTopicHeadLike like;




        public ViewHolderEquip(View view) {
            equiPanel = (RelativeLayout) view.findViewById(R.id.equiPanel);
            equiImageView = (RoundedImageView) view.findViewById(R.id.equiImageView);
            equiPriceView = (TextView) view.findViewById(R.id.equiPriceView);
            equiRatingBar = (RatingBar) view.findViewById(R.id.equiRatingBar);
            equiNameView = (TextView) view.findViewById(R.id.equiNameView);
            tvClubName = (TextView) view.findViewById(R.id.tv_club_name);

            layoutTag = (LinearLayout) view.findViewById(R.id.layout_tag);
            tvTag1 = (TextView) view.findViewById(R.id.tv_tag1);
            tvTag2 = (TextView) view.findViewById(R.id.tv_tag2);
            tvTag3 = (TextView) view.findViewById(R.id.tv_tag3);

            like = new LSClubTopicHeadLike(mContext);
            like.InitView(view);

        }
    }


    //  内容
    protected class ViewHolderInfo {
        private TextView tvTitle;
        private TextView tvInfo;
        private RelativeLayout layoutIv;
        private ImageView contentImageView;
        private ImageView ivLoad;
        private TextView tvDescrible;
        private ImageView vedio;

        public ViewHolderInfo(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvInfo = (TextView) view.findViewById(R.id.tv_info);
            layoutIv = (RelativeLayout) view.findViewById(R.id.layout_iv);
            contentImageView = (ImageView) view.findViewById(R.id.contentImageView);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            tvDescrible = (TextView) view.findViewById(R.id.tv_describle);
            vedio = (ImageView) view.findViewById(R.id.vedio);
        }
    }


    //   标题
    protected class ViewHolderTitle {
        private RoundedImageView roundedImageView1;
        private TextView nameView;
        private ImageView vipStar;
        private Button btnAttention;
        private ImageView ivTagFloor;
        private ImageView ivModerator;
        private TextView tvUserTag3;
        private TextView tvUserTag4;
        private TextView titleView;
        private TextView dateView;
        private TextView lookNum;

        public ViewHolderTitle(View view) {
            roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            nameView = (TextView) view.findViewById(R.id.nameView);
            vipStar = (ImageView) view.findViewById(R.id.vipStar);
            btnAttention = (Button) view.findViewById(R.id.btn_attention);
            ivTagFloor = (ImageView) view.findViewById(R.id.iv_tag_floor);
            ivModerator = (ImageView) view.findViewById(R.id.iv_moderator);
            tvUserTag3 = (TextView) view.findViewById(R.id.tv_user_tag3);
            tvUserTag4 = (TextView) view.findViewById(R.id.tv_user_tag4);
            titleView = (TextView) view.findViewById(R.id.titleView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            lookNum = (TextView) view.findViewById(R.id.lookNum);
        }
    }

}
