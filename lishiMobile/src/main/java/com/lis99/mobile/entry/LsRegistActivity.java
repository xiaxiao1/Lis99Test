package com.lis99.mobile.entry;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.mobel.LSRegistModel;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.ClearEditText;
import com.lis99.mobile.util.InternetUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.util.HashMap;

public class LsRegistActivity extends ActivityPattern {
	ImageView iv_back;
	ClearEditText ls_regist_nickname,ls_regist_email,ls_regist_password;
	String nickname,email,password;
	Button ls_regist_bt;
	private static final int REGIST_SUCCESS = 200;
	private TextView tv_regist_deal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_regist_new);
		setView();
		setListener();
	}
	private void setView() {
		StatusUtil.setStatusBar(this);
		tv_regist_deal = (TextView) findViewById(R.id.tv_regist_deal);
		tv_regist_deal.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		tv_regist_deal.setOnClickListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_regist_nickname = (ClearEditText) findViewById(R.id.ls_regist_nickname);
		ls_regist_email = (ClearEditText) findViewById(R.id.ls_regist_email);
		ls_regist_password = (ClearEditText) findViewById(R.id.ls_regist_password);
		ls_regist_bt = (Button) findViewById(R.id.ls_regist_bt);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		ls_regist_bt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == ls_regist_bt.getId()){
			getFormInfo();
			if(checkForm()){
				postMessage(POPUP_PROGRESS,"注册中…");
				doRegistTask();
//				getRegist();
			}
		}
		//注册协议
		else if ( v.getId() == R.id.tv_regist_deal )
		{
			
		}
	}
	private void doRegistTask() {
		Task task = new Task(null, C.USER_SIGNUP_URL, null, "USER_SIGNUP_URL", this);
		task.setPostData(getRegistParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getRegistParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("pwd", password);
		params.put("nickname", nickname);
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
				if (((String) task.getParameter()).equals("USER_SIGNUP_URL")) {
					parserRegist(result);
				}
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	private void parserRegist(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				DataManager.getInstance().jsonParse(params, DataManager.TYPE_SIGNUP);
				postMessage(REGIST_SUCCESS);
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
		case REGIST_SUCCESS:
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.ACCOUNT,email);
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.PASSWORD,password);
			SharedPreferencesHelper.saveUserName(email);
			SharedPreferencesHelper.saveUserPass(password);
			Toast.makeText(this, "注册成功", 0).show();
			Intent intent = new Intent(this,NewHomeActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("login", "loginup");
			startActivity(intent);
			finish();
			break;
		}
		return true;
	}
	private void getFormInfo() {
		nickname = ls_regist_nickname.getText().toString();
		email = ls_regist_email.getText().toString();
		password = ls_regist_password.getText().toString(); 
	}
	private boolean checkForm() {
		if(nickname==null || "".equals(nickname.trim())){
			Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(email==null || "".equals(email.trim())){
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.checkEmail(email)) {
			Toast.makeText(this,
					"邮箱格式不正确",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if(password==null || "".equals(password.trim())){
			Toast.makeText(this, "密码不能为空", 0).show();
			return false;
		}
		if (!StringUtil.checkPassword(password)) {
			Toast.makeText(
					this,
					"请输入6至16位的密码，密码为字母、数字或下划线",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// 检查网络
				if (!InternetUtil.checkNetWorkStatus(this)) {
					Toast.makeText(this,
							"网络好像有问题",
							Toast.LENGTH_SHORT).show();
					return false;
				}
		return true;
	}
	
	private LSRegistModel model;
	
	private void getRegist ()
	{
		LSRequestManager.getInstance().getRegist(email, password, nickname, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
				Toast.makeText(LsRegistActivity.this, "注册成功", 0).show();
				Intent intent = new Intent(LsRegistActivity.this,NewHomeActivity.class);
				intent.putExtra("login", "loginup");
				startActivity(intent);
				finish();
			}
		});
	}
	
}
