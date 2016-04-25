package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.ApplyManagerModel;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.club.widget.applywidget.ApplyManagerItem;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManager extends LSBaseActivity implements com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener
{

    private TextView tv_title, tv_pay;

    private Button btn_enter, btn_refuse, btn_need_enter;

    private View  view_enter, view_refuse , view_need_enter, currentView;

    private ListView list;

    private PullToRefreshView pull_refresh_view;

//    private Button btn_ok, btn_out;

//    private ImageView iv_pay_state;
//    private TextView tv_pay_state;

    private Page pageEnter, pageNeed, pageRefuse, currentPage;

    private ApplyManagerModel model;

    private int club_id;
    private int topic_id;

    private ApplyManagerItem adapterEnter, adapterRefuse, adapterNeed;

    private boolean NEWACTIVE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsclub_apply_manager);

        initViews();

        setTitle("管理报名");

        topic_id = getIntent().getIntExtra("topicID", 0);
        club_id = getIntent().getIntExtra("clubID", 0);
        NEWACTIVE = getIntent().getBooleanExtra("NEWACTIVE", false);

        pageEnter = new Page();
        pageNeed = new Page();
        pageRefuse = new Page();
        currentPage = pageNeed;

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setOnClickListener(this);
        tv_pay = (TextView) findViewById(R.id.tv_pay);

        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_refuse = (Button) findViewById(R.id.btn_refuse);
        btn_need_enter = (Button) findViewById(R.id.btn_need_enter);

        btn_enter.setOnClickListener(this);
        btn_refuse.setOnClickListener(this);
        btn_need_enter.setOnClickListener(this);

        view_enter = findViewById(R.id.view_enter);
        view_refuse = findViewById(R.id.view_refuse);
        view_need_enter = findViewById(R.id.view_need_enter);

        view_enter.setVisibility(View.GONE);
        view_refuse.setVisibility(View.GONE);
        view_need_enter.setVisibility(View.VISIBLE);

        btn_enter.setTextColor(getResources().getColor(R.color.color_nine));
        btn_refuse.setTextColor(getResources().getColor(R.color.color_nine));


        currentView = view_need_enter;

        list = (ListView) findViewById(R.id.list);

//        btn_ok = (Button) findViewById(R.id.btn_ok);
//        btn_out = (Button) findViewById(R.id.btn_out);

//        iv_pay_state = (ImageView) findViewById(R.id.iv_pay_state);
//        tv_pay_state = (TextView) findViewById(R.id.tv_pay_state);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.btn_enter:
                if ( currentView == view_enter ) return;
                currentView.setVisibility(View.GONE);
                view_enter.setVisibility(View.VISIBLE);
                currentView = view_enter;
                currentPage = pageEnter;

                    onHeaderRefresh(pull_refresh_view);

                btn_enter.setTextColor(getResources().getColor(R.color.text_color_blue));
                btn_refuse.setTextColor(getResources().getColor(R.color.color_nine));
                btn_need_enter.setTextColor(getResources().getColor(R.color.color_nine));

                break;
            case R.id.btn_refuse:
                if ( currentView == view_refuse ) return;
                currentView.setVisibility(View.GONE);
                view_refuse.setVisibility(View.VISIBLE);
                currentView = view_refuse;
                currentPage = pageRefuse;
                onHeaderRefresh(pull_refresh_view);


                btn_enter.setTextColor(getResources().getColor(R.color.color_nine));
                btn_refuse.setTextColor(getResources().getColor(R.color.text_color_blue));
                btn_need_enter.setTextColor(getResources().getColor(R.color.color_nine));

                break;
            case R.id.btn_need_enter:
                if ( currentView == view_need_enter ) return;
                currentView.setVisibility(View.GONE);
                view_need_enter.setVisibility(View.VISIBLE);
                currentView = view_need_enter;
                currentPage = pageNeed;
                onHeaderRefresh(pull_refresh_view);


                btn_enter.setTextColor(getResources().getColor(R.color.color_nine));
                btn_refuse.setTextColor(getResources().getColor(R.color.color_nine));
                btn_need_enter.setTextColor(getResources().getColor(R.color.text_color_blue));

                break;
            case R.id.tv_title:
                if ( NEWACTIVE )
                {
                    Intent intent = new Intent(activity, LSClubTopicActiveOffLine.class);
                    intent.putExtra("topicID", topic_id);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(activity, LSClubTopicActivity.class);
                    intent.putExtra("topicID", topic_id);
                    startActivity(intent);
                }
                break;
        }
    }

    private void getList ()
    {
        if ( currentPage.isLastPage() )
        {
            return;
        }
        model = new ApplyManagerModel();

        String userId = DataManager.getInstance().getUser().getUser_id();

        int type = 1;
        if ( currentPage == pageEnter )
        {
            type = 1;

        }
        else if ( currentPage == pageRefuse )
        {
            type = -1;
        }
        else if ( currentPage == pageNeed )
        {
            type = 2;
        }

        String url = C.MANAGER_JON_ACTIVE_LIST + topic_id + "/" + currentPage.getPageNo();
        if ( NEWACTIVE )
        {
            url = C.MANAGER_JON_ACTIVE_LIST_NEW + topic_id + "/" + currentPage.getPageNo();
        }

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("club_id", club_id);
        map.put("type", type);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ApplyManagerModel) mTask.getResultModel();
                if (model == null) {
                    return;
                }

                currentPage.pageNo += 1;

                btn_enter.setText("已确认（" + model.info.applyPass + "）");
                btn_refuse.setText("已拒绝（" + model.info.applyRefuse + "）");
                btn_need_enter.setText("待确认（" + model.info.applyAudit + "）");

                tv_title.setText(model.info.title);
                tv_pay.setText("共" + model.info.applyTotal + "个用户，" + model.info.totalApplyInfo + "个报名信息");

                if (currentPage == pageEnter) {
                    if (adapterEnter == null) {
                        pageEnter.setPageSize(model.totPage);
                        adapterEnter = new ApplyManagerItem(activity, model.info.lists);
                        adapterEnter.setType(0, model.info.club_id, model.info.topic_id, ApplyManager.this);
                        list.setAdapter(adapterEnter);
                    } else {
                        adapterEnter.addList(model.info.lists);
                    }
                } else if (currentPage == pageRefuse) {
                    if (adapterRefuse == null) {
                        pageRefuse.setPageSize(model.totPage);
                        adapterRefuse = new ApplyManagerItem(activity, model.info.lists);
                        adapterRefuse.setType(1, model.info.club_id, model.info.topic_id, ApplyManager.this);
                        list.setAdapter(adapterRefuse);
                    } else {
                        adapterRefuse.addList(model.info.lists);
                    }
                } else if (currentPage == pageNeed) {
                    if (adapterNeed == null) {
                        pageNeed.setPageSize(model.totPage);
                        adapterNeed = new ApplyManagerItem(activity, model.info.lists);
                        adapterNeed.setType(2, model.info.club_id, model.info.topic_id, ApplyManager.this);
                        list.setAdapter(adapterNeed);
                    } else {
                        adapterNeed.addList(model.info.lists);
                    }
                }


            }
        });


    }

    private void cleanList ()
    {
        list.setAdapter(null);
        if ( currentPage == pageEnter )
        {
            pageEnter = new Page();
            currentPage = pageEnter;
            if ( adapterEnter != null )
            adapterEnter.clean();
            adapterEnter = null;
        }
        else if ( currentPage == pageRefuse )
        {
            pageRefuse = new Page();
            currentPage = pageRefuse;
            if ( adapterRefuse != null )
            adapterRefuse.clean();
            adapterRefuse = null;
        }
        else if ( currentPage == pageNeed )
        {
            pageNeed = new Page();
            currentPage = pageNeed;
            if ( adapterNeed != null )
            adapterNeed.clean();
            adapterNeed = null;
        }
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
