package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.mobel.LSRegistModel;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.ClearEditText;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.InternetUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.UIUtils;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LsLoginActivity extends ActivityPattern
{

	ImageView iv_back;
	Button ls_login_bt;
	TextView ls_regist_bt;
	ClearEditText ls_account_email, ls_account_password;
	String email, password;
	TextView ls_forget_password;
	private static final int LOGIN_SUCCESS = 200;
	private static final int THIRDLOGIN_SUCCESS = 201;
	private static final int THIRDLOGIN_SUCCESS1 = 202;

	TextView ls_ll_qq_login, ls_ll_sina_login, ls_wechat_login;

	/**
	 * WeiboSDKDemo 程序的 APP_SECRET。 请注意：请务必妥善保管好自己的
	 * APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
	 */
	private static final String WEIBO_DEMO_APP_SECRET = C.SINA_APP_SERCET;

	/** 通过 code 获取 Token 的 URL */
	private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";

	/** 获取 Token 成功或失败的消息 */
	private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
	private static final int MSG_FETCH_TOKEN_FAILED = 2;

	/** 获取到的 Code */
	private String mCode;
	/** 获取到的 Token */
	private Oauth2AccessToken mAccessToken;
	String unlogin;
	String account1;
	String password1;
	String token1;
	String tokenaccount1;
	String tokenpassword1;

	String access_token;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_login_new);
		unlogin = getIntent().getStringExtra("unlogin");
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire))
		{
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
		setView();
		setListener();
	}

	private void setView()
	{
		StatusUtil.setStatusBar(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_regist_bt = (TextView) findViewById(R.id.ls_regist_bt);
		ls_login_bt = (Button) findViewById(R.id.ls_login_bt);
		ls_account_email = (ClearEditText) findViewById(R.id.ls_account_email);
		ls_account_password = (ClearEditText) findViewById(R.id.ls_account_password);
		ls_forget_password = (TextView) findViewById(R.id.ls_forget_password);
		ls_ll_qq_login = (TextView) findViewById(R.id.ls_ll_qq_login);
		ls_ll_sina_login = (TextView) findViewById(R.id.ls_ll_sina_login);
//		ls_forget_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
	}

	private void setListener()
	{
		iv_back.setOnClickListener(this);
		ls_regist_bt.setOnClickListener(this);
		ls_login_bt.setOnClickListener(this);
		ls_forget_password.setOnClickListener(this);
		ls_ll_qq_login.setOnClickListener(this);
		ls_ll_sina_login.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		account1 = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, C.ACCOUNT);
		password1 = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, C.PASSWORD);
		token1 = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME,
				Context.MODE_PRIVATE, C.TOKEN);
		tokenaccount1 = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_ACCOUNT);
		tokenpassword1 = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TOKEN_PASSWORD);
		postMessage(DISMISS_PROGRESS);
	}

	String api_type, api_uid, api_token, api_nickname;

	@Override
	public void onClick(View v)
	{
		if (v.getId() == iv_back.getId())
		{
			finish();
		} else if (v.getId() == ls_regist_bt.getId())
		{
			Intent intent = new Intent(this, LsRegistActivity.class);
			startActivity(intent);
		} else if (v.getId() == ls_login_bt.getId())
		{
			getFormInfo();
			if (checkForm())
			{
				// postMessage(POPUP_PROGRESS, getString(R.string.sending));
				// doLoginTask();
				LoginNow();
			}
		} else if (v.getId() == ls_forget_password.getId())
		{
			alert("忘记密码，请到砾石网网站找回密码");
		} else if (v.getId() == ls_ll_qq_login.getId())
		{
			api_type = "qq";
			// if (token1 != null && !"".equals(token1)) {
			// if (tokenaccount1 != null && !"".equals(tokenaccount1)) {
			// postMessage(POPUP_PROGRESS, getString(R.string.sending));
			// email = tokenaccount1;
			// password = tokenpassword1;
			// doLoginTask();
			// } else {
			// Intent intent = new Intent(LSLoginActivity.this,
			// LsImproveInfoActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent.putExtra("login", "loginin");
			// intent.putExtra("unlogin", "unlogin");
			// startActivity(intent);
			// }
			// }

			postMessage(POPUP_PROGRESS);

			if (mTencent.isSessionValid())
			{
				UserInfo info = new UserInfo(LsLoginActivity.this,
						mTencent.getQQToken());
				info.getUserInfo(new IUiListener()
				{

					@Override
					public void onError(UiError arg0)
					{
						postMessage(DISMISS_PROGRESS);
					}

					@Override
					public void onComplete(Object res)
					{
						JSONObject json = (JSONObject) res;
						api_nickname = json.optString("nickname", "");
						doThirdLoginTask(api_type);
					}

					@Override
					public void onCancel()
					{
						postMessage(DISMISS_PROGRESS);
					}
				});
			} else
			{
				mTencent.login(this, "all", new IUiListener()
				{

					@Override
					public void onError(UiError arg0)
					{
						Toast.makeText(LsLoginActivity.this, arg0.errorMessage,
								Toast.LENGTH_SHORT).show();
						postMessage(DISMISS_PROGRESS);
					}

					@Override
					public void onComplete(Object res)
					{
						JSONObject json = (JSONObject) res;
						postMessage(POPUP_PROGRESS, getString(R.string.sending));
						System.out.println(json);
						api_uid = json.optString("openid");
						api_token = json.optString("access_token");
						final String expires_in = json.optString("expires_in");

						// SharedPreferencesHelper.putValue(LSLoginActivity.this,
						// C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						// C.TENCENT_OPEN_ID, api_uid);
						// SharedPreferencesHelper.putValue(LSLoginActivity.this,
						// C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						// C.TENCENT_ACCESS_TOKEN, api_token);
						// SharedPreferencesHelper.putValue(LSLoginActivity.this,
						// C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						// C.TENCENT_EXPIRES_IN, expires_in);
						// SharedPreferencesHelper.putValue(LSLoginActivity.this,
						// C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						// C.TOKEN, api_token);
						SharedPreferencesHelper.saveQQOpenId(api_uid);
						SharedPreferencesHelper.saveLSToken(api_token);
//						SharedPreferencesHelper.saveexpires_in(expires_in);
//						SharedPreferencesHelper.saveQQToken(api_token);

						UserInfo info = new UserInfo(LsLoginActivity.this,
								mTencent.getQQToken());
						info.getUserInfo(new IUiListener()
						{

							@Override
							public void onError(UiError arg0)
							{
								postMessage(DISMISS_PROGRESS);
							}

							@Override
							public void onComplete(Object res)
							{
								JSONObject json = (JSONObject) res;
								api_nickname = json.optString("nickname", "");
								doThirdLoginTask(api_type);
							}

							@Override
							public void onCancel()
							{
								postMessage(DISMISS_PROGRESS);
							}
						});
					}

					@Override
					public void onCancel()
					{
						postMessage(DISMISS_PROGRESS);
						Toast.makeText(LsLoginActivity.this, "登录取消", 0).show();
					}
				});
			}
		} else if (v.getId() == ls_ll_sina_login.getId())
		{
			// 取本地token
			api_type = "sina";
			mAccessToken = AccessTokenKeeper.readAccessToken(this);
			mCode = SharedPreferencesHelper.getValue(LsLoginActivity.this,
					C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.SINA_CODE);
			if (mAccessToken.isSessionValid())
			{
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				doGetSinaWeiboNicknameTask();
			} else
			{
				// 微博登陆
				LsWeiboSina
						.getInstance(this)
						.getWeiboAuth()
						.authorize(new AuthListener(),
								WeiboAuth.OBTAIN_AUTH_CODE);
			}

		}
	}

	/**
	 * 微博认证授权回调类。
	 */
	class AuthListener implements WeiboAuthListener
	{

		@Override
		public void onComplete(Bundle values)
		{
			if (null == values)
			{
				Toast.makeText(LsLoginActivity.this, "授权失败", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			String code = values.getString("code");
			if (TextUtils.isEmpty(code))
			{
				Toast.makeText(LsLoginActivity.this, "授权失败", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			mCode = code;
			SharedPreferencesHelper.saveSinaCode(code);
			// SharedPreferencesHelper
			// .putValue(LSLoginActivity.this, C.CONFIG_FILENAME,
			// Context.MODE_PRIVATE, C.SINA_CODE, mCode);
			fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
		}

		@Override
		public void onCancel()
		{
			Toast.makeText(LsLoginActivity.this, "登录取消", Toast.LENGTH_LONG)
					.show();
		}

		@Override
		public void onWeiboException(WeiboException e)
		{
			UIUtils.showToast(LsLoginActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
		}
	}

	/**
	 * 异步获取 Token。
	 * 
	 * @param authCode
	 *            授权 Code，该 Code 是一次性的，只能被获取一次 Token
	 * @param appSecret
	 *            应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
	 *            不要直接暴露在程序中，此处仅作为一个DEMO来演示。
	 */
	public void fetchTokenAsync(String authCode, String appSecret)
	{
		/*
		 * LinkedHashMap<String, String> requestParams = new
		 * LinkedHashMap<String, String>();
		 * requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,
		 * Constants.APP_KEY);
		 * requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET,
		 * appSecretConstantS.APP_SECRET);
		 * requestParams.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,
		 * "authorization_code");
		 * requestParams.put(WBConstants.AUTH_PARAMS_CODE, authCode);
		 * requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,
		 * Constants.REDIRECT_URL);
		 */
		WeiboParameters requestParams = new WeiboParameters();
		requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID, C.SINA_APP_KEY);
		requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
		requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE,
				"authorization_code");
		requestParams.add(WBConstants.AUTH_PARAMS_CODE, authCode);
		requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL,
				C.SINA_REDIRECT_URL);

		/**
		 * 请注意： {@link RequestListener} 对应的回调是运行在后台线程中的， 因此，需要使用 Handler 来配合更新
		 * UI。
		 */
		AsyncWeiboRunner.requestAsync(OAUTH2_ACCESS_TOKEN_URL, requestParams,
				"POST", new RequestListener()
				{
					@Override
					public void onComplete(String response)
					{

						// 获取 Token 成功
						Oauth2AccessToken token = Oauth2AccessToken
								.parseAccessToken(response);
						// 保存 Token 到 SharedPreferences

						if (token != null && token.isSessionValid())
						{
							postMessage(POPUP_PROGRESS,
									getString(R.string.sending));
							AccessTokenKeeper.writeAccessToken(
									LsLoginActivity.this, token);
							mAccessToken = token;
							doGetSinaWeiboNicknameTask();
							// SharedPreferencesHelper.putValue(LSLoginActivity.this,
							// C.CONFIG_FILENAME, Context.MODE_PRIVATE,
							// C.TOKEN,token.getToken());
							// mHandler1.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
						} else
						{

						}
					}

					@Override
					public void onWeiboException(WeiboException arg0)
					{
						// TODO Auto-generated method stub
						mHandler1.obtainMessage(MSG_FETCH_TOKEN_FAILED)
								.sendToTarget();
					}

				});
	}

	private Handler mHandler1 = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MSG_FETCH_TOKEN_SUCCESS:
					// 显示 Token
					Intent intent = new Intent(LsLoginActivity.this,
							LsImproveInfoActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("login", "loginin");
					intent.putExtra("unlogin", "unlogin");
					startActivity(intent);
					break;

				case MSG_FETCH_TOKEN_FAILED:
					Toast.makeText(LsLoginActivity.this, "授权失败",
							Toast.LENGTH_SHORT).show();
					break;
				case 2000:
					finish();
					break;

				default:
					break;
			}
		};
	};

	private void doThirdLoginTask(String type)
	{
		 postMessage(POPUP_PROGRESS);
		 Task task = new Task(null, C.USER_THIRDSIGN_URL, null,
		 "USER_THIRDSIGN_URL", this);
		 task.setPostData(getThirdLoginParams(type).getBytes());
		 publishTask(task, IEvent.IO);
//		if (type.equals("qq"))
//		{
//			QQLogin(type);
//		} else
//		{
//			SinaLogin(type);
//		}
	}

	private String getThirdLoginParams(String type)
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		try
		{
			params.put("api_type", type);
			params.put("api_uid", api_uid);
			params.put("access_token", api_token);
			params.put("third_nick", screen_name);
			params.put("oauth_token_secret", C.SINA_APP_SERCET);
			params.put("oauth_token", mCode);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	private void doLoginTask()
	{
		Task task = new Task(null, C.USER_SIGNIN_URL, null, "USER_SIGNIN_URL",
				this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	// TODO---------------------------------------------------------------
	private String getLoginParams()
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("pwd", password);
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

	String screen_name;

	private void doGetSinaWeiboNicknameTask()
	{
		WeiboParameters requestParams = new WeiboParameters();
		requestParams.add("source", C.SINA_APP_KEY);
		requestParams.add("access_token", mAccessToken.getToken());
		api_token = mAccessToken.getToken();
		requestParams.add("uid", mAccessToken.getUid());
		api_uid = mAccessToken.getUid();
		AsyncWeiboRunner.requestAsync(
				"https://api.weibo.com/2/users/show.json", requestParams,
				"GET", new RequestListener()
				{
					@Override
					public void onComplete(String response)
					{
						System.out.println(response);
						try
						{
							JSONObject js = new JSONObject(response);
							screen_name = js.optString("screen_name");
							api_nickname = screen_name;
							doThirdLoginTask("sina");
						} catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onWeiboException(WeiboException arg0)
					{
						// TODO Auto-generated method stub
						postMessage(DISMISS_PROGRESS);
					}

				});
	}

	@Override
	public void handleTask(int initiator, Task task, int operation)
	{
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator)
		{
			case IEvent.IO:
				if (task.getData() instanceof byte[])
				{
					result = new String((byte[]) task.getData());
					String p = (String) task.getParameter();
					if ("USER_SIGNIN_URL".equals(p))
					{
						parserLogin(result);
					} else if ("USER_SHOW_URL".equals(p))
					{
						// parserUserInfo(result);
					} else if ("USER_THIRDSIGN_URL".equals(p))
					{
						parserThirdLogin(result);
					}
				}
				break;
			default:
				break;
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserThirdLogin(String params)
	{
		String result = DataManager.getInstance().validateResult(params);
		if (result != null)
		{
			if ("OK".equals(result))
			{
				DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_THIRDLOGIN);
				postMessage(THIRDLOGIN_SUCCESS);
			} else
			{
				postMessage(THIRDLOGIN_SUCCESS1);
			}
		}
		postMessage(DISMISS_PROGRESS);
	}

	private void parserLogin(String params)
	{
		Common.log("params====" + params);
		String result = DataManager.getInstance().validateResult(params);
		if (result != null)
		{
			if ("OK".equals(result))
			{
				DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_SIGNUP);
				postMessage(LOGIN_SUCCESS);
			} else
			{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (super.handleMessage(msg))
			return true;
//////		//登陆成功， 上传设备信息
//		LSRequestManager.getInstance().upDataInfo();
		switch (msg.what)
		{
			case LOGIN_SUCCESS:
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.ACCOUNT, email);
				SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
						Context.MODE_PRIVATE, C.PASSWORD, password);
//				Toast.makeText(this, "登录成功", 0).show();
				if ("unlogin".equals(unlogin))
				{

				} else
				{
					// Intent intent = new Intent(this,MainActivity.class);
					// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// intent.putExtra("login", "loginin");
					// startActivity(intent);
				}
				//登陆成功， 上传设备信息
				LSRequestManager.getInstance().upDataInfo();
				finish();
				break;
			case THIRDLOGIN_SUCCESS:
//				登陆成功， 上传设备信息
				LSRequestManager.getInstance().upDataInfo();
//				Toast.makeText(this, "登录成功", 0).show();
				saveThirdUserMeg(DataManager.getInstance().getUser());
				if ("unlogin".equals(unlogin))
				{
				} else
				{
					Intent intent = new Intent(this, NewHomeActivity.class);
					// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("login", "loginin");
					startActivity(intent);
				}
				finish();
				break;
			case THIRDLOGIN_SUCCESS1:
				Intent intent = new Intent(LsLoginActivity.this,
						LsImproveInfoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("api_type", api_type);
				intent.putExtra("api_uid", api_uid);
				intent.putExtra("access_token", api_token);
				intent.putExtra("third_nick", api_nickname);
				intent.putExtra("bind", true);
				intent.putExtra("login", "loginin");
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
				break;
		}
		return true;
	}

	private void getFormInfo()
	{
		// TODO-------------------------------------------------------------------------
		email = ls_account_email.getText().toString();
		password = ls_account_password.getText().toString();
	}

	private boolean checkForm()
	{
		if (email == null || "".equals(email.trim()))
		{
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.checkEmail(email))
		{
			Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password == null || "".equals(password.trim()))
		{
			Toast.makeText(this, "密码不能为空", 0).show();
			return false;
		}
		if (!StringUtil.checkPassword(password))
		{
			Toast.makeText(this, "请输入6至16位的密码，密码为字母、数字或下划线", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		// 检查网络
		if (!InternetUtil.checkNetWorkStatus(this))
		{
			Toast.makeText(this, "网络好像有问题", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void saveThirdUserMeg(UserBean user)
	{
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, "accounttype", "third");
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, C.ACCOUNT, user.getEmail());
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, "nickname", user.getNickname());
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, "user_id", user.getUser_id());
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, "headicon", user.getHeadicon());
//		SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//				Context.MODE_PRIVATE, "sn", user.getSn());
		SharedPreferencesHelper.saveaccounttype("third");
		SharedPreferencesHelper.saveUserName(user.getEmail());
		SharedPreferencesHelper.savenickname(user.getNickname());
		SharedPreferencesHelper.saveuser_id(user.getUser_id());
		SharedPreferencesHelper.saveheadicon(user.getHeadicon());
		SharedPreferencesHelper.saveSn(user.getSn());
		
	}

	private LSRegistModel model;

	private HashMap<String, Object> getreeustMap(String type)
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		try
		{
			params.put("api_type", type);
			params.put("api_uid", api_uid);
			params.put("access_token", api_token);
			params.put("third_nick", screen_name);
			params.put("oauth_token_secret", C.SINA_APP_SERCET);
			params.put("oauth_token", mCode);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	private void SinaLogin(String type)
	{
		LoginBase(getreeustMap(type), C.USER_THIRDSIGN_URL);
	}

	private void QQLogin(String type)
	{
		LoginBase(getreeustMap(type), C.USER_THIRDSIGN_URL);
	}

	private void LoginNow()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("pwd", password);
		LoginBase(map, C.LS_SIGNIN);
	}

	private void LoginBase( HashMap<String, Object> map, String url )
	{
		model = new LSRegistModel();
		MyRequestManager.getInstance().requestPost(url, map, model,
				new CallBack()
				{

					@Override
					public void handler(MyTask mTask)
					{
						// TODO Auto-generated method stub
						model = (LSRegistModel) mTask.getResultModel();
						if (model == null)
							return;
						
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
						Toast.makeText(LsLoginActivity.this, "登录成功", 0).show();
						
						//上传设备信息
						LSRequestManager.getInstance().upDataInfo();
						
						finish();
					}
				});
	}

}
