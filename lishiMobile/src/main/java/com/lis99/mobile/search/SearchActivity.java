package com.lis99.mobile.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.SearchMainListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.ClearEditText;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;

/**
 * 搜索页
 */
public class SearchActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private ClearEditText et_search;

    private ListView list;

    private SearchAdapter adapter;

    public static String searchText;

    private SearchMainListModel model;

    private PullToRefreshView pull_refresh_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

//        getSearchList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        et_search = (ClearEditText) findViewById(R.id.et_search);

        list = (ListView) findViewById(R.id.list);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

//        ArrayList<String> alist = new ArrayList<String>();
//        for ( int i = 0; i < 10; i++ )
//        {
//            String s = "i="+i;
//            alist.add(s);
//        }
//        adapter = new SearchAdapter(this, alist);

//        list.setAdapter(adapter);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something;
                    Common.hideSoftInput(activity);
                    cleanList();
                    getSearchList();
                    return true;
                }
                return false;
            }
        });


    }

    private void cleanList() {

        if (adapter != null) {
            adapter.clean();
            adapter = null;
        }

        if (model != null) {
            model = null;
        }
    }


    private void getSearchList() {
        searchText = et_search.getText().toString().trim();
//        String text = searchText;
        if (TextUtils.isEmpty(searchText)) {
            Common.toast("不能为空");
            return;
        }
        model = new SearchMainListModel();

        searchText = Common.getSearchText(searchText);

        String url = C.SEARCH_MAIN + searchText;

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (SearchMainListModel) mTask.getResultModel();

                if (adapter == null) {
                    adapter = new SearchAdapter(activity, model);
                    list.setAdapter(adapter);
                }

            }
        });

    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanList();
        getSearchList();
    }

}
