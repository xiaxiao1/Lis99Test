package com.lis99.mobile.newhome.equip;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.MyScrollView;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.Common;

/**
 * Created by yy on 15/9/23.
 */
public class LSEquipInfoActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView pull_refresh_view;

    private MyScrollView scroll;

    private ListView list_info, list_reply;

    private ImageView iv_title;

    private TextView tv_title, tv_price, tv_info, tv_shop, tv_reply, tv_like;

    private RatingBar ratingBar;

    private ReplayAdapter rAdapter;

    private PropertyAdapter pAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ls_equip_info);

        initViews();

        setTitle("");

    }

    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        list_info = (ListView) findViewById(R.id.list_info);

        list_reply = (ListView) findViewById(R.id.list_reply);

        iv_title = (ImageView) findViewById(R.id.iv_title);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_price = (TextView) findViewById(R.id.tv_price);

        tv_info = (TextView) findViewById(R.id.tv_info);

        tv_shop = (TextView) findViewById(R.id.tv_shop);

        tv_reply = (TextView) findViewById(R.id.tv_reply);

        tv_like = (TextView) findViewById(R.id.tv_like);

        scroll = (MyScrollView) findViewById(R.id.scroll);



    }


    private void getlist ()
    {

    }

    private void cleanlist ()
    {

    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getlist();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanlist();
        getlist();
    }





    // 设置title右边按钮
    private void setTitleRight(boolean isBg)
    {
        if (isBg)
        {
            setRightView(R.drawable.club_share);
        } else
        {
            setRightView(R.drawable.club_share_nul);
        }
    }

    // 设置返回按钮
    private void setBack(boolean isBg)
    {
        if (isBg)
        {
            setLeftView(R.drawable.ls_club_back_icon_bg);
        } else
        {
            setLeftView(R.drawable.ls_page_back_icon);
        }
    }


    MyScrollView.OnScrollListener scrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onBottom() {

        }

        @Override
        public void onTop() {

        }

        @Override
        public void onScroll() {

        }

        @Override
        public void onAutoScroll(int l, int t, int oldl, int oldt) {
            setTitleAlpha(t);
        }
    };

    CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            //透明
            setTitleBarAlpha(0);
            setTitleRight(true);
            setBack(true);
            scroll.setOnScrollListener(scrollListener);
        }
    };

    private float headHeight = Common.dip2px(200);

    /**
     * 设置标题栏透明度
     *
     * @param num
     */
    private void setTitleAlpha(float num)
    {
        if (num > headHeight)
        {
            num = headHeight;
        } else if (num < 0)
        {
            num = 0;
        }
        if (num < headHeight)
        {
            setTitleRight(true);
            setBack(true);
        } else
        {
            setTitleRight(false);
            setBack(false);
        }
        float alpha = num / headHeight;
        setTitleBarAlpha(alpha);
    }

}
