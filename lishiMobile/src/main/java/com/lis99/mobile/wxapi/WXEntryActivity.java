package com.lis99.mobile.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.ShareManager;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends LSBaseActivity implements IWXAPIEventHandler {

    private IWXAPI mWeixinAPI;

    boolean authResp = false;

    String access_token;
    String openid;
    int expires_in;
    String refresh_token;
    String scope;
    String unionid;

    public static CallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, false);
        mWeixinAPI.registerApp(C.WEIXIN_APP_ID);
        mWeixinAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        postMessage(LSBaseActivity.POPUP_PROGRESS, R.string.sending);
    }


    public void loadToken(String code) {
        String url  = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + C.WEIXIN_APP_ID + "&secret="  + C.WEIXIN_APP_KEY + "&code=" + code + "&grant_type=authorization_code";
        Task task = new Task(null, url, C.HTTP_GET, "weixin_auth",
                this);
        publishTask(task, IEvent.IO);
    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    String param = (String) task.getParameter();
                    result = new String((byte[]) task.getData());
                    if (param.equals("weixin_auth")) {
                        parserWeixinAuthInfo(result);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void parserWeixinAuthInfo(String result) {

        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            JsonNode data = root.get("data");
            if (!"OK".equals(errCode)) {
                String error = data.get("error").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, error);
                return;
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(LSBaseActivity.DISMISS_PROGRESS);
        }

    }


    @Override
    public void onResp(BaseResp resp) {
        authResp = false;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
                if (resp instanceof SendAuth.Resp) {
                    authResp = true;
                    SendAuth.Resp res = (SendAuth.Resp) resp;
                    String code = res.code;

                    SharedPreferencesHelper.saveWeixinCode(code);
                }
                //分享
                else
                {
                    if ( callBack != null )
                    {
                        MyTask task = new MyTask();
                        task.setresult("WECHAT");
                        callBack.handler(task);
                    }
                    callBack = null;
                    //朋友
                    if (ShareManager.state == ShareManager.wechat)
                    {
                        LSScoreManager.getInstance().sendScore(LSScoreManager.sharewechatfriends, ShareManager.topicid);
                    }
//                    朋友圈
                    else if (ShareManager.state == ShareManager.wechat_friends)
                    {
                        LSScoreManager.getInstance().sendScore(LSScoreManager.sharewechatfriendscircle, ShareManager.topicid);
                    }
                }


            }
            break;

            default:
                break;
        }
//        if (!authResp) {
            postMessage(LSBaseActivity.DISMISS_PROGRESS);
            finish();
//        }


    }

}
