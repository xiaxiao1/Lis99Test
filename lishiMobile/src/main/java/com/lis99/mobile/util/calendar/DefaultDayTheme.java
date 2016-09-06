package com.lis99.mobile.util.calendar;

import android.graphics.Color;

/**
 * Created by Administrator on 2016/7/30.
 */
public class DefaultDayTheme implements IDayTheme {
    @Override
    public int colorSelectBG() {
        return Color.parseColor("#3ac01a");
    }

    @Override
    public int colorSelectDay() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int colorToday() {
        return Color.parseColor("#3ac01a");
    }

    @Override
    public int colorMonthView() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int colorWeekday() {
        return Color.parseColor("#C2C2C2");
    }

    @Override
    public int colorWeekend() {
        return Color.parseColor("#C2C2C2");
    }

    @Override
    public int colorDecor() {
        return Color.parseColor("#68CB00");
    }

    @Override
    public int colorRest() {
        return Color.parseColor("#68CB00");
    }

    @Override
    public int colorWork() {
        return Color.parseColor("#FF9B12");
    }

    @Override
    public int colorDesc() {
        return Color.parseColor("#3ac01a");
    }

    @Override
    public int sizeDay() {
        return 14;
    }

    @Override
    public int sizeDecor() {
        return 6;
    }

    @Override
    public int sizeDesc() {
        return 10;
    }

    @Override
    public int dateHeight() {
        return 50;
    }

    @Override
    public int colorLine() {
        return Color.parseColor("#EEEEEE");
    }

    @Override
    public int smoothMode() {
        return 0;
    }

    @Override
    public boolean noneClick() {
        return false;
    }

    /**
     * 能选择日期颜色
     *
     * @return
     */
    @Override
    public int clickDay() {
        return Color.parseColor("#000000");
    }

    /**
     * 不能用的描述颜色
     *
     * @return
     */
    @Override
    public int unDesc() {
        return Color.parseColor("#999999");
    }

    /**
     * 默认日期剧中， paddingTop为负数上提一些
     *
     * @return
     */
    @Override
    public int paddingTop() {
        return -5;
    }

    public int colorAfterDay ()
    {
        return Color.parseColor("#000000");
    }
}
