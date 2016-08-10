package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.MyJoinActiveModel;
import com.lis99.mobile.club.widget.applywidget.MyJoinActiveItem;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.mine.LSMyActivityDetailActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveActivity extends LSBaseActivity implements com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener
{

    private PullToRefreshView pull_refresh_view;

    private ListView list;

    private MyJoinActiveItem adapter;

    private Page page;

    private MyJoinActiveModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_jion_active_main);

        initViews();

        setTitle("我报名的活动");

        page = new Page();

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        list = (ListView) findViewById(R.id.list);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null ) return;
                MyJoinActiveModel.Lists item = (MyJoinActiveModel.Lists) adapter.getItem(i);
                if ( item == null ) return;
//                Intent intent = new Intent(activity, MyJoinActiveInfoActivity.class);
//                intent.putExtra("ORDERID", item.id);
//                startActivity(intent);
                Intent intent = new Intent(activity, LSMyActivityDetailActivity.class);
                intent.putExtra("orderID", item.id);
                startActivity(intent);
            }
        });

    }


    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        String url = C.MY_APPLY_LIST + page.pageNo;

        String userId = DataManager.getInstance().getUser().getUser_id();

        model = new MyJoinActiveModel();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (MyJoinActiveModel) mTask.getResultModel();
                if ( model == null )
                {
                    return;
                }

                page.nextPage();

                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);

                    adapter = new MyJoinActiveItem(activity, model.lists);

                    list.setAdapter(adapter);

                }
                else
                {
                    adapter.addList(model.lists);
                }

            }
        });


    }

    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        if ( adapter != null )
            adapter.clean();
        adapter = null;
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
