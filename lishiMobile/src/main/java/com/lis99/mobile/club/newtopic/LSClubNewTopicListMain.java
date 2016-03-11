package com.lis99.mobile.club.newtopic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.adapter.ClubNewTopicListItem;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.model.TopicNewListMainModel;
import com.lis99.mobile.club.model.TopicNewListMainModelEquip;
import com.lis99.mobile.club.model.TopicNewListMainModelTitle;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ShareManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/3/8.
 */
public class LSClubNewTopicListMain extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, LSClubTopicImageListener {

    private ClubNewTopicListItem adapter;

    private ListView listView;

    private PullToRefreshView refreshView;

    private RelativeLayout layoutMain;

//    private Page page;

    private ImageView titleRightImage, iv_weichat, iv_friend;

    private String topicId = "1";

    private TopicNewListMainModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_new_topic_list_main);

        initViews();

        onHeaderRefresh(refreshView);

    }

    @Override
    protected void initViews() {
        super.initViews();


        listView = (ListView) findViewById(R.id.listView);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        iv_weichat = (ImageView) findViewById(R.id.iv_weichat);
        iv_friend = (ImageView) findViewById(R.id.iv_friend);

        titleRightImage.setOnClickListener(this);
        iv_weichat.setOnClickListener(this);
        iv_friend.setOnClickListener(this);

    }

    private void getList ()
    {
//        if ( page.isLastPage() )
//        {
//            return;
//        }


        String url = C.CLUB_NEW_TOPIC_LIST_MAIN;// + page.getPageNo();

        HashMap<String, Object> map = new HashMap<>();

        String userId = DataManager.getInstance().getUser().getUser_id();

        map.put("topics_id", topicId);
        map.put("user_id", userId);

        model = new TopicNewListMainModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                Common.log("=============================="+mTask.getresult());

                model = (TopicNewListMainModel) mTask.getResultModel();

                if ( model == null ) return;

//                page.nextPage();


                if (model.topicsreplylist != null && model.topicsreplylist.size() >= 1 )
                {
                    for ( int i = 0; i < model.topicsreplylist.size(); i++ )
                    {
                        model.imgShareUrl = model.topicsdetaillist.get(i).images;
                        if ( !TextUtils.isEmpty(model.imgShareUrl) )
                        {
                            break;
                        }
                    }
                }


                if ( adapter == null )
                {
//                    page.setPageSize(model.totpage);

                    TopicNewListMainModelTitle title = model;

                    ArrayList<Object> mList = new ArrayList<Object>();

                    mList.add(title);

                    mList.addAll(model.topicsdetaillist);

                    TopicNewListMainModelEquip equipModel = model;

                    mList.add(equipModel);
                    mList.addAll(model.topicsreplylist);

                    adapter = new ClubNewTopicListItem(activity, mList);

                    listView.setAdapter(adapter);
                }
//                else
//                {
//                    adapter.addList(model.topicsdetaillist);
//
//                    if ( page.isLastPage() )
//                    {
//                        TopicNewListMainModelEquip equipModel = model;
//
//                        ArrayList<Object> mList = new ArrayList<Object>();
//
//                        mList.add(equipModel);
//                        mList.addAll(model.topicsreplylist);
//
//                        adapter.addList(mList);
//                    }
//                }

            }
        });





    }

    private void cleanList ()
    {
//        page = new Page();
        listView.setAdapter(null);
        adapter = null;
    }

    @Override
    protected void rightAction() {

        ShareManager.getInstance().showPopWindowInShare(model, layoutMain, null);


        super.rightAction();
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.titleRightImage:
                rightAction();
                break;
            case R.id.iv_weichat:

                ShareManager.getInstance().share2Weichat(model, shareFandW);
                break;
            case R.id.iv_friend:

//				ShareManager.getInstance().share2Weichat("" + topicID, imgUrl, clubhead.title, null);
                ShareManager.getInstance().share2Friend(model, shareFandW);
                break;
        }
    }

    @Override
    public void onClickImage(String imageUrl) {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
//        getList();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    private CallBack shareFandW = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            Common.toast("分享成功");
        }
    };


}
