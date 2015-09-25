package com.lis99.mobile.newhome.equip;

import android.os.Bundle;
import android.view.View;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;

/**
 * Created by yy on 15/9/25.
 */
public class LSEquipReplyActivity extends LSBaseActivity  implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }
}
