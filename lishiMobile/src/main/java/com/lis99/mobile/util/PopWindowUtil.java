package com.lis99.mobile.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.filter.model.NearbyFilterModel;
import com.lis99.mobile.club.model.ActiveMainCityListModel;
import com.lis99.mobile.club.model.TopicSeriesBatchsListModel;
import com.lis99.mobile.club.newtopic.series.model.ManagerSeriesLineListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lis99.mobile.R.id.iv_line;

/**
 * Created by yy on 15/7/27.
 */
public class PopWindowUtil {

    private static PopupWindow pop;

    public static PopupWindow showTopicReply ( View parent, CallBack callBack )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }



        return pop;
    }


    /**
     * 选择发帖俱乐部
     * */
    public static PopupWindow showTopicClub (  int position, View parent, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.topic_club_new_topic, null);

        View bg = v.findViewById(R.id.bg);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final ArrayList<HashMap<String, String>> alist = PopWindowData.getTopicClub();

        currentMapClub = alist.get(0);

        setMap(alist.get(position), currentMapClub);

        final ActiveAllTimesAdapter adapter = new ActiveAllTimesAdapter(LSBaseActivity.activity, alist);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callBack != null) {
                    HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(i);
                    setMap(map, currentMapClub);
                    String[] values = new String[2];
                    values[0] = alist.get(i).get("name");
                    values[1] = alist.get(i).get("value");
                    MyTask task = new MyTask();
                    task.setresult(""+i);
                    task.setResultModel(values);
                    callBack.handler(task);
                    closePop();
                }
            }
        });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
//        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.showAsDropDown(parent);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//                WindowManager.LayoutParams lp = LSBaseActivity.activity
//                        .getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                LSBaseActivity.activity.getWindow().setAttributes(lp);
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

//        WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
//                .getAttributes();
//        lp.alpha = 0.7f;
//        LSBaseActivity.activity.getWindow().setAttributes(lp);

        return pop;
    }

    /**
     * 选择城市 ， 线路筛选
     * */
    public static PopupWindow showActiveCityList (  int position, View parent, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.active_all_times_chose, null);

        View bg = v.findViewById(R.id.bg);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final ArrayList<HashMap<String, String>> alist = PopWindowData.getActiveCityList();

        currentActiveCity = alist.get(0);

        setMap(alist.get(position), currentActiveCity);

        final ActiveAllTimesAdapter adapter = new ActiveAllTimesAdapter(LSBaseActivity.activity, alist);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callBack != null) {
                    HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(i);
                    setMap(map, currentActiveCity);
                    String[] values = new String[2];
                    values[0] = alist.get(i).get("name");
                    values[1] = alist.get(i).get("value");
                    MyTask task = new MyTask();
                    task.setresult(""+i);
                    task.setResultModel(values);
                    callBack.handler(task);
                    closePop();
                }
            }
        });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                Common.HEIGHT - Common.dip2px(200));

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.showAtLocation(parent, Gravity.BOTTOM, 0, Common.dip2px(50));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//                WindowManager.LayoutParams lp = LSBaseActivity.activity
//                        .getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                LSBaseActivity.activity.getWindow().setAttributes(lp);
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

//        WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
//                .getAttributes();
//        lp.alpha = 0.7f;
//        LSBaseActivity.activity.getWindow().setAttributes(lp);

        return pop;
    }

  /**
   * 选择城市
   * */
    public static void showActiveMainCityList ( String cityName, String cityId, final int position, final View parent, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }

//        获取城市列表
        PopWindowData.getActiveMainCityList(cityName, cityId, new CallBack() {
            @Override
            public void handler(MyTask mTask) {

                if ( mTask == null ) return;
                ActiveMainCityListModel model = (ActiveMainCityListModel) mTask.getResultModel();

                if ( model == null ) return;

                setCurrentCity(model.citylist.get(position));

                View v = View.inflate(LSBaseActivity.activity, R.layout.active_all_times_chose, null);

                View bg = v.findViewById(R.id.bg);

                bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closePop();
                    }
                });

                final ListView list = (ListView) v.findViewById(R.id.list);

                final ActiveCityMainList adapter = new ActiveCityMainList(LSBaseActivity.activity, model.citylist);

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (callBack != null) {
                            ActiveMainCityListModel.CitylistEntity map = (ActiveMainCityListModel
                                    .CitylistEntity) adapter.getItem(i);
                            setCurrentCity(map);
                            String[] values = new String[2];
                            values[0] = map.name;
                            values[1] = map.id;
                            MyTask task = new MyTask();
                            task.setresult(""+i);
                            task.setResultModel(values);
                            callBack.handler(task);
                            closePop();
                        }
                    }
                });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

                pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                        Common.HEIGHT - Common.dip2px(100));

                pop.setOutsideTouchable(true);
                pop.setBackgroundDrawable(new BitmapDrawable());
                pop.setFocusable(true);
                pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        // TODO Auto-generated method stub
                        WindowManager.LayoutParams lp = LSBaseActivity.activity
                                .getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        LSBaseActivity.activity.getWindow().setAttributes(lp);
                        if ( callBack != null )
                        {
                            callBack.handler(null);
                        }
                    }
                });

                WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
                        .getAttributes();
                lp.alpha = 0.7f;
                LSBaseActivity.activity.getWindow().setAttributes(lp);


            }
        });
    }
  /**
    *全部活动筛选
    *
    * */
    public static PopupWindow showActiveAllTimes ( int position, View parent, final CallBack callBack )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.active_all_times_chose, null);

        View bg = v.findViewById(R.id.bg);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final ArrayList<HashMap<String, String>> alist = PopWindowData.getActiveTime();

        currentMap = alist.get(0);

        if ( position != 0 )
        {
            setMap(alist.get(position), currentMap);
        }

        final ActiveAllTimesAdapter adapter = new ActiveAllTimesAdapter(LSBaseActivity.activity, alist);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callBack != null) {
                    HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(i);
                    setMap(map, currentMap);
                    String value = alist.get(i).get("value");
                    MyTask task = new MyTask();
                    task.setresult(value);
                    task.setResultModel(i);
                    callBack.handler(task);
                    closePop();
                }
            }
        });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
                Common.HEIGHT - Common.dip2px(50));

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
//        pop.showAsDropDown(parent);
//        pop.showAsDropDown(parent, 0, Common.dip2px(3));
        pop.showAtLocation(parent, Gravity.BOTTOM, 0, Common.dip2px(50));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//                WindowManager.LayoutParams lp = LSBaseActivity.activity
//                        .getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                LSBaseActivity.activity.getWindow().setAttributes(lp);
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

//        WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
//                .getAttributes();
//        lp.alpha = 0.2f;
//        LSBaseActivity.activity.getWindow().setAttributes(lp);

        return pop;


    }
    private static HashMap<String, String> currentMapClub = new HashMap<String, String>();
    private static HashMap<String, String> currentMap = new HashMap<String, String>();
    private static HashMap<String, String> currentMapCity = new HashMap<String, String>();
    private static HashMap<String, String> currentActiveCity = new HashMap<String, String>();
//  当前选择的城市
    private static ActiveMainCityListModel.CitylistEntity currentCity;

//   设置选择城市
    private static void setCurrentCity ( ActiveMainCityListModel.CitylistEntity select )
    {
        if ( currentCity == select ) return;
//        第一次current为空
        if ( currentCity == null )
        {
            currentCity = select;
            currentCity.select = "1";
        }
        else
        {
            currentCity.select = "0";
            currentCity = select;
            currentCity.select = "1";
        }
    }


    private static void setMap ( HashMap<String, String> map, HashMap<String, String> cmap )
    {
        if ( map == cmap ) return;
        cmap.put("select", "0");
        cmap = map;
        cmap.put("select", "1");
    }

    static class ActiveAllTimesAdapter extends MyBaseAdapter
    {

        public ActiveAllTimesAdapter(Context c, ArrayList listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {
            Holder holder = null;
            if ( view == null )
            {
                view = View.inflate(mContext, R.layout.active_all_times_chose_item, null);
                holder = new Holder();
                holder.tv_all = (TextView) view.findViewById(R.id.tv_all);
                holder.tv_select = (ImageView) view.findViewById(R.id.tv_select);
                holder.iv_line = view.findViewById(iv_line);

                view.setTag(holder);
            }
            else {
                holder = (Holder) view.getTag();
            }

            HashMap<String, String> map = (HashMap<String, String>) getItem(i);
            if ( map == null ) return view;

            if ( "1".equals(map.get("select")) )
            {
                holder.tv_select.setVisibility(View.VISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.text_color_green));
            }
            else
            {
                holder.tv_select.setVisibility(View.INVISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.color_six));
            }

            if ( i == getCount() - 1 )
            {
                holder.iv_line.setVisibility(View.GONE);
            }
            else
            {
                holder.iv_line.setVisibility(View.VISIBLE);
            }


            holder.tv_all.setText(map.get("name"));

            return view;
        }

        class Holder
        {
            TextView tv_all;
            ImageView tv_select;
            View iv_line;
        }

    }


    static class ActiveCityMainList extends MyBaseAdapter
    {

        public ActiveCityMainList(Context c, ArrayList listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {
            Holder holder = null;
            if ( view == null )
            {
                view = View.inflate(mContext, R.layout.active_all_times_chose_item, null);
                holder = new Holder();
                holder.tv_all = (TextView) view.findViewById(R.id.tv_all);
                holder.tv_select = (ImageView) view.findViewById(R.id.tv_select);
                holder.iv_line = view.findViewById(iv_line);

                view.setTag(holder);
            }
            else {
                holder = (Holder) view.getTag();
            }

            ActiveMainCityListModel.CitylistEntity item = (ActiveMainCityListModel.CitylistEntity) getItem(i);
            if ( item == null ) return view;

            if ( "1".equals(item.select) )
            {
                holder.tv_select.setVisibility(View.VISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.text_color_green));
            }
            else
            {
                holder.tv_select.setVisibility(View.INVISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.color_six));
            }

            if ( i == getCount() - 1 )
            {
                holder.iv_line.setVisibility(View.GONE);
            }
            else
            {
                holder.iv_line.setVisibility(View.VISIBLE);
            }


            holder.tv_all.setText(item.name);

            return view;
        }

        class Holder
        {
            TextView tv_all;
            ImageView tv_select;
            View iv_line;
        }

    }

//  线路活动筛选， 根据CityId获取cityName
    public static String[] getCityNameWithId ( String cityId )
    {
        String[] name = new String[]{"全部归属地", "0"};
        int i = 0;
        final ArrayList<HashMap<String, String>> alist = PopWindowData.getActiveCityList();
        for ( HashMap<String, String> map : alist )
        {
            if ( cityId.equals(map.get("value")))
            {
                name[1] = ""+i;
                name[0] = map.get("name");
                return name;
            }
            i++;
        }

        return name;
    }

    //  线路活动筛选， 根据CityId获取cityName
    public static String[] getMainCityNameWithId ( String cityId )
    {
        String[] name = new String[]{"北京", "2"};
        int i = 0;
        final ArrayList<HashMap<String, String>> alist = PopWindowData.getActiveMainCityList();
        for ( HashMap<String, String> map : alist )
        {
            if ( cityId.equals(map.get("value")))
            {
                name[1] = ""+i;
                name[0] = map.get("name");
                return name;
            }
            i++;
        }

        return name;
    }


    /**
     *      选择系列活动报名列表
     * */
    public static PopupWindow showActiveSeriesLine (int position, View parent, TopicSeriesBatchsListModel modelBatch, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.pop_active_series_line, null);

        View bg = v.findViewById(R.id.bg);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);

        layout.getLayoutParams().height = Common.HEIGHT - Common.dip2px(200);

        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final PopListAdapter.TopicActiveSeriesLineAdapter adapter = new PopListAdapter
                .TopicActiveSeriesLineAdapter(LSBaseActivity.activity, modelBatch.batchList);

        list.setAdapter(adapter);

//        如果只有一条， 并且是可以报名， 默认选中
        if ( position == -1 && modelBatch.batchList.size() == 1 && modelBatch.batchList.get(0).isEnd != 1 && modelBatch.batchList.get(0).isBaoming != 1 )
        {
            adapter.setPosition(0);
        }
        else
        {
            adapter.setPosition(position);
        }



        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    MyTask task = new MyTask();
                    task.setresult("" + adapter.getPosition());
//                task.setResultModel(values);
                    callBack.handler(task);
                    closePop();
                }

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    TopicSeriesBatchsListModel.BatchListEntity item = (TopicSeriesBatchsListModel
                            .BatchListEntity) adapter.getItem(i);
                    if ( item.isEnd == 1 || item.isBaoming == 1 ) return;

                    adapter.setPosition(i);
            }
        });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//                WindowManager.LayoutParams lp = LSBaseActivity.activity
//                        .getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                LSBaseActivity.activity.getWindow().setAttributes(lp);
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

//        WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
//                .getAttributes();
//        lp.alpha = 0.7f;
//        LSBaseActivity.activity.getWindow().setAttributes(lp);

        return pop;
    }

    /**
     *      选择系列活动管理报名列表
     * */
    public static PopupWindow showActiveSeriesLineManager (int position, View parent, ManagerSeriesLineListModel model, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.pop_active_series_line_manager, null);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);

        layout.getLayoutParams().height = Common.HEIGHT - Common.dip2px(200);

        View bg = v.findViewById(R.id.bg);

        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final PopListAdapter.TopicActiveSeriesLineManagerAdapter adapter = new PopListAdapter
                .TopicActiveSeriesLineManagerAdapter(LSBaseActivity.activity, model.batchList);

        list.setAdapter(adapter);

        adapter.setPosition(position);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callBack != null) {
                    MyTask task = new MyTask();
                    task.setresult(""+adapter.getPosition());
                    callBack.handler(task);
                    closePop();
                }

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setPosition(i);
            }
        });

//        pop = new PopupWindow(v, ViewGroup.LayoutParams.FILL_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);

        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
//                WindowManager.LayoutParams lp = LSBaseActivity.activity
//                        .getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                LSBaseActivity.activity.getWindow().setAttributes(lp);
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

//        WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
//                .getAttributes();
//        lp.alpha = 0.7f;
//        LSBaseActivity.activity.getWindow().setAttributes(lp);

        return pop;
    }

    public static PopupWindow showNearbyActiveTime (int position, View parent, final CallBack callBack  )
    {
        return showNearbyActive(position, parent, NativeEntityUtil.getInstance().getNearbyActiveTime(), callBack);
    }

    public static PopupWindow showNearbyActivePrice (int position, View parent, final CallBack callBack  )
    {
        return showNearbyActive(position, parent, NativeEntityUtil.getInstance().getNearbyActivePrice(), callBack);
    }

    private static PopupWindow showNearbyActive (int position, View parent, ArrayList<HashMap<String, String>> ilist, final CallBack callBack  )
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.pop_window_nearby_active_time, null);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);

//        layout.getLayoutParams().height = Common.HEIGHT - Common.dip2px(200);

        View bg = v.findViewById(R.id.bg);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });

        final ListView list = (ListView) v.findViewById(R.id.list);

        final PopListAdapter.NearByActiveTime adapter = new PopListAdapter.NearByActiveTime(LSBaseActivity.activity, ilist);

        list.setAdapter(adapter);

        adapter.setposition(position);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ( callBack != null )
                {
                    MyTask myTask = new MyTask();
                    myTask.setresult(""+position);
                    callBack.handler(myTask);
                }
                closePop();
            }
        });

        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
//        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.showAsDropDown(parent);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

        return pop;
    }


    public static PopupWindow showNearbyFilter(List<Integer> types, View parent,
                                               NearbyFilterModel model, final CallBack callBack)
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            return pop;
        }

        View v = View.inflate(LSBaseActivity.activity, R.layout.nearby_filter_main, null);

        final ListView list = (ListView) v.findViewById(R.id.exList);

        Button btnReset = (Button) v.findViewById(R.id.btn_reset);
        Button btnOk = (Button) v.findViewById(R.id.btn_ok);

        final PopListAdapter.NearbyFilter adapter = new PopListAdapter.NearbyFilter(LSBaseActivity.activity, model.sievenlist);

        list.setAdapter(adapter);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( adapter != null )
                {
                    adapter.reSetSelect();
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePop();
                if ( adapter == null || callBack == null ) return;
                MyTask myTask = new MyTask();
                myTask.setResultModel(adapter.getSelect());
                callBack.handler(myTask);

            }
        });


        pop = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
//        pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        pop.showAsDropDown(parent);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                if ( callBack != null )
                {
                    callBack.handler(null);
                }
            }
        });

        return pop;
    }





    public static void closePop ()
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

}
