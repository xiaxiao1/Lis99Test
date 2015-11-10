package com.lis99.mobile.club;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.adapter.LSClubAdapter;
import com.lis99.mobile.club.adapter.RecommendClubAdapter;
import com.lis99.mobile.club.model.ClubMainListModel;
import com.lis99.mobile.club.model.LSClub;
import com.lis99.mobile.club.model.RecommendClubModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.LocationUtil.getLocation;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.constens;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LSClubListActivity extends LSBaseActivity {

	ListView listView;
	LSClubAdapter adapter;
	List<LSClub> clubs = new ArrayList<LSClub>();
	List<LSClub> allClubs = new ArrayList<LSClub>();
	List<LSClub> cityClubs = new ArrayList<LSClub>();
	private final static int SHOW_CLUB = 1001;
	private final static int SHOW_LISH_CLUB = 1002;
	private final static int SHOW_CITY_CLUB = 1003;
	
	private final static int CITY_CHANGE = 1005;
	private final static int ASK_CHANGE = 1006;
	
	TextView brandView;
	TextView lishiView;
	TextView cityView;
	TextView tv_recommend;
	
	View brandLine;
	View lishiLine;
	View cityLine;
	View view_recommend;
	
	TextView selectView;
	TextView locView;

	
	private SharedPreferences preferences;
	private String gpsCity = null;
	private String gpsCityid = "0";
	private String city;
	private String cityid = "";
	private String Latitude1; // 纬度
	private String Longtitude1; // 经度
	private MyReciever myReciever;
	boolean firstClickCityPanel = true;
	List<LSClub> cityTempClubs = new ArrayList<LSClub>();
	
	View noClubView;
	//定位
	private LocationUtil location;

	//===3.6.1===

	private RecommendClubModel recommendModel;
	private RecommendClubAdapter recommendClubAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsclub_list);
		
		
		preferences = getSharedPreferences(constens.CITYINFO, MODE_PRIVATE);
		cityid = preferences.getString("clubCityid", "2");
		city = preferences.getString("clubCity", "北京");
		
		myReciever = new MyReciever();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.lis99.mobile.loc");
		registerReceiver(myReciever, intentFilter);
		
//		getLocation();
		
		initViews();
		setTitle("全部俱乐部");
//		loadCityClub();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("Tab","Lis99");
		MobclickAgent.onEvent(this, "Club_All_Exposed", map);
		
		
//		if (firstClickCityPanel) {
//			firstClickCityPanel = false;
//			startService(new Intent("com.lis99.mobile.service.LocService"));
//		}

		locView.setVisibility(View.GONE);
		noClubView.setVisibility(View.GONE);

		getLocation();
		
	}

	private void getRecommendList () {
		if (recommendModel != null && recommendModel.reccmmendList != null) {
			if (recommendClubAdapter == null) {
				recommendClubAdapter = new RecommendClubAdapter(activity, recommendModel.reccmmendList);
				listView.setAdapter(recommendClubAdapter);
				return;
			} else {
				listView.setAdapter(recommendClubAdapter);
				return;
			}
		}
		recommendModel = new RecommendClubModel();

		String userId = DataManager.getInstance().getUser().getUser_id();
		if (TextUtils.isEmpty(userId))
		{
			userId = "0";
		}

		String url = C.RECOMMEND_CLUB_LIST + "?user_id="+userId;// + "&latitude="+Latitude1+"&longitude="+Longtitude1;

		MyRequestManager.getInstance().requestGet(url, recommendModel, new CallBack() {
			@Override
			public void handler(MyTask mTask) {
				recommendModel = (RecommendClubModel) mTask.getResultModel();
				recommendClubAdapter = new RecommendClubAdapter(activity, recommendModel.reccmmendList);
				listView.setAdapter(recommendClubAdapter);
			}
		});
	}
	
	
	private void getLocation ()
	{
		location = LocationUtil.getinstance();
		location.setGlocation( new getLocation()
		{
			
			@Override
			public void Location(double latitude, double longitude)
			{
				// TODO Auto-generated method stub
				location.setGlocation(null);
				if ( latitude == 0 )
				{
					return;
				}
				LocationOk(""+latitude, ""+longitude);
				
				location.stopLocation();
				location = null;
			}
		});
		location.getLocation();
	}
	
	@Override
	protected void onDestroy() {
		if ( location != null )
		location.stopLocation();
//		stopService(new Intent("com.lis99.mobile.service.LocService"));
		unregisterReceiver(myReciever);
		super.onDestroy();
	}
	
	private void loadClubList() {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		String url = C.CLUB_GET_LIST + "?type=brand";
		Task task = new Task(null, url, null, "brand", this);
		publishTask(task, IEvent.IO);
	}
	
	private void loadCityClub() {
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		
		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("latitude", Latitude1);
//		params.put("longitude", Longtitude1);
		params.put("type", 2);
		params.put("cityid", cityid);
		
		String url = C.CLUB_SEL_LIST;
		Task task = new Task(null, url, C.HTTP_POST, "cityClubList", this);
		task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}
	
	private void loadLishiClubList(){
		
		postMessage(ActivityPattern1.POPUP_PROGRESS,
				getString(R.string.sending));
		
		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("latitude", Latitude1);
//		params.put("longitude", Longtitude1);
		params.put("type", 3);
		params.put("cityid", cityid);
		
		String url = C.CLUB_SEL_LIST;
		Task task = new Task(null, url, C.HTTP_POST, "lishi", this);
		task.setPostData(RequestParamUtil.getInstance(this).getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}
	
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("brand")) {
					parserClubInfo(result);
				} else if ("cityClubList".equals(((String) task.getParameter()))) {
					parserCityClubInfo(result);
				} else if (((String) task.getParameter()).equals(constens.getLocationByBaidu)) {
					parserCityInfo(result);
				} else {
					parserAllClubInfo(result);
				}
			}
			break;
		default:
			break;
		}
//		postMessage(DISMISS_PROGRESS);
	}

	private void parserClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data").get("list");
			List<LSClub> temp = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSClub>>() {
			});
			clubs.clear();
			clubs.addAll(temp);
			postMessage(SHOW_CLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	private void parserAllClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			List<LSClub> temp = LSFragment.mapper.readValue(data.traverse(), new TypeReference<List<LSClub>>() {
			});
			allClubs.clear();
			allClubs.addAll(temp);
			postMessage(SHOW_LISH_CLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	private void parserCityClubInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				cityTempClubs.clear();
				postMessage(SHOW_CITY_CLUB);
//				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			/*
			JsonNode data = root.get("data").get("list");
			List<LSClub> clubs = new ArrayList<LSClub>();
			for (Iterator<String> it = data.fieldNames(); it.hasNext(); ) {
				String field = it.next();
				if (data.get(field) != NullNode.instance) {
					LSClub fakeClub = new LSClub();
					fakeClub.setLocal(true);
					fakeClub.setTitle(field);
					clubs.add(fakeClub);
					List<LSClub> temp = LSFragment.mapper.readValue(data.get(field).traverse(), new TypeReference<List<LSClub>>() {
					});
					clubs.addAll(temp);
				}
			}
			*/
			JsonNode data = root.get("data");
			int size = data.size();
			List<LSClub> clubs = new ArrayList<LSClub>();
			for (int i = 0; i < size; ++i) {
				JsonNode node = data.get(i);
				LSClub fakeClub = new LSClub();
				fakeClub.setLocal(true);
				fakeClub.setTitle(node.get("cate_title").asText());
				clubs.add(fakeClub);
				List<LSClub> temp = LSFragment.mapper.readValue(node.get("clubList").traverse(), new TypeReference<List<LSClub>>() {
				});
				clubs.addAll(temp);
			}
			
			cityTempClubs.clear();
			cityTempClubs.addAll(clubs);
			postMessage(SHOW_CITY_CLUB);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == SHOW_CLUB) {
//			if (adapter == null) {
				adapter = new LSClubAdapter(this, clubs);
				listView.setAdapter(adapter);
//			} else {
//				adapter.notifyDataSetChanged();
//			}
			return true;
		} else if (msg.what == SHOW_LISH_CLUB) {
			adapter = new LSClubAdapter(this, allClubs);
			listView.setAdapter(adapter);
			return true;
		} else if (msg.what == SHOW_CITY_CLUB) {
			cityClubs.clear();
			cityClubs.addAll(cityTempClubs);
			adapter = new LSClubAdapter(this, cityClubs);
			listView.setAdapter(adapter);
			if (cityClubs.size() == 0) {
				noClubView.setVisibility(View.VISIBLE);
			} else {
				noClubView.setVisibility(View.GONE);
			}
			return true;
		} else if (msg.what == CITY_CHANGE) {
			if (selectView == cityView) {
//				DialogManager.getInstance().showDialogGpsError(activity);
				locView.setText(city);
				loadCityClub();
			}
			return true;
		} else if (msg.what == ASK_CHANGE) {
			dialog();
			return true;
		}
		return super.handleMessage(msg);
	}

	@Override
	public void onClick(View v) {

		if ( v.getId() == R.id.layout_recommend )
		{
			if ( selectView == tv_recommend ) return;

			locView.setVisibility(View.GONE);
			noClubView.setVisibility(View.GONE);

			cityLine.setVisibility(View.GONE);
			lishiLine.setVisibility(View.GONE);
			brandLine.setVisibility(View.GONE);

			view_recommend.setVisibility(View.VISIBLE);

			tv_recommend.setTextColor(getResources().getColor(R.color.text_color_blue));

			lishiView.setTextColor(Color.rgb(0x66, 0x66, 0x66));

			cityView.setTextColor(Color.rgb(0x66, 0x66, 0x66));

			brandView.setTextColor(Color.rgb(0x66, 0x66, 0x66));

			selectView = tv_recommend;

			getRecommendList();


		}
		else if (v.getId() == R.id.brandPanel) {
			if (selectView == brandView)
				return;
			locView.setVisibility(View.GONE);
			noClubView.setVisibility(View.GONE);
			view_recommend.setVisibility(View.GONE);

			selectView = brandView;
			brandView.setTextColor(getResources().getColor(R.color.text_color_blue));
			brandLine.setVisibility(View.VISIBLE);
			lishiLine.setVisibility(View.GONE);
			lishiView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			
			cityView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			tv_recommend.setTextColor(Color.rgb(0x66, 0x66, 0x66));


			cityLine.setVisibility(View.GONE);
			
			if (clubs.size() == 0) {
				loadClubList();
			} else {
				postMessage(SHOW_CLUB);
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Tab","Brand");
			MobclickAgent.onEvent(this, "Club_All_Exposed", map);
		} else if (v.getId() == R.id.allPanel) {
			if (selectView == lishiView) 
				return;
			locView.setVisibility(View.GONE);
			noClubView.setVisibility(View.GONE);
			view_recommend.setVisibility(View.GONE);
			
			selectView = lishiView;
			lishiView.setTextColor(getResources().getColor(R.color.text_color_blue));
			brandLine.setVisibility(View.GONE);
			lishiLine.setVisibility(View.VISIBLE);
			brandView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			tv_recommend.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			
			cityView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			cityLine.setVisibility(View.GONE);
			
			if (allClubs.size() == 0) {
				loadLishiClubList();
			} else {
				postMessage(SHOW_LISH_CLUB);
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Tab", "Region");
			MobclickAgent.onEvent(this, "Club_All_Exposed", map);
		} else if (v.getId() == R.id.cityPanel) {
			if (selectView == cityView) 
				return;
			if (firstClickCityPanel) {
				firstClickCityPanel = false;
				startService(new Intent("com.lis99.mobile.service.LocService"));
			}
			locView.setVisibility(View.VISIBLE);
			selectView = cityView;
			cityView.setTextColor(getResources().getColor(R.color.text_color_blue));
			cityLine.setVisibility(View.VISIBLE);
			
			brandView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			brandLine.setVisibility(View.GONE);
			tv_recommend.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			lishiView.setTextColor(Color.rgb(0x66, 0x66, 0x66));
			lishiLine.setVisibility(View.GONE);
			view_recommend.setVisibility(View.GONE);
			
			if (cityClubs.size() == 0) {
				loadCityClub();
			} else {
				postMessage(SHOW_CITY_CLUB);
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("Tab","Region");
			MobclickAgent.onEvent(this, "Club_City_Exposed", map);
		} else if (v.getId() == R.id.locView) {
			Intent intent = new Intent(this,
					LSClubCityListActivityActivity.class);
			intent.putExtra("cityID", TextUtils.isEmpty(gpsCityid) ? 0 : Integer.valueOf(gpsCityid));
			intent.putExtra("cityName", gpsCity == null ? "" : gpsCity);
			startActivity(intent);
		} else {
			super.onClick(v);
		}
	}

	@Override
	protected void initViews() {
		super.initViews();
		listView = (ListView) findViewById(R.id.listView);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if ( listView.getAdapter() instanceof RecommendClubAdapter )
				{
					ClubMainListModel item = (ClubMainListModel) recommendClubAdapter.getItem(position);
					Intent intent = new Intent(LSClubListActivity.this, LSClubDetailActivity.class);
					intent.putExtra("clubID", item.id);
					startActivity(intent);
					return;
				}

				LSClub club = (LSClub) adapter.getItem(position);
				if ( club.getId() == 0 )
				{
					return;
				}
				Intent intent = new Intent(LSClubListActivity.this, LSClubDetailActivity.class);
				intent.putExtra("clubID", club.getId());
				startActivity(intent);
			}
		});
		
		brandView = (TextView) findViewById(R.id.brandView);
		lishiView = (TextView) findViewById(R.id.allView);
		cityView = (TextView) findViewById(R.id.cityView);
		locView = (TextView) findViewById(R.id.locView);
		locView.setText(city);
		locView.setOnClickListener(this);
//		brandView.setOnClickListener(this);
//		allView.setOnClickListener(this);
		
		noClubView = findViewById(R.id.noClubView);
		
		brandLine = findViewById(R.id.brandLine);
		lishiLine = findViewById(R.id.allLine);
		cityLine = findViewById(R.id.cityLine);

		tv_recommend = (TextView) findViewById(R.id.tv_recommend);
		view_recommend = findViewById(R.id.view_recommend);


		selectView = tv_recommend;
	}
	
	
	@Override
	protected void rightAction() {
		
	}
	
	private void getLocationCity() {
		//postMessage(ActivityPattern1.POPUP_PROGRESS,getString(R.string.sending));
		String url = constens.getLocationByBaidu + "?moduleType=club&latitude="+ Latitude1 +"&longitude=" + Longtitude1;
		Task task = new Task(null, url, null, constens.getLocationByBaidu, this);
		publishTask(task, IEvent.IO);
	}
	
	boolean emptyString(String str) {
		return str == null || "".equals(str);
	}
	
	private void parserCityInfo(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				postMessage(ActivityPattern1.POPUP_TOAST, errCode);
				return;
			}
			JsonNode data = root.get("data");
			
			gpsCityid = data.get("id").asText("2");
			gpsCity = data.get("name").asText("北京");
			if (!emptyString(city)) {
				if (!emptyString(gpsCity) && !city.equals(gpsCity)) {
					postMessage(ASK_CHANGE);
				}
			} else {
				if (emptyString(gpsCity)) {
					cityid = "2";
					city = "北京";
				} else {
					cityid = gpsCityid;
					city = gpsCity;
				}
				postMessage(CITY_CHANGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
	}
	
	protected void dialog() {

		AlertDialog.Builder builder = new Builder(this);

		builder.setMessage("你当前位置是" + gpsCity + ",是否切换？");
		builder.setTitle("提示");

		builder.setPositiveButton("切换", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				
				cityid = gpsCityid;
				city = gpsCity;

				SharedPreferences.Editor editor = getSharedPreferences(constens.CITYINFO, Context.MODE_PRIVATE).edit();
				editor.putString("clubCityid", cityid);
				editor.putString("clubCity", city);
				editor.commit();

				postMessage(CITY_CHANGE);
				dialog.dismiss();
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void LocationOk ( String lati, String longti )
	{
		String Latitude = lati;//(String) intent.getStringExtra("X");
		String Longtitude = longti;//(String) intent.getStringExtra("Y");
		
		if (Latitude != null && !"".equals(Latitude)) {
			Latitude1 = Latitude;
			Longtitude1 = Longtitude;
//			if (!emptyString(city)) {
//				postMessage(CITY_CHANGE);
//			}
//			getLocationCity();

			getRecommendList();

		} else {
			cityid = preferences.getString("clubCityid", "");
			city = preferences.getString("clubCity", "");
			if (city == null || "".equals(city)) {
				cityid = "2";
				city = "北京";
			}
			postMessage(CITY_CHANGE);
		}
	}
	
	class MyReciever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String Latitude = (String) intent.getStringExtra("X");
			String Longtitude = (String) intent.getStringExtra("Y");
			
			if (Latitude != null && !"".equals(Latitude)) {
				Latitude1 = Latitude;
				Longtitude1 = Longtitude;
				stopService(new Intent("com.lis99.mobile.service.LocService"));
//				if (!emptyString(city)) {
//					postMessage(CITY_CHANGE);
//				}
				getLocationCity();
			} else {
				cityid = preferences.getString("clubCityid", "");
				city = preferences.getString("clubCity", "");
				if (city == null || "".equals(city)) {
					cityid = "2";
					city = "北京";
				}
				postMessage(CITY_CHANGE);
			}
		}
	}

	
}
