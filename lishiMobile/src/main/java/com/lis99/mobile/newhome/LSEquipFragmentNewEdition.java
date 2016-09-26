package com.lis99.mobile.newhome;


import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;

import com.lis99.mobile.club.model.WelfareModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;

import com.lis99.mobile.newhome.sysmassage.SysMassageActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

/**
 * create by xiaxiao 2016.09.18.
 * 新版福利社页 4.4.8
 */
public class LSEquipFragmentNewEdition extends LSFragment implements View.OnClickListener,PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {



    //页面列表适配器
    LSWelfareAdapter adapter;
    private PullToRefreshView refreshView;
    private ListView listView;
    private Page page;
    private WelfareModel model;
    String url;
    HashMap<String,Object> map=new HashMap<String,Object>();
    //组装后的福利信息数据
    List<Object> welfares=new ArrayList<Object>();

    public LSEquipFragmentNewEdition() {
        // Required empty public constructor
    }


    @Override
    protected void leftAction() {
        super.leftAction();
        if ( Common.isLogin(getActivity()))
        startActivity(new Intent(getActivity(), SysMassageActivity.class));

    }

    @Override
    protected void initViews(ViewGroup container) {
//        super.initViews(container);
        page=new Page();

        getDatas();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.fragment_lsequip_new_edition, container, false);
        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        view.onFooterRefreshComplete();
        getDatas();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        view.onHeaderRefreshComplete();
        clearDatas();
        getDatas();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 清理数据
     */
    public void clearDatas(){
        welfares.clear();
       page.setPageNo(0);
        listView.setAdapter(null);
        adapter=null;
    }

    /**
     * 获取数据
     */
    public void getDatas(){
        if ( page.isLastPage())
        {
            return;
        }

        String url = C.FULISHE+page.getPageNo();

        model = new WelfareModel();

        MyRequestManager.getInstance().requestPost(url,map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (WelfareModel) mTask.getResultModel();
                if (model == null) {
                    return;
                }

                page.nextPage();
                if ( adapter == null ) {
                    page.setPageSize(model.getTotPage());

                    //页面包含福利和积分兑换两部分数据
                    //添加所有的免费福利数据
                    welfares.addAll(model.getFreegoods());
                    //将积分兑换数据添加到指定位置
                    welfares.add(1, model.getJfgoods());
                    adapter=new LSWelfareAdapter(LSEquipFragmentNewEdition.this.getContext(),welfares);
                    listView.setAdapter(adapter);
                }
                else
                {
                    //                    最后一页
                    //积分兑换部分待定，是否要也刷新
                    welfares.addAll(model.getFreegoods());
                }

                adapter.notifyDataSetChanged();

            }
        });
    }
}
