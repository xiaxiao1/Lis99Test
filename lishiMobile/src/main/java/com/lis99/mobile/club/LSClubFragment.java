package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubGridViewAdapter;
import com.lis99.mobile.club.model.ClubMainListModel;
import com.lis99.mobile.club.model.ClubMainModel;
import com.lis99.mobile.club.model.LSClubBannerItem;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.ImagePageAdapter.ImagePageAdapterListener;
import com.lis99.mobile.club.widget.ImagePageAdapter.ImagePageClickListener;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.LSSelectAdapter.OnSelectItemClickListener;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.LocationUtil.getLocation;
import com.lis99.mobile.util.LoginCallBackManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LSClubFragment extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener,
		OnSelectItemClickListener, ImagePageAdapterListener, ImagePageClickListener {

	LSClubGridViewAdapter gridViewAdapter;
	
	BannerView bannerView;

	private final static int SHOW_HOME = 1001;
	
	List<View> plusViews = new ArrayList<View>();
	
	PullToRefreshView refreshView;

	ImageView titleRightImage, titleLeftImage;

	RelativeLayout titleLeft, titleRight;

	View head;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	//===============新版本==================
	private ListView my_club_listview;
	private LocationUtil location;

	//====3.4======

	private LinearLayout layout_club_level, layout_leader_level, layout_hot_topic, layout_lis_special;
	private ClubMainModel model;
	
	private void buildOptions() {
		options = ImageUtil.getDefultImageOptions();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onPageStart("Club_Homepage_Exposed");
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			MobclickAgent.onPageEnd("Club_Homepage_Exposed");
		} else {
			MobclickAgent.onPageStart("Club_Homepage_Exposed");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	protected void initViews(ViewGroup container) {
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.fragment_club, container, false);
//头
		head = View.inflate(this.getActivity(), R.layout.club_main_list_head, null);

		//搜索按钮
		titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
		titleRightImage.setImageResource(R.drawable.club_search_title_left);
		titleRightImage.setOnClickListener(this);

		titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
		titleLeftImage.setImageResource(R.drawable.club_search_title_right);
		titleLeftImage.setOnClickListener(this);

		titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);
		titleRight = (RelativeLayout) findViewById(R.id.titleRight);
		titleRight.setOnClickListener(this);
		titleLeft.setOnClickListener(this);

//		按钮
		layout_club_level = (LinearLayout) head.findViewById(R.id.layout_club_level);
		layout_leader_level = (LinearLayout) head.findViewById(R.id.layout_leader_level);
		layout_hot_topic = (LinearLayout) head.findViewById(R.id.layout_hot_topic);
		layout_lis_special = (LinearLayout) head.findViewById(R.id.layout_lis_special);

		layout_lis_special.setOnClickListener(this);
		layout_hot_topic.setOnClickListener(this);
		layout_leader_level.setOnClickListener(this);
		layout_club_level.setOnClickListener(this);

//
		bannerView = (BannerView) head.findViewById(R.id.viewBanner);


		my_club_listview = (ListView) findViewById(R.id.my_club_listview);

		my_club_listview.addHeaderView(head);


		bannerView.mViewPager.setOnTouchListener(new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {

				v.getParent().requestDisallowInterceptTouchEvent(true);

		        switch (event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		            bannerView.stopAutoScroll();
		            break;
		        case MotionEvent.ACTION_MOVE:
		            bannerView.stopAutoScroll();
		            break;
		        case MotionEvent.ACTION_UP:
		        case MotionEvent.ACTION_CANCEL:
		        	bannerView.startAutoScroll();
		        	v.getParent().requestDisallowInterceptTouchEvent(false);
		            break;
		        }
		        return false;
		    }
		});
		
		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
		setTitle("聚乐部");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// /注册通知
		LoginCallBackManager.getInstance().addCallBack(LoginState);

		getLocation();
		buildOptions();
	}
	
	public void getLocation ()
	{
		if (location != null ) return;

		cleanList();

		DialogManager.getInstance().startWaiting(getActivity(), null, "数据加载中...");
		location = LocationUtil.getinstance();
		location.setGlocation(new getLocation() {

			@Override
			public void Location(double latitude, double longitude) {
				// TODO Auto-generated method stub

				getClubHomePageList(latitude, longitude);

				if (location != null)
					location.stopLocation();
				location = null;
			}
		});
		location.getLocation();
	}

	private void getClubHomePageList ( double latitude, double longitude )
	{

		model = new ClubMainModel();

		String userID = DataManager.getInstance().getUser().getUser_id();

		String url = C.CLUB_MAIN_INFO;
		if (userID != null && !"".equals(userID)) {
			url += "?user_id=" + userID;
			url += "&latitude="+latitude + "&longitude=" + longitude;
		}
		else
		{
			url += "?latitude="+latitude + "&longitude=" + longitude;
		}

		MyRequestManager.getInstance().requestGet(url, model, new CallBack() {
			@Override
			public void handler(MyTask mTask) {
				model = (ClubMainModel) mTask.getResultModel();

				if (model.banners.size() > 0) {
					ImagePageAdapter adapter = new ImagePageAdapter(getActivity(), model.banners.size());
					adapter.addImagePageAdapterListener(LSClubFragment.this);
					adapter.setImagePageClickListener(LSClubFragment.this);
					bannerView.setBannerAdapter(adapter);
					bannerView.startAutoScroll();
				}

				if ( model.joinclub == null || model.joinclub.size() == 0 )
				{
					String userId = DataManager.getInstance().getUser().getUser_id();
					//没有登录
					if (TextUtils.isEmpty(userId))
					{
						model.joinclub = new ArrayList<ClubMainListModel>();
						ClubMainListModel item = new ClubMainListModel();
						item.type = LSClubGridViewAdapter.NEEDLOGIN;
						model.joinclub.add(item);
					}
					else
					{
						model.joinclub = new ArrayList<ClubMainListModel>();
						ClubMainListModel item = new ClubMainListModel();
						item.type = LSClubGridViewAdapter.NOJOINCLUB;
						model.joinclub.add(item);
					}
				}
				else
				{
					for ( ClubMainListModel item : model.joinclub )
					{
						item.type = LSClubGridViewAdapter.JOINEDCLUB;
					}
				}

				ArrayList<ClubMainListModel> recommend = null;
//				排重， 设置类型
				if ( model.clubtyperank != null && model.clubtyperank.get(0).getTyperanks() != null )
				{
					recommend = model.clubtyperank.get(0).getTyperanks();
					Iterator<ClubMainListModel> iterator = recommend.iterator();

					while ( iterator.hasNext() )
					{
						ClubMainListModel item = iterator.next();
						for ( ClubMainListModel mine : model.joinclub )
						{
							Common.log("mine.id="+mine.id);

							if ( mine.id == item.id )
							{
								iterator.remove();
								break;
							}
							item.type = LSClubGridViewAdapter.RECOMMENDCLUB;
						}
					}
				}
			//用来显示全部
				if ( recommend == null || recommend.size() == 0 )
				{
					ClubMainListModel item = new ClubMainListModel();
					item.type = LSClubGridViewAdapter.ALL_CLUB;
					recommend.add(item);
				}

				gridViewAdapter = new LSClubGridViewAdapter(model.joinclub, recommend, getActivity());
				my_club_listview.setAdapter(gridViewAdapter);

			}
		});

	}


	@Override
	public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch ( v.getId() )
		{
		case R.id.layout_club_level:
			intent.setClass(getActivity(), LSClubLevelActivity.class);
			intent.putExtra("CLUB_LEVEL", 0);
			startActivity(intent);
			break;
		case R.id.layout_leader_level:
			intent.setClass(getActivity(), LSClubLevelActivity.class);
			intent.putExtra("CLUB_LEVEL", 1);
			startActivity(intent);
			break;
			case R.id.layout_hot_topic:
				startActivity( new Intent(getActivity(), ClubHotTopicActivity.class));
				break;
			case R.id.layout_lis_special:
				startActivity( new Intent(getActivity(), LSCLubSpecialMain.class));
				break;
			case R.id.titleRightImage:
			case R.id.titleRight:
				startActivity(new Intent(getActivity(), LSClubListActivity.class));
				break;
			case R.id.titleLeftImage:
			case R.id.titleLeft:
				startActivity( new Intent(getActivity(), SearchActivity.class));
				break;
		}
	}

	@Override
	public void dispalyImage(ImageView banner, ImageView iv_load, int position) {
		if ( model == null || model.banners == null || model.banners.size() <= position )
		{
			return;
		}
		LSClubBannerItem bannerItem = model.banners.get(position);
		imageLoader.displayImage(bannerItem.getImages(), banner, options, ImageUtil.getImageLoading(iv_load, banner));
	}

	@Override
	public void onClick(int index) {
		MobclickAgent.onEvent(getActivity(), "Banner_In_Club_Homepage_Clicked");
		if ( model == null || model.banners == null || model.banners.size() <= index )
		{
			return;
		}
		LSClubBannerItem item = model.banners.get(index);
		if (item.getType() == 1){
//			Intent intent = new Intent(getActivity(),LSWebActivity.class);
//			intent.putExtra("url", item.getContents());
//			startActivity(intent);

			Intent intent = new Intent(getActivity(), MyActivityWebView.class);
			Bundle bundle = new Bundle();
			bundle.putString("URL", item.getContents());
			bundle.putString("TITLE", "");
			bundle.putString("IMAGE_URL", item.getImages());
			bundle.putInt("TOPIC_ID", 0);
			intent.putExtras(bundle);
			startActivity(intent);

		} else if (item.getType() == 2){
			Intent intent = new Intent(getActivity(), LSClubTopicActivity.class);
			intent.putExtra("topicID", Integer.parseInt(item.getContents()));
			startActivity(intent);
		} else if (item.getType() == 3) {
			Intent intent = new Intent(getActivity(), LSClubDetailActivity.class);
			intent.putExtra("clubID", Integer.parseInt(item.getContents()));
			startActivity(intent);
		}
		
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
//		loadClubHomePageInfo();
		getLocation();
		view.onHeaderRefreshComplete();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		view.onFooterRefreshComplete();
	}
	
	@Override
	public void onDestroy()
	{
		if ( location != null )
		location.stopLocation();
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private CallBack LoginState = new CallBack() {
		@Override
		public void handler(MyTask mTask) {
			getLocation();
		}
	};

	private void cleanList ()
	{
		my_club_listview.setAdapter(null);
		gridViewAdapter = null;
	}

}
