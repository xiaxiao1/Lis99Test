package com.lis99.mobile.util;

import com.lis99.mobile.engine.base.CallBack;

import java.util.LinkedList;

/**
 * Created by yy on 15/8/5.
 */
public class LoginCallBackManager {

    private LinkedList<CallBack> list;

    private static LoginCallBackManager instance;

    public static LoginCallBackManager getInstance ()
    {
        if ( instance == null ) instance = new LoginCallBackManager();
        return instance;
    }

    public void addCallBack ( CallBack call )
    {
        if ( list == null ) list = new LinkedList<CallBack>();
        list.add(call);
    }

    public void cleanAll ()
    {
        list.clear();
    }

    public void handler ()
    {
        if ( list == null ) return;
        for ( CallBack call : list )
        {
            call.handler(null);
        }
    }

}
