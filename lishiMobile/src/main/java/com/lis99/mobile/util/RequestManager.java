package com.lis99.mobile.util;

import android.app.Activity;

import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.IOManager;

/**
 * 			联网请求
 * @author Administrator
 *
 */
public class RequestManager {

	private static RequestManager Instance;
	/** IO管理器实例 */
	private IOManager ioManager = null;
	
	public static RequestManager getInstance ()
	{
		if ( null == Instance ) Instance = new RequestManager();
		return Instance;
	}
	
	/**
	 * 发布任务
	 */
	public boolean publishTask(Activity a, Task task, int taskType) {
		if (IEvent.IO == taskType) {
			// 先判断网络是否连接，如果未连接则提示网络出问题了
			if (!InternetUtil.checkNetWorkStatus(a)) {
//				postMessage(POPUP_TOAST, "网络好像有问题");
//				postMessage(DISMISS_PROGRESS);
				DialogManager.getInstance().alert(a, "网络好像有问题");
				return false;
			}
			if (ioManager == null) {
				ioManager = IOManager.getInstance();
			}
			ioManager.addTask(task);
			return true;
		}
		return false;
	}

	/**
	 * 取消任务
	 */
	public void cancelIOTask(Object owner) {
		ioManager.cancelCurrentTask(owner);
	}
	
	/**
	 * 清理当前page发起的任务
	 */
	public void clearCurTasks() {
		if (ioManager == null) {
			ioManager = IOManager.getInstance();
		}
		// 取消所有当前进行的任务(有些任务不可暂停)
		ioManager.cancelCurrentTask(null);
		// 清空当前对列
		ioManager.removeAllHandlerTasks();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
