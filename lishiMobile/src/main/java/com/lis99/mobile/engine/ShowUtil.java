package com.lis99.mobile.engine;

import android.content.Context;
import android.widget.Toast;

public class ShowUtil {
	private static final int TIME_LENGTH = 1000;
	/**
	 * 提示�?
	 * @param context Context/class.this
	 * @param show_str 提示内容
	 */
	public static void showToast(Context context, int resId)
	{
		Toast.makeText(context, context.getString(resId), TIME_LENGTH).show();
	}
	public static void showToast(Context context, String content)
	{
		Toast.makeText(context, content, TIME_LENGTH).show();
	}
}