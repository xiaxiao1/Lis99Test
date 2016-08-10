package com.lis99.mobile.engine.base;

/**
 * Created by yy on 16/8/10.
 */
public abstract class CallBackBase {

    abstract public void handler(MyTask mTask);

    abstract public void handlerError (MyTask mTask);

    abstract public void handlerCancel (MyTask mTask);
}
