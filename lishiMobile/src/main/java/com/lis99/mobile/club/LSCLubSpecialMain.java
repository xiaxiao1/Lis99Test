package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.LSClubSpecialMainAdapter;
import com.lis99.mobile.club.model.LSClubSpecialList;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

/**
 * Created by yy on 15/8/6.
 */
public class LSCLubSpecialMain extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {


    private PullToRefreshView pull_refresh_view;

    private ListView listView;

    private Page page;

    private LSClubSpecialMainAdapter adapter;

    private LSClubSpecialList model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ls_club_special_main);

        initViews();

        setTitle("金牌专栏");

        page = new Page();

        getList();

    }


    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        listView = (ListView) findViewById(R.id.listView);

        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if( adapter == null ) return;
                LSClubSpecialList.Taglist info = (LSClubSpecialList.Taglist) adapter.getItem(i);

                Intent intent = new Intent(activity, ClubSpecialListActivity.class);
                intent.putExtra("tagid", info.id);
                startActivity( intent );
            }
        });

    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }
        String url = C.CLBU_MIAN_SPECIAL + page.pageNo;
        model = new LSClubSpecialList();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (LSClubSpecialList) mTask.getResultModel();
                page.nextPage();
                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new LSClubSpecialMainAdapter(activity, model.taglist);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(listView);
                    listView.setAdapter(animationAdapter);
                }
                else {
                    adapter.addList(model.taglist);
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
