/**
 * 
 */
package com.lis99.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 *
 */
public class Stream2StringUtils {
	public static String inputStream2String(InputStream in){		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		int len=0;
		byte[] buf=new byte[102400];
		try {
			while ((len = in.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(bos!=null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		return bos.toString();
		
	}
}
