package com.lis99.mobile.club.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManager extends LSBaseActivity {

    private TextView title, tv_pay;

    private Button btn_enter, btn_refuse, btn_need_enter;

    private View  view_enter, view_refuse , view_need_enter;

    private ListView list;

    private Button btn_ok, btn_out;

    private ImageView iv_pay_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lsclub_apply_manager);

        initViews();

        setTitle("管理报名");

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

        view_enter = findViewById(R.id.view_enter);
        view_refuse = findViewById(R.id.view_refuse);
        view_need_enter = findViewById(R.id.view_need_enter);

        list = (ListView) findViewById(R.id.list);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_out = (Button) findViewById(R.id.btn_out);

        iv_pay_state = (ImageView) findViewById(R.id.iv_pay_state);




    }

    private void getList ()
    {

    }

    private void cleanList ()
    {

    }


}
