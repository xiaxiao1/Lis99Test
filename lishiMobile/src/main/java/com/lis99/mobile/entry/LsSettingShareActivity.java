package com.lis99.mobile.entry;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;

public class LsSettingShareActivity extends ActivityPattern {

	
	ImageView iv_back,iv_guanzhu,iv_tiwen,iv_like,iv_tencent,iv_qq,iv_sina;
	boolean guanzhu_flag,tiwen_flag,like_flag,tencent_flag,qq_flag,sina_flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_share);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
	}
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_guanzhu = (ImageView) findViewById(R.id.iv_guanzhu);
		iv_tiwen = (ImageView) findViewById(R.id.iv_tiwen);
		iv_like = (ImageView) findViewById(R.id.iv_like);
		iv_tencent = (ImageView) findViewById(R.id.iv_tencent);
		iv_qq = (ImageView) findViewById(R.id.iv_qq);
		iv_sina = (ImageView) findViewById(R.id.iv_sina);
		
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_guanzhu.setOnClickListener(this);
		iv_tiwen.setOnClickListener(this);
		iv_like.setOnClickListener(this);
		iv_tencent.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
		iv_sina.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == iv_guanzhu.getId()){
			if(guanzhu_flag){
				iv_guanzhu.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_GUANZHU, "");
				guanzhu_flag = false;
			}else{
				iv_guanzhu.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_GUANZHU, "1");
				guanzhu_flag = true;
			}
		}else if(v.getId() == iv_tiwen.getId()){
			if(tiwen_flag){
				iv_tiwen.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_TIWEN, "");
				tiwen_flag = false;
			}else{
				iv_tiwen.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_TIWEN, "1");
				tiwen_flag = true;
			}
		}else if(v.getId() == iv_like.getId()){
			if(like_flag){
				iv_like.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_LIKE, "");
				like_flag = false;
			}else{
				iv_like.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_LIKE, "1");
				like_flag = true;
			}
		}else if(v.getId() == iv_tencent.getId()){
			if(tencent_flag){
				iv_tencent.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_TENCENT, "");
				tencent_flag = false;
			}else{
				iv_tencent.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_TENCENT, "1");
				tencent_flag = true;
			}
		}else if(v.getId() == iv_qq.getId()){
			if(qq_flag){
				iv_qq.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_QQ, "");
				qq_flag = false;
			}else{
				iv_qq.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_QQ, "1");
				qq_flag = true;
			}
		}else if(v.getId() == iv_sina.getId()){
			if(sina_flag){
				iv_sina.setBackgroundResource(R.drawable.ls_off_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_SINA, "");
				sina_flag = false;
			}else{
				iv_sina.setBackgroundResource(R.drawable.ls_open_icon);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.TYPE_SHARE_SINA, "1");
				sina_flag = true;
			}
		}
	}
}
