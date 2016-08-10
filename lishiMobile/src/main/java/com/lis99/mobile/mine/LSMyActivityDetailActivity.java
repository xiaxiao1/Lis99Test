package com.lis99.mobile.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.mine.adapter.LSMyActivityPersonAdapter;
import com.lis99.mobile.mine.model.LSMyActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.PayUtil;
import com.lis99.mobile.wxapi.WXPayEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

public class LSMyActivityDetailActivity extends LSBaseActivity implements CompoundButton.OnCheckedChangeListener {

    TextView titleView, priceView, totalView, orderIDView, statusView, personTitleView, payTypeView, payStatusView;

    LSMyActivityPersonAdapter adapter;
    ListView listView;
    LSMyActivity activity;
    int orderID;

    Button btn_pay;

    static Map<Integer, String> commentStatus = new HashMap<Integer, String>();
    static Map<Integer, String> payStatus = new HashMap<Integer, String>();
    static Map<Integer, String> payTypes = new HashMap<Integer, String>();

    static {
        commentStatus.put(-1, "被拒绝");
        commentStatus.put(1, "通过");
        commentStatus.put(2, "待审核");

        payStatus.put(0, "支付中");
        payStatus.put(1, "已支付");
        payStatus.put(2, "退款已完成");
        payStatus.put(3, "退款申请中");
        payStatus.put(4, "线下支付");
        payStatus.put(5, "免费活动");
        payStatus.put(6, "退款处理中");
        payStatus.put(7, "逾期未支付");
        payStatus.put(8, "订单关闭");
        payStatus.put(9, "无法退款");
        payStatus.put(10, "取消报名");

        payTypes.put(0, "免费活动");
        payTypes.put(1, "线下支付");
        payTypes.put(2, "微信支付");
        payTypes.put(3, "支付宝支付");
    }


    RoundedImageView headerView;
    Button closeButton, doneButton, commentButton;
    EditText contentView;
    CheckBox diffcultView, serviceView, feeView, tripView;
    View tucaoPanel, labelPanel, commentPanel, commentView, myCommentPanel;
    TextView rateInfoView, comentTitleView, labelView3, labelView2, labelView1, nameView, myCommentView;
    RatingBar ratingBar, myRateBar;

//    备注    取消原因
    private TextView tv_info, tv_cancel;
//    取消报名
    private Button btn_cancel;

    private View layout_cancel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsmy_activity_detail);
        this.orderID = getIntent().getIntExtra("orderID", 0);
        initViews();
        setTitle("我报名的活动");
        getData();
    }

    @Override
    protected void initViews() {
        super.initViews();

        titleView = (TextView) findViewById(R.id.titleView);
        titleView.setOnClickListener(this);
        priceView = (TextView) findViewById(R.id.priceView);
        totalView = (TextView) findViewById(R.id.totalView);
        orderIDView = (TextView) findViewById(R.id.orderIDView);
        statusView = (TextView) findViewById(R.id.statusView);
        personTitleView = (TextView) findViewById(R.id.personTitleView);
        payTypeView = (TextView) findViewById(R.id.payTypeView);
        payStatusView = (TextView) findViewById(R.id.payStatusView);
        commentButton = (Button) findViewById(R.id.commentButton);

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null ) return;
//                MyJoinActiveDetailModel.Apply_info item = (MyJoinActiveDetailModel.Apply_info) adapter.getItem(i);
                LSMyActivity.Applicant item = (LSMyActivity.Applicant) adapter.getItem(i);

                if ( item == null ) return;
                Intent intent = new Intent(LSMyActivityDetailActivity.this, MyJoinActivePeopleInfo.class);
                intent.putExtra("PEOPLEINFO", item);
                intent.putExtra("NUM", (i + 1));
                startActivity(intent);
            }
        });


        commentView = findViewById(R.id.commentView);
        commentPanel = commentView.findViewById(R.id.commentPanel);
        headerView = (RoundedImageView) commentView.findViewById(R.id.headerView);
        closeButton = (Button) commentView.findViewById(R.id.closeButton);


        doneButton = (Button) commentPanel.findViewById(R.id.doneButton);
        contentView = (EditText) commentPanel.findViewById(R.id.contentView);

        diffcultView = (CheckBox) commentView.findViewById(R.id.diffcultView);
        serviceView = (CheckBox) commentView.findViewById(R.id.serviceView);
        feeView = (CheckBox) commentView.findViewById(R.id.feeView);
        tripView = (CheckBox) commentView.findViewById(R.id.tripView);

        tucaoPanel = commentPanel.findViewById(R.id.tucaoPanel);
        labelPanel = commentPanel.findViewById(R.id.labelPanel);

        rateInfoView = (TextView) commentPanel.findViewById(R.id.rateInfoView);
        comentTitleView = (TextView) commentPanel.findViewById(R.id.comentTitleView);
        labelView3 = (TextView) commentPanel.findViewById(R.id.labelView3);
        labelView2 = (TextView) commentPanel.findViewById(R.id.labelView2);
        labelView1 = (TextView) commentPanel.findViewById(R.id.labelView1);
        nameView = (TextView) commentPanel.findViewById(R.id.nameView);

        serviceView.setOnCheckedChangeListener(this);
        diffcultView.setOnCheckedChangeListener(this);
        feeView.setOnCheckedChangeListener(this);
        tripView.setOnCheckedChangeListener(this);

        tv_info = (TextView) findViewById(R.id.tv_info);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        layout_cancel = findViewById(R.id.layout_cancel);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);


        ratingBar = (RatingBar) commentPanel.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                switch ((int)rating){
                    case 1:
                        contentView.setHint("在这里吐槽...");
                        tucaoPanel.setVisibility(View.VISIBLE);
                        rateInfoView.setText("“强烈不满，我要吐槽”");
                        break;
                    case 2:
                        contentView.setHint("在这里吐槽...");
                        tucaoPanel.setVisibility(View.VISIBLE);
                        rateInfoView.setText("“不是很满意”");
                        break;
                    case 3:
                        contentView.setHint("在这里吐槽...");
                        tucaoPanel.setVisibility(View.VISIBLE);
                        rateInfoView.setText("“一般般吧”");
                        break;
                    case 4:
                        contentView.setHint("在这里吐槽...");
                        tucaoPanel.setVisibility(View.VISIBLE);
                        rateInfoView.setText("“还不错吧”");
                        break;
                    case 5:
                        contentView.setHint("您宝贵的建议...");
                        tucaoPanel.setVisibility(View.GONE);
                        rateInfoView.setText("“难忘的体验”");
                        break;
                    default:
                        break;
                }
            }
        });

        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity == null || TextUtils.isEmpty(activity.ordercode)) {
                    Common.toast("订单获取失败");
                    return;
                }
                switch (activity.pay_type) {
                    case 2:

                        WXPayEntryActivity.PayBackA = LSMyActivityDetailActivity.this;

                        PayUtil.getInstance().payWeiXin(activity.ordercode);

                        break;
                    case 3:

                        WXPayEntryActivity.PayBackA = LSMyActivityDetailActivity.this;

                        PayUtil.getInstance().payZhiFuBao(activity.ordercode);

                        break;
                }
            }
        });

        myCommentPanel = findViewById(R.id.myCommentPanel);
        myRateBar = (RatingBar) findViewById(R.id.myRatingBar);
        myRateBar.setEnabled(false);
        myCommentView = (TextView) findViewById(R.id.myCommentView);
    }

    private void getData ()
    {
        if (!Common.isLogin(this))
        {
            return;
        }



        String url = C.MY_ORDER_DETAIL;

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("orderid", this.orderID);
        map.put("user_id", DataManager.getInstance().getUser().getUser_id());

        activity = new LSMyActivity();

        MyRequestManager.getInstance().requestPost(url, map, activity, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                activity = (LSMyActivity) mTask.getResultModel();
                adapter = new LSMyActivityPersonAdapter(LSMyActivityDetailActivity.this, activity.apply_info);
                listView.setAdapter(adapter);

                tv_info.setText(activity.remark);

                personTitleView.setText("报名人员（共" + (activity.apply_info == null ? 0 : activity.apply_info.size()) + "）");

                titleView.setText(activity.title);
                priceView.setText("第"+ activity.batch +"批,"+activity.consts + "元 ×" + (activity.apply_info == null ? 0 : activity.apply_info.size()));
                totalView.setText("费用总计" + activity.totprice + "元");

                orderIDView.setText(activity.ordercode);


                statusView.setText(commentStatus.get(activity.flag));
                payStatusView.setText(payStatus.get(activity.pay_status));
//                取消支付， 显示取消支付原因
                if ( activity.pay_status == 10 )
                {
                    layout_cancel.setVisibility(View.VISIBLE);
                    tv_cancel.setText(activity.reason);
                }
                else
                {
                    layout_cancel.setVisibility(View.GONE);
                }


                payTypeView.setText(payTypes.get(activity.pay_type));

                if (activity.leader_userid != 0) {
                    ImageLoader.getInstance().displayImage(activity.leader_headicon, headerView, ImageUtil.getclub_topic_headImageOptions());
                    comentTitleView.setText(activity.title);
                    nameView.setText(activity.leader_nickname);
                } else {
                    ImageLoader.getInstance().displayImage(activity.create_user_headicon, headerView, ImageUtil.getclub_topic_headImageOptions());
                    comentTitleView.setText(activity.title);
                    nameView.setText(activity.create_user_nickname);
                }


                if (activity.leader_tag == null || activity.leader_tag.size() == 0) {
                    labelPanel.setVisibility(View.GONE);
                } else {
                    labelPanel.setVisibility(View.VISIBLE);
                    labelView1.setVisibility(View.VISIBLE);
                    labelView1.setText(activity.leader_tag.get(0));
                    if (activity.leader_tag.size() > 1) {
                        labelView2.setVisibility(View.VISIBLE);
                        labelView2.setText(activity.leader_tag.get(1));
                        if (activity.leader_tag.size() > 2) {
                            labelView3.setVisibility(View.VISIBLE);
                            labelView3.setText(activity.leader_tag.get(2));
                        } else {
                            labelView3.setVisibility(View.GONE);
                        }
                    } else {
                        labelView2.setVisibility(View.GONE);
                        labelView3.setVisibility(View.GONE);
                    }
                }


                Drawable drawable = null;
                switch (activity.pay_type )
                {
                    case 0:
                        payTypeView.setText("免费活动");
                        drawable = getResources().getDrawable(R.drawable.pay_free);
                        break;
                    case 1:
                        payTypeView.setText("线下支付");
                        drawable = getResources().getDrawable(R.drawable.pay_off_line);
                        break;
                    case 2:
                        payTypeView.setText("微信支付");
                        drawable = getResources().getDrawable(R.drawable.pay_weixin);
                        break;
                    case 3:
                        payTypeView.setText("支付宝");
                        drawable = getResources().getDrawable(R.drawable.pay_zhifubao);
                        break;
                }

                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                    payTypeView.setCompoundDrawables(null, null, drawable, null);
                }

                if (activity.is_comment == 1) {
                    commentButton.setVisibility(View.VISIBLE);
                } else {
                    commentButton.setVisibility(View.GONE);
                }

                if (activity.pay_status == 0) {
                    btn_pay.setVisibility(View.VISIBLE);
                    btn_cancel.setVisibility(View.VISIBLE);
                } else {
                    btn_pay.setVisibility(View.GONE);
                    btn_pay.setVisibility(View.GONE);
                }

                if (activity.star > 0) {
                    myCommentPanel.setVisibility(View.VISIBLE);
                    myRateBar.setRating(activity.star);
                    if ( !TextUtils.isEmpty(activity.comment) )
                    myCommentView.setText("“" + activity.comment + "”");
                } else {
                    myCommentPanel.setVisibility(View.GONE);
                }

//                listView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setAdapterHeightBasedOnChildren1(listView);
//                    }
//                });
            }
        });

    }


    public void setAdapterHeightBasedOnChildren1(ListView listView) {

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void sendComment(){
        int rate = (int)ratingBar.getRating();
        if (rate == 0) {
            Toast.makeText(this, "请先打个星级吧", Toast.LENGTH_LONG).show();
            return;
        }

        StringBuffer sb = new StringBuffer("");
        if (rate < 5) {
            if (tripView.isChecked()) {
                if (sb.length() == 0) {
                    sb.append("行程问题");
                } else {
                    sb.append("_行程问题");
                }
            }
            if (diffcultView.isChecked()) {
                if (sb.length() == 0) {
                    sb.append("难度问题");
                } else {
                    sb.append("_难度问题");
                }
            }
            if (feeView.isChecked()) {
                if (sb.length() == 0) {
                    sb.append("费用问题");
                } else {
                    sb.append("_费用问题");
                }
            }
            if (serviceView.isChecked()) {
                if (sb.length() == 0) {
                    sb.append("服务态度问题");
                } else {
                    sb.append("_服务态度问题");
                }
            }
        }

        String content = contentView.getEditableText().toString();
        if (content == null) {
            content = "";
        }


        String url = C.MY_ACTIVITY_ADD_COMMENT;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("comment", sb.toString());
        map.put("custom", content);
        map.put("star", rate);
        map.put("user_id", DataManager.getInstance().getUser().getUser_id());
        map.put("ordercode", activity.ordercode);
        map.put("login_id", 153);
        map.put("leader_userid", activity.leader_userid);
        map.put("create_userid", activity.create_userid);
        map.put("club_id", activity.club_id);
        if (activity.activity_code != null) {
            map.put("activity_code", activity.activity_code);
        }

        final BaseModel model = new BaseModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if (model.error != null && model.error.length() > 0) {
                    Toast.makeText(LSMyActivityDetailActivity.this, model.error, Toast.LENGTH_LONG).show();
                    return;
                }
                commentView.setVisibility(View.GONE);
                Common.toast("感谢您的评价");
                getData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                this.commentView.setVisibility(View.GONE);
                break;
            case R.id.doneButton:
                this.sendComment();
                break;
            case R.id.commentButton:
                this.commentView.setVisibility(View.VISIBLE);
                break;
            case R.id.titleView:
                Common.goTopic(this, 4, activity.topic_id);
                break;
//            取消报名
            case R.id.btn_cancel:
                DialogManager.getInstance().cancelApplyDialog(activity.orderid, new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {
                        //更新数据
                        getData();
                    }
                });

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setTextColor(Color.rgb(0x2b, 0xca, 0x63));
        } else {
            buttonView.setTextColor(Color.parseColor("#999999"));
        }
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

    private void sendResult() {
        setResult(RESULT_OK);
        finish();
    }


}
