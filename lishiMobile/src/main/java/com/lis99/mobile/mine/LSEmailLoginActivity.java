package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.mobel.LSRegistModel;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.InternetUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StringUtil;

import java.util.HashMap;

public class LSEmailLoginActivity extends LSBaseActivity {

	ImageView iv_back;
	Button ls_login_bt;
	EditText ls_account_email, ls_account_password;
	String email, password;
	View ls_forget_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_email_login);
		setView();
		setListener();
	}

	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_login_bt = (Button) findViewById(R.id.ls_login_bt);
		ls_account_email = (EditText) findViewById(R.id.emailView);
		ls_account_password = (EditText) findViewById(R.id.pwdView);
		ls_forget_password = findViewById(R.id.forgetButton);
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		ls_login_bt.setOnClickListener(this);
		ls_forget_password.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	String api_type, api_uid, api_token, api_nickname;

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == ls_login_bt.getId()) {
			getFormInfo();
			if (checkForm()) {
				LoginNow();
			}
		} else if (v.getId() == R.id.forgetButton) {
			alert("忘记密码，请到砾石网网站找回密码");
		}
	}

	private void getFormInfo() {
		email = ls_account_email.getText().toString();
		password = ls_account_password.getText().toString();
	}

	private boolean checkForm() {
		if (email == null || "".equals(email.trim())) {
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.checkEmail(email)) {
			Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password == null || "".equals(password.trim())) {
			Toast.makeText(this, "密码不能为空", 0).show();
			return false;
		}
		if (!StringUtil.checkPassword(password)) {
			Toast.makeText(this, "请输入6至16位的密码，密码为字母、数字或下划线", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		// 检查网络
		if (!InternetUtil.checkNetWorkStatus(this)) {
			Toast.makeText(this, "网络好像有问题", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private LSRegistModel model;

	private void LoginNow() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("pwd", password);
		LoginBase(map, C.LS_SIGNIN);
	}

	private void LoginBase(HashMap<String, Object> map, String url) {
		model = new LSRegistModel();
		MyRequestManager.getInstance().requestPost(url, map, model,
				new CallBack() {

					@Override
					public void handler(MyTask mTask) {
						// TODO Auto-generated method stub
						model = (LSRegistModel) mTask.getResultModel();
						if (model == null)
							return;
						SharedPreferencesHelper.saveaccounttype("email");
						SharedPreferencesHelper.saveLSToken(model.token);
						SharedPreferencesHelper.saveUserName(email);
						SharedPreferencesHelper.saveUserPass(password);
						SharedPreferencesHelper.saveIsVip(model.is_vip);
						UserBean u1 = new UserBean();
						u1.setUser_id(model.user_id);
						u1.setEmail(email);
						u1.setNickname(model.nickname);
						u1.setHeadicon(model.headicon);
						u1.setSex("");
						u1.setPoint("");
						DataManager.getInstance().setUser(u1);
						DataManager.getInstance().setLogin_flag(true);
						Toast.makeText(LSEmailLoginActivity.this, "登录成功", 0)
								.show();
						// 上传设备信息
						LSRequestManager.getInstance().upDataInfo();
						Intent intent = new Intent();
						intent.putExtra("login", true);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
	}

}
