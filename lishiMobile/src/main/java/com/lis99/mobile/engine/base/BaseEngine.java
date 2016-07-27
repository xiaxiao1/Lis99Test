/**
 * 
 */
package com.lis99.mobile.engine.base;

import com.lis99.mobile.util.C;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;


/**
 * 业务公共部分
 * @author Administrator
 *
 */
public abstract class BaseEngine {
	HttpUriRequest httpUriRequest=null;
	
	/**
	 * Http请求
	 * @param url 请求的url
	 * @param method 请求的方式
	 * @return
	 */
	public HttpUriRequest getHttpRequest(String url,String method){
		
		if(method.equalsIgnoreCase("get")){
			
			httpUriRequest=new HttpGet(C.getDOMAIN()+url);
		}else if(method.equalsIgnoreCase("post")){			
			httpUriRequest=new HttpPost(C.getDOMAIN()+url);
		}
		addRequestHeader();
		return httpUriRequest;
	}
	private void addRequestHeader(){		
		//添加公共的header
		httpUriRequest.addHeader("appkey", "Md5");
		httpUriRequest.addHeader("userid","");
		
	}
}
