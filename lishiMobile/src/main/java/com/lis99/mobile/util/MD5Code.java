package com.lis99.mobile.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*******************************************
 * @ClassName: MD5Code 
 * @Description: md5帮助类 
 * @author xingyong  cosmos250.xy@gmail.com 
 * @date 2013-12-25 下午3:03:56 
 *  
 ******************************************/
public class MD5Code {
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F' };
	public static String toHexString(byte[] b) {  
		 StringBuilder sb = new StringBuilder(b.length * 2);  
		 for (int i = 0; i < b.length; i++) {  
		     sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
		     sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		 }  
		 return sb.toString();  
	}
	
	/**
	 * MD5加密
	 * @param message 要加密的字符串
	 * @return
	 */
	public static String encode(String message){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		byte[] result = md5.digest(message.getBytes());
		return toHexString(result).toLowerCase();
	}
}
