package com.lis99.mobile.util;

import java.util.Hashtable;

/*******************************************
 * @ClassName: L 
 * @Description: 调试信息工具 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:40:15 
 *  
 ******************************************/
public class L {
	
	private static Hashtable<String, Integer> mTable = new Hashtable<String, Integer>();

	private static final int ALL_OFF = 100;
	public static final int OFF_LEVEL = 100;
	public static final int ERROR_LEVEL = 4;
	public static final int WARN_LEVEL = 3;
	public static final int INFO_LEVEL = 2;
	public static final int DEBUG_LEVEL = 1;
	private static final int ALL_ON = 0;

	public static int LEVEL = ALL_ON; // 默认全部打开

	public static boolean ERROR = (LEVEL <= ERROR_LEVEL);
	public static boolean WARN = (LEVEL <= WARN_LEVEL);
	public static boolean INFO = (LEVEL <= INFO_LEVEL);
	public static boolean DEBUG = (LEVEL <= DEBUG_LEVEL);

	private static boolean mEnableTagControl = true;

	public static void set(int newLevel) {
		LEVEL = newLevel;
		ERROR = (LEVEL <= ERROR_LEVEL);
		WARN = (LEVEL <= WARN_LEVEL);
		INFO = (LEVEL <= INFO_LEVEL);
		DEBUG = (LEVEL <= DEBUG_LEVEL);
	}

	public static void turnOff() {
		set(ALL_OFF);
	}

	public static void turnOn() {
		turnOn(true);
	}

	public static void turnOn(boolean tagControl) {
		set(ALL_ON);
		mEnableTagControl = tagControl;
	}

	public static void enable(String name, int newLevel) {
		if (name == null || name.length() == 0)
			return;

		mTable.put(name.toLowerCase(), Integer.valueOf(newLevel));
	}

	private static boolean isEnabled(String name, int level) {
		if (name == null || name.length() == 0)
			return false;

		Integer value = mTable.get(name.toLowerCase());
		if (value == null) {
			return false; // 没有注册的, 默认关闭
		} else {
			if (value.intValue() <= level)
				return true;
			else
				return false;
		}
	}

	public static void i(String tag, String string) {
		if (INFO) {
			if (mEnableTagControl) {
				if (isEnabled(tag, INFO_LEVEL))
					android.util.Log.i(tag, string);
			} else {
				android.util.Log.i(tag, string);
			}
		}
	}

	public static void e(String tag, String string) {
		if (ERROR) {
			if (mEnableTagControl) {
				if (isEnabled(tag, ERROR_LEVEL))
					android.util.Log.e(tag, string);
			} else {
				android.util.Log.e(tag, string);
			}
		}
	}

	public static void e(String tag, String string, Throwable e) {
		if (ERROR) {
			if (mEnableTagControl) {
				if (isEnabled(tag, ERROR_LEVEL))
					android.util.Log.e(tag, string, e);
			} else {
				android.util.Log.e(tag, string, e);
			}
		}
	}

	public static void d(String tag, String string) {
		if (DEBUG) {
			if (mEnableTagControl) {
				if (isEnabled(tag, DEBUG_LEVEL))
					android.util.Log.d(tag, string);
			} else {
				android.util.Log.d(tag, string);
			}
		}
	}

	public static void w(String tag, String string) {
		if (WARN) {
			if (mEnableTagControl) {
				if (isEnabled(tag, WARN_LEVEL))
					android.util.Log.w(tag, string);
			} else {
				android.util.Log.w(tag, string);
			}
		}
	}

	public static void w(String tag, String string, Throwable e) {
		if (WARN) {
			if (mEnableTagControl) {
				if (isEnabled(tag, WARN_LEVEL))
					android.util.Log.w(tag, string, e);
			} else {
				android.util.Log.w(tag, string, e);
			}
		}
	}
}
