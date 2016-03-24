package com.lis99.mobile.newhome.activeline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.newhome.sysmassage.SysMassageActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.RedDotUtil;
import com.lis99.mobile.util.ScrollTopUtil;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/1/14.
 */
public class LSActiveLineFragment extends LSFragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener, ScrollTopUtil.ToTop {

    private TextView tvMassage;
    private TextView tvLocation;
    private ListView list;
    private PullToRefreshView pull_refresh_view;

    private BannerView viewBanner;
    private ImagePageAdapter bannerAdapter;

    private LocationUtil location;

    private View head;

    private Page page;

    private double Latitude = -1, Longitude = -1;

    private ActiveLineNewModel model;

    private LSActiveLineAdapter adapter;

    private List<Object> l = new ArrayList<Object>();

    private View v;

    private View titleLeft, titleRight;

    private int position;
    public static int cityId = -1;
    private String cityName = "北京", locationCityName = "", locationCityId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = View.inflate(getActivity(), R.layout.active_line, null);

        titleRight = v.findViewById(R.id.titleRight);

        tvMassage = (TextView)v.findViewById(R.id.tv_massage);
        tvLocation = (TextView)v.findViewById(R.id.tv_location);
        tvLocation.setOnClickListener(this);
        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
//
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView)v.findViewById(R.id.list);

        titleLeft = v.findViewById(R.id.titleLeft);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);

        tvMassage.setText("");
        tvMassage.setVisibility(View.GONE);


        head = View.inflate(getActivity(), R.layout.active_line_head, null);

        viewBanner = (BannerView) head.findViewById(R.id.viewBanner);

//        viewBanner.setVisibility(View.GONE);

        viewBanner.mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewBanner.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        viewBanner.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        viewBanner.startAutoScroll();
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        list.addHeaderView(head);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( model == null || model.getActivitylist() == null || model.getActivitylist().size() == 0 || adapter == null )
                {
                    return;
                }


                Object o = adapter.getItem(i - 1);
                if ( o instanceof  ActiveLineNewModel.ActivitylistEntity )
                {
                    ActiveLineNewModel.ActivitylistEntity item = (ActiveLineNewModel.ActivitylistEntity) o;

                    Intent intent = new Intent(getActivity(), LSClubTopicActiveOffLine.class);
//                    activity_id = getIntent().getIntExtra("topicID", 0);
                    int num = Common.string2int(item.getId());
                    intent.putExtra("topicID", num);
                    startActivity(intent);


                }
            }
        });

        redDotUtil.setRedText(tvMassage);

        list.setAdapter(null);

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
        page = new Page();
        list.setAdapter(null);
        adapter = null;
        l = new ArrayList<Object>();
    }


    private void getList (double latitude, double longitude)
    {
//        if ( latitude == -1 ) return;

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
                if ( model.getDefault_data() == 1 )
                {
                    showNoCityDialog();
                }

                page.nextPage();

                cityId = model.city_id;

                String[] name = PopWindowUtil.getMainCityNameWithId(""+cityId);

                if (TextUtils.isEmpty(locationCityId))
                {
                    locationCityId = ""+cityId;
                    locationCityName = name[0];
                }

                tvLocation.setText(name[0]);
//                position = Common.string2int(name[1]);

                for ( ActiveLineNewModel.ActivitylistEntity item : model.getActivitylist())
                {
                    l.add(item);
                }

                if ( adapter == null ) {
                    page.setPageSize(model.getTotalpage());
                    if (l.size() != 0)
                    {
                        if ( l.size() >=3 )
                            l.add(2, model.getAreaweblist());
                        else
                            l.add(model.getAreaweblist());
                    }
//                    最后一页
                    if ( page.isLastPage() )
                    {
                        l.add("last");
                    }

                    adapter = new LSActiveLineAdapter(getActivity(), l);
                    list.setAdapter(adapter);

                    if ( viewBanner.getVisibility() == View.VISIBLE && model.adlist != null && model.adlist.size() != 0 )
                    {
                        int num = model.adlist.size() - 1;
                        for ( int i = num; i >= 0; i-- )
                        {
                            ActiveLineNewModel.Adlist item = model.adlist.get(i);
                            if ( !"1".equals(item.platform))
                            {
                                model.adlist.remove(i);
                                continue;
                            }
                        }
                        bannerAdapter = new ImagePageAdapter(getActivity(), model.adlist.size());
                        bannerAdapter.addImagePageAdapterListener(LSActiveLineFragment.this);
                        bannerAdapter.setImagePageClickListener(LSActiveLineFragment.this);
                        viewBanner.setBannerAdapter(bannerAdapter);
                        viewBanner.startAutoScroll();
                    }


                }
                else
                {
                    //                    最后一页
                    if ( page.isLastPage() )
                    {
                        l.add("last");
                    }
//                    adapter.addList(l);
                    adapter.setList(l);
                }



            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.titleLeft:
//                RedDotUtil.getInstance().InVisibleDot();
                startActivity(new Intent(getActivity(), SysMassageActivity.class));

                break;
            case R.id.tv_location:

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

                        tvLocation.setText(cityName);

                        position = Integer.parseInt(mTask.getresult());

                        onHeaderRefresh(pull_refresh_view);
                    }
                });

                break;
            case R.id.titleRight:
                Intent intent = new Intent(getActivity(), LSAllLineCateActivity.class);
                intent.putExtra("cityId", cityId);
                intent.putExtra("latitude", Latitude);
                intent.putExtra("longitude", Longitude);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PopWindowUtil.closePop();
    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

        if ( model == null || model.adlist == null || model.adlist.size() <= position ) return;
        ActiveLineNewModel.Adlist adlist = model.adlist.get(position);
        ImageLoader.getInstance().displayImage(adlist.images, banner, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(iv_load, banner));

    }

    @Override
    public void onClick(int index) {

        if ( model == null || model.adlist == null || model.adlist.size() <= index ) return;

        Intent intent = null;

        ActiveLineNewModel.Adlist item = model.adlist.get(index);

        switch (item.type) {
//            话题
//            线下贴
            case 0:
            case 1:
                intent = new Intent(getActivity(), LSClubTopicActivity.class);
                intent.putExtra("topicID", Common.string2int(item.url));
                startActivity(intent);
                break;
//            线上贴
            case 2:
                intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
                intent.putExtra("topicID", Common.string2int(item.url));
                startActivity(intent);
                break;
//            URL
            case 3:
                intent = new Intent(getActivity(), MyActivityWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", item.url);
                bundle.putString("TITLE", item.title);
                bundle.putString("IMAGE_URL", item.images);
                bundle.putInt("TOPIC_ID", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
//            俱乐部
            case 4:
                intent = new Intent(getActivity(), LSClubDetailActivity.class);
                intent.putExtra("clubID", Common.string2int(item.url));
                startActivity(intent);

                break;
            case 5:
                intent = new Intent(getActivity(), LSClubTopicActiveOffLine.class);
                intent.putExtra("topicID", Common.string2int(item.url));
                startActivity(intent);
                break;
        }

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
            getList(Latitude, Longitude);
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

//                if ( latitude == 0 )
//                {
//                    return;
//                }
//                getClubHomePageList(latitude, longitude);

                Latitude = latitude;
                Longitude = longitude;

//                LSActiveLineFragment.this.cityName = cityName;
//                tvLocation.setText(cityName);

                getList(latitude, longitude);

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



}
