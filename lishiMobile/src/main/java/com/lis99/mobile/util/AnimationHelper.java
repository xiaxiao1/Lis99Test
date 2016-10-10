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

/**负责动画
 * Created by xiaxiao on 2016/9/28.
 */
public class AnimationHelper {
    int delay=0;
    Context mContext;

    public static final int TYPE_HIDE=0;
    public static final int TYPE_SHOW=1;
    public static final int TIME_NORMAL=500;
    public static final int TIME_SHORT=250;
    public static final int TIME_LONG=1000;
    public AnimationHelper(Context context) {
        this.mContext=context;
    }

    /**
     * 重新设置动画启动延时
     */
    public void resetDelay(){
        delay=0;
    }

    /**
     * 为view添加动画
     * @param targetView 要执行动画的view
     * @param animId 要显示的动画id
     */
    public void showAnimation(View targetView, int animId){
        Animation a = AnimationUtils.loadAnimation(mContext, animId);
        a.setStartOffset(delay);
        targetView.startAnimation(a);
    }

    /**
     * 为View添加动画
     * @param targetView 要执行动画的view
     * @param animId 要显示的动画id
     * @param delay 动画的启动延时，本延时是基于上一个动画的启动时间的。
     */
    public void showAnimation(View targetView,int animId,int delay){
        this.delay+=delay;
        showAnimation(targetView,animId);
    }

    /**
     * 垂直方向显示与隐藏的属性动画
     * @param targetView 目标View
     * @param type 动画类型 显示/隐藏
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startPropertyAnimationY(View targetView,int type){
        final View vvv=targetView;
        final ViewGroup.LayoutParams lp=targetView.getLayoutParams();
        ValueAnimator va;
        if (type==TYPE_HIDE) {
            va=ValueAnimator.ofInt(targetView.getHeight(),0);
//            Common.Log_i("targetView.getHeight():"+targetView.getHeight()+"  "+lp.height);
        } else if (type == TYPE_SHOW) {
            va = ValueAnimator.ofInt(0, targetView.getHeight());
        } else {
            return;
        }
        va.setDuration(TIME_SHORT);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height=(int)animation.getAnimatedValue();
                vvv.setLayoutParams(lp);
            }
        });
        va.start();

    }

    /**
     * 垂直方向显示与隐藏的属性动画
     * @param targetView 目标View
     * @param fromY 开始高度
     * @param toY  结束高度
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startPropertyAnimationY(View targetView,int fromY,int toY){
        final View vvv=targetView;
        final ViewGroup.LayoutParams lp=targetView.getLayoutParams();
        ValueAnimator va;
        va=ValueAnimator.ofInt(fromY,toY);
        va.setDuration(TIME_SHORT);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height=(int)animation.getAnimatedValue();
                vvv.setLayoutParams(lp);
            }
        });
        va.start();

    }

    /**
     * 旋转属性动画
     * @param targetView 目标View
     * @param from 开始角度
     * @param to 结束角度
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startRotateAnimation(View targetView, float from, float to) {
        ObjectAnimator o = ObjectAnimator.ofFloat(targetView, "rotation", from, to);
        o.setDuration(TIME_SHORT);
        o.start();
    }
}
