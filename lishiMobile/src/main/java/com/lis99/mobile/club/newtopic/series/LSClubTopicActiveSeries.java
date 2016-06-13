package com.lis99.mobile.club.newtopic.series;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.lis99.mobile.club.model.ClubTopicActiveSeriesLineMainModel;
import com.lis99.mobile.club.model.EquipRecommendInterFace;
import com.lis99.mobile.club.model.TopicSeriesBatchsListModel;
import com.lis99.mobile.club.newtopic.ActiveLineEquipRecommend;
import com.lis99.mobile.club.newtopic.LSClubTopicInfoAdapter;
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
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.ShareManager;
import com.lis99.mobile.view.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/1/8.
 * <p/>
 * 系列活动帖
 *  int topicID
 *  topicID int
 */
public class LSClubTopicActiveSeries extends LSBaseActivity implements
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

    private ClubTopicActiveSeriesLineMainModel model;

    private ImagePageAdapter bannerAdapter;

    private int activity_id;

    private int clubID;

    private RelativeLayout layoutMain;

    private ImageView titleRightImage, iv_weichat, iv_friend;

    private View title;

    private ActiveLineEquipRecommend equipRecommend;

    private View include_equip;

    private TextView tv_tel;

//  批次信息
    private TopicSeriesBatchsListModel modelBatch;

    private int activePosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lsclub_active_offline_new_series);

        activity_id = getIntent().getIntExtra("topicID", 0);

//        setTitle("");

        initialize();

        getInfo();

    }

    private void getInfo() {
        String url = C.CLUB_TOPIC_ACTIVE_SERIES_LINE_MIAN;

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("activity_id", activity_id);

        model = new ClubTopicActiveSeriesLineMainModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ClubTopicActiveSeriesLineMainModel) mTask.getResultModel();

                if (model == null) return;

                clubID = model.clubId;

                titleView.setText(model.getTitle());
                if ( TextUtils.isEmpty(model.batchDesc))
                {
                    tvdata.setText(model.activitytimes);
                }
                else
                {
                    tvdata.setText(model.activitytimes+" 共" + model.batchTotal+"批次" +"\n"+model.batchDesc);
                }
                tvprice.setText("¥ "+model.consts);

                if (model.activityimgs != null && model.activityimgs.size() != 0 ) {
                    viewBanner.setVisibility(View.VISIBLE);
                    bannerAdapter = new ImagePageAdapter(LSClubTopicActiveSeries.this, model.activityimgs.size());
                    bannerAdapter.addImagePageAdapterListener(LSClubTopicActiveSeries.this);
                    bannerAdapter.setImagePageClickListener(LSClubTopicActiveSeries.this);
                    viewBanner.setBannerAdapter(bannerAdapter);
                    viewBanner.startAutoScroll();
                }
                else {
                    viewBanner.setVisibility(View.GONE);
                }

                if (model.activelightspot != null && model.activelightspot.size() != 0) {
                    listhighlights.setVisibility(View.VISIBLE);
                    highlightsAdapter = new LSClubTopicInfoAdapter(activity, model.activelightspot);
                    listhighlights.setAdapter(highlightsAdapter);
                } else {

                    if (model.activitydetail != null && model.activitydetail.size() != 0) {
                        tvhighlights.setVisibility(View.VISIBLE);
                        tvhighlights.setText(model.activitydetail.get(0).content);
                    }
                }

                if ( TextUtils.isEmpty(""+model.leaderUserid) || "0".equals(model.leaderUserid))
                {
                    if (!TextUtils.isEmpty(model.clubIconv)) {
                        ImageLoader.getInstance().displayImage(model.clubIconv, roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
                    }
//                    tvname.setText(model.getClub_title());
                    tvname.setVisibility(View.GONE);
                    vipStar.setVisibility(View.GONE);
                }
                else
                {
                    if (!TextUtils.isEmpty(model.leaderheadicon)) {
                        ImageLoader.getInstance().displayImage(model.leaderheadicon, roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
                    }
                    vipStar.setVisibility(View.VISIBLE);
                    tvname.setVisibility(View.VISIBLE);
                    tvname.setText(model.leadernickname);
                }


//                标签
                if (model.leaderdesc != null && model.leaderdesc.size() != 0) {
                    for (int ii = 0; ii < model.leaderdesc.size(); ii++) {
                        if (ii == 0) {
                            tvtags1.setVisibility(View.VISIBLE);
                            tvtags1.setText(model.leaderdesc.get(ii));
                        } else if (ii == 1) {
                            tvtags2.setVisibility(View.VISIBLE);
                            tvtags2.setText(model.leaderdesc.get(ii));
                        } else if (ii == 2) {
                            tvtags3.setVisibility(View.VISIBLE);
                            tvtags3.setText(model.leaderdesc.get(ii));
                        } else if (ii == 3) {
                            tvtags4.setVisibility(View.VISIBLE);
                            tvtags4.setText(model.leaderdesc.get(ii));
                        }
                    }
                }

                clubname.setText(model.clubTitle);

                tvtraveltag.setText(model.batchTotal+" 批次");

//                ivtravelbg

                if (model.tripdetail != null && model.tripdetail.size() != 0) {
                    ImageLoader.getInstance().displayImage(model.tripdetail.get(0).images, ivtravelbg, ImageUtil.getDefultTravelImageOptions());
                }

                if (model.reportnote != null && model.reportnote.size() != 0) {
                    layout_readme.setVisibility(View.VISIBLE);

                    joinAdapter = new LSClubTopicInfoAdapter(activity, model.reportnote);

                    listjoinreadme.setAdapter(joinAdapter);
                } else {
                    layout_readme.setVisibility(View.GONE);
                }

//                装备
                if ( model.zhuangbeilist != null && model.zhuangbeilist.size() != 0 )
                {
                    include_equip.setVisibility(View.VISIBLE);
                    equipRecommend = new ActiveLineEquipRecommend(activity);
                    equipRecommend.init(include_equip);

                    ArrayList<EquipRecommendInterFace> item = new ArrayList<EquipRecommendInterFace>(model.zhuangbeilist);

                    equipRecommend.setModel(item);

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

        if (model == null || model.activityimgs == null || model.activityimgs.size() == 0)
            return;

        ImageLoader.getInstance().displayImage(model.activityimgs.get(position).images, banner, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));


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
                i = new Intent(activity, LSClubTopicActiveDetailSeries.class);
                i.putExtra("MODEL", model);
                i.putExtra("TYPE", "0");
                startActivity(i);
                break;
//            查看活动行程
            case R.id.btn_travel:
                i = new Intent(activity, LSClubTopicActiveDetailSeries.class);
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
                if (model.activityimgs != null && model.activityimgs.size() >= 1) {
                    imgUrl = model.activityimgs.get(0).images;
                }
                ShareManager.getInstance().share2Weichat(model, shareFandW);
                break;
            case R.id.iv_friend:
                String imgUrl1 = null;
                if (model == null) return;
                if (model.activityimgs != null && model.activityimgs.size() >= 1) {
                    imgUrl1 = model.activityimgs.get(0).images;
                }
//				ShareManager.getInstance().share2Weichat("" + topicID, imgUrl, clubhead.title, null);
                ShareManager.getInstance().share2Friend( model, shareFandW);
                break;
            case R.id.titleRightImage:
                rightAction();
                break;
            case R.id.roundedImageView1:

                if ( model != null )
                {
                    if ( TextUtils.isEmpty(""+model.leaderUserid) || "0".equals(model.leaderUserid) )
                    {
                        i = new Intent(activity, LSClubDetailActivity.class);
                        i.putExtra("clubID", clubID);
                        startActivity(i);
                    }
                    else
                    {
                        Common.goUserHomeActivit(activity, ""+model.leaderUserid);
                    }
                }
                break;
        }
    }

    private void initialize() {

        tv_tel = (TextView) findViewById(R.id.tv_tel);

        final String tel = getResources().getString(R.string.tel);

        String telInfo = "<font color=\"#525252\">免费咨询电话</font><font color=\"#2bca63\">" + tel + "</font><font color=\"#525252\">(工作日9:00-18:00)</font>";

        tv_tel.setText(Html.fromHtml(telInfo));

        tv_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.telPhone(tel);
            }
        });

        View titleLeft = findViewById(R.id.titleLeft);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        include_equip = findViewById(R.id.include_equip);
        include_equip.setVisibility(View.GONE);


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

        ShareManager.getInstance().showPopWindowInShare(model, layoutMain, null);

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

//        if ( true )
//        {
//            PushModel p = new PushModel();
//            p.type = 3;
//
//            Intent i = new Intent();
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra(PushManager.TAG, p);
//            i.setClass(activity,
//                    LsStartupActivity.class);
//            startActivity(i);
//            return;
//        }

//        if ( true )
//        {
//            Intent intent = new Intent(activity, WXPayEntryActivity.class);
//            startActivity(intent);
//            return;
//        }



        String userID = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userID))
        {
            Intent intent = new Intent(activity, LSLoginActivity.class);
            startActivity(intent);
        } else
        {
            getSeriesList();
        }
    }


    //获取系列活动列表
    private void getSeriesList ()
    {

//        if ( modelBatch != null && modelBatch.batchList != null && modelBatch.batchList.size() != 0 )
//        {
//            showBacthList();
//            return;
//        }

        String url = C.CLUB_TOPIC_ACTIVE_SERIES_LINE_LIST;

        String userId = Common.getUserId();

        HashMap<String, Object> map = new HashMap<>();

        map.put("user_id", userId);
        map.put("activity_id", activity_id);

        modelBatch = new TopicSeriesBatchsListModel();

        MyRequestManager.getInstance().requestPost(url, map, modelBatch, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                modelBatch = (TopicSeriesBatchsListModel) mTask.getResultModel();

                if ( modelBatch == null || modelBatch.batchList == null || modelBatch.batchList.size() == 0 ) return;

                showBacthList();
            }
        });



    }

    private void showBacthList ()
    {
        PopWindowUtil.showActiveSeriesLine(activePosition, btnok, modelBatch, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if ( mTask == null )
                {
                    return;
                }

                activePosition = Integer.parseInt(mTask.getresult());

                if ( activePosition == -1 )
                {
                    return;
                }

                TopicSeriesBatchsListModel.BatchListEntity item = modelBatch.batchList.get(activePosition);

                if ( item.isBaoming == 1 || item.isEnd == 1 )
                {
                    return;
                }

                Intent intent = new Intent(activity, LSApplySeriesNew.class);
                intent.putExtra("clubID", clubID);
                intent.putExtra("batchID", item.batchId);
                intent.putExtra("topicID", activity_id);
                startActivityForResult(intent, 997);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // 报名
        if (resultCode == RESULT_OK && requestCode == 997)
        {

        }
    }




}
