package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.adapter.ClubNewTopicListItem;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.model.IsToRecommendActive;
import com.lis99.mobile.club.model.TopicNewListMainModel;
import com.lis99.mobile.club.model.TopicNewListMainModelEquip;
import com.lis99.mobile.club.model.TopicNewListMainModelTitle;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ShareManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/3/8.
 * 新版话题帖
 *
 *  TOPICID String
 *
 *
 */
public class LSClubNewTopicListMain extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, LSClubTopicImageListener {

    private ClubNewTopicListItem adapter;

    private ListView listView;

    private PullToRefreshView refreshView;

    private RelativeLayout layoutMain;

//    private Page page;
    
    private ImageView titleRightImage, iv_weichat, iv_friend;

    private String topicId = "1";

    private TopicNewListMainModel model;

    private TextView tv_reply, tv_like, tv_reply_num;

    private ImageView iv_like;

    private View layout_like, layout_reply;

    private Animation animation;

    private HandlerList likeCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_new_topic_list_main);

        topicId = getIntent().getStringExtra("TOPICID");

        initViews();

        animation = AnimationUtils.loadAnimation(this, R.anim.like_anim);

        addCallLike();

//        MyEmotionsUtil.getInstance().setVisibleEmotion(callBack);
//        MyEmotionsUtil.getInstance().initView(this, bodyView, btn_emotion, footerForEmoticons,
//                layoutMain);

//        setTitle("123");

        onHeaderRefresh(refreshView);

    }



    @Override
    protected void initViews() {


        findViewById(R.id.titleLeft).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        iv_weichat = (ImageView) findViewById(R.id.iv_weichat);
        iv_friend = (ImageView) findViewById(R.id.iv_friend);

        tv_reply = (TextView) findViewById(R.id.tv_reply);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_reply_num = (TextView) findViewById(R.id.tv_reply_num);

        iv_like = (ImageView) findViewById(R.id.iv_like);

        layout_like = findViewById(R.id.layout_like);
        layout_reply = findViewById(R.id.layout_reply);


        tv_reply.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        layout_reply.setOnClickListener(this);

        titleRightImage.setOnClickListener(this);
        iv_weichat.setOnClickListener(this);
        iv_friend.setOnClickListener(this);

    }

    private void getList ()
    {
//        if ( page.isLastPage() )
//        {
//            return;
//        }


        String url = C.CLUB_NEW_TOPIC_LIST_MAIN;// + page.getPageNo();

        HashMap<String, Object> map = new HashMap<>();

        String userId = DataManager.getInstance().getUser().getUser_id();

        map.put("topics_id", topicId);
        map.put("user_id", userId);

        model = new TopicNewListMainModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                Common.log("==============================" + mTask.getresult());

                model = (TopicNewListMainModel) mTask.getResultModel();

                if (model == null) return;

//                page.nextPage();
//              分享图片获取URL
                if (model.topicsdetaillist != null && model.topicsdetaillist.size() >= 1) {
                    for (int i = 0; i < model.topicsdetaillist.size(); i++) {
                        model.imgShareUrl = model.topicsdetaillist.get(i).images;
                        if (!TextUtils.isEmpty(model.imgShareUrl)) {
                            break;
                        }
                    }
                }

                tv_reply_num.setText("" + model.topictot);
                tv_like.setText("" + model.likeNum);

                if (model.myLikeStatus == 0) {
                    iv_like.setImageResource(R.drawable.topic_new_like_normal);
                } else {
                    iv_like.setImageResource(R.drawable.topic_new_liked);
                }

                if (adapter == null) {
//                    page.setPageSize(model.totpage);

                    TopicNewListMainModelTitle title = model;




                    ArrayList<Object> mList = new ArrayList<Object>();

                    mList.add(title);

                    mList.addAll(model.topicsdetaillist);

                    TopicNewListMainModelEquip equipModel = model;

                    mList.add(equipModel);
                    if ( model.topicsreplylist == null || model.topicsreplylist.size() == 0 )
                    {
                        mList.add("NoReply");
                    }
                    else
                    {
                        mList.addAll(model.topicsreplylist);
                    }
                    //根据is_tagid判断是否有推荐活动，有则将info添加到List尾端
                    //组装话题帖底部到推荐活动页的入口信息
                    if (model.getIs_tagId()>0) {
                        IsToRecommendActive info=new IsToRecommendActive();
                        info.setIs_tagid(model.getIs_tagId());
                        info.setReason(model.getReason());
                        mList.add(info);
                    }
                    //else语句块是测试用例，正常时应该注释掉
                    /*else{
                        IsToRecommendActive info=new IsToRecommendActive();
                        info.setIs_tagid(202);
                        info.setReason("这是测试用的，显示出这个表示当前话题没有关联的活动，");
                        mList.add(info);
                    }*/
                    adapter = new ClubNewTopicListItem(activity, mList);

                    adapter.setLikeCall(likeCall);

                    adapter.setMain(LSClubNewTopicListMain.this);

                    listView.setAdapter(adapter);
                }
//                else
//                {
//                    adapter.addList(model.topicsdetaillist);
//
//                    if ( page.isLastPage() )
//                    {
//                        TopicNewListMainModelEquip equipModel = model;
//
//                        ArrayList<Object> mList = new ArrayList<Object>();
//
//                        mList.add(equipModel);
//                        mList.addAll(model.topicsreplylist);
//
//                        adapter.addList(mList);
//                    }
//                }

            }
        });





    }

    private void cleanList ()
    {
//        page = new Page();
        listView.setAdapter(null);
        adapter = null;
    }

    @Override
    protected void rightAction() {

        ShareManager.getInstance().showPopWindowInShare(model, layoutMain, null);


        super.rightAction();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Intent intent = null;
        switch (arg0.getId())
        {
            case R.id.titleLeft:
                finish();
                break;
            case R.id.titleRightImage:
                rightAction();
                break;
            case R.id.iv_weichat:

                ShareManager.getInstance().share2Weichat(model, shareFandW);
                break;
            case R.id.iv_friend:

//				ShareManager.getInstance().share2Weichat("" + topicID, imgUrl, clubhead.title, null);
                ShareManager.getInstance().share2Friend(model, shareFandW);
                break;
            case R.id.layout_like:
                if ( model == null ) return;
                if ( model.myLikeStatus == 1 )
                {
                    return;
                }
                if ( Common.isLogin(activity) )
                {

                    iv_like.startAnimation(animation);

                    likeCall.handlerAall();

                    LSRequestManager.getInstance().clubTopicLikeNew(Common.string2int(topicId), null);

//                    LSRequestManager.getInstance().clubTopicLike(Common.string2int(topicId), new CallBack() {
//                        @Override
//                        public void handler(MyTask mTask) {
//                            //点赞成功
////							likeCall.handlerAall();
//                        }
//                    });
                }
                break;
            case R.id.layout_reply:
//                跳转评论列表
                if ( model == null ) return;
                intent = new Intent(activity, LSClubNewTopicListMainReply.class);
                intent.putExtra("TITLE", model.title);
                intent.putExtra("TOPICID", Common.string2int(model.topicsId));
                intent.putExtra("CLUBID", model.clubId);
                startActivity(intent);

                break;
            case R.id.tv_reply:
//                调用评论
                if ( model == null ) return;
                intent = new Intent(activity, LSClubTopicNewReply.class);
                intent.putExtra("replyedName", "");
                intent.putExtra("replyedcontent", "");
                intent.putExtra("replyedfloor", "");
                intent.putExtra("replyedId", "");
                intent.putExtra("clubId", "" + model.clubId);
                intent.putExtra("topicId", "" + topicId);
//                startActivityForResult(intent, 999);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_open_down_up, 0);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        // 回复成功， 翻页到最后一页
//        if (resultCode == RESULT_OK && requestCode == 999)
//        {
//            int pagetot = data.getIntExtra("lastPage", -1);
//            if (pagetot == -1)
//            {
//                return;
//            }
//            // 上翻页标记
//            needup = true;
//            cleanListUp();
//            // 最后一页
//            page.pageNo = pagetot;
//            // 获取最后一页内容
//            getReplyListUp();
//        }
//        // 报名
//        else if (resultCode == RESULT_OK && requestCode == 997)
//        {
//            headViewActive.applyOK();
//        }
//    }

    public void addCallLike ()
    {
        likeCall = HandlerList.getInstance();
        likeCall.addItem(new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if (model == null) return;
//                clubhead.LikeStatus = "1";
                model.myLikeStatus = 1;
                iv_like.setImageResource(R.drawable.topic_new_liked);
                int num = model.likeNum;
                num += 1;
                tv_like.setText(""+num);
                tv_like.setTextColor(getResources().getColor(R.color.topic_like_color));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        刷新
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == 999 )
            {
                onHeaderRefresh(refreshView);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanList();
        if ( likeCall != null )
        likeCall.clean();
//		likeCall = null;
    }


    @Override
    public void onClickImage(String imageUrl) {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
//        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    private CallBack shareFandW = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            Common.toast("分享成功");
        }
    };


}
