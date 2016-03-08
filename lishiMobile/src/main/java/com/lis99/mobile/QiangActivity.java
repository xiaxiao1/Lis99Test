package com.lis99.mobile;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.ControllShake.OnShakeListener;
import com.lis99.mobile.entry.ActivityPattern;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;

import java.util.ArrayList;

public class QiangActivity extends ActivityPattern implements IWXAPIEventHandler{
public static WebView webView1;
private ImageView iv_home;
private TextView title;
Dialog dialogView = null;
private Tencent tencent;
private LinearLayout ll_gb;
private IWXAPI api;
private int weixin =1;


IWeiboShareAPI mWeiboShareAPI;
private float totalShake ;
private SoundPool sp;//声明一个SoundPool
private int music;//定义一个整型用load（）；来设置suondID
private String murl="http://m.lis99.com/qiangyouhui";
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_qiang);

		StatusUtil.setStatusBar(this);
	initWeibo(savedInstanceState);
	setListener();
	initData();
	ControllShake mShaker = new ControllShake(this);

	mShaker.setOnShakeListener(new OnShakeListener() {
		public void onShake() {
			// action while shaking
			if(murl.equals("http://m.lis99.com/qiangyouhui/activity")){
			sp.play(music, 1, 1, 0, 0, 1);
			webView1.loadUrl("javascript:shake()");
			onVibrator();
			}
		}
	});
	}
	private void initWeibo(Bundle savedInstanceState) {
		 // 创建微博 SDK 接口实例
    mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
    if (savedInstanceState != null) {
        mWeiboShareAPI.handleWeiboResponse(getIntent(), wh);
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
			// TODO Auto-generated method stub
			super.onNewIntent(intent);
			 mWeiboShareAPI.handleWeiboResponse(intent, wh);
				setIntent(intent);
		        api.handleIntent(intent, this);

		}
	@SuppressLint("JavascriptInterface")
	public void setListener() {
		// TODO Auto-generated method stub
		webView1 =(WebView)findViewById(R.id.webView1);
		iv_home =(ImageView)findViewById(R.id.iv_home);
		ll_gb=(LinearLayout)findViewById(R.id.ll_gb);
		title =(TextView)findViewById(R.id.title);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		WebSettings setting = webView1.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setDefaultTextEncodingName("GBK");//设置字符编码
		webView1.addJavascriptInterface(new WebAppInterface(QiangActivity.this), "share");

		webView1.loadUrl("http://m.lis99.com/qiangyouhui");
		initData();
	}


	public void initData() {


		ll_gb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dia();
			}
		});
		sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.shake_sound, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级

		iv_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(murl.equals("http://m.lis99.com/qiangyouhui/")|murl.equals("http://m.lis99.com/qiangyouhui")){
					finish();
				}else{
					webView1.loadUrl("javascript:backUrl()");
				}
			}
		});
		// TODO Auto-generated method stub
		webView1.setWebChromeClient(new WebChromeClient() {

			/*
			 * 此处覆盖的是javascript中的alert方法。当网页需要弹出alert窗口时，会执行onJsAlert中的方法
			 * 网页自身的alert方法不会被调用。
			 */
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 此处覆盖的是javascript中的confirm方法。当网页需要弹出confirm窗口时，会执行onJsConfirm中的方法
			 * 网页自身的confirm方法不会被调用。
			 */
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}

			/*
			 * 如果页面被强制关闭,弹窗提示：是否确定离开？ 点击确定 保存数据离开，点击取消，停留在当前页面
			 */
			public boolean onJsBeforeUnload(WebView view, String url,
					String message, JsResult result) {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
				result.confirm();
				return true;
			}
			 public void onReceivedTitle(WebView view, String title1) {
	                super.onReceivedTitle(view, title1);
	                if(title1.equals("收银台-高端版-支付宝")){
	                	ll_gb.setVisibility(View.VISIBLE);
	                	iv_home.setVisibility(View.INVISIBLE);
	                }else{
	                	iv_home.setVisibility(View.VISIBLE);
	                	ll_gb.setVisibility(View.INVISIBLE);
	                }
	                title.setText(title1);
	            }

		});
		webView1.setWebViewClient(new WebViewClient() {
			/*
			 * 点击页面的某条链接进行跳转的话，会启动系统的默认浏览器进行加载，调出了我们本身的应用
			 * 因此，要在shouldOverrideUrlLoading方法中
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用当前的WebView加载页面

				view.loadUrl(url);
				murl=url;
				if(!murl.equals("http://m.lis99.com/qiangyouhui/") && !murl.equals("http://m.lis99.com/qiangyouhui")){
					iv_home.setBackgroundResource(R.drawable.back);

					android.view.ViewGroup.LayoutParams params = iv_home.getLayoutParams();
				    params.height=35;
				    params.width =30;

				    iv_home.setLayoutParams(params);

				}else{
					iv_home.setBackgroundResource(R.drawable.ls_page_home_icon);

					android.view.ViewGroup.LayoutParams params = iv_home.getLayoutParams();
				    params.height=80;
				    params.width =80;
				    iv_home.setLayoutParams(params);
				}
				return true;
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageStarted(WebView view, String url,
					Bitmap favicon) {

				super.onPageStarted(view, url, favicon);
			}

			/*
			 * 网页加载完毕(仅指主页，不包括图片)
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);


			}

			/*
			 * 加载页面资源
			 */
			@Override
			public void onLoadResource(WebView view, String url) {

				super.onLoadResource(view, url);

			}

			/*
			 * 错误提示
			 */
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				super.onReceivedError(view, errorCode, description,
						failingUrl);
			}
		});

	}



	@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode==KeyEvent.KEYCODE_BACK){
				if(murl.equals("http://m.lis99.com/qiangyouhui/")|murl.equals("http://m.lis99.com/qiangyouhui")){
					finish();
				}else{
					webView1.loadUrl("javascript:backUrl()");
				}


			}
			return super.onKeyDown(keyCode, event);
		}

	 private void onVibrator() {
		  Vibrator vibrator = (Vibrator) QiangActivity.this
		    .getSystemService(Context.VIBRATOR_SERVICE);
		  if (vibrator == null) {
		   Vibrator localVibrator = (Vibrator) QiangActivity.this.getApplicationContext()
		     .getSystemService("vibrator");
		   vibrator = localVibrator;
		  }
		  vibrator.vibrate(100L);
		 }



	public class WebAppInterface {
		private Activity activity;

		public WebAppInterface(Activity activity) {
			this.activity = activity;
		}

		public void shareFunction() {
			showShareList();
		}

	}


		  private void showShareList() {

				postMessage(5, "分享到", R.array.share_items,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 0:

									String shareText = "＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！"+murl;

									LsWeiboSina.getInstance(QiangActivity.this)
											.shares(shareText,null,1);

									break;
								case 1:

									Resources res = getResources();
									Bitmap bmp1 = BitmapFactory.decodeResource(res,
											R.drawable.icon_ls);


									String shareWx1Text = "＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！"+murl;

									String title1 = "";
									String desc1 = "";
									LsWeiboWeixin
											.getInstance(QiangActivity.this)
											.getApi()
											.handleIntent(getIntent(),
													QiangActivity.this);
									LsWeiboWeixin.getInstance(QiangActivity.this)
											.share(shareWx1Text, title1, desc1, bmp1,
													SendMessageToWX.Req.WXSceneSession);
									weixin=2;
									break;
								case 2:
									weixin=3;
									Resources res1 = getResources();
									Bitmap bmp2 = BitmapFactory.decodeResource(res1,
											R.drawable.icon_ls);
									//Bitmap bmp4 = ImageCacheManager.getInstance().getBitmapFromCache(shopDetailItem.getPic().get(0).getTh_pic());
									String shareWx2Text = "＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！"+murl;
									String title2 = "";
									String desc2 = "";
									LsWeiboWeixin
											.getInstance(QiangActivity.this)
											.getApi()
											.handleIntent(getIntent(),
													QiangActivity.this);
									LsWeiboWeixin
											.getInstance(QiangActivity.this)
											.share(shareWx2Text, title2, desc2, bmp2,
													SendMessageToWX.Req.WXSceneTimeline);

									break;
								case 3:

									final Bundle params = new Bundle();
									params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
											"摇一摇");
									params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
											"＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！");
									params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
											murl);
									ArrayList<String> imageUrls = new ArrayList<String>();
									params.putStringArrayList(
											QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
									// 支持传多个imageUrl
									tencent.shareToQzone(QiangActivity.this,
											params, new IUiListener() {

												@Override
												public void onCancel() {
													Util2.toastMessage(
															QiangActivity.this,
															"onCancel: ");
												}

												@Override
												public void onComplete(
														Object response) {
													// TODO Auto-generated method stub
													Util2.toastMessage(
															QiangActivity.this,
															"onComplete: ");
													webView1.loadUrl("javascript:shareCallback('qzone')");
												}

												@Override
												public void onError(UiError e) {
													// TODO Auto-generated method stub
													Util2.toastMessage(
															QiangActivity.this,
															"onComplete: "
																	+ e.errorMessage,
															"e");
												}

											});
									break;

								case 4:

									String shareWx4Text = "＃摇一摇，0元抢＃砾石帮你选APP抢优惠活动开始啦！都是高大上的装备，都是神一样的价格！手机摇一次，价格降一次，你敢摇我就敢降！记住，下手一定要快哟！"+murl;
									LsWeiboTencent.getInstance(QiangActivity.this)
											.shares(shareWx4Text);


									break;
								}
							}
						});
				if(LsWeiboSina.ret==1){
					webView1.loadUrl("javascript:shareCallback('weibo')");
				}
			}
		  HttpCallback mCallBack = new HttpCallback() {
				@Override
				public void onResult(Object object) {
					ModelResult result = (ModelResult) object;
					if(result!=null && result.isSuccess()){
						Toast.makeText(QiangActivity.this,
								"分享成功", Toast.LENGTH_SHORT)
								.show();
						webView1.loadUrl("javascript:shareCallback('tweibo')");
					}else{
						Toast.makeText(QiangActivity.this,
								"分享失败", Toast.LENGTH_SHORT)
								.show();
					}
				}
			};//回调函数
			private Response wh = new Response() {
				@Override
				public void onResponse(BaseResponse arg0) {
					 switch (arg0.errCode) {
				        case WBConstants.ErrorCode.ERR_OK:
				            Toast.makeText(QiangActivity.this, "分享成功", Toast.LENGTH_LONG).show();
				            webView1.loadUrl("javascript:shareCallback('weibo')");
				            break;
				        case WBConstants.ErrorCode.ERR_CANCEL:
				            Toast.makeText(QiangActivity.this, "取消分享", Toast.LENGTH_LONG).show();
				            break;
				        case WBConstants.ErrorCode.ERR_FAIL:
				            Toast.makeText(QiangActivity.this,
				                    "分享失败" + "Error Message: " + arg0.errMsg,
				                    Toast.LENGTH_LONG).show();
				            break;
				        }
				}
			};
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
					if(weixin==3){
					webView1.loadUrl("javascript:shareCallback('weixin')");}
					if(weixin==2){
					webView1.loadUrl("javascript:shareCallback('wxfriend ')");}
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


		protected void showWaiting(Context context) {
			dialogView = new Dialog(context, R.style.theme_dialog_alert);
			dialogView.setContentView(R.layout.window_layout);
			dialogView.setCancelable(true);
			dialogView
					.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							hideDialog();
						}
					});
			dialogView.show();
		}


		protected void hideDialog() {
			if (dialogView != null) {
				dialogView.dismiss();
				dialogView = null;
			}
		}
		protected boolean isShowDialog(){
			   if(dialogView != null&&dialogView.isShowing()){
				   return true;
			   }else{
				   return false;
			   }
			}
//		public void vawe(){
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					sm = (SensorManager) QiangActivity.this.getSystemService(Context.SENSOR_SERVICE);
//					  int sensorType = android.hardware.Sensor.TYPE_ACCELEROMETER;
//					  sm.registerListener(mySensorEventListener, sm
//					    .getDefaultSensor(sensorType),
//					    SensorManager.SENSOR_DELAY_FASTEST);
//					  try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//			});
//
//		}
	public void dia(){
		new AlertDialog.Builder(this).setTitle("您的订单尚未支付成功").setNegativeButton(
				 "知道了", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						webView1.loadUrl("http://m.lis99.com/qiangyouhui/confirmOrder/");
					}
				}).show();
	}
}

