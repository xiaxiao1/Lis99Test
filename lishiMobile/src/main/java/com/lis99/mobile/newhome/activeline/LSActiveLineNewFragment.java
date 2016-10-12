package com.lis99.mobile.newhome.activeline;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.DestinationMainActivity;
import com.lis99.mobile.club.filter.FilterMainActivity;
import com.lis99.mobile.club.model.ActiveBannerInfoModel;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ioscitychoose.GridActiveAdapter;
import com.lis99.mobile.club.widget.ioscitychoose.GridPageAdapter;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.newhome.activeline.adapter.ActiveMainRecommendAdapter;
import com.lis99.mobile.newhome.activeline.adapter.MyBaseRecycler;
import com.lis99.mobile.newhome.activeline.adapter.SupperLaderRecycler;
import com.lis99.mobile.newhome.sysmassage.SysMassageActivity;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.NativeEntityUtil;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.RedDotUtil;
import com.lis99.mobile.util.ScrollTopUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/1/14.
 */
public class LSActiveLineNewFragment extends LSFragment implements View.OnClickListener,
        GridPageAdapter.GridPageAdapterListener, GridPageAdapter.GridPageClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener,
        LSSelectAdapter.OnSelectItemClickListener, ScrollTopUtil.ToTop {

    private TextView tvMassage;
    private TextView tvLocation;
    private ListView list;
    private PullToRefreshView pull_refresh_view;

    private BannerView viewBanner;
    private GridPageAdapter gadapter;

    private LocationUtil location;

    private View head;

    private Page page;

    public static double Latitude = -1, Longitude = -1;

    private ActiveLineNewModel model;

//    private LSActiveLineAdapter adapter;

    private List<Object> l = new ArrayList<Object>();

    private View v, foodView;

    private View titleLeft;

    private int position = 0;
    public static int cityId = -1;
    private String cityName = "北京", locationCityName = "", locationCityId = "";


//    Grid信息
    private ArrayList<ActiveBannerInfoModel> gridList;

    private ListView s_list;
    private ActiveMainRecommendAdapter recommendAdapter;
//    超级领队
    private RecyclerView recycler_supper_leader;
    private SupperLaderRecycler supperRecycler;
//    本地活动
    private LSActiveLineNewAdapter adapter;
//
    private ActiveMainHeadModel headModel;

    private View include_search;

//俱乐部活动标题
    private TextView tv_club_name;
    private Button btn_change;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = View.inflate(getActivity(), R.layout.active_new_line, null);
        tvMassage = (TextView)v.findViewById(R.id.tv_massage);
        tvLocation = (TextView)v.findViewById(R.id.tv_location);
        tvLocation.setText("活动");
//        tvLocation.setOnClickListener(this);
        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
//
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView)v.findViewById(R.id.list);

        titleLeft = v.findViewById(R.id.titleLeft);

        titleLeft.setOnClickListener(this);

        tvMassage.setText("");
        tvMassage.setVisibility(View.GONE);


        head = View.inflate(getActivity(), R.layout.active_line_new_head, null);

        tv_club_name = (TextView) head.findViewById(R.id.tv_club_name);

        btn_change = (Button) head.findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);

        foodView = View.inflate(getActivity(),R.layout.active_no_info_food, null );

        recycler_supper_leader = (RecyclerView) head.findViewById(R.id.recycler_supper_leader);
        recycler_supper_leader.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false));
        s_list = (ListView) head.findViewById(R.id.s_list);

        viewBanner = (BannerView) head.findViewById(R.id.viewBanner);
        viewBanner.setDot(R.drawable.active_banner_grid_dot_select, R.drawable.active_banner_grid_nomal_dot);

        include_search = head.findViewById(R.id.include_search);

        include_search.setOnClickListener(this);

        list.addHeaderView(head);
//        list.addFooterView(foodView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if ( model == null || model.getActivitylist() == null || model.getActivitylist().size() == 0 || adapter == null )
                {
                    return;
                }


                ActiveLineNewModel.ActivitylistEntity item = (ActiveLineNewModel
                        .ActivitylistEntity) adapter.getItem(i - 1);
                    if ( item == null ) return;

                    int num = Common.string2int(item.getId());

                Common.goTopic(getActivity(), 4, num);


            }
        });

        redDotUtil.setRedText(tvMassage);

        list.setAdapter(null);

        head.setVisibility(View.GONE);

        //                9宫格
        if ( gridList == null )
        {
            gridList = NativeEntityUtil.getInstance().getActiveBanner();
        }

        gadapter = new GridPageAdapter(getActivity(), gridList.size());
        gadapter.setGridPageClickListener(LSActiveLineNewFragment.this);
        gadapter.addGridPageAdapterListener(LSActiveLineNewFragment.this);
        viewBanner.setBannerAdapter(gadapter);

        return v;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        page = new Page();

        getLocation();

    }

    //  没有当前城市数据， 弹出提示
    private void showNoCityDialog ()
    {
        DialogManager.getInstance().showActiveDialog(getActivity());
    }


    RedDotUtil redDotUtil = RedDotUtil.getInstance();

    private void cleanList ()
    {
//        foodView.setVisibility(View.VISIBLE);
//        list.addFooterView(foodView);
        head.setVisibility(View.GONE);
        page = new Page();
        list.setAdapter(null);
        adapter = null;

        s_list.setAdapter(null);
        recommendAdapter = null;




    }

    private void getHead ()
    {
        if ( recommendAdapter != null )
        {
            getList(Latitude, Longitude);
            return;
        }
        String url = C.ACTIVE_NEW_MAIN_HEAD;
        headModel = new ActiveMainHeadModel();
        MyRequestManager.getInstance().requestGet(url, headModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                headModel = (ActiveMainHeadModel) mTask.getResultModel();

                if ( headModel == null ) return;

                head.setVisibility(View.VISIBLE);

//              推荐活动
                recommendAdapter = new ActiveMainRecommendAdapter(getActivity(), headModel.hotlist);
                s_list.setAdapter(recommendAdapter);
                getList(Latitude, Longitude);

                supperRecycler = new SupperLaderRecycler(headModel.leaderlist, getActivity());

                recycler_supper_leader.setAdapter(supperRecycler);
                supperRecycler.setOnItemClickLitener(new MyBaseRecycler.OnItemClickLitener() {
                    @Override
                    public void onItemClick(int position) {
                        ActiveMainHeadModel.LeaderlistEntity item = headModel.leaderlist.get(position);
                        Common.goUserHomeActivit(getActivity(), ""+item.userId);
                    }
                });

            }
        });

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

//                tvLocation.setText(model.city_name);
//                tv_club_name.setText(model.city_name+"俱乐部活动");


                if ( adapter == null ) {
                    page.setPageSize(model.getTotalpage());
//                    有数据
                    if ( model.getActivitylist() != null && model.getActivitylist().size() > 0 )
                    {

                        if ( list.getFooterViewsCount() != 0 )
                        {
                            list.removeFooterView(foodView);
                        }

                        adapter = new LSActiveLineNewAdapter(getActivity(), model.getActivitylist());
                        list.setAdapter(adapter);
                    }
                    else
                    {
                        if ( list.getFooterViewsCount() == 0 )
                        {
                            list.addFooterView(foodView);
                        }
                    }
                }
                else
                {
                    //                    最后一页
                    adapter.addList(model.getActivitylist());
                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.include_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.titleLeft:
//                RedDotUtil.getInstance().InVisibleDot();
                if ( Common.isLogin(getActivity()) )
                startActivity(new Intent(getActivity(), SysMassageActivity.class));

                break;
            case R.id.tv_location:
            case R.id.btn_change:

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
                        tv_club_name.setText(cityName+"俱乐部活动");

                        position = Integer.parseInt(mTask.getresult());

                        onHeaderRefresh(pull_refresh_view);
                    }
                });

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PopWindowUtil.closePop();
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

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }

    public void getLocation ()
    {
//        选择了城市
        if ( cityId != -1 )
        {
//            getList(Latitude, Longitude);
            getHead();
            return;
        }

        if (location != null ) return;

        DialogManager.getInstance().startWaiting(getActivity(), null, "数据加载中...");
        location = LocationUtil.getinstance();
        location.setGlocation(new LocationUtil.getLocation() {

            @Override
            public void Location(double latitude, double longitude, String cityName) {
                // TODO Auto-generated method stub

                DialogManager.getInstance().stopWaitting();

                location.setGlocation(null);

                Latitude = latitude;
                Longitude = longitude;

//                getList(latitude, longitude);
                getHead();

                if (location != null)
                    location.stopLocation();
                location = null;
            }
        });
        location.getLocation();
    }

//    切换View 会被调用
    @Override
    public void handler() {

        redDotUtil.getRedDot();

        ScrollTopUtil.getInstance().setToTop(new ScrollTopUtil.ToTop() {
            @Override
            public void handler() {
                if ( list == null ) return;
                if ( list.getAdapter() != null )
                {
                    list.setSelection(0);
                }
            }
        });

    }


    @Override
    public void dispalyImage(final GridView gridView, int position) {

        if ( gridList == null )
        {
            gridList = NativeEntityUtil.getInstance().getActiveBanner();
        }

        ArrayList<ActiveBannerInfoModel> item = new ArrayList<>();
        int star = position * 8;

        for ( int i = star; i < gridList.size() && i < star + 8; i++ )
        {
            item.add(gridList.get(i));
        }


        final GridActiveAdapter gridadapter = new GridActiveAdapter(getActivity(), item);
        gridView.setAdapter(gridadapter);
        Common.Log_i("cc"+gridView.getChildCount());


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActiveBannerInfoModel item = (ActiveBannerInfoModel) gridadapter.getItem(position);

//                目的地
                if ( item.id == -1 )
                {
                    Intent intent = new Intent(getActivity(), DestinationMainActivity.class);
                    startActivity(intent);
                }
//                附近的活动
                else if ( item.id == -2 )
                {
//                    FilterMainActivity
                    Intent intent = new Intent(getActivity(), FilterMainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), FilterMainActivity.class);
                    intent.putExtra("TITLE", item.name);
                    intent.putExtra("TAGID", item.id);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onClick(int index) {

    }
}
