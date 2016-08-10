package com.lis99.mobile.club.activityinfo;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;


public class ActivityFullInfoActivity extends LSBaseActivity implements ISlideCallback,View.OnClickListener{

    //负责实现UI上拉加载显示详情
    private SlideDetailsLayout mSlideDetailsLayout;
    //显示主要页面内容
    ListFragment f1;
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
        FragmentManager fm ;
        fm=getFragmentManager();
        f1=new ListFragment();
        fm.beginTransaction().replace(R.id.slidedetails_front, f1).commit();
        back_img.setOnClickListener(this);
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
    }

    /**
     * 展开底部详情view
     * @param smooth 是否平滑
     */
    @Override
    public void openDetails(boolean smooth) {
        mSlideDetailsLayout.smoothOpen(smooth);
    }

    /**
     * 关闭底部详情view
     * @param smooth  是否平滑
     */
    @Override
    public void closeDetails(boolean smooth) {
        mSlideDetailsLayout.smoothClose(smooth);
    }


    public void initViews(){
        mSlideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slidedetails);
        fullInfoView_ll = (LinearLayout) findViewById(R.id.activityinfo_fullinfo_ll);
        advice_ll = (LinearLayout) findViewById(R.id.afullinfo_zixun_ll);
        toPlay_tv = (TextView) findViewById(R.id.afullinfo_baoming_img);
        back_img = (ImageView) findViewById(R.id.afullinfo_title_back_img);
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

        tvGatherTime.setVisibility(View.VISIBLE);
        tvJourney.setVisibility(View.VISIBLE);
        tvEquip.setVisibility(View.GONE);
        tvPrice.setVisibility(View.GONE);
        tvReadme.setVisibility(View.GONE);
        tvSafely.setVisibility(View.GONE);
    }



    @Override
    public void onClick(View v) {

        if (v == back_img) {
            closeDetails(true);
        } else if (v == share_img) {
            Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
        } else if (v==advice_ll) {
            Toast.makeText(this,"advice",Toast.LENGTH_SHORT).show();
        } else if (v==toPlay_tv) {
            Toast.makeText(this,"toplay",Toast.LENGTH_SHORT).show();
        } else if (v==layoutGatherTime) {
            showInfo(tvGatherTime,ivGatherTime);
        } else if (v==layoutLocation) {
            Toast.makeText(this, "go location ", Toast.LENGTH_SHORT).show();
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
        }


    }

    public void showInfo(TextView tv, ImageView iv) {
        if ( tv.getVisibility() == View.VISIBLE )
        {
            iv.setImageResource(R.drawable.club_info_dot_down);
            tv.setVisibility(View.GONE);
        }
        else
        {
            iv.setImageResource(R.drawable.club_info_dot_up);
            tv.setVisibility(View.VISIBLE);
        }
    }
}
