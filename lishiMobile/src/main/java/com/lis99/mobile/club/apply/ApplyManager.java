package com.lis99.mobile.club.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ApplyManagerModel;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManager extends LSBaseActivity implements com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener
{

    private TextView title, tv_pay;

    private Button btn_enter, btn_refuse, btn_need_enter;

    private View  view_enter, view_refuse , view_need_enter;

    private ListView list;

    private PullToRefreshView pull_refresh_view;

//    private Button btn_ok, btn_out;

    private ImageView iv_pay_state;

    private Page pageEnter, pageNeed, pageRefuse, currentPage;

    private ApplyManagerModel model;

    private int club_id;
    private int topic_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsclub_apply_manager);

        initViews();

        setTitle("管理报名");

        topic_id = getIntent().getIntExtra("topicID", 0);
        club_id = getIntent().getIntExtra("clubID", 0);

        pageEnter = new Page();
        pageNeed = new Page();
        pageNeed = new Page();
        currentPage = pageEnter;

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        title = (TextView) findViewById(R.id.title);
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

        list = (ListView) findViewById(R.id.list);

//        btn_ok = (Button) findViewById(R.id.btn_ok);
//        btn_out = (Button) findViewById(R.id.btn_out);

        iv_pay_state = (ImageView) findViewById(R.id.iv_pay_state);

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
                break;
            case R.id.btn_refuse:
                break;
            case R.id.btn_need_enter:
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

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);

//        MyRequestManager.getInstance().requestPost(url, );


    }

    private void cleanList ()
    {

    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }
}
