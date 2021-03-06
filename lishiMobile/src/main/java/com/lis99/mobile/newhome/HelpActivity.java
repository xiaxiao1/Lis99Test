package com.lis99.mobile.newhome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.PushManager;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;

import java.util.ArrayList;


public class HelpActivity extends Activity {

    private ViewPager mViewPager;
    private LinearLayout scrollbtn;

    private Button startbtn;

    // 每个页面的view数据
    private ArrayList<View> views;
    private ArrayList<ImageView> dot;
    private View currentView;
    private int currentPosition;

    private int[] is = new int[] { R.drawable.help1, R.drawable.help2,
            R.drawable.help3, R.drawable.help4, R.drawable.help5 };

    private int[] dots = new int[]{
            R.drawable.ico_s,
            R.drawable.ico_n,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息
        setContentView(R.layout.help);


        StatusUtil.setStatusBarDefult(this);

        mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        startbtn = (Button) findViewById(R.id.startbtn);

        startbtn.setVisibility(View.GONE);

        scrollbtn = (LinearLayout) findViewById(R.id.scrollbtn);
        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);

        views = new ArrayList<View>();
        dot = new ArrayList<ImageView>();
        for ( int i = 0; i < is.length; i++ )
        {
            View view = new View(this);
            view.setBackgroundResource(is[i]);
            views.add(view);

            ImageView d = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = Common.dip2px(10);
            d.setLayoutParams(lp);
            if ( i == 0 )
            {
                d.setImageResource(dots[0]);
                currentView = d;
                currentPosition = 0;
            }
            else
            {
                d.setImageResource(dots[1]);
            }

            dot.add(d);
            scrollbtn.addView(d);

        }
        setRitght2Left();
        mViewPager.setAdapter(mPagerAdapter);

    }

    private float currentX = -1;
    private float moveX = -1;
//    设置滑动到最后一张图片， 继续滑动进入主页
    private void setRitght2Left ()
    {
        if ( views == null ) return;
        View last = views.get(views.size() - 1);

        last.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        currentX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX = event.getX();
                        if ( moveX > currentX )
                        {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if ( moveX < currentX )
                        {
//                            跳转
                            startbutton(startbtn);
                        }
                        else
                        {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                }

                return true;
            }
        });
    }

    // 填充ViewPager的数据适配器
    PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }
    };

    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {

            if (arg0 == dot.size() - 1)
            {
                startbtn.setVisibility(View.VISIBLE);
                scrollbtn.setVisibility(View.GONE);
            }
            else
            {
                startbtn.setVisibility(View.GONE);
                scrollbtn.setVisibility(View.VISIBLE);
            }

            if ( currentPosition != arg0 )
            {
                dot.get(currentPosition).setImageResource(dots[1]);
                dot.get(arg0).setImageResource(dots[0]);
                currentPosition = arg0;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public void startbutton(View v) {

            Intent intent = new Intent();
        intent.setClass(HelpActivity.this, NewHomeActivity.class);

        intent.putExtra(PushManager.TAG, PushManager.getInstance().getPushModel(HelpActivity.this.getIntent()));

        startActivity(intent);
        SharedPreferencesHelper.saveHelp("help");
        this.finish();
    }

}
