package com.lis99.mobile.engine.io;

import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.engine.base.MFormFile;
import com.lis99.mobile.engine.io.core.IOProtocol;
import com.lis99.mobile.engine.io.core.IOResponse;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.L;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/*******************************************
 * @ClassName: HttpProtocol 
 * @Description: HTTP 通信协议 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午2:54:37 
 *  
 ******************************************/
public class HttpProtocol extends IOProtocol {
	
	private static final String TAG = "HttpProtocol";

	/** HTTP 任务的类型 */
	public static final String GET = C.HTTP_GET;
	public static final String POST = C.HTTP_POST;

	/** 网络连接管理器 */
	private static NetConnection mNetConn;

	public HttpProtocol() {
		L.enable(TAG, L.DEBUG_LEVEL);
		mNetConn = NetConnection.getInstance();
	}

	/**
	 * 初始化
	 */
	public boolean init() {
		try {
			setType(HTTP);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 退出HTTP模块
	 */
	public boolean exit() {
		return true;
	}

	/**
	 * 检查 HTTP 请求是否符合要求
	 * 
	 * @param task
	 */
	public boolean validateRequest(IOTask task) {
		String method = task.getType();

		// 判断类型
		if (method != null) {
			method = method.toUpperCase();
			if (!(method.equals(GET) || method.equals(POST))) {
				if (L.WARN)
					L.w(TAG, "Unrecognized Method - " + method);
				return false;
			}
		} else {
			task.setType(GET);
		}

		// 判断 URL
		Object data = task.getData();
		if (!(data instanceof String)) {
			if (L.WARN)
				L.w(TAG, "Unrecognized URL - " + data);
			return false;
		}
		return true;
	}

	/**
	 * 处理 HTTP 请求
	 * 
	 * @param task
	 * @return
	 */
	public IOResponse handleRequest(IOTask task) {
		if (task == null)
			return null;

		boolean isPOST = false;
		HttpResponse response = null;

		try {
			// ------------------------------------------------------------------
			// 1. 预处理阶段
			// ------------------------------------------------------------------

			// 验证任务的有效性, 这可以及时URL重定向的过程中断
			if (task.isCanceled())
				return null;

			// 创建并初始化HTTP请求的响应对象
			response = new HttpResponse();
			response.setTask(task);
			task.setResponse(response);

			// 检查是否为POST操作
			String method = task.getType();
			if (method.equals(POST))
				isPOST = true;

			String url = (String) task.getData();
			if (L.DEBUG)
				L.d(TAG, task.getType().toUpperCase() + " [" + url + "]");

			// 验证待提交的数据, 并更新 URL
			byte[] postData = null;
			if (isPOST) {
				Object param = task.getPostData();
				if (param != null && param instanceof byte[]) {
					postData = (byte[]) param;
				}
			}

			// ------------------------------------------------------------------
			// 2. 连接远程服务器, 处理IO请求
			// ------------------------------------------------------------------
			if (task.isCanceled())
				return null;

			// 建立服务器连接
			Object conn = mNetConn.getConnection(url, true);
			if (conn == null) {
				response.setCode(HttpResponse.OPEN_EXCEPTION,
						"Failed to Open Connection");
				return response;
			}
			response.setConnection(conn); // 将链接句柄保存到Response对象中

			// 设置HTTP头属性: 公共部分
			mNetConn.setRequestMethod(conn, task.getType().toUpperCase());
			// 向服务器提交数据 (POST方式)
			if (isPOST && task.getParameter() != null && !(task.getParameter() instanceof String)) {
				mNetConn.setUseCaches(conn, false);// 不使用Cache
				mNetConn.setRequestProperty(conn, "Connection", "keep-alive");
				mNetConn.setRequestProperty(conn, "Charset", C.CHARSET);
				mNetConn.setRequestProperty(conn, "Content-Type",
						C.MULTIPART_FROM_DATA + ";boundary=" + C.BOUNDARY);

				StringBuilder sb = new StringBuilder();
				Map<String, String> params = new HashMap<String, String>();
				if (task.getPostData() != null) {
					params = (Map<String, String>) task.getPostData();
				}

				// 上传的表单参数部分，格式请参考文章
				for (Map.Entry<String, String> entry : params.entrySet()) {// 构建表单字段内容
					sb.append(C.PREFIX);
					sb.append(C.BOUNDARY);
					sb.append(C.LINEND);
					sb.append("Content-Disposition: form-data;name=\""
							+ entry.getKey() + "\"" + C.LINEND);
					sb.append("Content-Type: text/plain; charset=" + C.CHARSET
							+ C.LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + C.LINEND);
					sb.append(C.LINEND);
					sb.append(entry.getValue());
					sb.append(C.LINEND);
				}

				OutputStream os = response.getOutputStream();
				DataOutputStream outStream = new DataOutputStream(os);
				outStream.write(sb.toString().getBytes());// 发送表单字段数据

				MFormFile[] files = (MFormFile[]) task.getParameter();
				// 上传的文件部分，格式请参考文章
				for (MFormFile file : files) {
					StringBuilder split = new StringBuilder();
					split.append(C.PREFIX);
					split.append(C.BOUNDARY);
					split.append(C.LINEND);
					split.append("Content-Disposition: form-data;name=\""
							+ file.getParameterName() + "\";filename=\""
							+ file.getFilname() + "\"" + C.LINEND);
					split.append("Content-Type:" + file.getContentType()
							+ "; charset=" + C.CHARSET + C.LINEND);
					split.append(C.LINEND);
					outStream.write(split.toString().getBytes());

					if (file.getInStream() != null) {
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = file.getInStream().read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}
						file.getInStream().close();
					} else {
						outStream.write(file.getData(), 0,
								file.getData().length);
					}
					outStream.write(C.LINEND.getBytes());
				}
				// 请求结束标志
				byte[] end_data = (C.PREFIX + C.BOUNDARY + C.PREFIX + C.LINEND)
						.getBytes();
				outStream.write(end_data);
				outStream.flush();
				outStream.close();

			} else if (isPOST && postData != null && postData.length > 0) {
				mNetConn.setRequestProperty(conn, "Content-Length", ""
						+ postData.length);
				mNetConn.setRequestProperty(conn, "Content-Type",
						"application/json; charset=UTF-8");
				mNetConn.setRequestProperty(conn, "Content-Type",
						"application/x-www-form-urlencoded");
				OutputStream out = response.getOutputStream();
				if (out != null) {
					out.write(postData);
					out.flush(); // TODO: 移植过程中需要仔细注意
					out.close();
				}

				if (L.DEBUG)
					L.d("HttpClient", "Post data[" + postData.length
							+ "] sent.content[" + new String(postData) + "]");
			}

			// ------------------------------------------------------------------
			// 3. 获取服务器的反馈信息
			if (L.DEBUG)
				L.d(TAG, "Retrieving response code ... " + " [" + url + "]");
			int code = mNetConn.getResponseCode(conn);
			response.setCode(code); // 保存服务器的响应代码
			if (L.DEBUG)
				L.d(TAG, "Response code (" + code + "), " + " [" + url + "]");
			// ------------------------------------------------------------------

			// 解析服务器的反馈信息
			response.parseHeaderFields();

			// ------------------------------------------------------------------
			// 4. 服务器返回 HTTP_OK, HTTP_PARTIAL 时, 表示当前获得的是正常响应.
			switch (code) {
			case NetConnection.HTTP_OK:
			case NetConnection.HTTP_PARTIAL:
				return response;
				// 其它响应, 默认为错误响应
			default:
				if (code <= 0) {
					response.setCode(HttpResponse.FAIL_TO_DOWNLOAD,
							"HTTP: Received invalid response code " + "["
									+ code + "]");
				} else {
					response.setCode(HttpResponse.UNKNOWN_CODE, "HTTP: "
							+ String.valueOf(code));
				}
				return response;
			}

		} catch (SecurityException sex) {
			if (L.ERROR)
				L.e(TAG,
						"Security Exception while connecting "
								+ (String) task.getData(), sex);

			// 记录错误信息
			if (response != null) {
				response.setCode(HttpResponse.SECURITY_ISSUE,
						"HTTP: " + sex.getMessage());
				response.close();
			}
			return response;

		} catch (OutOfMemoryError oomx) {
			if (L.ERROR)
				L.e(TAG, "OOM while connecting " + (String) task.getData());

			// 记录错误信息
			if (response != null) {
				response.setCode(HttpResponse.OUT_OF_MEMORY,
						"HTTP: " + oomx.getMessage());
				response.close();
			}
			return response;

		} catch (UnknownHostException cnfe) {
			if (response != null) {
				response.setCode(HttpResponse.CONNECTION_NOT_FOUND, "HTTP: "
						+ cnfe.getMessage());
				response.close();
			}
			return response;
		} catch (SocketTimeoutException ste) {
			// 记录错误信息
                  ste.printStackTrace();
			if (response != null) {
				// HttpURLConnection中的 ConnectTimeout和readTimeout都会抛此异常
				response.setCode(HttpResponse.SOCKET_TIME_OUT_EXCEPTION,
						"HTTP: " + ste.getMessage());
				response.close();
			}
			return response;
		} catch (IOException iox) {
			if (L.ERROR)
				L.e(TAG,
						"IO Exception while connecting "
								+ (String) task.getData(), iox);

			// 记录错误信息
			if (response != null) {

				response.setCode(HttpResponse.IO_EXCEPTION,
						"HTTP: " + iox.getMessage());
				response.close();
			}
			return response;

		} catch (Exception ex) {
			if (L.ERROR)
				L.e(TAG,
						"Unknown Exception while connecting "
								+ (String) task.getData(), ex);

			// 记录错误信息
			if (response != null) {
				response.setCode(HttpResponse.UNKNOWN_EXCEPTION,
						"HTTP: " + ex.getMessage());
				response.close();
			}
			return response;

		}
	}

	/**
	 * 处理服务器的不同响应
	 * 
	 * @param mResponse
	 * @return true - 成功; false - 失败
	 */
	public int handleResponse(IOResponse rsp) {
		String url = null;

		try {
			/* 验证任务的有效性: 必须在try-catch内部, 从而复用finally中的操作 */
			if (rsp == null)
				return C.FAIL;
			IOTask task = rsp.getTask();
			if (task == null)
				return C.FAIL;
			if (task.isCanceled())
				return C.FAIL;

			HttpResponse response = (HttpResponse) rsp;
			url = (String) task.getData();
			boolean downloadNeeded = true;

			// ------------------------------------------------------------------
			// 0. 去除移动网络运营商返回的无效响应
			if (!mNetConn.isValidResponse(response)) {
				response.setCode(HttpResponse.FAIL_TO_DOWNLOAD,
						"HTTP: Useless Response from Operator");
				return C.FAIL;
			}

			// ------------------------------------------------------------------
			// 1. 根据服务器响应做预处理
			int rspCode = response.getCode();
			switch (rspCode) {
			/* 服务器响应正常 */
			case NetConnection.HTTP_OK:
				task.reset();
				task.setType(response.getContentType());
				task.setContentLength(response.getContentLength());
				break;

			/* 服务器其它响应一律被当做失败来处理 */
			default:
				return C.FAIL;
			}
			String contentType = response.getContentType();
			ImageCacheManager imageCacheManager = ImageCacheManager.getInstance();
			// 如果是图片，则保存下载数据到缓存
			/*if (contentType.startsWith("image")) {
				imageCacheManager = ImageCacheManager.getInstance();
				imageCacheManager.addBitmapToCache(url, null);
			}*/
			// ------------------------------------------------------------------
			// 2. 实际下载数据, 然后关闭网络链接
			//
			// 注意: 必须尽可能地缩短这段代码与Connector.open之间的距离, 从而
			// 避免服务器端并发连接数目过多.
			if (task.getContentLength() == 0) { // 服务器返回数据为空
				task.setData(null); // 无需下载, 直接返回空的数据
				return C.OK;
			} else if (downloadNeeded) {
				byte[] bout = download(response); // 网络下载数据
				if (bout != null
						&& (rspCode == NetConnection.HTTP_OK || rspCode == NetConnection.HTTP_PARTIAL)) {
					task.setData(bout); // 数据已全部下载
					// 保存下载数据到缓存
					if (contentType.startsWith("image")) {
						imageCacheManager.addBitmapToCache(url, bout);
					}
				} else if (task.isCanceled()) {
					// 下载失败重缓存中移除
					if (contentType.startsWith("image")) {
						imageCacheManager.removeBitmapFromCache(url);
					}
					return C.FAIL;
				} else {
					if (L.DEBUG)
						L.d(TAG, "Failed to download " + url);
					response.setCode(HttpResponse.FAIL_TO_DOWNLOAD,
							"HTTP: Failed to Downoad Data from Server");
					// 下载失败重缓存中移除
					if (contentType.startsWith("image")) {
						imageCacheManager.removeBitmapFromCache(url);
					}
					return C.FAIL;
				}
			}
			// 尽早关闭网络链接, 以提高服务器吞吐率
			response.close();
			return C.OK;

		} catch (OutOfMemoryError oomx) {
			if (L.ERROR)
				L.e(TAG, "OOM while loading data from " + url, oomx);

			// 记录错误信息
			if (rsp != null)
				rsp.setCode(HttpResponse.OUT_OF_MEMORY, oomx.getMessage());
			return C.FAIL;

		} catch (SocketTimeoutException ste) {
			// 记录错误信息
			if (rsp != null) {
				// HttpURLConnection中的 ConnectTimeout和readTimeout都会抛此异常
				rsp.setCode(HttpResponse.SOCKET_TIME_OUT_EXCEPTION, "HTTP: "
						+ ste.getMessage());
			}
			return C.FAIL;
		} catch (IOException iox) {
			if (L.ERROR)
				L.e(TAG, "IO Exception while loading data from " + url, iox);

			// 记录错误信息
			if (rsp != null)
				rsp.setCode(HttpResponse.IO_EXCEPTION, iox.getMessage());
			return C.FAIL;

		} catch (Exception ex) {
			if (L.ERROR)
				L.e(TAG, "Unknown Exception while loading data from " + url, ex);

			// 记录错误信息
			if (rsp != null)
				rsp.setCode(HttpResponse.UNKNOWN_EXCEPTION, ex.getMessage());
			return C.FAIL;

		} finally {
			// 必须保障网络链接在使用完毕后被关闭
			if (rsp != null) {
				rsp.close();
			}
		}
	}

	/**
	 * 完成任务的扫尾工作
	 * 
	 * @param task
	 */
	public void finishRequest(IOTask task) {
		if (task == null || task.isCanceled())
			return;

		HttpResponse response = (HttpResponse) task.getResponse();
		if (response == null)
			return;
		String url = null;
		try {
			// 如果是图片资源则保存数据到本地Cache
		} catch (Exception x) {
			if (L.DEBUG)
				L.d(TAG, "Failed to save data to Local Cache [" + url
						+ "], ignored, " + "Exception: " + x.getMessage());
		}
	}

	/**
	 * 从网络下载数据的主体函数
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected byte[] download(HttpResponse response) throws Exception {
		IOTask task = response.getTask();
		if (task == null)
			return null;
		ByteArrayOutputStream bout = null;
		try {
			InputStream in = response.getInputStream();
			int length = response.getContentLength();

			// 创建接收数据的缓冲区
			byte buf[] = new byte[4096];
			if (length > 0) { // 若服务器指定文件长度, 则直接创建相同大小的缓冲区
				bout = new ByteArrayOutputStream(length);
			} else { // 否则, 只创建一个临时缓冲区
				bout = new ByteArrayOutputStream(buf.length);
			}
			// 下载数据到缓冲区
			int size;
			int downloaded = task.getCurDataSize();
			while ((size = in.read(buf)) > -1
					&& (length == -1 || downloaded < length)) {
				// 主动判断任务是否被取消, 加速返回过程
				if (task.isCanceled())
					return null;

				// 写入缓冲区(自动增大)
				bout.write(buf, 0, size);
				downloaded += size;
				task.setCurDataSize(downloaded); // 更新已下载数据的总数
			}

			// 如果实际下载的数据超过服务器指定的长度, 报警
			if (length > -1) {
				if (task.getCurDataSize() > length) {
					if (L.WARN)
						L.w(TAG, "Got more Bytes than Content-Length"
								+ ", while loading " + task.getData()
								+ ", [Content-Length=" + length
								+ ", Downloaded=" + downloaded + "]");
				}
			}
			byte[] result = bout.toByteArray();
			bout.close();
			bout = null;

			// 如果实际下载长度小于服务器指定的长度, 返回空
			if (downloaded < length) {
				if (L.DEBUG)
					L.w(TAG, "Got less Bytes than Content-Length"
							+ ", while loading " + task.getData()
							+ ", [Content-Length=" + length + ", Downloaded="
							+ downloaded + "]");
				return null;
			}

			return result;
		} finally {
		}
	}

	/**
	 * 取消当前请求
	 * 
	 * @param task
	 */
	public void cancelRequest(IOTask task) {
		IOResponse response = (IOResponse) task.getResponse();
		if (response != null) {
			response.close();
		}
	}
}
