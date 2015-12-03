package com.lis99.mobile.newhome.sysmassage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.apply.ApplyManager;
import com.lis99.mobile.club.apply.MyJoinActiveInfoActivity;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.SysMassageModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/8/28.
 */
public class SysMassageActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private ListView list;
    private PullToRefreshView pull_refresh_view;
    private Page page;
    private SysMassageAdapter adapter;

    private SysMassageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sys_massage);

        initViews();

        setTitle("系统消息");

        setRightView("清空");

        page = new Page();

        getList();

    }

    @Override
    protected void initViews() {

        super.initViews();

        list = (ListView) findViewById(R.id.list);
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter == null) return;
                SysMassageModel.Lists item = (SysMassageModel.Lists) adapter.getItem(i);
                if (item == null) return;
                Intent intent = null;
                switch (item.skip_type) {
                    case 0:
                        break;
//                    帖子
                    case 1:
//                        intent = new Intent(activity, LSClubTopicActivity.class);
//                        intent.putExtra("topicID", item.topicid);
//                        startActivity(intent);
                        break;
                    case 7:

                        intent = new Intent(activity, ApplyManager.class);
                        intent.putExtra("topicID", item.topicid);
                        intent.putExtra("clubID", item.club_id);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(activity, MyJoinActiveInfoActivity.class);
                        intent.putExtra("ORDERID", item.orderid);
                        startActivity(intent);
                        break;
                }


            }
        });


    }

    @Override
    protected void rightAction() {
        super.rightAction();
        DialogManager.getInstance().startAlert(activity, "清空消息", "确认清空所有消息？", true, "取消", null, true, "清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cleanAll();
            }
        });

    }

    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
    }

    private void cleanAll ()
    {
        String url = C.SYS_MASSAGE_CLEANALL;

        BaseModel model = new BaseModel();

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                Common.toast("清空成功");
                cleanList();
            }
        });

    }

    private void getList ()
    {
        if ( page.isLastPage())
        {
            return;
        }

        model = new SysMassageModel();

        String url = C.SYS_MASSAGE_LIST + page.pageNo;

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (SysMassageModel) mTask.getResultModel();

                page.nextPage();

                if ( adapter == null )
                {
                    page.setPageSize(model.totPage);

                    adapter = new SysMassageAdapter(activity, model.lists);

                    list.setAdapter(adapter);

                }
                else
                {
                    adapter.addList(model.lists);
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
