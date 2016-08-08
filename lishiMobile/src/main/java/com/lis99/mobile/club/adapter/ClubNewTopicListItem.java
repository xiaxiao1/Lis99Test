package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSRecommendActivity;
import com.lis99.mobile.club.model.IsToRecommendActive;
import com.lis99.mobile.club.model.TopicNewListMainModel;
import com.lis99.mobile.club.model.TopicNewListMainModelEquip;
import com.lis99.mobile.club.model.TopicNewListMainModelTitle;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMainReply;
import com.lis99.mobile.club.topicstrimg.LSTopicStringImageActivity;
import com.lis99.mobile.club.widget.LSClubTopicHeadLike;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.NativeEntityUtil;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.lis99.mobile.util.letv.MovieActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yy on 16/3/8.
 */
public class ClubNewTopicListItem extends MyBaseAdapter {


    static HashMap<String, Integer> tagBackgrounds = new HashMap<>();

    static {
//        tagBackgrounds.put("潜水员", R.drawable.label_bg_qianshui);
//        tagBackgrounds.put("攀冰狂人", R.drawable.label_bg_panbing);
//        tagBackgrounds.put("岩壁舞者", R.drawable.label_bg_yanbi);
//        tagBackgrounds.put("装备玩家", R.drawable.label_bg_zhuangbei);
//        tagBackgrounds.put("光影大师", R.drawable.label_bg_guangying);
//        tagBackgrounds.put("徒步行者", R.drawable.label_bg_tubu);
//        tagBackgrounds.put("企业官方帐号", R.drawable.label_bg_qiye);
//        tagBackgrounds.put("潜白色瘾君子", R.drawable.label_bg_baise);
//        tagBackgrounds.put("山友", R.drawable.label_bg_shanyou);
//        tagBackgrounds.put("老司机", R.drawable.label_bg_laosiji);
        tagBackgrounds = NativeEntityUtil.getInstance().getCommunityStarTags();
    }

    private final int TITLE = 0;

    private final int INFO = 1;

    private final int EQUIP = 2;

    private final int REPLY = 3;

    private final int NO_REPLY = 4;

    //增加底部推荐活动入口
    private final int RECOMMEND = 5;
//  总数
    private final int count = 6;



    private LSClubTopicHeadLike like;

    private Drawable drawable;

    private HandlerList likeCall;

    private int topicId = -1, clubId = -1;

    private String title;

    public LSClubNewTopicListMain getMain() {
        return main;
    }

    public void setMain(LSClubNewTopicListMain main) {
        this.main = main;
    }

    private LSClubNewTopicListMain main;


    public ClubNewTopicListItem(Activity c, List listItem) {
        super(c, listItem);

        drawable = LSBaseActivity.activity.getResources().getDrawable(
                R.drawable.club_tier_master);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

    }

    public void setLikeCall ( HandlerList likeCall )
    {
        this.likeCall = likeCall;
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
            case NO_REPLY:
                return getNoReply (i, view, viewGroup);
            case RECOMMEND:
                Log.i("xx"," return getRecommend(i,view,viewGroup);");
                return getRecommend(i,view,viewGroup);
        }


        return view;
    }


    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        int num = 0;
        Object o = getItem(position);
        if (o instanceof TopicNewListMainModelTitle && position == 0 ) {
            num = TITLE;
        } else if (o instanceof TopicNewListMainModel.TopicsdetaillistEntity) {
            num = INFO;
        } else if (o instanceof TopicNewListMainModelEquip) {
            num = EQUIP;
        } else if (o instanceof TopicNewListMainModel.TopicsreplylistEntity) {
            num = REPLY;
        }
        else if ( o instanceof String )
        {
            num = NO_REPLY;
        }
        //判断是否是推荐活动item，等待接口匹配
        else if(o instanceof IsToRecommendActive){
            num = RECOMMEND;
            Log.i("xx","o instanceof IsToRecommendActive");
        }

        return num;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    private View getNoReply(int i, View view, ViewGroup viewGroup) {

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.adapter_club_new_topic_no_reply, null);
        }

        return view;
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

        final TopicNewListMainModelTitle item = (TopicNewListMainModelTitle) getItem(i);

        if (item == null) return view;

        topicId = Common.string2int(item.topicsId);
        clubId = item.clubId;
        title = item.title;

        holder.titleView.setText(item.title);
        holder.nameView.setText(item.nickname);
        holder.dateView.setText(item.createtime);
        if (!TextUtils.isEmpty(item.headicon))
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1,
                    ImageUtil.getclub_topic_headImageOptions());

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

        holder.tvUserTag3.setVisibility(View.GONE);
        holder.tvUserTag4.setVisibility(View.GONE);
        if (item.usercatelist != null && item.usercatelist.size() != 0) {
            if (item.usercatelist.size() > 0) {
                holder.tvUserTag3.setVisibility(View.VISIBLE);
                String tag = item.usercatelist.get(0).title;
                holder.tvUserTag3.setText(tag);
                if (tagBackgrounds.containsKey(tag)) {
                    holder.tvUserTag3.setBackgroundResource(tagBackgrounds.get(tag));
                } else {
                    holder.tvUserTag3.setBackgroundResource(R.drawable.label_bg_default);
                }
            }
            if (item.usercatelist.size() > 1) {
                holder.tvUserTag4.setVisibility(View.VISIBLE);
                String tag = item.usercatelist.get(1).title;
                holder.tvUserTag4.setText(tag);
                if (tagBackgrounds.containsKey(tag)) {
                    holder.tvUserTag4.setBackgroundResource(tagBackgrounds.get(tag));
                } else {
                    holder.tvUserTag3.setBackgroundResource(R.drawable.label_bg_default);
                }
            }
        }

        if (item.moderator == 1) {
            holder.tvUserTag4.setVisibility(View.GONE);
        }


        holder.ivTagFloor.setTag("1楼");

        holder.tv_club_name.setText("发布于  " + item.clubTitle);

        holder.layout_club_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LSClubDetailActivity.class);
                intent.putExtra("clubID", item.clubId);
                mContext.startActivity(intent);
            }
        });

        holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+item.userId);
                mContext.startActivity(intent);
            }
        });


        if (item.is_jingpin == 1) {
            holder.specialPanel.setVisibility(View.VISIBLE);
            String special = item.is_jingpin_con;
            if (special.length() > 4) {
                special = special.substring(0, 4);
            }
            holder.specialTitle.setText(special);
        } else {
            holder.specialPanel.setVisibility(View.GONE);
        }

        if (item.areaid == 0) {
            holder.destinationPanel.setVisibility(View.GONE);
        } else {
            holder.destinationPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Intent intent = new Intent(mContext, MyActivityWebView.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("URL", item.areaurl);
//                    bundle.putString("TITLE", item.areaname);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);

                    ActivityUtil.goDestinationInfo(item.tag_id, item.desti_id);


                }
            });
            holder.destinationPanel.setVisibility(View.VISIBLE);
            holder.destinationName.setText(item.areaname);
        }

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

        final TopicNewListMainModel.TopicsdetaillistEntity item = (TopicNewListMainModel
                .TopicsdetaillistEntity) getItem(i);

        if (item == null) return view;

        holder.layoutIv.setVisibility(View.GONE);
        holder.tvDescrible.setVisibility(View.GONE);
        holder.tvInfo.setVisibility(View.GONE);
        holder.tvTitle.setVisibility(View.GONE);
        holder.vedio.setVisibility(View.GONE);
        holder.view_transprant.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.content)) {
            holder.tvInfo.setVisibility(View.VISIBLE);
//            holder.tvInfo.setText(item.content);
            holder.tvInfo.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.content));
        }

        if (!TextUtils.isEmpty(item.videoid) && !TextUtils.isEmpty(item.videoimg)) {
            holder.layoutIv.setVisibility(View.VISIBLE);
            holder.vedio.setVisibility(View.VISIBLE);
            holder.view_transprant.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(item.videoimg, holder.contentImageView,
                    ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder
                            .ivLoad, holder
                            .contentImageView));
        } else {
            if (!TextUtils.isEmpty(item.images)) {
                holder.layoutIv.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.images, holder.contentImageView,
                        ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.ivLoad, holder
                                .contentImageView ));
            }
        }

        holder.layoutIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !TextUtils.isEmpty(item.videoid) && !"0".equals(item.videoid))
                {
//                跳转视频
                    Intent intent = new Intent(mContext, MovieActivity.class);
                    intent.putExtra("VUID", item.videoid);
                    mContext.startActivity(intent);
                }
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

        String userId = DataManager.getInstance().getUser().getUser_id();

        if ( !TextUtils.isEmpty(userId) && userId.equals(""+item.userId) )
        {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, LSTopicStringImageActivity.class);
                    intent.putExtra("clubID", item.clubId);//clubID);
                    intent.putExtra("topicId", Common.string2int(item.topicsId));
                    intent.putExtra("clubName", item.clubTitle);//clubHead.title);
                    intent.putExtra("ADD", true);
                    intent.putExtra("TITLE", item.title);
//                    mContext.startActivity(intent);
                    main.startActivityForResult(intent, 999);

                }
            });
        }
        else
        {
            holder.btn_add.setVisibility(View.GONE);
        }

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
            holder.tagTitleView.setVisibility(View.VISIBLE);
            holder.layoutTag.setVisibility(View.VISIBLE);
            holder.tvTag1.setVisibility(View.INVISIBLE);
            holder.tvTag2.setVisibility(View.INVISIBLE);
            holder.tvTag3.setVisibility(View.INVISIBLE);
            int num = item.taglist.size();
            if ( num >0 )
            {
                holder.tvTag1.setText(item.taglist.get(0).tagname);
                holder.tvTag1.setVisibility(View.VISIBLE);
                holder.tvTag1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        SpecialInfoActivity
//                        Intent intent = new Intent(mContext, SpecialInfoActivity.class);
////                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
//                        intent.putExtra("tagid", item.taglist.get(0).tagid);
//                        mContext.startActivity(intent);
                        ActivityUtil.goSpecialInfoActivity(mContext, item.taglist.get(0).tagid);
                    }
                });
            }
            if ( num > 1)
            {
                holder.tvTag2.setText(item.taglist.get(1).tagname);
                holder.tvTag2.setVisibility(View.VISIBLE);
                holder.tvTag2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
//                        Intent intent = new Intent(mContext, SpecialInfoActivity.class);
//                        intent.putExtra("tagid", item.taglist.get(1).tagid);
//                        mContext.startActivity(intent);
                        ActivityUtil.goSpecialInfoActivity(mContext, item.taglist.get(1).tagid);
                    }
                });
            }
            if ( num > 2 )
            {
                holder.tvTag3.setText(item.taglist.get(2).tagname);
                holder.tvTag3.setVisibility(View.VISIBLE);
                holder.tvTag3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
//                        Intent intent = new Intent(mContext, SpecialInfoActivity.class);
//                        intent.putExtra("tagid", item.taglist.get(2).tagid);
//                        mContext.startActivity(intent);
                        ActivityUtil.goSpecialInfoActivity(mContext, item.taglist.get(2).tagid);
                    }
                });
            }
        }
        else {
            holder.tagTitleView.setVisibility(View.GONE);
            holder.layoutTag.setVisibility(View.GONE);
        }



        if ( like == null )
        {
            like = new LSClubTopicHeadLike(mContext);
            like.setLikeCall(likeCall);
            //新版话题帖
            like.setLikeUrl(C.CLUB_TOPIC_LIKE_NEW);
            like.InitView(view);
            like.setInfo(item);
        }

        if (item.is_show_user == 1) {
            holder.hostInfoPanel.setVisibility(View.VISIBLE);
            holder.hostInfoPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.goUserHomeActivit((Activity)mContext, ""+item.userId);
                }
            });
            if (!TextUtils.isEmpty(item.headicon))
                ImageLoader.getInstance().displayImage(item.headicon, holder.hostInfoHeaderView,
                        ImageUtil.getclub_topic_headImageOptions());
            holder.hostInfoDescView.setText(item.note);
            holder.hostInfoDataView.setText("粉丝数" + item.totfans + "  |  帖子数" + item.tottopics);

            holder.hostInfoNameView.setText(item.nickname);

            holder.hostInfoModerator.setVisibility(View.GONE);
            if (item.moderator == 1) {
                holder.hostInfoModerator.setVisibility(View.VISIBLE);
            } else {
                holder.hostInfoModerator.setVisibility(View.GONE);
            }


            holder.hostInfoTag1.setVisibility(View.GONE);
            holder.hostInfoTag2.setVisibility(View.GONE);
            holder.hostInfoTag3.setVisibility(View.GONE);
            if (item.usercatelist != null && item.usercatelist.size() != 0) {
                if (item.usercatelist.size() > 0) {
                    holder.hostInfoTag1.setVisibility(View.VISIBLE);
                    String tag = item.usercatelist.get(0).title;
                    holder.hostInfoTag1.setText(tag);
                    if (tagBackgrounds.containsKey(tag)) {
                        holder.hostInfoTag1.setBackgroundResource(tagBackgrounds.get(tag));
                    } else {
                        holder.hostInfoTag1.setBackgroundResource(R.drawable.label_bg_default);
                    }
                }
                if (item.usercatelist.size() > 1) {
                    holder.hostInfoTag2.setVisibility(View.VISIBLE);
                    String tag = item.usercatelist.get(1).title;
                    holder.hostInfoTag2.setText(tag);
                    if (tagBackgrounds.containsKey(tag)) {
                        holder.hostInfoTag2.setBackgroundResource(tagBackgrounds.get(tag));
                    } else {
                        holder.hostInfoTag2.setBackgroundResource(R.drawable.label_bg_default);
                    }
                }

                if (item.moderator == 0 && item.usercatelist.size() > 2) {
                    holder.hostInfoTag3.setVisibility(View.VISIBLE);
                    String tag = item.usercatelist.get(2).title;
                    holder.hostInfoTag3.setText(tag);
                    if (tagBackgrounds.containsKey(tag)) {
                        holder.hostInfoTag3.setBackgroundResource(tagBackgrounds.get(tag));
                    } else {
                        holder.hostInfoTag3.setBackgroundResource(R.drawable.label_bg_default);
                    }
                }
            }
        } else {
            holder.hostInfoPanel.setVisibility(View.GONE);
        }

        if (item.topictot > 0) {
            holder.tvClubName.setVisibility(View.VISIBLE);
            holder.tvClubName.setText("精彩评论（"+ item.topictot +"）");
        } else {
            holder.tvClubName.setVisibility(View.GONE);
        }

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

        final TopicNewListMainModel.TopicsreplylistEntity item = (TopicNewListMainModel
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
                    Intent intent = new Intent(mContext, LSClubNewTopicListMainReply.class);
                    intent.putExtra("TITLE", title);
                    intent.putExtra("TOPICID", topicId);
                    intent.putExtra("CLUBID", clubId);
                    mContext.startActivity(intent);
                }
            });
        }

        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1,
                    ImageUtil.getclub_topic_headImageOptions());
        }

        holder.roundedImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LSUserHomeActivity.class);
                intent.putExtra("userID", item.userId);
                mContext.startActivity(intent);
            }
        });

        holder.nameView.setText(item.nickname);

        if ( item.is_floor == 0 )
        {
            holder.ivFloor.setVisibility(View.GONE);
        }
        else
        {
            holder.ivFloor.setVisibility(View.VISIBLE);
        }

        holder.dateView.setText(item.createtime);
        holder.tvFloor.setText(item.floor+"楼");
        holder.contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, item.content));
        holder.vipStar.setVisibility(View.GONE);

        if ( !TextUtils.isEmpty(item.replyId) && !"0".equals(item.replyId))
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
//          版主
        if ( item.moderator == 1 )
        {
            holder.ivModerator.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivModerator.setVisibility(View.GONE);
        }
        holder.tvUserTag3.setVisibility(View.GONE);
        holder.tvUserTag4.setVisibility(View.GONE);
//      达人标签
        if (item.usercatelist != null && item.usercatelist.size() != 0) {
            if (item.usercatelist.size() > 0) {
                holder.tvUserTag3.setVisibility(View.VISIBLE);
                String tag = item.usercatelist.get(0).title;
                holder.tvUserTag3.setText(tag);
                if (tagBackgrounds.containsKey(tag)) {
                    holder.tvUserTag3.setBackgroundResource(tagBackgrounds.get(tag));
                } else {
                    holder.tvUserTag3.setBackgroundResource(R.drawable.label_bg_default);
                }

            }
            if (item.usercatelist.size() > 1) {
                holder.tvUserTag4.setVisibility(View.VISIBLE);
                String tag = item.usercatelist.get(1).title;
                holder.tvUserTag4.setText(tag);
                if (tagBackgrounds.containsKey(tag)) {
                    holder.tvUserTag4.setBackgroundResource(tagBackgrounds.get(tag));
                } else {
                    holder.tvUserTag4.setBackgroundResource(R.drawable.label_bg_default);
                }
            }
        }



        return view;
    }


    //     推荐活动，，，，等待匹配接口
    private View getRecommend(int i,View view,ViewGroup viewGroup){
        ViewHolderRecommend holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.club_topic_to_recommended_activity, null);
            holder = new ViewHolderRecommend(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderRecommend) view.getTag();
        }
        Log.i("xx", "获取返回的数据，判断是否显示推荐活动");
        //获取返回的数据，判断是否显示推荐活动，及具体的参数设置
        final IsToRecommendActive item = (IsToRecommendActive) getItem(i);

        //show
        Log.i("xx", item.toString() + "");
        if (item.getIs_tagid()>0) {//表示有要推荐的活动
            holder.clubTopicToRecommendViewLl.setVisibility(View.VISIBLE);
            //用于在textview上显示两行效果
            String reason=item.getReason();
            if (reason != null && !reason.equals("")) {
                holder.clubTopicToRecommendTextTv.setText(reason);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mContext,LSRecommendActivity.class);
                    i.putExtra("is_tagid", item.getIs_tagid());
                    mContext.startActivity(i);
                }
            });
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
        private View ivFloor;
        private View ivModerator;
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
            ivModerator = view.findViewById(R.id.iv_moderator);
            ivFloor = view.findViewById(R.id.iv_tag_floor);
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

        private TextView tagTitleView;

        private View hostInfoPanel;
        private RoundedImageView hostInfoHeaderView;
        private TextView hostInfoNameView;
        private View hostInfoModerator;
        private TextView hostInfoTag1;
        private TextView hostInfoTag2;
        private TextView hostInfoTag3;
        private TextView hostInfoDescView;
        private TextView hostInfoDataView;


        private LSClubTopicHeadLike like;

        private Button btn_add;

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

            btn_add = (Button) view.findViewById(R.id.btn_add);


            tagTitleView = (TextView) view.findViewById(R.id.tagTitleView);

            hostInfoPanel = view.findViewById(R.id.hostInfoPanel);
            hostInfoHeaderView = (RoundedImageView) view.findViewById(R.id.hostInfoHeaderView);
            hostInfoNameView = (TextView) view.findViewById(R.id.hostInfoNameView);
            hostInfoModerator = view.findViewById(R.id.hostInfoModerator);
            hostInfoTag1 = (TextView) view.findViewById(R.id.hostInfoTag1);
            hostInfoTag2 = (TextView) view.findViewById(R.id.hostInfoTag2);
            hostInfoTag3 = (TextView) view.findViewById(R.id.hostInfoTag3);
            hostInfoDescView = (TextView) view.findViewById(R.id.hostInfoDescView);
            hostInfoDataView = (TextView) view.findViewById(R.id.hostInfoDataView);
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
        private View view_transprant;

        public ViewHolderInfo(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvInfo = (TextView) view.findViewById(R.id.tv_info);
            layoutIv = (RelativeLayout) view.findViewById(R.id.layout_iv);
            contentImageView = (ImageView) view.findViewById(R.id.contentImageView);
            ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            tvDescrible = (TextView) view.findViewById(R.id.tv_describle);
            vedio = (ImageView) view.findViewById(R.id.vedio);
            view_transprant = view.findViewById(R.id.view_transprant);
        }
    }


    //   标题
    protected class ViewHolderTitle {
        private RoundedImageView roundedImageView1;
        private TextView nameView;
        private ImageView vipStar;
        private View ivTagFloor;
        private View ivModerator;
        private TextView tvUserTag3;
        private TextView tvUserTag4;
        private TextView titleView;
        private TextView dateView;
        private View layout_club_name;
        private TextView tv_club_name;

        private View destinationPanel;
        private TextView destinationName;

        private View specialPanel;
        private TextView specialTitle;



        public ViewHolderTitle(View view) {
            roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            nameView = (TextView) view.findViewById(R.id.nameView);
            vipStar = (ImageView) view.findViewById(R.id.vipStar);
            ivTagFloor =  view.findViewById(R.id.iv_tag_floor);
            ivModerator = view.findViewById(R.id.iv_moderator);
            tvUserTag3 = (TextView) view.findViewById(R.id.tv_user_tag3);
            tvUserTag4 = (TextView) view.findViewById(R.id.tv_user_tag4);
            titleView = (TextView) view.findViewById(R.id.titleView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            layout_club_name = view.findViewById(R.id.layout_club_name);
            tv_club_name = (TextView) view.findViewById(R.id.tv_club_name);

            destinationPanel = view.findViewById(R.id.destinationPanel);
            destinationName = (TextView) view.findViewById(R.id.destinationName);

            specialPanel = view.findViewById(R.id.specialPanel);
            specialTitle = (TextView) view.findViewById(R.id.specialTitle);
        }
    }



    /**
     * 底部，推荐活动入口项item
     */
    protected class ViewHolderRecommend {
        private LinearLayout clubTopicToRecommendViewLl;
        private ImageView clubTopicToRecommendBgImg;
        private ImageView clubTopicToRecommendLeftImg;
        private TextView clubTopicToRecommendTextTv;
        private ImageView clubTopicToRecommendRightImg;

        public ViewHolderRecommend(View view) {
            clubTopicToRecommendViewLl = (LinearLayout) view.findViewById(R.id.club_topic_to_recommend_view_ll);
            clubTopicToRecommendBgImg = (ImageView) view.findViewById(R.id.club_topic_to_recommend_bg_img);
            clubTopicToRecommendLeftImg = (ImageView) view.findViewById(R.id.club_topic_to_recommend_left_img);
            clubTopicToRecommendTextTv = (TextView) view.findViewById(R.id.club_topic_to_recommend_text_tv);
            clubTopicToRecommendRightImg = (ImageView) view.findViewById(R.id.club_topic_to_recommend_right_img);
        }
    }

}
