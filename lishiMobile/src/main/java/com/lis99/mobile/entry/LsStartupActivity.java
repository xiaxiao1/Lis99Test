package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.newhome.HelpActivity;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.FullScreenADImage;
import com.lis99.mobile.util.PushManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StartLogoOption;
import com.lis99.mobile.util.ThirdLogin;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class LsStartupActivity extends ActivityPatternStartUp {

    LinearLayout ll_startup;
    String account;
    String password;
    String token;
    String tokenaccount;
    String tokenpassword;
    String phone;


    String weixinNickName;
    String weixinHeader;
    String weixinSex;
    String openid;
    String unionid;


    private ImageView iv_img;
    private ImageView iv_channel;
    private ImageView iv_bg;
    private ImageView iv_name;

    private Animation animation_img, animation_info, animation_scale;

    private ImageView iv_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LSBaseActivity.activity = this;

//      推送
        if (Common.mainIsStart) {
            Intent intent = new Intent(LsStartupActivity.this,
                    NewHomeActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //传送push信息
            intent.putExtra(PushManager.TAG, PushManager.getInstance().getPushModel(LsStartupActivity.this.getIntent()));
            startActivity(intent);
            finish();
            return;
        }
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息

        setContentView(R.layout.ls_startup);

//        StatusUtil.setStatusBar(this);

        Display mDisplay = getWindowManager().getDefaultDisplay();
        Common.WIDTH = mDisplay.getWidth();
        Common.HEIGHT = mDisplay.getHeight();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        Common.WIDTH = metric.widthPixels;  // 屏幕宽度（像素）
        Common.HEIGHT = metric.heightPixels;  // 屏幕高度（像素）
        Common.scale = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        //设备信息
        DeviceInfo.getDeviceInfo(this);

//		ll_startup = (LinearLayout) findViewById(R.id.ll_startup);

        iv_img = (ImageView) findViewById(R.id.iv_img);
        iv_channel = (ImageView) findViewById(R.id.iv_channel);
        iv_name = (ImageView) findViewById(R.id.iv_name);
        iv_name.setVisibility(View.GONE);

        //＝＝＝＝＝＝＝＝＝＝＝根据渠道更换启动Icon ＝＝＝＝＝＝＝＝＝＝＝
        StartLogoOption.showStartLogoOption(iv_channel);

//        animation_img = AnimationUtils.loadAnimation(this, R.anim.star_img_time);
//        animation_info = AnimationUtils.loadAnimation(this, R.anim.star_info_time);
//
//        animation_scale = AnimationUtils.loadAnimation(this, R.anim.ls_lauch_scale_anim_in);


        animation_img = AnimationUtils.loadAnimation(this, R.anim.ls_lauch_forground_in);
//        animation_info = AnimationUtils.loadAnimation(this, R.anim.ls_lauch_new_logo_in);
//        animation_scale = AnimationUtils.loadAnimation(this, R.anim.ls_lauch_scale_anim_in);


        iv_ad = (ImageView) findViewById(R.id.iv_ad);

        iv_bg = (ImageView) findViewById(R.id.iv_bg);

        account = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
                Context.MODE_PRIVATE, C.ACCOUNT);
        phone = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
                Context.MODE_PRIVATE, C.ACCOUNT_PHONE);
        password = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
                Context.MODE_PRIVATE, C.PASSWORD);
        token = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
                Context.MODE_PRIVATE, C.TOKEN);
        tokenaccount = SharedPreferencesHelper.getValue(this,
                C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_ACCOUNT);
        tokenpassword = SharedPreferencesHelper.getValue(this,
                C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_PASSWORD);

//        String accountType = SharedPreferencesHelper.getValue(this,
//                C.CONFIG_FILENAME, Context.MODE_PRIVATE, "accounttype");
        String accountType = SharedPreferencesHelper.getaccounttype();

        if (SharedPreferencesHelper.THIRD.equals(accountType)) {
            UserBean user = new UserBean();
            user.setEmail(SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.ACCOUNT));
            user.setNickname(SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "nickname"));
            user.setUser_id(SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "user_id"));
            user.setHeadicon(SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "headicon"));
            user.setSn(SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "sn"));
            DataManager.getInstance().setLogin_flag(true);
            DataManager.getInstance().setUser(user);
            //手机号登陆
        } else if ("phone".equals(accountType)) {
            if (phone != null && !"".equals(phone)) {
//                postMessage(POPUP_PROGRESS, getString(R.string.sending));
                doPhoneLoginTask(phone, password);
            }
            //微信登录
        } else if (SharedPreferencesHelper.WEIXINLOGIN.equals(accountType)) {
            weixinHeader = SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "weixin_header");
            weixinNickName = SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "weixin_nickName");
            weixinSex = SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, "weixin_sex");
            openid = SharedPreferencesHelper.getValue(this,
                    C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.WEIXIN_OPENID);
            unionid = SharedPreferencesHelper.getWeiXinUnionid();
            doWechatLogin();
        }
        //QQ登录
        else if ( SharedPreferencesHelper.QQLOGIN.equals(accountType))
        {
            QQLogin();
        }
        else if ( SharedPreferencesHelper.SINALOGIN.equals(accountType))
        {
            SinaLogin();
        }
        else
        {
            if (account != null && !"".equals(account))
            {
//                postMessage(POPUP_PROGRESS, getString(R.string.sending));
                doLoginTask(account, password);
            }
            else
            {
                if (token != null && !"".equals(token))
                {
//                    postMessage(POPUP_PROGRESS, getString(R.string.sending));
                    doLoginTask(tokenaccount, tokenpassword);
                }
            }
        }

//        startInfoAnimation();

//        goNext();

//    new Handler().postDelayed(new Runnable() {
//
//        @Override
//        public void run() {
////				Intent intent = new Intent(LsStartupActivity.this,
////						NewHomeActivity.class);
////				//传送push信息
////				intent.putExtra(PushManager.TAG, PushManager.getInstance().getPushModel(LsStartupActivity.this.getIntent()));
////				startActivity(intent);
////				finish();
////            startInfoAnimation();
//            startAD();
//        }
//    }, 600);

//        iv_name.startAnimation(animation_info);
        iv_img.startAnimation(animation_img);
//        iv_bg.startAnimation(animation_scale);

        animation_img.setFillAfter(true);
//        animation_info.setFillAfter(true);
//        animation_scale.setFillAfter(true);

        animation_img.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setFillAfter(true);
                startAD();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


}




    private void startAD ()
    {

        //获取本地图片
        Bitmap b = FullScreenADImage.getInstance().getbAD();
        //获取更新
        FullScreenADImage.getInstance().getUpdata();
        if ( b == null )
        {
//            startInfoAnimation();
            goNext();
        }
        else {
            iv_ad.setVisibility(View.VISIBLE);
            iv_ad.setImageBitmap(b);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    startInfoAnimation();
                    goNext();
                }
            }, 2800);
        }
    }

//    private void startInfoAnimation() {
//
////        iv_bg.startAnimation(animation_scale);
//
//        iv_ad.setVisibility(View.GONE);
//        iv_info.startAnimation(animation_info);
//
//        animation_info.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                iv_info.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                animation.setFillAfter(true);
//
//                startImgAnimation();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

//    private void startImgAnimation() {
//        iv_img.setVisibility(View.VISIBLE);
//        iv_img.startAnimation(animation_img);
//        animation_img.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                animation.setFillAfter(true);
////                goNext();
//                startAD();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

    private void goNext() {

        Intent intent = null;

        boolean visible = false;
//      如果版本号增加， 则显示引导页
        int version = Integer.parseInt(SharedPreferencesHelper.getClientVersion());

        Common.log("version="+version + "\ncurrentVersion="+DeviceInfo.CLIENTVERSIONCODE);

        if ( version == -1 || DeviceInfo.CLIENTVERSIONCODE > version )
        {
            SharedPreferencesHelper.saveClientVersion(""+DeviceInfo.CLIENTVERSIONCODE);
            visible = true;
        }
//        visible = true;
        if (TextUtils.isEmpty(SharedPreferencesHelper.getHelp()) || visible )
        {
//            startActivity( new Intent(this, HelpActivity.class));
            intent = new Intent(LsStartupActivity.this,
                    HelpActivity.class);
//            intent = new Intent(LsStartupActivity.this,
//                    HelpMovieActivity.class);
//            return;
        }
        else {
            intent = new Intent(LsStartupActivity.this,
                    NewHomeActivity.class);
        }

        //传送push信息
        intent.putExtra(PushManager.TAG, PushManager.getInstance().getPushModel(LsStartupActivity.this.getIntent()));
        startActivity(intent);

        overridePendingTransition(R.anim.ls_alpha_in, R.anim.ls_alpha_out);

        finish();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//
//            }
//        }, 1000);


    }

    private void doWechatLogin() {
//        postMessage(POPUP_PROGRESS, getString(R.string.sending));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("openid", openid);
        params.put("nickname", weixinNickName);
        params.put("sex", weixinSex);
        params.put("headimgurl", weixinHeader);
        params.put("unionid", TextUtils.isEmpty(unionid) ? "0" : unionid );
        Task task = new Task(null, C.WEIXIN_LOGIN, C.HTTP_POST, C.WEIXIN_LOGIN,
                this);
        task.setPostData(RequestParamUtil.getInstance(this)
                .getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);
    }

    private void doPhoneLoginTask(String account2, String password2) {
        Task task = new Task(null, C.PHONE_LOGIN, C.HTTP_POST, C.PHONE_LOGIN,
                this);
        task.setPostData(getPhoneLoginParams(account2, password2).getBytes());
        publishTask(task, IEvent.IO);
    }

    private String getPhoneLoginParams(String account2, String password2) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mobile", account2);
        params.put("passwd", password2);
        return RequestParamUtil.getInstance(this).getRequestParams(params);
    }


    private void doLoginTask(String account2, String password2) {
        Task task = new Task(null, C.USER_SIGNIN_URL, null, "USER_SIGNIN_URL",
                this);
        task.setPostData(getLoginParams(account2, password2).getBytes());
        publishTask(task, IEvent.IO);
    }

    private String getLoginParams(String account2, String password2) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("email", account2);
        params.put("pwd", password2);
        return RequestParamUtil.getInstance(this).getRequestParams(params);
    }

    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals("USER_SIGNIN_URL")) {
                        parserLogin(result);
                    } else if (((String) task.getParameter()).equals(C.PHONE_LOGIN)) {
                        parserPhoneLoginInfo(result);
                    } else if (((String) task.getParameter()).equals(C.WEIXIN_LOGIN)) {
                        parseWeixinLoginInfo(result);
                    }
                }
                break;
            default:
                break;
        }
//        postMessage(DISMISS_PROGRESS);
    }

    private void parserPhoneLoginInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            JsonNode data = root.get("data");
            if (!"OK".equals(errCode)) {
                String error = data.get("error").asText();
//                postMessage(ActivityPattern1.POPUP_TOAST, error);
                return;
            }

            UserBean u = new UserBean();
            u.setUser_id(data.get("user_id").asText());
            String nickName = data.get("nickname").asText();
            u.setNickname(nickName);
            DataManager.getInstance().setUser(u);
            DataManager.getInstance().setLogin_flag(true);

//            postMessage(LOGIN_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }

    }


    private void parseWeixinLoginInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            JsonNode data = root.get("data");
            if (!"OK".equals(errCode)) {
                String error = data.get("error").asText();
//                postMessage(ActivityPattern1.POPUP_TOAST, error);
                return;
            }

            UserBean u = new UserBean();

            String nickName = data.get("nickname").asText();


            u.setUser_id(data.get("user_id").asText());

            String headicon = data.get("headicon").asText();

            u.setHeadicon(headicon);

            u.setNickname(nickName);
            DataManager.getInstance().setUser(u);
            DataManager.getInstance().setLogin_flag(true);

            SharedPreferencesHelper.saveWeixinOpenID(openid);
//            SharedPreferencesHelper.saveWeixinHeader(weixinHeader);
            SharedPreferencesHelper.saveWeixinNickName(weixinNickName);
            SharedPreferencesHelper.saveWeixinSex(weixinSex + "");

            SharedPreferencesHelper.saveaccounttype(SharedPreferencesHelper.WEIXINLOGIN);

//            postMessage(LOGIN_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }

    }


private static final int LOGIN_SUCCESS = 200;

    private void parserLogin(String params) {
        String result = DataManager.getInstance().validateResult(params);
        if (result != null) {
            if ("OK".equals(result)) {
                DataManager.getInstance().jsonParse(params,
                        DataManager.TYPE_SIGNUP);
//                postMessage(LOGIN_SUCCESS);
            } else {
                // postMessage(POPUP_TOAST, result);
//                postMessage(DISMISS_PROGRESS);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (super.handleMessage(msg))
            return true;
        switch (msg.what) {
            case LOGIN_SUCCESS:
                // Toast.makeText(this, "登录成功", 0).show();
                break;
        }
        return true;
    }


    private void QQLogin ()
    {
        ThirdLogin thirdLogin = ThirdLogin.getInstance();
        thirdLogin.QQLogin(false);
    }

    private void SinaLogin ()
    {
        ThirdLogin thirdLogin = ThirdLogin.getInstance();
        thirdLogin.SinaLogin(false);
    }


}
