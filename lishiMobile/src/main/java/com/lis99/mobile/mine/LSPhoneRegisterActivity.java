package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.regex.Pattern;

public class LSPhoneRegisterActivity extends LSBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_phone_register);
		initViews();

	}

	@Override
	protected void initViews() {
		super.initViews();
		
		loginBtn = (Button) findViewById(R.id.ls_login_bt);
		loginBtn.setOnClickListener(this);
		
		getAuthCodeBtn = (Button) findViewById(R.id.getCodeButton);
		getAuthCodeBtn.setOnClickListener(this);
		
		phoneET = (EditText) findViewById(R.id.phoneView);
		authCodeET   = (EditText) findViewById(R.id.codeView);
		pwdET   = (EditText) findViewById(R.id.pwdView);
		
		View v = findViewById(R.id.iv_back);
		v.setOnClickListener(this);
	}
	
	

	
	private Button loginBtn;
	private Button getAuthCodeBtn;
	
	private EditText phoneET;
	private EditText authCodeET;
	private EditText pwdET;
	
	private String userName;
	private String nickName;
	private String code;
	private String pwd;
	
	private int second = 60;
	
	String token;
	
	private static final int REGIST_SUCCESS = 200;
	
	
	private static final int CountdownMsg = 1001;
	
	
	
	private void requestAuthCode(String phone) {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", phone);
		Task task = new Task(null, C.GET_AUTHCODE, C.HTTP_POST, C.GET_AUTHCODE,
				this);
		task.setPostData(RequestParamUtil.getInstance(this)
				.getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}
	
	private void register() {
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", userName);
		params.put("code", code);
		params.put("passwd", pwd);
		Task task = new Task(null, C.PHONE_REGISTER, C.HTTP_POST, C.PHONE_REGISTER,
				this);
		task.setPostData(RequestParamUtil.getInstance(this)
				.getRequestParams(params).getBytes());
		publishTask(task, IEvent.IO);
	}
	

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				String param = (String) task.getParameter();
				result = new String((byte[]) task.getData());
				if (param.equals(C.PHONE_REGISTER)) {
					parserRegisterInfoList(result);
				} else if (param.equals(C.GET_AUTHCODE)) {
					parserGetCodeInfoList(result);
				} 
			}
			break;
		default:
			break;
		}
		postMessage(DISMISS_PROGRESS);
	}
	
	private void parserGetCodeInfoList(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			JsonNode data = root.get("data");
			if (!"OK".equals(errCode)) {
				String error = data.get("error").asText();
				postMessage(ActivityPattern1.POPUP_TOAST, error);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
		
	}
	
	private void parserRegisterInfoList(String result) {
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			JsonNode data = root.get("data");
			if (!"OK".equals(errCode)) {
				String error = data.get("error").asText();
				postMessage(ActivityPattern1.POPUP_TOAST, error);
				return;
			}
			
			UserBean u = new UserBean();
			u.setUser_id(data.get("user_id").asText());
			nickName = data.get("nickname").asText();
			u.setNickname(nickName);
			u.setMobile(userName);
			DataManager.getInstance().setUser(u);
			DataManager.getInstance().setLogin_flag(true);
			
			postMessage(REGIST_SUCCESS);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMessage(ActivityPattern1.DISMISS_PROGRESS);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ls_login_bt:
				userName = phoneET.getText().toString().trim();
				code = authCodeET.getText().toString().trim();
				pwd = pwdET.getText().toString();
				if(StringUtils.isEmpty(userName) || !Pattern.matches("1[3|5|7|8][0-9]{9}",userName)){
					postMessage(POPUP_TOAST, "手机号码填写不正确");
					return;
				}
				if(StringUtils.isEmpty(code)){
					postMessage(POPUP_TOAST, "验证码填写不正确");
					return;
				}	

				if(StringUtils.isEmpty(pwd)){
					postMessage(POPUP_TOAST, "密码不能为空");
					return;
				}	
				
				if(pwd.length() < 6 || pwd.length() > 20){
					postMessage(POPUP_TOAST, "密码长度必须在6-20位之间");
					return;
				}	

				register();;
				
				return;
			case R.id.getCodeButton:  //获取验证码
				userName = phoneET.getText().toString();
				boolean isMobile = Pattern.matches("1[3|5|7|8][0-9]{9}",userName); 
				if(!isMobile){
					postMessage(POPUP_TOAST, "请输入合法的手机号码");
					return;
				}
				
				requestAuthCode(userName);
				
				//一旦点击之后设置按钮不可点击
				getAuthCodeBtn.setClickable(false);
				getAuthCodeBtn.setText("正在获取...");
		        postMessage(CountdownMsg);
		        return;
			case R.id.iv_back:
				finish();
				break;
			default:
				break;
		}
		super.onClick(v);
	}
	
    public boolean handleMessage(Message msg) {
    	if (msg.what == CountdownMsg) {
    		second--;  
        	getAuthCodeBtn.setText(second + "秒后重发");  
        	
            if(second > 0){  
            	postDelay(CountdownMsg, 1000);
            }else{  
            	getAuthCodeBtn.setClickable(true);
            	getAuthCodeBtn.setText("获取验证码");
            	second = 60;
            }
    		return true;
    	} else if (msg.what == REGIST_SUCCESS) {
			SharedPreferencesHelper.saveUserPass(pwd);
			SharedPreferencesHelper.saveUserPhone(userName);
			Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
			LSRequestManager.getInstance().upDataInfo();
			Intent intent = new Intent();
			intent.putExtra("login", true);
			setResult(RESULT_OK, intent);
			
			finish();
    	}
    	return super.handleMessage(msg);
    }



	
}
