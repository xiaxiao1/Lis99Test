package com.lis99.mobile.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.mine.model.LSMyActivity;

/**
 * Created by yy on 15/12/2.
 */
public class MyJoinActivePeopleInfo extends LSBaseActivity{

    private TextView tv_title, nameView, idNumView, phoneView, et_telOhter, et_QQ, et_address;
    private View layout_name, layout_idcode, layout_phone, layout_telOhter, layout_qq, layout_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_join_people_info);

        initViews();

        setTitle("查看报名人信息");

        getInfo();

    }

    private void getInfo ()
    {
        LSMyActivity.Applicant item = (LSMyActivity.Applicant) getIntent().getSerializableExtra("PEOPLEINFO");
        if ( item == null ) return;

        tv_title.setText("报名人"+getIntent().getIntExtra("NUM", 1));

        if (TextUtils.isEmpty(item.sex))
        {
            nameView.setText(item.name);
        }
        else
        {
            nameView.setText(item.name + "（" + item.sex + "）");
        }

        if ( !TextUtils.isEmpty(item.credentials))
        {
            layout_idcode.setVisibility(View.VISIBLE);
            idNumView.setText(item.credentials);
        }
        if ( !TextUtils.isEmpty(item.mobile))
        {
            layout_phone.setVisibility(View.VISIBLE);
            phoneView.setText(item.mobile);
        }
        if ( !TextUtils.isEmpty(item.phone))
        {
            layout_telOhter.setVisibility(View.VISIBLE);
            et_telOhter.setText(item.phone);
        }
        if ( !TextUtils.isEmpty(item.qq))
        {
            layout_qq.setVisibility(View.VISIBLE);
            et_QQ.setText(item.qq);
        }
        if ( !TextUtils.isEmpty(item.postaladdress))
        {
            layout_address.setVisibility(View.VISIBLE);
            et_address.setText(item.postaladdress);
        }


    }


    @Override
    protected void initViews() {
        super.initViews();

        tv_title = (TextView) findViewById(R.id.tv_title);
        nameView = (TextView) findViewById(R.id.nameView);
        idNumView = (TextView) findViewById(R.id.idNumView);
        phoneView = (TextView) findViewById(R.id.phoneView);
        et_telOhter = (TextView) findViewById(R.id.et_telOhter);
        et_QQ = (TextView) findViewById(R.id.et_QQ);
        et_address = (TextView) findViewById(R.id.et_address);

        layout_name = findViewById(R.id.layout_name);
        layout_idcode = findViewById(R.id.layout_idcode);
        layout_phone = findViewById(R.id.layout_phone);
        layout_telOhter = findViewById(R.id.layout_telOhter);
        layout_qq = findViewById(R.id.layout_qq);
        layout_address = findViewById(R.id.layout_address);


        layout_idcode.setVisibility(View.GONE);
        layout_phone.setVisibility(View.GONE);
        layout_telOhter.setVisibility(View.GONE);
        layout_qq.setVisibility(View.GONE);
        layout_address.setVisibility(View.GONE);


    }
}
