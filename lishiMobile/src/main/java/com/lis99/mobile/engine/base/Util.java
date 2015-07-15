package com.lis99.mobile.engine.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 项目名称：framework 类名称：Util 类描述： 创建人：杨琳 创建时间�?013-9-10 下午4:34:54 修改人：杨琳
 * 修改时间�?013-9-10 下午4:34:54 修改备注�?
 * 
 * @version
 * 
 */
public class Util {
	public static boolean isNet(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			// ��ǰ���粻����
			
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Gps�Ƿ�� ��Ҫ<uses-permission
	 * android:name="android.permission.ACCESS_FINE_LOCATION" />Ȩ��
	 * 
	 * @param context
	 *            the context
	 * @return true, if is gps enabled
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * wifi�Ƿ��.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is wifi enabled
	 */
	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * �жϵ�ǰ�����Ƿ���wifi����.
	 * 
	 * @param context
	 *            the context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * �жϵ�ǰ�����Ƿ���3G����.
	 * 
	 * @param context
	 *            the context
	 * @return boolean
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public static String getCurrentDate(String format) {
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;

	}

	/**
	 * ��װӦ��
	 */
	public static void install(Context context, String filePath) {
		chmod("777", filePath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * ��ȡȨ��
	 * 
	 * @param permission
	 *            Ȩ��
	 * @param path
	 *            ·��
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��4�жϷ����Ƿ�����.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param className
	 *            �жϵķ�������
	 * @return true ������ false ��������
	 */
	public static boolean isServiceRunning(Context ctx, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * ֹͣ����.
	 * 
	 * @param ctx
	 *            the ctx
	 * @param className
	 *            the class name
	 * @return true, if successful
	 */
	public static boolean stopRunningService(Context ctx, String className) {
		Intent intent_service = null;
		boolean ret = false;
		try {
			intent_service = new Intent(ctx, Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (intent_service != null) {
			ret = ctx.stopService(intent_service);
		}
		return ret;
	}

	/**
	 * ִ���߳�
	 * 
	 * @param runnable
	 *            �߳�
	 */
	public static void threadExecute(Runnable runnable) {
		ThreadPoolFactory threadPoolFactory = ThreadPoolFactory.getInstance();
		threadPoolFactory.execute(runnable);
	}

	/**
	 * ��ȡ��Ļ����ܶ�
	 * 
	 * @param activity
	 * @param flag
	 *            1����
	 * @return
	 */
	public static int getScreenWH(Activity activity, int flag) {
		DisplayMetrics dm = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getMetrics(dm);
		switch (flag) {
		case 0:
			return display.getHeight();
		case 1:
			return display.getWidth();
		case 2:
			return dm.densityDpi;
		}
		return 3;

	}
}
