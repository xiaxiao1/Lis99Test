package com.lis99.mobile.util;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class DeviceInfo
{

	public static String IMEI, SDKVERSION, CLIENTVERSION, CHANNELVERSION, MODEL;
	public static int CLIENTVERSIONCODE, SDKVERSIONCODE;
	public static void getDeviceInfo (Activity a)
	{
		getVersion(a);
		MODEL = android.os.Build.MODEL;
		SDKVERSION = (String)android.os.Build.VERSION.SDK;
		SDKVERSIONCODE = android.os.Build.VERSION.SDK_INT;
		
		try
		{
			ApplicationInfo ai = a.getPackageManager().getApplicationInfo(a.getPackageName(), PackageManager.GET_META_DATA);
			CHANNELVERSION = ai.metaData.getString("UMENG_CHANNEL");
//			CHANNELVERSION = "zs360";
		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		TelephonyManager tm = (TelephonyManager) a.getSystemService(a.TELEPHONY_SERVICE);  
		IMEI = tm.getDeviceId();
		Common.log("MODEL="+MODEL+"=IMEI="+IMEI+"=SDKVERSION="+SDKVERSION + "=SDKVERSIONCODE=" + SDKVERSIONCODE + "=CLIENTVERSION="+CLIENTVERSION + "=CLIENTVERSIONCODE="+CLIENTVERSIONCODE + "=CHANNELVERSION=" + CHANNELVERSION);
		
	}
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	private static void getVersion(Activity a) {
	    try {
	        PackageManager manager = a.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(a.getPackageName(), 0);
	        CLIENTVERSION = info.versionName;
	        CLIENTVERSIONCODE = info.versionCode;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
