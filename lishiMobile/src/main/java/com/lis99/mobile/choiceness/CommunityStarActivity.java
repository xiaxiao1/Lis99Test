package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.choiceness.adapter.CommunityStarInfoAdapter;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.CommunityInfoModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 16/6/28.
 */
public class CommunityStarActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private CommunityStarInfoAdapter adapter;

    private CommunityInfoModel model;

    private PullToRefreshView pull_refresh_view;

    private ListView listView;

    private Page page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.community_star_main);

        initViews();

        setTitle("社区明星");

        onHeaderRefresh(pull_refresh_view);

    }


    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

    }


    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<>();

        map.put("user_id", TextUtils.isEmpty(userId) ? "0" : userId);

        String url = C.COMMUNITY_STAR_INFO_LIST + page.getPageNo();

        model = new CommunityInfoModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (CommunityInfoModel) mTask.getResultModel();
                page.nextPage();
                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new CommunityStarInfoAdapter(activity, model.lists);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if ( model == null || model.lists == null || model.lists.size() == 0 ) return;
                             String userid = ""+model.lists.get(position).userId;
                            if ( TextUtils.isEmpty(userid))
                            {
                                return;
                            }

                            Common.goUserHomeActivit(activity, userid);
                        }
                    });

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
        listView.setAdapter(null);
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
