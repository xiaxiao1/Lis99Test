/** 
 * 文件名：DataParseFactory.java
 * 版本信息�?
 * 日期�?013-4-2
 */

package com.lis99.mobile.json;

/**
 * 
 * 项目名称：framework 类名称：DataParseFactory 类描述： 创建人：吴安�?创建时间�?013-4-2 下午7:29:29
 * 修改人：吴安�?修改时间�?013-4-2 下午7:29:29 修改备注�?
 * 
 * @version
 * 
 */
public class DataParseFactory {
	public static boolean BUG = false;

	public static <T> IDataParse<T> newDataParse(String str) {
		return new JSONParse<T>();
	}
}
