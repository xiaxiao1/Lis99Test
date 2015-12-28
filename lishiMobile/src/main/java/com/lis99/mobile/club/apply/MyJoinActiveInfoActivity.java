package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.MyJoinActiveDetailModel;
import com.lis99.mobile.club.widget.applywidget.MyJoinActiveDetailItem;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.PayUtil;
import com.lis99.mobile.wxapi.WXPayEntryActivity;

import java.util.HashMap;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveInfoActivity extends LSBaseActivity
{

    private TextView tv_title, tv_pay, tv_pay_all, tv_join_state, tv_joinNum, tv_pay_type, tv_pay_state, tv_pay_info, tv_pay_phone;
    private ImageView iv_pay_type;
    private ListView list;

    private MyJoinActiveDetailModel model;

    private MyJoinActiveDetailItem adapter;

    private int orderid;
//    支付状态（0待支付1已支付 2退款已完成 3退款申请中 4 线下支付 5免费活动 6 退款中 7逾期未支付 8 无法付款 9 无法退款）
    public static String[] PAY_TYPE = new String[]{
            "待支付","已支付","2退款已完成","退款申请中","线下支付","免费活动", "退款中", "逾期未支付", "无法付款", "无法退款",
    };

    private Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_jion_active_detail);

        initViews();

        setTitle("我报名的活动");

        orderid = getIntent().getIntExtra("ORDERID", -1);

        getList();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        String str = intent.getStringExtra("CLOSE");
        if ( !TextUtils.isEmpty(str) )
        {
            getList();
        }

    }

    private void getList ()
    {
        String url = C.MY_APPLY_DETAIL;

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("orderid", orderid);

        model = new MyJoinActiveDetailModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (MyJoinActiveDetailModel) mTask.getResultModel();
                if ( model == null ) return;
                tv_title.setText(model.title);
                tv_pay.setText(model.consts + "元 x" + model.applyNum);
                tv_pay_all.setText("费用总计"+model.totprice + "元");

                adapter = new MyJoinActiveDetailItem(activity, model.apply_info);
                list.setAdapter(adapter);

                tv_joinNum.setText("报名人员共（" + model.applyNum + "）");


                if (TextUtils.isEmpty(model.payhint))
                {
                    tv_pay_info.setVisibility(View.INVISIBLE);
                }
                else
                {
                    tv_pay_info.setVisibility(View.VISIBLE);
                    tv_pay_info.setText(model.payhint);
                }


                if ( model.pay_status >= 0 && model.pay_status < PAY_TYPE.length )
                {
                    tv_pay_state.setText( PAY_TYPE[model.pay_status] );
//                  微支付
                    if ( model.pay_status == 0 )
                    {
                        btn_pay.setVisibility(View.VISIBLE);
                    }
                    else {
                        btn_pay.setVisibility(View.GONE);
                    }

                }

                switch (model.pay_type )
                {
                    case 0:
                        tv_pay_type.setText("免费活动");
                        iv_pay_type.setImageResource(R.drawable.pay_free);
                        break;
                    case 1:
                        tv_pay_type.setText("线下支付");
                        iv_pay_type.setImageResource(R.drawable.pay_off_line);
                        break;
                    case 2:
                        tv_pay_type.setText("微信支付");
                        iv_pay_type.setImageResource(R.drawable.pay_weixin);
                        break;
                    case 3:
                        tv_pay_type.setText("支付宝");
                        iv_pay_type.setImageResource(R.drawable.pay_zhifubao);
                        break;
                }
                //报名状态
                switch (model.flag)
                {
                    case 1:
                        tv_join_state.setText("已审核");
                        break;
                    case -1:
                        tv_join_state.setText("被拒绝");
                        break;
                    case 2:
                        tv_join_state.setText("待审核");
                        break;
                }



            }
        });

    }

    private void cleanList ()
    {
        list.setAdapter(null);
        if ( adapter != null )
        adapter.clean();
        adapter = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanList();
    }

    @Override
    protected void initViews() {
        super.initViews();

        btn_pay = (Button) findViewById(R.id.btn_pay);

        tv_pay_info = (TextView) findViewById(R.id.tv_pay_info);

        tv_pay_phone = (TextView) findViewById(R.id.tv_pay_phone);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_pay_all = (TextView) findViewById(R.id.tv_pay_all);
        tv_join_state = (TextView) findViewById(R.id.tv_join_state);
        tv_joinNum = (TextView) findViewById(R.id.tv_joinNum);
        tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
        tv_pay_state = (TextView) findViewById(R.id.tv_pay_state);


        iv_pay_type = (ImageView) findViewById(R.id.iv_pay_type);
        list = (ListView) findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null ) return;
                MyJoinActiveDetailModel.Apply_info item = (MyJoinActiveDetailModel.Apply_info) adapter.getItem(i);
                if ( item == null ) return;
                Intent intent = new Intent(activity, MyJoinActivePeopleInfo.class);
                intent.putExtra("PEOPLEINFO", item);
                intent.putExtra("NUM", (i + 1));
                startActivity(intent);
            }
        });


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model == null || TextUtils.isEmpty(model.ordercode)) {
                    Common.toast("订单获取失败");
                    return;
                }
                switch (model.pay_type) {
                    case 2:

                        WXPayEntryActivity.PayBackA = MyJoinActiveInfoActivity.this;

                        PayUtil.getInstance().payWeiXin(model.ordercode);

                        break;
                    case 3:

                        WXPayEntryActivity.PayBackA = MyJoinActiveInfoActivity.this;

                        PayUtil.getInstance().payZhiFuBao(model.ordercode);

                        break;
                }
            }
        });

        tv_pay_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-53525135"));
                startActivity(intent);
            }
        });

    }
}
