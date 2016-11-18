package com.lis99.mobile.club.activityinfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.activityinfo.adapter.SpecAdapterNew;
import com.lis99.mobile.club.model.BatchListEntity;
import com.lis99.mobile.club.model.SpecInfoListModel;
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

    private View layout;
    private MyListView list;

    private SpecAdapterNew adapter;

    private TopicSeriesBatchsListModel model;

    private SpecInfoListModel specModel;

    private int activityId, clubId;

    private List<CalendarInfo> listInfo;

    private CalendarInfo info, currentMonth;
//  选中的系列活动
    private BatchListEntity selectEntity;

    private Button btnOk;

    private TextView tv_desc;

    private int batchId = -1;

    private PayModel payModel;
//  出行日期
    private String startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityId = getIntent().getIntExtra("ACTIVITYID", -1);
        clubId = getIntent().getIntExtra("CLUBID", -1);

        setContentView(R.layout.activity_grid_calendar_view);

        initViews();

        setTitle("选择出发日期");

        setBtnClick(false);

//        getList();
        getListNew();
    }

    @Override
    protected void initViews() {
        super.initViews();

        tv_desc = (TextView) findViewById(R.id.tv_desc);

        layout = findViewById(R.id.layout);
        list = (MyListView) findViewById(R.id.list);
        list.setAdapter(null);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

//        setBtnClick(false);

        gridCalendarView = (GridCalendarView) findViewById(R.id.gridMonthView);

//        gridCalendarView.setDayTheme(new CalendarTheme());
        gridCalendarView.setWeekTheme(new DefaultWeekTheme());

//        gridCalendarView.setCalendarInfos(listInfo);
        gridCalendarView.setDateClick(new MonthView.IDateClick(){

            @Override
            public void onClickOnDate(int year, int month, int day) {

                setBtnClick(false);



                info = iscalendarInfo(year, month, day);

//                已过期， 的不能报名
                if ( info == null ) return;

                int num = listInfo.indexOf(info);
                if ( num == -1 ) return;

                BatchListEntity item = model.batchList.get(num);

                String m = DateUtils.getMonth(item.settime)+"月";
                String d  = getDay(item.settime)+"日";
                String t = " "+getTime(item.settime);

                tv_desc.setText("（名额"+item.people+"人， 集合时间："+m+d+t+"）");

                batchId = item.batchId;

                startTime = item.starttime;

                getSpecs(item.batchId);


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
            btnOk.setText("下一步：填写报名信息");
            btnOk.setClickable(true);
        }
        else
        {
            btnOk.setBackgroundColor(Color.parseColor("#e2e2e2"));
            btnOk.setText("请选择规格");
            btnOk.setClickable(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                //TODO implement

                if ( adapter == null || adapter.getSelectNum() == 0 ) return;
//
                if ( batchId == -1 )
                {
                    return;
                }
//                    Common.toast("is click="+adapter.getSelectNum());
                goNextActivity(batchId);

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanList();
    }
//  获取活动规格
    private void getSpecs ( int batchId )
    {
            list.setAdapter(null);
            layout.setVisibility(View.VISIBLE);

        String url = C.SPEC_LIST;
        HashMap<String, Object> map = new HashMap<>();

        map.put("batch_id", batchId);
        map.put("activity_id", activityId);

        specModel = new SpecInfoListModel();

        MyRequestManager.getInstance().requestPost(url, map, specModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                specModel = (SpecInfoListModel) mTask.getResultModel();
                if ( specModel == null || specModel.guigelist == null ) return;

                if ( layout.getVisibility() == View.VISIBLE )
                    layout.setVisibility(View.GONE);

                adapter = new SpecAdapterNew(activity, specModel.guigelist);

                list.setAdapter(adapter);

            }
        });

    }
//    获取活动日期
    private void getListNew ()
    {
        String url = C.BATCH_TIMES;

        String userId = Common.getUserId();

        HashMap<String, Object> map = new HashMap<>();

//        activityId = 6436;

        map.put("user_id", userId);
        map.put("activity_id", activityId);

        model = new TopicSeriesBatchsListModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (TopicSeriesBatchsListModel) mTask.getResultModel();

                if (model == null || model.batchList == null || model.batchList.size() == 0) return;

                listInfo = new ArrayList<CalendarInfo>();

                for (int i = 0; i < model.batchList.size(); i++) {
                    BatchListEntity item = model.batchList.get(i);
                    int[] ymd = new int[3];
                    ymd[0] = DateUtils.getYear(item.starttime);
                    ymd[1] = DateUtils.getMonth(item.starttime);
                    ymd[2] = DateUtils.getDay(item.starttime);

                        CalendarInfo ci = null;
                        if (item.isEnd == 1) {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 1, "已过期");
                        } else if (item.isBaoming == 1) {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 2, "已报名");
                        } else {
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 0, "¥" + getPrice(item.price)+"起");
//                            默认显示的月份
                            if (currentMonth == null) {
                                currentMonth = ci;
                            }
                        }
                        listInfo.add(ci);
                    }

//                如果没有可报名的信息， 展示最后一个服务器信息月份
                if ( currentMonth == null && listInfo != null && listInfo.size() > 0 )
                {
                    currentMonth = listInfo.get(listInfo.size() - 1);
                }
//              设置活动启止时间
                gridCalendarView.setLastDay(listInfo.get(0), listInfo.get(listInfo.size() - 1));
//                设置活动列表
                gridCalendarView.setCalendarInfos(listInfo);
//                设置当前选中的日期
                gridCalendarView.setCurrentMonth(currentMonth);

            }
        });



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
                CalendarInfo lastDay = null;
                CalendarInfo firstDay = null;

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
                            ci = new CalendarInfo(ymd[0], ymd[1], ymd[2], 2, "已报名");
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
//                        查找最后一个活动
                        lastDay = getLastDay (ci, lastDay);
                        firstDay = getFirstDay(ci, firstDay);

                    } else {
                        CalendarInfo cinfo = map.get(item.starttime);
                        if (item.isEnd == 1) {
                            cinfo.isOverdue = 1;
                            cinfo.des = "已过期";
                        } else if (item.isBaoming == 1) {
                            cinfo.isOverdue = 2;
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

                gridCalendarView.setLastDay(lastDay, firstDay);
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
        payModel = new PayModel();
        payModel.clubId = clubId;
        payModel.batchId = batchId;
        payModel.topicId = activityId;
        payModel.selectNum = adapter.getSelectNum();
//                获取选择的规格信息
        payModel.batchs = adapter.getInfo();
//        先计算规格数量， 再获取价格
        payModel.price = adapter.getPrice();

        payModel.joinList = adapter.getJoinList();

        payModel.startTime = startTime;

        Intent intent = new Intent(activity, LSApplySeriesNew.class);
        intent.putExtra("PAYMODEL", payModel);
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

    /**
     *      判断日期， 返回日期最大的对象
     * @param info
     * @param last
     * @return
     */
    private CalendarInfo getLastDay ( CalendarInfo info, CalendarInfo last )
    {
        if ( last == null ) return info;
        if ( info.year > last.year || (info.year == last.year && info.month > last.month) )
        {
            return info;
        }
        return last;
    }

    /**
     *      判断日期， 返回日期最小的对象
     * @param info
     * @param first
     * @return
     */
    private CalendarInfo getFirstDay ( CalendarInfo info, CalendarInfo first )
    {
        if ( first == null ) return info;
        if ( info.year < first.year || (info.year == first.year && info.month < first.month) )
        {
            return info;
        }
        return first;
    }


    /**
     * 		获取日
     * @param str
     * @return
     */
    public static String getTime (String str)
    {
        String day = "";

        if (TextUtils.isEmpty(str))
        {
            return day;
        }
        String[] d = str.split(" ");
        if ( d.length > 1 )
            day = d[1];

        return day;
    }

    /**
     * 		获取日
     * @param str
     * @return
     */
    public static int getDay (String str)
    {
        int day = 1;

        if (TextUtils.isEmpty(str))
        {
            return day;
        }
        String[] d = str.split("\\.");
        if ( d.length > 2 )
        {
            if ( !TextUtils.isEmpty(d[2]))
            {
                String[] dd = d[2].split(" ");
                if ( dd.length > 1)
                {
                    day = Common.string2int(dd[0]);
                }
            }
        }

        return day;
    }

}
