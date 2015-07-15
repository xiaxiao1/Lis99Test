package com.lis99.mobile.entry.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.lis99.mobile.club.BaseConfig;
import com.lis99.mobile.util.ImageLoaderOption;
import com.lis99.mobile.util.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

public class DemoApplication extends Application {

	private static DemoApplication mInstance = null;
	public boolean m_bKeyRight = true;
	private BDLocation location;
	public int versionCode;
	public String versionName;
	

	public BDLocation getLocation() {
		return location;
	}

	public void setLocation(BDLocation location) {
		this.location = location;
	}

	/**
	 * 陈俊key: FKuW26myex26ePwHMe3kb1Vt V9sroLzTO6btKdauS9emWGZz 崔晓华key:
	 * WF5wVTiwIG8p0m8IcAqwg9Si
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SDKInitializer.initialize(this);
		MobclickAgent.openActivityDurationTrack(true);
		getPackageIngo();
		BaseConfig.init(this);
		
//		DisplayImageOptions opt = new DisplayImageOptions.Builder()
//		.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();

//		DemoApplication.initImageLoader(this, opt);
//		initImageLoader(this, opt);

		//	设置ImageLoader 属性
		ImageLoaderOption.init(this);

		if ( PushManager.isPush )
		{
	//		推送
			PushManager.getInstance().RegisterPush(this);
		}
		//崩溃记录本地
		CrashHandler1.getInstance().init(getApplicationContext());
	}

	private void getPackageIngo() {
		PackageManager packageManager = getPackageManager();
		String packageName = getPackageName();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Bitmap bitmap;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public static DemoApplication getInstance() {
		return mInstance;
	}

	public static void initImageLoader(Context context, DisplayImageOptions opt) {
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context).threadPriority(Thread.NORM_PRIORITY - 4)
//				.denyCacheImageMultipleSizesInMemory()
//				.defaultDisplayImageOptions(opt).threadPoolSize(3)
//				.memoryCacheExtraOptions(480, 800)
//				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//				.memoryCacheSize(2 * 1024 * 1024)
//				.discCacheSize(10 * 1024 * 1024)
//				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//				.tasksProcessingOrder(QueueProcessingType.LIFO) // Not
//				.build();
//		ImageLoader.getInstance().init(config);
	}
}