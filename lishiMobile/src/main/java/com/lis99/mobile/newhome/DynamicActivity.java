package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.view.PullToRefreshView;

/**
 * Created by yy on 15/8/13.
 */
public class DynamicActivity extends LSBaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_main);


        initViews();

        setTitle("动态");



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



    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
    }
}
