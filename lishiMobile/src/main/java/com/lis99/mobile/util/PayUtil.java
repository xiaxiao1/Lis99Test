package com.lis99.mobile.util;

import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;

/**
 * Created by yy on 15/12/7.
 *          支付工具， 微信， 支付宝
 */
public class PayUtil {

    private static PayUtil instance;

    private AsyncHttpClient client = new AsyncHttpClient();

    private String access_token;

    private IWXAPI api;

    private String appId, partnerId, prepayId, nonceStr, timeStamp, sign = "26f33f45de7f2a851319b647a28a5cdb";

    public PayUtil ()
    {
        if ( client == null )
        {
            client = new AsyncHttpClient();
        }
        if ( api == null )
        {
            api = WXAPIFactory.createWXAPI(LSBaseActivity.activity, C.WEIXIN_APP_ID);
        }
//        APP_ID = Base64.encodeToString(APP_ID.getBytes(), Base64.DEFAULT);
//        byte[] app_id_byte = Base64.decode(APP_ID.getBytes(), Base64.DEFAULT);
//        Common.log("app_id_byte="+app_id_byte.toString());
    }


    public static PayUtil getInstance ()
    {
        if ( instance == null ) instance = new PayUtil();
        return instance;
    }

    public void getWXToken ()
    {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ C.WEIXIN_APP_ID +"&secret=" + C.WEIXIN_APP_KEY;

        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                try {
                    JsonNode root = LSFragment.mapper.readTree(s);
                    if ( root.has("access_token"))
                    {
                        access_token = root.get("access_token").asText();
                    }
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }
                finally {

                }

            }
        });
    }

    public void payWeiXin ()
    {
        if ( !api.isWXAppInstalled())
        {
            Toast.makeText(LSBaseActivity.activity, "未安装微信", Toast.LENGTH_LONG).show();
            return;
        }

        api.registerApp(C.WEIXIN_APP_ID);

        PayReq req = new PayReq();

        req.appId = appId;

        req.partnerId = partnerId;

        req.prepayId = prepayId;

        req.nonceStr = nonceStr;

        req.timeStamp = timeStamp;

        req.packageValue = "Sign=WXPay";

        req.sign = sign;

        api.sendReq(req);

    }

    public void payZhiFuBao ()
    {

    }



}
