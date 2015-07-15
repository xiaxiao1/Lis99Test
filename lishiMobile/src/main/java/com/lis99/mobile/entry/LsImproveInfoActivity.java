package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
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

public class LsImproveInfoActivity extends ActivityPattern {

	ImageView iv_back;
	ClearEditText ls_regist_nickname,ls_regist_email,ls_regist_password;
	String nickname,email,password;
	Button ls_submit_bt;
	TextView ls_improve_nouser_tv,ls_improve_haveuser_tv;
	LinearLayout ll_nickname;
	View line_nickname;
	private static final int REGIST_SUCCESS = 200;
	private static final int REGIST_SUCCESS2 = 201;
	
	boolean needBind = false;
	boolean bindThird = false;
	
	String flag = "no";
	//======新加的＝＝＝＝＝＝＝＝＝＝＝＝＝
	private ImageView iv_nouser, iv_haveuser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_improve_info_new);

		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		
		
		needBind = getIntent().getBooleanExtra("bind", false);
		if(needBind){
			nickname = getIntent().getStringExtra("third_nick");
		}
		
	}
	private void setView() {
		StatusUtil.setStatusBar(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_regist_nickname = (ClearEditText) findViewById(R.id.ls_regist_nickname);
		ls_regist_email = (ClearEditText) findViewById(R.id.ls_regist_email);
		ls_regist_password = (ClearEditText) findViewById(R.id.ls_regist_password);
		ls_submit_bt = (Button) findViewById(R.id.ls_submit_bt);
		ls_improve_nouser_tv = (TextView) findViewById(R.id.ls_improve_nouser_tv);
		ls_improve_haveuser_tv = (TextView) findViewById(R.id.ls_improve_haveuser_tv);
		ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
		line_nickname = findViewById(R.id.line_nickname);
		iv_nouser = (ImageView) findViewById(R.id.iv_nouser);
		iv_haveuser = (ImageView) findViewById(R.id.iv_haveuser);
		
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		ls_submit_bt.setOnClickListener(this);
		ls_improve_nouser_tv.setOnClickListener(this);
		ls_improve_haveuser_tv.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == ls_submit_bt.getId()){
			getFormInfo();
			if(checkForm()){
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				doRegistTask();
			}
		}else if(v.getId() == ls_improve_nouser_tv.getId()){
			flag = "no";
			bindThird = false;
			ls_improve_nouser_tv.setTextColor(0xff2acbc2);
			ls_improve_haveuser_tv.setTextColor(0xff999999);
			ll_nickname.setVisibility(View.VISIBLE);
			line_nickname.setVisibility(View.VISIBLE);
			iv_nouser.setBackgroundColor(getResources().getColor(R.color.text_color_blue));
			iv_haveuser.setBackgroundColor(getResources().getColor(R.color.color_eee));
		}else if(v.getId() == ls_improve_haveuser_tv.getId()){
			flag = "yes";
			bindThird = true;
			ls_improve_nouser_tv.setTextColor(0xff999999);
			ls_improve_haveuser_tv.setTextColor(0xff2acbc2);
			ll_nickname.setVisibility(View.GONE);
			line_nickname.setVisibility(View.GONE);
			iv_haveuser.setBackgroundColor(getResources().getColor(R.color.text_color_blue));
			iv_nouser.setBackgroundColor(getResources().getColor(R.color.color_eee));
		}
	}
	private void doRegistTask() {
		Task task;
		if("yes".equals(flag)){
			task = new Task(null, C.USER_BIND_URL, null, "USER_SIGNUP_URL", this);
			task.setPostData(getBindParams().getBytes());
			publishTask(task, IEvent.IO);			
		}else{
//			task = new Task(null, C.USER_SIGNUP_URL, null, "USER_SIGNUP_URL", this);
//			task.setPostData(getRegistParams().getBytes());

//			//登陆成功， 上传设备信息
//			LSRequestManager.getInstance().upDataInfo();

			LSRequestManager.getInstance().getRegist(email, password, nickname, new CallBack()
			{
				
				@Override
				public void handler(MyTask mTask)
				{
					// TODO Auto-generated method stub
					Toast.makeText(LsImproveInfoActivity.this, "注册成功", 0).show();
					Intent intent = new Intent(LsImproveInfoActivity.this,NewHomeActivity.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("login", "loginup");
					startActivity(intent);
					finish();
				}
			});
		}
	}
	private String getBindParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user", email);
		params.put("pwd", password);
		params.put("api_type", getIntent().getStringExtra("api_type"));
		params.put("api_uid", getIntent().getStringExtra("api_uid"));
		params.put("access_token", getIntent().getStringExtra("access_token"));
		params.put("third_nick", nickname);
		if(DataManager.getInstance().getUser().getUser_id()!=null){
			params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		}
		return RequestParamUtil.getInstance(this).getRequestParams(params);
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
				if("yes".equals(flag)){
					postMessage(REGIST_SUCCESS);
				}else{
					postMessage(REGIST_SUCCESS2);
				}
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
			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_ACCOUNT,email);
			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_PASSWORD,password);
			if (needBind) {
				saveThirdUserMeg(DataManager.getInstance().getUser());
			}
			if("yes".equals(flag)){
				Toast.makeText(this, "登录成功", 0).show();
			}else{
				Toast.makeText(this, "注册成功", 0).show();
			}
			//if("unlogin".equals(getIntent().getStringExtra("unlogin"))){
				
			//}else{
				Intent intent = new Intent(this,NewHomeActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("login", "loginup");
				startActivity(intent);
			//}
			finish();
			break;
		case REGIST_SUCCESS2:
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			flag = "yes";
			doRegistTask();
			break;
		}
		return true;
	}
	private void getFormInfo() {
		if(!needBind)
			nickname = ls_regist_nickname.getText().toString();
		email = ls_regist_email.getText().toString();
		password = ls_regist_password.getText().toString(); 
	}
	private boolean checkForm() {
		if(!needBind){
			if(nickname==null || "".equals(nickname.trim())){
				Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				return false;
			}
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
	
	private void saveThirdUserMeg(UserBean user){
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, "accounttype","third");
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, C.ACCOUNT, user.getEmail());
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, "nickname", user.getNickname());
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, "user_id", user.getUser_id());
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, "headicon", user.getHeadicon());
		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, "sn", user.getSn());
	}
}
