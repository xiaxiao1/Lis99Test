package com.lis99.mobile.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/*******************************************
 * @ClassName: RequestParamUtil 
 * @Description: 请求参数 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-26 下午4:46:27 
 *  
 ******************************************/
public class RequestParamUtil {
	
	private final String TAG = "RequestParamUtil";

	private static RequestParamUtil mInstance;
	/** 手机操作系统 */
	private String os = "android";
	/** 手机操作系统版本号 */
	private String osver = Build.VERSION.RELEASE;
	/** 后台保存的session值 */
	private String session = "";
	/** 是否刷机 */
	private String root = "0";
	/** 设备类型 */
	private String device = android.os.Build.MANUFACTURER;
	/** 支持的协议编码 */
	private String accept = "0";
	/** 网络类型 */
	private String network = "";
	private String deviceId = "";
	/** cookie */
	private String cookie = "";
	/** Wlan物理地址 */
	private String wlan = "";
	private String resolution = "800*480";
	private String deviceX = "0";
	private String deviceY = "0";
	
	
	public String getSession() {
		return session;
	}

	public String getWlan() {
		return wlan;
	}

	private Context context;

	
	public String getDeviceX() {
		return deviceX;
	}

	public void setDeviceX(String deviceX) {
		this.deviceX = deviceX;
	}

	public String getDeviceY() {
		return deviceY;
	}

	public void setDeviceY(String deviceY) {
		this.deviceY = deviceY;
	}

	public void clear(){
		if(mInstance != null)
			mInstance = null;
	}

	/**
	 * 多线程安全: 初始化ActivityManager
	 */
	synchronized public static RequestParamUtil getInstance(Context context) {
		
		if (mInstance == null) {
			mInstance = new RequestParamUtil(context);
		}
		return mInstance;
	}
	synchronized public static RequestParamUtil getInstance(){
		
		return mInstance;
	}
	private RequestParamUtil(Context context) {
		L.enable(TAG, L.DEBUG_LEVEL);
		this.context = context;
	}

	/**
	 * 获取请求参数（公共参数头+各请求私有参数）
	 * 
	 * @param map
	 *            各请求私有参数集合
	 * @return
	 */
	public String getRequestParams(Map map) {
		String params;
		try {
			params = StringUtil.formatKeyValue(map);
		} catch (Exception e) {
			return "";
		}
		
		L.w(TAG, "request params: "+params);
		return params;
	}

	

	private String getDeviceId() {
		if(deviceId!=null && !"".equals(deviceId)){
			return deviceId;
		} else{
			deviceId = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			return deviceId;
		}
	}

	
	private String getOperatorName() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getNetworkOperatorName();
	}
	private String getCountryIso() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getSimCountryIso();
	}
	public String getNetwork(){
		if(network!=null && !"".equals(network)){
			return network;
		}else{
			network = String.valueOf(InternetUtil.getConnectedType(context));
			return network;
		}
	}
	
	private final static int kSystemRootStateUnknow = -1;
	private final static int kSystemRootStateDisable = 0;
	private final static int kSystemRootStateEnable = 1;
	private static int systemRootState = kSystemRootStateUnknow;

	public static boolean isRootSystem() {
		if (systemRootState == kSystemRootStateEnable) {
			return true;
		} else if (systemRootState == kSystemRootStateDisable) {
			return false;
		}
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = kSystemRootStateEnable;
					return true;
				}
			}
		} catch (Exception e) {
		}
		systemRootState = kSystemRootStateDisable;
		return false;
	}	

	/**
	 * 设置session值
	 * 
	 * @param session
	 */
	public void setSession(String session) {
		this.session = session;
	}
	
	/**
	 * 获取手机系统的语言，暂支持简体中文和英文
	 * 
	 * @return
	 */
	private String getLanguage() {
		//return "zh".equals(Locale.getDefault().getLanguage()) ? "zh_cn" : "en";
		return "zh".equals(Locale.getDefault().getLanguage()) ? "2" : "1";
	}

	/**
	 * map转Json串
	 * 
	 * @param map
	 * @return
	 */
	private String mapToJson(Map<String, Object> map) {
		if (map == null || map.isEmpty())
			return "";
		StringBuilder sBuffer = new StringBuilder();
		sBuffer.append("{");
		for (Entry<String, Object> entry : map.entrySet()) {
			sBuffer.append("\"");
			sBuffer.append(entry.getKey());
			sBuffer.append("\":");
			if (entry.getValue() instanceof Map) {
				sBuffer.append(mapToJson((Map<String, Object>) entry.getValue()));
			} else if (entry.getValue() instanceof String) {
				sBuffer.append("\"");
				sBuffer.append(entry.getValue());
				sBuffer.append("\"");
			} else if (entry.getValue() instanceof Integer) {
				sBuffer.append(entry.getValue());
			}
			sBuffer.append(",");
		}
		sBuffer.deleteCharAt(sBuffer.lastIndexOf(","));
		sBuffer.append("}");
		return sBuffer.toString();
	}

	/**
	 * 获取本机MAC地址
	 * 
	 * @return
	 */
	private String getLocalMacAddress() {
		if(wlan!=null && !"".equals(wlan)){
			return wlan;
		} else {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			wlan = info.getMacAddress();
			return wlan;
		}
	}
}
