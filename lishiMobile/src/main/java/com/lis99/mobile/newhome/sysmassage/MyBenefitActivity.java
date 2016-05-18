package com.lis99.mobile.newhome.sysmassage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.Page;

import java.util.List;

/**
 * Created by yy on 16/5/18.
 *      我的福利
 */
public class MyBenefitActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView refreshView;
    private ListView listView;
    private Page page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_benefit);

        initViews();

        setTitle("我的福利");

        onHeaderRefresh(refreshView);


    }

    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);



    }

    private void getList ()
    {

    }

    private void cleanList ()
    {
        page = new Page();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    class MyBenefitAdapter extends MyBaseAdapter
    {

        public MyBenefitAdapter(Context c, List listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {

            if ( view == null )
            {
                view = View.inflate(activity, R.layout.my_benefit_adapter, null);
            }



            return view;
        }
    }




}
