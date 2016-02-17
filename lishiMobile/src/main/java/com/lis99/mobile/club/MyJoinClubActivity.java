package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.MyJoinAdapter;
import com.lis99.mobile.club.model.MyJoinClubModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/10/23.
 */
public class MyJoinClubActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener {

    private PullToRefreshView pull_refresh_view;
    private ListView list;
    private MyJoinAdapter adapter;

    private MyJoinClubModel model;

    private Page page;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_my_join);

        initViews();

        setTitle("我加入的俱乐部");

//        setRightView("全部俱乐部");

//        setRightViewColor(getResources().getColor(R.color.ff7800));

        page = new Page();

        getList();

    }

//    @Override
//    protected void rightAction() {
//        super.rightAction();
//        startActivity(new Intent(activity, LSClubListActivity.class));
//    }

    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        list = (ListView) findViewById(R.id.list);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);



    }

    private void getList ()
    {
        if ( page.isLastPage())
        {
            return;
        }

        if (!Common.isLogin(this))
        {
            return;
        }



        String url = C.MY_JOIN_CLUB_LIST + page.getPageNo();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", DataManager.getInstance().getUser().getUser_id());

        model = new MyJoinClubModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (MyJoinClubModel) mTask.getResultModel();

                page.nextPage();

                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new MyJoinAdapter(activity, model.clublist);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            MyJoinClubModel.Clublist item = (MyJoinClubModel.Clublist) adapter.getItem(i);
                            Intent intent = new Intent(activity, LSClubDetailActivity.class);
                            intent.putExtra("clubID", item.id);
                            startActivity(intent);

                        }
                    });

                }
                else
                {
                    adapter.addList(model.clublist);
                }

            }
        });

    }

    private void cleanList ()
    {
        adapter = null;
        page = new Page();
        list.setAdapter(null);
        list.setOnItemClickListener(null);
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

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }
}
