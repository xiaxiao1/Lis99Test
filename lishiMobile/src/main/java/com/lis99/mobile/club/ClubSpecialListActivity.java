package com.lis99.mobile.club;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.ClubSpecialListItemAdapter;
import com.lis99.mobile.club.model.ClubSpecialListModel;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by yy on 15/8/6.
 *
 *  tagid = getIntent().getIntExtra("tagid", -1);
 */
public class ClubSpecialListActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView pull_refresh_view;

    private ListView listView;

    private Page page;

    private View head;

    private ClubSpecialListItemAdapter adapter;

    private ClubSpecialListModel model;

    private int tagid;

    private ImageView iv_bg, iv_load;

    private TextView tv_title;

    private float HeadAdHeight = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_club_special_list);

        initViews();

        setLeftView(R.drawable.ls_club_back_icon_bg);

        page = new Page();

        tagid = getIntent().getIntExtra("tagid", -1);

        getList();

    }

    @Override
    protected void initViews() {
        super.initViews();

        listView = (ListView) findViewById(R.id.listView);

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        head = View.inflate(activity, R.layout.ls_club_special_item, null);

        ViewTreeObserver vto1 = head.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                getHeadAdHeight();

                ViewTreeObserver obs = head.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });

        iv_bg = (ImageView) head.findViewById(R.id.iv_bg);
        iv_load = (ImageView) head.findViewById(R.id.iv_load);
        tv_title = (TextView) head.findViewById(R.id.tv_title);


        listView.addHeaderView(head);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                View v = listView.getChildAt(0);
                if (v == null) return;
                float alpha = v.getTop();
                if (firstVisibleItem > 0) {
                    setTitleAlpha(HeadAdHeight);
                } else {
                    setTitleAlpha(-alpha);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( adapter == null ) return;
                if ( i == 0 ) return;
                ClubSpecialListModel.Topiclist item = (ClubSpecialListModel.Topiclist) adapter.getItem(i - 1);
                if ( item == null ) return;

                if ( !TextUtils.isEmpty(item.activity_code))
                {
                    Intent intent = new Intent(activity, LSClubTopicActiveOffLine.class);
                    intent.putExtra("topicID", item.topic_id);
                    startActivity(intent);
                    return;
                }

                Intent intent = new Intent(activity, LSClubTopicActivity.class);
                intent.putExtra("topicID", item.topic_id);
                startActivity(intent);
            }
        });

    }

    private void cleanList() {
        page = new Page();
        listView.setAdapter(null);
        adapter = null;
    }

    private void getList()
    {
        if ( page.isLastPage() || -1 == tagid )
        {
            return;
        }

        model = new ClubSpecialListModel();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("tagid", tagid);

        String url = C.CLBU_SPECIAL_LIST_INFO + page.pageNo;

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (ClubSpecialListModel) mTask.getResultModel();
                page.nextPage();

                if (adapter == null) {
                    page.setPageSize(model.totalpage);

                    if (!TextUtils.isEmpty(model.taginfo.tag_images)) {
                        ImageLoader.getInstance().displayImage(model.taginfo.tag_images, iv_bg, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(iv_load, iv_bg));
                    }
                    tv_title.setText(model.taginfo.name);
                    setTitle(model.taginfo.name);

                    adapter = new ClubSpecialListItemAdapter(activity, model.topiclist);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(listView);
                    listView.setAdapter(animationAdapter);

                } else {
                    adapter.addList(model.topiclist);

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


    /**
     * 设置标题栏透明度
     * @param num
     */
    private void setTitleAlpha ( float num )
    {
        if ( num >= HeadAdHeight )
        {
            num = HeadAdHeight;
            setBack(false);
        }
        else if ( num <= 0 )
        {
            num = 0;
        }
        if ( num < HeadAdHeight && num >= 0 )
        {
            setBack(true);
        }
        float alpha = num / HeadAdHeight;
        setTitleBarAlpha(alpha);
    }

    //	设置返回按钮
    private void setBack ( boolean isBg)
    {
        if ( isBg )
        {
            setLeftView(R.drawable.ls_club_back_icon_bg);
        }
        else
        {
            setLeftView(R.drawable.ls_page_back_icon);
        }
    }

    private void getHeadAdHeight ()
    {
        int titleHeight = iv_title_bg.getHeight();
        HeadAdHeight = head.getHeight() - titleHeight;
    }


}
