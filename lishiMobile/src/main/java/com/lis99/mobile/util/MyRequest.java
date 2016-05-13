package com.lis99.mobile.util;

import android.os.AsyncTask;
import android.os.Build;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.MyTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyRequest{

	private MyTask mTask;
	private MyHttpClient http;
	private static ExecutorService myExecutor;
	
	static{
		//线程池， 线程数量3
		myExecutor = Executors.newFixedThreadPool(3);
	}
	
	
	public MyRequest ( MyTask task )
	{
		mTask = task;
		http = new MyHttpClient();
		
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
				return;
			}

			//没有生成解析类
			if (mTask.getResultModel() == null && !mTask.isErrorCallBack() )
			{
				return;
			}
			if ( mTask.getCallBack() != null )
				mTask.getCallBack().handler(mTask);


//			String res = result.toString();
//			if ( result != null && mTask != null )
//			{
//				//jason 数据
//				mTask.setresult(res);
////				jason解析类
//				if ( mTask.getResultModel() != null )
//				{
//					mTask.setResultModel(ParserUtil.getParserResult(res, mTask.getResultModel(), mTask));
//				}
//				//没有生成解析类
//				if (mTask.getResultModel() == null && !mTask.isErrorCallBack() )
//				{
//					return;
//				}
//				if ( mTask.getCallBack() != null )
//				mTask.getCallBack().handler(mTask);
//			}
			
			
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
			Common.log("HttpURL="+mTask.getUrl());
			switch ( mTask.getRequestState() )
			{
			case MyTask.POST:
				result = http.HttpPost(mTask.getUrl(), mTask.getMap());
				break;
			case MyTask.GET:
				result = http.HttpGet(mTask.getUrl());
				break;
			case MyTask.IMAGE:
				result = http.HttpImage(mTask.getUrl(), mTask.getMap());
				break;
			}
//			Common.log("Httpresult="+result);
			Common.log("Httpresult="+Common.convert(result));

			if ( result == null )
			{
//				Common.toast("拉取失败");
//				Common.log("result==null");
				return null;
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
			}




			return result;
		}
	}
	
	
	

}
