package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsImproveInfoActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.ThirdLogin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;

import java.util.HashMap;


public class LSLoginActivity extends LSBaseActivity {


    private static final int THIRDLOGIN_SUCCESS = 201;
    private static final int THIRDLOGIN_SUCCESS1 = 202;
    private static final int WEIXIN_SUCCESS = 203;
    private static final int LOGIN_SUCCESS = 204;
    private static final int WEIXIN_LOGIN_FAIL = 205;

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;


    String api_type, api_uid, api_token, api_nickname;
    String screen_name;

    boolean weixin_auth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ls_login);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (weixin_auth) {
            weixin_auth = false;
            String code = SharedPreferencesHelper.getWeixinCode();
            if (code != null && !"".equals(code))
            loadToken(code);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        View v = findViewById(R.id.ls_regist_bt);
        v.setOnClickListener(this);

        v = findViewById(R.id.iv_back);
        v.setOnClickListener(this);

        v = findViewById(R.id.ls_login_bt);
        v.setOnClickListener(this);

        v = findViewById(R.id.ls_wx_login_bt);
        v.setOnClickListener(this);

        v = findViewById(R.id.ls_ll_sina_login);
        v.setOnClickListener(this);

        v = findViewById(R.id.ls_ll_qq_login);
        v.setOnClickListener(this);

        v = findViewById(R.id.ls_email_login);
        v.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ls_regist_bt: {
                Intent intent = new Intent(this, LSPhoneRegisterActivity.class);
                startActivityForResult(intent, 1001);
            }
            return;
            case R.id.iv_back: {
                finish();
            }
            return;
            case R.id.ls_login_bt: {
                Intent intent = new Intent(this, LSPhoneLoginActivity.class);
                startActivityForResult(intent, 1001);
            }
            return;
            case R.id.ls_email_login: {
                Intent intent = new Intent(this, LSEmailLoginActivity.class);
                startActivityForResult(intent, 1001);
            }
            return;
            case R.id.ls_wx_login_bt: {
//                loadWeixinUserInfo("","");
                loginWithWeixin();
//                loadToken("001ca81fde151143e45f4b344520624x");
            }
            return;
            case R.id.ls_ll_sina_login: {
                SinaLogin();
            }
            break;
            case R.id.ls_ll_qq_login: {
                QQLogin();
            }
            break;

            default:
                break;
        }
        super.onClick(v);
    }

    private Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FETCH_TOKEN_SUCCESS:
                    // 显示 Token
                    Intent intent = new Intent(LSLoginActivity.this,
                            LsImproveInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("login", "loginin");
                    intent.putExtra("unlogin", "unlogin");
                    startActivity(intent);
                    break;

                case MSG_FETCH_TOKEN_FAILED:
                    Toast.makeText(LSLoginActivity.this, "授权失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2000:
                    finish();
                    break;

                default:
                    break;
            }
        }

        ;
    };

//    private void doThirdLoginTask(String type) {
//        postMessage(POPUP_PROGRESS);
//        Task task = new Task(null, C.USER_THIRDSIGN_URL, null,
//                "USER_THIRDSIGN_URL", this);
//        task.setPostData(getThirdLoginParams(type).getBytes());
//        publishTask(task, IEvent.IO);
//    }
//
//    private String getThirdLoginParams(String type) {
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        try {
//            params.put("api_type", type);
//            params.put("api_uid", api_uid);
//            params.put("access_token", api_token);
//            params.put("third_nick", screen_name);
//            params.put("oauth_token_secret", C.SINA_APP_SERCET);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return RequestParamUtil.getInstance(this).getRequestParams(params);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("login", false)) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private IWXAPI mWeixinAPI;

    private void loginWithWeixin() {
        if (mWeixinAPI == null) {
            mWeixinAPI = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, false);
        }

        if (!mWeixinAPI.isWXAppInstalled()) {
            Toast.makeText(this, "未安装微信", Toast.LENGTH_LONG).show();
            return;
        }

        mWeixinAPI.registerApp(C.WEIXIN_APP_ID);

        weixin_auth = true;

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "lis99";
        mWeixinAPI.sendReq(req);
    }


    private void loadToken(String code) {
        String url  = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + C.WEIXIN_APP_ID + "&secret="  + C.WEIXIN_APP_KEY + "&code=" + code + "&grant_type=authorization_code";
       // Task task = new Task(null, url, C.HTTP_GET, "weixin_auth",
       //         this);
       // publishTask(task, IEvent.IO);

        client.get(url, new TextHttpResponseHandler(){
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                parserWeixinAuthInfo(s);
            }
        });

    }

    private void loadWeixinUserInfo(String token, String openid) {
//        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=OezXcEiiBSKSxW0eoylIeMpHgFML8iCaZxdHA83ayyWrRmeiCIBNgTIyTf6blNAP5UiDIQCwlfs5mDQI6gRatdSPOrVzCYoJ-GpOdMpeaUrSrhbpQKUNFcN1Zrpdl1okAl3SzUff8ixD6F03oLlqrQ&openid=obEKXuBn0N8uZDnfcmbQ1u47LCzI";
        String url  = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openid;
//        Task task = new Task(null, url, C.HTTP_GET, "weixin_userinfo",
//                this);
//        publishTask(task, IEvent.IO);

        client.get(url, new TextHttpResponseHandler(){
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                parserWeixinUserInfo(s);
            }
        });

    }


    String weixin_access_token;
    String openid;
    int expires_in;
    String refresh_token;
    String scope;
    String unionid;

    private void parserWeixinAuthInfo(String result) {

        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            if (root.has("access_token")) {
                weixin_access_token = root.get("access_token").asText();
                openid = root.get("openid").asText();
                unionid = root.get("unionid").asText();
                expires_in = root.get("expires_in").asInt();

                SharedPreferencesHelper.saveWeixinOpenID(openid);
                SharedPreferencesHelper.saveWeixinToken(weixin_access_token);

                loadWeixinUserInfo(weixin_access_token, openid);


            } else {
                String msg = root.get("errmsg").asText();
                postMessage(POPUP_TOAST, msg);
            }



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(LSBaseActivity.DISMISS_PROGRESS);
        }

    }

    String weixinNickName;
    int weixinSex;
    String weixinHeader;

    private void parserWeixinUserInfo(String result) {

        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            if (root.has("openid")) {
                weixinNickName = root.get("nickname").asText();
                weixinHeader = root.get("headimgurl").asText();
                weixinSex = root.get("sex").asInt();

                postMessage(WEIXIN_SUCCESS);


            } else {
                String msg = root.get("errmsg").asText();
                postMessage(POPUP_TOAST, msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(LSBaseActivity.DISMISS_PROGRESS);
        }

    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    String p = (String) task.getParameter();
                    if ("USER_THIRDSIGN_URL".equals(p)) {
                        parserThirdLogin(result);
                    } else if (p.equals("weixin_auth")) {
                        parserWeixinAuthInfo(result);
                    } else if (p.equals(C.WEIXIN_LOGIN)) {
                        parseWeixinLoginInfo(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }

    private void parserThirdLogin(String params) {
        String result = DataManager.getInstance().validateResult(params);
        if (result != null) {
            if ("OK".equals(result)) {
                DataManager.getInstance().jsonParse(params,
                        DataManager.TYPE_THIRDLOGIN);
                postMessage(THIRDLOGIN_SUCCESS);
            } else {
                postMessage(THIRDLOGIN_SUCCESS1);
            }
        }
        postMessage(DISMISS_PROGRESS);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (super.handleMessage(msg))
            return true;
        //登陆成功， 上传设备信息

        switch (msg.what) {

            case THIRDLOGIN_SUCCESS:
                LSRequestManager.getInstance().upDataInfo();
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                saveThirdUserMeg(DataManager.getInstance().getUser());

                finish();
                break;
            case THIRDLOGIN_SUCCESS1: {
                LSRequestManager.getInstance().upDataInfo();
                Intent intent = new Intent(LSLoginActivity.this,
                        LsImproveInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("api_type", api_type);
                intent.putExtra("api_uid", api_uid);
                intent.putExtra("access_token", api_token);
                intent.putExtra("third_nick", api_nickname);
                intent.putExtra("bind", true);
                intent.putExtra("login", "loginin");
                intent.putExtra("unlogin", "unlogin");
                startActivity(intent);
            }
                break;
            case WEIXIN_LOGIN_FAIL:
            {
                Intent intent = new Intent(this, LSWeixinLoginActivity.class);
                intent.putExtra("nickName", weixinNickName);
                intent.putExtra("openID", openid);
                intent.putExtra("openID", "obEKXuBn0N8uZDnfcmbQ1u47LCzI");
                intent.putExtra("headerUrl", weixinHeader);
                intent.putExtra("sex", weixinSex);
                startActivityForResult(intent, 1001);

            }

                break;
            case WEIXIN_SUCCESS:
            {
                postMessage(POPUP_PROGRESS, getString(R.string.sending));
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("openid", openid);
                params.put("nickname", weixinNickName);
                params.put("sex", weixinSex);
                params.put("headimgurl", weixinHeader);
                params.put("unionid", TextUtils.isEmpty(unionid) ? "0" : unionid);
                Task task = new Task(null, C.WEIXIN_LOGIN, C.HTTP_POST, C.WEIXIN_LOGIN,
                        this);
                task.setPostData(RequestParamUtil.getInstance(this)
                        .getRequestParams(params).getBytes());
                publishTask(task, IEvent.IO);
            }
            break;
            case LOGIN_SUCCESS:
            {
                finish();

            }

            break;
        }
        return true;
    }


    private void parseWeixinLoginInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            JsonNode data = root.get("data");
            if (!"OK".equals(errCode)) {
                String error = data.get("error").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, error);
                return;
            }

            UserBean u = new UserBean();

            String nickName = data.get("nickname").asText();

            String headicon = data.get("headicon").asText();

            u.setUser_id(data.get("user_id").asText());

            u.setHeadicon(headicon);

            u.setNickname(nickName);

            DataManager.getInstance().setUser(u);
            DataManager.getInstance().setLogin_flag(true);

            SharedPreferencesHelper.saveWeixinOpenID(openid);
            SharedPreferencesHelper.saveWeixinHeader(weixinHeader);
            SharedPreferencesHelper.saveWeixinNickName(weixinNickName);
            SharedPreferencesHelper.saveWeixinSex(weixinSex + "");
            SharedPreferencesHelper.saveWeiXinUnionid(unionid);

            SharedPreferencesHelper.saveaccounttype(SharedPreferencesHelper.WEIXINLOGIN);

            postMessage(LOGIN_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }

    }

    private void saveThirdUserMeg(UserBean user) {
        SharedPreferencesHelper.saveaccounttype("third");
        SharedPreferencesHelper.saveUserName(user.getEmail());
        SharedPreferencesHelper.savenickname(user.getNickname());
        SharedPreferencesHelper.saveuser_id(user.getUser_id());
        SharedPreferencesHelper.saveheadicon(user.getHeadicon());

    }

    private void QQLogin ()
    {
        CallBack callBack = new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                finish();
            }
        };

        ThirdLogin thirdLogin = ThirdLogin.getInstance();
        thirdLogin.setCallBack(callBack);
        thirdLogin.QQLogin(true);

    }

    private void SinaLogin ()
    {
        CallBack call = new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                finish();
            }
        };
        ThirdLogin thirdLogin = ThirdLogin.getInstance();
        thirdLogin.setCallBack(call);
        thirdLogin.SinaLogin(true);
    }



}
