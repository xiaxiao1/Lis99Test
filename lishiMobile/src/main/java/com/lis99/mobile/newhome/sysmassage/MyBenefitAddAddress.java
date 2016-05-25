package com.lis99.mobile.newhome.sysmassage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BenefitListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LSRequestManager;

import java.util.HashMap;

/**
 * Created by yy on 16/5/18.
 * 收获地址
 */
public class MyBenefitAddAddress extends LSBaseActivity implements View.OnClickListener {


    private ImageView iv_title_bg;
    private ImageView titleLeftImage;
    private RelativeLayout titleLeft;
    private TextView titleRightText;
    private ImageView titleRightImage;
    private RelativeLayout titleRight;
    private TextView title;
    private RelativeLayout titlehead;
    private EditText et_name;
    private EditText et_num;
    private EditText et_address;
    private Button btn_ok;

    private BenefitListModel.BenefitItem info;

    private String name;

    private String num;

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_benefit_add_address);

        info = (BenefitListModel.BenefitItem) getIntent().getSerializableExtra("OBJECT");

        initViews();

        setTitle("填写收货地址");


    }


    @Override
    protected void initViews() {
        super.initViews();

        iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);
        titleRightText = (TextView) findViewById(R.id.titleRightText);
        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        titleRight = (RelativeLayout) findViewById(R.id.titleRight);
        title = (TextView) findViewById(R.id.title);
        titlehead = (RelativeLayout) findViewById(R.id.titlehead);
        et_name = (EditText) findViewById(R.id.et_name);
        et_num = (EditText) findViewById(R.id.et_num);
        et_address = (EditText) findViewById(R.id.et_address);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        num = et_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( num.length() != 11 )
        {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        address = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something

        DialogManager.getInstance().showAddressEnter(this, name, num, address, callBack);



    }


    private CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {

            HashMap<String, Object> map = new HashMap<>();

            String userId = Common.getUserId();

            map.put("user_id", userId);
            map.put("welfare_id", info.welfare_id);
            map.put("type", info.type);
            map.put("platfrom", "Android");
            map.put("username", name);
            map.put("phone", num);
            map.put("address", address);


            LSRequestManager.getInstance().getIntegral(map, new CallBack() {
                @Override
                public void handler(MyTask mTask) {
                    MyBenefitAddAddress.this.setResult(RESULT_OK);
                    finish();
                }
            });

        }
    };

}
