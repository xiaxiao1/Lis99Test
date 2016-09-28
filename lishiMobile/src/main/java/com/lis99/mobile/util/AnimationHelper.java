package com.lis99.mobile.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by xiaxiao on 2016/9/28.
 */
public class AnimationHelper {
    int delay=0;
    Context mContext;

    public AnimationHelper(Context context) {
        this.mContext=context;
    }
    public void resetDelay(){
        delay=0;
    }
    public void showAnimation(View targetView, int animId){
        Animation a = AnimationUtils.loadAnimation(mContext, animId);
        a.setStartOffset(delay);
        targetView.startAnimation(a);
    }

    public void showAnimation(View targetView,int animId,int delay){
        this.delay+=delay;
        showAnimation(targetView,animId);
    }
}
