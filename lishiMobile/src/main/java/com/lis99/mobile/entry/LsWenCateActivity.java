package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lis99.mobile.R;
import com.lis99.mobile.util.StatusUtil;

public class LsWenCateActivity extends ActivityPattern {

	ImageView iv_back;
	LinearLayout ll_paobu,ll_tubu,ll_dengshan,ll_panyan,ll_zijia,ll_huaxue,ll_qixing,ll_qita;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_wen_cate);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_paobu = (LinearLayout) findViewById(R.id.ll_paobu);
		ll_tubu = (LinearLayout) findViewById(R.id.ll_tubu);
		ll_dengshan = (LinearLayout) findViewById(R.id.ll_dengshan);
		ll_panyan = (LinearLayout) findViewById(R.id.ll_panyan);
		ll_zijia = (LinearLayout) findViewById(R.id.ll_zijia);
		ll_huaxue = (LinearLayout) findViewById(R.id.ll_huaxue);
		ll_qixing = (LinearLayout) findViewById(R.id.ll_qixing);
		ll_qita = (LinearLayout) findViewById(R.id.ll_qita);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		ll_paobu.setOnClickListener(this);
		ll_tubu.setOnClickListener(this);
		ll_dengshan.setOnClickListener(this);
		ll_panyan.setOnClickListener(this);
		ll_zijia.setOnClickListener(this);
		ll_huaxue.setOnClickListener(this);
		ll_qixing.setOnClickListener(this);
		ll_qita.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == ll_paobu.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "3");
			startActivity(intent);
		}else if(v.getId() == ll_tubu.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "5");
			startActivity(intent);
		}else if(v.getId() == ll_dengshan.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "2");
			startActivity(intent);
		}else if(v.getId() == ll_panyan.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "6");
			startActivity(intent);
		}else if(v.getId() == ll_zijia.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "9");
			startActivity(intent);
		}else if(v.getId() == ll_huaxue.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "4");
			startActivity(intent);
		}else if(v.getId() == ll_qixing.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "7");
			startActivity(intent);
		}else if(v.getId() == ll_qita.getId()){
			Intent intent = new Intent(this,LsCateListActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
		}
	}
}
