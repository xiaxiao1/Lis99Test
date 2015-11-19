package com.lis99.mobile.club.apply;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.widget.applywidget.MyApplyItem;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplayNew extends LSBaseActivity {

    private ListView list;

    private Button btn_ok, btn_add;

    private MyApplyItem adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_new_apply_main);

        initViews();

        setTitle("报名");

        getList();


    }

    @Override
    protected void initViews() {
        super.initViews();

        list = (ListView) findViewById(R.id.list);

        btn_add = (Button) findViewById(R.id.btn_add);

        btn_ok = (Button) findViewById(R.id.btn_ok);


    }


    private void getList() {

        ArrayList<String> item = new ArrayList<String>();
        item.add("0");
        item.add("0");
        adapter = new MyApplyItem(this, item);
//        设置显示属性
        adapter.setVisibleItem(null);
        list.setAdapter(adapter);

    }

    private void cleanList()
    {

    }



}
