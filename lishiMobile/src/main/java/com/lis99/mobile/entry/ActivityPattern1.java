package com.lis99.mobile.entry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.huewu.pla.lib.MultiColumnListView;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.ShaiTuBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.IEventHandler;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.engine.io.IOManager;
import com.lis99.mobile.entry.cptslide.ChapterFragment.onChapterPageClickListener;
import com.lis99.mobile.entry.view.CustomProgressDialog;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.InternetUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.tauth.Tencent;

import java.util.HashMap;
import java.util.Map;

/*******************************************
 * @ClassName: ActivityPattern
 * @Description: Activity基类
 * @author xingyong cosmos250.xy@gmail.com
 * @date 2013-12-25 下午3:19:08
 * 
 *       1 ActivityPattern继承自Activity, 即程序中每一个**Activity都是一个单独的Activity,
 *       它们都涵盖Activity的整个生命周期. 2 Activity在onCreate时需处理初始化,判断程序状态,记录当前页等工作 3
 *       Activity切走时所处理的事情应在onPause中进行, 切回时的操作应在onResume中进行 4
 *       注意重载系统函数时的先后顺序(super的处理时机) 5 统一处理各Dialog
 * 
 ******************************************/
public class ActivityPattern1 extends FragmentActivity implements
		onChapterPageClickListener, OnClickListener, Callback, OnTouchListener,
		IEventHandler, OnLongClickListener, IWeiboHandler.Response {

	/** 消息类型定义 */
	public static final int POPUP_ALERT = 0; // 弹出提示对话框
	public static final int POPUP_PROGRESS = 1; // 弹出进度条
	public static final int DISMISS_PROGRESS = 2; // 关闭进度条
	public static final int POPUP_TOAST = 3;// 弹出Toast提示
	public static final int EXIT_PROGRAM = 4; // 退出整个程序
	public static final int POPUP_DIALOG_LIST = 5; // 弹出列表对话框

	protected Handler mHandler; // 线程通信管理器
	protected int mActivityState = -1; // Activity状态标识,
	public static boolean weiboFlag = false; // 对应于ActivityManager中的State
	private CustomProgressDialog customProgressDialog;
	protected Tencent mTencent;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	/** 微博 Web 授权接口类，提供登陆等功能 */
//	protected WeiboAuth mWeiboAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LSBaseActivity.activity = this;
		if (!weiboFlag) {
			initWeibo(savedInstanceState);
			weiboFlag = true;
		}
		mHandler = new Handler(this);
//		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		buildOptions();
	}

	// ----------------------------------------------------------------------------

	private void buildOptions() {
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.image_empty)
				// 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_empty) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory() // 设置下载的图片是否缓存在内存中
				.cacheOnDisc() // 设置下载的图片是否缓存在SD卡中
				.build(); // 创建配置过得DisplayImageOption对象
	}

	IWeiboShareAPI mWeiboShareAPI;

	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire)) {
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
		// 初始化微博对象
//		mWeiboAuth = new WeiboAuth(this, C.SINA_APP_KEY, C.SINA_REDIRECT_URL,
//				C.SINA_SCOPE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	public void setAdapterHeightBasedOnChildren(MultiColumnListView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount() * 3 / 5; i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public void setAdapterHeightBasedOnChildren1(ListView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public void setAdapterHeightBasedOnChildren2(GridView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			if (i % 5 == 0) {
				totalHeight += listItem.getMeasuredHeight();
			}
			// 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		LSBaseActivity.activity = this;
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog create = bld.create();
		create.setCanceledOnTouchOutside(false);
		create.setCancelable(false);
		create.show();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用.
	 * 
	 * @param operation
	 *            操作类型
	 * @param params
	 *            操作执行所需的参数列表
	 */
	public void postMessage(int operation, Object params) {
		Message msg = Message.obtain();
		msg.what = operation;
		msg.obj = params;
		mHandler.sendMessage(msg);
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用.
	 * 
	 * @param operation
	 *            操作类型
	 * @param params
	 *            操作执行所需的参数列表
	 */
	public void postMessage(int operation, String title, int itemsId,
			DialogInterface.OnClickListener listener) {
		Message msg = Message.obtain();
		msg.what = operation;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		map.put("items", itemsId);
		map.put("listener", listener);
		msg.obj = map;
		mHandler.sendMessage(msg);
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用.
	 * 
	 * @param operation
	 *            操作类型
	 * @param params
	 *            操作执行所需的参数列表
	 */
	public void postMessage(int operation, String title, String content,
			boolean leftBtn, String leftStr,
			DialogInterface.OnClickListener leftBtnListener, boolean rightBtn,
			String rightStr, DialogInterface.OnClickListener rightBtnListener) {
		Message msg = Message.obtain();
		msg.what = operation;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", title);
		map.put("message", content);
		map.put("left_btn_show", leftBtn);
		map.put("left_btn_text", leftStr);
		map.put("left_btn_listener", leftBtnListener);
		map.put("right_btn_show", rightBtn);
		map.put("right_btn_text", rightStr);
		map.put("right_btn_listener", rightBtnListener);
		msg.obj = map;
		mHandler.sendMessage(msg);
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用.
	 * 
	 * @param operation
	 *            操作类型
	 */
	public void postMessage(int operation) {
		postMessage(operation, null);
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用. 延迟一定时间
	 * 
	 * @param operation
	 *            操作类型
	 * @param time
	 *            延迟时间
	 */
	public void postDelay(int operation, long time) {
		postDelay(operation, null, time);
	}

	/**
	 * 通过消息的方式, 发送UI操作到主程序窗口管理器. 该函数可由非UI主线程调用. 延迟一定时间
	 * 
	 * @param operation
	 *            操作类型
	 * @param params
	 *            操作执行所需的参数列表
	 * @param time
	 *            延迟时间
	 */
	public void postDelay(int operation, Object params, long time) {
		Message msg = Message.obtain();
		msg.what = operation;
		msg.obj = params;
		mHandler.sendMessageDelayed(msg, time);
	}

	/**
	 * 处理所有和窗口相关的消息，由Activity调用 此方法在主线程中调用, 注意避免耗时操作
	 * 
	 * @param msg
	 */
	public boolean handleMessage(Message msg) {
		boolean result = true;
		switch (msg.what) {
		case POPUP_ALERT:
			if (msg.obj instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				startAlert((String) map.get("title"),
						(String) map.get("message"),
						(Boolean) map.get("left_btn_show"),
						(String) map.get("left_btn_text"),
						(DialogInterface.OnClickListener) map
								.get("left_btn_listener"),
						(Boolean) map.get("right_btn_show"),
						(String) map.get("right_btn_text"),
						(DialogInterface.OnClickListener) map
								.get("right_btn_listener"));
			} else if (msg.obj instanceof String) {
				startAlert(null, (String) msg.obj, true, "", null, false, null,
						null);
			}
			break;

		case POPUP_PROGRESS:
			if (msg.obj instanceof Map) {
				Map<String, String> map = (Map<String, String>) msg.obj;
				startWaiting(map.get("title"), map.get("message"));
			} else if (msg.obj instanceof String) {
				startWaiting(null, (String) msg.obj);
			}
			break;

		case DISMISS_PROGRESS:
			stopWaitting();
			break;

		case EXIT_PROGRAM:
			// 退出前保存数据
			break;

		case POPUP_TOAST:
			// 不允许参数为空, 而且必须是String
			if (msg.obj instanceof String) {
				Toast.makeText(this, (String) msg.obj, 1000).show();
			}
			break;

		case POPUP_DIALOG_LIST:
			// 弹出列表对话框
			if (msg.obj instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				showDialogList((String) map.get("title"),
						(Integer) map.get("items"),
						(DialogInterface.OnClickListener) map.get("listener"));
			}
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	/**
	 * 发布任务
	 */
	public boolean publishTask(Task task, int taskType) {
		if (IEvent.IO == taskType) {
			// 先判断网络是否连接，如果未连接则提示网络出问题了
			if (!InternetUtil.checkNetWorkStatus(this)) {
				postMessage(POPUP_TOAST, "网络好像有问题");
				postMessage(DISMISS_PROGRESS);
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

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		Object data;
		if (task == null) {
			postMessage(DISMISS_PROGRESS);
			return;
		}
		if (task.isFailed()) {
			postMessage(DISMISS_PROGRESS);
			return;
		}
		if (task.isCanceled()) {
			postMessage(DISMISS_PROGRESS);
			return;
		}
	}

	/** IO管理器实例 */
	private IOManager ioManager = null;

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

	/**
	 * 关闭activity方法
	 **/
	protected void closeActivity() {
		finish();
	}

	/**
	 * 启动等待对话框
	 * 
	 * @param title
	 * @param content
	 */
	synchronized public void startWaiting(String title, String content) {
//		if (customProgressDialog == null) {
//			customProgressDialog = CustomProgressDialog.getInstance(this);
//		}
//		if (customProgressDialog != null
//				&& customProgressDialog.isShow() == false) {
//			customProgressDialog.popup(this, this, title, content);
//		}

		DialogManager.getInstance().startWaiting(this, null, null);

	}

	/**
	 * 结束等待对话框
	 */
	public synchronized void stopWaitting() {
//		if (customProgressDialog != null && customProgressDialog.isShow()) {
//			customProgressDialog.close();
//		}

		DialogManager.getInstance().stopWaitting();

	}

	/**
	 * 启动提示对话框
	 */
	synchronized public void startAlert(String title, String content,
			boolean leftBtn, String leftStr,
			DialogInterface.OnClickListener leftBtnListener, boolean rightBtn,
			String rightStr, DialogInterface.OnClickListener rightBtnListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(content);
		if (leftBtn)
			builder.setPositiveButton(leftStr, leftBtnListener);
		if (rightBtn)
			builder.setNegativeButton(rightStr, rightBtnListener);
		builder.create().show();
	}

	/**
	 * 开启列表对话框
	 * 
	 * @param title
	 * @param itemsId
	 * @param listener
	 */
	synchronized public void showDialogList(String title, int itemsId,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(itemsId, listener).setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.create().show();
	}

	@Override
	public void onResponse(BaseResponse arg0) {

	}

	@Override
	public void onChapterItemClick(int position, ShaiTuBean name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChapterSpaceClick() {
		// TODO Auto-generated method stub

	}

}
