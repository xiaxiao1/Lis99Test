package com.lis99.mobile.util;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.entry.AccessTokenKeeper;
import com.lis99.mobile.weibo.LsWeiboSina;
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
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.SystemUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yy on 15/7/20.
 */
public class ThirdLogin {

    private static ThirdLogin instance;

    private Tencent mTencent;

    private UserInfo mInfo;

    private String sex, nickname, headicon, openid, token, expires;

    private CallBack callBack;

    private boolean showDialog;

//    ＝＝＝＝＝＝＝＝新浪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

    /**
     * 获取到的 Code
     */
    private String mCode;
    /**
     * 获取到的 Token
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

    private String api_token, api_uid, api_nickname, api_sex, api_headicon;


    public static ThirdLogin getInstance() {
        if (instance == null) instance = new ThirdLogin();
        return instance;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void QQLogin(boolean showDialog) {
        this.showDialog = showDialog;

        if (!showDialog) {
            requestLisLogin();
            return;
        }


        mTencent = Tencent.createInstance(C.TENCENT_APP_ID, LSBaseActivity.activity);

//        //SSO登录检查
        if (!QQInstalled(LSBaseActivity.activity)) {
            return;
        }


        if (mTencent != null && mTencent.isSessionValid()) {
            mTencent.logout(LSBaseActivity.activity);
        }
        mTencent.login(LSBaseActivity.activity, "all", loginListener);


    }

    private void initmTencent() {
        if (!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)) {
            mTencent.setOpenId(openid);
            mTencent.setAccessToken(token, expires);
        }
    }

    public IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (null == o) {

                return;
            }
            JSONObject jsonResponse = (JSONObject) o;
            if (null != jsonResponse && jsonResponse.length() == 0) {

                return;
            }
            try {
                if (jsonResponse.has(Constants.PARAM_OPEN_ID)) {
                    openid = jsonResponse.getString(Constants.PARAM_OPEN_ID);
                }
                if (jsonResponse.has(Constants.PARAM_ACCESS_TOKEN)) {
                    token = jsonResponse.getString(Constants.PARAM_ACCESS_TOKEN);
                }
                if (jsonResponse.has(Constants.PARAM_EXPIRES_IN)) {
                    expires = jsonResponse.getString(Constants.PARAM_EXPIRES_IN);
                }

            } catch (Exception e) {

            }

            initmTencent();

            if ( Common.isTest())
                Common.toast("openid = "+openid);

            if (!TextUtils.isEmpty(openid)) {

                SharedPreferencesHelper.saveQQOpenId(openid);
            }
            updateUserInfo();
        }

        @Override
        public void onError(UiError uiError) {
            Common.toast("" + uiError.errorDetail);
        }

        @Override
        public void onCancel() {

        }
    };

    private void updateUserInfo() {
        if (!mTencent.isSessionValid()) {
            return;
        }
        IUiListener listener = new IUiListener() {

            @Override
            public void onError(UiError e) {

            }

            @Override
            public void onComplete(final Object response) {
                JSONObject json = (JSONObject) response;
//                    Common.log("Userinfo Json="+json.toString());
                try {
                    if (json.has("nickname")) {
                        nickname = json.getString("nickname");
                    }
                    if (json.has("gender")) {
                        sex = json.getString("gender");
                    }
                    if (json.has("figureurl_qq_2")) {
                        headicon = json.getString("figureurl_qq_2");
                    }

                } catch (Exception e) {

                }

                if (!TextUtils.isEmpty(nickname)) {
                    SharedPreferencesHelper.saveQQNickName(nickname);
                }
                if (!TextUtils.isEmpty(sex)) {
                    SharedPreferencesHelper.saveQQSex(sex);
                }
                if (!TextUtils.isEmpty(headicon)) {
                    SharedPreferencesHelper.saveQQHeadIcon(headicon);
                }


//                    Common.log("openid="+openid+"\n nickname="+nickname+"\n sex="+sex+"\n headicon="+headicon);
                LSRequestManager.getInstance().QQLogin(openid, nickname, sex, headicon, callBack, showDialog);

            }

            @Override
            public void onCancel() {

            }
        };
        mInfo = new UserInfo(LSBaseActivity.activity, mTencent.getQQToken());
        mInfo.getUserInfo(listener);
    }

    //        首页自动登陆用到
    private void requestLisLogin() {
        sex = SharedPreferencesHelper.getQQSex();
        openid = SharedPreferencesHelper.getQQOpenId();
        nickname = SharedPreferencesHelper.getQQNickName();
        headicon = SharedPreferencesHelper.getQQHeadIcon();
        if (!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(nickname) && !TextUtils.isEmpty(headicon)) {
            LSRequestManager.getInstance().QQLogin(openid, nickname, sex, headicon, callBack, showDialog);
        } else {
            Common.toast("QQLoginParamsError");
        }
    }
//检查是否安装QQ客户端
    public boolean QQInstalled (Activity var1)
    {
        String var2 = SystemUtils.getAppVersionName(var1, "com.tencent.mobileqq");
        if(var2 == null) {
            Toast.makeText(var1, "没有安装QQ", 0).show();
            return false;
        } else if(SystemUtils.checkMobileQQ(var1)) {
            return true;
        }
        else
        {
            Toast.makeText(var1, "已安装的QQ版本不支持登陆", 0).show();
            return false;
        }
    }

//    ＝＝＝＝＝＝＝＝＝＝＝＝新浪＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝


    public void SinaLogin ( boolean showDialog )
    {
        this.showDialog = showDialog;

        if ( !showDialog )
        {
            requestSinaLogin();
            return;
        }

        LsWeiboSina
                .getInstance(LSBaseActivity.activity)
                .getWeiboAuth()
                .authorize(new AuthListener(),
                        WeiboAuth.OBTAIN_AUTH_CODE);

//        mAccessToken = AccessTokenKeeper.readAccessToken(LSBaseActivity.activity);
//        mCode = SharedPreferencesHelper.getSinaCode();
//        mAccessToken = new Oauth2AccessToken();
//        mCode = "";
//        if (mAccessToken != null && mAccessToken.isSessionValid()) {
////            postMessage(POPUP_PROGRESS, getString(R.string.sending));
////            doGetSinaWeiboNicknameTask();
//        } else {
//            // 微博登陆
//            LsWeiboSina
//                    .getInstance(LSBaseActivity.activity)
//                    .getWeiboAuth()
//                    .authorize(new AuthListener(),
//                            WeiboAuth.OBTAIN_AUTH_CODE);
//        }
    }


    private void doGetSinaWeiboNicknameTask() {
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add("source", C.SINA_APP_KEY);
        api_token = mAccessToken.getToken();
        requestParams.add("access_token", api_token);
        api_uid = mAccessToken.getUid();
        requestParams.add("uid", api_uid);

        if ( Common.isTest())
        Common.toast("api_uid="+api_uid);

        SharedPreferencesHelper.saveSinaUid(api_uid);

        AsyncWeiboRunner.requestAsync(
                "https://api.weibo.com/2/users/show.json", requestParams,
                "GET", new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        System.out.println(response);
                        try {
                            JSONObject js = new JSONObject(response);
                            api_nickname = js.optString("screen_name");
                            api_headicon = js.optString("avatar_large");
                            api_sex = js.optString("gender");
//                            doThirdLoginTask("sina");

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        SharedPreferencesHelper.saveSinaNickName(api_nickname);
                        SharedPreferencesHelper.saveSinaSex(api_sex);
                        SharedPreferencesHelper.saveSinaHeadIcon(api_headicon);

//                        requestSinaLogin();
                        LSRequestManager.getInstance().SinaLogin(api_uid, api_nickname, api_sex, api_headicon, callBack, showDialog );

                    }

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        // TODO Auto-generated method stub
//                        postMessage(DISMISS_PROGRESS);
                        Common.toast(arg0.getMessage());
                    }

                });

        /**
         * 微博认证授权回调类。
         */
        class AuthListener implements WeiboAuthListener {

            @Override
            public void onComplete(Bundle values) {
                if (null == values) {
                    Toast.makeText(LSBaseActivity.activity, "授权失败", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                String code = values.getString("code");
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(LSBaseActivity.activity, "授权失败", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                mCode = code;
                SharedPreferencesHelper.saveSinaCode(code);
                // SharedPreferencesHelper
                // .putValue(LSLoginActivity.this, C.CONFIG_FILENAME,
                // Context.MODE_PRIVATE, C.SINA_CODE, mCode);
                fetchTokenAsync(mCode, C.SINA_APP_SERCET);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LSBaseActivity.activity, "登录取消", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                UIUtils.showToast(LSBaseActivity.activity,
                        "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
            }
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
//                            postMessage(POPUP_PROGRESS,
//                                    getString(R.string.sending));
                            AccessTokenKeeper.writeAccessToken(
                                    LSBaseActivity.activity, token);
                            mAccessToken = token;
                            doGetSinaWeiboNicknameTask();
                        } else {

                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        // TODO Auto-generated method stub
//                        mHandler1.obtainMessage(MSG_FETCH_TOKEN_FAILED)
//                                .sendToTarget();
                    }

                });
    }

    /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(LSBaseActivity.activity, "授权失败", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(LSBaseActivity.activity, "授权失败", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            mCode = code;
            SharedPreferencesHelper.saveSinaCode(code);
            fetchTokenAsync(mCode, C.SINA_APP_SERCET);
        }

        @Override
        public void onCancel() {
            Toast.makeText(LSBaseActivity.activity, "登录取消", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(LSBaseActivity.activity,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }


    private void requestSinaLogin ()
    {
        api_sex = SharedPreferencesHelper.getSinaSex();
        api_headicon = SharedPreferencesHelper.getSinaHeadIcon();
        api_nickname = SharedPreferencesHelper.getSinaNickName();
        api_uid = SharedPreferencesHelper.getSinaUid();
        if ( !TextUtils.isEmpty(api_sex) && !TextUtils.isEmpty(api_nickname) && !TextUtils.isEmpty(api_uid) && !TextUtils.isEmpty(api_headicon) )
        {
            LSRequestManager.getInstance().SinaLogin(api_uid, api_nickname, api_sex, api_headicon, callBack, showDialog);
        }
        else
        {
            Common.log("SinaLoginParamsError");
        }
    }













}
