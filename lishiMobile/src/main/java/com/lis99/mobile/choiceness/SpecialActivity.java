package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.adapter.SpecialMainAdapter;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.LSClubSpecialList;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

/**
 * Created by yy on 16/6/23.
 */
public class SpecialActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{


    private PullToRefreshView pullRefreshView;
    private ListView listView;

    private SpecialMainAdapter adapter;

    private Page page;

    private LSClubSpecialList model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.special_main_view);

        initViews();

        setTitle("全部专栏");

        onHeaderRefresh(pullRefreshView);

    }

    @Override
    protected void initViews() {
        super.initViews();

        pullRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        listView = (ListView) findViewById(R.id.listView);

        pullRefreshView.setOnFooterRefreshListener(this);
        pullRefreshView.setOnHeaderRefreshListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( parent.getAdapter() == null ) return;
                LSClubSpecialList.Taglist item = (LSClubSpecialList.Taglist)parent.getAdapter().getItem(position);

                if ( item == null ) return;

//                Intent intent = new Intent(activity, SpecialInfoActivity.class);
//                intent.putExtra("tagid", item.id);
//                startActivity(intent);
                ActivityUtil.goSpecialInfoActivity(activity, item.id);
            }
        });


    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }
        String url = C.COMMUNITY_SPECIAL_MAIN + page.pageNo;
        model = new LSClubSpecialList();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (LSClubSpecialList) mTask.getResultModel();
                page.nextPage();
                if (adapter == null) {
                    page.setPageSize(model.totalpage);
                    adapter = new SpecialMainAdapter(activity, model.taglist);
                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(listView);
                    listView.setAdapter(animationAdapter);
                } else {
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
        pullRefreshView.onFooterRefreshComplete();
        getList();

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        cleanList();
        getList();
        pullRefreshView.onHeaderRefreshComplete();

    }
}
