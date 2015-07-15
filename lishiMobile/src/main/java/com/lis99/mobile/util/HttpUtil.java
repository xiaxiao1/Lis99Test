package com.lis99.mobile.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class HttpUtil {

	/**
	 * 锟斤拷锟接凤拷锟斤拷锟斤拷 锟皆达拷锟斤拷锟絬rl锟斤拷锟斤拷锟斤拷写锟斤拷锟斤拷锟缴凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param url
	 *            锟斤拷锟斤拷锟街凤拷
	 * @return HttpURLConnection 锟斤拷锟斤拷锟斤拷锟斤拷
	 * @see [锟洁、锟斤拷#锟斤拷锟斤拷锟斤拷锟斤拷#锟斤拷员]
	 */
	public static HttpURLConnection connectServer(String url) {

		if (url == null || url.length() == 0) {
			return null;
		}
		// 通锟斤拷url锟斤拷锟斤拷锟斤拷锟皆凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		HttpURLConnection httpConn = null;
		try {
			URL urls = new URL(url);
			// 锟斤拷锟斤拷锟接ｏ拷锟饺达拷锟饺★拷锟较�
			httpConn = (HttpURLConnection) urls.openConnection();
		} catch (UnknownHostException e) {
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return httpConn;
	}

	/**
	 * 锟斤拷取锟窖撅拷锟斤拷锟接的凤拷锟斤拷锟斤拷锟剿碉拷锟斤拷锟斤拷
	 * 
	 * @param hc
	 *            HttpURLConnection
	 * @return byte[]
	 * @see [锟洁、锟斤拷#锟斤拷锟斤拷锟斤拷锟斤拷#锟斤拷员]
	 */
	public static String readFromServer(HttpURLConnection hc) {
		// 锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		InputStream is = null;
		String str = "";
		boolean gerror = false;
		try {
			hc.setRequestMethod("POST");
			hc.setRequestProperty("Content-Type", "text/xml");
			hc.setConnectTimeout(30000);
			hc.setReadTimeout(30000);
			// hc.setRequestProperty("Charset", "UTF-8");
			// Log.d(TAG, hc.set);
			// int length=hc.getContentLength();
			int rc = hc.getResponseCode();
			Log.i("HttpConnector", "ResponseCode :" + rc);
			// dis = hc.openInputStream();
			if (rc != HttpURLConnection.HTTP_OK) {
				gerror = true;
				Log.i("HttpConnector-sendToServer()-363", "ResponseCode :" + rc);

			} else {
				is = hc.getInputStream();
				str = convertStreamToString2(is);
				Log.i("str", str);
			}

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("HttpConnector-readFromServer()", e.getMessage()
					+ " _-exception");
			gerror = true;
			return "err";
		} finally {
			try {
				if (null != is) {
					is.close();
				}

			} catch (IOException e) {
				Log.e("HttpConnector-readGroupInfoFromServer()", e.getMessage()
						+ " _exception");
				gerror = true;
			} finally {
				if (gerror) {
					is = null;
				}
				// 锟截憋拷锟斤拷锟斤拷锟斤拷锟斤拷
				closeHttpConnection(hc);
			}
		}

		return str;
	}

	/**
	 * InputStream锟斤拷锟阶拷锟轿猻tring
	 * 
	 * @param is
	 *            锟斤拷锟斤拷锟�
	 * @return 锟街凤拷
	 * @return
	 */
	public static String convertStreamToString2(InputStream is) {

		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = is.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outSteam.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if (new String(outSteam.toByteArray()).equals("err")) {
			return "err";
		}
		try {
			return new String(outSteam.toByteArray(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "err";
	}

	/**
	 * 锟截憋拷锟斤拷锟斤拷
	 * 
	 * @param httpConn
	 *            httpConn
	 * @see [锟洁、锟斤拷#锟斤拷锟斤拷锟斤拷锟斤拷#锟斤拷员]
	 */
	public static void closeHttpConnection(HttpURLConnection httpConn) {
		if (httpConn == null) {
			return;
		} else {
			// 锟截憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
			httpConn.disconnect();
		}
	}

	/**
	 * 锟斤拷取锟窖撅拷锟斤拷锟接的凤拷锟斤拷锟斤拷锟剿碉拷锟斤拷锟捷ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟捷的ｏ拷
	 * 
	 * @param hc
	 * @param upData
	 *            锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * @return 锟窖撅拷锟斤拷锟接的凤拷锟斤拷锟斤拷锟剿凤拷锟截碉拷锟斤拷锟斤拷
	 * @see [锟洁、锟斤拷#锟斤拷锟斤拷锟斤拷锟斤拷#锟斤拷员]
	 */
	public static String readFromServer(HttpURLConnection hc, String upData) {
		// 锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		InputStream dis = null;

		byte[] buf = null;
		boolean error = false;
		DataOutputStream dos = null;
		String str = "";
		if (hc == null) {

			return str;
		}
		try {
			// 锟斤拷锟斤拷http头
			hc.setRequestMethod("POST");
			 //hc.setRequestProperty("Content-Type", "text/xml");
		    hc.setRequestProperty("Charset", "UTF-8");
			hc.setConnectTimeout(30000);
			hc.setReadTimeout(30000);

			hc.setDoInput(true);
			hc.setDoOutput(true);

			hc.setUseCaches(false);
			hc.setInstanceFollowRedirects(true);

			dos = new DataOutputStream(hc.getOutputStream());
			// 写锟斤拷突锟斤拷锟斤拷锟揭拷锟斤拷偷锟斤拷锟斤拷
			//dos.writeBytes(upData);
			dos.write(upData.getBytes("utf-8"));

			dos.flush();
			dos.close();
			// 锟斤拷应锟斤拷
			int rc = 0;

			rc = hc.getResponseCode();
			// String message=hc.
			// Log.d("", message);
			Log.i("str_json", "HttpURLConnection");
			if (rc != HttpURLConnection.HTTP_OK) {
				error = true;

			} else {
				dis = hc.getInputStream();
				str = convertStreamToString(dis);
			}
		} catch (IOException e) {
			error = true;
		} finally {
			try {
				if (null != dis) {
					dis.close();
				}
			} catch (IOException e) {
				error = true;
			} finally {
				if (error) {
					dis = null;
					dos = null;
				}
				// 锟截憋拷锟斤拷锟斤拷锟斤拷锟斤拷
				closeHttpConnection(hc);
			}
		}
		return str;
	}

	/**
	 * InputStream锟斤拷锟阶拷锟轿猻tring
	 * 
	 * @param is
	 *            锟斤拷锟斤拷锟�
	 * @return 锟街凤拷
	 */
	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + " ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				is.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 锟斤拷取锟窖撅拷锟斤拷锟接的凤拷锟斤拷锟斤拷锟剿碉拷锟斤拷锟斤拷
	 * 
	 * @param hc
	 *            HttpURLConnection
	 * @return byte[]
	 * @throws IOException 
	 * @see [锟洁、锟斤拷#锟斤拷锟斤拷锟斤拷锟斤拷#锟斤拷员]
	 */
	public static InputStream getPicture(HttpURLConnection hc) throws IOException {
		// 锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
		InputStream is = null;
		// Bitmap bitmap = null;
		// DataOutputStream dos = null;
		boolean gerror = false;
		try {
			hc.setRequestMethod("POST");
			hc.setRequestProperty("Content-Type", "text/xml");
			hc.setConnectTimeout(30000);
			hc.setReadTimeout(30000);
			hc.setDoInput(true);
			hc.setDoOutput(true);

			int rc = hc.getResponseCode();
			Log.i("HttpConnector - getpicture", "ResponseCode :" + rc);
			if (rc != HttpURLConnection.HTTP_OK) {
				gerror = true;
				Log.i("HttpConnector-sendToServer()-picture", "ResponseCode :"
						+ rc);
			} else {
				is = hc.getInputStream();
			}

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("HttpConnector-readFromServer()-picture", e.getMessage()
					+ " _-exception");
			gerror = true;
			return null;
		} finally {
			is.close();
			hc.disconnect();
		}
		return is;
	}

	/**
	 * 通锟斤拷httpurl锟斤拷取锟斤拷锟�
	 * 
	 * @param strUrl
	 * @return
	 * @throws Exception
	 */
	public static String questDataAsString(String strUrl, String data)
			throws Exception {
		String returnData = null;
		HttpURLConnection con = connectServer(strUrl);
		returnData = readFromServer(con, data);
		closeHttpConnection(con);
		return returnData;
	}

	/**
	 * 鑾峰彇鍥剧墖
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("null")
	public static InputStream getPic(String url) {
		InputStream is = null;
		HttpURLConnection conn = null;
		URL urls = null;
		try {
			urls = new URL(url);
			conn= (HttpURLConnection) urls.openConnection();
			conn.connect();
			int code = conn.getResponseCode();
			if (code == 200) {
			   InputStream istream = conn.getInputStream();
			   if (istream!=null) {
				   is = istream;
			   }else{
				   istream.close();
				   is.close();
			   }
			   
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 下载文件
	 * 
	 * @param url
	 * @return
	 */
	public boolean downFile(String url, String filePath) {

		URL imageUrl = null;
		try {
			imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				try {
					if (is != null) {
						// 需要保存
						byte[] imageBytes = new byte[conn.getContentLength()];
						int rc = 0;

						File file = new File(filePath);
						if (file.exists()) {
							file.delete();
						} else if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						FileOutputStream fos = new FileOutputStream(file);
                     //  ByteArrayOutputStream outOfMemoryError=new ByteArrayOutputStream();
						while ((rc = is.read()) != -1) {
							//outOfMemoryError.write(imageBytes, 0, rc);
							fos.write(rc);

						}
						fos.flush();
						fos.close();
						//outOfMemoryError.close();
						is.close();
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} catch (Error e) {
					e.printStackTrace();
					return false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public static void writeFile(String path, byte[] data) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		} else if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
