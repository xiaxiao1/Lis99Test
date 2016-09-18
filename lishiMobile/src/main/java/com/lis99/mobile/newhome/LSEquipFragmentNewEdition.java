package com.lis99.mobile.newhome;


import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.lis99.mobile.R;

import com.lis99.mobile.club.filter.FilterAdapter;
import com.lis99.mobile.club.filter.model.NearbyListMainModel;
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
 * create by xiaxiao.
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
    //福利信息数据
    List<Object> welfares=new ArrayList<Object>();
//    List<LSEquipContent> loadedContents = new ArrayList<LSEquipContent>();

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

        getDatas("asasas");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.fragment_lsequip_new_edition, container, false);
        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

               /* LSEquipContent content = loadedContents.get(position);
                if (content.getType() == LSEquipContent.CHANGE_FOOTER) {
                    Intent intent = new Intent(getActivity(),LSWebActivity.class);
                    intent.putExtra("url", "http://m.lis99.com/club/integralshop/goodList");
                    startActivity(intent);
                }*/
                Common.toast("haha"+welfares.get(position));


            }
        });
       /* adapter = new LSWelfareAdapter(this.getActivity(), welfares);
        listView.setAdapter(adapter);*/
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        view.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
     //   getEquipContents();
        view.onHeaderRefreshComplete();
        clearDatas();
        getDatas(System.currentTimeMillis()+"");
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public void clearDatas(){
        welfares.clear();
        adapter=null;
    }
    //测试数据
    public void initdatas(String s){
        for (int i=0;i<20;i++) {
            welfares.add(i + " 福利"+s);
        }
        //测试数据
        List<String> goods = new ArrayList<>();
        for (int i=0;i<10;i++) {
            goods.add(s+"积分兑换 " + i);
        }
        welfares.add(1,goods);
    }
    public void getDatas(String s){
        getList();
    }

    public void getList(){

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
                    welfares.addAll(model.getFreegoods());
                    welfares.add(1, model.getJfgoods());
                    adapter=new LSWelfareAdapter(LSEquipFragmentNewEdition.this.getContext(),welfares);
                    listView.setAdapter(adapter);
                }
                else
                {
                    //                    最后一页
                    welfares.addAll(model.getFreegoods());
                }

                adapter.notifyDataSetChanged();

            }
        });
    }
}
