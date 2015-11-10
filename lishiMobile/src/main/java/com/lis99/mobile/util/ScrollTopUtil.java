package com.lis99.mobile.util;

/**
 * Created by yy on 15/11/6.\
 *      控制界面滚动到顶部
 */
public class ScrollTopUtil {

    private static ScrollTopUtil instance;

    private ToTop toTop;

    public static ScrollTopUtil getInstance ()
    {
        if ( instance == null ) instance = new ScrollTopUtil();
        return instance;
    }

    public void setToTop ( ToTop top )
    {
        toTop = top;
    }

    public void scrollToTop ()
    {
        if ( toTop != null )
        {
            toTop.handler();
        }
    }

    public interface ToTop
    {
        public void handler();
    }


}
