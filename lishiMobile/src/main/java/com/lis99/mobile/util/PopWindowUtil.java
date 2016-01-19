package com.lis99.mobile.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lis99.mobile.R.id.iv_line;

/**
 * Created by yy on 15/7/27.
 */
public class PopWindowUtil {

    private static PopupWindow pop;

//    public static PopupWindow showActiveCitys ()
//    {
//
//    }

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

        final ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("select", "1");
        map.put("name", "全部开始日期");
        map.put("value", "0");
        alist.add(map);

        currentMap = map;

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1周内开始");
        map.put("value", "1");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1-2周内开始");
        map.put("value", "2");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "2周-1个月内开始");
        map.put("value", "3");
        alist.add(map);

        map = new HashMap<String, String>();

        map.put("select", "0");
        map.put("name", "1个月以后开始");
        map.put("value", "4");
        alist.add(map);

        if ( position != 0 )
        {
            setMap(alist.get(position));
        }

        final ActiveAllTimesAdapter adapter = new ActiveAllTimesAdapter(LSBaseActivity.activity, alist);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (callBack != null) {
                    HashMap<String, String> map = (HashMap<String, String>) adapter.getItem(i);
                    setMap(map);
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
//    private static int position = 0;
    private static HashMap<String, String> currentMap;
    private static void setMap ( HashMap<String, String> map )
    {
        if ( map == currentMap ) return;
        currentMap.put("select", "0");
        currentMap = map;
        currentMap.put("select", "1");
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
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
            }
            else
            {
                holder.tv_select.setVisibility(View.GONE);
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

    public static void closePop ()
    {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

}
