package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ClubTopicActiveLineMainModel;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.util.C;
import com.lis99.mobile.view.MyListView;

/**
 * Created by yy on 16/1/8.
 *
 *      新的线下活动帖
 *
 */
public class LSClubTopicActiveOffLine extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener
{


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

    private LSClubTopicInfoAdapter highlightsAdapter, joinAdapter;

    private ClubTopicActiveLineMainModel model;

    private int activity_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lsclub_active_offline_new);

        activity_id = getIntent().getIntExtra("topicID", 0);

        initViews();

        initialize();
    }



    private void getInfo ()
    {
        String url = C.CLUB_TOPIC_ACTIVE_LINE_MIAN;

        String UserId = DataManager.getInstance().getUser().getUser_id();



    }

    private void cleanInfo ()
    {

    }










    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

    }

    @Override
    public void onClick(int index) {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Intent i = null;
        switch (arg0.getId())
        {
//            报名
            case R.id.btn_ok:
                break;
//            查看详情
            case R.id.btn_info:
                break;
//            查看活动行程
            case R.id.btn_travel:
                break;
//            目的地
            case R.id.btn_destination:
                break;
        }
    }

    private void initialize() {

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

        btnok.setOnClickListener(this);
        btninfo.setOnClickListener(this);
        btntravel.setOnClickListener(this);
        btndestination.setOnClickListener(this);

    }
}
