package com.lis99.mobile.util.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;

import java.util.List;

/**
 * Created by Administrator on 2016/7/31.
 */
public class GridCalendarView extends LinearLayout implements View.OnClickListener {
    private WeekView weekView;
    private GridMonthView gridMonthView;
    private TextView textViewYear, textViewMonth;
    private ImageView left, right;

    private CalendarInfo lastDay, firstDay;

    public GridCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.display_grid_date, null);
        weekView = new WeekView(context, null);
        gridMonthView = new GridMonthView(context, null);
        addView(view, llParams);
        addView(weekView, llParams);
        addView(gridMonthView, llParams);

        left = (ImageView) view.findViewById(R.id.left);
        right = (ImageView) view.findViewById(R.id.right);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        textViewYear = (TextView) view.findViewById(R.id.year);
        textViewMonth = (TextView) view.findViewById(R.id.month);

        gridMonthView.setMonthLisener(new MonthView.IMonthLisener() {
            @Override
            public void setTextMonth() {
                textViewYear.setText(gridMonthView.getSelYear() + "-");
                textViewMonth.setText((gridMonthView.getSelMonth() + 1) + "");

                dotClickable(gridMonthView.getSelYear(), gridMonthView.getSelMonth());
            }
        });
    }

    public void setRightUnClick ()
    {
        right.setBackgroundResource(R.drawable.calendar_right_unclick);
    }

    public void setRightClick ()
    {
        right.setBackgroundResource(R.drawable.calendar_right);
    }

    public void setLeftUnClick ()
    {
        left.setBackgroundResource(R.drawable.calendar_left_unclick);
    }

    public void setLeftClick ()
    {
        left.setBackgroundResource(R.drawable.calendar_left);
    }

    /**
     *      最后一个月， 最早一个月
     * @param info
     */
    public void setLastDay(CalendarInfo ...info)
    {
        lastDay = info[0];
        firstDay = info[1];
        gridMonthView.setLastDay(info[0], info[1]);
    }


    public void setCurrentMonth ( CalendarInfo info )
    {
        if ( gridMonthView != null && info != null )
        {
            gridMonthView.setCurrentMonth(info.year, info.month - 1, info.day);
        }
        else
        {
            gridMonthView.setCurrentMonth();
        }
    }

    /**
     * 设置日历点击事件
     * @param dateClick
     */
    public void setDateClick(MonthView.IDateClick dateClick){
        gridMonthView.setDateClick(dateClick);
    }

    /**
     * 设置星期的形式
     * @param weekString
     * 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString){
        weekView.setWeekString(weekString);
    }

    public void setCalendarInfos(List<CalendarInfo> calendarInfos){
        gridMonthView.setCalendarInfos(calendarInfos);
    }

    public void setDayTheme(IDayTheme theme){
        gridMonthView.setTheme(theme);
    }

    public void setWeekTheme(IWeekTheme weekTheme){
        weekView.setWeekTheme(weekTheme);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.left){
            gridMonthView.onLeftClick();
        }else{
            gridMonthView.onRightClick();
        }
    }

    //    设置按钮点击状态
    private void dotClickable ( int selYear, int selMonth )
    {
        setLeftClick();
        setRightClick();
        //如果当前年、月 》＝ 数据最后的年 、月  返回false
        if ( lastDay != null && selYear >= lastDay.year && selMonth+1 >= lastDay.month )
        {
            setRightUnClick();
        }

        //如果当前年、月 》＝ 数据最后的年 、月  返回false
        if ( firstDay != null && selYear <= firstDay.year && selMonth + 1 <= firstDay.month )
        {
            setLeftUnClick();
        }
    }

}