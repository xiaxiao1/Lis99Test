package com.lis99.mobile;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.ShowUtil;
import com.lis99.mobile.engine.base.BaseActivity;
import com.lis99.mobile.entity.bean.PJBean;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.HttpNetRequest;
import com.lis99.mobile.util.OptData;
import com.lis99.mobile.util.QueryData;
import com.lis99.mobile.util.constens;

import java.util.HashMap;
import java.util.Map;

public class QTActivity extends BaseActivity {
	private HttpNetRequest httpNetRequest;
	private TextView iv_home;
	private TextView tv_tj;
	private EditText editText1;
	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_qt;
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		iv_home=(TextView)findViewById(R.id.iv_home);
		tv_tj=(TextView)findViewById(R.id.tv_tj);
		editText1=(EditText)findViewById(R.id.editText1);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		iv_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_tj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				iflogback("3");
			}
		});
	}

	@Override
	protected View getView() {
		// TODO Auto-generated method stub
		return null;
	}
public void addfeedback(final String e_type) {
		
		new OptData<PJBean>(QTActivity.this).readData(
				new QueryData<PJBean>(){
					
					public String callData() {
						// TODO Auto-generated method stub
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("user_id", DataManager.getInstance().getUser().getUser_id());
						data.put("shop_id",getIntent().getStringExtra("shop_id"));
						data.put("e_type",e_type);
						data.put("content",editText1.getText().toString());
						httpNetRequest = new HttpNetRequest();
						String str = httpNetRequest.connect(constens.URLBC,
								data);
						return str;
					}

					@Override
					public void showData(PJBean t) {
						// TODO Auto-generated method stub
						if(t!=null&&t.getData().equals("ok")){
							ShowUtil.showToast(QTActivity.this, "提交成功");
							finish();
						}else{
							ShowUtil.showToast(QTActivity.this, "提交失败");
						}
					}}, PJBean.class);
				
	}
	public void iflogback(String e_type){
	if (DataManager.getInstance().getUser().getUser_id() != null
			&& !"".equals(DataManager.getInstance().getUser()
					.getUser_id())) {
		addfeedback(e_type);
	} else {
		ShowUtil.showToast(QTActivity.this, "请先登录");
		Intent intent = new Intent(this, LSLoginActivity.class);
		intent.putExtra("unlogin", "unlogin");
		startActivity(intent);
	}
}
}
