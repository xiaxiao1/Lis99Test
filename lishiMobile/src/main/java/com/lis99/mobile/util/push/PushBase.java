package com.lis99.mobile.util.push;

import android.content.Context;

/**
 * Created by yy on 16/1/5.
 */
public interface PushBase {

    public void RegisterPush ();

    public void UnRegisterPush ();

    public void init ( Context c );

    public String getToken ();

    public void setToken (String token);

}
