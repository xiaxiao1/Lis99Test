package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAllActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private LinearLayout layout_tab_data, layout_tab_city;
    private TextView tv_data, tv_city;
    private ImageView iv_data, iv_city;
    private PullToRefreshView pull_refresh_view;
    private ListView list;
    private Page page;

    private ActiveAllAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_all);


        initViews();

        setTitle("全部活动");

        page = new Page();

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        layout_tab_data = (LinearLayout) findViewById(R.id.layout_tab_data);
        layout_tab_city = (LinearLayout) findViewById(R.id.layout_tab_city);

        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_city = (TextView) findViewById(R.id.tv_city);

        iv_data = (ImageView) findViewById(R.id.iv_data);
        iv_city = (ImageView) findViewById(R.id.iv_city);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }

    private void cleanList ()
    {
        page = new Page();
        if ( adapter != null )
        {
            adapter.clean();
            adapter = null;
        }
    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }



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
