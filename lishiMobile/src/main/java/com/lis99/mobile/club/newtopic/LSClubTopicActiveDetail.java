package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicInfoLocation;
import com.lis99.mobile.club.model.ClubTopicActiveLineMainModel;
import com.lis99.mobile.util.Common;

/**
 * Created by yy on 16/1/12.
 */
public class LSClubTopicActiveDetail extends LSBaseActivity {


    private Button btnlf;
    private Button btnrg;
    private ListView list_info;
    private TextView tvdate;
    private ImageView dot;
    private TextView tvlocation;
    private TextView tvtel;
    private ImageView ivjourney;
    private TextView tvjourney;
    private TextView tvjoinNum;
    private ImageView ivequip;
    private TextView tvequip;
    private ImageView ivprice;
    private TextView tvprice;
    private ImageView ivreadme;
    private TextView tvreadme;
    private ImageView ivsafely;
    private TextView tvsafely;
    private RelativeLayout layoutlocation;
    private RelativeLayout layoutjourney;
    private RelativeLayout layoutequip;
    private RelativeLayout layoutprice;
    private RelativeLayout layoutreadme;
    private RelativeLayout layoutsafely;

    private LSClubTopicDetailAdapter adapter;

    private ClubTopicActiveLineMainModel model;

    private View head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_topic_info_detail);

        initViews();


        initialize();

        setTitle("详细信息");

        model = (ClubTopicActiveLineMainModel) getIntent().getSerializableExtra("MODEL");

        String type = getIntent().getStringExtra("TYPE");

        getInfo();

        if ( "0".equals(type))
        {
            lfBtn();
        }
        else {
            rgBtn();
        }


    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Intent i = null;
        switch (arg0.getId())
        {
            case R.id.btn_lf:
                lfBtn();
                break;
            case R.id.btn_rg:
                rgBtn();
                break;
//            集合地点
            case R.id.layout_location:
                toMap();
                break;
//            具体行程
            case R.id.layout_journey:
                visibleView(ivjourney, tvjourney);
                break;
//            装备建议
            case R.id.layout_equip:
                visibleView(ivequip, tvequip);
                break;
//            费用说明
            case R.id.layout_price:
                visibleView(ivprice, tvprice);
                break;
//            免责声明
            case R.id.layout_readme:
                visibleView(ivreadme, tvreadme);
                break;
//            注意事项
            case R.id.layout_safely:
                visibleView(ivsafely, tvsafely);
                break;
        }


    }

    private void visibleView ( ImageView iv, TextView tv )
    {
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



    private void lfBtn ()
    {
        btnlf.setTextColor(getResources().getColor(R.color.text_color_green));
        btnrg.setTextColor(getResources().getColor(R.color.color_nine));

        list_info.setAdapter(null);
        list_info.removeHeaderView(head);
        list_info.setAdapter(adapter);
    }

    private void rgBtn ()
    {
        btnrg.setTextColor(getResources().getColor(R.color.text_color_green));
        btnlf.setTextColor(getResources().getColor(R.color.color_nine));
        list_info.setAdapter(null);
        list_info.addHeaderView(head);
        list_info.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list_info.setAdapter(null);
    }

    private void getInfo ()
    {
        adapter = new LSClubTopicDetailAdapter(activity, model.getActivitydetail());

//        list_info.addHeaderView(head);
//        list_info.setAdapter(adapter);

        tvdate.setText(model.getSettime());

        tvlocation.setText(model.getSetaddress());

        if ( model.applyStatus == 1 )
        tvtel.setText(model.getLeadermobile());
        else
        tvtel.setText("报名后可见");

        if ( model.getTripdetail() != null && model.getTripdetail().size() != 0 )
        tvjourney.setText(model.getTripdetail().get(0).getContent());

        tvjoinNum.setText(model.getActivenum()+"人");

        tvequip.setText(model.getEquipadvise());

        tvprice.setText(model.getConstdesc());

        tvreadme.setText(model.getDisclaimer());

        tvsafely.setText(model.getCatematter());



    }

    private void cleanInfo ()
    {

    }

    private void initialize() {

        btnlf = (Button) findViewById(R.id.btn_lf);
        btnrg = (Button) findViewById(R.id.btn_rg);
        list_info = (ListView) findViewById(R.id.list_info);

        head = View.inflate(activity, R.layout.club_topic_info_detail_route, null);

        tvdate = (TextView) head.findViewById(R.id.tv_date);
        dot = (ImageView) head.findViewById(R.id.dot);
        tvlocation = (TextView) head.findViewById(R.id.tv_location);
        tvtel = (TextView) head.findViewById(R.id.tv_tel);
        ivjourney = (ImageView) head.findViewById(R.id.iv_journey);
        tvjourney = (TextView) head.findViewById(R.id.tv_journey);
        tvjoinNum = (TextView) head.findViewById(R.id.tv_join_Num);
        ivequip = (ImageView) head.findViewById(R.id.iv_equip);
        tvequip = (TextView) head.findViewById(R.id.tv_equip);
        ivprice = (ImageView) head.findViewById(R.id.iv_price);
        tvprice = (TextView) head.findViewById(R.id.tv_price);
        ivreadme = (ImageView) head.findViewById(R.id.iv_readme);
        tvreadme = (TextView) head.findViewById(R.id.tv_readme);
        ivsafely = (ImageView) head.findViewById(R.id.iv_safely);
        tvsafely = (TextView) head.findViewById(R.id.tv_safely);
        layoutlocation = (RelativeLayout) head.findViewById(R.id.layout_location);
        layoutjourney = (RelativeLayout) head.findViewById(R.id.layout_journey);
        layoutequip = (RelativeLayout) head.findViewById(R.id.layout_equip);
        layoutprice = (RelativeLayout) head.findViewById(R.id.layout_price);
        layoutreadme = (RelativeLayout) head.findViewById(R.id.layout_readme);
        layoutsafely = (RelativeLayout) head.findViewById(R.id.layout_safely);

        btnlf.setOnClickListener(this);
        btnrg.setOnClickListener(this);

        layoutjourney.setOnClickListener(this);
        layoutlocation.setOnClickListener(this);
        layoutequip.setOnClickListener(this);
        layoutprice.setOnClickListener(this);
        layoutreadme.setOnClickListener(this);
        layoutsafely.setOnClickListener(this);

        tvjourney.setVisibility(View.VISIBLE);
        tvequip.setVisibility(View.GONE);
        tvprice.setVisibility(View.GONE);
        tvreadme.setVisibility(View.GONE);
        tvsafely.setVisibility(View.GONE);

    }

    private void toMap ()
    {
        //跳转地图
        Intent intent = new Intent(activity, LSClubTopicInfoLocation.class);
        Double latitude = Common.string2Double(model.getGaodelatitude());
        Double longtitude = Common.string2Double(model.getGaodlongitude());
        if ( latitude == -1 || longtitude == -1 )
        {
            Common.toast("暂时没集合地图位置");
            return;
        }
        intent.putExtra("latitude", latitude);
        intent.putExtra("longtitude", longtitude);
        startActivity(intent);
    }


}
