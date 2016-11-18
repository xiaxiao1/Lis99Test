package com.lis99.mobile.club.newtopic.series;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.newtopic.series.model.ApplyManagerSeriesModel;
import com.lis99.mobile.club.newtopic.series.model.ManagerSeriesLineListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;

import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManagerSeries extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener
{

    private TextView tv_title;

    private Button btn_enter, btn_refuse, btn_need_enter;

    private View  view_enter, view_refuse , view_need_enter, currentView;

    private ListView list;

    private PullToRefreshView pull_refresh_view;

//    private Button btn_ok, btn_out;

//    private ImageView iv_pay_state;
//    private TextView tv_pay_state;

    private Page pageEnter, pageNeed, pageRefuse, currentPage;

    private ApplyManagerSeriesModel model;

    private int club_id;
    private int topic_id;

    private ApplySeriesManagerItem adapterEnter, adapterRefuse, adapterNeed;

//    新加的
    private View view_series;
    private TextView tv1, tv2;

    private int positionSeries = 0;

    private ManagerSeriesLineListModel modelSeries;

    private int batch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsclub_apply_manager_series);

        initViews();

        setTitle("管理报名");

        topic_id = getIntent().getIntExtra("topicID", 0);
        club_id = getIntent().getIntExtra("clubID", 0);

        pageEnter = new Page();
        pageNeed = new Page();
        pageRefuse = new Page();
        currentPage = pageNeed;

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        view_series = findViewById(R.id.view_series);
        view_series.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);

        tv1.setText("全部批次");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setOnClickListener(this);

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

                btn_enter.setTextColor(getResources().getColor(R.color.text_color_green));
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
                btn_refuse.setTextColor(getResources().getColor(R.color.text_color_green));
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
                btn_need_enter.setTextColor(getResources().getColor(R.color.text_color_green));

                break;
            case R.id.tv_title:
                    /*Intent intent = new Intent(activity, LSClubTopicActiveSeries.class);
                    intent.putExtra("topicID", topic_id);
                    startActivity(intent);*/
                Common.goTopic(activity,4,topic_id);
                break;
//            批次
            case R.id.view_series:

                getSeriesBatchList();

                break;
        }
    }


    private void getSeriesBatchList ()
    {
        if ( modelSeries != null && modelSeries.batchList != null && modelSeries.batchList.size() != 0 )
        {
            showSeriesList();
            return;
        }


            String url = C.MANAGER_SERIES_LINE_JOIN_LIST;

        HashMap<String, Object> map = new HashMap<>();

        map.put("activity_id", topic_id);

        modelSeries = new ManagerSeriesLineListModel();

        MyRequestManager.getInstance().requestPost(url, map, modelSeries, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                modelSeries = (ManagerSeriesLineListModel) mTask.getResultModel();

                if ( modelSeries == null || modelSeries.batchList == null || modelSeries.batchList.size() == 0 ) return;

                //        增加全部批次
                ManagerSeriesLineListModel.BatchListEntity item = new ManagerSeriesLineListModel
                        .BatchListEntity();
                item.batchId = 0;
                modelSeries.batchList.add(0, item);

                showSeriesList();

            }
        });

    }

    private void showSeriesList ()
    {
        PopWindowUtil.showActiveSeriesLineManager(positionSeries, view_series, modelSeries, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if ( mTask == null )
                {
                    return;
                }

                positionSeries = Integer.parseInt(mTask.getresult());

                ManagerSeriesLineListModel.BatchListEntity item = modelSeries.batchList.get(positionSeries);

                batch_id = item.batchId;

                if ( positionSeries == 0 )
                {
                    tv1.setText("全部批次");
                }
                else
                {
                    tv1.setText("第"+positionSeries+"批，"+item.starttime+"~"+item.endtime);
                }

                onHeaderRefresh(pull_refresh_view);
            }
        });
    }



    private void getList ()
    {
        if ( currentPage.isLastPage() )
        {
            return;
        }
        model = new ApplyManagerSeriesModel();

        String userId = DataManager.getInstance().getUser().getUser_id();

//        报名状态是否审核：0未审核，1审核通过 2:已拒绝
        int type = 1;
        if ( currentPage == pageEnter )
        {
            type = 1;

        }
        else if ( currentPage == pageRefuse )
        {
            type = 2;
        }
        else if ( currentPage == pageNeed )
        {
            type = 0;
        }

        String url = C.MANAGER_SERIES_LINE_JOIN_NEW + "/" + currentPage.getPageNo();


        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("club_id", club_id);
        map.put("trial_status", type);
        map.put("activity_id", topic_id);
        map.put("batch_id", batch_id);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ApplyManagerSeriesModel) mTask.getResultModel();
                if (model == null) {
                    return;
                }

                currentPage.pageNo += 1;

                btn_enter.setText("已确认（" + model.applyPass + "）");
                btn_refuse.setText("已关闭（" + model.applyRefuse + "）");
                btn_need_enter.setText("待确认（" + model.applyAudit + "）");

                tv2.setText("共"+model.applyTotPeople+"个用户，"+model.applyTotal+"个报名信息");


                tv_title.setText(model.title);

                if (currentPage == pageEnter) {
                    if (adapterEnter == null) {
                        pageEnter.setPageSize(model.totPage);
                        adapterEnter = new ApplySeriesManagerItem(activity, model.applylist);
                        adapterEnter.setType(0, topic_id, ApplyManagerSeries.this);
                        list.setAdapter(adapterEnter);
                    }
                    else
                    {
                        adapterEnter.addList(model.applylist);
                    }
                } else if (currentPage == pageRefuse) {
                    if (adapterRefuse == null) {
                        pageRefuse.setPageSize(model.totPage);
                        adapterRefuse = new ApplySeriesManagerItem(activity, model.applylist);
                        adapterRefuse.setType(1, topic_id, ApplyManagerSeries.this);
                        list.setAdapter(adapterRefuse);
                    } else {
                        adapterRefuse.addList(model.applylist);
                    }
                } else if (currentPage == pageNeed) {
                    if (adapterNeed == null) {
                        pageNeed.setPageSize(model.totPage);
                        adapterNeed = new ApplySeriesManagerItem(activity, model.applylist);
                        adapterNeed.setType(2, topic_id, ApplyManagerSeries.this);
                        list.setAdapter(adapterNeed);
                    } else {
                        adapterNeed.addList(model.applylist);
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
