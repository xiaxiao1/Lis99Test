/**
 * 
 */
package com.lis99.mobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Administrator
 *
 */
public class CheckNetWorkUtils {
	
	public static boolean netWorkState(Context context){
		
		boolean isWifi=isWifiConnect(context);
		boolean isApn=isApnConnect(context);
		System.out.println("isWifi:"+isWifi);
		System.out.println("isApn:"+isApn);
		//如果两者有一个为true说明当前开启了其中一个,如果两个都为false说明没有网络。
		if(!isWifi&&!isApn){
			return false;
		}
		return true;
	}

	/**获取APN连接状态
	 * @return
	 */
	private static boolean isApnConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(networkInfo!=null){
			return networkInfo.isConnected();
		}
		return false;
	}

	/**获取WIFI连接状态
	 * @return
	 */
	private static boolean isWifiConnect(Context context) {
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(networkInfo!=null){
			return networkInfo.isConnected();
		}
		return false;
	}
}
