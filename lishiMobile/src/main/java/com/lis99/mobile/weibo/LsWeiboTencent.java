package com.lis99.mobile.weibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.lis99.mobile.QiangActivity;
import com.tencent.weibo.sdk.android.api.WeiboAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;

public class LsWeiboTencent {


	//私有的默认构造子  
    private LsWeiboTencent() {} 
    //注意，这里没有final      
    private static LsWeiboTencent single=null;
    
    private static Context context;
    
    private String shareContent;
    private Bitmap shareBitmap;
    static boolean b=false;
    private String accessToken;// 用户访问令牌
    private WeiboAPI weiboAPI;//微博相关API
    private String requestFormat = "json";
    
    //静态工厂方法   
    public synchronized  static LsWeiboTencent getInstance(Context ct) {  
    	context = ct;
         if (single == null) {    
             single = new LsWeiboTencent();
         }
        return single;  
    }
    
    public void share(String shareText){
    	share(shareText,null);
    }
    
	
	public void share(String shareText,Bitmap bitmap){
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lishi99.broadcast.tencentauth");
        context.registerReceiver(new MyBroadcastReciver(), intentFilter);
		shareContent = shareText;
		shareBitmap = bitmap;
		accessToken = Util.getSharePersistent(context,"ACCESS_TOKEN");
		if (accessToken == null || "".equals(accessToken)) {
			long appid = Long.valueOf(Util.getConfig().getProperty("APP_KEY"));
			String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
			auth(appid, app_secket);
		}else{
			AccountModel account = new AccountModel(accessToken);
			weiboAPI = new WeiboAPI(account);
			if(shareBitmap == null){
				weiboAPI.addWeibo(context, shareText, requestFormat, 0d, 0d, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
			}else{
				weiboAPI.addPic(context, shareText, requestFormat, 0d, 0d, bitmap, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
			}
						
		}
	}
	public void shares(String shareText){
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lishi99.broadcast.tencentauth");
        context.registerReceiver(new MyBroadcastReciver(), intentFilter);
		shareContent = shareText;
		shareBitmap = null;
		accessToken = Util.getSharePersistent(context,"ACCESS_TOKEN");
		if (accessToken == null || "".equals(accessToken)) {
			long appid = Long.valueOf(Util.getConfig().getProperty("APP_KEY"));
			String app_secket = Util.getConfig().getProperty("APP_KEY_SEC");
			auth(appid, app_secket);
		}else{
			AccountModel account = new AccountModel(accessToken);
			weiboAPI = new WeiboAPI(account);
			if(shareBitmap == null){
				weiboAPI.addWeibo(context, shareText, requestFormat, 0d, 0d, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
			}else{
				weiboAPI.addPic(context, shareText, requestFormat, 0d, 0d, shareBitmap, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
			}
						
		}
	}
	
	private HttpCallback mCallBack = new HttpCallback() {
		@Override
		public void onResult(Object object) {
			ModelResult result = (ModelResult) object;
			if(result!=null && result.isSuccess()){
				Toast.makeText(context,
						"分享成功", Toast.LENGTH_SHORT)
						.show();
				QiangActivity.webView1.loadUrl("javascript:shareCallback('tweibo')");
			}else{
				Toast.makeText(context,
						"分享失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};//回调函数
	
	private void auth(long appid, String app_secket) {
		//注册当前应用的appid和appkeysec，并指定一个OnAuthListener
		//OnAuthListener在授权过程中实施监听
		AuthHelper.register(context, appid, app_secket, new OnAuthListener() {

			//如果当前设备没有安装腾讯微博客户端，走这里
			@Override
			public void onWeiBoNotInstalled() {
				AuthHelper.unregister(context);
				Intent i = new Intent(context,Authorize.class);
				context.startActivity(i);
			}

			//如果当前设备没安装指定版本的微博客户端，走这里
			@Override
			public void onWeiboVersionMisMatch() {
				AuthHelper.unregister(context);
				Intent i = new Intent(context,Authorize.class);
				context.startActivity(i);
			}

			//如果授权失败，走这里
			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(context, "分享失败", 1000)
						.show();
				AuthHelper.unregister(context);
			}

			//授权成功，走这里
			//授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
			//在这里，存放到了applicationcontext中
			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				
				Util.saveSharePersistent(context, "ACCESS_TOKEN", token.accessToken);
				Util.saveSharePersistent(context, "EXPIRES_IN", String.valueOf(token.expiresIn));
				Util.saveSharePersistent(context, "OPEN_ID", token.openID);
//				Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
				Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
//				Util.saveSharePersistent(context, "NAME", name);
//				Util.saveSharePersistent(context, "NICK", name);
				Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig().getProperty("APP_KEY"));
				Util.saveSharePersistent(context, "AUTHORIZETIME",
						String.valueOf(System.currentTimeMillis() / 1000l));
				accessToken = token.accessToken;
				AccountModel account = new AccountModel(accessToken);
				weiboAPI = new WeiboAPI(account);
				if(shareBitmap == null){
					weiboAPI.addWeibo(context, shareContent, requestFormat, 0d, 0d, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
				}else{
					weiboAPI.addPic(context, shareContent, requestFormat, 0d, 0d, shareBitmap, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
				}
			}
		});

		AuthHelper.auth(context, "");
	}
	
	private class MyBroadcastReciver extends BroadcastReceiver {  
		@Override
		public void onReceive(Context context, Intent intent) {
		   String action = intent.getAction();
		   if(action.equals("com.lishi99.broadcast.tencentauth")) {
			   accessToken = Util.getSharePersistent(context,"ACCESS_TOKEN");
				AccountModel account = new AccountModel(accessToken);
				weiboAPI = new WeiboAPI(account);
				if(shareBitmap == null){
					weiboAPI.addWeibo(context, shareContent, requestFormat, 0d, 0d, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
				}else{
					weiboAPI.addPic(context, shareContent, requestFormat, 0d, 0d, shareBitmap, 0, 0, mCallBack, null, BaseVO.TYPE_JSON);
				}
		    //在结束时可取消广播
		    context.unregisterReceiver(this);
		   }
		  }
	}
	
}
