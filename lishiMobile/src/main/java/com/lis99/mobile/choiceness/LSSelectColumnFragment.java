package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.adapter.LSSelectColumnAdapter;
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
 * Created by zhangjie on 1/28/16.
 */
public class LSSelectColumnFragment extends Fragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, LSSelectColumnAdapter.LSSelectColumnAdapterListener {
    private PullToRefreshView pull_refresh_view;

    private ListView listView;

    private Page page;

    private LSSelectColumnAdapter adapter;

    private LSClubSpecialList model;

    private View v;

    private boolean isInit = false;


    public LSSelectColumnFragment() {
        page = new Page();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = View.inflate(getActivity(), R.layout.ls_select_column, null);

        initViews();
        return v;

    }

    public void init() {
        if (!isInit) {
            isInit = true;
            if (adapter != null) return;
            getList();
        }


    }

    protected void initViews() {

        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);

        listView = (ListView) v.findViewById(R.id.listView);

        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);



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
                if (adapter == null) {
                    page.setPageSize(model.totalpage);
                    adapter = new LSSelectColumnAdapter(getActivity(), model.taglist);
                    adapter.listener = LSSelectColumnFragment.this;
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
        pull_refresh_view.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    @Override
    public void onSelectColumn(int columnID) {

        Intent intent = new Intent(getActivity(), ClubSpecialListActivity.class);
        intent.putExtra("tagid", columnID);
        startActivity(intent);
    }

    public void scrollToTop() {
        if (listView.getAdapter() != null) {
            listView.setSelection(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
