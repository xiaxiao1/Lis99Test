package com.lis99.mobile.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.C;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends LSBaseActivity implements IWXAPIEventHandler{

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private ImageView iv_img;

    private TextView tv_pay_state, tv_content;

    private Button btn_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);

        setContentView(R.layout.weixin_pay_entry_main);

        iv_img = (ImageView) findViewById(R.id.iv_img);

        tv_pay_state = (TextView) findViewById(R.id.tv_pay_state);

        tv_content = (TextView) findViewById(R.id.tv_content);

        btn_ok = (Button) findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("提示");
//            String result = String.format("微信支付结果：%s", String.valueOf(resp.errCode));
//            builder.setMessage(result);
//            builder.show();

            switch ( resp.errCode )
            {
//                支付成功
                case 0:
                    iv_img.setImageResource(R.drawable.pay_ok_img);
                    tv_content.setVisibility(View.GONE);
                    tv_pay_state.setText("支付成功");
                    btn_ok.setText("完成");
                break;
//                支付失败
                case -1:
                    iv_img.setImageResource(R.drawable.pay_refuse_img);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_pay_state.setText("支付失败");
                    btn_ok.setText("我知道了");
                break;
//                取消支付
                case -2:
                    iv_img.setImageResource(R.drawable.pay_cancel_img);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_pay_state.setText("支付已取消");
                    btn_ok.setText("我知道了");
                break;
            }


        }
    }
}