package com.lis99.mobile.search;

import android.os.Bundle;

import com.lis99.mobile.club.model.SearchInfoTopicModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoTopic extends SearchInfoBase implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private SearchInfoTopicAdapter adapter;

    private SearchInfoTopicModel model;

    private Page page;

    public static String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_type.setText("话题");


        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        searchText = getIntent().getStringExtra("SEARCHTEXT");

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if ( model == null || model.clubtopiclist == null || model.clubtopiclist.size() == 0 )
//                    return;
//                Intent intent = new Intent(SearchInfoTopic.this, LSClubDetailActivity.class);
//                intent.putExtra("topicID", model.clubtopiclist.get(i).id);
//                startActivity(intent);
//            }
//        });

        clean();
        getTopicList();

    }

    @Override
    public void searchNew(String str) {
        searchText = str;
        clean();
        getTopicList();
    }

    private void clean ()
    {
        if ( adapter != null )
        {
            adapter = null;
        }
        if ( model != null ) model = null;
        page = new Page();
    }

    private void getTopicList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        model = new SearchInfoTopicModel();

        String url = C.SEARCH_TOPIC + searchText + "?page="+page.pageNo + "&category=0";

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (SearchInfoTopicModel) mTask.getResultModel();
                if ( model.clubtopiclist == null || model.clubtopiclist.size() == 0 )
                {
                    return;
                }
                page.pageNo += 1;

                if ( adapter == null )
                {
                    page.setPageItemSize(model.totpage);
                    adapter = new SearchInfoTopicAdapter(activity, model.clubtopiclist);
                    list.setAdapter(adapter);
                    return;
                }
                adapter.addList(model.clubtopiclist);


            }
        });


    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getTopicList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        clean();
        getTopicList();
    }
}
