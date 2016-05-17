package com.lis99.mobile.util;

import com.lis99.mobile.util.push.JPush;
import com.lis99.mobile.util.push.PushBase;

/**
 * Created by yy on 16/5/17.
 */
public class PushManagerJPush extends PushManagerF{
    @Override
    PushBase create() {
        return new JPush();
    }
}
