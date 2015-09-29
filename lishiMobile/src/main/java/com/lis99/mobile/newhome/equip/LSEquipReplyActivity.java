package com.lis99.mobile.newhome.equip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.EquipReplyList;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.equip.LSEquipCommentActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/9/25.
 */
public class LSEquipReplyActivity extends LSBaseActivity  implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private Page page;

    private ReplayListItem adapter;

    private PullToRefreshView pull_refresh_view;

    private ListView list;

    private EquipReplyList model;

    private int id;

    private View headView;

    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = Common.string2int(getIntent().getStringExtra("id"));

        if ( id == -1 )
        {
            id = getIntent().getIntExtra("id", 0);
        }

        setContentView(R.layout.ls_equip_replay_list);

        initViews();

        setTitle("点评");

        page = new Page();

        setRightView("发表");

        getList();


    }

    @Override
    protected void rightAction() {
        super.rightAction();
        Intent i = new Intent(this, LSEquipCommentActivity.class);
        i.putExtra("equipID", id);
        startActivity(i);
    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        model = new EquipReplyList();

        String url = C.EQUIP_REPLY_LIST + id + "/"+page.pageNo;

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (EquipReplyList) mTask.getResultModel();

                page.nextPage();

                ratingBar.setRating(model.totstart);

                if ( adapter == null )
                {
                    page.setPageSize(model.totalpage);
                    adapter = new ReplayListItem(activity, model.commentlist);
                    list.setAdapter(adapter);

                }
                else
                {
                    adapter.addList(model.commentlist);
                }


            }
        });



    }

    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
    }

    @Override
    protected void initViews() {
        super.initViews();

        headView = View.inflate(this, R.layout.ls_equip_reply_head, null);

        ratingBar = (RatingBar) headView.findViewById(R.id.ratingBar);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        list = (ListView) findViewById(R.id.list);

        list.addHeaderView(headView);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
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
