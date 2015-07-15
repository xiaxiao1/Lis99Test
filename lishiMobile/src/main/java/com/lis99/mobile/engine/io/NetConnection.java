package com.lis99.mobile.engine.io;

import com.lis99.mobile.util.C;
import com.lis99.mobile.util.L;
import com.lis99.mobile.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*******************************************
 * @ClassName: NetConnection 
 * @Description: 网络连接接口 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:57:57 
 *  
 ******************************************/
public class NetConnection {
	private static NetConnection instance = null;

	/** 网络响应类型 */
	public static final int HTTP_OK = HttpURLConnection.HTTP_OK;
	public static final int HTTP_PARTIAL = HttpURLConnection.HTTP_PARTIAL;

	/** 移动CMWAP网关地址 */
	private static final String CMCCGATEWAY = "10.0.0.172";
	/** 移动CMWAP网关代理Header */
	private static final String CMCCHOST = "X-Online-Host";

	/** 联网方式 */
	private static final int NORMAL = 0;
	private static final int CMWAP = 1;
	private static final int MAX_CONNSETTING = 1;

	/** 当前使用的联网方式 */
	private static int currentSetting = NORMAL;

	private NetConnection() {
	}

	private boolean init() {
		return true;
	}

	/**
	 * 多线程安全: 获取NetworkConnection句柄
	 */
	synchronized public static final NetConnection getInstance() {
		if (instance == null) {
			instance = new NetConnection();
			if (!instance.init()) {
				if (L.ERROR)
					L.e("NetworkConnection", "Failed to init itself");
			}
		}
		return instance;
	}

	/**
	 * 获得网络链接
	 * 
	 * @param url
	 *            为要打开的网络地址
	 * @return HttpConnection连接实例的值
	 */
	public Object getConnection(String url, boolean timeouts) throws Exception {
		switch (currentSetting) {
		case CMWAP:
			// cmwap联网方式
			return handleCmwap(url, timeouts);

		case NORMAL:
		default:
			// cmnet联网方式
			return handleHttp(url, timeouts);
		}
	}

	/**
	 * 处理普通的HTTP联网方式
	 * 
	 * @throws Exception
	 */
	private HttpURLConnection handleHttp(String url, boolean timeouts)
			throws Exception {
		return openConnection(url, timeouts);
	}

	/**
	 * 用于处理CMWAP链接
	 */
	private HttpURLConnection handleCmwap(String url, boolean timeouts)
			throws Exception {
		String urlRoot = StringUtil.getUrlRoot(url);
		int index = urlRoot.indexOf("://");
		if (index == -1)
			return null;

		urlRoot = urlRoot.substring(index + 3);
		String urlFile = url.substring(("http://" + urlRoot).length());
		if (urlFile == null)
			return null;

		HttpURLConnection conn = openConnection("http://" + CMCCGATEWAY
				+ urlFile, timeouts);
		setRequestProperty(conn, CMCCHOST, urlRoot);
		return conn;

	}

	/**
	 * Android接口: 创建网络连接
	 */
	private HttpURLConnection openConnection(String url, boolean timeouts)
			throws Exception {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		conn.setConnectTimeout(60000); // TODO: 60000 ms
		conn.setReadTimeout(60000);
		conn.setDoInput(true);
		return conn;
	}

	/**
	 * 过滤无线运营商返回的无用响应
	 * 
	 * @param response
	 * @return
	 */
	public boolean isValidResponse(HttpResponse response) {
		if (response == null)
			return false;

		String type = response.getContentType();
		if (type == null) {
			return true;
		} else if (type.indexOf("wml") == -1) {
			return true;
		} else {
			return false; // CMWAP返回的计费页面被视为无用响应
		}
	}

	/**
	 * 获得下一个联网设置
	 */
	public void getNextSetting() {
		++currentSetting;
	}

	public boolean hasMoreSettings() {
		return currentSetting < MAX_CONNSETTING - 1;
	}

	public void reset() {
		currentSetting = 0;
	}

	public void setRequestProperty(Object conn, String key, String value) {
		((HttpURLConnection) conn).setRequestProperty(key, value);
	}

	public String getHeaderField(Object conn, String key) {
		return ((HttpURLConnection) conn).getHeaderField(key);
	}

	public OutputStream openOutputStream(Object conn) throws IOException {
		return ((HttpURLConnection) conn).getOutputStream();
	}

	public InputStream openInputStream(Object conn) throws IOException {
		return ((HttpURLConnection) conn).getInputStream();
	}

	public void close(Object conn) {
		((HttpURLConnection) conn).disconnect();
	}

	public void setRequestMethod(Object conn, String method) throws IOException {
		HttpURLConnection connection = ((HttpURLConnection) conn);

		if (C.HTTP_POST.equals(method)) {
			connection.setDoOutput(true);
			connection.setRequestMethod(C.HTTP_POST);
		} else {
			connection.setRequestMethod(C.HTTP_GET);
		}
	}

	public int getResponseCode(Object conn) throws IOException {
		return ((HttpURLConnection) conn).getResponseCode();
	}
	
	public void setUseCaches(Object conn,boolean flag)throws IOException {
		((HttpURLConnection) conn).setUseCaches(flag);
	}
}
