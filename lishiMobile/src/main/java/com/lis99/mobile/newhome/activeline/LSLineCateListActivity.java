package com.lis99.mobile.newhome.activeline;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

public class LSLineCateListActivity extends LSBaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {

    ListView listView;
    LSLineCateListAdapter adapter;
    private PullToRefreshView refreshView;
    private Page page;
    private int cateId;
    private String cateName;

    private int cityId = -1;
    private double latitude = -1, longitude = -1;

    private int sortByTime = 1;
    private int sortByPrice = -1;

    LSLineCateListModel listModel;

    Button maskButton;
    View sortPanel;

    TextView selectedSortView;
    TextView sortTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = new Page();
        setContentView(R.layout.activity_lsline_cate_list);
        cityId = getIntent().getIntExtra("cityId", -1);
        latitude = getIntent().getDoubleExtra("latitude", -1);
        longitude = getIntent().getDoubleExtra("longitude", -1);
        cateId = getIntent().getIntExtra("cateId", -1);
        cateName = getIntent().getStringExtra("cateName");
        initViews();
        setTitle(cateName);
        getActiveList();
    }

    @Override
    protected void initViews() {
        super.initViews();
        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                LSLineCateListModel.ActivityItem item = null;
                item = (LSLineCateListModel.ActivityItem) adapter.getItem(position);
                if (item == null) return;

                if (!TextUtils.isEmpty(item.activity_code)) {
//                    Intent intent = new Intent(LSLineCateListActivity.this, LSClubTopicActiveOffLine.class);
//                    intent.putExtra("topicID", item.id);
//                    startActivity(intent);

                    Common.goTopic(activity, 4, item.id);

                }

            }
        });


        titleRight.setOnClickListener(this);

        maskButton = (Button) findViewById(R.id.maskView);
        maskButton.setOnClickListener(this);

        sortPanel = findViewById(R.id.sortPanel);

        TextView sortView = (TextView) findViewById(R.id.priceDescView);
        sortView.setOnClickListener(this);

        sortView = (TextView) findViewById(R.id.priceAscView);
        sortView.setOnClickListener(this);

        sortView = (TextView) findViewById(R.id.timeDescView);
        sortView.setOnClickListener(this);


        sortView = (TextView) findViewById(R.id.timeAscView);
        selectedSortView = sortView;
        sortView.setOnClickListener(this);

        sortTypeView = (TextView) findViewById(R.id.sortTypeView);
    }

    public void getActiveList ()
    {
        if ( page.pageNo >= page.pageSize )
        {
            Common.toast("没有更多帖子");
            return;
        }
        String urlStr = C.LINE_CATE_PAGE_HOME + page.getPageNo() + "?prov_id=" + cityId
                                            + "&latitude=" + latitude
                                            + "&longitude=" + longitude
                                            + "&cate_id=" + cateId;
        if (sortByPrice != -1) {
            urlStr += "&orderprice=" + sortByPrice;
        }
        if (sortByTime != -1) {
            urlStr += "&ordertime=" + sortByTime;
        }

        listModel = new LSLineCateListModel();

        MyRequestManager.getInstance().requestGet(urlStr, listModel, new CallBack() {

            @Override
            public void handler(MyTask mTask) {
                // TODO Auto-generated method stub
                page.pageNo += 1;
                listModel = (LSLineCateListModel) mTask.getResultModel();
                if (listModel.activitylist == null) {
                    return;
                }
                if (adapter == null) {
                    page.pageSize = listModel.totalpage;
                    adapter = new LSLineCateListAdapter(LSLineCateListActivity.this, listModel.activitylist);

                    listView.setAdapter(adapter);
                    return;
                }
                adapter.addList(listModel.activitylist);
            }
        });
    }

    public void cleanList ()
    {
        page = new Page();
        adapter = null;
        listView.setAdapter(null);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
        getActiveList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();

        cleanList();
        getActiveList();
    }


    private void showAlbum(){
        titleRight.setEnabled(false);
        maskButton.setVisibility(View.VISIBLE);
        sortPanel.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        maskButton.startAnimation(animation);
        sortPanel.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                titleRight.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void hideAlbum(){
        titleRight.setEnabled(false);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(300);
        maskButton.startAnimation(animation);
        sortPanel.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                maskButton.setVisibility(View.INVISIBLE);
                sortPanel.setVisibility(View.INVISIBLE);
                titleRight.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleRight:
                if (sortPanel.getVisibility() == View.VISIBLE) {
                    hideAlbum();
                    return;
                }
                showAlbum();
                break;

            case R.id.maskView:
                hideAlbum();
                break;

            case R.id.priceAscView:
            case R.id.priceDescView:
            case R.id.timeAscView:
            case R.id.timeDescView:
                setSelectedSortView((TextView)v);
                break;
        }

    }

    private void setSelectedSortView(TextView sortView) {
        if (sortView == selectedSortView) {
            return;
        }
        hideAlbum();
        sortView.setTextColor(getResources().getColor(R.color.text_color_green));
        Drawable drawable= getResources().getDrawable(R.drawable.icon_line_sort_selected);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        sortView.setCompoundDrawables(null, null, drawable, null);

        selectedSortView.setTextColor(Color.rgb(0x52, 0x52, 0x52));
        selectedSortView.setCompoundDrawables(null, null, null, null);

        selectedSortView = sortView;

        sortTypeView.setText(selectedSortView.getText());

        switch (sortView.getId()) {

            case R.id.priceDescView:
                sortByPrice = 2;
                sortByTime = -1;
                break;
            case R.id.priceAscView:
                sortByPrice = 1;
                sortByTime = -1;
                break;
            case R.id.timeDescView:
                sortByPrice = -1;
                sortByTime = 2;
                break;
            case R.id.timeAscView:
                sortByPrice = -1;
                sortByTime = 1;
                break;

        }

        cleanList();
        getActiveList();

    }

}
