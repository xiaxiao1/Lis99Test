package com.lis99.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lis99.mobile.entry.application.DemoApplication;

import java.util.Map;

/**
 * @ClassName: SharedPreferencesHelper
 * @Description: SharedPreferences帮助类
 * @author <xingyong>xingyong@cy2009.com
 * @date 2012-7-6 下午3:29:12
 */
public class SharedPreferencesHelper {
	
	/**
	 * 插入参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 * @param value
	 */
	public static void putValue(Context context, String fileName, int mode,
			String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, mode).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 插入参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 * @param value
	 */
	public static void putValue(Context context, String fileName, int mode,
			String key, Boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, mode).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获得参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 */
	public static String getValue(Context context, String fileName, int mode,
			String key) {
		return context.getSharedPreferences(fileName, mode).getString(key, "");
	}
	
	/**
	 * 获得参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 * @param key
	 */
	public static String getValue(Context context, String fileName, int mode,
			String key,String defaultValue) {
		return context.getSharedPreferences(fileName, mode).getString(key, defaultValue);
	}

	/**
	 * 清空参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 */
	public static void emptyFile(Context context, String fileName, int mode) {
		context.getSharedPreferences(fileName, mode).edit().clear().commit();
	}

	/**
	 * 获得所以参数
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 * @return
	 */
	public static Map getAllByFileName(Context context, String fileName,
			int mode) {
		return context.getSharedPreferences(fileName, mode).getAll();
	}
	//登陆用户名
	public static void saveUserName ( String name )
	{
		saveBase(C.ACCOUNT, name);
	}
	
	public static void removeName ()
	{
		removeBase(C.ACCOUNT);
	}
	
	public static void saveUserPass ( String pass )
	{
		saveBase(C.PASSWORD, pass);
	}
	
	public static void removeUserPass ()
	{
		removeBase(C.PASSWORD);
	}
	
	public static void saveLSToken (String str )
	{
		saveBase("LIS99TOKEN", str);
	}
	
	public static void removeLSToken () 
	{
		removeBase("LIS99TOKEN");
	}
	
	public static String getLSToken ()
	{
		return getBase("LIS99TOKEN", "");
	}
	
	private static void saveBase ( String key, String value )
	{
		SharedPreferences.Editor editor = DemoApplication.getInstance().getSharedPreferences(
				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	private static void removeBase ( String key )
	{
		SharedPreferences.Editor editor = DemoApplication.getInstance().getSharedPreferences(
				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();
	}
	
	private static String getBase ( String key, String value )
	{
		String str = DemoApplication.getInstance().getSharedPreferences(C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).getString(key, value);
		return str;
	}
	
	public static void cleanUserInfo ()
	{
		removeName();
		removeUserPass();
		removeLSToken();
		//==QQ===
		removeapi_token();
		removeapi_uid();
		removeexpires_in();
		removeLSToken();
		//===sina-----
		removeSinaCode();
		
		removeSn();
		removeaccounttype();
		
		removenickname();
		removeuser_id();
		removeheadicon();
		removeIsVip();
		
		//===push token===
		removePushToken();
	}

	public static void saveUserPhone(String phone) {
		saveBase(C.ACCOUNT_PHONE, phone);
	}

	public static void saveWeixinCode(String s) {
		saveBase(C.WEIXIN_CODE,s);
	}

	public static String getWeixinCode() {
		return getBase(C.WEIXIN_CODE,null);
	}

	public static void removeWeixinCode() {
		removeBase(C.WEIXIN_CODE);
	}

	public static void saveWeixinToken(String s) {
		saveBase(C.WEIXIN_TOKEN,s);
	}

	public static void removeWeixinToken() {
		removeBase(C.WEIXIN_TOKEN);
	}

	public static void saveWeixinOpenID(String s) {
		saveBase(C.WEIXIN_OPENID, s);
	}

	public static void removeWeixinOpenID() {
		removeBase(C.WEIXIN_OPENID);
	}



	public static void saveWeixinNickName(String s) {
		saveBase("weixin_nickName",s);
	}

	public static void removeWeixinNickName() {
		removeBase("weixin_nickName");
	}

	public static void saveWeixinSex(String s) {
		saveBase("weixin_sex",s);
	}

	public static void removeWeixinSex() {
		removeBase("weixin_sex");
	}

	public static void saveWeixinHeader(String s) {
		saveBase("weixin_header",s);
	}

	public static void removeWeixinHeader() {
		removeBase("weixin_header");
	}



	public static void removePhone() {
		removeBase(C.ACCOUNT_PHONE);
	}
	
	public static void saveapi_uid ( String str )
	{
		saveBase(C.TENCENT_OPEN_ID, str);
	}
	
	public static void removeapi_uid ()
	{
		removeBase(C.TENCENT_OPEN_ID);
	}
	
	public static void saveapi_token ( String str )
	{
		saveBase(C.TENCENT_ACCESS_TOKEN, str);
	}
	
	public static void removeapi_token ()
	{
		removeBase(C.TENCENT_ACCESS_TOKEN);
	}
	
	public static void saveexpires_in ( String str )
	{
		saveBase(C.TENCENT_EXPIRES_IN, str);
	}
	
	public static void removeexpires_in ()
	{
		removeBase(C.TENCENT_EXPIRES_IN);
	}
	
	public static void saveTOKEN ( String str )
	{
		saveBase(C.TOKEN, str);
	}
	
	public static void removeTOKEN ()
	{
		removeBase(C.TOKEN);
	}
	
	public static void saveSinaCode ( String str )
	{
		saveBase(C.SINA_CODE, str);
	}
	
	public static void removeSinaCode ()
	{
		removeBase(C.SINA_CODE);
	}
	
	public static void saveaccounttype ( String str )
	{
		saveBase("accounttype", str);
	}
	
	public static void removeaccounttype ()
	{
		removeBase("accounttype");
	}
	public static void saveSn ( String str )
	{
		saveBase("sn", str);
	}
	
	public static void removeSn ()
	{
		removeBase("sn");
	}
	
	public static void savenickname ( String str )
	{
		saveBase("nickname", str);
	}
	
	public static void removenickname ()
	{
		removeBase("nickname");
	}
	
	public static void saveuser_id ( String str )
	{
		saveBase("user_id", str);
	}
	
	public static void removeuser_id ()
	{
		removeBase("user_id");
	}
	
	public static void saveheadicon ( String str )
	{
		saveBase("headicon", str);
	}
	
	public static void removeheadicon ()
	{
		removeBase("headicon");
	}
	
	public static void saveIsVip (String vip)
	{
		saveBase("isvip", vip);
	}
	
	public static void removeIsVip ()
	{
		removeBase("isvip");
	}
	
	public static void savePushToken ( String token )
	{
		saveBase("PUSHTOKEN", token);
	}
	public static String getPushToken ()
	{
		String Token = getBase("PUSHTOKEN", "");
		if ( TextUtils.isEmpty(Token))
		{
//			Common.log("pushToken=====null");
			Token = PushManager.getInstance().Token;
		}
		return Token;
	}
	
	public static void removePushToken ()
	{
//		removeBase("PUSHTOKEN");
	}

//	@SuppressLint("NewApi")
//	public static void saveSearchHistory ( Set<String> set )
//	{
//		SharedPreferences.Editor editor = DemoApplication.getInstance().getSharedPreferences(
//				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).edit();
//		editor.putStringSet("SEARCH_HISTORY", set);
//		editor.commit();
//	}
//
//	public static ArrayList<String> getSearchHistory ()
//	{
//		SharedPreferences editor = DemoApplication.getInstance().getSharedPreferences(
//				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE);
//		Set<String> set = editor.getStringSet("SEARCH_HISTORY", null);
//
//		if ( set == null || set.isEmpty() ) return null;
//		String[] list = (String[]) set.toArray();
//		return null;
//	}

}
