package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.ActiveAllCity;
import com.lis99.mobile.club.model.ActiveAllModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by yy on 15/7/27.
 */
public class ActiveAllActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private LinearLayout layout_tab_data, layout_tab_city;
    private TextView tv_data, tv_city;
    private ImageView iv_data, iv_city;
    private PullToRefreshView pull_refresh_view;
    private ListView list, list_city;
    private Page page;

    private ActiveAllAdapter adapter;

    private ActiveAllCityAdapter cityAdapter;

    private ActiveAllModel activeAllModel;

    private ActiveAllCity cityModel;
//城市列表
    private static ArrayList<HashMap<String, String>> cityMap;

    private HashMap<String, String> currentMap;

    private String times = "0", cityId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_all);


        initViews();
        setTitle("全部活动");

        list_city.setVisibility(View.GONE);


        page = new Page();

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        layout_tab_data = (LinearLayout) findViewById(R.id.layout_tab_data);
        layout_tab_city = (LinearLayout) findViewById(R.id.layout_tab_city);

        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_city = (TextView) findViewById(R.id.tv_city);

        iv_data = (ImageView) findViewById(R.id.iv_data);
        iv_city = (ImageView) findViewById(R.id.iv_city);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);
        list = (ListView) findViewById(R.id.list);
        list_city = (ListView) findViewById(R.id.list_city);

        layout_tab_data.setOnClickListener(this);
        layout_tab_city.setOnClickListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null || adapter.getTopicId(i) == -1 ) return;
                Intent intent = new Intent(activity, LSClubTopicActivity.class);
                intent.putExtra("topicID", adapter.getTopicId(i));
                startActivity(intent);
            }
        });

        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) cityAdapter.getItem(i);
                setCurrentMap(map);
                cityId = map.get("id");
//                Common.log("cityId="+cityId);
                onHeaderRefresh(pull_refresh_view);
            }
        });

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch ( arg0.getId())
        {
            case R.id.layout_tab_data:
                if ( cityListShow() )
                {
                    getCityList();
                    return;
                }
                selectTab(tv_data, iv_data);
                PopWindowUtil.showActiveAllTimes(layout_tab_data, dataCallBack);
                break;
            case R.id.layout_tab_city:
                getCityList();
                break;
        }
    }

    private void cleanList ()
    {
        page = new Page();
        if ( adapter != null )
        {
            adapter.clean();
            adapter = null;
        }
    }

    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        String url = C.ACTIVE_ALL + page.pageNo;
        activeAllModel = new ActiveAllModel();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("type", times);
        map.put("cityid", cityId);

        MyRequestManager.getInstance().requestPost(url, map, activeAllModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                activeAllModel = (ActiveAllModel) mTask.getResultModel();
                page.nextPage();
                if (adapter == null) {
                    page.setPageSize(activeAllModel.totalpage);
                    adapter = new ActiveAllAdapter(activity, activeAllModel.clubtopiclist);
//                    list.setAdapter(adapter);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(list);
                    list.setAdapter(animationAdapter);


                } else {
                    adapter.addList(activeAllModel.clubtopiclist);
                }

            }
        });




    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    CallBack dataCallBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            unSelectTab(tv_data, iv_data);
            if ( mTask == null )
            {
                return;
            }
            times = mTask.getresult();
            onHeaderRefresh(pull_refresh_view);
//            Common.log("data = " + times);
        }
    };

    private void getCityList ()
    {
        if ( cityMap != null && cityMap.size() != 0 )
        {
            setCityAdapter();
            return;
        }
        String url = C.ACTIVE_ALL_CITY;
        cityModel = new ActiveAllCity();
        MyRequestManager.getInstance().requestGet(url, cityModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                cityModel = (ActiveAllCity) mTask.getResultModel();
                initCity(cityModel);
                setCityAdapter();
            }
        });
    }

    private void setCityAdapter ()
    {
        if ( cityAdapter == null )
        {
            cityAdapter = new ActiveAllCityAdapter(activity, cityMap);
            list_city.setAdapter(cityAdapter);
        }

        if ( cityListShow() )
        {
            unSelectTab(tv_city, iv_city);
            list_city.setVisibility(View.GONE);
        }
        else
        {
            selectTab(tv_city, iv_city);
            list_city.setVisibility(View.VISIBLE);
        }

    }

    private void setCurrentMap ( HashMap<String, String> map )
    {
        if ( currentMap == map ) return;
        currentMap.put("select", "0");
        currentMap = map;
        currentMap.put("select", "1");
        if ( cityAdapter != null )
        {
            cityAdapter.notifyDataSetChanged();
        }
        setCityAdapter();

    }

    private boolean cityListShow ()
    {
        return list_city.getVisibility() == View.VISIBLE;
    }

    //设置城市数据
    private void initCity ( ActiveAllCity model )
    {
        if ( cityMap != null ) cityMap.clear();
        collectionsList(model.citylist);
        cityMap = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> all = new HashMap<String, String>();
        all.put("name", "全部");
        all.put("id", "0");
        all.put("select", "1");
        cityMap.add(all);

        currentMap = all;

        all = new HashMap<String, String>();
        all.put("name", "热门集合地");
        all.put("type", "title");
        cityMap.add(all);

        for ( int i = 0; model.hotcity != null && model.hotcity.size() != 0 && i < model.hotcity.size(); i++ )
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", model.hotcity.get(i).name);
            map.put("id", ""+model.hotcity.get(i).id);
            all.put("select", "0");
            cityMap.add(map);
        }

        for ( int i = 0; model.citylist != null && model.citylist.size() != 0 && i < model.citylist.size(); i++ )
        {
            String pinyin = getFirstCode(model.citylist.get(i).pinyin);
            if ( !TextUtils.isEmpty(pinyin))
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", pinyin);
                map.put("type", "title");
                cityMap.add(map);
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", model.citylist.get(i).name);
            map.put("id", ""+model.citylist.get(i).id);
            map.put("pinyin", model.citylist.get(i).pinyin);
            all.put("select", "0");
            cityMap.add(map);
        }
    }

    private String currentStr = "";
    //获取第一个字母
    private String getFirstCode ( String str )
    {
        if ( TextUtils.isEmpty(str)) return "";
        String temp = str.substring(0, 1).toUpperCase();
        if ( !currentStr.equals(temp) )
        {
            currentStr = temp;
            return currentStr;
        }
        return "";
    }

    private void collectionsList ( ArrayList<ActiveAllCity.Citylist> list )
    {
        Collections.sort(list, new Comparator<ActiveAllCity.Citylist>() {
            @Override
            public int compare(ActiveAllCity.Citylist citylist, ActiveAllCity.Citylist t1) {
                String city1 = citylist.pinyin.toUpperCase();
                String city2 = t1.pinyin.toUpperCase();
                return city1.compareTo(city2);
            }
        });
    }

    private void selectTab ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_blue));
        iv.setImageResource(R.drawable.active_all_dot_default);
    }

    private void unSelectTab ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.black));
        iv.setImageResource(R.drawable.active_all_dot_select);
    }

}
