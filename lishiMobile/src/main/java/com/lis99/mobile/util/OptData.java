/** 
 * 文件名：ActivityManager.java
 * 版本信息�?
 * 日期�?013-4-10
 */

package com.lis99.mobile.util;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lis99.mobile.engine.base.Util;
import com.lis99.mobile.json.DataParseFactory;
import com.lis99.mobile.json.IDataParse;

/**
 * 
 * 项目名称：framework 类名称：ActivityManager 类描述： 创建人：吴安�?创建时间�?013-4-10 下午1:25:53
 * 修改人：杨琳 修改时间�?013-10-23 下午1:25:53 修改备注�?
 * 
 * @version
 * @param <T>
 * 
 */
public final class OptData<T> {
	private final Context context;
	private QueryData<T> queryData;

	public OptData(Context context) {
		this.context = context;
	}

	public void readData(QueryData<T> queryData, final Class<T> clazz) {
		this.queryData = queryData;
		Util.threadExecute(new Runnable() {

			@Override
			public void run() {
				String json = getQueryData().callData();
				if (DataParseFactory.BUG) {
					Log.i("json", json);
				}
				IDataParse<T> dataparse = DataParseFactory.newDataParse("json");
				T t = dataparse.toBean(json, clazz);
				showData(t);
			}

		});
	}
	public void readHTML(QueryData<T> queryData, final Class<T> clazz) {
		this.queryData = queryData;
		Util.threadExecute(new Runnable() {

			@Override
			public void run() {
				String json = getQueryData().callData();
				if (DataParseFactory.BUG) {
					Log.i("json", json);
				}
				showData(json);
			}

		});
	}

	public void readData() {
		Util.threadExecute(new Runnable() {

			@Override
			public void run() {
				getQueryData().callData();
			}
		});
	}

	private void showData(final T t) {

		android.os.Message msg = showDataHandler.obtainMessage();
		msg.obj = t;
		msg.sendToTarget();
	}
	private void showData(final String string) {

		android.os.Message msg = showDataHandler.obtainMessage();
		msg.obj = string;
		msg.sendToTarget();
	}

	private final ShowDataHandler showDataHandler = new ShowDataHandler();

	private class ShowDataHandler extends Handler {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message message) {
			if (null == message.obj && Util.isNet(context)) {
//				ShowUtil.showToast(context, R.string.fuwuqifukefangwen);
				getQueryData().showData(null);
			} else {
				getQueryData().showData((T) message.obj);
			}
		}

	};

	public QueryData<T> getQueryData() {
		return queryData;
	}

}
