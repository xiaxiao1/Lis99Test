package com.lis99.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.FavBean;
import com.lis99.mobile.entity.item.FavItem;
import com.lis99.mobile.entry.adapter.FavAdapter;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavActivity extends BaseActivity {
private String Latitude1="";
private String Longtitude1="";
private HttpNetRequest httpNetRequest;
private MyReciever myReciever;
private Intent intent=null;
private ListView listView;
private FavAdapter<FavItem> favAdapter;
private ImageView iv_home;
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_fav;
	}

	@Override
	public void setListener() {
		if(intent==null){
			intent=new Intent("com.lis99.mobile.service.LocService");
		}
		startService(intent);
		myReciever = new MyReciever();
		iv_home =(ImageView)findViewById(R.id.iv_home);
		listView =(ListView)findViewById(R.id.listView1);
		favAdapter= new FavAdapter<FavItem>(FavActivity.this, null);
		listView.setAdapter(favAdapter);
		initData();
		initOptions();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.lis99.mobile.loc");
		myReciever = new MyReciever();
		registerReceiver(myReciever, intentFilter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FavItem favItem = (FavItem) favAdapter.getItem(arg2);
				Intent intent = new Intent(FavActivity.this,
						ShopDetailActivity.class);
				String oid=favItem.getOid();
				intent.putExtra(constens.OID, oid);
				intent.putExtra("fav", "fav");
				intent.putExtra("dis", favItem.getDistance());
				startActivity(intent);
				
			}
		});
		iv_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				stopService(new Intent("com.lis99.mobile.service.LocService"));
			}
		});
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
	}
	protected void onDestroy() {
		stopService(intent);
		super.onDestroy();
	}
	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		DemoApplication.initImageLoader(FavActivity.this, opt);
	}
	public void getFav(){
		
		new OptData<FavBean>(FavActivity.this).readData(
				new QueryData<FavBean>() {

					@Override
					public String callData() {
						Map<String, Object> data = new HashMap<String, Object>();
						data.put(constens.LATITUDE, Latitude1);
						data.put(constens.LONGITUDE, Longtitude1);
						data.put("user_id", DataManager.getInstance().getUser().getUser_id());
						httpNetRequest = new HttpNetRequest();
						String str=httpNetRequest.connect(constens.urlshowlike, data);
						return str;
					}

					@Override
					public void showData(FavBean t) {
						
						// TODO Auto-generated method stub
						if (t != null) {
							favAdapter.clearData();
							ArrayList<FavItem> favItems=t.getData();
							favAdapter.setData(favItems);
									
						} else {
							ShowUtil.showToast(FavActivity.this, getString(R.string.lj_false));
						}
					}
				}, FavBean.class);
	}
	class MyReciever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
//			Bundle data = (Bundle)intent.getBundleExtra("LOC");
			Latitude1=(String)intent.getStringExtra("X");
			Longtitude1=(String)intent.getStringExtra("Y");
			iflog();
		}
	}
	public void iflog(){
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser()
						.getUser_id())) {
			
			getFav();
		} else {
			ShowUtil.showToast(FavActivity.this, "请先登录");
			
		}
	}
}
