package com.lis99.mobile.mine;

import android.content.Context;
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
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.AccessTokenKeeper;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.LsImproveInfoActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.UIUtils;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.HashMap;


public class LSLoginActivity extends LSBaseActivity {


    private static final int THIRDLOGIN_SUCCESS = 201;
    private static final int THIRDLOGIN_SUCCESS1 = 202;
    private static final int WEIXIN_SUCCESS = 203;

    private static AsyncHttpClient client = new AsyncHttpClient();


    /**
     * WeiboSDKDemo 程序的 APP_SECRET。 请注意：请务必妥善保管好自己的
     * APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = C.SINA_APP_SERCET;

    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;

    /**
     * 获取到的 Code
     */
    private String mCode;
    /**
     * 获取到的 Token
     */
    private Oauth2AccessToken mAccessToken;
    String unlogin;
    String account1;
    String password1;
    String token1;
    String tokenaccount1;
    String tokenpassword1;

    String access_token;

    String api_type, api_uid, api_token, api_nickname;
    String screen_name;

    boolean weixin_auth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
                this.getApplicationContext());
        mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
                C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
        String expire = SharedPreferencesHelper.getValue(this,
                C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
        if (expire == null || "".equals(expire)) {
            expire = "0";
        }
        mTencent.setAccessToken(SharedPreferencesHelper
                .getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
                        C.TENCENT_ACCESS_TOKEN), expire);


        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }

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

                // 取本地token
                api_type = "sina";
                mAccessToken = AccessTokenKeeper.readAccessToken(this);
                mCode = SharedPreferencesHelper.getValue(LSLoginActivity.this,
                        C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.SINA_CODE);
                if (mAccessToken.isSessionValid()) {
                    postMessage(POPUP_PROGRESS, getString(R.string.sending));
                    doGetSinaWeiboNicknameTask();
                } else {
                    // 微博登陆
                    LsWeiboSina
                            .getInstance(this)
                            .getWeiboAuth()
                            .authorize(new AuthListener(),
                                    WeiboAuth.OBTAIN_AUTH_CODE);
                }


            }
            break;
            case R.id.ls_ll_qq_login: {

                api_type = "qq";

                postMessage(POPUP_PROGRESS);

                if (mTencent.isSessionValid()) {
                    UserInfo info = new UserInfo(LSLoginActivity.this,
                            mTencent.getQQToken());
                    info.getUserInfo(new IUiListener() {

                        @Override
                        public void onError(UiError arg0) {
                            postMessage(DISMISS_PROGRESS);
                        }

                        @Override
                        public void onComplete(Object res) {
                            JSONObject json = (JSONObject) res;
                            api_nickname = json.optString("nickname", "");
                            doThirdLoginTask(api_type);
                        }

                        @Override
                        public void onCancel() {
                            postMessage(DISMISS_PROGRESS);
                        }
                    });
                } else {
                    mTencent.login(this, "all", new IUiListener() {

                        @Override
                        public void onError(UiError arg0) {
                            Toast.makeText(LSLoginActivity.this, arg0.errorMessage,
                                    Toast.LENGTH_SHORT).show();
                            postMessage(DISMISS_PROGRESS);
                        }

                        @Override
                        public void onComplete(Object res) {
                            JSONObject json = (JSONObject) res;
                            postMessage(POPUP_PROGRESS, getString(R.string.sending));
                            System.out.println(json);
                            api_uid = json.optString("openid");
                            api_token = json.optString("access_token");
                            final String expires_in = json.optString("expires_in");

                            SharedPreferencesHelper.saveapi_uid(api_uid);
                            SharedPreferencesHelper.saveLSToken(api_token);
                            SharedPreferencesHelper.saveexpires_in(expires_in);
                            SharedPreferencesHelper.saveapi_token(api_token);

                            UserInfo info = new UserInfo(LSLoginActivity.this,
                                    mTencent.getQQToken());
                            info.getUserInfo(new IUiListener() {

                                @Override
                                public void onError(UiError arg0) {
                                    postMessage(DISMISS_PROGRESS);
                                }

                                @Override
                                public void onComplete(Object res) {
                                    JSONObject json = (JSONObject) res;
                                    api_nickname = json.optString("nickname", "");
                                    doThirdLoginTask(api_type);
                                }

                                @Override
                                public void onCancel() {
                                    postMessage(DISMISS_PROGRESS);
                                }
                            });
                        }

                        @Override
                        public void onCancel() {
                            postMessage(DISMISS_PROGRESS);
                            Toast.makeText(LSLoginActivity.this, "登录取消", 0).show();
                        }
                    });
                }

            }
            break;

            default:
                break;
        }
        super.onClick(v);
    }


    /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(LSLoginActivity.this, "授权失败", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(LSLoginActivity.this, "授权失败", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            mCode = code;
            SharedPreferencesHelper.saveSinaCode(code);
            // SharedPreferencesHelper
            // .putValue(LSLoginActivity.this, C.CONFIG_FILENAME,
            // Context.MODE_PRIVATE, C.SINA_CODE, mCode);
            fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
        }

        @Override
        public void onCancel() {
            Toast.makeText(LSLoginActivity.this, "登录取消", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(LSLoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    /**
     * 异步获取 Token。
     *
     * @param authCode  授权 Code，该 Code 是一次性的，只能被获取一次 Token
     * @param appSecret 应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
     *                  不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    public void fetchTokenAsync(String authCode, String appSecret) {
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID, C.SINA_APP_KEY);
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
        requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE,
                "authorization_code");
        requestParams.add(WBConstants.AUTH_PARAMS_CODE, authCode);
        requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL,
                C.SINA_REDIRECT_URL);

        /**
         * 请注意： {@link RequestListener} 对应的回调是运行在后台线程中的， 因此，需要使用 Handler 来配合更新
         * UI。
         */
        AsyncWeiboRunner.requestAsync(OAUTH2_ACCESS_TOKEN_URL, requestParams,
                "POST", new RequestListener() {
                    @Override
                    public void onComplete(String response) {

                        // 获取 Token 成功
                        Oauth2AccessToken token = Oauth2AccessToken
                                .parseAccessToken(response);
                        // 保存 Token 到 SharedPreferences

                        if (token != null && token.isSessionValid()) {
                            postMessage(POPUP_PROGRESS,
                                    getString(R.string.sending));
                            AccessTokenKeeper.writeAccessToken(
                                    LSLoginActivity.this, token);
                            mAccessToken = token;
                            doGetSinaWeiboNicknameTask();
                        } else {

                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        // TODO Auto-generated method stub
                        mHandler1.obtainMessage(MSG_FETCH_TOKEN_FAILED)
                                .sendToTarget();
                    }

                });
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

    private void doThirdLoginTask(String type) {
        postMessage(POPUP_PROGRESS);
        Task task = new Task(null, C.USER_THIRDSIGN_URL, null,
                "USER_THIRDSIGN_URL", this);
        task.setPostData(getThirdLoginParams(type).getBytes());
        publishTask(task, IEvent.IO);
    }

    private String getThirdLoginParams(String type) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("api_type", type);
            params.put("api_uid", api_uid);
            params.put("access_token", api_token);
            params.put("third_nick", screen_name);
            params.put("oauth_token_secret", C.SINA_APP_SERCET);
            params.put("oauth_token", mCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return RequestParamUtil.getInstance(this).getRequestParams(params);
    }

    private void doGetSinaWeiboNicknameTask() {
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add("source", C.SINA_APP_KEY);
        requestParams.add("access_token", mAccessToken.getToken());
        api_token = mAccessToken.getToken();
        requestParams.add("uid", mAccessToken.getUid());
        api_uid = mAccessToken.getUid();
        AsyncWeiboRunner.requestAsync(
                "https://api.weibo.com/2/users/show.json", requestParams,
                "GET", new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        System.out.println(response);
                        try {
                            JSONObject js = new JSONObject(response);
                            screen_name = js.optString("screen_name");
                            api_nickname = screen_name;
                            doThirdLoginTask("sina");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        // TODO Auto-generated method stub
                        postMessage(DISMISS_PROGRESS);
                    }

                });
    }

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
                Toast.makeText(this, "登录成功", 0).show();
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
            case WEIXIN_SUCCESS:
            {
                Intent intent = new Intent(this, LSWeixinLoginActivity.class);
                intent.putExtra("nickName", weixinNickName);
                intent.putExtra("openID", openid);
//                intent.putExtra("openID", "obEKXuBn0N8uZDnfcmbQ1u47LCzI");
                intent.putExtra("headerUrl", weixinHeader);
                intent.putExtra("sex", weixinSex);
                startActivityForResult(intent, 1001);

            }

                break;
        }
        return true;
    }

    private void saveThirdUserMeg(UserBean user) {
        SharedPreferencesHelper.saveaccounttype("third");
        SharedPreferencesHelper.saveUserName(user.getEmail());
        SharedPreferencesHelper.savenickname(user.getNickname());
        SharedPreferencesHelper.saveuser_id(user.getUser_id());
        SharedPreferencesHelper.saveheadicon(user.getHeadicon());
        SharedPreferencesHelper.saveSn(user.getSn());

    }

}
