package com.lis99.mobile.club;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubTopicCommentAdapter;
import com.lis99.mobile.club.model.ClubTopicDetailHead;
import com.lis99.mobile.club.model.ClubTopicReplyList;
import com.lis99.mobile.club.model.LSClubTopic;
import com.lis99.mobile.club.widget.ClubActionPanel;
import com.lis99.mobile.club.widget.LSClubTopicHead;
import com.lis99.mobile.club.widget.LSClubTopicHeadActive;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.HandlerList;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.util.ShareManager;

/**
 * 帖子详情
 * 
 * @author yy
 * 
 */
public class LSClubTopicActivity extends LSBaseActivity implements
		OnHeaderRefreshListener, OnFooterRefreshListener
{

	int topicID;
	int clubID;
	String clubName;
	LSClubTopic topic;
	ListView listView;
	LSClubTopicCommentAdapter adapter;
	// 弹出的回复界面
	View replyPanel;

	Bitmap bitmap;

	String replyName;
	int replyID = -1;

	private Button publishButton, replyDelButton, addImageButton;
	View cancelButton;
	private View imagePanel;
	private ImageView replyImageView;
	private EditText replyContentView;
	private TextView replyNameView;

	ClubActionPanel menuWindow;

	Button b;

	int offset = 0;
	private final int pageCount = 30;

	private final static int TOPIC_CHANGE = 1002;
	private final static int DEL_TOPIC_CHANGE = 1003;
	private final static int SHOW_MORE = 1008;

	private PullToRefreshView refreshView;

	// ＝＝＝＝＝＝2.3=＝＝＝＝＝＝＝＝
	private LSClubTopicHead headView;
	private LSClubTopicHeadActive headViewActive;
	// 帖子内容
	private View CurrentHeader;
	private View view_reference;
	// ListView 第一个可见item
	private int visibleFirst;

	private ClubTopicDetailHead clubhead;

	private ClubTopicReplyList clubreply;

	private Page page;

	private RelativeLayout layoutMain;

//	＝＝＝＝3.5.5＝＝＝＝＝＝＝
	private HandlerList likeCall;

	private LinearLayout layout_like;
	private ImageView iv_like;
	private TextView tv_like;
	private Animation animation;

	//分享的图片
//	public Bitmap shareBit;

	//
	// // 删帖
	public void delTopic()
	{
		LSRequestManager.getInstance().mClubTopicReplyDelete("" + clubID,
				"" + topicID, new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra("deleteTopic", "ok");
						setResult(RESULT_OK, intent);
						finish();
					}

				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		likeCall.clean();
		likeCall = null;
	}

	@Override
	public void setIntent(Intent newIntent) {
		super.setIntent(newIntent);
	}

	@Override
	public Context getBaseContext() {
		return super.getBaseContext();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		topicID = getIntent().getIntExtra("topicID", 0);
		// clubID = getIntent().getIntExtra("clubID", 0);
		// clubName = getIntent().getStringExtra("clubName");
		setContentView(R.layout.activity_lsclub_topic);
		initViews();
		setTitle("帖子详情");
		// setTitleRight(true);
		// setBack(true);

		title.setOnClickListener(this);

		clubhead = new ClubTopicDetailHead();
		clubreply = new ClubTopicReplyList();

		addCallLike();

//		animation = AnimationUtils.loadAnimation(this, R.anim.welcome_loading);
		animation = AnimationUtils.loadAnimation(this, R.anim.like_anim);

		page = new Page();
		getTopicHead();
		// loadTopicInfo2(true);


	}

	@SuppressLint("CutPasteId")
	@Override
	protected void initViews()
	{
		super.initViews();
		listView = (ListView) findViewById(R.id.listView);
		layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
		// headView = new LSClubTopicHead(activity);
		// headView.setTopic(this);
		// headView.setClubName(clubName, clubID);
		// listView.addHeaderView(headView);

		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		// adapter = new LSClubTopicCommentAdapter(this, null);
		// listView.setAdapter(adapter);

		replyPanel = findViewById(R.id.include2);

		publishButton = (Button) replyPanel.findViewById(R.id.publishButton);
		publishButton.setOnClickListener(this);
		replyDelButton = (Button) replyPanel.findViewById(R.id.replyDelButton);
		replyDelButton.setOnClickListener(this);
		cancelButton = replyPanel.findViewById(R.id.closeButton);
		cancelButton.setOnClickListener(this);
		addImageButton = (Button) replyPanel.findViewById(R.id.addImage);
		addImageButton.setOnClickListener(this);

		imagePanel = replyPanel.findViewById(R.id.imagePanel);
		replyImageView = (ImageView) replyPanel
				.findViewById(R.id.replyImageView);
		replyNameView = (TextView) replyPanel.findViewById(R.id.replyNameView);
		replyContentView = (EditText) replyPanel
				.findViewById(R.id.replyContentView);

		View replyTopicButton = findViewById(R.id.replyTopicButton);
		replyTopicButton.setOnClickListener(this);

		b = (Button) replyPanel.findViewById(R.id.addImage);
		// ===========2.3====================

		view_reference = findViewById(R.id.view_reference);

//		=====3.5.5=====
		layout_like = (LinearLayout) findViewById(R.id.layout_like);
		iv_like = (ImageView) findViewById(R.id.iv_like);
		tv_like = (TextView) findViewById(R.id.tv_like);

		layout_like.setOnClickListener(this);

	}

	private void loadTopicInfoNoUse()
	{
		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_TOPIC_GET_INFO + "?topic_id=" + topicID;
		if (userID != null && !"".equals(userID))
		{
			url += "&user_id=" + userID;
		}

		Task task = new Task(null, url, null, C.CLUB_TOPIC_GET_INFO, this);
		publishTask(task, IEvent.IO);
	}

	// true加载第一页， false loadmore
	private void loadTopicInfo2(boolean refresh)
	{

		String userID = DataManager.getInstance().getUser().getUser_id();

		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_TOPIC_GET_INFO2 + offset + "?topic_id=" + topicID;
		if (userID != null && !"".equals(userID))
		{
			url += "&user_id=" + userID;
		}

		Task task = new Task(null, url, null, refresh ? C.CLUB_TOPIC_GET_INFO2
				: "loadMore", this);
		publishTask(task, IEvent.IO);
	}

	boolean needRefresh = false;
	boolean loginBeforePause = true;

	@Override
	protected void onResume()
	{
		super.onResume();
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!loginBeforePause && !TextUtils.isEmpty(userID))
		{
			needRefresh = true;
		}
		if (needRefresh)
		{
			needRefresh = false;
			offset = 0;
			// loadTopicInfo2(true);
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!TextUtils.isEmpty(userID))
		{
			loginBeforePause = true;
		} else
		{
			loginBeforePause = false;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.title:
				if (Common.isDoubleClick())
				{
					// listView.smoothScrollToPosition(0);
					if (needup)
					{
						needup = false;
						refreshView.setHeadText(null, null);
						cleanList();
						// 显示帖子内容
						addHead();
						visibleTitleReference(false);
						getReplyList();
					} else
					{
						// listView.smoothScrollToPosition(0);
						// // listView.smoothScrollToPosition(0, 500);
						listView.setSelection(0);
					}
				}
				break;
			// 回复
			case R.id.replyTopicButton:
				replyName = "";
				replyID = -1;
				showReplyPanel();
				break;
			case R.id.addImage:
				// addImage();
				break;
			case R.id.replyDelButton:
				bitmap = null;
				replyImageView.setImageBitmap(null);
				imagePanel.setVisibility(View.GONE);
				// changeButtonBg();
				break;
			case R.id.closeButton:
				// closeReplyPanel();
				break;
			case R.id.publishButton:
				// publish();
				break;
			case R.id.makeTopButton:
				// topTopic();
				break;
			case R.id.layout_like:
				if ( "1".equals(clubhead.LikeStatus) )
				{
					return;
				}
				if ( Common.isLogin(activity) )
				{
					iv_like.setImageResource(R.drawable.like_button_press);
					tv_like.setText("已赞");
					tv_like.setTextColor(getResources().getColor(R.color.topic_like_color));

					iv_like.startAnimation(animation);

					likeCall.handlerAall();

					LSRequestManager.getInstance().clubTopicLike(topicID, new CallBack() {
						@Override
						public void handler(MyTask mTask) {
							//点赞成功
//							likeCall.handlerAall();
						}
					});
				}
				break;
			default:
				super.onClick(v);
				break;
		}
	}

	public void showReplyPanel()
	{

		if (!Common.isLogin(activity))
		{
			return;
		}
		Intent intent = new Intent(this, LSClubTopicReplyActivity.class);
		intent.putExtra("replyedName", clubhead.nickname);
		intent.putExtra("replyedcontent", "");
		intent.putExtra("replyedfloor", "");
		intent.putExtra("replyedId", "");
		intent.putExtra("clubId", "" + clubID);
		intent.putExtra("topicId", "" + topicID);
		startActivityForResult(intent, 999);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		// 回复成功， 翻页到最后一页
		if (resultCode == RESULT_OK && requestCode == 999)
		{
			int pagetot = data.getIntExtra("lastPage", -1);
			if (pagetot == -1)
			{
				return;
			}
			// 上翻页标记
			needup = true;
			cleanListUp();
			// 最后一页
			page.pageNo = pagetot;
			// 获取最后一页内容
			getReplyListUp();
		}
		// 报名
		else if (resultCode == RESULT_OK && requestCode == 997)
		{
			headViewActive.applyOK();
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view)
	{
		refreshView.onFooterRefreshComplete();
		if (needup)
		{
			Common.toast("没有更多评论");
			return;
		}
		getReplyList();
	}

	// 是否是最后一页， 需要上翻页
	boolean needup = false;

	@Override
	public void onHeaderRefresh(PullToRefreshView view)
	{
		refreshView.onHeaderRefreshComplete();

		if (needup)
		{
			getReplyListUp();
		} else
		{
			cleanList();
			getTopicHead();
		}
	}

	private float headHeight;

	// 获取大图高度
	public void getHeadAdHeight()
	{
		int titleHeight = iv_title_bg.getHeight();
		if (headViewActive != null && headViewActive.getHeadHeight() != 0)
		{
			headHeight = headViewActive.getHeadHeight() - titleHeight;
		}
	}

	/**
	 * 设置标题栏透明度
	 * 
	 * @param num
	 */
	private void setTitleAlpha(float num)
	{
		if (num > headHeight)
		{
			num = headHeight;
		} else if (num < 0)
		{
			num = 0;
		}
		if (num < headHeight)
		{
			setTitleRight(true);
			setBack(true);
		} else
		{
			setTitleRight(false);
			setBack(false);
		}
		float alpha = num / headHeight;
		setTitleBarAlpha(alpha);
	}

	// 设置title不透明
	private void setNoTitleAlpha()
	{
		// 不是活动不要做处理
		if (CurrentHeader != headViewActive)
			return;
		setTitleAlpha(headHeight);
	}

	/** 获取帖子信息 */
	private void getTopicHead()
	{
		String url = C.CLUB_TOPIC_DETAIL_HEAD_3 + topicID;
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (!TextUtils.isEmpty(userID))
		{
			url = url + "/" + userID;
		}
		MyRequestManager.getInstance().requestGet(url, clubhead, callHead);
	}

	private CallBack callHead = new CallBack()
	{

		@Override
		public void handler(MyTask mTask)
		{
			// TODO Auto-generated method stub
			clubhead = (ClubTopicDetailHead) mTask.getResultModel();
			if (clubhead == null)
			{
				return;
			}
			clubID = Common.string2int(clubhead.club_id);
			setHeadView();
			// 获取回复列表
			getReplyList();
		}
	};

	private void setHeadView()
	{
		if ( "1".equals(clubhead.LikeStatus) )
		{
			iv_like.setImageResource(R.drawable.like_button_press);
			tv_like.setText("已赞");
			tv_like.setTextColor(getResources().getColor(R.color.topic_like_color));
		}
		else
		{
			iv_like.setImageResource(R.drawable.like_button);
			tv_like.setText("赞");
			tv_like.setTextColor(getResources().getColor(R.color.color_nine));
		}

		// 话题帖
		if ("0".equals(clubhead.category))
		{
			// 设置不透明
			setTitleBarAlpha(1);
			setTitleRight(false);
			setBack(false);
			// setTitleAlpha(headHeight);
			if (headView == null)
			{
				headView = new LSClubTopicHead(activity);
				headView.setTopic(this);
				headView.setClubName(clubName, clubID);
				listView.addHeaderView(headView);
			}
			// headView.setModel(topic);
			headView.setHead(clubhead);
			//
			headView.setLikeHandler(likeCall);
			CurrentHeader = headView;
		}
		// 活动帖
		else
		{
			visibleTitleReference(false);
			// view_reference.setVisibility(View.GONE);
			// 标题透明
			setTitleBarAlpha(0);
			setTitleRight(true);
			setBack(true);
			if (headViewActive == null)
			{
				headViewActive = new LSClubTopicHeadActive(activity);
				headViewActive.setTopic(this);
				listView.addHeaderView(headViewActive);
				headViewActive.setClubName(clubName, clubID);
			}
			// headViewActive.setModel(topic);

			headViewActive.setLikeHandler(likeCall);
			headViewActive.setModel(clubhead);
			CurrentHeader = headViewActive;
			// 获取AD高度
			getHeadAdHeight();
		}
		listView.setAdapter(null);
	}

	// 上翻页列表
	public void getReplyListUp()
	{
		String url = null;
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userID))
		{
			url = C.CLUB_T0PIC_DETAIL_REPLY + topicID + "?page=" + page.pageNo;
		} else
		{
			url = C.CLUB_T0PIC_DETAIL_REPLY + topicID + "/" + userID + "?page="
					+ page.pageNo;
		}
		MyRequestManager.getInstance().requestGet(url, clubreply,
				new CallBack() {

					@Override
					public void handler(MyTask mTask) {
						// TODO Auto-generated method stub
						clubreply = (ClubTopicReplyList) mTask.getResultModel();
						if (clubreply.topiclist == null) {
							return;
						}
						page.pageNo -= 1;
						// 最上面一页了
						if (page.pageNo == -1) {
							needup = false;
							refreshView.setHeadText(null, null);
							// 显示帖子内容
							addHead();
							visibleTitleReference(false);
						}
						if (adapter == null) {
							// 如果最后一条在第一页， 不隐藏内容
							if (needup) {
								// 隐藏帖子内容
								removeHead();
								refreshView.setHeadText("上一页", "松开加载上一页");
							}
							page.pageSize = clubreply.totpage;
							adapter = new LSClubTopicCommentAdapter(
									LSClubTopicActivity.this,
									clubreply.topiclist, clubID, topicID);
							listView.setAdapter(adapter);
							// 移动到最后
							listView.setSelection(adapter.getCount());
							title.setText("双击此处回到1楼");
							setNoTitleAlpha();
							visibleTitleReference(true);
							listView.setOnScrollListener(listScroll);
							return;
						}
						adapter.addListUp(clubreply.topiclist);
						listView.setSelection(clubreply.topiclist.size());
					}
				});
	}

	/**
	 * 回复列表 topic_id int 帖子id REST user_id int 用户id REST page int 页数 get
	 */
	public void getReplyList()
	{
		if (page.pageNo >= page.pageSize)
		{
			Common.toast("没有更多评论");
			return;
		}
		String url = null;
		String userID = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userID))
		{
			url = C.CLUB_T0PIC_DETAIL_REPLY + topicID + "?page=" + page.pageNo;
		} else
		{
			url = C.CLUB_T0PIC_DETAIL_REPLY + topicID + "/" + userID + "?page="
					+ page.pageNo;
		}
		MyRequestManager.getInstance().requestGet(url, clubreply, callReply);
	}

	private CallBack callReply = new CallBack()
	{

		@Override
		public void handler(MyTask mTask)
		{
			// TODO Auto-generated method stub

			clubreply = (ClubTopicReplyList) mTask.getResultModel();
			if (clubreply.topiclist == null)
			{
				return;
			}
			page.pageNo += 1;
			if (adapter == null)
			{
				page.pageSize = clubreply.totpage;
				adapter = new LSClubTopicCommentAdapter(
						LSClubTopicActivity.this, clubreply.topiclist, clubID,
						topicID);
				listView.setAdapter(adapter);
				listView.setOnScrollListener(listScroll);
				return;
			}
			adapter.addList(clubreply.topiclist);

		}
	};

	public void cleanList()
	{
		page = new Page();
		adapter = null;
		listView.setAdapter(null);
	}

	public void cleanListUp()
	{
		page = new Page();
		adapter = null;
	}

	// 隐藏的占位view， 用来控制listviewtop位置
	private void visibleTitleReference(boolean b)
	{
		// 不是活动不要做处理
		if (CurrentHeader != headViewActive)
			return;
		if (b)
		{
			view_reference.setVisibility(View.VISIBLE);
		} else
		{
			view_reference.setVisibility(View.GONE);
		}
	}

	@Override
	protected void rightAction()
	{
		// TODO Auto-generated method stub
		String imgUrl = null;
		if ( clubhead == null ) return;
		if (clubhead.topic_image != null && clubhead.topic_image.size() >= 1 )
		{
			imgUrl = clubhead.topic_image.get(0).image;
		}
		boolean viewManager = Common.clubDelete(clubhead.is_jion);

		ShareManager.getInstance().showPopWindowInShare(clubhead, "" + clubID,
				imgUrl, clubhead.title, clubhead.shareTxt,
				"" + clubhead.topic_id, viewManager, layoutMain, new CallBack() {

					@Override
					public void handler(MyTask mTask) {
						// TODO Auto-generated method stub
						if ("deleteOk".equals(mTask.result)) {
							deleteThis();
						}
						// 置顶
						else if ("topTopic".equals(mTask.result)) {
							topTopic();
						}
					}
				});

		super.rightAction();
	}

	/** 刷新回复， 删回复后需要调用 */
	public void refrenshReply()
	{
		needup = false;
		cleanList();
		getReplyList();
	}

	/** 删除本贴 */
	private void deleteThis()
	{
		if ( clubhead != null && "0".equals(clubhead.category) )
		{
			LSScoreManager.getInstance().sendScore(clubhead.user_id, LSScoreManager.delpubtalktopic);
		}
		else
		{
			LSScoreManager.getInstance().sendScore(clubhead.user_id, LSScoreManager.delpubactivetopic);
		}
		setResult(RESULT_OK);
		finish();
	}

	private void topTopic()
	{
		setResult(RESULT_OK);
	}

	private void removeHead()
	{
		listView.removeHeaderView(CurrentHeader);
		if (adapter != null)
			adapter.notifyDataSetChanged();
		else
			listView.setAdapter(null);
	}

	private void addHead()
	{
		if (listView.getHeaderViewsCount() == 0)
		{
			listView.setAdapter(null);
			listView.addHeaderView(CurrentHeader);
			if (adapter != null)
			{
				listView.setAdapter(adapter);
//				adapter.notifyDataSetChanged();
			}
			else
				listView.setAdapter(null);
		}
	}

	// 设置title右边按钮
	private void setTitleRight(boolean isBg)
	{
		if (isBg)
		{
			setRightView(R.drawable.club_share);
		} else
		{
			setRightView(R.drawable.club_share_nul);
		}
	}

	// 设置返回按钮
	private void setBack(boolean isBg)
	{
		if (isBg)
		{
			setLeftView(R.drawable.ls_club_back_icon_bg);
		} else
		{
			setLeftView(R.drawable.ls_page_back_icon);
		}
	}

	OnScrollListener listScroll = new OnScrollListener()
{

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
	int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub
		visibleFirst = firstVisibleItem;
		int num = firstVisibleItem + visibleItemCount;
		if (num > pageCount
				&& title.getText().toString().equals("帖子详情"))
		{
			title.setText("双击此处回到1楼");
		} else if (num < pageCount
				&& !title.getText().toString().equals("帖子详情"))
		{
			title.setText("帖子详情");
		}
		// 获取头的高度
		if (headHeight == 0)
		{
			// 获取AD高度
			getHeadAdHeight();
		}
		View v = listView.getChildAt(0);
		if (v == null)
			return;
		float alpha = v.getTop();
		// 只有活动页才做处理
		if (!needup && clubhead != null
				&& "1".equals(clubhead.category))
		{
			if (visibleFirst > 0)
			{
				setTitleAlpha(headHeight);
			} else
			{
				setTitleAlpha(-alpha);
			}
		}
	}
};

	public void addCallLike ()
	{
		likeCall = new HandlerList();
		likeCall.addItem(new CallBack() {
			@Override
			public void handler(MyTask mTask) {
				clubhead.LikeStatus = "1";
				iv_like.setImageResource(R.drawable.like_button_press);
				tv_like.setText("已赞");
				tv_like.setTextColor(getResources().getColor(R.color.topic_like_color));
			}
		});
	}


}
