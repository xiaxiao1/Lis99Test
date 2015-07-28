package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ChoicenessAllModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

/**
 * Created by yy on 15/7/23.
 */
public class ChoicenessAllActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private ListView list;
    private PullToRefreshView pull_refresh_view;
    private Page page;
    private ChoicenessAllAdapter adapter;
    private ChoicenessAllModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choiceness_all);

        initViews();

        setTitle("全部专题");


        list = (ListView) findViewById(R.id.list);
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChoicenessAllModel.Omnibuslist item = (ChoicenessAllModel.Omnibuslist) adapter.getItem(i);

                Intent intent = new Intent(activity, SubjectActivity.class);
                intent.putExtra("TITLE", item.title);
                intent.putExtra("ID", item.id);
//                Common.log("item.id=" + item.id);
                startActivity(intent);
            }
        });


        page = new Page();
        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();
    }


    private void cleanList ()
    {
        page = new Page();
        adapter = null;

    }

    private void getList ()
    {
        if ( page.isLastPage())
        {
            return;
        }
        model = new ChoicenessAllModel();
        String url = C.CHOICENESSALL + page.pageNo;

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ChoicenessAllModel) mTask.getResultModel();
                page.pageNo += 1;
                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new ChoicenessAllAdapter(activity, model.omnibuslist);
//                    list.setAdapter(adapter);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(list);
                    list.setAdapter(animationAdapter);

                }
                else
                {
                    adapter.addList(model.omnibuslist);
                }
            }
        });

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
