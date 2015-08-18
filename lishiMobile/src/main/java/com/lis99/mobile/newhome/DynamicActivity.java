package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.DynamicListModel;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/8/13.
 */
public class DynamicActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    //登录
    private Button btn_login;
    private RelativeLayout layout_login;
//    没有关注
    private LinearLayout layout_need_add_attention;
    private PullToRefreshView pull_refresh_view;
    private ListView list;
// 动态
    private FrameLayout layout_dynamic;
    private PullToRefreshView pull_refresh_dynamic;
    private ListView list_dynamic;

    private NeedAttentionAdapter attentionAdapter;

    private DynamicAdapter adapter;

    private Page dPage, rPage;

    private DynamicListModel model;

    private boolean needLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_main);


        initViews();

        setTitle("动态");

        dPage = new Page();
        rPage = new Page();
        getDynamicList();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if ( needLogin )
        {
            needLogin = false;
            getDynamicList();
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        btn_login = (Button) findViewById(R.id.btn_login);
        layout_login = (RelativeLayout) findViewById(R.id.layout_login);

        layout_need_add_attention = (LinearLayout) findViewById(R.id.layout_need_add_attention);
        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        list = (ListView) findViewById(R.id.list);

        layout_dynamic = (FrameLayout) findViewById(R.id.layout_dynamic);
        pull_refresh_dynamic = (PullToRefreshView) findViewById(R.id.pull_refresh_dynamic);
        list_dynamic = (ListView) findViewById(R.id.list_dynamic);


        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);


        pull_refresh_dynamic.setOnFooterRefreshListener(this);
        pull_refresh_dynamic.setOnHeaderRefreshListener(this);

        btn_login.setOnClickListener(this);

    }
    //动态
    private void getDynamicList ()
    {
        if ( dPage.isLastPage() )
        {
            return;
        }
        //判断是否登陆， 没有显示登录界面
        String UserId = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(UserId))
        {
            layout_login.setVisibility(View.VISIBLE);
            return;
        }
        else {
            layout_login.setVisibility(View.GONE);
        }

        model = new DynamicListModel();

        String url = C.DYNAMIC_LIST + dPage.pageNo;

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", UserId);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (DynamicListModel) mTask.getResultModel();
                //没有关注
                if ( 0 == model.nofans )
                {
                    layout_need_add_attention.setVisibility(View.VISIBLE);
                    cleanRecommendList();
                    getRecommendList();
                    return;
                }

                layout_need_add_attention.setVisibility(View.GONE);
                layout_dynamic.setVisibility(View.VISIBLE);
                setRightView("");

                dPage.nextPage();
                if ( adapter == null )
                {
                    dPage.setPageSize(model.totalpage);
                    adapter = new DynamicAdapter(activity, model.topicslist);
                    list_dynamic.setAdapter(adapter);
                }
                else
                {
                    adapter.addList(model.topicslist);
                }
            }
        });


    }

    private  void cleanDynamicList ()
    {
        dPage = new Page();
        list_dynamic.setAdapter(null);
        adapter = null;
    }
//推荐关注
    private void getRecommendList ()
    {
        if ( rPage.isLastPage() )
        {
            return;
        }

        LSRequestManager.getInstance().getFriendsAttentionRecommend(rPage.getPageNo(), new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                MyFriendsRecommendModel model = (MyFriendsRecommendModel) mTask.getResultModel();
                setRightView("下一步");
                rPage.nextPage();
                if ( attentionAdapter == null )
                {
                    rPage.setPageSize(model.totPage);
                    attentionAdapter = new NeedAttentionAdapter(activity, model.lists);
                    list.setAdapter(attentionAdapter);
                }
                else
                {
                    attentionAdapter.addList(model.lists);
                }

            }
        });
    }

    private void cleanRecommendList ()
    {
        rPage = new Page();
        list.setAdapter(null);
        attentionAdapter = null;
    }


    @Override
    protected void rightAction() {
        super.rightAction();
        if ( layout_need_add_attention.getVisibility() == View.VISIBLE )
        {
            //获取动态
            cleanDynamicList();
            getDynamicList();
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        if ( arg0.getId() == R.id.btn_login )
        {
            needLogin = true;
            Common.isLogin(activity);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        view.onFooterRefreshComplete();
        if ( layout_need_add_attention.getVisibility() == View.VISIBLE )
        {
            getRecommendList();
        }
        else if ( layout_dynamic.getVisibility() == View.VISIBLE )
        {
            getDynamicList();
        }

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        view.onHeaderRefreshComplete();
        if ( layout_need_add_attention.getVisibility() == View.VISIBLE )
        {
            cleanRecommendList();
            getRecommendList();
        }
        else if ( layout_dynamic.getVisibility() == View.VISIBLE )
        {
            cleanDynamicList();
            getDynamicList();
        }
    }
}
