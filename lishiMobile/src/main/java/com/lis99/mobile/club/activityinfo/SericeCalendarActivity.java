package com.lis99.mobile.club.activityinfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.activityinfo.adapter.SpecAdapter;
import com.lis99.mobile.club.model.BatchListEntity;
import com.lis99.mobile.club.model.TopicSeriesBatchsListModel;
import com.lis99.mobile.club.newtopic.series.LSApplySeriesNew;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.calendar.CalendarInfo;
import com.lis99.mobile.util.calendar.DateUtils;
import com.lis99.mobile.util.calendar.DefaultWeekTheme;
import com.lis99.mobile.util.calendar.GridCalendarView;
import com.lis99.mobile.util.calendar.MonthView;
import com.lis99.mobile.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by yy on 16/8/23.
 */
public class SericeCalendarActivity extends LSBaseActivity {

    private GridCalendarView gridCalendarView;

    private TextView tvNone;
    private MyListView list;

    private SpecAdapter adapter;

    private TopicSeriesBatchsListModel model;

    private int activityId, clubId;

    private List<CalendarInfo> listInfo;

    private CalendarInfo info, currentMonth;
//  选中的系列活动
    private BatchListEntity selectEntity;

    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityId = getIntent().getIntExtra("ACTIVITYID", -1);
        clubId = getIntent().getIntExtra("CLUBID", -1);

        setContentView(R.layout.activity_grid_calendar_view);

        initViews();

        setTitle("选择出发日期");

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        tvNone = (TextView) findViewById(R.id.tv_none);
        list = (MyListView) findViewById(R.id.list);
        list.setAdapter(null);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        setBtnClick(false);

//        Calendar calendar = Calendar.getInstance();
//        int currYear = calendar.get(Calendar.YEAR);
//        int currMonth = calendar.get(Calendar.MONTH)+ 1;
//
//        listInfo = new ArrayList<CalendarInfo>();
//        listInfo.add(new CalendarInfo(currYear,currMonth,4,"已截至"));
//        listInfo.add(new CalendarInfo(currYear,currMonth,6,"已报名"));
//        listInfo.add(new CalendarInfo(currYear,currMonth,12,"￥1200"));
//        listInfo.add(new CalendarInfo(currYear,currMonth,16,"￥1200"));
//        listInfo.add(new CalendarInfo(currYear,currMonth,28,"￥1200"));
//        listInfo.add(new CalendarInfo(currYear,currMonth,1,"￥1200",1));
//        listInfo.add(new CalendarInfo(currYear,currMonth,11,"￥1200",1));
//        listInfo.add(new CalendarInfo(currYear,currMonth,19,"￥1200",2));
//        listInfo.add(new CalendarInfo(currYear,currMonth,21,"￥1200",1));

        gridCalendarView = (GridCalendarView) findViewById(R.id.gridMonthView);

//        gridCalendarView.setDayTheme(new CalendarTheme());
        gridCalendarView.setWeekTheme(new DefaultWeekTheme());

//        gridCalendarView.setCalendarInfos(listInfo);
        gridCalendarView.setDateClick(new MonthView.IDateClick(){

            @Override
            public void onClickOnDate(int year, int month, int day) {
//                Toast.makeText(activity,"点击了" +  year + "-" + month + "-" + day,Toast.LENGTH_SHORT).show();

                info = iscalendarInfo(year, month, day);

                if ( info == null ) return;
                if ( tvNone.getVisibility() == View.VISIBLE)
                tvNone.setVisibility(View.GONE);

                adapter = new SpecAdapter(activity, info.batchList);

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if ( adapter == null ) return;
                        selectEntity = (BatchListEntity) adapter.getItem(position);
                        if ( selectEntity.isEnd == 1 || selectEntity.isBaoming == 1 )
                        {
                            return;
                        }
                        adapter.setCurrentPosition(position);
                        setBtnClick(true);
                    }
                });

//                if ( adapter != null && adapter.getCurrentPosition() != -1 )
//                setBtnClick(true);
            }
        });




    }

//  按钮状态， true可点击， false 不可点击
    public void setBtnClick ( boolean clickable )
    {
        if ( clickable )
        {
            btnOk.setBackgroundColor(getResources().getColor(R.color.text_color_green));
            btnOk.setText("下一步");
            btnOk.setClickable(true);
        }
        else
        {
            btnOk.setBackgroundColor(Color.parseColor("#e2e2e2"));
            btnOk.setText("请选择游玩日期");
            btnOk.setClickable(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                //TODO implement

                if ( adapter == null || adapter.getCurrentPosition() == -1 ) return;

                int batchId = adapter.getBatchId();

                if ( batchId == -1 )
                {
                    return;
                }

                goNextActivity(batchId);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanList();
    }

    private void getList ()
    {
        listInfo = new ArrayList<CalendarInfo>();

        String url = C.CLUB_TOPIC_ACTIVE_SERIES_LINE_LIST;

        String userId = Common.getUserId();

        HashMap<String, Object> map = new HashMap<>();

        map.put("user_id", userId);
        map.put("activity_id", activityId);

        model = new TopicSeriesBatchsListModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (TopicSeriesBatchsListModel) mTask.getResultModel();

                if (model == null || model.batchList == null || model.batchList.size() == 0) return;

                LinkedHashMap<String, CalendarInfo> map = new LinkedHashMap<>();

                for (int i = 0; i < model.batchList.size(); i++) {
                    BatchListEntity item = model.batchList.get(i);
                    int[] ymd = new int[3];
                    ymd[0] = DateUtils.getYear(item.starttime);
                    ymd[1] = DateUtils.getMonth(item.starttime);
                    ymd[2] = DateUtils.getDay(item.starttime);

                    if (!map.containsKey(item.starttime)) {
                        CalendarInfo ci = null;
                        if (item.isEnd == 1) {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 1, "已过期");
                        } else if (item.isBaoming == 1) {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 1, "已报名");
                        } else {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 0, "¥" + getPrice(item.price)+"起");
//                            默认显示的月份
                            if (currentMonth == null) {
                                currentMonth = ci;
                            }
                        }
                        if (ci.batchList == null) ci.batchList = new ArrayList<BatchListEntity>();
                        ci.batchList.add(item);
                        map.put(item.starttime, ci);
                    } else {
                        CalendarInfo cinfo = map.get(item.starttime);
                        if (item.isEnd == 1) {
                            cinfo.isOverdue = 1;
                            cinfo.des = "已过期";
                        } else if (item.isBaoming == 1) {
                            cinfo.isOverdue = 1;
                            cinfo.des = "已报名";
                        }
                        cinfo.batchList.add(item);
                    }

                }

//              合并列表
                listInfo = new ArrayList<CalendarInfo>();
                Set<String> set = map.keySet();
                for (String key : set) {
                    listInfo.add(map.get(key));
                }
//                价格做一个排序
                for (CalendarInfo info : listInfo)
                {
//                    规格根据价格从小到大排序
//                    Collections.sort(info.batchList, new Comparator<BatchListEntity>() {
//                        @Override
//                        public int compare(BatchListEntity lhs, BatchListEntity rhs) {
//                            return Common.string2Integer(getPrice(lhs.price)).compareTo(Common.string2Integer(getPrice(rhs.price)));
//                        }
//                    });
                    int price = Integer.MAX_VALUE;
                    for ( BatchListEntity be : info.batchList )
                    {
                        int p = Common.string2Integer(getPrice(be.price));
                        if ( price >  p )
                        {
                            price = p;
                        }
                    }

//                    显示价格的, 取最低的价格
                    if ( info.isOverdue == 0 )
                    {
                        info.des = "¥"+getPrice(""+price)+"起";
                    }

                }

//                如果没有可报名的信息， 展示最后一个服务器信息月份
                if ( currentMonth == null && listInfo != null && listInfo.size() > 0 )
                {
                    currentMonth = listInfo.get(listInfo.size() - 1);
                }

                gridCalendarView.setCalendarInfos(listInfo);
                gridCalendarView.setCurrentMonth(currentMonth);
            }
        });
    }

    private void cleanList ()
    {
        listInfo = null;
        currentMonth = null;
        info = null;
    }

    private void goNextActivity (int batchId)
    {
        Intent intent = new Intent(activity, LSApplySeriesNew.class);
        intent.putExtra("clubID", clubId);
        intent.putExtra("batchID", batchId);
        intent.putExtra("topicID", activityId);
        startActivityForResult(intent, 997);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      退出
        if (requestCode == 997 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }


    /**
     * 判断是否为事务天数,通过获取desc来辨别
     * @param day
     * @return
     */
    protected CalendarInfo iscalendarInfo(int year,int month,int day){
        if(listInfo == null || listInfo.size() == 0)return null;
        for(CalendarInfo calendarInfo : listInfo){
            if(calendarInfo.day == day && calendarInfo.month == month && calendarInfo.year == year){
                return calendarInfo;
            }
        }
        return null;
    }


    private String getPrice ( String price )
    {
        int end = price.indexOf(".");
        if ( end == -1 ) return price;
        return price.substring(0, end);
    }




}
