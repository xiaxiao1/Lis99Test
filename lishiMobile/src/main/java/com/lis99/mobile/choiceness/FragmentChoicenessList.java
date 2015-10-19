package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.view.PullToRefreshView;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessList extends Fragment  implements
        com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener {

    private View v;
    private ListView list;

    private PullToRefreshView pull_refresh_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = View.inflate(getActivity(), R.layout.club_level_list, null );

        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView) v.findViewById(R.id.list);



        return v;
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFooterRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {

    }

    @Override
    public void onHeaderRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {

    }
}
