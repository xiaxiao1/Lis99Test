package com.lis99.mobile.club.widget.applywidget;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ApplyContactsListModel;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.ContactsUtil;
import com.lis99.mobile.util.DialogManager;

/**
 * Created by yy on 16/8/5.
 */
public class ContactsActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{


    private LinearLayout layoutMain;
    private PullToRefreshView pullRefreshView;
    private ListView listInfo;
    private TextView tvNothing;

    private ContactsAdapter adapter;

    private ApplyContactsListModel model;
//  添加，编辑报名
    public static final int ADD_EDIT_CONTACTS = 999;

    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_list);

        currentPosition = getIntent().getIntExtra("POSITION", -1);

        initViews();

        setTitle("常用报名信息");

        onHeaderRefresh(pullRefreshView);

    }

    @Override
    protected void initViews() {
        super.initViews();

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        pullRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pullRefreshView.setOnFooterRefreshListener(this);
        pullRefreshView.setOnHeaderRefreshListener(this);
        listInfo = (ListView) findViewById(R.id.list_info);
        tvNothing = (TextView) findViewById(R.id.tv_nothing);

        tvNothing.setVisibility(View.GONE);

        findViewById(R.id.btn_new).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new:
                //TODO implement

                startActivityForResult( new Intent(activity, ContactsEdtingActivity.class), ADD_EDIT_CONTACTS);


                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == ADD_EDIT_CONTACTS )
            {
                onHeaderRefresh(pullRefreshView);
            }
        }
    }

    private void getList ()
    {
        ContactsUtil.getInstance().getContactsList(new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ApplyContactsListModel) mTask.getResultModel();

                if ( model == null || model.user_list == null || model.user_list.size() == 0 )
                {
                    tvNothing.setVisibility(View.VISIBLE);
                    return;
                }

                tvNothing.setVisibility(View.GONE);

                adapter = new ContactsAdapter(activity, model.user_list);

                listInfo.setAdapter(adapter);

                listInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        sendResult(position);
                    }
                });

                listInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                            position, long id) {

                        DialogManager.getInstance().startAlert(activity, "提示", "删除本条数据", true, "删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeContactsInfo(position);
                            }
                        }, true, "取消", null);

                        return true;
                    }
                });
            }
        });
    }
//    返回上一界面， 传递当前选择的内容
    private void sendResult ( int position )
    {
        Intent intent = new Intent();
        NewApplyUpData info = (NewApplyUpData) adapter.getItem(position);
        intent.putExtra("INFO", info);
        intent.putExtra("POSITION", currentPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

    private  void cleanList ()
    {
        if ( listInfo != null )
        listInfo.setAdapter(null);
        if ( adapter != null )
        adapter = null;
    }

    private void removeContactsInfo (final int position )
    {
        if ( adapter == null ) return;

        NewApplyUpData info = (NewApplyUpData) adapter.getItem(position);

        if ( info == null ) return;

        ContactsUtil.getInstance().removeContactsInfo(info.id, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
//                onHeaderRefresh(pullRefreshView);
                if ( adapter != null )
                {
                    adapter.removeAt(position);
                }
            }
        });
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pullRefreshView.onFooterRefreshComplete();
//        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pullRefreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }
}
