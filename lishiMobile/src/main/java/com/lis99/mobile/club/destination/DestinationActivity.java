package com.lis99.mobile.club.destination;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.service.RetrofitCall;
import com.lis99.mobile.service.ServiceManager;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.Page;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      目的地详情
 */
public class DestinationActivity extends LSBaseActivity implements DestinationHeaderView.OnTabSelectListener, PullToRefreshView.OnFooterRefreshListener, PullToRefreshView.OnHeaderRefreshListener {

    @Bind(R.id.pull_refresh_view)
    PullToRefreshView pull_refresh_view;
    @Bind(R.id.listView)
    ListView listView;

    DestinationHeaderView headerView;

    DestinationNoteAdapter noteAdapter;
    DestinationEventAdapter eventAdapter;

    int type = 1;

    Call<DestinationEventListModel> eventCall;
    Call<DestinationNoteListModel> noteCall;

    private Page page;
    private Page notePage;
    int destID;
    int tagID;

    Destination destination;

    float HeadAdHeight = 1;

    //ListView 第一个可见item
    private int visibleFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        destID = getIntent().getIntExtra("destID", 0);
        tagID = getIntent().getIntExtra("tagID", 0);
        setContentView(R.layout.activity_destination);
        initViews();
        ButterKnife.bind(this);
        setTitle("");
        headerView = new DestinationHeaderView(this);
        headerView.setOnTabSelectListener(this);
        listView.addHeaderView(headerView);
        eventAdapter = new DestinationEventAdapter(this, null);
        noteAdapter = new DestinationNoteAdapter(this, null);
        listView.setAdapter(eventAdapter);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        listView.setOnScrollListener(listScroll);


        setTitleBarAlpha(0f);
        setLeftView(R.drawable.ls_club_back_icon_bg);

        ViewTreeObserver vto1 = headerView.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                HeadAdHeight = headerView.coverView.getHeight();

                ViewTreeObserver obs = headerView.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int category = 0;
                int topic_id = 0;
                if (type == 1) {
                    DestinationEvent item = eventAdapter.getItem(position - 1);
                    category = item.category;
                    topic_id = item.topic_id;
                } else {
                    DestinationNote item = noteAdapter.getItem(position - 1);
                    category = item.category;
                    topic_id = item.topic_id;
                }


                if (category == 0 || category == 1) {
                    Intent intent = new Intent(DestinationActivity.this, LSClubTopicActivity
                            .class);
                    intent.putExtra("topicID", topic_id);
                    startActivity(intent);
                } else if (category == 2) {
                    Intent intent = new Intent(activity, LSClubTopicNewActivity.class);
                    intent.putExtra("topicID", topic_id);
                    startActivity(intent);
                } else if (category == 3) {
                    Intent intent = new Intent(activity, LSClubNewTopicListMain.class);
                    intent.putExtra("TOPICID", topic_id + "");
                    startActivity(intent);
                } else if (category == 4) {
                    Common.goTopic(activity, 4, topic_id);
                }
            }
        });

        refresh();
    }

    private void setBack(boolean isBg) {
        if (isBg) {
            setLeftView(R.drawable.ls_club_back_icon_bg);
        } else {
            setLeftView(R.drawable.ls_page_back_icon);
        }
    }

    /**
     * 设置标题栏透明度
     *
     * @param num
     */
    private void setTitleAlpha(float num) {
        if (num >= HeadAdHeight) {
            num = HeadAdHeight;
            setBack(false);

        } else if (num <= 0) {
            num = 0;
        }
        if (num < HeadAdHeight && num >= 0) {
            setBack(true);
        }
        float alpha = num / HeadAdHeight;
        setTitleBarAlpha(alpha);
    }

    AbsListView.OnScrollListener listScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            visibleFirst = firstVisibleItem;
            View v = listView.getChildAt(0);
            if (v == null) return;
            float alpha = v.getTop();
            if (visibleFirst > 0) {
                setTitleAlpha(HeadAdHeight);
            } else {
                setTitleAlpha(-alpha);
            }
        }
    };

    private void refresh() {
        page = new Page();
        notePage = new Page();
        eventAdapter.clear();
        noteAdapter.clear();
        getDestinationDetail();
        loadList();
        ;
    }

    private void loadList() {
        if (type == 1) {
            listView.setAdapter(eventAdapter);
            if (eventAdapter == null || eventAdapter.getCount() == 0) {
                headerView.noContentView.setVisibility(View.VISIBLE);
                getEventList();
            } else {
                headerView.noContentView.setVisibility(View.GONE);
            }
        } else {
            listView.setAdapter(noteAdapter);
            if (noteAdapter == null || noteAdapter.getCount() == 0) {
                headerView.noContentView.setVisibility(View.VISIBLE);
                getNoteList();
            } else {
                headerView.noContentView.setVisibility(View.GONE);
            }
        }
    }

    private void getDestinationDetail() {
        DestinationService service = ServiceManager.getHttpsApiService(DestinationService.class);
        Call<Destination> call = service.getDestinationDetail(destID);
//        call.enqueue(new Callback<Destination>() {
//            @Override
//            public void onResponse(Call<Destination> call, Response<Destination> response) {
//                destination = response.body();
//                if (destination != null) {
//                    headerView.setDestination(destination);
//                    setTitle(destination.title);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Destination> call, Throwable t) {
//
//            }
//        });


        call.enqueue(new RetrofitCall<Destination>() {

            @Override
            public void myResponse(Call<Destination> call, Response<Destination> response) {
                destination = response.body();
                if (destination != null) {
                    headerView.setDestination(destination);
                    setTitle(destination.title);
                }
            }

            @Override
            public void myFailure(Call<Destination> call, Throwable t) {
                
            }


        });

    }

    private void getEventList() {
        if (page.isLastPage()) {
            pull_refresh_view.onHeaderRefreshComplete();
            pull_refresh_view.onFooterRefreshComplete();
            return;
        }
        if (eventCall != null && !eventCall.isCanceled()) {
            eventCall.cancel();
        }
        DestinationService service = ServiceManager.getHttpsApiService(DestinationService.class);
        eventCall = service.getDestinationEventList(page.pageNo, destID, tagID);
        eventCall.enqueue(new Callback<DestinationEventListModel>() {
            @Override
            public void onResponse(Call<DestinationEventListModel> call, Response<DestinationEventListModel> response) {
                pull_refresh_view.onHeaderRefreshComplete();
                pull_refresh_view.onFooterRefreshComplete();
                DestinationEventListModel eventList = response.body();
                page.nextPage();
                page.setPageSize(eventList.pageTotal);
                if (eventList.pageTotal == 0) {
                    headerView.noContentView.setVisibility(View.VISIBLE);
                } else {
                    headerView.noContentView.setVisibility(View.GONE);
                }
                eventAdapter.appendData(eventList.activityList);
            }

            @Override
            public void onFailure(Call<DestinationEventListModel> call, Throwable t) {
                pull_refresh_view.onHeaderRefreshComplete();
                pull_refresh_view.onFooterRefreshComplete();

                if (eventAdapter.getCount() == 0) {
                    headerView.noContentView.setVisibility(View.VISIBLE);
                } else {
                    headerView.noContentView.setVisibility(View.GONE);
                }

            }
        });

    }

    private void getNoteList() {
        if (notePage.isLastPage()) {
            pull_refresh_view.onHeaderRefreshComplete();
            pull_refresh_view.onFooterRefreshComplete();
            return;
        }
        if (noteCall != null && !noteCall.isCanceled()) {
            noteCall.cancel();
        }
        DestinationService service = ServiceManager.getHttpsApiService(DestinationService.class);
        noteCall = service.getDestinationNoteList(notePage.pageNo, destID, tagID);
        noteCall.enqueue(new Callback<DestinationNoteListModel>() {
            @Override
            public void onResponse(Call<DestinationNoteListModel> call, Response<DestinationNoteListModel> response) {
                pull_refresh_view.onHeaderRefreshComplete();
                pull_refresh_view.onFooterRefreshComplete();
                DestinationNoteListModel eventList = response.body();
                notePage.nextPage();
                notePage.setPageSize(eventList.pageTotal);
                if (eventList.pageTotal == 0) {
                    headerView.noContentView.setVisibility(View.VISIBLE);
                } else {
                    headerView.noContentView.setVisibility(View.GONE);
                }
                noteAdapter.appendData(eventList.topiclist);
            }

            @Override
            public void onFailure(Call<DestinationNoteListModel> call, Throwable t) {
                pull_refresh_view.onHeaderRefreshComplete();
                pull_refresh_view.onFooterRefreshComplete();
                if (noteAdapter.getCount() == 0) {
                    headerView.noContentView.setVisibility(View.VISIBLE);
                } else {
                    headerView.noContentView.setVisibility(View.GONE);
                }
            }
        });

    }


    private void setNoContent(boolean value) {
        if (value) {
            headerView.noContentView.setVisibility(View.VISIBLE);
        } else {
            headerView.noContentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabSelected(int tabIndex) {
        type = tabIndex + 1;
        loadList();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (type == 1) {
            getEventList();
        } else {
            getNoteList();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refresh();
    }
}
