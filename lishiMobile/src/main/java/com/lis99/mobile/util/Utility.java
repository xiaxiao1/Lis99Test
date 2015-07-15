package com.lis99.mobile.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {

	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return;

		}
		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		listView.setLayoutParams(params);

	}

	/**
	 * 把uri转化成文件路径
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static String getRealPathFromURI(Uri contentUri, Context context) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

}
