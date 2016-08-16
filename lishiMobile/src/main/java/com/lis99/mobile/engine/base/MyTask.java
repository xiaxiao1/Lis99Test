package com.lis99.mobile.engine.base;

import java.util.Map;

public class MyTask{

	
	public static final int POST = 0;
	
	public static final int GET = 1;
	
	public static final int IMAGE = 2;
	
	public int RequestState = GET;

	public CallBack callBack;
	//post用到的
	public Map<String, Object> map;

	public boolean showDialog;

	//  解析错误， 是否回调
	public boolean ErrorCallBack = false;

	//显示错误信息文本提示， 默认显示
	public boolean showErrorTost = true;

	public String url;

	public String result;
	//请求标识
	public int RequestCode;
	//返回模型类
	public Object resultModel;

	public String error;



	public MyTask ()
	{

	}
	//get
	public MyTask ( String url, int RequestState, CallBack callBack )
	{
		this.url = url;
		this.RequestState = RequestState;
		this.callBack = callBack;

	}
	//post
	public MyTask ( String url, int RequestState, CallBack callBack, Map<String, Object> map )
	{
		this.url = url;
		this.RequestState = RequestState;
		this.callBack = callBack;
		this.map = map;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setErrorCallBack(boolean errorCallBack) {
		ErrorCallBack = errorCallBack;
	}

	public boolean isErrorCallBack() {
		return ErrorCallBack;
	}

	public boolean isShowErrorTost() {
		return showErrorTost;
	}

	public void setShowErrorTost(boolean showErrorTost) {
		this.showErrorTost = showErrorTost;
	}
	public boolean isShowDialog()
	{
		return showDialog;
	}
	public void setShowDialog(boolean showDialog)
	{
		this.showDialog = showDialog;
	}
	public Map<String, Object> getMap()
	{
		return map;
	}
	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}
	public CallBack getCallBack() {
		return callBack;
	}

	
	
	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}


	public int getRequestState() {
		return RequestState;
	}

	public void setRequestState(int requestState) {
		RequestState = requestState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getresult() {
		return result;
	}

	public void setresult(String postEntity) {
		result = postEntity;
	}
	
	public Object getResultModel() {
		return resultModel;
	}
	public void setResultModel(Object resultModel) {
		this.resultModel = resultModel;
	}
	
	
}
