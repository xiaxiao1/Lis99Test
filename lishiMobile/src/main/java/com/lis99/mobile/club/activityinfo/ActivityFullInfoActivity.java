package com.lis99.mobile.club.activityinfo;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicInfoLocation;
import com.lis99.mobile.club.model.BatchListEntity;
import com.lis99.mobile.club.model.ClubTopicActiveSeriesLineMainModel;
import com.lis99.mobile.club.model.TopicSeriesBatchsListModel;
import com.lis99.mobile.club.newtopic.series.LSApplySeriesNew;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.ShareManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActivityFullInfoActivity extends LSBaseActivity implements ISlideCallback, View
        .OnClickListener {


    ClubTopicActiveSeriesLineMainModel model;
    TopicSeriesBatchsListModel modelBatch;
    private int activePosition = -1;
    String s = "ssss";
    boolean close = true;
    int activity_id;
    List<View> baomingxvzhiList;
    //root View
    RelativeLayout layoutmain;
    //负责实现UI上拉加载显示详情
    private SlideDetailsLayout mSlideDetailsLayout;
    //显示主要页面内容
    ListFragment f1;
    //标题栏返回区域
    RelativeLayout back_rl;
    //标题栏返回
    ImageView back_img;
    //标题栏分享
    ImageView share_img;
    //我要咨询
    LinearLayout advice_ll;
    //我要去玩
    TextView toPlay_tv;
    //标题栏滑动时透明
    LinearLayout titleBackground_ll;


    //活动详情信息view
    LinearLayout fullInfoView_ll;
    //集合时间栏
    private RelativeLayout layoutGatherTime;
    private ImageView ivGatherTime;
    //集合时间信息
    private TextView tvGatherTime;
    //集合地点栏
    private RelativeLayout layoutLocation;
    //集合地点图标
    private ImageView dot;
    //集合地点信息
    private TextView tvLocation;
    //联系方式
    private TextView tvTel;
    //具体行程
    private RelativeLayout layoutJourney;
    private ImageView ivJourney;
    //具体行程信息
    private TextView tvJourney;
    //装备建议
    private RelativeLayout layoutEquip;
    private ImageView ivEquip;
    //装备建议信息
    private TextView tvEquip;
    //费用说明
    private RelativeLayout layoutPrice;
    private ImageView ivPrice;
    //费用说明信息
    private TextView tvPrice;
    //报名须知
    private RelativeLayout layoutBaomingxvzhi;
    private ImageView ivBaomingxvzhi;
    //报名须知信息
    private LinearLayout tvBaomingxvzhi;
    //免责声明
    private RelativeLayout layoutReadme;
    private ImageView ivReadme;
    //免责声明信息
    private TextView tvReadme;
    //注意事项
    private RelativeLayout layoutSafely;
    private ImageView ivSafely;
    //注意事项信息
    private TextView tvSafely;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityinfo_activity_main);

        initViews();
        baomingxvzhiList = new ArrayList<View>();
        FragmentManager fm;
        fm = getFragmentManager();
        f1 = new ListFragment();
        f1.setmSlideDetailsLayout(mSlideDetailsLayout);
        //在ListFragment中回调， 更新本地的底部详情信息
        f1.setFullInfoInterface(new FullInfoInterface() {
            @Override
            public void initFullInfo(Object datas) {
                model = (ClubTopicActiveSeriesLineMainModel) datas;
                cleanBaomingxvzhi();

                /*集合时间，后期决定去掉，*/
                /*if (!model.settime.equals("")) {
                    tvGatherTime.setText(model.settime);
                }*/
                if (!model.setaddress.equals("")) {
                    tvLocation.setText(model.setaddress);
                }
                if (!model.leadermobile.equals("")) {
                    tvTel.setText(model.leadermobile);
                }
                if (model.tripdetail != null && model.tripdetail.size() != 0) {
                    tvJourney.setText(model.tripdetail.get(0).content);
                }
                if (!model.equipadvise.equals("")) {
                    tvEquip.setText(model.equipadvise);
                }
                if (!model.constdesc.equals("")) {
                    tvPrice.setText(model.constdesc);
                }
                if (!model.disclaimer.equals("")) {
                    tvReadme.setText(model.disclaimer);
                }
                if (model.reportnote != null && model.reportnote.size() > 0) {
                    /*String text = "";
                    for (String s : model.reportnote) {
                        text = text +"· "+ s + "\n";
                    }
                    tvBaomingxvzhi.setText(text);*/
                    addBaomingxvzhiSpots(model.reportnote);
                }
                if (!model.catematter.equals("")) {
                    tvSafely.setText(model.catematter);
                }
            }
        });
        f1.setAlphaInterface(new AlphaInterface() {
            @Override
            public void setTitleAlpha(float alpha) {
                alpha = Math.abs(alpha);
                if (alpha < 500) {
                    titleBackground_ll.setAlpha(alpha / 500);
                    back_img.setImageResource(R.drawable.ls_club_back_icon_bg);
                    share_img.setImageResource(R.drawable.more_banner);
                } else {
                    titleBackground_ll.setAlpha(1.0f);
                    back_img.setImageResource(R.drawable.ls_page_back_icon);
                    share_img.setImageResource(R.drawable.topic_more);

                }
            }
        });
        fm.beginTransaction().replace(R.id.slidedetails_front, f1).commit();
        //    back_img.setOnClickListener(this);
        back_rl.setOnClickListener(this);
        share_img.setOnClickListener(this);
        advice_ll.setOnClickListener(this);
        toPlay_tv.setOnClickListener(this);
        layoutGatherTime.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);
        layoutJourney.setOnClickListener(this);
        layoutEquip.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        layoutReadme.setOnClickListener(this);
        layoutSafely.setOnClickListener(this);
        layoutBaomingxvzhi.setOnClickListener(this);
    }

    /**
     * 展开底部详情view
     *
     * @param smooth 是否平滑
     */
    @Override
    public void openDetails(boolean smooth) {
        mSlideDetailsLayout.smoothOpen(smooth);
    }

    /**
     * 关闭底部详情view
     *
     * @param smooth 是否平滑
     */
    @Override
    public void closeDetails(boolean smooth) {
        mSlideDetailsLayout.smoothClose(smooth);
        //   mSlideDetailsLayout.close=true;

    }


    public void initViews() {
        layoutmain = (RelativeLayout) findViewById(R.id.afullinfo_parent);
        mSlideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slidedetails);
        mSlideDetailsLayout.close = true;
        fullInfoView_ll = (LinearLayout) findViewById(R.id.activityinfo_fullinfo_ll);
        advice_ll = (LinearLayout) findViewById(R.id.afullinfo_zixun_ll);
        toPlay_tv = (TextView) findViewById(R.id.afullinfo_baoming_img);
        back_img = (ImageView) findViewById(R.id.afullinfo_title_back_img);
        back_rl = (RelativeLayout) findViewById(R.id.afullinfo_title_back_area_rl);
        share_img = (ImageView) findViewById(R.id.afullinfo_title_share_img);
        titleBackground_ll = (LinearLayout) findViewById(R.id.title_bg_ll);

        //活动详情view
        layoutGatherTime = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_gather_time);
        ivGatherTime = (ImageView) fullInfoView_ll.findViewById(R.id.iv_gather_time);
        tvGatherTime = (TextView) fullInfoView_ll.findViewById(R.id.tv_gather_time);
        layoutLocation = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_location);
        dot = (ImageView) fullInfoView_ll.findViewById(R.id.dot);
        tvLocation = (TextView) fullInfoView_ll.findViewById(R.id.tv_location);
        tvTel = (TextView) fullInfoView_ll.findViewById(R.id.tv_tel);
        layoutJourney = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_journey);
        ivJourney = (ImageView) fullInfoView_ll.findViewById(R.id.iv_journey);
        tvJourney = (TextView) fullInfoView_ll.findViewById(R.id.tv_journey);
        layoutEquip = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_equip);
        ivEquip = (ImageView) fullInfoView_ll.findViewById(R.id.iv_equip);
        tvEquip = (TextView) fullInfoView_ll.findViewById(R.id.tv_equip);
        layoutPrice = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_price);
        ivPrice = (ImageView) fullInfoView_ll.findViewById(R.id.iv_price);
        tvPrice = (TextView) fullInfoView_ll.findViewById(R.id.tv_price);
        layoutReadme = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_readme);
        ivReadme = (ImageView) fullInfoView_ll.findViewById(R.id.iv_readme);
        tvReadme = (TextView) fullInfoView_ll.findViewById(R.id.tv_readme);
        layoutSafely = (RelativeLayout) fullInfoView_ll.findViewById(R.id.layout_safely);
        ivSafely = (ImageView) fullInfoView_ll.findViewById(R.id.iv_safely);
        tvSafely = (TextView) fullInfoView_ll.findViewById(R.id.tv_safely);
        layoutBaomingxvzhi=(RelativeLayout)fullInfoView_ll.findViewById(R.id.layout_baomingxvzhi);
        ivBaomingxvzhi=(ImageView)fullInfoView_ll.findViewById(R.id.iv_baomingxvzhi);
        tvBaomingxvzhi=(LinearLayout) fullInfoView_ll.findViewById(R.id.tv_baomingxvzhi);

//        tvGatherTime.setVisibility(View.VISIBLE);
        tvJourney.setVisibility(View.VISIBLE);
        tvEquip.setVisibility(View.VISIBLE);
        tvPrice.setVisibility(View.VISIBLE);
        tvReadme.setVisibility(View.VISIBLE);
        tvSafely.setVisibility(View.VISIBLE);
        tvBaomingxvzhi.setVisibility(View.VISIBLE);
    }

    //报名须知，每一条前面加点·
    public void addBaomingxvzhiSpots(List<String> lightspots) {
        for (int i=0;i<lightspots.size();i++) {
            View spot = this.getLayoutInflater().inflate(R.layout
                    .ls_club_topic_list_adapter, null);
            TextView txt = (TextView) spot.findViewById(R.id.tv);
            txt.setText(lightspots.get(i));
            txt.setTextColor(Color.parseColor("#999999"));
            txt.setTextSize(14);
            tvBaomingxvzhi.addView(spot);
            baomingxvzhiList.add(spot);
        }
    }
    //清除报名须知内容，每次刷新时处理
    public void cleanBaomingxvzhi(){
        for (int i=0;i<baomingxvzhiList.size();i++) {
            tvBaomingxvzhi.removeView(baomingxvzhiList.get(i));
        }
        baomingxvzhiList.clear();
    }

    @Override
    public void onClick(View v) {

        if (v == back_rl) {
            Log.i("slide", "clickback:" + mSlideDetailsLayout.close);
            if (mSlideDetailsLayout.close) {
                finish();
            } else {
                closeDetails(true);
                //       mSlideDetailsLayout.close=true;
                //  close=false;
            }
        } else if (v == share_img) {
            ShareManager.getInstance().showPopWindowInShare(model, layoutmain, null);
            super.rightAction();
        } else if (v == advice_ll) {
            Common.telPhone("4006728099");
        } else if (v == toPlay_tv) {
            baoMing();
        } else if (v == layoutGatherTime) {
            showInfo(tvGatherTime, ivGatherTime);
        } else if (v == layoutLocation) {
            if (model != null) {
                //跳转地图
                Intent intent = new Intent(this, LSClubTopicInfoLocation.class);
                Double latitudex = Common.string2Double(model.gaodelatitude);
                Double longtitudex = Common.string2Double(model.gaodlongitude);

                if (latitudex == -1 || longtitudex == -1) {
                    Common.toast("暂时没集合地图位置");
                    return;
                }
                intent.putExtra("latitude", latitudex);
                intent.putExtra("longtitude", longtitudex);
                startActivity(intent);
            }


        } else if (v==layoutJourney) {
            showInfo(tvJourney,ivJourney);
        } else if (v==layoutEquip) {
            showInfo(tvEquip,ivEquip);
        } else if (v==layoutPrice) {
            showInfo(tvPrice,ivPrice);
        } else if (v==layoutReadme) {
            showInfo(tvReadme,ivReadme);
        } else if (v==layoutSafely) {
            showInfo(tvSafely,ivSafely);
        } else if (v==layoutBaomingxvzhi) {
            showInfo(tvBaomingxvzhi,ivBaomingxvzhi);
        } else if (v == layoutJourney) {
            showInfo(tvJourney, ivJourney);
        } else if (v == layoutEquip) {
            showInfo(tvEquip, ivEquip);
        } else if (v == layoutPrice) {
            showInfo(tvPrice, ivPrice);
        } else if (v == layoutReadme) {
            showInfo(tvReadme, ivReadme);
        } else if (v == layoutSafely) {
            showInfo(tvSafely, ivSafely);
        }


    }

    public void showInfo(View tv, ImageView iv) {
        if (tv.getVisibility() == View.VISIBLE) {
            iv.setImageResource(R.drawable.club_info_dot_down);
            tv.setVisibility(View.GONE);
        } else {
            iv.setImageResource(R.drawable.club_info_dot_up);
            tv.setVisibility(View.VISIBLE);
        }
    }

    public void baoMing() {

        if (model == null) {
            return;
        }

        if (Common.isLogin(activity)) {
            Intent intent = new Intent(activity, SericeCalendarActivity.class);
            intent.putExtra("ACTIVITYID", model.activityId);
            intent.putExtra("CLUBID", model.clubId);
            startActivityForResult(intent, 997);
        }



//        activity_id = model.activityId;
//        String userID = DataManager.getInstance().getUser().getUser_id();
//        if (TextUtils.isEmpty(userID)) {
//            Intent intent = new Intent(activity, LSLoginActivity.class);
//            startActivity(intent);
//        } else {
//            getSeriesList();
//        }
    }

    //获取系列活动列表
    private void getSeriesList() {

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

                if (modelBatch == null || modelBatch.batchList == null || modelBatch.batchList
                        .size() == 0)
                    return;

                showBacthList();
            }
        });
    }

    private void showBacthList() {

        PopWindowUtil.showActiveSeriesLine(this, activePosition, toPlay_tv, modelBatch, new
                CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if (mTask == null) {
                    return;
                }

                activePosition = Integer.parseInt(mTask.getresult());

                if (activePosition == -1 || modelBatch == null || modelBatch.batchList == null &&
                        modelBatch.batchList.size() == 0) {
                    return;
                }

                BatchListEntity item = modelBatch.batchList.get
                        (activePosition);

                if (item.isBaoming == 1 || item.isEnd == 1) {
                    return;
                }

                Intent intent = new Intent(activity, LSApplySeriesNew.class);
                intent.putExtra("clubID", model.clubId);
                intent.putExtra("batchID", item.batchId);
                intent.putExtra("topicID", activity_id);
                startActivityForResult(intent, 997);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 报名
        if (resultCode == RESULT_OK && requestCode == 997) {

        }
    }
}
