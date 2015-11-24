package com.lis99.mobile.club.apply;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.OrderInfoModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/11/18.
 */
public class LSApplyEnterActivity extends LSBaseActivity{

    private TextView title, tv_pay, tv_joinNum, tv_price;

    private GridView grid;

    private RadioGroup radioGroup;

    private View layout_off_line, layout_weixin, layout_zhifubao;

    private RadioButton radio_off_line, radio_weixin, radio_zhifubao, currentRadio;

    private Button btn_ok;

    private int topicID, clubID;

    private int jonNum;

    public OrderInfoModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lsclub_apply_enter_main);

        initViews();

        radio_off_line.setVisibility(View.GONE);
        radio_weixin.setVisibility(View.GONE);
        radio_zhifubao.setVisibility(View.GONE);

        setTitle("报名");

        topicID = getIntent().getIntExtra("topicID", 0);
        clubID = getIntent().getIntExtra("clubID", 0);

        jonNum = LSApplayNew.updata.size();

        getList();

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

        radio_off_line = (RadioButton) findViewById(R.id.radio_off_line);
        radio_weixin = (RadioButton) findViewById(R.id.radio_weixin);
        radio_zhifubao = (RadioButton) findViewById(R.id.radio_zhifubao);

        currentRadio = radio_off_line;

        layout_off_line.setOnClickListener(this);
        layout_weixin.setOnClickListener(this);
        layout_zhifubao.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String name = "0000000";
                switch (i) {
                    case R.id.layout_off_line:
                        if (currentRadio == radio_off_line) return;
                        currentRadio.setChecked(false);
                        radio_off_line.setChecked(true);
                        currentRadio = radio_off_line;
                        name = "111111";
                        break;
                    case R.id.layout_weixin:
                        if (currentRadio == radio_weixin) return;
                        currentRadio.setChecked(false);
                        radio_weixin.setChecked(true);
                        currentRadio = radio_weixin;
                        name = "222222";
                        break;
                    case R.id.layout_zhifubao:
                        if (currentRadio == radio_zhifubao) return;
                        currentRadio.setChecked(false);
                        currentRadio = radio_zhifubao;
                        currentRadio.setChecked(true);
                        name = "33333";
                        break;
                    default:
                        name = "4444444444";
                        break;
                }
                Common.toast("radio position is " + name);
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

                title.setText(model.title);

                tv_pay.setText("人均费用"+model.consts+"元");

                tv_joinNum.setText("报名人员（共"+jonNum+"人）");

                //总价
                float allPrice = model.consts * jonNum;

                allPrice = (float)(Math.round(allPrice*100)/100);

                tv_price.setText("总计："+allPrice+"元");

                //显示支付方式
                int typeLength = model.type.length;

                for ( int i = 0; i < typeLength; i++ )
                {
                    int num = model.type[i];
                    if ( num == 0 )
                    {

                    }
                    else if ( num == 1 )
                    {
                        radio_off_line.setVisibility(View.VISIBLE);
                    }
                    else if ( num == 2 )
                    {
                        radio_weixin.setVisibility(View.VISIBLE);
                    }
                    else if ( num == 3 )
                    {
                        radio_zhifubao.setVisibility(View.VISIBLE);
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
                break;
            case R.id.layout_off_line:
                radioGroup.check(layout_off_line.getId());
//                radio_off_line.setChecked(true);
                break;
            case R.id.layout_weixin:
                radioGroup.check(layout_weixin.getId());
//                radio_weixin.setChecked(true);
                break;
            case R.id.layout_zhifubao:
                radioGroup.check(layout_zhifubao.getId());
//                radio_zhifubao.setChecked(true);
                break;
        }


    }
}
