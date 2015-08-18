package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSClubFragmentAdapter;
import com.lis99.mobile.entry.ActivityPattern1;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsActivity extends ActivityPattern1 implements ViewPager.OnPageChangeListener{


    private ViewPager viewPager;
//    private FragmentLevel fLevelClub, fLevelLeader;

    private MyFriendsAttention attentionFragment;
    private MyFriendsFans fansFragment;
    private MyFriendsRecommend recommendFragment;

    private ArrayList<Fragment> fList = new ArrayList<Fragment>();
    private LSClubFragmentAdapter adapter;

    private Button btn_attention, btn_fans, btn_recommend;

    private int currentId;

    private RelativeLayout titleLeft;

    private TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_friends_main);

        initViews();

//        setTitle("我的好友");
        title.setText("我的好友");




    }

    protected void initViews() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        btn_attention = (Button) findViewById(R.id.btn_attention);

        btn_fans = (Button) findViewById(R.id.btn_fans);

        btn_recommend = (Button) findViewById(R.id.btn_recommend);

        titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);

        title = (TextView) findViewById(R.id.title);

        currentId = R.id.btn_fans;

        titleLeft.setOnClickListener(this);

        btn_attention.setOnClickListener(this);
        btn_fans.setOnClickListener(this);
        btn_recommend.setOnClickListener(this);


        viewPager.setCurrentItem(3);

        attentionFragment = new MyFriendsAttention();
        fList.add(attentionFragment);
        fansFragment = new MyFriendsFans();
        fList.add(fansFragment);
        recommendFragment = new MyFriendsRecommend();
        fList.add(recommendFragment);

        adapter = new LSClubFragmentAdapter(getSupportFragmentManager(), fList);

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(this);

    }


    @Override
    public void onClick(View arg0) {
        if ( currentId == arg0.getId() ) return;
        currentId = arg0.getId();
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.btn_recommend:
                clickRecommend();
                viewPager.setCurrentItem(2);
                break;
            case R.id.btn_fans:
                clickFans();
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_attention:
                clickAttention();
                viewPager.setCurrentItem(0);
                break;
            case R.id.titleLeft:
                finish();
                break;
        }
    }

    private void clickRecommend ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_unselect);
        btn_fans.setBackgroundResource(R.drawable.friends_title_center_unselect);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_select);

        btn_attention.setTextColor(getResources().getColor(R.color.text_color_blue));
        btn_fans.setTextColor(getResources().getColor(R.color.text_color_blue));
        btn_recommend.setTextColor(getResources().getColor(R.color.white));
    }

    private void clickFans ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_unselect);
        btn_fans.setBackgroundResource(R.color.text_color_blue);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_unselect);

        btn_attention.setTextColor(getResources().getColor(R.color.text_color_blue));
        btn_fans.setTextColor(getResources().getColor(R.color.white));
        btn_recommend.setTextColor(getResources().getColor(R.color.text_color_blue));
    }

    private void clickAttention ()
    {
        btn_attention.setBackgroundResource(R.drawable.friends_title_left_select);
        btn_fans.setBackgroundResource(R.drawable.friends_title_center_unselect);
        btn_recommend.setBackgroundResource(R.drawable.friends_title_right_unselect);

        btn_attention.setTextColor(getResources().getColor(R.color.white));
        btn_fans.setTextColor(getResources().getColor(R.color.text_color_blue));
        btn_recommend.setTextColor(getResources().getColor(R.color.text_color_blue));
    }


    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        switch (position)
        {
            case 0:
                clickAttention();
                break;
            case 1:
                clickFans();
                break;
            case 2:
                clickRecommend();
                break;
        }

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
