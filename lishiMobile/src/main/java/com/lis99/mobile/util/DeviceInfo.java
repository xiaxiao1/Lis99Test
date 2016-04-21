package com.lis99.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.lis99.mobile.club.LSBaseActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class DeviceInfo
{

	public static String IMEI, SDKVERSION, CLIENTVERSION, CHANNELVERSION, MODEL, PLATFORM = "1";//"Android";
	public static int CLIENTVERSIONCODE, SDKVERSIONCODE;
	/**
	 * 	manufacturer	厂商
	 */
	public static String MANUFACTURER;

	public static void getDeviceInfo (Activity a)
	{
		getVersion(a);
		MODEL = android.os.Build.MODEL;
		SDKVERSION = (String)android.os.Build.VERSION.SDK;
		SDKVERSIONCODE = android.os.Build.VERSION.SDK_INT;

		MANUFACTURER = Build.MANUFACTURER;
		
		try
		{
			ApplicationInfo ai = a.getPackageManager().getApplicationInfo(a.getPackageName(), PackageManager.GET_META_DATA);
//			CHANNELVERSION = ai.metaData.getString("UMENG_CHANNEL");
			CHANNELVERSION = ai.metaData.getString("JPUSH_CHANNEL");
			Common.log("JPUSH_CHANNEL="+CHANNELVERSION + "\nJPUSH_APPKEY="+ai.metaData.getString("JPUSH_APPKEY"));
			Common.log("\nJPushToken="+SharedPreferencesHelper.getJPushToken());
//			CHANNELVERSION = "zs360";
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		TelephonyManager tm = (TelephonyManager) a.getSystemService(a.TELEPHONY_SERVICE);
		IMEI = tm.getDeviceId();
		Common.log("MODEL="+MODEL+"=IMEI="+IMEI+"=SDKVERSION="+SDKVERSION + "=SDKVERSIONCODE=" + SDKVERSIONCODE + "=CLIENTVERSION="+CLIENTVERSION + "=CLIENTVERSIONCODE="+CLIENTVERSIONCODE + "=CHANNELVERSION=" + CHANNELVERSION+"\n MANUFACTURER="+MANUFACTURER);
		
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

	/**
	 * 判断wifi连接状态
	 *
	 * @return
	 */
	public boolean isWifiAvailable() {
		ConnectivityManager conMan = (ConnectivityManager) LSBaseActivity.activity
				.getSystemService(LSBaseActivity.activity.CONNECTIVITY_SERVICE);
		NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (NetworkInfo.State.CONNECTED == wifi) {
			return true;
		} else {
			return false;
		}
	}

	public static String getLocalIpAddress() {
		String ipAddress = null;
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface iface : interfaces) {
				if (iface.getDisplayName().equals("eth0")) {
					List<InetAddress> addresses = Collections.list(iface
							.getInetAddresses());
					for (InetAddress address : addresses) {
						if (address instanceof Inet4Address) {
							ipAddress = address.getHostAddress();
						}
					}
				} else if (iface.getDisplayName().equals("wlan0")) {
					List<InetAddress> addresses = Collections.list(iface
							.getInetAddresses());
					for (InetAddress address : addresses) {
						if (address instanceof Inet4Address) {
							ipAddress = address.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipAddress;
	}


//	获取本机WIFI
	private String getWifiIpAddress() {
		WifiManager wifiManager = (WifiManager) LSBaseActivity.activity.getSystemService(LSBaseActivity.activity.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAddress = wifiInfo.getIpAddress();

		//返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d",
				(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
	}

//	3G网络IP
	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * .获取手机MAC地址
	 * 只有手机开启wifi才能获取到mac地址
	 */
	public static String getMacAddress(){
		String result = "";
		WifiManager wifiManager = (WifiManager) LSBaseActivity.activity.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}






}
