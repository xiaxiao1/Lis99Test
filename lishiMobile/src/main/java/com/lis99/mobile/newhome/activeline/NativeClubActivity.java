package com.lis99.mobile.newhome.activeline;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;

/**
 * Created by yy on 16/10/11.
 *      本地俱乐部活动
 */
public class NativeClubActivity extends LSBaseActivity implements  PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener{

    private int position = 0;

    private String cityName = "北京", locationCityName = "", locationCityId = "";

    public static double Latitude = -1, Longitude = -1;

    public int cityId = -1;

    private LocationUtil location;

    //    本地活动
    private LSActiveLineNewAdapter adapter;

    private ListView list;

    private ActiveLineNewModel model;

    private Page page;

    private PullToRefreshView pull_refresh_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.native_active_activity);

        initViews();

        setTitle("本地俱乐部活动");

        page = new Page();
        getLocation();

    }

    @Override
    protected void rightAction() {
        super.rightAction();

        PopWindowUtil.showActiveMainCityList(locationCityName, locationCityId, position, list, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if ( mTask == null )
                {
                    return;
                }
                String[] values = (String[]) mTask.getResultModel();

                cityName = values[0];
                cityId = Common.string2int(values[1]);

//                        tvLocation.setText(cityName);
                setRightView(cityName);

                position = Integer.parseInt(mTask.getresult());

                onHeaderRefresh(pull_refresh_view);
            }
        });

    }

    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        list = (ListView) findViewById(R.id.list);
//
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if ( model == null || model.getActivitylist() == null || model.getActivitylist().size() == 0 || adapter == null )
                {
                    return;
                }


                ActiveLineNewModel.ActivitylistEntity item = (ActiveLineNewModel
                        .ActivitylistEntity) adapter.getItem(i);
                if ( item == null ) return;

                int num = Common.string2int(item.getId());

                Common.goTopic(activity, 4, num);


            }
        });


    }


    public void getLocation ()
    {
//        选择了城市
        if ( cityId != -1 )
        {
            getList(Latitude, Longitude);
            return;
        }

        if (location != null ) return;

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

                getList(latitude, longitude);

                if (location != null)
                    location.stopLocation();
                location = null;
            }
        });
        location.getLocation();
    }

    private void getList (double latitude, double longitude)
    {
        if ( page.isLastPage())
        {
            return;
        }

        String url = "";
        if ( cityId == -1 )
        {
            url = C.NEW_ACTIVE_LINE_MIAN + page.getPageNo() + "/?latitude="+latitude+"&longitude="+longitude;
        }
        else
        {
            url = C.NEW_ACTIVE_LINE_MIAN + page.getPageNo() + "/?city_id="+cityId;
        }

        model = new ActiveLineNewModel();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ActiveLineNewModel) mTask.getResultModel();

                if ( model == null ) return;
//              没有这个省的数据，弹出提示
//                if ( model.getDefault_data() == 1 )
//                {
//                    showNoCityDialog();
//                }

                page.nextPage();

                cityId = model.city_id;
                cityName = model.city_name;

                locationCityId = ""+cityId;
                locationCityName = model.city_name;

                setRightView(model.city_name);

//                tvLocation.setText(model.city_name);
//                tv_club_name.setText(model.city_name+"俱乐部活动");


                if ( adapter == null ) {
                    page.setPageSize(model.getTotalpage());
//                    有数据
                    if ( model.getActivitylist() != null && model.getActivitylist().size() > 0 )
                    {
                        adapter = new LSActiveLineNewAdapter(activity, model.getActivitylist());
                        list.setAdapter(adapter);
                    }
//                    else
//                    {
//                        if ( list.getFooterViewsCount() == 0 )
//                        {
//                            list.addFooterView(foodView);
//                        }
//                    }
                }
                else
                {
                    //                    最后一页
                    adapter.addList(model.getActivitylist());
                }
            }
        });


    }

    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
        getList(Latitude, Longitude);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();

        cleanList();
        getLocation();

    }
}
