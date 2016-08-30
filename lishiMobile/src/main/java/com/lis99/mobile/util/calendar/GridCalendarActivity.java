package com.lis99.mobile.util.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.lis99.mobile.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GridCalendarActivity extends Activity {
    private GridCalendarView gridCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_calendar_view);
        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH)+ 1;
        List<CalendarInfo> list = new ArrayList<CalendarInfo>();
        list.add(new CalendarInfo(currYear,currMonth,4,"已截至"));
        list.add(new CalendarInfo(currYear,currMonth,6,"已报名"));
        list.add(new CalendarInfo(currYear,currMonth,12,"￥1200"));
        list.add(new CalendarInfo(currYear,currMonth,16,"￥1200"));
        list.add(new CalendarInfo(currYear,currMonth,28,"￥1200"));
        list.add(new CalendarInfo(currYear,currMonth,1,"￥1200",1));
        list.add(new CalendarInfo(currYear,currMonth,11,"￥1200",1));
        list.add(new CalendarInfo(currYear,currMonth,19,"￥1200",2));
        list.add(new CalendarInfo(currYear,currMonth,21,"￥1200",1));
        gridCalendarView = (GridCalendarView) findViewById(R.id.gridMonthView);

//        gridCalendarView.setDayTheme(new CalendarTheme());
        gridCalendarView.setWeekTheme(new DefaultWeekTheme());

        gridCalendarView.setCalendarInfos(list);
        gridCalendarView.setDateClick(new MonthView.IDateClick(){

            @Override
            public void onClickOnDate(int year, int month, int day) {
                Toast.makeText(GridCalendarActivity.this,"点击了" +  year + "-" + month + "-" + day,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
