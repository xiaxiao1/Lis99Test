package com.lis99.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lis99.mobile.club.model.PushModel;
import com.lis99.mobile.util.push.PushBase;

import java.util.Set;

/**
 * 推送类
 * 
 * @author Administrator
 * 
 */
public class PushManager
{

	private static PushManager Instance;
	// =========推送=====
//	private PushAgent mPushAgent;
//
//	public static IUmengRegisterCallback mRegisterCallback;
//
//	public static IUmengUnregisterCallback mUnregisterCallback;

//	public Handler handler = new Handler();

	public static final String TAG = "pushModel";
//	push开关
	public static boolean isPush = true;

	public PushBase push;

	private PushManagerF pushF;

	public PushManager ()
	{
		if ( push == null )
		{
//			push = new JPush();
			pushF = new PushManagerJPush();
			push = pushF.create();
		}
	}

	public static PushManager getInstance()
	{
		if (null == Instance)
			Instance = new PushManager();
		return Instance;
	}

	// 获取Push到的信息
	public PushModel getPushModel(Intent intent)
	{
		PushModel model = (PushModel) intent.getSerializableExtra(TAG);
		return model;
	}

	public void RegisterPush  ( Context c )
	{
		push.init(c);
	}

	public PushBase getPushInstance ()
	{
		return push;
	}

////	注册push
//	public void RegisterPush(final Context c)
//	{
//		mPushAgent = PushAgent.getInstance(c);
//		mPushAgent.setDebugMode(true);
//
//		/**
//		 * 该Handler是在IntentService中被调用，故 1.
//		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 2.
//		 * IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
//		 * 或者可以直vice
//		 * */
//		UmengMessageHandler messageHandler = new UmengMessageHandler()
//		{
//			@Override
//			public void dealWithCustomMessage(final Context context,
//					final UMessage msg)
//			{
//				new Handler(c.getMainLooper()).post(new Runnable()
//				{
//
//					@Override
//					public void run()
//					{
//						// TODO Auto-generated method stub
//						UTrack.getInstance(c.getApplicationContext())
//								.trackMsgClick(msg);
//						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
//								.show();
//					}
//				});
//			}
//
//			@Override
//			public Notification getNotification(Context context, UMessage msg)
//			{
//				switch (msg.builder_id)
//				{
//					case 1:
//						NotificationCompat.Builder builder = new NotificationCompat.Builder(
//								context);
//						RemoteViews myNotificationView = new RemoteViews(
//								context.getPackageName(),
//								R.layout.notification_view);
//						myNotificationView.setTextViewText(
//								R.id.notification_title, msg.title);
//						myNotificationView.setTextViewText(
//								R.id.notification_text, msg.text);
//						myNotificationView.setImageViewBitmap(
//								R.id.notification_large_icon,
//								getLargeIcon(context, msg));
//						myNotificationView.setImageViewResource(
//								R.id.notification_small_icon,
//								getSmallIconId(context, msg));
//						builder.setContent(myNotificationView);
//						builder.setAutoCancel(true);
//						Notification mNotification = builder.build();
//						// 由于Android
//						// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//						mNotification.contentView = myNotificationView;
//						return mNotification;
//					default:
//						// 默认为0，若填写的builder_id并不存在，也使用默认。
//						return super.getNotification(context, msg);
//				}
//			}
//		};
//		mPushAgent.setMessageHandler(messageHandler);
//
//		/**
//		 * 该Handler是在BroadcastReceiver中被调用，故
//		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//		 * */
//		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler()
//		{
//			// 自定义
//			@Override
//			public void dealWithCustomAction(Context context, UMessage msg)
//			{
////				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//				Common.log("push==" + msg.custom);
//
//				PushModel model = new PushModel();
//				model = (PushModel) ParserUtil.getGosnParser(msg.custom, model);
//				if (model.type == -1)
//				{
//					return;
//				}
//				Intent intent = new Intent();
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra(TAG, model);
//				intent.setClass(c.getApplicationContext(),
//						LsStartupActivity.class);
//				c.startActivity(intent);
//
//			}
//
//			// 启动应用
//			@Override
//			public void launchApp(Context arg0, UMessage arg1)
//			{
//				// TODO Auto-generated method stub
//				super.launchApp(arg0, arg1);
//			}
//
//			// 打开指定Activity
//			@Override
//			public void openActivity(Context arg0, UMessage arg1)
//			{
//				// TODO Auto-generated method stub
//				super.openActivity(arg0, arg1);
//			}
//
//			// 打开WebView
//			@Override
//			public void openUrl(Context arg0, UMessage arg1)
//			{
//				// TODO Auto-generated method stub
//				super.openUrl(arg0, arg1);
//			}
//
//		};
//		mPushAgent.setNotificationClickHandler(notificationClickHandler);
//
//		// ======Register====
//		mRegisterCallback = new IUmengRegisterCallback()
//		{
//
//			@Override
//			public void onRegistered(String registrationId)
//			{
//				// TODO Auto-generated method stub
//				updateStatus();
//			}
//
//		};
//		mPushAgent.setRegisterCallback(mRegisterCallback);
//		// ======Unregister=====
//		mUnregisterCallback = new IUmengUnregisterCallback()
//		{
//
//			@Override
//			public void onUnregistered(String registrationId)
//			{
//				// TODO Auto-generated method stub
//				updateStatus();
//			}
//		};
//		mPushAgent.setUnregisterCallback(mUnregisterCallback);
//	}

//	/** 在activity界面注册， 不知道为什么文档上面说要每个activity都要注册 */
//	public void initViews(Activity a)
//	{
//		mPushAgent = PushAgent.getInstance(a);
//		mPushAgent.onAppStart();
//		mPushAgent.enable(mRegisterCallback);
//		updateStatus();
//	}

	public void onAppStart(Activity a)
	{
//		mPushAgent = PushAgent.getInstance(a);
//		mPushAgent.onAppStart();
	}

	private void printKeyValue(Activity a)
	{
		// 获取自定义参数
		Bundle bun = a.getIntent().getExtras();
		if (bun != null)
		{
			Set<String> keySet = bun.keySet();
			for (String key : keySet)
			{
				String value = bun.getString(key);
				Common.log(key + ":" + value);
			}
		}

	}

	private void updateStatus()
	{
		// String pkgName = a.getApplicationContext().getPackageName();
//		String info = String.format(
//				"enabled:%s  isRegistered:%s  DeviceToken:%s",
//				mPushAgent.isEnabled(), mPushAgent.isRegistered(),
//				mPushAgent.getRegistrationId());
//		// 设置Token
//		Token = mPushAgent.getRegistrationId();
//		SharedPreferencesHelper.savePushToken(Token);
//		Common.log("=======================push token==="+SharedPreferencesHelper.getPushToken());
//		Common.log("push Device info=" + info);
	}
}
