package com.lis99.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.entity.bean.PJBean;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.constens;

import java.util.HashMap;
import java.util.Map;

public class PJActivity extends ActivityPattern {
private TextView iv_home;
private TextView tv_tj;
private ImageView iv_star11;
private ImageView iv_star12;
private ImageView iv_star13;
private ImageView iv_star14;
private ImageView iv_star15;
private EditText et_pj;
private HttpNetRequest httpNetRequest;
public  float star=0;
private String id="" ;
private String user_id;

protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_pj);

	StatusUtil.setStatusBar(this);
	setListener();
}


public void setListener() {
	// TODO Auto-generated method stub
	tv_tj=(TextView)findViewById(R.id.tv_tj);
	et_pj=(EditText)findViewById(R.id.et_pj);
	iv_home =(TextView)findViewById(R.id.iv_home);
	iv_star11=(ImageView)findViewById(R.id.iv_star11);
	iv_star12=(ImageView)findViewById(R.id.iv_star12);
	iv_star13=(ImageView)findViewById(R.id.iv_star13);
	iv_star14=(ImageView)findViewById(R.id.iv_star14);
	iv_star15=(ImageView)findViewById(R.id.iv_star15);
	id=this.getIntent().getStringExtra(constens.ID);
	initData();
}


public void initData() {
	iv_star11.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			iv_star11.setImageResource(R.drawable.hwd_large_star_s);
			iv_star12.setImageResource(R.drawable.hwd_large_star_k);
			iv_star13.setImageResource(R.drawable.hwd_large_star_k);
			iv_star14.setImageResource(R.drawable.hwd_large_star_k);
			iv_star15.setImageResource(R.drawable.hwd_large_star_k);
			star=1;
		}
	});
	iv_star12.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			iv_star11.setImageResource(R.drawable.hwd_large_star_s);
			iv_star12.setImageResource(R.drawable.hwd_large_star_s);
			iv_star13.setImageResource(R.drawable.hwd_large_star_k);
			iv_star14.setImageResource(R.drawable.hwd_large_star_k);
			iv_star15.setImageResource(R.drawable.hwd_large_star_k);
			star=2;
		}
	});
	iv_star13.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
	// TODO Auto-generated method stub
	iv_star11.setImageResource(R.drawable.hwd_large_star_s);
	iv_star12.setImageResource(R.drawable.hwd_large_star_s);
	iv_star13.setImageResource(R.drawable.hwd_large_star_s);
	iv_star14.setImageResource(R.drawable.hwd_large_star_k);
	iv_star15.setImageResource(R.drawable.hwd_large_star_k);
	star=3;
	}
		});
	iv_star14.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
	// TODO Auto-generated method stub
	iv_star11.setImageResource(R.drawable.hwd_large_star_s);
	iv_star12.setImageResource(R.drawable.hwd_large_star_s);
	iv_star13.setImageResource(R.drawable.hwd_large_star_s);
	iv_star14.setImageResource(R.drawable.hwd_large_star_s);
	iv_star15.setImageResource(R.drawable.hwd_large_star_k);
	star=4;
	}
		});
	iv_star15.setOnClickListener(new View.OnClickListener() {


public void onClick(View v) {
	// TODO Auto-generated method stub
	iv_star11.setImageResource(R.drawable.hwd_large_star_s);
	iv_star12.setImageResource(R.drawable.hwd_large_star_s);
	iv_star13.setImageResource(R.drawable.hwd_large_star_s);
	iv_star14.setImageResource(R.drawable.hwd_large_star_s);
	iv_star15.setImageResource(R.drawable.hwd_large_star_s);
	star=5;
	}
		});
	// TODO Auto-generated method stub
	iv_home.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PJActivity.this,ShopDetailActivity.class);
			intent.putExtra(constens.OID,id );
			startActivity(intent);
			finish();
		}
	});
	tv_tj.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			iflog();
		}
	});
}

public void handcomit(){
	new OptData<PJBean>(PJActivity.this).readData(
			new QueryData<PJBean>() {

				@Override
				public String callData() {
					Map<String, Object> data = new HashMap<String, Object>();
					String edit =et_pj.getText().toString();
					data.put("shop_id", id);
					data.put("comment",edit);
					data.put("user_id", user_id);
					data.put("star", star);
					httpNetRequest = new HttpNetRequest();
					String str=httpNetRequest.connect(constens.URLpj,data);
					 return str;
				}

				@Override
				public void showData(PJBean t) {
					
					// TODO Auto-generated method stub
					if(t!=null){
						if(t.getData().equals("ok")){
							Intent intent = new Intent(PJActivity.this, ShopDetailActivity.class);
							intent.putExtra(constens.OID, id);
							startActivity(intent);
							finish();
						}else if(t.getData().equals("err")){
							postMessage(POPUP_TOAST, "提交失败");
						}
					
					}else{
						postMessage(POPUP_TOAST, "无数据");
					}
				}}, PJBean.class);}
public void iflog(){
	if (DataManager.getInstance().getUser().getUser_id() != null
			&& !"".equals(DataManager.getInstance().getUser()
					.getUser_id())) {
		user_id=DataManager.getInstance().getUser().getUser_id();
		handcomit();
	} else {
		postMessage(POPUP_TOAST, "请先登录");
		Intent intent = new Intent(this, LSLoginActivity.class);
		intent.putExtra("unlogin", "unlogin");
		startActivity(intent);
	}
}

}
	

