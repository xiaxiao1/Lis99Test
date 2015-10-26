package com.lis99.mobile.club;

import android.os.Bundle;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.MyJoinAdapter;
import com.lis99.mobile.club.model.MyJoinClubModel;
import com.lis99.mobile.entry.view.PullToRefreshView;

/**
 * Created by yy on 15/10/23.
 */
public class MyJoinClubActivity extends LSBaseActivity {

    private PullToRefreshView pull_refresh_view;
    private ListView list;
    private MyJoinAdapter adapter;

    private MyJoinClubModel model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_my_join);

        initViews();


    }


    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        list = (ListView) findViewById(R.id.list);



    }
}
