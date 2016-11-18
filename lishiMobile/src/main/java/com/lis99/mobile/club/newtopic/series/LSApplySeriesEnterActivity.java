package com.lis99.mobile.club.newtopic.series;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.activityinfo.PayModel;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.club.model.OrderInfoModel;
import com.lis99.mobile.club.model.PayEnterOrderModel;
import com.lis99.mobile.club.model.SpecInfoListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ParserUtil;
import com.lis99.mobile.util.PayUtil;
import com.lis99.mobile.wxapi.WXPayEntryActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/18.
 *  支付界面
 *
 */
public class LSApplySeriesEnterActivity extends LSBaseActivity {

    private TextView tv_title, /*tv_pay*/ tv_joinNum, tv_price;

//    private GridView grid;

    private ListView list;

    private RadioGroup radioGroup;

    private View layout_off_line, layout_weixin, layout_zhifubao, layout_free;

    private RadioButton radio_off_line, radio_weixin, radio_zhifubao, currentRadio, radio_free;

    private Button btn_ok;

    private int topicID, clubID, batchID;

    private int jonNum;

    public OrderInfoModel model;

    public PayEnterOrderModel bModel;

    private int payType = -1;
    //    不显示网上支付
    private boolean test = false;


    //    新加的
    private EditText et_info;

    private PayModel payModel;

    private TextView tv_date, tv_joins, tvAllPrice, tvPayPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_apply_enter_new);

        initViews();

        layout_off_line.setVisibility(View.GONE);
        layout_weixin.setVisibility(View.GONE);
        layout_zhifubao.setVisibility(View.GONE);
        layout_free.setVisibility(View.GONE);

        setTitle("报名");

        payModel = (PayModel) getIntent().getSerializableExtra("PAYMODEL");

        topicID = payModel.topicId;//getIntent().getIntExtra("topicID", 0);
        clubID = payModel.clubId;//getIntent().getIntExtra("clubID", 0);
        batchID = payModel.batchId;//getIntent().getIntExtra("batchID", 0);

        jonNum = payModel.selectNum;//getIntent().getIntExtra("SELECTNUM", -1);


        getList();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String str = intent.getStringExtra("CLOSE");
        if (!TextUtils.isEmpty(str)) {
            sendResult();
        }

    }

    @Override
    protected void initViews() {
        super.initViews();

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_joins = (TextView) findViewById(R.id.tv_joins);
        tvPayPrice = (TextView) findViewById(R.id.tvPayPrice);
        tvAllPrice = (TextView) findViewById(R.id.tvAllPrice);


        tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_pay = (TextView) findViewById(tv_pay);
        tv_joinNum = (TextView) findViewById(R.id.tv_joinNum);
        tv_price = (TextView) findViewById(R.id.tv_price);

//        grid = (GridView) findViewById(grid);
        list = (ListView) findViewById(R.id.list);

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

        et_info = (EditText) findViewById(R.id.et_info);

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


    private void getList() {

        String url = C.GET_ORDER_INFO;

        url = C.GET_ORDER_INFO_NEW;

        model = new OrderInfoModel();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("topic_id", topicID);
        map.put("batch_id", batchID);


        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                model = (OrderInfoModel) mTask.getResultModel();
                if (model == null) return;

                tv_title.setText(model.title);

//                tv_pay.setText("人均费用" + model.consts + "元");

                tv_joinNum.setText("报名人员（共" + jonNum + "人）");

                //总价
                double allPrice = payModel.price;

                DecimalFormat df = new DecimalFormat("0.00");

                String priceAll = df.format(allPrice);

                tv_price.setText("共计：" + priceAll + "元");

                tvAllPrice.setText("费用总计￥"+priceAll);
                tvPayPrice.setText("实际支付￥"+priceAll);

                //显示支付方式
                int typeLength = model.type.length;

                for (int i = 0; i < typeLength; i++) {
                    int num = model.type[i];
                    if (num == 0) {
                        layout_free.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            currentRadio = radio_free;
                            currentRadio.setChecked(true);
                            payType = 0;
                        }
                    } else if (num == 1) {
                        layout_off_line.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            currentRadio = radio_off_line;
                            currentRadio.setChecked(true);
                            payType = 1;
                        }
                    } else if (num == 2 && !test) {
                        layout_weixin.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            currentRadio = radio_weixin;
                            currentRadio.setChecked(true);
                            payType = 2;
                        }
                    } else if (num == 3) {
                        layout_zhifubao.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            currentRadio = radio_zhifubao;
                            currentRadio.setChecked(true);
                            payType = 3;
                        }
                    }
                }

                String startTime = payModel.startTime;
                startTime = startTime.replace(".", "-");
                tv_date.setText(startTime);
                String joins = "";
                int num = payModel.joinList.size();
                for ( int i = 0; i < num; i++ )
                {
                    SpecInfoListModel.GuigelistEntity item = payModel.joinList.get(i);
                    joins += item.name+"X"+item.selectNum+"，"+item.price+"/人";
                    if ( i < num - 1 )
                    {
                        joins += "\n";
                    }
                }
                tv_joins.setText(joins);

                ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();

//                for (int i = 0; i < jonNum; i++) {
                int nums = payModel.updata.size();
                for (int i = 0; i < nums; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    NewApplyUpData info = payModel.updata.get(i);
                    String name = info.name;
                    if ( !TextUtils.isEmpty(info.sex) )
                    {
                        name += "，"+info.sex;
                    }
                    if ( !TextUtils.isEmpty(info.mobile) )
                    {
                        name += "，"+info.mobile;
                    }
                    if ( !TextUtils.isEmpty(info.credentials) )
                    {
                        name += "\n"+info.credentials;
                    }
                    map.put("name", name);
                    item.add(map);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(activity, item, R.layout.apply_enter_list_item, new String[]{"name"}, new int[]{R.id.text});
                list.setAdapter(simpleAdapter);
//                grid.setAdapter(simpleAdapter);
            }
        });


    }

    private void cleanList() {

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.btn_ok:

                //            上传设备信息
                LSRequestManager.getInstance().upDataInfo();

                if ( bModel != null && !TextUtils.isEmpty(bModel.ordercode) )
                {
                    goPayNow();
                    return;
                }
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
     * 提交报名信息
     */
    private void SubmitOrderList() {

        if (payType == -1) {
            Common.toast("请选择支付方式");
            return;
        }

        String userId = DataManager.getInstance().getUser().getUser_id();

        int client_version = DeviceInfo.CLIENTVERSIONCODE;

        String platform = DeviceInfo.PLATFORM;

        String url = C.ADD_ACTIVE_SERIES_LINE;
//      报名信息
        String OrderList = ParserUtil.getGsonString(payModel.updata);
        OrderList = ParserUtil.getJsonArrayWithName("lists", OrderList);
        Common.log("OrderList==" + OrderList);
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("activity_id", topicID);
        map.put("user_id", userId);
//        map.put("club_id", clubID);
        map.put("batch_id", batchID);
        map.put("pay_type", payType);
        map.put("client_version", client_version);
        map.put("platform", platform);
        map.put("apply_info", OrderList);

//        规格
        map.put("guige_info", ParserUtil.getGsonString(payModel.batchs));
//        备注
        map.put("remark", et_info.getText().toString());

        bModel = new PayEnterOrderModel();

        MyRequestManager.getInstance().requestPost(url, map, bModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                bModel = (PayEnterOrderModel) mTask.getResultModel();
                if (bModel != null) {

                    if (payType == 0 || payType == 1) {
                        Common.toast("报名成功");
                        sendResult();
                    }
                    else {
                        goPayNow();
                    }


                }
            }
        });


    }


    public void goPayNow() {
        if (payType == 2) {
            if (TextUtils.isEmpty(bModel.ordercode)) {
                Common.toast("订单号获取失败");
                return;
            }

//                        sendResult();
            WXPayEntryActivity.PayBackA = LSApplySeriesEnterActivity.this;

            PayUtil.getInstance().payWeiXin(bModel.ordercode);
        } else if (payType == 3) {
            if (TextUtils.isEmpty(bModel.ordercode)) {
                Common.toast("订单号获取失败");
                return;
            }

//                        sendResult();
            WXPayEntryActivity.PayBackA = LSApplySeriesEnterActivity.this;

            PayUtil.getInstance().payZhiFuBao(bModel.ordercode);
        }
    }

    private void sendResult() {
        setResult(RESULT_OK);
        finish();
    }


}
