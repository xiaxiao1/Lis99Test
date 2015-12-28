package com.lis99.mobile.entry.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/*******************************************
 * @ClassName: CustomProgressDialog 
 * @Description: 等待对话框
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午4:05:57 
 *  
 ******************************************/
public class CustomProgressDialog extends ProgressDialog implements DialogInterface.OnKeyListener {
	
	/** CustomProgressDialog 实例 */
	private static CustomProgressDialog instance;
	
	/** 上下文对象*/
	private Context context;
	
	/** 对话框标题及提示信息 */
	private String dialogTitle = "";
	private String dialogContent = "";

	/** dialog状态回调父类*/
	private Object mParent = null;

	private CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
		// 设置按键事件监听器
		setOnKeyListener(this);
	}

	public static CustomProgressDialog getInstance(Context context) {
		if (instance == null) {
			instance = new CustomProgressDialog(context);
		}
		return instance;
	}

	public static CustomProgressDialog getInstance() {
		return instance;
	}

	/**
	 * 弹出提示框
	 * @param parent 触发者
	 * @param context 上下文对象
	 * @param title 标题
	 * @param content 显示内容
	 */
	public void popup(Object parent, Context context, String title, String content) {
		try{
			if (instance == null || this.context != context) {
				instance = new CustomProgressDialog(context);
				this.context = context;
			}
			instance.mParent = parent;
			instance.setTitle(title == null ? dialogTitle : title);
			instance.setMessage(content == null ? dialogContent : content);

			if (!instance.isShowing()) {
				instance.setCanceledOnTouchOutside(false);
				instance.show();
			}
		}catch(Exception e){}
	}

	/**
	 * 弹出提示框
	 * @param parent 触发者
	 * @param context 上下文对象
	 */
	public void popup(Object parent, Context context) {
		popup(parent, context, null, null);
	}

	/**
	 * 关闭提示框,并且销毁线程
	 */
	public void close() {
		try {
			if (instance != null) {
				instance.dismiss();
				instance = null;
			}
		}catch (Exception e){}
	}

	/**
	 * 判断当前提示框是否处理显示状态
	 * @return
	 */
	public boolean isShow() {
		if (instance == null || instance.isShowing() == false) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			// 不做操作，直接返回true以屏蔽系统弹出搜索框
			return true;
		}
		return false;
	}

}
