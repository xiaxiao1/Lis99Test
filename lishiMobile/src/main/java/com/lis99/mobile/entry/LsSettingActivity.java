package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LoginCallBackManager;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;

import java.lang.reflect.Method;

public class LsSettingActivity extends ActivityPattern {

	ImageView iv_back;
	LinearLayout ll_setting_item1, ll_setting_item2;
	RelativeLayout rl_userinfo, rl_share, rl_push, rl_guanzhu, rl_yijian,
			rl_pinglun, rl_huancun, rl_gengxin, rl_tuijian;
	Button bt_tuichu;
	TextView tv_size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_setting);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		getpkginfo("com.lis99.mobile");
	}

	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_setting_item1 = (LinearLayout) findViewById(R.id.ll_setting_item1);
		ll_setting_item2 = (LinearLayout) findViewById(R.id.ll_setting_item2);
		rl_userinfo = (RelativeLayout) findViewById(R.id.rl_userinfo);
		rl_share = (RelativeLayout) findViewById(R.id.rl_share);
		rl_push = (RelativeLayout) findViewById(R.id.rl_push);
		rl_guanzhu = (RelativeLayout) findViewById(R.id.rl_guanzhu);
		rl_yijian = (RelativeLayout) findViewById(R.id.rl_yijian);
		rl_pinglun = (RelativeLayout) findViewById(R.id.rl_pinglun);
		rl_huancun = (RelativeLayout) findViewById(R.id.rl_huancun);
		rl_gengxin = (RelativeLayout) findViewById(R.id.rl_gengxin);
		rl_tuijian = (RelativeLayout) findViewById(R.id.rl_tuijian);
		tv_size = (TextView) findViewById(R.id.tv_size);
		bt_tuichu = (Button) findViewById(R.id.bt_tuichu);
		if (DataManager.getInstance().getUser().getUser_id() != null
				&& !"".equals(DataManager.getInstance().getUser().getUser_id())) {
			ll_setting_item1.setVisibility(View.VISIBLE);
//			ll_setting_item2.setVisibility(View.VISIBLE);
			bt_tuichu.setVisibility(View.VISIBLE);
		} else {
			ll_setting_item1.setVisibility(View.GONE);
//			ll_setting_item2.setVisibility(View.GONE);
			bt_tuichu.setVisibility(View.GONE);

		}
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		rl_userinfo.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		rl_push.setOnClickListener(this);
		rl_guanzhu.setOnClickListener(this);
		rl_yijian.setOnClickListener(this);
		rl_pinglun.setOnClickListener(this);
		rl_huancun.setOnClickListener(this);
		rl_gengxin.setOnClickListener(this);
		rl_tuijian.setOnClickListener(this);
		bt_tuichu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == rl_userinfo.getId()) {
			Intent intent = new Intent(this, LsSettingInfoActivity.class);
			startActivity(intent);
		} else if (v.getId() == rl_share.getId()) {
			Intent intent = new Intent(this, LsSettingShareActivity.class);
			startActivity(intent);
		} else if (v.getId() == rl_push.getId()) {
			Intent intent = new Intent(this, LsSettingPushActivity.class);
			startActivity(intent);
		} else if (v.getId() == rl_guanzhu.getId()) {

		} else if (v.getId() == rl_yijian.getId()) {
			Intent intent = new Intent(this, LsSettingFeedbackActivity.class);
			startActivity(intent);
		} else if (v.getId() == rl_pinglun.getId()) {
			openWebURL(this, "http://apk.hiapk.com/html/2014/02/2349638.html");
		} else if (v.getId() == rl_huancun.getId()) {
			postMessage(POPUP_ALERT, null, "确定要清楚缓存吗？", true, "确定",
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							/*
							 * Intent intent = new Intent(
							 * Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
							 * Uri.fromParts("package", "com.lis99.mobile",
							 * null)); startActivityForResult(intent, 1);
							 * getpkginfo("com.lis99.mobile");
							 */
							DataCleanManager
									.cleanInternalCache(LsSettingActivity.this);
							getpkginfo("com.lis99.mobile");
						}
					}, true, "取消", null);
		} else if (v.getId() == rl_gengxin.getId()) {
			Intent intent = new Intent(this, LsSettingCheckActivity.class);
			startActivity(intent);
		} else if (v.getId() == rl_tuijian.getId()) {
			Intent intent = new Intent(this, LsSettingRecommendActivity.class);
			startActivity(intent);
		} else if (v.getId() == bt_tuichu.getId()) {
//通知接口， 退出登陆
			LoginCallBackManager.getInstance().handler();

			//调用退出登陆接口
			LSRequestManager.getInstance().Logout();
			
			DataManager.getInstance().setLogin_flag(false);
			DataManager.getInstance().setUser(null);
			
			CookieSyncManager cookieSyncMngr =
		            CookieSyncManager.createInstance(this);
		        CookieManager cookieManager = CookieManager.getInstance();
		        cookieManager.removeAllCookie();
		        CookieSyncManager.getInstance().sync();
			
			
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.ACCOUNT, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.PASSWORD, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TOKEN, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TOKEN_ACCOUNT, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TOKEN_PASSWORD, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TENCENT_OPEN_ID, "");
//
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN, "");
//
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, C.TENCENT_ACCESS_TOKEN, "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, "accounttype", "");
//
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, "nickname", "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, "user_id", "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, "headicon", "");
//			SharedPreferencesHelper.putValue(this, C.CONFIG_FILENAME,
//					Context.MODE_PRIVATE, "sn", "");
			

			SharedPreferencesHelper.cleanUserInfo();
			
			
			
			
			finish();
		}
	}

	/*
	 * public static void cleanInternalCache(Context context) {
	 * deleteFilesByDirectory(context.getCacheDir()); }
	 *//**
	 * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	/*
	 * public static void cleanDatabases(Context context) {
	 * deleteFilesByDirectory(new File("/data/data/" + context.getPackageName()
	 * + "/databases")); }
	 *//**
	 * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	/*
	 * public static void cleanSharedPreference(Context context) {
	 * deleteFilesByDirectory(new File("/data/data/" + context.getPackageName()
	 * + "/shared_prefs")); }
	 *//**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */
	/*
	 * public static void cleanDatabaseByName(Context context, String dbName) {
	 * context.deleteDatabase(dbName); }
	 *//**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	/*
	 * public static void cleanFiles(Context context) {
	 * deleteFilesByDirectory(context.getFilesDir()); }
	 *//**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	/*
	 * public static void cleanExternalCache(Context context) { if
	 * (Environment.getExternalStorageState().equals(
	 * Environment.MEDIA_MOUNTED)) {
	 * deleteFilesByDirectory(context.getExternalCacheDir()); } }
	 *//**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	/*
	 * public static void cleanCustomCache(String filePath) {
	 * deleteFilesByDirectory(new File(filePath)); }
	 *//**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	/*
	 * public static void cleanApplicationData(Context context, String...
	 * filepath) { cleanInternalCache(context); cleanExternalCache(context);
	 * cleanDatabases(context); cleanSharedPreference(context);
	 * cleanFiles(context); for (String filePath : filepath) {
	 * cleanCustomCache(filePath); } }
	 *//**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
	 * 
	 * @param directory
	 */
	/*
	 * private static void deleteFilesByDirectory(File directory) { if
	 * (directory != null && directory.exists() && directory.isDirectory()) {
	 * for (File item : directory.listFiles()) { item.delete(); } } }
	 */

	private static final String ATTR_PACKAGE_STATS = "PackageStats";
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String infoString = "";
				PackageStats newPs = msg.getData().getParcelable(
						ATTR_PACKAGE_STATS);
				// if (newPs!=null) {
				// infoString+="应用程序大小: "+formatFileSize(newPs.codeSize);
				// infoString+="\n数据大小: "+formatFileSize(newPs.dataSize);
				infoString += "" + formatFileSize(newPs.cacheSize);
				// }
				tv_size.setText(infoString);
				break;
			default:
				break;
			}
		}
	};

	public void getpkginfo(String pkg) {
		PackageManager pm = getPackageManager();
		try {
			Method getPackageSizeInfo = pm.getClass().getMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			getPackageSizeInfo.invoke(pm, pkg, new PkgSizeObserver());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	class PkgSizeObserver extends IPackageStatsObserver.Stub {
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
			Message msg = mHandler.obtainMessage(1);
			Bundle data = new Bundle();
			data.putParcelable(ATTR_PACKAGE_STATS, pStats);
			msg.setData(data);
			mHandler.sendMessage(msg);

		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

	public static void openWebURL(Context context, String inURL) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
		// it.setClassName("com.android.browser",
		// "com.android.browser.BrowserActivity");
		context.startActivity(it);
	}

}
