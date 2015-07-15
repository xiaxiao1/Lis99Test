package com.lis99.mobile.entry;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.CustomEditTextWhite;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;

public class LsSettingFeedbackActivity extends ActivityPattern {

	ImageView iv_back;
	CustomEditTextWhite ls_content,ls_contact;
	RelativeLayout bt_tiwen;
	String content,contact;
	private static final int LOGIN_SUCCESS = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_setting_feedback);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
	}
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_content = (CustomEditTextWhite) findViewById(R.id.ls_content);
		ls_contact = (CustomEditTextWhite) findViewById(R.id.ls_contact);
		bt_tiwen = (RelativeLayout) findViewById(R.id.bt_tiwen);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		bt_tiwen.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == bt_tiwen.getId()){
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			doFeedback();
		}
	}
	
	private void doFeedback() {
		Task task = new Task(null, C.MAIN_FEEDBACK_URL, null, "USER_INFO_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getLoginParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		params.put("email", ls_contact.getText());
		params.put("comment", ls_content.getText());
		params.put("version", C.VERSION);
		params.put("sdk", "android");
		params.put("channel", "android");
		String device="";
		if(android.os.Build.MODEL.contains(android.os.Build.MANUFACTURER)){
			device = android.os.Build.MODEL;
		}else{
			device = android.os.Build.MANUFACTURER+android.os.Build.MODEL;
		}
		params.put("device", device);
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
			Toast.makeText(this, "提交成功", 1).show();
			finish();
			break;
		}
		return true;
	}
}
