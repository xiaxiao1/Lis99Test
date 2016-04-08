package com.lis99.mobile.weibo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.AccessTokenKeeper;
import com.lis99.mobile.entry.LsShakesActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.ShareManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;

public class LsWeiboSina{

	//私有的默认构造子  
    private LsWeiboSina() {} 
    //注意，这里没有final      
    private static LsWeiboSina single=null;
    private static Activity activity;
//    private static Context context;
    int page=0;
//    private static WeiboAuth mWeiboAuth;
    private StatusesAPI mStatusesAPI;
    private String shareContent;
    private Bitmap shareBitmap;
    public static int ret=0;
    
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    
    private static WeiboAuth mWeiboAuth;

	private CallBack callBack;
    
    //静态工厂方法   
    public synchronized  static LsWeiboSina getInstance(Activity ct) {  
    	activity = ct;
		mWeiboAuth = new WeiboAuth(activity, C.SINA_APP_KEY,C.SINA_REDIRECT_URL, C.SINA_SCOPE);
         if (single == null) {    
             single = new LsWeiboSina();
         }
        return single;  
    }
    
    public void share(String shareText){
    	share(shareText,null, null);
    }
	
	public void share(String shareText,Bitmap bitmap, CallBack callBack ){
		this.callBack = callBack;
		shareContent = shareText;
		shareBitmap = bitmap;
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
		if(accessToken!=null && accessToken.isSessionValid()){
			send(accessToken);
		}else{
//			mSsoHandler = new SsoHandler(activity, mWeiboAuth);
//            mSsoHandler.authorize(new AuthListener());
			mWeiboAuth.anthorize(new AuthListener());
		}
	}
	public void shares(String shareText,Bitmap bitmap,int i){
		shareContent = shareText;
		page=i;
		
		shareBitmap = bitmap;
		Common.log("activity="+activity.getPackageName());
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
		
		if(accessToken!=null && accessToken.isSessionValid()){
			send(accessToken);
		}else{
			mWeiboAuth.anthorize(new AuthListener());
		}
	}
	
	private void send(Oauth2AccessToken token) {
		mStatusesAPI = new StatusesAPI(token);
		if(shareBitmap!=null){
			mStatusesAPI.upload(shareContent, shareBitmap, null, null, new ShareListener());
		}else{
			 mStatusesAPI.update(shareContent, null, null, new ShareListener());		
		}
	}
	
	 class ShareListener implements RequestListener{

		@Override
		public void onComplete(String arg0) {
			if ( callBack != null )
			{
				MyTask task = new MyTask();
				task.setresult("SINA");
				callBack.handler(task);
			}
			callBack = null;
			Toast.makeText(activity, "分享成功", Toast.LENGTH_LONG).show();

			LSScoreManager.getInstance().sendScore(LSScoreManager.shareweibo, ShareManager.topicid);

			if(page==1){
				LsShakesActivity.webview.loadUrl("javascript:shareCallback('weibo')");
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			Toast.makeText(activity, "分享失敗", Toast.LENGTH_LONG).show();	
		}
		
	}
	
	class AuthListener implements WeiboAuthListener {



        @Override
        public void onComplete(Bundle values) {
        	Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        	Common.log("token="+mAccessToken.getToken());
            if (mAccessToken.isSessionValid()) {
            	Common.log("token="+mAccessToken.getToken());
                AccessTokenKeeper.writeAccessToken(activity, mAccessToken);
                send(mAccessToken);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                Common.log("regist error = "+ message);
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(activity, 
                    "取消分享", Toast.LENGTH_LONG).show();
            Common.log("regist error = 取消分享");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(activity, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            Common.log("regist error = "+ e.getMessage());
        }
    }

	public WeiboAuth getWeiboAuth() {
		return mWeiboAuth;
	}

}