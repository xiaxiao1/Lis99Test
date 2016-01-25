package com.lis99.mobile.newhome.activeline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
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
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.ScrollTopUtil;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = View.inflate(getActivity(), R.layout.active_line, null);

        tvMassage = (TextView)v.findViewById(R.id.tv_massage);
        tvLocation = (TextView)v.findViewById(R.id.tv_location);
        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
//
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView)v.findViewById(R.id.list);

        tvMassage.setOnClickListener(this);
        tvLocation.setOnClickListener(this);

        head = View.inflate(getActivity(), R.layout.active_line_head, null);

        viewBanner = (BannerView) head.findViewById(R.id.viewBanner);

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


                Object o = adapter.getItem(i);
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

        return v;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        page = new Page();

        getLocation();

    }



    private void cleanList ()
    {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
        l = new ArrayList<Object>();
    }


    private void getList (double latitude, double longitude)
    {
        if ( latitude == -1 ) return;

        if ( page.isLastPage())
        {
            return;
        }

        String url = C.NEW_ACTIVE_LINE_MIAN + page.getPageNo() + "/?latitude="+latitude+"&longitude="+longitude;

        model = new ActiveLineNewModel();

        MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ActiveLineNewModel) mTask.getResultModel();

                if ( model == null ) return;

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

                    adapter = new LSActiveLineAdapter(getActivity(), l);
                    list.setAdapter(adapter);

                }
                else
                {
                    adapter.addList(l);
                }



            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_massage:

                break;
            case R.id.tv_location:

                break;
        }
    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

    }

    @Override
    public void onClick(int index) {

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
        if (location != null ) return;

        cleanList();

        DialogManager.getInstance().startWaiting(getActivity(), null, "数据加载中...");
        location = LocationUtil.getinstance();
        location.setGlocation(new LocationUtil.getLocation() {

            @Override
            public void Location(double latitude, double longitude) {
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

                getList(latitude, longitude);

                if (location != null)
                    location.stopLocation();
                location = null;
            }
        });
        location.getLocation();
    }


    @Override
    public void handler() {

        ScrollTopUtil.getInstance().setToTop(new ScrollTopUtil.ToTop() {
            @Override
            public void handler() {
                if ( list.getAdapter() != null )
                {
                    list.setSelection(0);
                }
            }
        });

    }



}
