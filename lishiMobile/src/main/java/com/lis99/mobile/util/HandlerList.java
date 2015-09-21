package com.lis99.mobile.util;

import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/16.
 */
public class HandlerList {

    private static  HandlerList instance;


    private ArrayList<CallBack> list;

    private MyTask o;

    public void setObject ( MyTask o )
    {
        this.o = o;
    }

    public HandlerList ()
    {
        if ( list == null )
            list = new ArrayList<CallBack>();
    }

    public static HandlerList getInstance ()
    {
        if ( instance == null ) instance = new HandlerList();
        return instance;
    }


    public void addItem ( CallBack call )
    {
        if ( list == null ) list = new ArrayList<CallBack>();
        if ( list.contains(call)) return;
        list.add(call);
    }

    public void handlerAall ()
    {
        if ( list == null ) return;
        for ( CallBack call : list )
        {
            call.handler(o);
        }
    }

    public void removeItem ( CallBack call )
    {
        if ( list == null ) return;
        list.remove(call);
    }

    public void clean ()
    {
        list.clear();
        list = null;
    }




}
