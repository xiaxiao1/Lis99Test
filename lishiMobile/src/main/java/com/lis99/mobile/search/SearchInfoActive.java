package com.lis99.mobile.search;

import android.os.Bundle;

import com.lis99.mobile.club.model.SearchInfoActiveModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoActive extends SearchInfoBase implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private SearchInfoActiveAdapter adapter;

    private SearchInfoActiveModel model;

    private Page page;

    public static String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_type.setText("线路活动");


        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        searchText = getIntent().getStringExtra("SEARCHTEXT");

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if ( model == null || model.clubtopiclist == null || model.clubtopiclist.size() == 0 )
//                    return;
//                Intent intent = new Intent(SearchInfoActive.this, LSClubDetailActivity.class);
//                intent.putExtra("topicID", model.clubtopiclist.get(i).id);
//                startActivity(intent);
//            }
//        });

        clean();
        getActive();
    }

    @Override
    public void searchNew(String str) {
        searchText = str;
        clean();
        getActive();
    }

    private void getActive ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        model = new SearchInfoActiveModel();

        String url = C.SEARCH_TOPIC + searchText + "?page="+page.pageNo + "&category=1";

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (SearchInfoActiveModel) mTask.getResultModel();

                if ( model.clubtopiclist == null || model.clubtopiclist.size() == 0 )
                {
                    return;
                }

                page.pageNo += 1;

                if ( adapter == null )
                {
                    page.setPageItemSize(model.totpage);

                    adapter = new SearchInfoActiveAdapter(activity, model.clubtopiclist);
                    list.setAdapter(adapter);

                    return;
                }

                adapter.addList(model.clubtopiclist);

            }
        });


    }


    private void clean ()
    {
        if ( adapter != null ) adapter = null;
        if ( model != null ) model = null;
        page = new Page();
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getActive();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        clean();
        getActive();
    }
}
