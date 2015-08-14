package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.adapter.LSClubFragmentAdapter;
import com.lis99.mobile.club.widget.FragmentLevel;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsActivity extends LSBaseActivity {


    private ViewPager viewPager;
    private FragmentLevel fLevelClub, fLevelLeader;
    private ArrayList<Fragment> fList = new ArrayList<Fragment>();
    private LSClubFragmentAdapter adapter;

    private Button btn_attention, btn_fans, btn_recommend;

    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_friends_main);

        initViews();

        setTitle("我的好友");



    }

    @Override
    protected void initViews() {
        super.initViews();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        btn_attention = (Button) findViewById(R.id.btn_attention);

        btn_fans = (Button) findViewById(R.id.btn_fans);

        btn_recommend = (Button) findViewById(R.id.btn_recommend);


        currentId = R.id.btn_fans;

        btn_attention.setOnClickListener(this);
        btn_fans.setOnClickListener(this);
        btn_recommend.setOnClickListener(this);



    }


    @Override
    public void onClick(View arg0) {
        if ( currentId == arg0.getId() ) return;
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.btn_recommend:
                clickRecommend();
                break;
            case R.id.btn_fans:
                clickFans();
                break;
            case R.id.btn_attention:
                clickAttention();
                break;
        }
    }

    private void clickRecommend ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_unselect);
        btn_fans.setBackgroundResource(R.drawable.friends_title_center_unselect);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_select);
    }

    private void clickFans ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_unselect);
        btn_fans.setBackgroundResource(R.color.text_color_blue);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_unselect);
    }

    private void clickAttention ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_select);
        btn_fans.setBackgroundResource(R.drawable.friends_title_center_unselect);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_unselect);
    }


}
