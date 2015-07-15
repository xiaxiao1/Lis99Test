package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ShaituDetailBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.C;
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

public class LsShaiDetailActivity extends ActivityPattern implements IWXAPIEventHandler{

	ImageView iv_back,iv_share,iv_comment;
	TextView tv_toptitle,tv_nickname,tv_title,tv_comment;
	AsyncLoadImageView iv_bg,iv_head;
	private static final int SHOW_QUESTION = 200;
	String id;
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
	 private String targetUrl = "http://www.qq.com";
		public static String mAppid;
		private Tencent tencent;
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_shai_detail);
		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
    	api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID,true);
    	api.registerApp(C.WEIXIN_APP_ID);
		id = getIntent().getStringExtra("id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getList();
	}
	private void getList() {
		String url = C.SHAITU_DETAIL_URL + id;
		Task task = new Task(null, url, null, "SHAITU_DETAIL_URL", this);
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
				if(((String) task.getParameter()).equals("SHAITU_DETAIL_URL")){
					parserList(result);
				}
			}
			break;
		default:
			break;
		}
		
	}
	ShaituDetailBean sdb;
	private void parserList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				sdb = (ShaituDetailBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_SHAITU_DETAIL);
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
			tv_toptitle.setText(sdb.getNickname()+"的晒图");
			tv_nickname.setText(sdb.getNickname());
			tv_title.setText(sdb.getDiet_desc());
			tv_comment.setText(sdb.getComments_num());
			if(sdb.getImages().size()>0){
				iv_bg.setImage(sdb.getImages().get(0).getImage_url(), null, null);
			}
			iv_head.setImage(sdb.getHeadicon(), null, null,"zhuangbei_detail");
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_comment = (ImageView) findViewById(R.id.iv_comment);
		tv_toptitle = (TextView) findViewById(R.id.tv_toptitle);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		iv_bg = (AsyncLoadImageView) findViewById(R.id.iv_bg);
		iv_head = (AsyncLoadImageView) findViewById(R.id.iv_head);
		
		
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_comment.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == iv_share.getId()){
			showShareList();
		}else if(v.getId() == iv_comment.getId()){
			Intent intent = new Intent(LsShaiDetailActivity.this, LsShaiCommentActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("type", "huodong");
			LsShaiDetailActivity.this.startActivity(intent);
		}
	}
	
	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到",
				R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "分享@｛"+sdb.getNickname()+"｝的晒装备，很赞！你们也来看看"+sdb.getShare_url()+"－来自@砾石帮你选 Android版";
							LsWeiboSina.getInstance(LsShaiDetailActivity.this).share(shareText);
							break;
						case 1:
							Bitmap bmp1 = null;
							if(sdb.getImages().size()>0){
								bmp1 = ImageCacheManager.getInstance().getBitmapFromCache(sdb.getImages().get(0).getImage_url());
							}
							String shareWx1Text = sdb.getShare_url();
							String title1 = sdb.getDiet_desc();
							String desc1 = "分享#晒装备#："+sdb.getDiet_desc()+" by "+sdb.getNickname();
							LsWeiboWeixin.getInstance(LsShaiDetailActivity.this).getApi().handleIntent(getIntent(), LsShaiDetailActivity.this);
							LsWeiboWeixin.getInstance(LsShaiDetailActivity.this).share(shareWx1Text,title1,desc1, bmp1, SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Bitmap bmp2 = null;
							if(sdb.getImages().size()>0){
								bmp2 = ImageCacheManager.getInstance().getBitmapFromCache(sdb.getImages().get(0).getImage_url());
							}
							String shareWx2Text = sdb.getShare_url();
							String title2 = sdb.getDiet_desc();
							String desc2 = "分享#晒装备#："+sdb.getDiet_desc()+" by "+sdb.getNickname();
							LsWeiboWeixin.getInstance(LsShaiDetailActivity.this).getApi().handleIntent(getIntent(), LsShaiDetailActivity.this);
							LsWeiboWeixin.getInstance(LsShaiDetailActivity.this).share(shareWx2Text,title2,desc2, bmp2, SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, sdb.getNickname()+"晒装备");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "砾石帮你选上看到@｛"+sdb.getNickname()+"｝的晒装备，很赞！");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, sdb.getShare_url());
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(LsShaiDetailActivity.this, params, new IUiListener() {

								@Override
								public void onCancel() {
									Util2.toastMessage(LsShaiDetailActivity.this, "onCancel: ");
								}

								@Override
								public void onComplete(Object response) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsShaiDetailActivity.this, "onComplete: ");
								}

								@Override
								public void onError(UiError e) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsShaiDetailActivity.this, "onComplete: " + e.errorMessage, "e");
								}

							});
							break;
						case 4:
							String shareWx4Text = "分享@｛"+sdb.getNickname()+"｝的晒装备，很赞！你们也来看看"+sdb.getShare_url()+"－来自@砾石帮你选 Android版" ;
							LsWeiboTencent.getInstance(LsShaiDetailActivity.this).share(shareWx4Text);
						}
					}
				});
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
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
