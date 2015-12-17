package com.lis99.mobile.mine;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.adapter.MyJoinAdapter;
import com.lis99.mobile.club.model.MyJoinClubModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.mine.adapter.LSUserHomeAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *      userID  String
 */
public class LSUserHomeActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    ListView listView;
    LSUserHomeAdapter adapter;

    String userID;
    View headerView;
//    View view_line_head;
    RoundedImageView headerImageView;
    TextView nameView;
    TextView fansView;
    TextView noteView;

    boolean needRefresh = true;
    boolean loginBeforePause = false;

    List<LSBaseTopicModel> topics = new ArrayList<LSBaseTopicModel>();


    int lastScrollY = 0;

    int type = -1;
    int offset = 0;

    private final static int SHOW_CLUB = 1001;
    private final static int SHOW_MORE_TOPIC = 1003;
    private final static int NO_MORE_TOPIC = 1004;
    private final static int SHOW_ADDBUTTON = 1002;



    DisplayImageOptions options;


    private float HeadAdHeight = 1;


    private Page page, pageclub;

    private View headViewMain;
    //ListView 第一个可见item
    private int visibleFirst;

    private UserBean user;


    List<TextView> tagViews = new ArrayList<TextView>();

    //没有动态小人
    private View layout_no_item;
    //    动态数量
    private TextView tv_num_reply;
    //数量
    int replyNum;

    private void buildOptions() {
        options = ImageUtil.getclub_topic_headImageOptions();
    }

    private PullToRefreshView refreshView;

    //====3.6.1=====
    private View allPanel1, allPanel, eventPanel1, eventPanel;
    private TextView allView1, allView, eventView1, eventView;
    private View allLine1, allLine, eventLine1, eventLine;
    private View topPanel1, topPanel, include1;

    private MyJoinClubModel model;

    private Page clubPage;

    private MyJoinAdapter clubAdapter;

    private boolean visible = true;

    private View view_line;

//    3.9.1====
    private View iv_moderator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsuser_home);
        initViews();
        userID = getIntent().getStringExtra("userID");

        buildOptions();

        page = new Page();

        pageclub = new Page();

    }


    @Override
    protected void rightAction() {
        super.rightAction();
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(this, LSLoginActivity.class);
            startActivity(intent);
            return;
        }

        if (!user.isIs_follows()) {
            joinClub(userID);
        } else {
            doQuit(userID);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();


        setTitleBarAlpha(0f);
        setLeftView(R.drawable.ls_club_back_icon_bg);
        setRightView(R.drawable.bg_button_follow);
//        titleRightImage.setOnClickListener(this);



        titleLeftImage.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        headViewMain = View.inflate(activity, R.layout.user_page_top, null);

        headerView = headViewMain.findViewById(R.id.headView);

        view_line = headViewMain.findViewById(R.id.view_line);

        layout_no_item = headViewMain.findViewById(R.id.layout_no_item);

        tv_num_reply = (TextView) headViewMain.findViewById(R.id.tv_num_reply);

        iv_moderator = headViewMain.findViewById(R.id.iv_moderator);


        allPanel1 = headViewMain.findViewById(R.id.allPanel1);
        eventPanel1 = headViewMain.findViewById(R.id.eventPanel1);
        allView1  = (TextView) headViewMain.findViewById(R.id.allView1);
        eventView1  = (TextView) headViewMain.findViewById(R.id.eventView1);
        allLine1  = headViewMain.findViewById(R.id.allLine1);
        eventLine1  = headViewMain.findViewById(R.id.eventLine1);
        topPanel1  = headViewMain.findViewById(R.id.topPanel1);


        allPanel = findViewById(R.id.allPanel);
        eventPanel = findViewById(R.id.eventPanel);
        allView  = (TextView) findViewById(R.id.allView);
        eventView  = (TextView) findViewById(R.id.eventView);
        allLine  = findViewById(R.id.allLine);
        eventLine  =findViewById(R.id.eventLine);
        topPanel  = findViewById(R.id.topPanel);

        include1 = findViewById(R.id.include1);

        topPanel.setVisibility(View.GONE);

        allPanel1.setOnClickListener(this);
        eventPanel1.setOnClickListener(this);
        allPanel.setOnClickListener(this);
        eventPanel.setOnClickListener(this);



        //=====
        if ( !visible )
        {
            topPanel.setVisibility(View.GONE);
            topPanel1.setVisibility(View.GONE);
        }




//        view_line_head = headViewMain.findViewById(R.id.view_line_head);

        ViewTreeObserver vto1 = headerView.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                getHeadAdHeight();

                ViewTreeObserver obs = headerView.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) return;
                if ( listView.getAdapter() == null ) return;
                if ( allLine.getVisibility() == View.VISIBLE )
                {
                    LSBaseTopicModel item = topics.get(position - 1);
                    if (item == null) return;
                    Intent intent = new Intent(LSUserHomeActivity.this, LSClubTopicActivity.class);
                    intent.putExtra("topicID", Integer.parseInt(item.topic_id));
                    startActivity(intent);
                }
                else
                {
                    MyJoinClubModel.Clublist item = (MyJoinClubModel.Clublist) clubAdapter.getItem(position - 1);
                    Intent intent = new Intent(activity, LSClubDetailActivity.class);
                    intent.putExtra("clubID", item.id);
                    startActivity(intent);

                }




            }
        });

        headerImageView = (RoundedImageView)headViewMain.findViewById(R.id.roundedImageView1);

        nameView = (TextView) headViewMain.findViewById(R.id.nameView);
        fansView = (TextView) headViewMain.findViewById(R.id.fansView);
        noteView = (TextView) headViewMain.findViewById(R.id.noteView);

        noteView.setOnClickListener(this);


        TextView tagView = (TextView) headViewMain.findViewById(R.id.tagTextView1);
        tagViews.add(tagView);
        tagView = (TextView) headViewMain.findViewById(R.id.tagTextView2);
        tagViews.add(tagView);
        tagView = (TextView) headViewMain.findViewById(R.id.tagTextView3);
        tagViews.add(tagView);
        tagView = (TextView) headViewMain.findViewById(R.id.tagTextView4);
        tagViews.add(tagView);
        tagView = (TextView) headViewMain.findViewById(R.id.tagTextView5);
        tagViews.add(tagView);
        tagView = (TextView) headViewMain.findViewById(R.id.tagTextView6);
        tagViews.add(tagView);



        listView.addHeaderView(headViewMain);

    }


    private void setTags(List<String> tags)
    {
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        for (int i = 0; i < tagViews.size(); ++i)
        {
            TextView tagView = tagViews.get(i);
            if (tags.size() > i)
            {
                tagView.setVisibility(View.VISIBLE);
                tagView.setText(tags.get(i));
            } else
            {
                tagView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            needRefresh = true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (!loginBeforePause && !TextUtils.isEmpty(userID)) {
            needRefresh = true;
        }
        if (needRefresh) {
            needRefresh = false;
            loadClubInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (!TextUtils.isEmpty(userID)) {
            loginBeforePause = true;
        } else {
            loginBeforePause = false;
        }
    }


    private void loadClubInfo() {
		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
                getString(R.string.sending));
		String url = C.LS_USER_HOME_PAGE + page.pageNo;






        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", userID);
        params.put("uid", this.userID);

        Task task = new Task(null, url, C.HTTP_POST,
                C.LS_USER_HOME_PAGE, this);
        task.setPostData(RequestParamUtil.getInstance(this)
                .getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);


    }


    private void configTopView ()
    {
        if (user == null) {
            return;
        }

        if ( Common.isModerator(user.moderator))
        {
            iv_moderator.setVisibility(View.VISIBLE);
        }
        else {
            iv_moderator.setVisibility(View.GONE);
        }

        setTitle(user.getNickname());


        String loginedID = DataManager.getInstance().getUser().getUser_id();

        if (this.userID.equals(loginedID)) {
            titleRight.setVisibility(View.INVISIBLE);
        } else {
            titleRight.setVisibility(View.VISIBLE);
        }

        headerView.setBackgroundResource(R.drawable.club_0);
        nameView.setText(user.getNickname());

        if (user.getNote() == null || user.getNote().length() == 0) {
            noteView.setText("哎吆，简介没写哦...");
        } else {
            noteView.setText(user.getNote());
        }

        fansView.setText(user.getFollows() + "位粉丝");

        setTags(user.getTags());

        if (user.isIs_follows()) {
            setRightView(R.drawable.bg_button_followed);
        } else{
            setRightView(R.drawable.bg_button_follow);
        }

        tv_num_reply.setText("Ta的发帖（" + replyNum + "）");

        if ( !TextUtils.isEmpty(user.getHeadicon()))
            ImageLoader.getInstance().displayImage(user.getHeadicon(),
                    headerImageView, options);



    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.LS_USER_HOME_PAGE)) {
					    parserClubInfo(result);
                    } else if (((String) task.getParameter()).equals(C.LS_USER_ADD_FOLLOW)) {
                        parserFollowInfo(result);
                    } else if (((String) task.getParameter()).equals(C.LS_USER_CANCEL_FOLLOW)) {
                        parserUnFollowInfo(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }

    private void parserFollowInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                postMessage(ActivityPattern1.POPUP_TOAST, errCode);
                return;
            }
            postMessage(ActivityPattern1.POPUP_TOAST, "已关注");
            user.setIsFollowed(true);
            user.setFollows(user.getFollows()+1);
            postMessage(SHOW_ADDBUTTON);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }

    private void parserUnFollowInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                postMessage(ActivityPattern1.POPUP_TOAST, errCode);
                return;
            }
            postMessage(ActivityPattern1.POPUP_TOAST, "已取消关注");
            user.setIsFollowed(false);
            user.setFollows(user.getFollows() - 1);
            postMessage(SHOW_ADDBUTTON);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }

    private void loadMore() {

        /*
        String userID = DataManager.getInstance().getUser().getUser_id();

        postMessage(ActivityPattern1.POPUP_PROGRESS,
                getString(R.string.sending));
        String url = C.CLUB_GET_INFO + "?club_id=" + clubID + "&category=" + type + "&offset=" + offset;
        if (userID != null && !"".equals(userID)) {
            url += "&user_id=" + userID;
        }

        Task task = new Task(null, url, null, "moreTopic", this);
        publishTask(task, IEvent.IO);
        */

    }

	private void parserClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");

            if (data.has("userinfo")) {
                UserBean u =  LSFragment.mapper.readValue(data.get("userinfo").traverse(), UserBean.class);
                this.user = u;
            }

            page.pageSize = data.get("totalpage").asInt();
            page.pageNo++;

            replyNum = data.get("total").asInt();

            List<LSBaseTopicModel> temp = LSFragment.mapper.readValue(data.get("topiclist").traverse(), new TypeReference<List<LSBaseTopicModel>>() {
            });

            topics.addAll(temp);

            if ( adapter == null )
            {
                if ( topics.size() == 0 )
                {
                    //没有数据
                    layout_no_item.setVisibility(View.VISIBLE);
                }
                else {
                    layout_no_item.setVisibility(View.GONE);
                }
            }


			postMessage(SHOW_CLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}




//	private void parserMoreClubInfo(String result) {
//		try {
//			JsonNode root = LSFragment.mapper.readTree(result);
//			String errCode = root.get("status").asText("");
//			if (!"OK".equals(errCode)) {
//				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
//				return;
//			}
//			JsonNode data = root.get("data");
//			List<LSClubTopic> topics = LSFragment.mapper.readValue(data.get("topiclist").traverse(), new TypeReference<List<LSClubTopic>>() {
//			});
//			if (topics != null && topics.size() > 0) {
//				offset++;
//				club.addTopics(topics);
//				postMessage(SHOW_MORE_TOPIC);
//			} else {
//				postMessage(POPUP_TOAST, "没有更多帖子");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			postMessage(ActivityPattern1.DISMISS_PROGRESS);
//		}
//	}

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == SHOW_CLUB) {
            configTopView();

            if (adapter == null) {
                adapter = new LSUserHomeAdapter(this, topics);
                listView.setAdapter(adapter);
                listView.setOnScrollListener( listScroll );
            } else {
                adapter.setData(topics);
            }


            return true;
        } else if (msg.what == SHOW_MORE_TOPIC) {
            adapter.notifyDataSetChanged();
            return true;
        } else if (msg.what == NO_MORE_TOPIC){

        } else if (msg.what == SHOW_ADDBUTTON) {
            fansView.setText(user.getFollows()+"位粉丝");
            //设置按钮
            setTitleRight(isRightBg);
//            if (user.isIs_follows()) {
//              setRightView(R.drawable.bg_button_followed);
//            } else{
//                setRightView(R.drawable.bg_button_follow);
//            }
            return true;
        }
        return super.handleMessage(msg);
    }

    private void joinClub(String userID){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("touid", this.userID);
        params.put("fromuid", userID);

        Task task = new Task(null, C.LS_USER_ADD_FOLLOW, C.HTTP_POST, C.LS_USER_ADD_FOLLOW, this);
        task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);
    }

    private void quitClub2(final String userID) {

        postMessage(POPUP_ALERT, null, "确定要退出俱乐部吗？", true, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doQuit(userID);
                    }
                }, true, "取消", null);

    }

    private void doQuit(String userID){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("touid", this.userID);
        params.put("fromuid", userID);

        Task task = new Task(null, C.LS_USER_CANCEL_FOLLOW, C.HTTP_POST,
                C.LS_USER_CANCEL_FOLLOW, this);
        task.setPostData(RequestParamUtil.getInstance(this)
                .getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.titleLeftImage) {
            finish();
            return;
        }
        else if (view.getId() == R.id.noteView) {

            if (noteView.getMaxLines() < 1000) {
                noteView.setMaxLines(1000);
            } else {
                noteView.setMaxLines(2);
            }

            return;
        }
//       else if (view.getId() == R.id.addButton || view.getId() == R.id.titleRightImage ) {
//
//            String userID = DataManager.getInstance().getUser().getUser_id();
//            if (TextUtils.isEmpty(userID)) {
//                Intent intent = new Intent(this, LSLoginActivity.class);
//                startActivity(intent);
//                return;
//            }
//
//            if (!user.isIs_follows()) {
//                joinClub(userID);
//            } else {
//                doQuit(userID);
//            }
//            return;
//        }
        else if ( view.getId() == R.id.allPanel1 || view.getId() == R.id.allPanel )
        {
            view_line.setVisibility(View.VISIBLE);
            allView.setTextColor(getResources().getColor(R.color.text_color_blue));
            allView1.setTextColor(getResources().getColor(R.color.text_color_blue));
            eventView.setTextColor(getResources().getColor(R.color.color_six));
            eventView1.setTextColor(getResources().getColor(R.color.color_six));
            allLine.setVisibility(View.VISIBLE);
            allLine1.setVisibility(View.VISIBLE);
            eventLine1.setVisibility(View.GONE);
            eventLine.setVisibility(View.GONE);

            listView.setAdapter(adapter);

            if ( topics.size() == 0 )
            {
                //没有数据
                layout_no_item.setVisibility(View.VISIBLE);
            }
            else {
                layout_no_item.setVisibility(View.GONE);
            }


        }
        else if ( view.getId() == R.id.eventPanel || view.getId() == R.id.eventPanel1 )
        {
            view_line.setVisibility(View.GONE);
            eventView.setTextColor(getResources().getColor(R.color.text_color_blue));
            eventView1.setTextColor(getResources().getColor(R.color.text_color_blue));
            allView.setTextColor(getResources().getColor(R.color.color_six));
            allView1.setTextColor(getResources().getColor(R.color.color_six));
            allLine.setVisibility(View.GONE);
            allLine1.setVisibility(View.GONE);
            eventLine1.setVisibility(View.VISIBLE);
            eventLine.setVisibility(View.VISIBLE);

            if ( clubAdapter == null )
            {
                listView.setAdapter(null);
                getclublist();
            }
            else {

                if ( model.clublist == null || model.clublist.size() == 0 )
                {
                    //没有数据
                    layout_no_item.setVisibility(View.VISIBLE);
                }
                else
                {
                    layout_no_item.setVisibility(View.GONE);
                }

                listView.setAdapter(clubAdapter);
            }

        }
        super.onClick(view);
    }

    private void refresh()
    {
        if ( page.pageNo >= page.pageSize )
        {
            Common.toast("没有更多帖子");
            return;
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        refreshView.onFooterRefreshComplete();
        if ( allLine.getVisibility() == View.VISIBLE )
        {
            if ( page.pageNo >= page.pageSize )
            {
                Common.toast("没有更多帖子");
                return;
            }
            loadClubInfo();
        }
        else
        {
            getclublist();
        }

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
//		offset = 0;
        if ( allLine.getVisibility() == View.VISIBLE )
        {
            cleanList();
            loadClubInfo();
        }
        else
        {
            cleanclublist();
            getclublist();
        }

    }

    private void getHeadAdHeight ()
    {
        if ( !visible )
        {
            HeadAdHeight = headerView.getHeight();
            return;
        }
        int titleHeight = include1.getHeight();
        HeadAdHeight = topPanel1.getTop() - titleHeight;
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
            setTitleRight(false);
            setBack(false);
            if ( visible )
            {
                visibleTab (true);
            }
        }
        else if ( num <= 0 )
        {
            num = 0;
        }
        if ( num < HeadAdHeight && num >= 0 )
        {
            setTitleRight(true);
            setBack(true);
            visibleTab(false);
        }
        float alpha = num / HeadAdHeight;
//			iv_title_bg.setAlpha(alpha);
//        title.setAlpha(alpha);
        setTitleBarAlpha(alpha);
    }
// 展示固定tab
    private void visibleTab ( boolean b )
    {
        if ( b )
        {
            topPanel.setVisibility(View.VISIBLE);
        }
        else
        {
            topPanel.setVisibility(View.GONE);
        }
    }

    private boolean isRightBg;
    //设置title右边按钮
    private void setTitleRight ( boolean isBg )
    {
        isRightBg = isBg;
        if ( isBg )
        {
            if (user.isIs_follows()) {
                setRightView(R.drawable.bg_button_followed);
            } else{
                setRightView(R.drawable.bg_button_follow);
            }
        }
        else
        {
            if (user.isIs_follows()) {
                setRightView(R.drawable.bg_button_followed_none);
            } else{
                setRightView(R.drawable.bg_button_follow_none);
            }
        }
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


    //下拉刷新用
    public void cleanList ()
    {
        page = new Page();
        adapter = null;
        topics.clear();
        listView.setAdapter(null);
    }

    AbsListView.OnScrollListener listScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
        int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            visibleFirst = firstVisibleItem;
            View v = listView.getChildAt(0);
            if ( v == null ) return;
            float alpha = v.getTop();
            if ( visibleFirst > 0 )
            {
                setTitleAlpha(HeadAdHeight);
            }
            else
            {
                setTitleAlpha(-alpha);
            }
        }
    };

    private void getclublist ()
    {
        if ( pageclub.isLastPage())
        {
            return;
        }

        String url = C.MY_JOIN_CLUB_LIST + pageclub.getPageNo();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userID);

        model = new MyJoinClubModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (MyJoinClubModel) mTask.getResultModel();

                pageclub.nextPage();

                if ( clubAdapter == null )
                {
                    pageclub.setPageSize(model.totalpage);

                    if ( model.clublist == null || model.clublist.size() == 0 )
                    {
                        //没有数据
                        layout_no_item.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        layout_no_item.setVisibility(View.GONE);
                    }

                    clubAdapter = new MyJoinAdapter(activity, model.clublist);
//                    clubAdapter.setVisibleLine(false);
                    listView.setAdapter(clubAdapter);
                }
                else
                {
                    clubAdapter.addList(model.clublist);
                }

            }
        });

    }

    private void cleanclublist ()
    {
        pageclub = new Page();
        listView.setAdapter(null);
        clubAdapter = null;
    }

}
