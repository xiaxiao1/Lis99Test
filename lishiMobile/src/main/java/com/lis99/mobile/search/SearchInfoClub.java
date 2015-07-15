package com.lis99.mobile.search;

import android.os.Bundle;

import com.lis99.mobile.club.model.SearchInfoClubModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/7/6.
 */
public class SearchInfoClub extends SearchInfoBase implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private SearchInfoClubAdapter adapter;

    private SearchInfoClubModel model;

    private Page page;

    public static String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        btn_type.setText("俱乐部");


        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        searchText = getIntent().getStringExtra("SEARCHTEXT");

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if ( model == null || model.clublist == null || model.clublist.size() == 0 )
//                    return;
//                Intent intent = new Intent(SearchInfoClub.this, LSClubDetailActivity.class);
//                intent.putExtra("clubID", model.clublist.get(i).id);
//                startActivity(intent);
//            }
//        });

        clean();
        getClubList();

    }

    @Override
    public void searchNew(String str) {
        searchText = str;
        clean();
        getClubList();
    }

    private void clean ()
    {
        if ( adapter != null )
        {
            adapter = null;
        }
        if ( model != null )
        {
            model = null;
        }
        page = new Page();
    }

    private void getClubList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        model = new SearchInfoClubModel();

        String url = C.SEARCH_CLUB + searchText + "?page="+page.pageNo;

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (SearchInfoClubModel) mTask.getResultModel();

                if ( model.clublist == null || model.clublist.size() == 0 )
                {
                    return;
                }
                page.pageNo += 1;

                if ( adapter == null )
                {
                    page.setPageItemSize(model.totpage);
                    adapter = new SearchInfoClubAdapter(activity, model.clublist);
                    list.setAdapter(adapter);
                    return;
                }
                adapter.addList(model.clublist);

            }
        });


    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();

        getClubList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        clean();
        getClubList();
    }
}
