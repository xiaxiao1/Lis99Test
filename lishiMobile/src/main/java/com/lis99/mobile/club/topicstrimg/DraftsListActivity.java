package com.lis99.mobile.club.topicstrimg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.dbhelp.DataHelp;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;
import com.lis99.mobile.util.dbhelp.StringImageModel;

import java.util.ArrayList;

/**
 * Created by yy on 16/4/18.
 *  草稿箱
 */
public class DraftsListActivity extends LSBaseActivity  implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {


    private PullToRefreshView refreshView;
    private ListView listView;
    private Page page;

    private DraftsListAdapter adapter;

    private ArrayList<StringImageModel> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_drafts_list);

        initViews();

        setTitle("草稿箱");

        onHeaderRefresh(refreshView);




    }

    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringImageModel item = model.get(position);
                if ( item == null ) return;
                Intent intent = new Intent(activity, LSTopicStringImageActivity.class);
                intent.putExtra("DATA_MODEL", item);
                startActivityForResult(intent, 999);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                final int i = position;

                DialogManager.getInstance().showDraftDialog(activity, new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {
                        if ( model == null ) return;
//                        删除本条
                        if ( "0".equals(mTask.getresult()) )
                        {
                            DataHelp.getInstance().removeDraft(model.get(i));

                            ArrayList<StringImageChildModel> childModel = model.get(i).item;
                            if ( childModel == null || childModel.size() == 0 )
                            {

                            }
                            else
                            {
                                for (  StringImageChildModel item : childModel )
                                {
                                    DataHelp.getInstance().removeItem(item);
                                }
                            }

                            onHeaderRefresh(refreshView);
                        }
                        else
                        {
                            DataHelp.getInstance().cleanAll();
                            onHeaderRefresh(refreshView);
                        }
                    }
                });


                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK )
        {
            switch (requestCode)
            {
                //发帖成功后， 刷新列表
                case 999:
                    onHeaderRefresh(refreshView);
                    break;
            }
        }

    }

    private void getList ()
    {
        model = DataHelp.getInstance().searchDraft();

        if ( model != null && model.size() != 0 )
        {
            adapter = new DraftsListAdapter(activity, model);
            listView.setAdapter(adapter);
        }
        else
        {
            Common.toast("草稿箱里没有数据");
        }
    }

    private void cleanList ()
    {
        page = new Page();
        listView.setAdapter(null);
        adapter = null;

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        cleanList();
        getList();

    }



}
