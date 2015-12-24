package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.OrderInfoModel;
import com.lis99.mobile.club.model.PayEnterOrderModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ParserUtil;
import com.lis99.mobile.util.PayUtil;
import com.lis99.mobile.wxapi.WXPayEntryActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplyEnterActivity extends LSBaseActivity{

    private TextView tv_title, tv_pay, tv_joinNum, tv_price;

    private GridView grid;

    private RadioGroup radioGroup;

    private View layout_off_line, layout_weixin, layout_zhifubao, layout_free;

    private RadioButton radio_off_line, radio_weixin, radio_zhifubao, currentRadio, radio_free;

    private Button btn_ok;

    private int topicID, clubID;

    private int jonNum;

    public OrderInfoModel model;

    public PayEnterOrderModel bModel;

    private int payType = -1;
//    不显示网上支付
    private boolean test = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_apply_enter_main);

        initViews();

        layout_off_line.setVisibility(View.GONE);
        layout_weixin.setVisibility(View.GONE);
        layout_zhifubao.setVisibility(View.GONE);
        layout_free.setVisibility(View.GONE);

        setTitle("报名");

        topicID = getIntent().getIntExtra("topicID", 0);
        clubID = getIntent().getIntExtra("clubID", 0);

        jonNum = LSApplayNew.updata.size();

        getList();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String str = intent.getStringExtra("CLOSE");
        if ( !TextUtils.isEmpty(str) )
        {
            sendResult();
        }

    }

    @Override
    protected void initViews() {
        super.initViews();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_joinNum = (TextView) findViewById(R.id.tv_joinNum);
        tv_price = (TextView) findViewById(R.id.tv_price);

        grid = (GridView) findViewById(R.id.grid);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        layout_off_line = findViewById(R.id.layout_off_line);
        layout_weixin = findViewById(R.id.layout_weixin);
        layout_zhifubao = findViewById(R.id.layout_zhifubao);
        layout_free = findViewById(R.id.layout_free);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        radio_off_line = (RadioButton) findViewById(R.id.radio_off_line);
        radio_weixin = (RadioButton) findViewById(R.id.radio_weixin);
        radio_zhifubao = (RadioButton) findViewById(R.id.radio_zhifubao);
        radio_free = (RadioButton) findViewById(R.id.radio_free);

//        currentRadio = radio_free;

        layout_free.setOnClickListener(this);
        layout_off_line.setOnClickListener(this);
        layout_weixin.setOnClickListener(this);
        layout_zhifubao.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String name = "0000000";
                switch (i) {
                    case R.id.layout_free:
                        if (currentRadio == radio_free) return;
                        currentRadio.setChecked(false);
                        radio_free.setChecked(true);
                        currentRadio = radio_free;
                        payType = 0;
                        name = "111111";
                        break;
                    case R.id.layout_off_line:
                        if (currentRadio == radio_off_line) return;
                        currentRadio.setChecked(false);
                        radio_off_line.setChecked(true);
                        currentRadio = radio_off_line;
                        name = "111111";
                        payType = 1;
                        break;
                    case R.id.layout_weixin:
                        if (currentRadio == radio_weixin) return;
                        currentRadio.setChecked(false);
                        radio_weixin.setChecked(true);
                        currentRadio = radio_weixin;
                        name = "222222";
                        payType = 2;
                        break;
                    case R.id.layout_zhifubao:
                        if (currentRadio == radio_zhifubao) return;
                        currentRadio.setChecked(false);
                        currentRadio = radio_zhifubao;
                        currentRadio.setChecked(true);
                        name = "33333";
                        payType = 3;
                        break;

                    default:
                        name = "4444444444";
                        break;
                }
//                Common.toast("radio position is " + name);
            }
        });



    }


    private void getList ()
    {

        String url = C.GET_ORDER_INFO;

        model = new OrderInfoModel();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("topic_id", topicID);


        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                model = (OrderInfoModel) mTask.getResultModel();
                if ( model == null ) return;

                tv_title.setText(model.title);

                tv_pay.setText("人均费用"+model.consts+"元");

                tv_joinNum.setText("报名人员（共"+jonNum+"人）");

                //总价
                float allPrice = model.consts * jonNum;

                DecimalFormat df = new DecimalFormat("0.00");

                String priceAll = df.format(allPrice);

                tv_price.setText("总计："+priceAll+"元");

                //显示支付方式
                int typeLength = model.type.length;

                for ( int i = 0; i < typeLength; i++ )
                {
                    int num = model.type[i];
                    if ( num == 0 )
                    {
                        layout_free.setVisibility(View.VISIBLE);
                        if ( i == 0 )
                        {
                            currentRadio = radio_free;
                            currentRadio.setChecked(true);
                            payType = 0;
                        }
                    }
                    else if ( num == 1 )
                    {
                        layout_off_line.setVisibility(View.VISIBLE);
                        if ( i == 0 )
                        {
                            currentRadio = radio_off_line;
                            currentRadio.setChecked(true);
                            payType = 1;
                        }
                    }
                    else if ( num == 2 && !test )
                    {
                        layout_weixin.setVisibility(View.VISIBLE);
                        if ( i == 0 )
                        {
                            currentRadio = radio_weixin;
                            currentRadio.setChecked(true);
                            payType = 2;
                        }
                    }
                    else if ( num == 3 )
                    {
                        layout_zhifubao.setVisibility(View.VISIBLE);
                        if ( i == 0 )
                        {
                            currentRadio = radio_zhifubao;
                            currentRadio.setChecked(true);
                            payType = 3;
                        }
                    }
                }

                ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < jonNum; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", LSApplayNew.updata.get(i).name);
                    item.add(map);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(activity, item, R.layout.apply_enter_grid_item, new String[]{"name"}, new int[]{R.id.text});

                grid.setAdapter(simpleAdapter);
            }
        });




    }

    private void cleanList ()
    {

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.btn_ok:
                SubmitOrderList();
                break;
            case R.id.layout_free:
                radioGroup.check(layout_free.getId());
                break;
            case R.id.layout_off_line:
                radioGroup.check(layout_off_line.getId());
                break;
            case R.id.layout_weixin:
                radioGroup.check(layout_weixin.getId());
                break;
            case R.id.layout_zhifubao:
                radioGroup.check(layout_zhifubao.getId());
                break;
        }
    }

    /**
     *   提交报名信息
     */
    private void SubmitOrderList ()
    {

        if ( payType == -1 )
        {
            Common.toast("请选择支付方式");
            return;
        }

        String userId = DataManager.getInstance().getUser().getUser_id();

        int client_version = DeviceInfo.CLIENTVERSIONCODE;

        String platform = DeviceInfo.PLATFORM;

        String url = C.SUBMIT_ORDER_INFO;

        String OrderList = ParserUtil.getGsonString(LSApplayNew.updata);
        OrderList = ParserUtil.getJsonArrayWithName("lists", OrderList);
        Common.log("OrderList==" + OrderList);
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("topic_id", topicID);
        map.put("user_id", userId);
        map.put("club_id", clubID);
        map.put("pay_type", payType);
        map.put("client_version", client_version);
        map.put("platform", platform);
        map.put("apply_info", OrderList);

        bModel = new PayEnterOrderModel();

        MyRequestManager.getInstance().requestPost(url, map, bModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                bModel = (PayEnterOrderModel) mTask.getResultModel();
                if ( bModel != null )
                {

                    if ( payType == 0 || payType == 1 )
                    {
                        Common.toast("报名成功");
                        sendResult();
                    }

                    else if ( payType == 2 )
                    {
                        if (TextUtils.isEmpty(bModel.ordercode))
                        {
                            Common.toast("订单号获取失败");
                            return;
                        }

//                        sendResult();
                        WXPayEntryActivity.PayBackA = LSApplyEnterActivity.this;

                        PayUtil.getInstance().payWeiXin(bModel.ordercode);
                    }
                    else if ( payType == 3 )
                    {
                        if (TextUtils.isEmpty(bModel.ordercode))
                        {
                            Common.toast("订单号获取失败");
                            return;
                        }

//                        sendResult();
                        WXPayEntryActivity.PayBackA = LSApplyEnterActivity.this;

                        PayUtil.getInstance().payZhiFuBao(bModel.ordercode);
                    }

                }
            }
        });




    }

    private void sendResult ()
    {
        setResult(RESULT_OK);
        finish();
    }


}
