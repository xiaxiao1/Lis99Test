package com.lis99.mobile.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpNetRequest {
                  /**
                   * Post请求
                   * @param url 
                   * @param values
                   * @return
                   */
	public String connect(String url, Map<String, Object> values) {
		HttpPost httpPost = null;
		httpPost = new HttpPost(url);
		String jsonStr = "";
		if (httpPost != null) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			if (values != null && !values.isEmpty()) {
				// if (!CompareUtils.isEmpty(values)) {
				Set<Entry<String, Object>> set = values.entrySet();
				for (Entry<String, Object> entry : set) {
					parameters.add(new BasicNameValuePair(entry.getKey(),
							String.valueOf(entry.getValue())));
				}
			}
			try {
//				httpPost.setEntity(new UrlEncodedFormEntity(parameters,"GBK"));
				httpPost.setEntity(new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse httpResponse = setHttp(httpPost);
			if (httpResponse != null) {
				jsonStr = jsonStr(httpResponse, url);
			}
		}
		return jsonStr;

	}
	/**
	 * Get请求
	 * @param url
	 * @param data
	 * @return
	 */
	public String connect(String url ,String data){
		
			Doget doget = new Doget();
			 HttpGet httpRequest = new HttpGet(url+data);  
			 String jsonstr="";
		 /*发送请求并等待响应*/  
		 try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			 if(httpResponse.getStatusLine().getStatusCode() == 200)   
	          {  
	            /*读*/  
				 jsonstr= EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);  
	          }  
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
		 return jsonstr;
	}

	/**
	 * 设置HTTP协议
	 * 
	 * @param parameters
	 * @param aspx
	 * @return
	 */
	private HttpResponse setHttp(HttpUriRequest request) {
		HttpClient httpClient = getHttpClient();
		HttpResponse httpResponse = null;
		try {

			httpResponse = httpClient.execute(request);
			// HttpGet httpGet = new
			// HttpGet(BASE_URL+aspx+"?ss=XY2013&sujectID=189");
			// HttpParams hp = httpGet.getParams();
			// hp.getParameter("true");
			// httpResponse = httpClient.execute(httpGet);

		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// httpClient.getConnectionManager().shutdown();
		}
		return httpResponse;

	}

	/**
	 * 从服务器获取JSON
	 * 
	 * @param httpResponse
	 * @return
	 */
	private String jsonStr(HttpResponse httpResponse, String url) {
		String result = "";
		int code = httpResponse.getStatusLine().getStatusCode();
		if (code == HttpStatus.SC_OK) {
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				
				result = getStreamString(httpEntity.getContent());
				// result = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			result = "";
			Log.i("从服务器获取JSON出错", url 
					+ httpResponse.getStatusLine().getStatusCode());
		}
		return result;
	}

	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream, HTTP.UTF_8));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	private DefaultHttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
