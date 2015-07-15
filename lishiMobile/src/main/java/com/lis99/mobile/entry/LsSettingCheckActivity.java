package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.VersionBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;

public class LsSettingCheckActivity extends ActivityPattern {

	ImageView iv_back;
	RelativeLayout rl_c1,bt_update;
	LinearLayout rl_c2;
	TextView tv_content;
	private static final int HAVE_NEW_VERSION = 200;
	private static final int NO_NEW_VERSION = 201;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_check);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getCheckTask();
	}
	private void getCheckTask() {
		Task task = new Task(null, C.MAIN_CHECKVERSION_URL, null, "USER_INFO_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getLoginParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		String version = C.VERSION.replaceAll("\\.", "");
		params.put("version", version);
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("USER_INFO_URL")) {
					parserInfo(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	VersionBean vb;
	private TextView newVersionText;
	private void parserInfo(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				vb = (VersionBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_MAIN_CHECKVERSION);
				postMessage(HAVE_NEW_VERSION);
			}else{
				//postMessage(POPUP_TOAST, result);
				postMessage(NO_NEW_VERSION);
			}
		}			
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case HAVE_NEW_VERSION:
			rl_c1.setVisibility(View.GONE);
			rl_c2.setVisibility(View.VISIBLE);
			tv_content.setText(vb.getChangelog());
			break;
		case NO_NEW_VERSION:
			rl_c1.setVisibility(View.VISIBLE);
			rl_c2.setVisibility(View.GONE);
			newVersionText.setText("恭喜你，您已经是最新版" + C.VERSION);
			break;
		}
		return true;
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_c1 = (RelativeLayout) findViewById(R.id.rl_c1);
		bt_update =  (RelativeLayout) findViewById(R.id.bt_update);
		rl_c2 =  (LinearLayout) findViewById(R.id.rl_c2);
		tv_content =  (TextView) findViewById(R.id.tv_content);
		newVersionText = (TextView)findViewById(R.id.check_new_version);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		bt_update.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == bt_update.getId()){
			openWebURL(this,vb.getUrl());
		}
	}
	
	public static void openWebURL(Context context, String inURL) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
		//it.setClassName("com.android.browser",
		//		"com.android.browser.BrowserActivity");
		context.startActivity(it);
	}
}
