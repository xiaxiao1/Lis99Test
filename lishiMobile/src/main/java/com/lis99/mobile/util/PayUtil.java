package com.lis99.mobile.util;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ZFBPayModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.wxapi.WXPayEntryActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

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

    private final String ZFBURL = "http://api.lis99.com/v4/club/ordersinfo";

    private final String WEIXINURL = "http://api.lis99.com/v4/club/wxpay";

    private ZFBPayModel zfbModel;
//  订单号
    public static String orderCode;

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


    public void payWeiXin ( String orderId )
    {
        orderCode = orderId;
        if ( !api.isWXAppInstalled())
        {
            Toast.makeText(LSBaseActivity.activity, "未安装微信", Toast.LENGTH_LONG).show();
            return;
        }

        api.registerApp(C.WEIXIN_APP_ID);

        HashMap<String, Object> map = new HashMap<String, Object>();

        String userId = DataManager.getInstance().getUser().getUser_id();

        if ( TextUtils.isEmpty(userId))
        {
            Common.toast("需要登录后才能支付");
            return;
        }

        map.put("orderCode", orderId);
        map.put("user_id", userId);


        MyRequestManager.getInstance().requestPostNoModel(WEIXINURL, map, null, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                String result = mTask.getresult();

                try {
                    JSONObject json = new JSONObject(result);

                    String ok = json.optString("status");

                    JSONObject jo = json.getJSONObject("data");

                    if (TextUtils.isEmpty(ok) || !"OK".equals(ok)) {
                        String err = jo.optString("error");
                        if ( !TextUtils.isEmpty(err) )
                        {
                            Common.toast(err);
                            return;
                        }
                        Common.toast("获取订单信息失败");
                        return;
                    }



                    appId = jo.optString("appid");

                    partnerId = jo.optString("partner_id");

                    prepayId = jo.optString("prepay_id");

                    nonceStr = jo.optString("nonce_str");

                    timeStamp = jo.optString("timestamp");

                    sign = jo.optString("sign");

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

                req.extData = "app data";


                api.sendReq(req);

            }
        });

    }

//    // 商户PID
//    public static final String PARTNER = "2088011634294350";
//    // 商户收款账号
//    public static final String SELLER = "baoming@lis99.com";
//    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMWyfKuaiK2p+YoLg90D2IYFhFP+EFJe4K3BwGs38Uqwf6h5Ip5D7STud/9mT+83fvzxMMwoawu+Q2NxQ9/Bqs9EyIprcGVXW9qSnzbBJAYTTo0ThG52+QTEt0PLkOai8otTEMWrtZpM4EgRyYUpa0ON7S/aJu8+p6UO17z0cE5zAgMBAAECgYBwBT4NQog11z11kibKwlYbQt8DdM+szOQEsOemGVHZH3+GZ/VMtnKWXaWTC1c51jlXfBdJZ5GYWtv2agSqsiNlRW+fKXM64H9wsB/+syQplmtwOCKUIYTS5BsMF8RCiWWv+mAEvJbqdQF35mVA6+pLXRx56nginyoaHUGiRXSCUQJBAOyVIWLeYLsNkhCp/3dhaLSjkSdmP3HW7MleE4U63p3az1reLMLHj7C006oPM+RftbtxBl+mUGuyyMC/zEq9DksCQQDV7FTZGOKpIEJY/Yt7EoBl7wq0nFZ3K5AZa196RzUn3sZ+8XbX9bT9cPjbJilOLYnQawDVUEtjAO38Lvt4wYd5AkEAqJUsETO9Yg0thEpfDEaRQgc8LAMkOo6YdHVhG5LzhzCgiXPAGZvyvExed9QVeirpaQQFMqtkqxnfC9qgTLGjOQJALK7dli8tgPgdA6uKC930ddY1XT5ejSvLQJP98HOZNcfBnFhhY4COGnYTdOsGq661X5RKK0RHStmx3AAQRMvfuQJBAIofEfKldjEzvmyU2q/71igalqMOjGnroTVszuhbSHiEIxIdfIfOmfrI/Y4OX+iOXzpdDVn5TFZyxJXS42diQ8M=";

    public void payZhiFuBao ( String orderId )
    {
        orderCode = orderId;

        HashMap<String, Object> map = new HashMap<String, Object>();

        String userId = DataManager.getInstance().getUser().getUser_id();

        if ( TextUtils.isEmpty(userId))
        {
            Common.toast("需要登录后才能支付");
            return;
        }

        zfbModel = new ZFBPayModel();

        map.put("orderCode", orderId);
        map.put("user_id", userId);

        MyRequestManager.getInstance().requestPostNoModel(ZFBURL, map, zfbModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                zfbModel = (ZFBPayModel) mTask.getResultModel();

                if (zfbModel == null || TextUtils.isEmpty(zfbModel.seller_id)) {
//                    Common.toast("获取订单信息失败");
                    return;
                }

                // 订单
                String orderInfo = getOrderInfo(zfbModel.subject, zfbModel.body, zfbModel.total_fee);

                // 对订单做RSA 签名
                String sign = sign(orderInfo);
                try {
                    // 仅需对sign 做URL编码
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // 完整的符合支付宝参数规范的订单信息
                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                        + getSignType();


                new payZFBTask().execute(payInfo);


            }
        });

    }
/*
*       支付宝用到
* */
    class payZFBTask extends AsyncTask
    {
//
        private int state;

        public payZFBTask() {
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if ( o == null )
            {
                return;
            }

            String result = (String) o;

            PayResult payResult = new PayResult(result);

            // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
            String resultInfo = payResult.getResult();

            String resultStatus = payResult.getResultStatus();

            Intent intent = new Intent(LSBaseActivity.activity, WXPayEntryActivity.class);

            if (TextUtils.equals(resultStatus, "9000") )
            {
                intent.putExtra("CODE", 0);
            }
            else if ( TextUtils.equals(resultStatus, "8000") )
            {
                intent.putExtra("CODE", 0);
            }
//            中途取消
            else if ( TextUtils.equals(resultStatus, "6001") )
            {
                intent.putExtra("CODE", -2);
            }
            else
            {
                intent.putExtra("CODE", -1);
            }

            LSBaseActivity.activity.startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground( Object[] objects ) {
            String payInfo = (String) objects[0];
            PayTask alipay = new PayTask(LSBaseActivity.activity);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo);
            return result;
//            return null;
        }
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + zfbModel.partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + zfbModel.seller_id + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + zfbModel.out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + zfbModel.notify_url//"http://api.lis99.com/alipayapi/wapapi"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"" + zfbModel.it_b_pay +"\"";    ///*30m*/
//        orderInfo += "&it_b_pay=\"1m\"";    ///*30m*/

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";
        orderInfo += "&return_url=\""+zfbModel.return_url+"\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, zfbModel.rsa_private_key);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
