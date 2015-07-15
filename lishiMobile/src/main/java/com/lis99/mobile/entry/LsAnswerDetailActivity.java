package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.AnswerDetailBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.HashMap;

public class LsAnswerDetailActivity extends ActivityPattern  implements IWXAPIEventHandler{

	ImageView iv_back,iv_share;
	TextView tv_title,tv_nickname,tv_conntent,tv_desc,tv_zannum,tv_answer,tv_time,tv_comment;
	AsyncLoadImageView iv_head;
	LinearLayout ll_zan;
	String askId,answerId;
	private static final int SHOW_QUESTION = 200;
	private static final int REFRESH = 201;
	Bitmap bmp;
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	private void initWeibo(Bundle savedInstanceState) {
		 // 创建微博 SDK 接口实例
     mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);	
     if (savedInstanceState != null) {
         mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
     }
     mTencent = Tencent.createInstance(C.TENCENT_APP_ID, this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if(expire ==null || "".equals(expire)){
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_ACCESS_TOKEN),expire );
	}
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_answer_detail);

		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		Resources res = getResources();  
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
    	api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID,true);
    	api.registerApp(C.WEIXIN_APP_ID);
    	api.handleIntent(getIntent(), this);
		bmp = BitmapFactory.decodeResource(res, R.drawable.ls_nologin_header_icon);
		askId = getIntent().getStringExtra("ask_id");
		answerId = getIntent().getStringExtra("answer_id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getList();
	}
	private void getList() {
		String url = C.WENDA_ANSWER_URL+askId + "/"+answerId;
		Task task = new Task(null, url, null, "WENDA_ANSWER_URL", this);
		publishTask(task, IEvent.IO);		
	}
	
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if(((String) task.getParameter()).equals("WENDA_ANSWER_URL")){
					parserList(result);
				}else if(((String) task.getParameter()).equals("MAIN_ADDLIKE_URL")){
					parserDoLike(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	private void parserDoLike(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				if(zan){
					postMessage(POPUP_TOAST, "支持成功");
				}else{
					postMessage(POPUP_TOAST, "取消支持");
				}
			}else{
				postMessage(POPUP_TOAST, "操作失败");
			}
		}
		postMessage(DISMISS_PROGRESS);
	}
	AnswerDetailBean adb;
	private void parserList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
			    adb = (AnswerDetailBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_ANSWER);
				postMessage(SHOW_QUESTION);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}		
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_QUESTION:
			String title = adb.getTitle();
			if(title !=null && title.length()>7){
				title = adb.getTitle().substring(0,7)+"...";
			}
			tv_title.setText(title);
			tv_nickname.setText(adb.getAnswer().getNickname());
			tv_conntent.setText(adb.getDesc());
			iv_head.setImage(adb.getAnswer().getHeadicon(), bmp, bmp, "zhuangbei_detail");
			tv_desc.setText(adb.getAnswer().getVtitle());
			tv_zannum.setText(adb.getAnswer().getLikenum());
			tv_answer.setText(adb.getAnswer().getContent());
			tv_time.setText(adb.getAnswer().getCreate_time());
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_conntent = (TextView) findViewById(R.id.tv_conntent);
		iv_head = (AsyncLoadImageView) findViewById(R.id.iv_head);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_zannum = (TextView) findViewById(R.id.tv_zannum);
		tv_answer = (TextView) findViewById(R.id.tv_answer);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		ll_zan = (LinearLayout) findViewById(R.id.ll_zan);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		tv_comment.setOnClickListener(this);
		ll_zan.setOnClickListener(this);
	}
	
	
	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到",
				R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "提问［"+adb.getTitle()+"］，答案在这里"+adb.getShare_url()+"－分享自@砾石帮你选 Android版";
							LsWeiboSina.getInstance(LsAnswerDetailActivity.this).share(shareText);
							break;
						case 1:
							Resources res = getResources();
							Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.icon_ls);
							String shareWx1Text = adb.getShare_url();
							String title1 = adb.getTitle();
							String desc1 = adb.getAnswer().getContent();
							LsWeiboWeixin.getInstance(LsAnswerDetailActivity.this).getApi().handleIntent(getIntent(), LsAnswerDetailActivity.this);
							LsWeiboWeixin.getInstance(LsAnswerDetailActivity.this).share(shareWx1Text,title1,desc1, bmp1, SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Resources res1 = getResources();
							Bitmap bmp2 = BitmapFactory.decodeResource(res1, R.drawable.icon_ls);
							String shareWx2Text = adb.getShare_url();
							String title2 = adb.getTitle();
							String desc2 = adb.getAnswer().getContent();
							LsWeiboWeixin.getInstance(LsAnswerDetailActivity.this).getApi().handleIntent(getIntent(), LsAnswerDetailActivity.this);
							LsWeiboWeixin.getInstance(LsAnswerDetailActivity.this).share(shareWx2Text,title2,desc2, bmp2, SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, adb.getTitle());
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "砾石帮你选上看到［"+adb.getTitle()+"］这个问题，对我有帮助。");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, adb.getShare_url());
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(LsAnswerDetailActivity.this, params, new IUiListener() {

								@Override
								public void onCancel() {
									Util2.toastMessage(LsAnswerDetailActivity.this, "onCancel: ");
								}

								@Override
								public void onComplete(Object response) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsAnswerDetailActivity.this, "onComplete: ");
								}

								@Override
								public void onError(UiError e) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsAnswerDetailActivity.this, "onComplete: " + e.errorMessage, "e");
								}

							});
							break;
						case 4:
							String shareWx4Text = "提问［"+adb.getTitle()+"］，答案在这里"+adb.getShare_url()+"－分享自@砾石帮你选 Android版" ;
							LsWeiboTencent.getInstance(LsAnswerDetailActivity.this).share(shareWx4Text);
							break;
						}
					}
				});
	}
	private String targetUrl = "http://www.qq.com";
	public static String mAppid;
	private Tencent tencent;
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	@Override
	public void onResponse(BaseResponse arg0) {
		 switch (arg0.errCode) {
	        case WBConstants.ErrorCode.ERR_OK:
	            Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_CANCEL:
	            Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_FAIL:
	            Toast.makeText(this, 
	                    "分享失败" + "Error Message: " + arg0.errMsg, 
	                    Toast.LENGTH_LONG).show();
	            break;
	        }
	}
	boolean zan = false;
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == iv_share.getId()){
			showShareList();
		}else if(v.getId() == ll_zan.getId()){
			ll_zan.setBackgroundResource(R.drawable.ls_zan_bg2);
			if(DataManager.getInstance().getUser().getUser_id()!=null&&!"".equals(DataManager.getInstance().getUser().getUser_id())){
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				if(zan){
					tv_zannum.setText(String.valueOf(Integer.parseInt(tv_zannum.getText().toString())-1));
					zan = false;
				}else{
					tv_zannum.setText(String.valueOf(Integer.parseInt(tv_zannum.getText().toString())+1));
					zan = true;
				}
				doLikeTask();
			}else{
				postMessage(POPUP_TOAST,"请先登录");
				Intent intent = new Intent(this, LSLoginActivity.class);
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
			}
			
		}else if(v.getId() == tv_comment.getId()){
			Intent intent = new Intent(this,LsWendaCommentActivity.class);
			intent.putExtra("id", adb.getAnswer().getId());
			intent.putExtra("type", C.MODULE_TYPE_HUIDA);
			startActivity(intent);
		}
	}
	private void doLikeTask() {
		String url = C.MAIN_ADDLIKE_URL;
		Task task = new Task(null, url, null, "MAIN_ADDLIKE_URL", this);
		task.setPostData(getDoLikeParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getDoLikeParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", adb.getAnswer().getId());
		params.put("module", "huida");
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			System.out.println("COMMAND_GETMESSAGE_FROM_WX");
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			System.out.println("COMMAND_SHOWMESSAGE_FROM_WX");
			break;
		default:
			break;
		}		
	}
	

	@Override
	public void onResp(BaseResp resp) {
		System.out.println("获取到微信消息了...");
		String result = "";
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消发送";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "AUTH_DENIED";
			break;
		default:
			result = "未知错误";
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();		
	}
	
}
