package com.lis99.mobile.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.PayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;


public class WXPayEntryActivity extends LSBaseActivity implements IWXAPIEventHandler{

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private TextView tv_pay_type, tv_info, tv_content;

    private Button btn_ok, btn_ok1;

    public static Activity PayBackA;

    private View layout_ok, layout_cancel;
//<font color="red">I love android</font><br>
    private String infoZFB; // = "<font color=\"#73706e\">请在“</font><font color=\"#ff7800\">我----我报名的活动</font><font color=\"#73706e\">”中继续完成支付。<br>超过2小时未支付，报名将被拒绝，需要重新报名。<br>如果您需要帮助，请联系"+getResources().getString(R.string.tel)+"</font>";
    private String infoWX; // = "<font color=\"#73706e\">请在“</font><font color=\"#ff7800\">我----我报名的活动</font><font color=\"#73706e\">”中继续完成支付。<br>超过2小时未支付，报名将被拒绝，需要重新报名。<br>如果您需要帮助，请联系"+getResources().getString(R.string.tel)+"</font>";

    private String infoOk; // = "希望您玩得愉快\\n如果您需要帮助 请联系"+getResources().getString(R.string.tel);

    private int state;

    private final int sZFB = 1;
    private final int sWX = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weixin_pay_entry_main);

        super.initViews();

        infoZFB = "<font color=\"#73706e\">请在“</font><font color=\"#ff7800\">我----我报名的活动</font><font color=\"#73706e\">”中继续完成支付。<br>超过2小时未支付，报名将被拒绝，需要重新报名。<br>如果您需要帮助，请联系"+getResources().getString(R.string.tel)+"</font>";

        infoWX = "<font color=\"#73706e\">请在“</font><font color=\"#ff7800\">我----我报名的活动</font><font color=\"#73706e\">”中继续完成支付。<br>超过2小时未支付，报名将被拒绝，需要重新报名。<br>如果您需要帮助，请联系"+getResources().getString(R.string.tel)+"</font>";

        infoOk = "希望您玩得愉快\n如果您需要帮助 请联系"+getResources().getString(R.string.tel);

        setTitle("支付结果");

        layout_ok = findViewById(R.id.layout_ok);

        layout_cancel = findViewById(R.id.layout_cancel);

        tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);

        tv_info = (TextView) findViewById(R.id.tv_info);

        tv_content = (TextView) findViewById(R.id.tv_content);

        btn_ok1 = (Button) findViewById(R.id.btn_ok1);

        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
//      支付宝传进来用到的
        int code = getIntent().getIntExtra("CODE", -100);
        if ( code != -100 )
        {
            state = sZFB;
            setPayStatus(code);
        }

    }

    @Override
    protected void leftAction() {
        super.leftAction();
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//        Common.log("onPayFinish======="+resp.toString());

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("提示");
//            String result = String.format("微信支付结果：%s", String.valueOf(resp.errCode));
//            builder.setMessage(result);
//            builder.show();
            state = sWX;
            setPayStatus(resp.errCode);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( PayBackA != null )
        {
            Intent intent = new Intent(activity, PayBackA.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("CLOSE", "close");
            startActivity(intent);
        }
        if ( PayBackA != null )
        {
            PayBackA = null;
        }
        finish();
    }

    private void setPayStatus ( int code )
    {
        switch ( code )
        {
//                支付成功
            case 0:
                setTitle("支付成功");
                tv_content.setText(infoOk);
                layout_ok.setVisibility(View.VISIBLE);
                layout_cancel.setVisibility(View.GONE);
                // 发送定单号给服务器
                sendOk2Service();
                break;
//                支付失败
            case -1:

                tv_info.setText(Html.fromHtml(state == sWX ? infoWX : infoZFB));
                setTitle("支付失败");
                tv_pay_type.setText("支付失败");
                layout_ok.setVisibility(View.GONE);
                layout_cancel.setVisibility(View.VISIBLE);
                break;
//                取消支付
            case -2:
                tv_info.setText(Html.fromHtml(state == sWX ? infoWX : infoZFB));
                setTitle("支付取消");
                tv_pay_type.setText("支付取消");
                layout_ok.setVisibility(View.GONE);
                layout_cancel.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void sendOk2Service ()
    {
//        PayUtil.orderCode;
        String url = "http://api.lis99.com/v4/club/sendMobileInfo";

        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", PayUtil.orderCode);

        MyRequestManager.getInstance().requestPostNoModel(url, map, null, null);

    }

}