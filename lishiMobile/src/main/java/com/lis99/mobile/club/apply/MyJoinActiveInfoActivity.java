package com.lis99.mobile.club.apply;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.lis99.mobile.util.MyRequestManager;

import java.util.HashMap;

/**
 * Created by yy on 15/12/1.
 */
public class MyJoinActiveInfoActivity extends LSBaseActivity
{

    private TextView tv_title, tv_pay, tv_pay_all, tv_join_state, tv_joinNum, tv_pay_type, tv_pay_state;
    private ImageView iv_pay_type;
    private ListView list;

    private MyJoinActiveDetailModel model;

    private MyJoinActiveDetailItem adapter;

    private int orderid;

    private String[] PAY_TYPE = new String[]{
            "未支付","已支付","已退款","退款中","线下支付","免费活动",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_jion_active_detail);

        initViews();

        setTitle("我报名的活动");

        orderid = getIntent().getIntExtra("ORDERID", -1);

        getList();

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

                if ( model.pay_status > 0 && model.pay_status < PAY_TYPE.length )
                {
                    tv_pay_state.setText( PAY_TYPE[model.pay_status] );
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

    }
}
