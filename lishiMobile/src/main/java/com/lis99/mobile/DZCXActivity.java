package com.lis99.mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.CXlistBean;
import com.lis99.mobile.entity.item.CXListItem;
import com.lis99.mobile.entity.item.CXinfo;
import com.lis99.mobile.entry.adapter.DZCXAdapter;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class DZCXActivity extends BaseActivity {
private DZCXAdapter<CXinfo> dzcxAdapter;
private ImageLoader imageLoader = ImageLoader.getInstance();
private ListView listView1;
private String oid;
private TextView address;
private String title;
private ArrayList<CXinfo> cXinfo;
private LinearLayout ll_home;
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_dzcx;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		initOptions();
		address =(TextView)findViewById(R.id.address);
		listView1 = (ListView)findViewById(R.id.listView1);
		dzcxAdapter = new DZCXAdapter<CXinfo>(DZCXActivity.this, null);
		listView1.setAdapter(dzcxAdapter);
		ll_home =(LinearLayout)findViewById(R.id.ll_home);
		oid = this.getIntent().getStringExtra(constens.OID);
		title=this.getIntent().getStringExtra("title");
		getinfos();
		address.setText(title);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(DZCXActivity.this,GoodsDetailActivity.class);
				intent.putExtra("id", cXinfo.get(arg2).getId());
				startActivity(intent);
			}
			
		});
		ll_home.setOnClickListener(new View.OnClickListener(
				) {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
		
	}
	public void getinfos(){
		new OptData<CXlistBean>(DZCXActivity.this).readData(
				new QueryData<CXlistBean>() {
					
					@Override
					public void showData(CXlistBean t) {
						// TODO Auto-generated method stub
						if(t!=null){
							CXListItem cxListItem=t.getData();
							if(cxListItem!=null){
								cXinfo = cxListItem.getResult();
							
								if(cXinfo.size()>0&&cXinfo!=null){
									dzcxAdapter.setData(cXinfo);
								}
							}
						}
					}
					
					@Override
					public String callData() {
						// TODO Auto-generated method stub
						String url="http://api.lis99.com/shop/storeGoodsList?oid=";
						String data=oid+"&offset=0&limit=10";
						HttpNetRequest httpNetRequest = new HttpNetRequest();
						
						return httpNetRequest.connect(url, data);
					}
				}, CXlistBean.class);
	}
	
	private void initOptions() {
		ImageScaleType ist = ImageScaleType.IN_SAMPLE_POWER_OF_2;
		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc().imageScaleType(ist)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		DemoApplication.initImageLoader(DZCXActivity.this, opt);
	}
//	private BackBean getBAB(String text){
//		BackBean backBean=new BackBean();
//		try {
//			JSONObject jsonObject=new JSONObject(text);
//			backBean.setStatus(jsonObject.getString("status"));
//			JSONObject jsonObject2=jsonObject.getJSONObject("data");
//			int id=Integer.parseInt(jsonObject2.getString("id"));
//			String shop_id=jsonObject2.getString("shop_id");
//			String title=jsonObject2.getString("title");
//			String marketPrice=jsonObject2.getString("marketPrice");
//			String salePrice=jsonObject2.getString("salePrice");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}
