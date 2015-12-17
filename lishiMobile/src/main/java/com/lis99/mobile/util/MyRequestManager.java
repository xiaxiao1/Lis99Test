package com.lis99.mobile.util;

import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.Map;

public class MyRequestManager {

	private MyTask mTask;
	
	private MyRequest mRequest;
	
	private static MyRequestManager Instance;
	
	public static MyRequestManager getInstance ()
	{
		if ( Instance == null ) Instance = new MyRequestManager();
		return Instance;
	}
	/**
	 * 			get请求
	 * @param url
	 * @param resultModel
	 * @param callBack
	 */
	public void requestGet ( String url, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setShowDialog(true);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}
	/**
	 * 			post请求
	 * @param url
	 * @param map
	 * @param resultModel
	 * @param callBack
	 */
	public void requestPost ( String url, Map<String, Object> map, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setRequestState(mTask.POST);
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setMap(map);
		mTask.setShowDialog(true);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}
	
	/**
	 * 			get请求
	 * @param url
	 * @param resultModel
	 * @param callBack
	 */
	public void requestGetNoDialog ( String url, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setShowDialog(false);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}
	
	/**
	 * 			post请求
	 * @param url
	 * @param resultModel
	 * @param callBack
	 */
	public void requestPostNoDialog ( String url, Map<String, Object> map, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setRequestState(mTask.POST);
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setMap(map);
		mTask.setShowDialog(false);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}

	/***
	 * 				上传图片
	 * @param url
	 * @param map
	 * @param resultModel
	 * @param callBack
	 */
	public void requestImage ( String url, Map<String, Object> map, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setRequestState(mTask.IMAGE);
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setMap(map);
		mTask.setShowDialog(true);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}

	/**
	 * 			get请求
	 * @param url
	 * @param resultModel
	 * @param callBack
	 */
	public void requestGetNoModel ( String url, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setErrorCallBack(true);
		mTask.setShowDialog(true);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}


	/**
	 * 			post请求
	 * @param url
	 * @param map
	 * @param resultModel
	 * @param callBack
	 */
	public void requestPostNoModel ( String url, Map<String, Object> map, Object resultModel, CallBack callBack )
	{
		mTask = new MyTask();
		mTask.setRequestState(mTask.POST);
		mTask.setUrl(url);
		mTask.setResultModel(resultModel);
		mTask.setCallBack(callBack);
		mTask.setMap(map);
		mTask.setErrorCallBack(true);
		mTask.setShowDialog(true);
		mRequest = new MyRequest(mTask);
		mRequest.start();
	}

}
