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
	
	synchronized private static void saveBase ( String key, String value )
	{
		SharedPreferences.Editor editor = DemoApplication.getInstance().getSharedPreferences(
				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	synchronized private static void removeBase ( String key )
	{
		SharedPreferences.Editor editor = DemoApplication.getInstance().getSharedPreferences(
				C.CONFIG_FILENAME, DemoApplication.getInstance().MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();
	}
	
	synchronized private static String getBase ( String key, String value )
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
		removQQOpenId();
		removeQQSex();
		removeQQNickName();
		removeQQHeadIcon();

		//===sina-----
		removeSinaCode();
		removeSinaNickName();
		removeSinaHeadIcon();
		removeSinaSex();
		removeSinaUid();
		

		//用户
		removeaccounttype();
		
		removenickname();
		removeuser_id();
		removeheadicon();
		removeIsVip();


		removeTOKEN();
		
		//===push token===
//		removePushToken();

		//=====weixin====
		removeWeixinCode();
		removeWeixinToken();
		removeWeixinOpenID();
		removeWeixinNickName();
		removeWeixinSex();
		removeWeixinHeader();
		removeWeiXinUnionid();

		//==========手机号=========
		removePhone();
//		引导页
		cleanHelp();

//		===========活动弹出菜单===========
		cleanFirstActive();


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
		saveBase(C.WEIXIN_TOKEN, s);
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
		saveBase("weixin_nickName", s);
	}

	public static void removeWeixinNickName() {
		removeBase("weixin_nickName");
	}

	public static void saveWeixinSex(String s) {
		saveBase("weixin_sex", s);
	}

	public static void removeWeixinSex() {
		removeBase("weixin_sex");
	}

	public static void saveWeixinHeader(String s) {
		saveBase("weixin_header", s);
	}

	public static void removeWeixinHeader() {
		removeBase("weixin_header");
	}

	public static void saveWeiXinUnionid ( String unionid )
	{
		saveBase("WEIXINUNIONID", unionid);
	}

	public static String getWeiXinUnionid ()
	{
		return getBase("WEIXINUNIONID", "");
	}

	private static void removeWeiXinUnionid ()
	{
		removeBase("WEIXINUNIONID");
	}



	public static void removePhone() {
		removeBase(C.ACCOUNT_PHONE);
	}
	
	public static void saveQQOpenId(String str)
	{
		saveBase(C.TENCENT_OPEN_ID, str);
	}

	public static void removQQOpenId()
	{
		removeBase(C.TENCENT_OPEN_ID);
	}

	public static String getQQOpenId ()
	{
		return getBase(C.TENCENT_OPEN_ID, "");
	}

//	public static void saveQQToken(String str)
//	{
//		saveBase(C.TENCENT_ACCESS_TOKEN, str);
//	}
//	public static void removeQQToken()
//	{
//		removeBase(C.TENCENT_ACCESS_TOKEN);
//	}
//
//	public static String getQQToken ()
//	{
//		return  getBase(C.TENCENT_ACCESS_TOKEN, "");
//	}
	
//	public static void saveexpires_in ( String str )
//	{
//		saveBase(C.TENCENT_EXPIRES_IN, str);
//	}
//
//	public static void removeexpires_in ()
//	{
//		removeBase(C.TENCENT_EXPIRES_IN);
//	}
//
//	public static String getExpires_in ()
//	{
//		return getBase(C.TENCENT_EXPIRES_IN, "");
//	}
	
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

	public static String getSinaCode ()
	{
		return getBase(C.SINA_CODE, "");
	}
	
	public static void saveaccounttype ( String str )
	{
		saveBase("accounttype", str);
	}
	public static String getaccounttype ()
	{
		return getBase("accounttype", "");
	}
	public static void removeaccounttype ()
	{
		removeBase("accounttype");
	}
//	public static void saveSn ( String str )
//	{
//		saveBase("sn", str);
//	}
//
//	public static void removeSn ()
//	{
//		removeBase("sn");
//	}
	
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
			Token = PushManager.getInstance().Token;
		}
		return Token;
	}
	
	public static void removePushToken ()
	{
		removeBase("PUSHTOKEN");
	}

	public static void saveQQSex ( String sex )
	{
		saveBase("QQSEX", sex);
	}

	public static String getQQSex ()
	{
		return getBase("QQSEX", "");
	}

	private static void removeQQSex ()
	{
		removeBase("QQSEX");
	}

	public static void saveQQNickName ( String nickname )
	{
		saveBase("QQNICKNAME", nickname);
	}

	public static String getQQNickName ()
	{
		return getBase("QQNICKNAME", "");
	}

	private  static void removeQQNickName ()
	{
		removeBase("QQNICKNAME");
	}

	private static void removeQQHeadIcon ()
	{
		removeBase("QQHEADICON");
	}

	public static void saveQQHeadIcon ( String headIcon )
	{
		saveBase("QQHEADICON", headIcon);
	}

	public static String getQQHeadIcon ()
	{
		return getBase("QQHEADICON", "");
	}

	public static void saveSinaSex ( String sex )
	{
		saveBase("SINASEX", sex);
	}

	public static String getSinaSex ()
	{
		return getBase("SINASEX", "");
	}

	private static void removeSinaSex ()
	{
		removeBase("SINASEX");
	}

	public static void saveSinaNickName (String nickName )
	{
		saveBase("SINANICKNAME", nickName);
	}

	public static String getSinaNickName ()
	{
		return getBase("SINANICKNAME", "");
	}

	private static void removeSinaNickName ()
	{
		removeBase("SINANICKNAME");
	}

	public static void saveSinaHeadIcon ( String headIcon )
	{
		saveBase("SINAHEADICON", headIcon);
	}

	public static String getSinaHeadIcon ()
	{
		return getBase("SINAHEADICON", "");
	}

	private static void removeSinaHeadIcon ()
	{
		removeBase("SINAHEADICON");
	}

	public static void saveSinaUid ( String uid )
	{
		saveBase("SINAUID", uid);
	}

	public static String getSinaUid ()
	{
		return getBase("SINAUID", "");
	}

	private static void removeSinaUid ()
	{
		removeBase("SINAUID");
	}



	//登陆类型
	public static String QQLOGIN = "QQLOGIN";
	public static String WEIXINLOGIN = "wechat";
	public static String SINALOGIN = "SINALOGIN";
	public static String THIRD = "third";
	public static String EMAIL = "email";
	public static String PHONE = "phone";

	//全屏广告
	public static void saveAD ( String ad )
	{
		saveBase("ADINFO", ad );
	}

	public static String getAD ()
	{
		return getBase("ADINFO", "");
	}

	public static void cleanAD ()
	{
		removeBase("ADINFO");
	}


	public static void saveHelp ( String state )
	{
		saveBase("SAVEHELP0", state);
	}

	public static String getHelp ()
	{
		return getBase("SAVEHELP0", "");
	}

	public static void cleanHelp ()
	{
		removeBase("SAVEHELP0");
	}


//	管理报名位置移动， 首次进入下线活动帖， 弹出菜单提示
	public static void saveFirstActive ( String state )
	{
		saveBase("FirstActive", state);
	}

	public static String getFirstActive ()
	{
		return getBase("FirstActive", "");
	}

	public static void cleanFirstActive ()
	{
		removeBase("FirstActive");
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
