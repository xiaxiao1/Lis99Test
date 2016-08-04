package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.filter.FilterAdapter;
import com.lis99.mobile.club.filter.model.NearbyListMainModel;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.activeline.LSActiveLineNewAdapter;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xx on 2016/7/25.
 */
public class LSRecommendActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    //接收返回的推荐活动列表
    private List<Object> recommendList=new ArrayList<Object>();
    private Page page;
    private ListView listview;
    private PullToRefreshView refreshView;
    int is_tagid;
    HashMap<String,Object> map=new HashMap<String,Object>();

    //复用 本地活动 的adapter和model和listview的item
    private FilterAdapter adapter;
    //
    private NearbyListMainModel model;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_club_recommendedactivity);
        initViews();
        page=new Page();
        //获取intent传递过来的参数值
        is_tagid=getIntent().getIntExtra("is_tagid",-1);
        map.put("tag_id", is_tagid);
        onHeaderRefresh(refreshView);
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

    @Override
    protected void initViews() {
        super.initViews();
        setTitle("说不定您喜欢");
        refreshView=(PullToRefreshView)findViewById(R.id.recommended_activity_pull_refresh_view);
        refreshView.setOnFooterRefreshListener(this);
        refreshView.setOnHeaderRefreshListener(this);
        listview = (ListView) findViewById(R.id.recommended_activity_listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (model != null) {
                    NearbyListMainModel.ListsEntity active= model.lists.get(position);
                    Log.i("xx",""+active.activityCode);
                    Common.goTopic(activity, 4, Common.string2int(active.id));
                }
            }
        });
    }

    public void getList(){

        if ( page.isLastPage())
        {
            return;
        }

        String url = C.NEARBY_MAIN_LIST_FILTER+page.getPageNo();

        model = new NearbyListMainModel();

        MyRequestManager.getInstance().requestPost(url,map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (NearbyListMainModel) mTask.getResultModel();

                if (model == null) {

                    return;
                }

                page.nextPage();
                if ( adapter == null ) {
                    page.setPageSize(model.totalpage);

                    adapter = new FilterAdapter(LSRecommendActivity.this, model.lists);
                    listview.setAdapter(adapter);

                    Log.i("xx",model.lists.size()+"    mei you shu ju ");
                }
                else
                {
                    //                    最后一页
                    adapter.addList(model.lists);
                }



            }
        });
    }

    private void cleanList ()
    {
//        page = new Page();
        listview.setAdapter(null);
        adapter = null;
    }
}
