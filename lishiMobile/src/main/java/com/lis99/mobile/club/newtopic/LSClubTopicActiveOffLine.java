package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.apply.LSApplayNew;
import com.lis99.mobile.club.model.ClubTopicActiveLineMainModel;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ShareManager;
import com.lis99.mobile.view.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by yy on 16/1/8.
 * <p/>
 * 新的线下活动帖
 */
public class LSClubTopicActiveOffLine extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener {


    private Button btnok;
    private BannerView viewBanner;
    private TextView titleView;
    private ImageView iv;
    private ImageView ivtravelbg;
    private ImageView ivdestinationbg;
    private TextView tvdata;
    private TextView tv;
    private TextView tvtags1;
    private TextView tvtags2;
    private TextView tvtags3;
    private TextView tvtags4;
    private TextView tvprice;
    private MyListView listhighlights;
    private TextView tvhighlights;
    private Button btninfo;
    private RoundedImageView roundedImageView1;
    private ImageView vipStar;
    private TextView tvname;
    private TextView clubname;
    private TextView tvtraveltag;
    private Button btntravel;
    private MyListView listjoinreadme;
    private Button btndestination;
    private PullToRefreshView pull_refresh_view;
    //    目的地， 本版不展示 报名须知
    private View layout_address, layout_readme;

    private LSClubTopicInfoAdapter highlightsAdapter, joinAdapter;

    private ClubTopicActiveLineMainModel model;

    private ImagePageAdapter bannerAdapter;

    private int activity_id;

    private int clubID;

    private RelativeLayout layoutMain;

    private ImageView titleRightImage, iv_weichat, iv_friend;

    View title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lsclub_active_offline_new);

        activity_id = getIntent().getIntExtra("topicID", 0);

//        setTitle("");

        initialize();

        getInfo();

    }

    private void getInfo() {
        String url = C.CLUB_TOPIC_ACTIVE_LINE_MIAN;

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("activity_id", activity_id);

        model = new ClubTopicActiveLineMainModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ClubTopicActiveLineMainModel) mTask.getResultModel();

                if (model == null) return;

                clubID = Common.string2int(model.getClub_id());

                titleView.setText(model.getTitle());
                tvdata.setText(model.getActivitytimes());
                tvprice.setText(model.getConsts());

                if (model.getActivityimgs() != null) {
                    bannerAdapter = new ImagePageAdapter(LSClubTopicActiveOffLine.this, model.getActivityimgs().size());
                    bannerAdapter.addImagePageAdapterListener(LSClubTopicActiveOffLine.this);
                    bannerAdapter.setImagePageClickListener(LSClubTopicActiveOffLine.this);
                    viewBanner.setBannerAdapter(bannerAdapter);
                    viewBanner.startAutoScroll();
                }

                if (model.getActivelightspot() != null && model.getActivelightspot().size() != 0) {
                    listhighlights.setVisibility(View.VISIBLE);
                    highlightsAdapter = new LSClubTopicInfoAdapter(activity, model.getActivelightspot());
                    listhighlights.setAdapter(highlightsAdapter);
                } else {

                    if (model.getActivitydetail() != null && model.getActivitydetail().size() != 0) {
                        tvhighlights.setVisibility(View.VISIBLE);
                        tvhighlights.setText(model.getActivitydetail().get(0).getContent());
                    }
                }

                if ( TextUtils.isEmpty(model.getLeader_userid()))
                {
                    if (!TextUtils.isEmpty(model.getClub_iconv())) {
                        ImageLoader.getInstance().displayImage(model.getClub_iconv(), roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
                    }
                    tvname.setText(model.getClub_title());
                }
                else
                {
                    if (!TextUtils.isEmpty(model.getLeaderheadicon())) {
                        ImageLoader.getInstance().displayImage(model.getLeaderheadicon(), roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
                    }
                    tvname.setText(model.getLeadernickname());
                }


//                标签
                if (model.getLeaderdesc() != null && model.getLeaderdesc().size() != 0) {
                    for (int ii = 0; ii < model.getLeaderdesc().size(); ii++) {
                        if (ii == 0) {
                            tvtags1.setVisibility(View.VISIBLE);
                            tvtags1.setText(model.getLeaderdesc().get(ii));
                        } else if (ii == 1) {
                            tvtags2.setVisibility(View.VISIBLE);
                            tvtags2.setText(model.getLeaderdesc().get(ii));
                        } else if (ii == 2) {
                            tvtags3.setVisibility(View.VISIBLE);
                            tvtags3.setText(model.getLeaderdesc().get(ii));
                        } else if (ii == 3) {
                            tvtags4.setVisibility(View.VISIBLE);
                            tvtags4.setText(model.getLeaderdesc().get(ii));
                        }
                    }
                }

                clubname.setText(model.getClub_title());

                tvtraveltag.setText(model.tripdays+"天行程");

//                ivtravelbg

                if (model.getTripdetail() != null && model.getTripdetail().size() != 0) {
                    ImageLoader.getInstance().displayImage(model.getTripdetail().get(0).getImages(), ivtravelbg, ImageUtil.getDefultTravelImageOptions());
                }

                if (model.getReportnote() != null && model.getReportnote().size() != 0) {
                    layout_readme.setVisibility(View.VISIBLE);

                    joinAdapter = new LSClubTopicInfoAdapter(activity, model.getReportnote());

                    listjoinreadme.setAdapter(joinAdapter);
                } else {
                    layout_readme.setVisibility(View.GONE);
                }

                //默认
                applyBtn();
//已报名

                //报名已结束
                if ( model.applyTimeStatus == 1 )
                {
                    applyPast();
                }
                else
                {
                    if (  model.applyStatus == 1 )
                    {
                        applyOK();
                    }
                }



            }
        });

    }

    private void cleanInfo() {
        tvhighlights.setVisibility(View.GONE);
        listhighlights.setVisibility(View.GONE);

        tvtags1.setVisibility(View.GONE);
        tvtags2.setVisibility(View.GONE);
        tvtags3.setVisibility(View.GONE);
        tvtags4.setVisibility(View.GONE);

        layout_readme.setVisibility(View.GONE);
        layout_address.setVisibility(View.GONE);
        model = null;
    }


    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

        if (model == null || model.getActivityimgs() == null || model.getActivityimgs().size() == 0)
            return;

        ImageLoader.getInstance().displayImage(model.getActivityimgs().get(position).getImages(), banner, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));


    }

    @Override
    public void onClick(int index) {
//        banner 点击事件

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
//        getInfo();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanInfo();
        getInfo();
    }

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Intent i = null;
        switch (arg0.getId()) {
            case R.id.club_name:
                i = new Intent(activity, LSClubDetailActivity.class);
                i.putExtra("clubID", clubID);
                startActivity(i);
                break;
//            报名
            case R.id.btn_ok:
                doAction();
                break;
//            查看详情
            case R.id.btn_info:
                i = new Intent(activity, LSClubTopicActiveDetail.class);
                i.putExtra("MODEL", model);
                i.putExtra("TYPE", "0");
                startActivity(i);
                break;
//            查看活动行程
            case R.id.btn_travel:
                i = new Intent(activity, LSClubTopicActiveDetail.class);
                i.putExtra("MODEL", model);
                i.putExtra("TYPE", "1");
                startActivity(i);
                break;
//            目的地
            case R.id.btn_destination:

                break;
            case R.id.iv_weichat:
                String imgUrl = null;
                if (model == null) return;
                if (model.getActivityimgs() != null && model.getActivityimgs().size() >= 1) {
                    imgUrl = model.getActivityimgs().get(0).getImages();
                }
                ShareManager.getInstance().share2Weichat("" + activity_id, imgUrl, model.getTitle(), shareFandW);
                break;
            case R.id.iv_friend:
                String imgUrl1 = null;
                if (model == null) return;
                if (model.getActivityimgs() != null && model.getActivityimgs().size() >= 1) {
                    imgUrl1 = model.getActivityimgs().get(0).getImages();
                }
//				ShareManager.getInstance().share2Weichat("" + topicID, imgUrl, clubhead.title, null);
                ShareManager.getInstance().share2Friend("" + activity_id, imgUrl1, model.getTitle(), shareFandW);
                break;
            case R.id.titleRightImage:
                rightAction();
                break;
            case R.id.roundedImageView1:

                if ( model != null )
                {
                    if ( TextUtils.isEmpty(model.getLeader_userid()))
                    {
                        i = new Intent(activity, LSClubDetailActivity.class);
                        i.putExtra("clubID", clubID);
                        startActivity(i);
                    }
                    else
                    {
                        Common.goUserHomeActivit(activity, model.getLeader_userid());
                    }
                }
                break;
        }
    }

    private void initialize() {


        View titleLeft = findViewById(R.id.titleLeft);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        btnok = (Button) findViewById(R.id.btn_ok);
        viewBanner = (BannerView) findViewById(R.id.viewBanner);
        titleView = (TextView) findViewById(R.id.titleView);
        iv = (ImageView) findViewById(R.id.iv);
        tvdata = (TextView) findViewById(R.id.tv_data);
        tv = (TextView) findViewById(R.id.tv);
        tvprice = (TextView) findViewById(R.id.tv_price);
        listhighlights = (MyListView) findViewById(R.id.list_highlights);
        tvhighlights = (TextView) findViewById(R.id.tv_highlights);
        btninfo = (Button) findViewById(R.id.btn_info);
        roundedImageView1 = (RoundedImageView) findViewById(R.id.roundedImageView1);
        vipStar = (ImageView) findViewById(R.id.vipStar);
        tvname = (TextView) findViewById(R.id.tv_name);
        tvtags1 = (TextView) findViewById(R.id.tv_tags1);
        tvtags2 = (TextView) findViewById(R.id.tv_tags2);
        tvtags3 = (TextView) findViewById(R.id.tv_tags3);
        tvtags4 = (TextView) findViewById(R.id.tv_tags4);
        clubname = (TextView) findViewById(R.id.club_name);
        ivtravelbg = (ImageView) findViewById(R.id.iv_travel_bg);
        tvtraveltag = (TextView) findViewById(R.id.tv_travel_tag);
        btntravel = (Button) findViewById(R.id.btn_travel);
        listjoinreadme = (MyListView) findViewById(R.id.list_join_readme);
        ivdestinationbg = (ImageView) findViewById(R.id.iv_destination_bg);
        btndestination = (Button) findViewById(R.id.btn_destination);

        layout_address = findViewById(R.id.layout_address);


        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);


        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        iv_weichat = (ImageView) findViewById(R.id.iv_weichat);
        iv_friend = (ImageView) findViewById(R.id.iv_friend);

        layout_readme = findViewById(R.id.layout_readme);

        titleRightImage.setOnClickListener(this);
        iv_weichat.setOnClickListener(this);
        iv_friend.setOnClickListener(this);


        viewBanner.mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewBanner.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        viewBanner.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        viewBanner.startAutoScroll();
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });


        btnok.setOnClickListener(this);
        btninfo.setOnClickListener(this);
        btntravel.setOnClickListener(this);
        btndestination.setOnClickListener(this);

        clubname.setOnClickListener(this);

        tvhighlights.setVisibility(View.GONE);
        listhighlights.setVisibility(View.GONE);

        tvtags1.setVisibility(View.GONE);
        tvtags2.setVisibility(View.GONE);
        tvtags3.setVisibility(View.GONE);
        tvtags4.setVisibility(View.GONE);

        layout_readme.setVisibility(View.GONE);

        layout_address.setVisibility(View.GONE);

        title = findViewById(R.id.titlebar);


        roundedImageView1.setOnClickListener(this);

    }

    @Override
    protected void rightAction() {
        // TODO Auto-generated method stub
        String imgUrl = null;
        if (model == null) return;

        if (model == null || model.getActivityimgs() == null || model.getActivityimgs().size() == 0) {

        } else {
            imgUrl = model.getActivityimgs().get(0).getImages();
        }

        String shareText = model.shareTxt;

        ShareManager.getInstance().showPopWindowInShare(model, "" + clubID,
                imgUrl, model.getTitle(), shareText,
                "" + model.getActivity_code(), layoutMain, null, "http://m.lis99.com/club/activity/detail/");

        super.rightAction();
    }


    private CallBack shareFandW = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            Common.toast("分享成功");
        }
    };

    public void doAction()
    {
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userID))
        {
            Intent intent = new Intent(activity, LSLoginActivity.class);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(activity, LSApplayNew.class);
            intent.putExtra("clubID", clubID);
            intent.putExtra("topicID", activity_id);
            intent.putExtra("clubName", model.getClub_title());
            intent.putExtra("TYPE", "TOPICNEW");
            startActivityForResult(intent, 997);
        }
    }


    //	默认
    public void applyBtn ()
    {
        btnok.setText("马上报名");
//        actionButton.setBackgroundResource(R.drawable.club_sign);
        btnok.setBackgroundResource(R.drawable.club_info_btn_ok);
        btnok.setEnabled(true);
        btnok.setClickable(true);
    }

    public void applyOK ()
    {
        btnok.setText("已报名");
//        actionButton.setBackgroundResource(R.drawable.club_sign);
        btnok.setBackgroundResource(R.drawable.club_info_btn_ok);
        btnok.setEnabled(false);
        btnok.setClickable(false);
    }
    //过期
    public void applyPast ()
    {
        btnok.setText("报名已截止");
//        actionButton.setBackgroundResource(R.drawable.club_action_past);
        btnok.setBackgroundResource(R.drawable.club_info_btn_ok_over);
        btnok.setClickable(false);
        btnok.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // 报名
        if (resultCode == RESULT_OK && requestCode == 997)
        {
            applyOK();
        }
    }


}
