package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.view.PullToRefreshView;

/**
 * Created by yy on 15/8/13.
 */
public abstract class MyFragmentBase extends Fragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    protected View v;
    protected ListView list;
    protected PullToRefreshView pull_refresh_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.club_level_list, null);
        list = (ListView) v.findViewById(R.id.list);
        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onHeaderRefresh(null);
    }


//    public abstract void onHeadRefresh();
//
//    public abstract void onFootRefresh();

    public abstract void cleanList();

    public abstract void getList();

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
