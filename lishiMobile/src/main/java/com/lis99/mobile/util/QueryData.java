/** 
 * 文件名：ShowData.java
 * 版本信息�?
 * 日期�?013-3-22
 */

package com.lis99.mobile.util;

/**
 * 
 * 项目名称�?framework
 * 
 * 类名称：ShowData
 * 
 * 类描述：
 * 
 * 创建人：吴安�?
 * 
 * @version
 * @param <T>
 * @param <T>
 * 
 */
public interface QueryData<T> {

	/**
	 * 调用接口数据
	 * 
	 * 从数据库、文件�?网络等媒体读取需要的数据
	 * 
	 * @return int 状�?�?
	 */
	String callData();

	/**
	 * 在界面显示数�?
	 */
	void showData(T t);
}
