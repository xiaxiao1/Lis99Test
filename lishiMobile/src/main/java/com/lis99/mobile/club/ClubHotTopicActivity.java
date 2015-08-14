package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.ClubHotTopicAdapter;
import com.lis99.mobile.club.model.ClubHotTopicModel;
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
public class ClubHotTopicActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView pull_refresh_view;

    private ListView listView;

    private Page page;

    private ClubHotTopicModel model;

    private ClubHotTopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clbu_hot_topic_main);
        initViews();
        setTitle("最新热帖");

        onHeaderRefresh(pull_refresh_view);
    }

    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null ) return;

                ClubHotTopicModel.Topicslist item = (ClubHotTopicModel.Topicslist) adapter.getItem(i);
                Intent intent = new Intent(activity, LSClubTopicActivity.class);
                intent.putExtra("topicID", item.topic_id);
                startActivity(intent);


            }
        });

    }

    private void cleanList ()
    {
        page = new Page();
        listView.setAdapter(null);
        adapter = null;
    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        String url = C.CLUB_HOT_TOPIC + page.pageNo;

        model = new ClubHotTopicModel();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ClubHotTopicModel) mTask.getResultModel();
                page.nextPage();
                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new ClubHotTopicAdapter(activity, model.topicslist);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(listView);
                    listView.setAdapter(animationAdapter);

                }
                else
                {
                    adapter.addList(model.topicslist);
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
