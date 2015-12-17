package com.lis99.mobile.util;

import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.newhome.LSFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yy on 15/12/7.
 *          支付工具， 微信， 支付宝
 */
public class PayUtil {

    private static PayUtil instance;

    private AsyncHttpClient client = new AsyncHttpClient();

    private String access_token;

    private IWXAPI api;
//                                                                          C380BEC2BFD727A4B6845133519F3AD6
    private String appId, partnerId, prepayId, nonceStr, timeStamp, sign; //= "26f33f45de7f2a851319b647a28a5cdb";

    public PayUtil ()
    {
        if ( client == null )
        {
            client = new AsyncHttpClient();
        }
        if ( api == null )
        {
//            api = WXAPIFactory.createWXAPI(LSBaseActivity.activity, C.WEIXIN_APP_ID);
            api = WXAPIFactory.createWXAPI(LSBaseActivity.activity, "wx7076726e66e9ad2c");
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

//        api.registerApp("wxd930ea5d5a258f4f");
//        api.registerApp(C.WEIXIN_APP_ID);


        MyRequestManager.getInstance().requestGetNoModel("http://pay.lis99.com/main/wechatpay", null, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                String result = mTask.getresult();

                try {
                    JSONObject json = new JSONObject(result);

                    appId = json.optString("appid");

                    partnerId = json.optString("partnerid");

                    prepayId = json.optString("prepayid");

                    nonceStr = json.optString("noncestr");

                    timeStamp = json.optString("timestamp");

                    sign = json.optString("sign");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Common.toast("订单信息获取错误");
                    return;
                }


                PayReq req = new PayReq();

                req.appId = appId;

                req.partnerId = partnerId;

                req.prepayId = prepayId;

                req.nonceStr = nonceStr;

                req.timeStamp = timeStamp;

                req.packageValue = "Sign=WXPay";

                req.sign = sign;

                req.extData	= "app data";


                api.sendReq(req);



            }
        });

    }

    public void payZhiFuBao ( String orderId )
    {

    }
/*
*       支付宝用到
* */
    class payTask extends AsyncTask
    {
//
        private int state;

        public payTask() {
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground( Object[] objects ) {
            return null;
        }
    }


}
