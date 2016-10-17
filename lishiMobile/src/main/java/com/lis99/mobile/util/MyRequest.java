package com.lis99.mobile.util;

import android.os.AsyncTask;
import android.os.Build;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.MyException;
import com.lis99.mobile.engine.base.MyTask;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyRequest{

	private MyTask mTask;
	private MyHttpClient http;
	private OkHttpUtil okhttp;
	private static ExecutorService myExecutor;
	
	static{
		//线程池， 线程数量3
		myExecutor = Executors.newFixedThreadPool(3);
	}
	
	
	public MyRequest ( MyTask task )
	{
		mTask = task;
		http = new MyHttpClient();
		okhttp = OkHttpUtil.getInstance();
		
	}

	public void start ()
	{
		if (!InternetUtil.checkNetWorkStatus(LSBaseActivity.activity)) {
			DialogManager.getInstance().alert(LSBaseActivity.activity, "网络好像有问题");
			return;
		}
		MyAsync myAsync = new MyAsync();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			myAsync.executeOnExecutor(myExecutor, "");
		}
//		new MyAsync().execute("");
	}
	
	
	public class MyAsync extends AsyncTask
	{
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ( mTask.isShowDialog())
			{
				DialogManager.getInstance().stopWaitting();
			}

			if ( result == null )
			{
//				Common.toast("拉取失败");
//				Common.log("result==null");
//				错误返回
				if ( mTask!=null && mTask.getCallBack() != null )
				mTask.getCallBack().handlerError(mTask);
				return;
			}

			String res = result.toString();
			if ( result != null && mTask != null )
			{
				//jason 数据
				mTask.setresult(res);
//				jason解析类
				if ( mTask.getResultModel() != null )
				{
					mTask.setResultModel(ParserUtil.getParserResult(res, mTask.getResultModel(), mTask));
				}
				//没有生成解析类
				if (mTask.getResultModel() == null && !mTask.isErrorCallBack() )
				{
					return;
				}
				if ( mTask.getCallBack() != null )
					try {
						mTask.getCallBack().handler(mTask);
					} catch (NullPointerException e) {
						Common.Log_i("MyRequest catched error:"+e.getMessage());
					}

			}
			
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if ( mTask.isShowDialog())
			{
				try{
					DialogManager.getInstance().startWaiting(LSBaseActivity.activity, null, "数据加载中...");
				}
				catch (Exception e)
				{

				}
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String result = null;
			Common.log("RequestHttpURL="+mTask.getUrl());
			if ( mTask.getMap() != null )
			Common.log("RequestInfo="+mTask.getMap().toString());
			switch ( mTask.getRequestState() )
			{
			case MyTask.POST:
//				result = http.HttpPost(mTask.getUrl(), mTask.getMap());
				try {
					result = okhttp.post(mTask.getUrl(), mTask.getMap());
				}
				catch ( ConnectException e )
				{
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (ConnectTimeoutException e)
				{
					// 捕获ConnectionTimeout
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (SocketTimeoutException e)
				{
					// 捕获SocketTimeout
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (IOException e) {
					e.printStackTrace();
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				break;
			case MyTask.GET:
//				result = http.HttpGet(mTask.getUrl());
				try {
					result = okhttp.get(mTask.getUrl());
				}
				catch ( ConnectException e )
				{
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (ConnectTimeoutException e)
				{
					// 捕获ConnectionTimeout
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (SocketTimeoutException e)
				{
					// 捕获SocketTimeout
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				catch (IOException e) {
					e.printStackTrace();
					MyException myException = new MyException(e.getMessage(), MyException.TIMEOUT);
					mTask.setMyException(myException);
				}
				break;
			case MyTask.IMAGE:
				result = http.HttpImage(mTask.getUrl(), mTask.getMap());
				break;
			}
//			Common.log("Httpresult="+result);
//			Common.log("Httpresult="+Common.convert(result));

			return result;
		}
	}
	
	
	

}
