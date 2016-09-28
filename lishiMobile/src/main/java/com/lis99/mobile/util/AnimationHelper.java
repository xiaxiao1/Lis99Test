package com.lis99.mobile.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by xiaxiao on 2016/9/28.
 */
public class AnimationHelper {
    int delay=0;
    Context mContext;

    public static final int TYPE_HIDE=0;
    public static final int TYPE_SHOW=1;
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startPropertyAnimationY(View targetView,int type){
        final View vvv=targetView;
        final ViewGroup.LayoutParams lp=targetView.getLayoutParams();
        ValueAnimator va;
        if (type==TYPE_HIDE) {
            va=ValueAnimator.ofInt(targetView.getHeight(),0);
            Common.Log_i("targetView.getHeight():"+targetView.getHeight()+"  "+lp.height);
        } else if (type == TYPE_SHOW) {
            va = ValueAnimator.ofInt(0, targetView.getHeight());
        } else {
            return;
        }
//        va=ValueAnimator.ofInt(fromY,toY);
        va.setDuration(1000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height=(int)animation.getAnimatedValue();
                vvv.setLayoutParams(lp);
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startRotateAnimation(View targetView, float from, float to) {
        ObjectAnimator o = ObjectAnimator.ofFloat(targetView, "rotation", from, to);
        o.setDuration(1000);
        o.start();
    }
}
