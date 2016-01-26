package com.lis99.mobile.util.push;

import android.content.Context;

import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.SharedPreferencesHelper;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by yy on 16/1/5.
 */
public class JPush implements PushBase {


    private String Token = "";


    @Override
    public void RegisterPush() {

    }

    @Override
    public void UnRegisterPush() {

    }

    @Override
    public void init(Context c ) {

            JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
            JPushInterface.init(c);     		// 初始化 JPush

    }

    @Override
    public String getToken() {
        return Token;
    }

    @Override
    public void setToken(String token)
    {
        Token = token;
//        保存token
        SharedPreferencesHelper.saveJPushToken(token);
        LSRequestManager.getInstance().upDataInfo();
    }
}
