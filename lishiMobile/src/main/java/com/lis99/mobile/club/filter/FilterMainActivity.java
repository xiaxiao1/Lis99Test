package com.lis99.mobile.club.filter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.filter.model.NearbyFilterModel;
import com.lis99.mobile.club.filter.model.NearbyListMainModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.NativeEntityUtil;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by yy on 16/7/13.
 */
public class FilterMainActivity extends LSBaseActivity implements PullToRefreshView.OnFooterRefreshListener, PullToRefreshView.OnHeaderRefreshListener{

    private View line;
    private LinearLayout layoutTabData;
    private TextView tvData;
    private ImageView ivData;
    private LinearLayout layoutTabCity;
    private TextView tvCity;
    private ImageView ivCity;
    private LinearLayout layoutTabType;
    private TextView tvType;
    private ImageView ivType;
    private PullToRefreshView pullRefreshView;
    private ListView list;
//  排序下标
    private int dataPosition = 0, pricePosition = -1;
    private Page page;

    private FilterAdapter adapter;
    private LocationUtil location;
    public static double Latitude = -1, Longitude = -1;

    private NearbyListMainModel listModel;
//    type	tinyint	筛选类型：1附近活动筛选，2活动筛选	REST
    private int activeType = 1;
    private int tagId;
    private NearbyFilterModel filterModel;
    private List<Integer> types;
//  筛选的Map      id.   3运动类型, 7 费用，
    private HashMap<String, String> filterMap;

    private static HashMap<String, String> modelMap;
    //        id.   3运动类型, 7 费用，8难度， 9999 距离，
    static {
        modelMap = new HashMap<>();

        modelMap.put("3", "typetags");
        modelMap.put("7", "pricetags");
        modelMap.put("8", "difftags");
        modelMap.put("9999", "rangetags");
//        modelMap.put("3", "");
//        modelMap.put("3", "");
//        modelMap.put("3", "");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_nearby_main);

        initViews();

        setTitle("附近的活动");

//        onHeaderRefresh(pullRefreshView);

        tagId = getIntent().getIntExtra("TAGID", -1);
//        附近的活动
        if ( tagId == -1 )
        {
            activeType = 1;
        }
//        筛选
        else
        {
            activeType = 2;
        }

        dataPosition = 0;
        pricePosition = -1;

        selectTabUp(tvData, ivData);

        getLocation();

    }



    @Override
    protected void initViews() {
        super.initViews();

        line = findViewById(R.id.line);
        layoutTabData = (LinearLayout) findViewById(R.id.layout_tab_data);
        tvData = (TextView) findViewById(R.id.tv_data);
        ivData = (ImageView) findViewById(R.id.iv_data);
        layoutTabCity = (LinearLayout) findViewById(R.id.layout_tab_city);
        tvCity = (TextView) findViewById(R.id.tv_city);
        ivCity = (ImageView) findViewById(R.id.iv_city);
        layoutTabType = (LinearLayout) findViewById(R.id.layout_tab_type);
        tvType = (TextView) findViewById(R.id.tv_type);
        ivType = (ImageView) findViewById(R.id.iv_type);
        pullRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        list = (ListView) findViewById(R.id.list);

        pullRefreshView.setOnHeaderRefreshListener(this);
        pullRefreshView.setOnFooterRefreshListener(this);


        layoutTabData.setOnClickListener(this);
        layoutTabCity.setOnClickListener(this);
        layoutTabType.setOnClickListener(this);


    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch ( arg0.getId())
        {
            case R.id.layout_tab_data:
                PopWindowUtil.showNearbyActiveTime(dataPosition, line, dataCallBack);
                break;
            case R.id.layout_tab_city:
                PopWindowUtil.showNearbyActivePrice(pricePosition, line, priceCallBack);
                break;
            case R.id.layout_tab_type:
                selectFilter(tvType, ivType);
                getFilterInfo();
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterModel = null;
    }

    private void getFilterInfo ()
    {
        if ( filterModel != null && filterModel.sievenlist != null )
        {
            PopWindowUtil.showNearbyFilter(types, line, filterModel, filetCallBack);
            return;
        }
//        PopWindowUtil.showNearbyFilter();
        String url = C.NEARBY_FILTER + activeType;
        filterModel = new NearbyFilterModel();
        MyRequestManager.getInstance().requestGet(url, filterModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                filterModel = (NearbyFilterModel) mTask.getResultModel();
                if ( filterModel == null || filterModel.sievenlist == null ) return;
                        PopWindowUtil.showNearbyFilter(types, line, filterModel, filetCallBack);
            }
        });





    }

    private CallBack dataCallBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
//            unSelectTab(tvData, ivData);

            if ( mTask != null )
            {
                unSelectTab(tvCity, ivCity);
                pricePosition = -1;
                dataPosition = Common.string2int(mTask.getresult());
                if ( dataPosition == 0 )
                {
                    selectTabUp(tvData, ivData);
                }
                else
                {
                    selectTabDown(tvData, ivData);
                }
                onHeaderRefresh(pullRefreshView);
            }
        }
    };

    private CallBack priceCallBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
//            unSelectTab(tvCity, ivCity);

            if ( mTask != null )
            {
                unSelectTab(tvData, ivData);
                dataPosition = -1;
                pricePosition = Common.string2int(mTask.getresult());
                if ( pricePosition == 0 )
                {
                    selectTabUp(tvCity, ivCity);
                }
                else
                {
                    selectTabDown(tvCity, ivCity);
                }
                onHeaderRefresh(pullRefreshView);
            }

        }
    };

    private CallBack filetCallBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            unSelectFilter(tvType, ivType);
            if ( mTask == null )
            {
                return;
            }
            filterMap = (HashMap<String, String>) mTask.getResultModel();
            onHeaderRefresh(pullRefreshView);
        }
    };

    private void getLocation ()
    {
        DialogManager.getInstance().startWaiting(activity, null, "数据加载中...");
        location = LocationUtil.getinstance();
        location.setGlocation(new LocationUtil.getLocation() {

            @Override
            public void Location(double latitude, double longitude, String cityName) {
                // TODO Auto-generated method stub

                DialogManager.getInstance().stopWaitting();

                location.setGlocation(null);

                Latitude = latitude;
                Longitude = longitude;

//                getList();
                onHeaderRefresh(pullRefreshView);

                if (location != null)
                    location.stopLocation();
                location = null;
            }
        });
        location.getLocation();
    }

    private HashMap<String, Object> getFilterMap (HashMap<String, Object> map )
    {
        //
        if ( filterMap == null ) return map;
        Set<String> set = filterMap.keySet();
        for ( String str : set )
        {
            if ( modelMap.containsKey(str))
            {
                map.put(modelMap.get(str), filterMap.get(str));
            }
        }
        return map;
    }


    private void getList ()
    {
        if ( page.isLastPage() )
        {
            return;
        }
        String url = C.NEARBY_MAIN_LIST;
//        附近的活动
        if ( activeType == 1 )
        {
            url = C.NEARBY_MAIN_LIST+page.getPageNo();
        }
//        筛选
        else if ( activeType == 2 )
        {
            url = C.NEARBY_MAIN_LIST_FILTER+page.getPageNo();
        }

        HashMap<String, Object> map = new HashMap<>();
        if ( tagId != -1 )
        {
            map.put("tag_id", tagId );
        }
        map.put("latitude", Latitude);
        map.put("longitude", Longitude);
        if ( dataPosition != -1 )
        {
            map.put("ordertime", NativeEntityUtil.getInstance().getNearbyActiveTime().get(dataPosition).get("id"));
        }
        if ( pricePosition != -1 )
        {
            map.put("orderprice", NativeEntityUtil.getInstance().getNearbyActivePrice().get(pricePosition).get("id"));
        }

        map = getFilterMap(map);

        listModel = new NearbyListMainModel();

        MyRequestManager.getInstance().requestPost(url, map, listModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                listModel = (NearbyListMainModel) mTask.getResultModel();
                if ( listModel == null || listModel.lists == null ) return;
                page.nextPage();
                if ( adapter == null )
                {
                    page.setPageSize(listModel.totalpage);
                    adapter = new FilterAdapter(activity, listModel.lists);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if ( adapter == null ) return;
                            NearbyListMainModel.ListsEntity item = (NearbyListMainModel
                                    .ListsEntity) adapter.getItem(position);

                            Common.goTopic(activity, 4, Common.string2int(item.id));

                        }
                    });
                }
                else
                {
                    adapter.addList(listModel.lists);
                }
            }
        });



    }

    private void cleanList ()
    {
        page = new Page();
        if ( list != null )
        list.setAdapter(null);
        adapter = null;
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pullRefreshView.onFooterRefreshComplete();
        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pullRefreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }


    private void selectTabDown ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_green));
        iv.setImageResource(R.drawable.nearby_sort_select_down);
    }

    private void selectTabUp ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_green));
        iv.setImageResource(R.drawable.nearby_select_up);
    }

    private void unSelectTab ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_black));
        iv.setImageResource(R.drawable.nearby_sort_nomal);
    }



    private void selectFilter ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_green));
        iv.setImageResource(R.drawable.nearby_filter_select);
    }

    private void unSelectFilter ( TextView tv, ImageView iv )
    {
        tv.setTextColor(getResources().getColor(R.color.text_color_black));
        iv.setImageResource(R.drawable.nearby_filter_nomal);
    }


}
