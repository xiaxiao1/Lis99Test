package com.lis99.mobile.util;

import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MyHttpClient {
	
	private HttpClient mClient;
	
	private HttpGet mHttpGet;
	private HttpPost mHttpPost;
	private int TIME_OUT = 15000;
	
	public MyHttpClient()
	{
		mClient = getHttpClient();
	}

	public String HttpImage (String url, Map<String, Object> map )
	{
		String result = null;

		try {

			if (TextUtils.isEmpty(url) || map == null || map.isEmpty() )
			{
				return null;
			}
			mHttpPost = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			Set<Entry<String, Object>> set = map.entrySet();
			for ( Entry<String, Object> entry : set )
			{
				if ( "photo".equals(entry.getKey()))
				{
					entity.addPart(entry.getKey(), new ByteArrayBody((byte[])entry.getValue(), "image.jpg"));
					continue;
				}
				entity.addPart(entry.getKey(), new StringBody((String)entry.getValue()));
			}
			mHttpPost.setEntity(entity);

			HttpResponse response = mClient.execute(mHttpPost);

			int status = response.getStatusLine().getStatusCode();

			if ( status == HttpStatus.SC_OK )
			{
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}

		}
		catch ( ConnectException e )
		{
			Common.log("ConnectException get="+e.getMessage());
		}
		catch (ConnectTimeoutException e)
		{
			// 捕获ConnectionTimeout
			Common.log("ConnectTimout Get="+e.getMessage());
		}
		catch (SocketTimeoutException e)
		{
			// 捕获SocketTimeout
			Common.log("SocketTimout Get="+e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Common.log("MyHttpClient Get Error="+e.getMessage());
		}

		return result;
	}

	public String HttpPost (String str, Map<String, Object> values)
	{
		String result = null;
		try {
			mHttpPost = new HttpPost(str);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			if (values != null && !values.isEmpty()) {
				// if (!CompareUtils.isEmpty(values)) {
				Set<Entry<String, Object>> set = values.entrySet();
				for (Entry<String, Object> entry : set) {
					parameters.add(new BasicNameValuePair(entry.getKey(),
							String.valueOf(entry.getValue())));
				}
			}
			mHttpPost.setEntity(new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8));
			Common.log("post Entity = "+EntityUtils.toString(mHttpPost.getEntity(), "UTF-8"));
			HttpResponse responce = mClient.execute(mHttpPost);
			
			if ( responce.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
			{
				result = EntityUtils.toString(responce.getEntity(), "UTF-8");
			}
			
		} 
		catch ( ConnectException e )
		{
			Common.log("ConnectException post="+e.getMessage());
		}
		catch (ConnectTimeoutException e)
        {
                // 捕获ConnectionTimeout
			Common.log("ConnectTimout Post="+e.getMessage());
        }
        catch (SocketTimeoutException e)
        {
                // 捕获SocketTimeout
        	Common.log("SocketTimout Post="+e.getMessage());
        }
		catch (Exception e) {
			// TODO: handle exception
			Common.log("MyHttpClient Post Error="+e.getMessage());
		}
		
		
		return result;
	}
	
	public String HttpGet ( String str )
	{
		String result = null;
		try {
			mHttpGet = new HttpGet(str);
			HttpResponse response = mClient.execute(mHttpGet);
			int responceCode = response.getStatusLine().getStatusCode();
			//连接成功
			if ( responceCode == HttpStatus.SC_OK )
			{
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
			
			
		}
		catch ( ConnectException e )
		{
			Common.log("ConnectException get="+e.getMessage());
		}
		catch (ConnectTimeoutException e)
        {
                // 捕获ConnectionTimeout
			Common.log("ConnectTimout Get="+e.getMessage());
        }
        catch (SocketTimeoutException e)
        {
                // 捕获SocketTimeout
        	Common.log("SocketTimout Get="+e.getMessage());
        }
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Common.log("MyHttpClient Get Error="+e.getMessage());
		}
		
		
		return result;
	}
	

	/**
	 * 创建httpClient实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private  HttpClient getHttpClient() {
		if (null == mClient) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时 */
			ConnManagerParams.setTimeout(params, TIME_OUT);
			HttpConnectionParams
					.setConnectionTimeout(params, TIME_OUT);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, TIME_OUT);
			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			mClient = new DefaultHttpClient(conMgr, params);
		}
		return mClient;
	}

}
