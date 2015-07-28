package com.lis99.mobile.club;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubGridViewAdapter;
import com.lis99.mobile.club.adapter.LSClubRecommendAdapter;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.club.model.LSClubBannerItem;
import com.lis99.mobile.club.model.LSClubGroup;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.ImagePageAdapter.ImagePageAdapterListener;
import com.lis99.mobile.club.widget.ImagePageAdapter.ImagePageClickListener;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.LSSelectAdapter.OnSelectItemClickListener;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.newhome.LSWebActivity;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.LocationUtil.getLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
//import com.lis99.mobile.entry.view.PullDownToRefreshView.OnFooterRefreshListener;
//import com.lis99.mobile.entry.view.PullDownToRefreshView.OnHeaderRefreshListener;

public class LSClubFragment extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener,
		OnSelectItemClickListener, ImagePageAdapterListener, ImagePageClickListener {

	List<LSClubBannerItem> bannerItems = new ArrayList<LSClubBannerItem>();
	List<LSClub> topClubs = new ArrayList<LSClub>();
	
	List<LSClubGroup> groups;
	
	ScrollView scrollView;

//	GridView topClubsView;
	LSClubGridViewAdapter gridViewAdapter;
	
	BannerView bannerView;

	private final static int SHOW_HOME = 1001;
	
	LinearLayout bodyView;
	
	List<View> plusViews = new ArrayList<View>();
	
	PullToRefreshView refreshView;

	ImageView titleRightImage;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
//	PullToRefreshScrollView refreshView;
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	//===============新版本==================
	private ListView my_club_listview;
	private Button btn_club_level, btn_leader_level;
	private LocationUtil location;
	
	private void buildOptions() {
		options = ImageUtil.getImageOptionsClubAD();
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
	protected void initViews(ViewGroup container) {
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.fragment_club, container, false);

		titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
		titleRightImage.setImageResource(R.drawable.club_search_title_right);
		titleRightImage.setOnClickListener(this);

		btn_club_level = (Button) findViewById(R.id.btn_club_level);
		btn_leader_level = (Button) findViewById(R.id.btn_leader_level);
		btn_leader_level.setOnClickListener(this);
		btn_club_level.setOnClickListener(this);
		
		my_club_listview = (ListView) findViewById(R.id.my_club_listview);
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		scrollView.smoothScrollTo(0, 0);
//		topClubsView = (GridView) findViewById(R.id.gridView);
		bannerView = (BannerView) findViewById(R.id.viewBanner);
		
//		bannerView.setOnTouchListener(new View.OnTouchListener() {
//
//		        @Override
//		        public boolean onTouch(View v, MotionEvent event) {
//		        	bannerView.getParent().requestDisallowInterceptTouchEvent(true);
//		            return false;
//		        }
//		    });
		
		
		
		
		bannerView.mViewPager.setOnTouchListener(new View.OnTouchListener() {

		    int dragthreshold = 30;
		    int downX;
		    int downY;

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {

		    	v.getParent().requestDisallowInterceptTouchEvent(true);
		    	refreshView.requestDisallowInterceptTouchEvent(true);
		    	
		        switch (event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		            downX = (int) event.getRawX();
		            downY = (int) event.getRawY();
		            bannerView.stopAutoScroll();
//		            refreshView.enable = false;
		            break;
		        case MotionEvent.ACTION_MOVE:
		            int distanceX = Math.abs((int) event.getRawX() - downX);
		            int distanceY = Math.abs((int) event.getRawY() - downY);
		            bannerView.stopAutoScroll();
//		            refreshView.enable = false;
		            
//		            if (distanceY > distanceX && distanceY > dragthreshold) {
//		            	bannerView.mViewPager.requestDisallowInterceptTouchEvent(true);
//		            bannerView.mViewPager.requestDisallowInterceptTouchEvent(true);
//		            } else if (distanceX > distanceY && distanceX > dragthreshold) {
//		            	bannerView.mViewPager.requestDisallowInterceptTouchEvent(true);
//		            	scrollView.requestDisallowInterceptTouchEvent(false);
//		            }
		            break;
		        case MotionEvent.ACTION_UP:
		        case MotionEvent.ACTION_CANCEL:
		        	bannerView.startAutoScroll();
//		        	refreshView.enable = true;
		        	v.getParent().requestDisallowInterceptTouchEvent(false);
//		            scrollView.requestDisallowInterceptTouchEvent(false);
//		            bannerView.mViewPager.getParent().requestDisallowInterceptTouchEvent(false);
		            break;
		        }
		        return false;
		    }
		});
		
		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
//		refreshView.setOnRefreshListener( new OnRefreshListener<ScrollView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				// TODO Auto-generated method stub
//				loadClubHomePageInfo();
//				refreshView.onRefreshComplete();
//			}
//		});
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
		my_club_listview.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if (position < topClubs.size()) {
					MobclickAgent.onEvent(getActivity(), "Stickie_Club_Clicked");
					LSClub club = topClubs.get(position);
					Intent intent = new Intent(getActivity(), LSClubDetailActivity.class);
					intent.putExtra("clubID", club.getId());
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), LSClubListActivity.class);
					startActivity(intent);
				}
			}
		});
		
		bodyView = (LinearLayout) findViewById(R.id.linearLayout1);
		
		setTitle("聚乐部");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		loadClubHomePageInfo();
		getLocation();
		buildOptions();
	}
	
	public void getLocation ()
	{
		Common.log("=======getLocation===============");
		if (location != null ) return;
		DialogManager.getInstance().startWaiting(getActivity(), null, "定位中...");
		location = LocationUtil.getinstance();
		location.setGlocation( new getLocation()
		{
			
			@Override
			public void Location(double latitude, double longitude)
			{
				// TODO Auto-generated method stub
				DialogManager.getInstance().stopWaitting();
				loadClubHomePageInfo(latitude, longitude);
				if ( location != null )
				location.stopLocation();
				location = null;
			}
		});
		location.getLocation();
	}

	public void loadClubHomePageInfo(double latitude, double longitude ) {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		
		String userID = DataManager.getInstance().getUser().getUser_id();

		String url = C.CLUB_HOMEPAGE;
		if (userID != null && !"".equals(userID)) {
			url += "?user_id=" + userID;
			url += "&latitude="+latitude + "&longitude=" + longitude;
		}
		else
		{
			url += "?latitude="+latitude + "&longitude=" + longitude;
		}
		Common.log("url=="+url);
		Task task = new Task(null, url, null, C.CLUB_HOMEPAGE, this);
		publishTask(task, IEvent.IO);
	}

	public void handleHttpResponseData(JsonNode data, String param) {
		try {
			Common.log("data"+data.toString());
			List<LSClubBannerItem> items = mapper.readValue(data.get("banners")
					.traverse(), new TypeReference<List<LSClubBannerItem>>() {
			});
			bannerItems.clear();
			bannerItems.addAll(items);

			List<LSClub> clubs = mapper.readValue(data.get("tops").traverse(),
					new TypeReference<List<LSClub>>() {
					});
			topClubs.clear();
			topClubs.addAll(clubs);
			
			
			JsonNode node = data.get("clubtyperank");
			if (node != null) {
				groups = mapper.readValue(node.traverse(),
						new TypeReference<List<LSClubGroup>>() {
						});
			}
			
			
			postMessage(SHOW_HOME);
		} catch (Exception e) {
		}


	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_HOME) {
			gridViewAdapter = new LSClubGridViewAdapter(topClubs, getActivity());
//			topClubsView.setAdapter(gridViewAdapter);
			my_club_listview.setAdapter(gridViewAdapter);
//			topClubsView.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					setGridHeightBasedOnChildren(topClubsView);
////					setListHeightBasedOnChildren(my_club_listview);
//				}
//			});
			
			if (bannerItems.size() > 0) {
				ImagePageAdapter adapter = new ImagePageAdapter(getActivity(), bannerItems.size());
				adapter.addImagePageAdapterListener(this);
				adapter.setImagePageClickListener(this);
				bannerView.setBannerAdapter(adapter);
				bannerView.startAutoScroll();
			}
			
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			
			int len = groups == null ? 0 : groups.size();
			
			for (View v : plusViews) {
				bodyView.removeView(v);
				
			}
			
			plusViews.clear();
			
			for (int i = 0; i < len; i++) {
				LSClubGroup group = groups.get(i);
				View titleView = inflater.inflate(R.layout.club_list_title_view, bodyView, false);
				bodyView.addView(titleView);
//				TextView titleTextView = (TextView) titleView.findViewById(R.id.clubListTitleView);
//				titleTextView.setText(group.getTypename());
				
				plusViews.add(titleView);
				
				final ListView listView = (ListView) inflater.inflate(R.layout.club_list_view, bodyView, false);
				bodyView.addView(listView);
				final LSClubRecommendAdapter adapter = new LSClubRecommendAdapter(getActivity(), group.getTyperanks());
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if ( position < (parent.getCount() - 1) )
						{
							MobclickAgent.onEvent(getActivity(), "Ranking_Club_Clicked");
							LSClub club = (LSClub) adapter.getItem(position);
							Intent intent = new Intent(getActivity(), LSClubDetailActivity.class);
							intent.putExtra("clubID", club.getId());
							startActivity(intent);
						}
						else
						{
							Intent intent = new Intent(getActivity(), LSClubListActivity.class);
							startActivity(intent);
						}
					}
				});
				
				plusViews.add(listView);
			}
			
			View v = new View(getActivity());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,50);
			v.setLayoutParams(lp);
			bodyView.addView(v);
			plusViews.add(v);
			return true;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch ( v.getId() )
		{
		case R.id.btn_club_level:
			intent.setClass(getActivity(), LSClubLevelActivity.class);
			intent.putExtra("CLUB_LEVEL", 0);
			startActivity(intent);
			
//			intent.setClass(getActivity(), MyActivityWebView.class);
//			intent.putExtra("TITLE", "TEST");
//			intent.putExtra("URL", "http://m.lis99.com/club/run/applyinfo");
//			startActivity(intent);
			
			break;
		case R.id.btn_leader_level:
			intent.setClass(getActivity(), LSClubLevelActivity.class);
			intent.putExtra("CLUB_LEVEL", 1);
			startActivity(intent);
			break;
			case R.id.titleRightImage:
				startActivity( new Intent(getActivity(), SearchActivity.class));
				break;
		}
	}

	@Override
	public void dispalyImage(ImageView banner, ImageView iv_load, int position) {
		LSClubBannerItem bannerItem = bannerItems.get(position);
		imageLoader.displayImage(bannerItem.getImages(), banner, options, ImageUtil.getImageLoading(iv_load, banner) );
	}

	@Override
	public void onClick(int index) {
		MobclickAgent.onEvent(getActivity(), "Banner_In_Club_Homepage_Clicked");
		LSClubBannerItem item = bannerItems.get(index);
		if (item.getType() == 1){
			Intent intent = new Intent(getActivity(),LSWebActivity.class);
			intent.putExtra("url", item.getContents());
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

}
