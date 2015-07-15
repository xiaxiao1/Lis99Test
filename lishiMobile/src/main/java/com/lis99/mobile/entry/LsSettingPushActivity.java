package com.lis99.mobile.entry;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;

public class LsSettingPushActivity extends ActivityPattern{

	ImageView iv_back,iv_sys,iv_tiwen,iv_pinglun;
	boolean sys_flag,tiwen_flag,pinglun_flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_push);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
	}
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_sys = (ImageView) findViewById(R.id.iv_sys);
		iv_tiwen = (ImageView) findViewById(R.id.iv_tiwen);
		iv_pinglun = (ImageView) findViewById(R.id.iv_pinglun);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_sys.setOnClickListener(this);
		iv_tiwen.setOnClickListener(this);
		iv_pinglun.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == iv_sys.getId()){
			if(sys_flag){
				iv_sys.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_SYS, "");
				sys_flag = false;
			}else{
				iv_sys.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_SYS, "1");
				sys_flag = true;
			}
		}else if(v.getId() == iv_tiwen.getId()){
			if(tiwen_flag){
				iv_tiwen.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_TIWEN, "");
				tiwen_flag = false;
			}else{
				iv_tiwen.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_TIWEN, "1");
				tiwen_flag = true;
			}
		}else if(v.getId() == iv_pinglun.getId()){
			if(pinglun_flag){
				iv_pinglun.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_PINGLUN, "");
				pinglun_flag = false;
			}else{
				iv_pinglun.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_NOTICE_PINGLUN, "1");
				pinglun_flag = true;
			}
		}
	}
	
}
