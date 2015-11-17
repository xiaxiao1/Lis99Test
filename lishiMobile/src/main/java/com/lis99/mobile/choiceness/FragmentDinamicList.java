package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.DynamicListModel;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.DynamicAdapter;
import com.lis99.mobile.newhome.NeedAttentionAdapter;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/10/19.
 */
public class FragmentDinamicList extends Fragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener
{

    //登录
    private Button btn_login, btn_next;
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

//点击TAB第一次加载
    private boolean isInit = false;

    private View v;
//  是否登陆
    private boolean isLogin = false;

    public FragmentDinamicList ()
    {
        dPage = new Page();
        rPage = new Page();
    }

    @Override
    public void onResume() {
        super.onResume();


        String userId = DataManager.getInstance().getUser().getUser_id();
        if ( !TextUtils.isEmpty(userId) && isLogin )
        {
            getDynamicList();
        }
//        if ( needLogin )
//        {
//            needLogin = false;
//            getDynamicList();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = View.inflate(getActivity(), R.layout.dynamic_main, null );

        btn_next = (Button) v.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        btn_login = (Button) v.findViewById(R.id.btn_login);
        layout_login = (RelativeLayout) v.findViewById(R.id.layout_login);

        layout_need_add_attention = (LinearLayout) v.findViewById(R.id.layout_need_add_attention);
        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
        list = (ListView) v.findViewById(R.id.list);

        layout_dynamic = (FrameLayout) v.findViewById(R.id.layout_dynamic);
        pull_refresh_dynamic = (PullToRefreshView) v.findViewById(R.id.pull_refresh_dynamic);
        list_dynamic = (ListView) v.findViewById(R.id.list_dynamic);


        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);


        pull_refresh_dynamic.setOnFooterRefreshListener(this);
        pull_refresh_dynamic.setOnHeaderRefreshListener(this);

        btn_login.setOnClickListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (attentionAdapter == null) return;
                MyFriendsRecommendModel.Lists item = (MyFriendsRecommendModel.Lists) attentionAdapter.getItem(i);
                if (item == null) return;
                Common.goUserHomeActivit(getActivity(), "" + item.user_id);

            }
        });

        list_dynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter == null) return;
                DynamicListModel.Topicslist item = (DynamicListModel.Topicslist) adapter.getItem(i);
                if (item == null) return;

                if ( item.category == 2 )
                {
                    Intent intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
                    intent.putExtra("topicID", item.topic_id);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), LSClubTopicActivity.class);
                    intent.putExtra("topicID", item.topic_id);
                    startActivity(intent);
                }


            }
        });

//        dPage = new Page();
//        rPage = new Page();

        layout_login.setVisibility(View.GONE);
        layout_need_add_attention.setVisibility(View.GONE);
        layout_dynamic.setVisibility(View.GONE);

        return v;

    }

    public void init ()
    {
        if ( isInit )
        {
            String userId = DataManager.getInstance().getUser().getUser_id();
            if ( !TextUtils.isEmpty(userId) && isLogin )
            {
                getDynamicList();
            }
            return;
        }
        isInit = true;
        getDynamicList();

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
            isLogin = true;
            layout_need_add_attention.setVisibility(View.GONE);
            layout_dynamic.setVisibility(View.GONE);
            layout_login.setVisibility(View.VISIBLE);
            return;
        }
        else {
            isLogin = false;
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
                if ( model == null ) return;
                Common.log("model.nofans="+model.nofans);
                //没有关注
                if ( 0 == model.nofans )
                {
                    layout_need_add_attention.setVisibility(View.VISIBLE);
                    layout_dynamic.setVisibility(View.GONE);
                    cleanRecommendList();
                    getRecommendList();
                    return;
                }

                layout_need_add_attention.setVisibility(View.GONE);
                layout_dynamic.setVisibility(View.VISIBLE);
//                setRightView("");

                dPage.nextPage();
                if ( adapter == null )
                {
                    dPage.setPageSize(model.totalpage);
                    adapter = new DynamicAdapter(getActivity(), model.topiclist);
                    list_dynamic.setAdapter(adapter);
                }
                else
                {
                    adapter.addList(model.topiclist);
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
//                setRightView("下一步");


                rPage.nextPage();
                if (attentionAdapter == null) {
                    rPage.setPageSize(model.totPage);
                    attentionAdapter = new NeedAttentionAdapter(getActivity(), model.lists);
                    list.setAdapter(attentionAdapter);
                } else {
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


//    @Override
//    protected void rightAction() {
//        super.rightAction();
//        if ( layout_need_add_attention.getVisibility() == View.VISIBLE )
//        {
//            //获取动态
//            cleanDynamicList();
//            getDynamicList();
//        }
//    }

    @Override
    public void onClick(View arg0) {
        if ( arg0.getId() == R.id.btn_login )
        {
            Common.isLogin(getActivity());
        }
        else if ( arg0.getId() == R.id.btn_next )
        {
            if ( layout_need_add_attention.getVisibility() == View.VISIBLE )
            {
                //获取动态
                cleanDynamicList();
                getDynamicList();
            }
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
            getDynamicList();
        }
        else if ( layout_dynamic.getVisibility() == View.VISIBLE )
        {
            cleanDynamicList();
            getDynamicList();
        }
    }

    public void scrollToTop ()
    {
        if ( list_dynamic.getVisibility() == View.VISIBLE && list_dynamic.getAdapter() != null )
        {
            list_dynamic.setSelection(0);
        }
        else if ( list.getVisibility() == View.VISIBLE && list.getAdapter() != null )
        {
            list.setSelection(0);
        }
    }

}
