package com.lis99.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************
 * @ClassName: StringUtil 
 * @Description: 封装字符串操作 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午3:08:44 
 *  
 ******************************************/
public class StringUtil {

	private static final String HTTP = "HTTP";
	private static final String FILE = "FILE";

	/**
	 * 获取url的根目录
	 * 
	 * @param url
	 * @return
	 */
	public static String getUrlRoot(String url) {
		if (url.toUpperCase().startsWith(HTTP) || url.toUpperCase().startsWith(FILE)) {
			int star = url.indexOf("://") + 3;
			int index = url.indexOf('/', star);
			if (index <= 0) {
				index = url.length();
			}
			return url.substring(0, index);
		}
		return null;
	}

	/**
	 * 获得一个字符串的长度（中、英文）
	 * 
	 * @param value
	 * @return
	 */
	public static int getLength(String value) {
		int valueLength = 0;
		if (value == null || "".equals(value)) {
			return valueLength;
		}
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 将MAP转化为key1=value&key2=vaule&key3=value格式的字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String formatKeyValue(Map<String, Object> params) {
		if (params == null)
			return null;
		StringBuffer sb = new StringBuffer();
		String key = "", result = "";
		Object value = "";
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				sb.append(key);
				sb.append("=");
				sb.append(value);
				sb.append("&");
			}
			result = sb.toString();
			result = result.substring(0, (result.length() - 1)); // 截掉末尾字符&
		}
		return result;
	}

	/**
	 * 检查邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {

		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();

	}
	
	/**检测只能包含数字和字母(false表示不是字母或者数字)*/
	public static boolean checkNumberAndLetter(String key) {

		Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
		Matcher matcher = pattern.matcher(key);

		return matcher.matches();

	}

	/**
	 * 检查密码
	 * 
	 * @param password
	 * @return
	 */
	public static boolean checkPassword(String password) {

		Pattern pattern = Pattern.compile("[0-9a-zA-Z_]{6,16}");

		Matcher matcher = pattern.matcher(password);

		return matcher.matches();
	}

	public static String decodeByBase64(String param) {
		if (param == null)
			return null;
		return new String(Base64.decode(param, Base64.DEFAULT));
	}

	/**
	 * 获取屏幕宽和高
	 */
	private static int[] pxy = { 0, 0 };

	public static int[] getXY(Activity act) {
		// if ((pxy[0] == 0) && (pxy[1] == 0)) {
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//context.getSystemService(Context.WINDOW_SERVICE);
		pxy[0] = dm.widthPixels;
		pxy[1] = dm.heightPixels;
		// }
		return pxy;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取手机系统的语言，暂支持简体中文和英文
	 * 
	 * @return
	 */
	public static String getLanguage() {
		return "zh".equals(Locale.getDefault().getLanguage()) ? "zh_cn" : "en";
	}

	public static String GetNetIp() {
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL("http://city.ip138.com/city0.asp");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();

				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("[");
				int end = strber.indexOf("]", start + 1);
				line = strber.substring(start + 1, end);
				return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
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
	
	/**根据输入的字符串，返回--干掉前面多个0后的字符串*/
	public static String clearFront0(String money){
		if(money==null||money.length()==1||!money.startsWith("0")){
			return money;
		}
	    return clearFront0( money.substring(1));		
	}

	/**检查是否包含汉字*/
    public static boolean checkChinese(String sequence) {

       final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
       Pattern pattern = Pattern.compile(format);
       Matcher matcher = pattern.matcher(sequence);
       return matcher.find();
   }
    
    /**清除小数*/
    public static String removeDecimal(String number){
    	return number.substring(0, number.lastIndexOf("."));
    }

	public static boolean checkCharacterNum(String str) {
		if(str!=null && str.length()>=6 && str.length()<=30){
			return true;
		}else{
			return false;
		}
	}
}
