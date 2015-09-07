package com.lis99.mobile.newhome.sysmassage;

import android.os.Bundle;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/8/28.
 */
public class SysMassageActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private ListView list;
    private PullToRefreshView pull_refresh_view;
    private Page page;
    private SysMassageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sys_massage);

        initViews();

        setTitle("系统消息");

        setRightView("清空");

        page = new Page();

        getList();

    }

    @Override
    protected void initViews() {

        super.initViews();

        list = (ListView) findViewById(R.id.list);
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);


    }

    @Override
    protected void rightAction() {
        super.rightAction();


    }

    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
    }

    private void getList ()
    {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanList();
        getList();
    }
}
