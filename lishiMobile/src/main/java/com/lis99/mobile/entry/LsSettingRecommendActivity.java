package com.lis99.mobile.entry;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.StatusUtil;

public class LsSettingRecommendActivity extends ActivityPattern {

	ImageView iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_recommend);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
	}
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}
	}
}
