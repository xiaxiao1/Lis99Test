package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.adapter.LSClubTopicImageListener;
import com.lis99.mobile.club.model.ClubTopicNewReplyList;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 16/3/14.
 *      新话题回复列表
 */
public class LSClubNewTopicListMainReply  extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, LSClubTopicImageListener {

    private RelativeLayout layoutMain;
    private RelativeLayout include;
    private TextView tvReply;
    private PullToRefreshView pullRefreshView;
    private ListView listView;


    private Page page;

    private int topicId, clubId;

    private ClubTopicNewReplyList model;

    private LSClubNewTopicReplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_new_topic_list_main_reply);

        topicId = getIntent().getIntExtra("TOPICID", -1);
        clubId = getIntent().getIntExtra("CLUBID", -1 );

        initViews();

        findViewById(R.id.include).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, LSClubTopicNewReply.class);
                intent.putExtra("replyedName", "");
                intent.putExtra("replyedcontent", "");
                intent.putExtra("replyedfloor", "");
                intent.putExtra("replyedId", "");
                intent.putExtra("clubId", "" + clubId);
                intent.putExtra("topicId", "" + topicId);
//                startActivityForResult(intent, 999);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_open_down_up, 0);
            }
        });

        onHeaderRefresh(pullRefreshView);


    }

    @Override
    protected void initViews() {
        super.initViews();

        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        include = (RelativeLayout) findViewById(R.id.include);
        tvReply = (TextView) findViewById(R.id.tv_reply);
        pullRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        listView = (ListView) findViewById(R.id.listView);

        pullRefreshView.setOnHeaderRefreshListener(this);
        pullRefreshView.setOnFooterRefreshListener(this);


    }

    private CallBack callReply = new CallBack()
    {

        @Override
        public void handler(MyTask mTask)
        {
            // TODO Auto-generated method stub

            model = (ClubTopicNewReplyList) mTask.getResultModel();
            if (model.topicsreplylist == null)
            {
                return;
            }




            page.pageNo += 1;
            if (adapter == null)
            {
                page.pageSize = model.totpage;
                adapter = new LSClubNewTopicReplyAdapter(activity, model.topicsreplylist);
                adapter.setId(topicId, clubId);

//                adapter.lsClubTopicCommentListener = LSClubTopicActivity.this;
                listView.setAdapter(adapter);
//                listView.setOnScrollListener(listScroll);
                return;
            }
            adapter.addList(model.topicsreplylist);

        }
    };


    private void cleanList ()
    {
        page = new Page();
        listView.setAdapter(null);
        if ( adapter != null )
        {
            adapter.clean();
            adapter = null;
        }
    }

    private void getList ()
    {

        if ( page.isLastPage())
        {
            Common.toast("没有更多评论");
            return;
        }

        String url = C.CLUB_NEW_TOPIC_LIST_MAIN_REPLY + page.getPageNo();

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<>();
        map.put("topics_id", topicId);
        map.put("user_id", userId);

        model = new ClubTopicNewReplyList();

        MyRequestManager.getInstance().requestPost(url, map, model, callReply);




    }

    @Override
    public void onClickImage(String imageUrl) {

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
}
