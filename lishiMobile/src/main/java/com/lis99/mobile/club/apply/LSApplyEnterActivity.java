package com.lis99.mobile.club.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplyEnterActivity extends LSBaseActivity{

    private TextView title, tv_pay, tv_joinNum, tv_price;

    private GridView grid;

    private RadioGroup radioGroup;

    private View layout_off_line, layout_weixin, layout_zhifubao;

    private Button btn_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_apply_enter_main);

        initViews();

        setTitle("报名");



    }

    @Override
    protected void initViews() {
        super.initViews();

        title = (TextView) findViewById(R.id.title);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_joinNum = (TextView) findViewById(R.id.tv_joinNum);
        tv_price = (TextView) findViewById(R.id.tv_price);

        grid = (GridView) findViewById(R.id.grid);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        layout_off_line = findViewById(R.id.layout_off_line);
        layout_weixin = findViewById(R.id.layout_weixin);
        layout_zhifubao = findViewById(R.id.layout_zhifubao);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        layout_off_line.setOnClickListener(this);
        layout_weixin.setOnClickListener(this);
        layout_zhifubao.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.btn_ok:
                break;
            case R.id.layout_off_line:
                break;
            case R.id.layout_weixin:
                break;
            case R.id.layout_zhifubao:
                break;
        }


    }
}
