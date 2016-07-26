package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.activeline.LSActiveLineNewAdapter;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class LSRecommendActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    //接收返回的推荐活动列表
    private List<Object> recommendList=new ArrayList<Object>();
    private Page page;
    private ListView listview;
    private PullToRefreshView refreshView;
    String value;
    HashMap<String,Object> map=new HashMap<String,Object>();

    //复用 本地活动 的adapter和model和listview的item
    private LSActiveLineNewAdapter adapter;
    //
    private ActiveLineNewModel model;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_club_recommendedactivity);
        initViews();
        page=new Page();
        //获取intent传递过来的参数值
        value=getIntent().getStringExtra("");
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        getList();
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitle("说不定您喜欢");
        refreshView=(PullToRefreshView)findViewById(R.id.recommended_activity_pull_refresh_view);
        listview = (ListView) findViewById(R.id.recommended_activity_listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ActiveLineNewModel.ActivitylistEntity active= model.getActivitylist().get(position);
               /*
               *跳转到具体的活动页
               Intent i=new Intent(LSRecommendActivity.this,XX.class);
                LSRecommendActivity.this.startActivity(i);*/
            }
        });
    }

    public void getList(){

        if ( page.isLastPage())
        {
            return;
        }

        String url = "+page.getPageNo()";
        //组装map的v-k参数对

        model = new ActiveLineNewModel();

        MyRequestManager.getInstance().requestPost(url,map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ActiveLineNewModel) mTask.getResultModel();

                if ( model == null ) return;
//

                page.nextPage();



                if ( adapter == null ) {
                    page.setPageSize(model.getTotalpage());

                    adapter = new LSActiveLineNewAdapter(LSRecommendActivity.this, model.getActivitylist());
                    listview.setAdapter(adapter);

                }
                else
                {
                    //                    最后一页
                    adapter.addList(model.getActivitylist());
                }



            }
        });
    }
}
