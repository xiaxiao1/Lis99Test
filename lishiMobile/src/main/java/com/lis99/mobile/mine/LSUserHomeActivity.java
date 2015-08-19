package com.lis99.mobile.mine;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.mine.adapter.LSUserHomeAdapter;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LSUserHomeActivity extends LSBaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    ListView listView;
    LSUserHomeAdapter adapter;

    String userID;
    View headerView, view_line_head;
    RoundedImageView headerImageView;
    TextView nameView;

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


    private Page page;

    private View headViewMain;
    //ListView 第一个可见item
    private int visibleFirst;


    private UserBean user;

    private void buildOptions() {
        options = ImageUtil.getImageOptionClubIcon();
    }

    private PullToRefreshView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsuser_home);
        initViews();
        userID = getIntent().getStringExtra("userID");
        //setTitle("");

        buildOptions();

        page = new Page();

        loadClubInfo();

    }




    @Override
    protected void initViews() {
        super.initViews();


        setTitleBarAlpha(0f);
        setRightView(R.drawable.club_join);
        titleRightImage.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        headViewMain = View.inflate(activity, R.layout.club_header_view, null);

        headerView = headViewMain.findViewById(R.id.headView);

        view_line_head = headViewMain.findViewById(R.id.view_line_head);

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
            }
        });

        headerImageView = (RoundedImageView)headViewMain.findViewById(R.id.roundedImageView1);

        nameView = (TextView) headViewMain.findViewById(R.id.nameView);



        listView.addHeaderView(headViewMain);

        listView.setOnScrollListener( new AbsListView.OnScrollListener() {

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
        });

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
        headerView.setBackgroundResource(R.drawable.club_0);
        nameView.setText("阿道夫分手");



        if (user.isIs_follows()) {
            setRightView(R.drawable.bg_button_follow);
        } else{
            setRightView(R.drawable.bg_button_followed);
        }

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


            List<LSBaseTopicModel> temp = LSFragment.mapper.readValue(data.get("topiclist").traverse(), new TypeReference<List<LSBaseTopicModel>>() {
            });

            topics.addAll(temp);


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

			adapter = new LSUserHomeAdapter(this, topics);
			listView.setAdapter(adapter);

            return true;
        } else if (msg.what == SHOW_MORE_TOPIC) {
            adapter.notifyDataSetChanged();
            return true;
        } else if (msg.what == NO_MORE_TOPIC){

        } else if (msg.what == SHOW_ADDBUTTON) {
            if (user.isIs_follows()) {
                setRightView(R.drawable.bg_button_follow);
            } else{
                setRightView(R.drawable.bg_button_followed);
            }
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

    private void quitClub(final String userID) {

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

    @Override
    public void onClick(View view) {
       if (view.getId() == R.id.addButton || view.getId() == R.id.titleRightImage ) {

            String userID = DataManager.getInstance().getUser().getUser_id();
            if (TextUtils.isEmpty(userID)) {
                Intent intent = new Intent(this, LSLoginActivity.class);
                startActivity(intent);
                return;
            }

            if (!user.isIs_follows()) {
                joinClub(userID);
            } else {
                quitClub(userID);
            }
            return;
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
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        refreshView.onHeaderRefreshComplete();
//		offset = 0;

        cleanList();
        loadClubInfo();

    }

    private void getHeadAdHeight ()
    {
        //int titleHeight = iv_title_bg.getHeight();
        HeadAdHeight = headerView.getHeight(); //- titleHeight;
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
        }
        else if ( num <= 0 )
        {
            num = 0;
        }
        if ( num < HeadAdHeight && num >= 0 )
        {
            setTitleRight(true);
            setBack(true);
        }
        float alpha = num / HeadAdHeight;
//			iv_title_bg.setAlpha(alpha);
//			title.setAlpha(alpha);
        setTitleBarAlpha(alpha);
    }

    //设置title右边按钮
    private void setTitleRight ( boolean isBg )
    {

    }
    //	设置返回按钮
    private void setBack ( boolean isBg)
    {
        /*
        if ( isBg )
        {
            setLeftView(R.drawable.ls_page_back_icon_bg);
        }
        else
        {
            setLeftView(R.drawable.ls_page_back_icon);
        }
        */
    }


    //下拉刷新用
    public void cleanList ()
    {
        page = new Page();
        adapter = null;
        listView.setAdapter(null);
    }


}
