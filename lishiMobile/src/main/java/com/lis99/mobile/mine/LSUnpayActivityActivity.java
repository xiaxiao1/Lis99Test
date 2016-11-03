package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.mine.adapter.LSMyActivityAdapter;
import com.lis99.mobile.mine.model.LSMyActivity;
import com.lis99.mobile.mine.model.LSMyActivityListModel;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

public class LSUnpayActivityActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {


    private PullToRefreshView pull_refresh_view;
    private ListView list;
    private LSMyActivityAdapter adapter;

    private LSMyActivityListModel model;

    private Page page;
    View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsunpay_activity);
        initViews();
        setTitle("待支付的活动");
        page = new Page();
        getList();
    }

    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        list = (ListView) findViewById(R.id.list);
//        list.setEmptyView(findViewById(R.id.show_when_empty));
        emptyView = (View) findViewById(R.id.empty_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        View v = getLayoutInflater().inflate(R.layout.ls_common_list_header, null);
        list.addHeaderView(v);
    }

    private void getList() {
        if (page.isLastPage()) {
            return;
        }

        if (!Common.isLogin(this)) {
            return;
        }


        String url = C.MY_ACTIVITY_WAITPAY_LIST + page.getPageNo();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", DataManager.getInstance().getUser().getUser_id());

        model = new LSMyActivityListModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                mTask.setShowErrorTost(false);
                model = (LSMyActivityListModel)mTask.getResultModel();
                if (model==null) {
                    Common.toast("ai aiyou ewei");
                }
                page.nextPage();

                if (adapter == null) {
                    page.setPageSize(model.totalpage);
                    adapter = new LSMyActivityAdapter(activity, model.lists);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            LSMyActivity item = (LSMyActivity) model.lists.get(i - 1);
                            Intent intent = new Intent(LSUnpayActivityActivity.this, LSMyActivityDetailActivity.class);
                            intent.putExtra("orderID", item.orderid);
//                            startActivity(intent);
                            startActivityForResult(intent, 999);
                        }
                    });

                } else {
                    adapter.addList(model.lists);
                }
                Common.showEmptyView(LSUnpayActivityActivity.this,R.id.empty_view,model.lists);
            }
        });

    }

    private void cleanList() {
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
        Common.showEmptyView(this,R.id.empty_view,null);
        cleanList();
        getList();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK && requestCode == 999 )
        {
            onHeaderRefresh(pull_refresh_view);
        }
    }
}


