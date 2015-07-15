package com.lis99.mobile.entry;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;

import java.util.HashMap;

public class LsSettingInfoActivity extends ActivityPattern {

	ImageView iv_back;
	TextView tv_email,tv_nickname,tv_city;
	AsyncLoadImageView user_head;
	private static final int LOGIN_SUCCESS = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_info);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserInfoTask();
	}
	
	private void getUserInfoTask() {
		Task task = new Task(null, C.USER_INFO_URL+DataManager.getInstance().getUser().getUser_id(), null, "USER_INFO_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getLoginParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
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
	private void parserInfo(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_INFO);
				postMessage(LOGIN_SUCCESS);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}			
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case LOGIN_SUCCESS:
			UserBean user = DataManager.getInstance().getUser();
			tv_email.setText(user.getEmail());
			tv_nickname.setText(user.getNickname());
			tv_city.setText(user.getCity());
			user_head.setImage(user.getHeadicon(), null, null, "zhuangbei_detail");
			break;
		}
		return true;
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_city = (TextView) findViewById(R.id.tv_city);
		user_head = (AsyncLoadImageView) findViewById(R.id.user_head);
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
