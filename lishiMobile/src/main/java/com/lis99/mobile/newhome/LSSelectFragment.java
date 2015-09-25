package com.lis99.mobile.newhome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lis99.mobile.LsActivityDetail;
import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.LsEquiCateActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.EquipAppraiseModel;
import com.lis99.mobile.club.model.EquipRecommendModel;
import com.lis99.mobile.club.model.EquipTypeModel;
import com.lis99.mobile.club.model.NearbyModel;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener;
import com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener;
import com.lis99.mobile.newhome.LSSelectAdapter.OnSelectItemClickListener;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.LocationUtil.getLocation;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LSSelectFragment extends LSFragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener, OnClickListener,OnSelectItemClickListener {

	int page;
	int totalPage = 0;
	private static final int SHOW_CONTENT_LIST = 1001;
	private static final int SHOW_QUICK_ENTER = 1002;
	LSSelectAdapter adapter;
	private PullToRefreshView refreshView;
	private ListView listView;
	List<LSSelectContent> loaedContents = new ArrayList<LSSelectContent>();
//	View headerView;
//	ImageView outdoorButton;
//	ImageView shoppeButton;
//	ImageView skiShopButton;
//	ImageView skiingParkButton;
	
	Bitmap outdoorImage;
	Bitmap shopeImage;
	Bitmap skiShopImage;
	Bitmap skiingParkImage;
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	List<String> quickEnters = new ArrayList<String>();
	String quickEnterTitle;
//	TextView shopTitleView;

	@Override
	protected void initViews(ViewGroup container) {
		super.initViews(container);

		LayoutInflater inflater = LayoutInflater.from(getActivity());
		body = inflater.inflate(R.layout.fragment_select, container, false);
		refreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);

		listView = (ListView) findViewById(R.id.listView);
		
//		addHeaderView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0){
					return;
				}
				LSSelectContent content = loaedContents.get(position-1);
				if (content.isBanner) {
					if (content.is_startbanner == 1) {
						Intent intent = new Intent(getActivity(),
								LsActivityDetail.class);
						Bundle bundle = new Bundle();
						bundle.putInt("id", content.eventID);
						bundle.putString("url", "http://www.lis99.com/huodong/detail/"+content.eventID);
						bundle.putString("title", "活动详情");
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
				        Intent intent = new Intent(getActivity(),LSWebActivity.class);
						intent.putExtra("url", content.bannerUrl);
						startActivity(intent);
					}
				}
			}
		});
	}
	
	@Override
	protected void setTitle(String title) {
	}

//	private void addHeaderView(){
//		LayoutInflater inflater = LayoutInflater.from(getActivity());
//		headerView = inflater.inflate(R.layout.select_header_shop, this.listView, false);
//		listView.addHeaderView(headerView);
//		
//		shopTitleView = (TextView) headerView.findViewById(R.id.title);
//		
//		outdoorButton = (ImageView) headerView.findViewById(R.id.outdoorButton);
//		shoppeButton = (ImageView) headerView.findViewById(R.id.shoppeButton);
//		skiShopButton = (ImageView) headerView.findViewById(R.id.skiShopButton);
//		skiingParkButton = (ImageView) headerView.findViewById(R.id.skiingParkButton);
//		
//		/*
//		outdoorImage = BitmapFactory.decodeResource(getResources(),
//				R.drawable.icon_outdoor);
//		shopeImage = BitmapFactory.decodeResource(getResources(),
//				R.drawable.icon_shoppe);
//		skiShopImage = BitmapFactory.decodeResource(getResources(),
//				R.drawable.icon_skishop);
//		skiingParkImage = BitmapFactory.decodeResource(getResources(),
//				R.drawable.icon_skiingpark);
//				*/
//		
//		outdoorButton.setOnClickListener(this);
//		shoppeButton.setOnClickListener(this);
//		skiShopButton.setOnClickListener(this);
//		skiingParkButton.setOnClickListener(this);
//	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		loadSelects();
//		loadQucikEnter();
		
		getLocation();
	}

	private void refresh() {
		page = 0;
		totalPage = 0;
		loadSelects();
		loadQucikEnter();
	}

	private void loadSelects() {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.SELECT_CONTENT + page;
		Task task = new Task(null, url, null, "SELECT_CONTENT", this);
		publishTask(task, IEvent.IO);
	}
	
	private void loadQucikEnter(){
		quickEnters.clear();
		//postMessage(ActivityPattern1.POPUP_PROGRESS,
		//		getString(R.string.sending));
		String url = C.QUICK_ENTER;
		Task task = new Task(null, url, null, "QUICK_ENTER", this);
		publishTask(task, IEvent.IO);
	}

	private void loadMore() {
		if (page >= totalPage) {
			ShowUtil.showToast(getActivity(), "加载完毕");
			refreshView.onFooterRefreshComplete();
			return;
		}
		loadSelects();
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		//super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				String param = (String) task.getParameter();
				if ("SELECT_CONTENT".equals(param)) {
					parserContents(result);
				}else if("QUICK_ENTER".equals(param)){
					parserQuickEnter(result);
				}
			}
		default:
			break;
		}
	}

	private void parserQuickEnter(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				try {
					JSONObject json = new JSONObject(params);
					JSONObject data = json.optJSONObject("data");
					
					quickEnterTitle = data.optJSONObject("info").optString("title");
					
					JSONArray list = data.optJSONArray("list");
					if (list != null) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject dict = list.getJSONObject(i);
							quickEnters.add(dict.optString("image"));
						}
					}
					postMessage(SHOW_QUICK_ENTER);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void parserContents(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				try {
					JSONObject json = new JSONObject(params);
					JSONObject data = json.optJSONObject("data");
					totalPage = data.optInt("totalPage");

					JSONArray list = data.optJSONArray("list");

					if (list == null)
						return;
					int length = list.length();
					List<LSSelectContent> contents = new ArrayList<LSSelectContent>(
							length);

					for (int i = 0; i < length; ++i) {
						JSONObject aDict = list.getJSONObject(i);

						LSSelectContent content = new LSSelectContent();
						content.content_type_id = aDict
								.optInt("content_type_id");
						content.content_type_title = aDict
								.optString("content_type_title");
						content.template_id = aDict.optInt("template_id");
						content.template_title = aDict
								.optString("template_title");
						content.title = aDict.optString("title");
						content.descript = aDict.optString("descript");

						JSONArray itemArray = aDict.optJSONArray("contentTemp");
						List<LSSelectItem> items = new ArrayList<LSSelectItem>();
						for (int j = 0; j < itemArray.length(); ++j) {
							JSONObject bDict = itemArray.getJSONObject(j);

							LSSelectItem item = new LSSelectItem();
							item.itemID = bDict.optInt("id");
							item.imageUrl = bDict.optString("fileimg");
							item.name = bDict.optString("name");
							item.discount = bDict.optString("discount");
							item.desc = bDict.optString("desc");

							if (content.template_id == 3) {
								item.name = item.discount + "折";
								item.desc = "￥" + item.desc;
							}

							items.add(item);
						}
						content.items = items;

						contents.add(content);

						int bannerType = aDict.optInt("is_startbanner");
						if (bannerType != -1) {
							LSSelectContent banner = new LSSelectContent();
							banner.isBanner = true;
							banner.is_startbanner = bannerType;
							banner.banner_image = aDict
									.optString("banner_image");
							if (banner.is_startbanner == 1) {
								banner.eventID = aDict.optInt("banner_info");
							} else if (banner.is_startbanner == 2) {
								banner.bannerUrl = aDict
										.optString("banner_info");
							}
							banner.weight = aDict.optInt("weight");
							contents.add(banner);
						}
					}
//					if (page == 0) {
//						loaedContents.clear();
//					}
					loaedContents.addAll(contents);
//					page++;
					postMessage(SHOW_CONTENT_LIST);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_CONTENT_LIST:
//			refreshView.onHeaderRefreshComplete();
//			refreshView.onFooterRefreshComplete();
//			if (adapter == null) {
//				adapter = new LSSelectAdapter(getActivity(), loaedContents);
//				adapter.setOnSelectItemListener(this);
//				listView.setAdapter(adapter);
//			} else {
//				adapter.notifyDataSetChanged();
//			}
			getNearby();
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
			return true;
		case SHOW_QUICK_ENTER:
		{
			if (!TextUtils.isEmpty(quickEnterTitle)) {
//				shopTitleView.setText(quickEnterTitle);
			}
			if (quickEnters.size() != 4) {
				return true;
			}
			String url = quickEnters.get(0);
			if (url != null && !"".equals(url)){
//				imageLoader.displayImage(url, outdoorButton);
			}
			
			url = quickEnters.get(1);
			if (url != null && !"".equals(url)){
//				imageLoader.displayImage(url, shoppeButton);
			}
			
			url = quickEnters.get(2);
			if (url != null && !"".equals(url)){
//				imageLoader.displayImage(url, skiShopButton);
			}
			
			url = quickEnters.get(3);
			if (url != null && !"".equals(url)){
//				imageLoader.displayImage(url, skiingParkButton);
			}
			
		}
		return true;
		default:
			break;
		}
		return false;
	}
	
	private void gotoShop(String shopType){
		NewHomeActivity ac = getLSActivity();
		if(ac != null){
			ac.gotoShop(shopType);
		}
	}

	private void goToSkiingParks(){
		Intent intent = new Intent(getActivity(),LsBuyActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.skiingParkButton:
		{
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Type","all");
//			map.put("Type","ski");
			MobclickAgent.onEvent(getActivity(), "Shop_List_Exposed", map);
			goToSkiingParks();
//			gotoShop("2");
		}
			break;
			
		case R.id.outdoorButton:
		{
//			HashMap<String,String> map = new HashMap<String,String>();
//			map.put("Type","outdoor");
//			MobclickAgent.onEvent(getActivity(), "Shop_List_Exposed", map);
//			gotoShop("0");
			Intent intent = new Intent(getActivity(), LSEquiFragment.class);
			startActivity(intent);
		}
			break;
		case R.id.shoppeButton:
		{
			HashMap<String,String> map = new HashMap<String,String>();
//			map.put("Type","mall");
			map.put("Type","outdoor");
			MobclickAgent.onEvent(getActivity(), "Shop_List_Exposed", map);
//			gotoShop("1");
			gotoShop("0");
		}
			break;
		case R.id.skiShopButton:
		{
			HashMap<String,String> map = new HashMap<String,String>();
//			map.put("Type","ski");
			map.put("Type","mall");
			MobclickAgent.onEvent(getActivity(), "Shop_List_Exposed", map);
//			gotoShop("2");
			gotoShop("1");
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
//		loadMore();
		refreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
//		refresh();
		refreshView.onHeaderRefreshComplete();
//		listView.setAdapter(null);
//		adapter = null;
//		loaedContents.clear();
		getLocation();
	}

	private void cleanList ()
	{
		listView.setAdapter(null);
		adapter = null;
		loaedContents.clear();
	}

	@Override
	public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {
		if(content.template_id == 1 || content.template_id == 4){
			Intent intent = new Intent(getActivity(),LsEquiCateActivity.class);
			intent.putExtra("cate", item.itemID);
			intent.putExtra("title", item.name);
			startActivity(intent);
	    }else if(content.template_id == 2 || content.template_id == 5){
//	    	Intent intent = new Intent(getActivity(),LsZhuangbeiDetail.class);
			Intent intent = new Intent(getActivity(),LSEquipInfoActivity.class);
			intent.putExtra("id", item.itemID+"");
			startActivity(intent);
	    }else if(content.template_id == 3){
	        String urlStr = "http://m.lis99.com/shop/Preferential/"+item.itemID;
	        Intent intent = new Intent(getActivity(),LSWebActivity.class);
			intent.putExtra("url", urlStr);
			startActivity(intent);
	    }
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if ( location != null )
		{
			location.stopLocation();
			location = null;
		}
	}
	
	private double latitude, longitude;
	private LocationUtil location;
	
	private void getLocation ()
	{
		location = LocationUtil.getinstance();
		location.setGlocation( call );
		location.getLocation();
	}
	
	private getLocation call = new getLocation()
	{
		
		@Override
		public void Location(double latitude, double longitude)
		{
			// TODO Auto-generated method stub
			if ( location != null )
			{
				location.setGlocation(null);
				location.stopLocation();
				location = null;
				LSSelectFragment.this.latitude = latitude;
				LSSelectFragment.this.longitude = longitude;
				
				getEquipRecommend();
			}
		}
	}; 
	
	private void getEquipType ()
	{
		LSRequestManager.getInstance().getEquipType(new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				EquipTypeModel model = (EquipTypeModel) mTask.getResultModel();
				if ( loaedContents.size() > 0 )
				{
					LSSelectContent item = loaedContents.get(0);
					item.equipType = LSSelectAdapter.EQUIP_TYEP;
					item.EquipTypeItem = model;
				}
				loadSelects();
				loadQucikEnter();
			}
		});
	}
	
	private void getEquipRecommend ()
	{
		cleanList();
		LSRequestManager.getInstance().getEquipRecommend(new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				EquipRecommendModel model = (EquipRecommendModel) mTask.getResultModel();
				LSSelectContent item = new LSSelectContent();
				item.RecommendItem = model;
				loaedContents.add(item);
				getEquipType();
			}
		});
	}
	
	private void getEquipAppraice ()
	{
		LSRequestManager.getInstance().getEquipAppraise(new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				EquipAppraiseModel model = (EquipAppraiseModel) mTask.getResultModel();
				for ( int i = 0; i < model.profilelist.size(); i+=2 )
				{
					LSSelectContent item = new LSSelectContent();
					item.equipType = LSSelectAdapter.EQUIP_APPRAISE_TYPE;
					item.AppraiseItem = new ArrayList<EquipAppraiseModel.Profilelist>();
					item.AppraiseItem.add(model.profilelist.get(i));
					if ( i == 0 )
					{
						item.AppraiseItem.get(0).first = true;
					}
					if ( i + 1 < model.profilelist.size() )
						item.AppraiseItem.add(model.profilelist.get(i+1));
					loaedContents.add(item);
				}
				
				setThisAdapter();
			}
		});
	}
	
	private void getNearby (){
		if ( latitude == 0 || longitude == 0 )
		{
			return;
		}
		LSRequestManager.getInstance().getNearby(latitude, longitude, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				NearbyModel model = (NearbyModel) mTask.getResultModel();
				for ( int i = 0; i < model.shoplist.size(); i++ )
				{
					LSSelectContent item = new LSSelectContent();
					item.equipType = LSSelectAdapter.NEARBY_TYPE;
					item.NearbyItem = model.shoplist.get(i);
					if ( i == 0 )
					{
						item.NearbyItem.isfirst = true;
					}
					//最后一条内容 
					if ( (i + 1) == model.shoplist.size() )
					{
						item.NearbyItem.line_all = true;
					}
					loaedContents.add(item);
					
				}
				//显示全部
				LSSelectContent item = new LSSelectContent();
				item.equipType = LSSelectAdapter.NEARBY_TYPE;
				item.NearbyItem = new NearbyModel().new Shoplist();
				item.NearbyItem.line_all = true;
				item.NearbyItem.isLast = true;
				loaedContents.add(item);
				
				
				getEquipAppraice();
			}
		});
	}
	
	private void setThisAdapter()
	{
		if (adapter == null) {
			adapter = new LSSelectAdapter(getActivity(), loaedContents);
			adapter.setOnSelectItemListener(this);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

}
