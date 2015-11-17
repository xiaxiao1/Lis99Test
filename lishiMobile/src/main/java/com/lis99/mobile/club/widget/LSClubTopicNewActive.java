package com.lis99.mobile.club.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSClubApplyActivity;
import com.lis99.mobile.club.LSClubApplyListActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ClubTopicNewActiveInfo;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


/**
 * Created by yy on 15/10/13.
 */
public class LSClubTopicNewActive extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private LayoutInflater inflater;

    private View v;

    private ImageView iv_head, iv_load;

    private RoundedImageView roundedImageView1;

    private TextView nameView, titleView;

    private Button btn_join;

    private TextView tv_time_over;

    private TextView tv_time, tv_location, tv_rmb;

    private ImageView img_prize, img_location;

    private LSClubTopicHeadLike like;

    private ClubTopicNewActiveInfo model;

    private AnimationDrawable animationDrawable;

    private LinearLayout layout_tag;
    private TextView tv_tag1, tv_tag2, tv_tag3;

    private LSClubTopicNewActivity main;

    private LinearLayout layout_detail;

    private View equiPanel;
    private ImageView equiImageView;
    private TextView equiPriceView;
    private TextView equiNameView;
    private RatingBar equiRatingBar;

//    3.6.3-＝＝＝＝＝
    private TextView dateView, tv_click_reply, tv_active_style;
    private Button btn_attention;
    private View layout_club_name;
    private TextView tv_club_name;

    public void setInstance (LSClubTopicNewActivity main)
    {
        this.main = main;
    }


    public LSClubTopicNewActive(Context context) {
        super(context);

        mContext = context;

        init();

    }

    public LSClubTopicNewActive(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        init();

    }

    private void init ()
    {
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.topic_new_active_info, null);

        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(v, lp);

        like = new LSClubTopicHeadLike(mContext);
        like.InitView(v);

        iv_head = (ImageView) v.findViewById(R.id.iv_head);
        iv_load = (ImageView) v.findViewById(R.id.iv_load);

        roundedImageView1 = (RoundedImageView) v.findViewById(R.id.roundedImageView1);

        roundedImageView1.setOnClickListener(this);

        nameView = (TextView) v.findViewById(R.id.nameView);
        titleView = (TextView) v.findViewById(R.id.titleView);

        tv_active_style = (TextView) v.findViewById(R.id.tv_active_style);
        btn_join = (Button) v.findViewById(R.id.btn_join);

        tv_time_over = (TextView) v.findViewById(R.id.tv_time_over);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_location = (TextView) v.findViewById(R.id.tv_location);
        tv_rmb = (TextView) v.findViewById(R.id.tv_rmb);

        img_prize = (ImageView) v.findViewById(R.id.img_prize);
        img_location = (ImageView) v.findViewById(R.id.img_location);

        btn_join.setOnClickListener(this);

        layout_tag = (LinearLayout) v.findViewById(R.id.layout_tag);
        tv_tag1 = (TextView) v.findViewById(R.id.tv_tag1);
        tv_tag2 = (TextView) v.findViewById(R.id.tv_tag2);
        tv_tag3 = (TextView) v.findViewById(R.id.tv_tag3);


        equiPanel =  v.findViewById(R.id.equiPanel);
        equiImageView = (ImageView)  v.findViewById(R.id.equiImageView);
        equiPriceView = (TextView)  v.findViewById(R.id.equiPriceView);
        equiNameView = (TextView)  v.findViewById(R.id.equiNameView);
        equiRatingBar = (RatingBar)  v.findViewById(R.id.equiRatingBar);

//        3.6.3===
        dateView = (TextView) v.findViewById(R.id.dateView);
        layout_club_name = v.findViewById(R.id.layout_club_name);
        tv_club_name = (TextView) v.findViewById(R.id.tv_club_name);
        layout_club_name.setOnClickListener(this);

        tv_click_reply = (TextView) v.findViewById(R.id.tv_click_reply);
        tv_click_reply.setOnClickListener(this);

        btn_attention = (Button) v.findViewById(R.id.btn_attention);
        btn_attention.setVisibility(GONE);
//        btn_attention.setOnClickListener(this);

    }


    public void setModel ( final ClubTopicNewActiveInfo model )
    {
        this.model = model;
        titleView.setText(model.title);

        nameView.setText(model.nickname);

        if ( "1".equals(model.days))
        {
            tv_time_over.setText("今天" + model.deadline + "截止");
        }
        else if ( "0".equals(model.days))
        {
            tv_time_over.setText("已截止");
        }
        else
        {
            tv_time_over.setText("还有" + model.days + "天可以申请");
        }


//  3.6.3 时间， 关注
        dateView.setText(model.createdate);
        tv_club_name.setText("来自 " + model.club_title);
//        if ( model.attenStatus == 0 )
//        {
//            btn_attention.setVisibility(VISIBLE);
//        }
//        else
//        {
//            btn_attention.setVisibility(GONE);
//        }

        if (model.zhuangbei_id != 0) {
            equiPanel.setVisibility(View.VISIBLE);
            equiRatingBar.setRating(model.zhuangbei_star);
            ImageLoader.getInstance().displayImage(model.zhuangbei_image, equiImageView);
            equiNameView.setText(model.zhuangbei_title);
            equiPriceView.setText("市场价："+model.zhuangbei_price+"元");


            equiPanel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, LSEquipInfoActivity.class);
                    intent.putExtra("id", model.zhuangbei_id);
                    mContext.startActivity(intent);
                }
            });

        } else {
            equiPanel.setVisibility(View.GONE);
        }



        tv_time.setText(model.times);

        tv_location.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)mContext, model.activeDesc));
        tv_rmb.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity) mContext, model.activeAward));

        if (TextUtils.isEmpty(model.activeFileUrl))
        {
            img_location.setVisibility(GONE);
        }
        else {
            ImageLoader.getInstance().displayImage(model.activeFileUrl, img_location, ImageUtil.getDefultImageOptions());
        }

        if (TextUtils.isEmpty(model.awardFileUrl))
        {
            img_prize.setVisibility(GONE);
        }
        else {
            ImageLoader.getInstance().displayImage(model.awardFileUrl, img_prize, ImageUtil.getDefultImageOptions());
        }


        //赞
        like.setInfo(model);

        //====emotion====
//        contentView.setText(MyEmotionsUtil.getInstance().getTextWithEmotion((Activity)c, clubhead.content));
        // 头像
        ImageLoader.getInstance().displayImage(model.headicon, roundedImageView1,
                ImageUtil.getImageOptionClubIcon());
        // 话题帖
        if (model.topic_image != null && model.topic_image.size() > 0)
        {
            iv_head.setVisibility(View.VISIBLE);
            iv_load.setVisibility(View.VISIBLE);
            animationDrawable = (AnimationDrawable) iv_load.getDrawable();
            animationDrawable.start();

            ViewTreeObserver vto = iv_head.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout()
                {
                    ImageWidth = iv_head.getWidth();
                    ViewTreeObserver obs = iv_head
                            .getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);

                    ImageLoader.getInstance().displayImage(
                            model.topic_image.get(0).image,
                            iv_head, ImageUtil.getclub_topic_imageOptions(),
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
            iv_head.setVisibility(View.GONE);
            iv_head.setImageBitmap(null);
            iv_load.setVisibility(GONE);
        }
        String replynum = model.replytopic;

        if ("0".equals(replynum))
        {
            replynum = "1";
        }

        tv_active_style.setText(model.hardDegree);

        //====3.5======
        if ( model.taglist != null && model.taglist.size() != 0 )
        {
            layout_tag.setVisibility(View.VISIBLE);
            tv_tag1.setVisibility(INVISIBLE);
            tv_tag2.setVisibility(INVISIBLE);
            tv_tag3.setVisibility(INVISIBLE);
            int num = model.taglist.size();
            if ( num >0 )
            {
                tv_tag1.setText("#"+model.taglist.get(0).name);
                tv_tag1.setVisibility(VISIBLE);
                tv_tag1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", model.taglist.get(0).id);
                        mContext.startActivity(intent);
                    }
                });
            }
            if ( num > 1)
            {
                tv_tag2.setText("#"+model.taglist.get(1).name);
                tv_tag2.setVisibility(VISIBLE);
                tv_tag2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", model.taglist.get(1).id);
                        mContext.startActivity(intent);
                    }
                });
            }
            if ( num > 2 )
            {
                tv_tag3.setText("#"+model.taglist.get(2).name);
                tv_tag3.setVisibility(VISIBLE);
                tv_tag3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ClubSpecialListActivity.class);
                        intent.putExtra("tagid", model.taglist.get(2).id);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
        else {
            layout_tag.setVisibility(View.INVISIBLE);
        }


        // 权限1创始人，2管理员，4成员,8网站编辑
        if ("4".equals(model.is_jion) || "-1".equals(model.is_jion))
        {
            //已报名
            if (  model.applyStauts == 1 )
            {
                applyOK();
            }
            else
            {
                //报名已结束
                if ( "0".equals(model.days) )
                {
                    applyPast();
                }
            }
        }
        else
        {
            btn_join.setText("查看已报名用户");
        }

        getHeight(iv_head);


    }

//跳转到申请相应界面
    public void doAction()
    {
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userID))
        {
            Intent intent = new Intent(getContext(), LSLoginActivity.class);
            getContext().startActivity(intent);
        } else
        {
            if ("4".equals(model.is_jion) || "-1".equals(model.is_jion))
            {
                Intent intent = new Intent(getContext(), LSClubApplyActivity.class);
                intent.putExtra("clubID", Common.string2int(model.club_id));
                intent.putExtra("topicID", Common.string2int(model.topic_id));
                intent.putExtra("clubName", model.club_title);
                main.startActivityForResult(intent, 997);
            } else
            {
                Intent intent = new Intent(getContext(), LSClubApplyListActivity.class);
                intent.putExtra("clubID", Common.string2int(model.club_id));
                intent.putExtra("topicID", Common.string2int(model.topic_id));
                intent.putExtra("clubName", model.club_title);
                getContext().startActivity(intent);
            }
        }
    }


    public void applyOK ()
    {
        model.applyStauts = 1;
        btn_join.setText("已申请");
        btn_join.setEnabled(false);
        btn_join.setClickable(false);
    }
    //过期
    public void applyPast ()
    {
        btn_join.setText("申请已截止");
        btn_join.setBackgroundResource(R.drawable.club_action_past);
        btn_join.setClickable(false);
        btn_join.setEnabled(false);
    }

    public void setLikeHandler (HandlerList likeCall)
    {
        like.setLikeCall(likeCall);
    }

    public int getHeadHeight()
    {
        return ImageWidth;
    }
    private int ImageWidth = 0;
    private void getHeight ( final ImageView v )
    {
        ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                ImageWidth = v.getHeight();

                Common.log("ImageWidth=" + ImageWidth);
                ViewTreeObserver obs = v.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_join:

                if (!Common.isLogin(main)) {
                    return;
                }
                //管理员可以直接进入报名管理列表
                String uid = DataManager.getInstance().getUser().getUser_id();
                if (Common.replyDelete(model.is_jion, uid))
                {
                    doAction();
                    return;
                }

                if ( !main.isShared )
                {
                    main.rightAction();
                    Common.toast("任意一种分享方式成功后，进入报名页面");
                }
                else
                {
                    doAction();
                }

                break;
            case R.id.tv_click_reply:
                main.showReplyPanel();
                break;
            // 俱乐部详情
            case R.id.layout_club_name:
                intent = new Intent(mContext, LSClubDetailActivity.class);
                intent.putExtra("clubID", Common.string2int(model.club_id));
                mContext.startActivity(intent);
                break;
            case R.id.roundedImageView1 :
                intent = new Intent(mContext, LSUserHomeActivity.class);
                intent.putExtra("userID", model.user_id);
                mContext.startActivity(intent);
                break;
            case R.id.btn_attention:
                btn_attention.setVisibility(GONE);
                if ( model == null ) return;
                LSRequestManager.getInstance().getFriendsAddAttention(model.id, null);
                break;
        }
    }
}
