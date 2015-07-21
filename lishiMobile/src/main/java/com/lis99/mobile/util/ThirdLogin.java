package com.lis99.mobile.util;

import android.text.TextUtils;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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

        //SSO登录检查
        if (!mTencent.isSupportSSOLogin(LSBaseActivity.activity)) {
//            Common.toast("请先安装QQ");
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


    private IUiListener loginListener = new IUiListener() {
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
            Common.toast("QQLoginParamError");
        }
    }

}
