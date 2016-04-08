package com.lis99.mobile.club.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.ClubTopicDetailHead;
import com.lis99.mobile.club.model.LikeInterface;
import com.lis99.mobile.club.model.LikeListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/15.
 */
public class LSClubTopicHeadLike implements View.OnClickListener{

    private Context c;
//19人赞过
    private TextView tv_like;

    private RoundedImageView iv_like_1, iv_like_2, iv_like_3, iv_like_4, iv_like_5, iv_like_6, iv_like_7, iv_like_8;

    private ImageView vipStar_1, vipStar_2, vipStar_3, vipStar_4, vipStar_5, vipStar_6, vipStar_7, vipStar_8, iv_like, iv_like_0, iv_like_9;
//tiezi Id
    private int topicid, like_num;
    private ArrayList<LikeListModel> list;

    private LikeInterface clubhead;

    private HandlerList likeCall;

    private Animation animation;

    private String likeUrl;

    public void setLikeCall ( HandlerList likeCall )
    {
        this.likeCall = likeCall;
        likeCall.addItem(callBack);
    }

    public void setLikeUrl (String url)
    {
        likeUrl = url;
    }

    public LSClubTopicHeadLike(Context context)
    {
        // TODO Auto-generated constructor stub
        c = context;
        animation = AnimationUtils.loadAnimation(c, R.anim.like_anim_rotate);
    }
//  设置数据
    public  void setInfo ( LikeInterface clubhead )
    {
        this.clubhead = clubhead;
        this.topicid = Common.string2int(clubhead.getTopic_id());
        this.like_num = Common.string2int(clubhead.getLikeNum());

         list = clubhead.getList();

        if ( "1".equals(clubhead.getLikeStatus()))
        {
            iv_like.setImageResource(R.drawable.like_button_press);
        }
        else
        {
            iv_like.setImageResource(R.drawable.topic_like_hand);
        }

        tv_like.setText(like_num + "人赞过");

        //没有赞
        if ( list == null || list.size() == 0 ) return;
        //更多
        if ( like_num > 8 )
        {
            iv_like_9.setVisibility(View.VISIBLE);
        }

        for ( int i = 0; i < list.size(); i++ )
        {
            LikeListModel model = list.get(i);
            if ( i == 0 )
            {
                iv_like_1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_1, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 1 )
                {
                    vipStar_1.setVisibility(View.VISIBLE);
                }
                else
                {
                    vipStar_1.setVisibility(View.GONE);
                }
            }
            if ( i == 1)
            {
                iv_like_2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_2, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_2.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_2.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 2)
            {
                iv_like_3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_3, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_3.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_3.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 3)
            {
                iv_like_4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_4, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_4.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_4.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 4)
            {
                iv_like_5.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_5, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_5.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_5.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 5)
            {
                iv_like_6.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_6, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_6.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_6.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 6)
            {
                iv_like_7.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_7, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_7.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_7.setVisibility(View.VISIBLE);
                }
            }
            if ( i == 7)
            {
                iv_like_8.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(model.getHeadIcon(), iv_like_8, ImageUtil.getclub_topic_headImageOptions());
                if ( model.getIsVip() == 0 )
                {
                    vipStar_8.setVisibility(View.GONE);
                }
                else
                {
                    vipStar_8.setVisibility(View.VISIBLE);
                }
            }
        }



    }

    public void InitView ( View v )
    {
        tv_like = (TextView) v.findViewById(R.id.tv_like_new);

        iv_like_0 = (ImageView) v.findViewById(R.id.iv_like_0);
        iv_like_1 = (RoundedImageView) v.findViewById(R.id.iv_like_1);
        iv_like_2 = (RoundedImageView) v.findViewById(R.id.iv_like_2);
        iv_like_3 = (RoundedImageView) v.findViewById(R.id.iv_like_3);
        iv_like_4 = (RoundedImageView) v.findViewById(R.id.iv_like_4);
        iv_like_5 = (RoundedImageView) v.findViewById(R.id.iv_like_5);
        iv_like_6 = (RoundedImageView) v.findViewById(R.id.iv_like_6);
        iv_like_7 = (RoundedImageView) v.findViewById(R.id.iv_like_7);
        iv_like_8 = (RoundedImageView) v.findViewById(R.id.iv_like_8);
        iv_like_9 = (ImageView) v.findViewById(R.id.iv_like_9);

        vipStar_1 = (ImageView) v.findViewById(R.id.vipStar_1);
        vipStar_2 = (ImageView) v.findViewById(R.id.vipStar_2);
        vipStar_3 = (ImageView) v.findViewById(R.id.vipStar_3);
        vipStar_4 = (ImageView) v.findViewById(R.id.vipStar_4);
        vipStar_5 = (ImageView) v.findViewById(R.id.vipStar_5);
        vipStar_6 = (ImageView) v.findViewById(R.id.vipStar_6);
        vipStar_7 = (ImageView) v.findViewById(R.id.vipStar_7);
        vipStar_8 = (ImageView) v.findViewById(R.id.vipStar_8);

        iv_like = (ImageView) v.findViewById(R.id.iv_like_new);

        iv_like_1.setVisibility(View.INVISIBLE);
        iv_like_2.setVisibility(View.INVISIBLE);
        iv_like_3.setVisibility(View.INVISIBLE);
        iv_like_4.setVisibility(View.INVISIBLE);
        iv_like_5.setVisibility(View.INVISIBLE);
        iv_like_6.setVisibility(View.INVISIBLE);
        iv_like_7.setVisibility(View.INVISIBLE);
        iv_like_8.setVisibility(View.INVISIBLE);
        iv_like_9.setVisibility(View.INVISIBLE);

        vipStar_1.setVisibility(View.GONE);
        vipStar_2.setVisibility(View.GONE);
        vipStar_3.setVisibility(View.GONE);
        vipStar_4.setVisibility(View.GONE);
        vipStar_5.setVisibility(View.GONE);
        vipStar_6.setVisibility(View.GONE);
        vipStar_7.setVisibility(View.GONE);
        vipStar_8.setVisibility(View.GONE);

        iv_like_0.setOnClickListener(this);
        iv_like_1.setOnClickListener(this);
        iv_like_2.setOnClickListener(this);
        iv_like_3.setOnClickListener(this);
        iv_like_4.setOnClickListener(this);
        iv_like_5.setOnClickListener(this);
        iv_like_6.setOnClickListener(this);
        iv_like_7.setOnClickListener(this);
        iv_like_8.setOnClickListener(this);
        iv_like_9.setOnClickListener(this);



    }

    public void setModel ()
    {

    }


    @Override
    public void onClick(View view) {

        Intent intent = null;
        LikeListModel model = null;
        switch (view.getId())
        {
            case R.id.iv_like_0:

                if ( Common.isLogin((Activity)c) )
                {
                    if ( "1".equals(clubhead.getLikeStatus())) return;

                    iv_like.setImageResource(R.drawable.like_button_press);

                    iv_like.startAnimation(animation);

                    likeCall.handlerAall();
//                    新版话题帖赞
                    if (C.CLUB_TOPIC_LIKE_NEW.equals(likeUrl))
                    {
                        LSRequestManager.getInstance().clubTopicLikeNew(topicid, null);
                    }
                    else
                    {
                        LSRequestManager.getInstance().clubTopicLike(topicid, new CallBack() {
                            @Override
                            public void handler(MyTask mTask) {
//                            likeCall.handlerAall();
                            }
                        });
                    }


                }
                break;
            case R.id.iv_like_1:
                if ( list == null || list.size() < 1 ) return;
                model = list.get(0);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_2:
                if ( list == null || list.size() < 2 ) return;
                model = list.get(1);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_3:
                if ( list == null || list.size() < 3 ) return;
                model = list.get(2);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_4:
                if ( list == null || list.size() < 4 ) return;
                model = list.get(3);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_5:
                if ( list == null || list.size() < 5 ) return;
                model = list.get(4);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_6:
                if ( list == null || list.size() < 6 ) return;
                model = list.get(5);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_7:
                if ( list == null || list.size() < 7 ) return;
                model = list.get(6);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_8:
                if ( list == null || list.size() < 8 ) return;
                model = list.get(7);
                intent = new Intent(c, LSUserHomeActivity.class);
                intent.putExtra("userID", ""+model.getId());
                c.startActivity(intent);
                break;
            case R.id.iv_like_9:
//                更多
                break;
        }

    }

    private CallBack  callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
//            clubhead.LikeStatus = "1";
            clubhead.setLikeStatus("1");

            like_num+=1;
//            clubhead.likeNum = ""+like_num;
            clubhead.setLikeNum(""+like_num);

            tv_like.setText(like_num + "人赞过");
            iv_like.setImageResource(R.drawable.like_button_press);

            ClubTopicDetailHead.LikeList item = new ClubTopicDetailHead.LikeList();
            item.is_vip = Common.string2int(DataManager.getInstance().getUser().getIs_vip());
            item.headicon = DataManager.getInstance().getUser().getHeadicon();
            item.id = Common.string2int(DataManager.getInstance().getUser().getUser_id());

            list.add(0, item);
//刷新
            setInfo(clubhead);

        }
    };

}
