package com.lis99.mobile.util.calendar;

import android.graphics.Color;

/**
 * Created by Administrator on 2016/7/31.
 */
public class DefaultWeekTheme implements IWeekTheme {
    @Override
    public int colorTopLinen() {
        return Color.parseColor("#F1F1F1");
    }

    @Override
    public int colorBottomLine() {
        return Color.parseColor("#F1F1F1");
    }

    @Override
    public int colorWeekday() {
        return Color.parseColor("#c2c2c2");
    }

    @Override
    public int colorWeekend() {
        return Color.parseColor("##c2c2c2");
    }

    @Override
    public int colorWeekView() {
        return Color.parseColor("#F1F1F1");
    }

    @Override
    public int sizeLine() {
        return 4;
    }

    @Override
    public int sizeText() {
        return 14;
    }
}
