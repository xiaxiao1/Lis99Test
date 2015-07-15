/** 
 * 文件名：DataParse.java
 * 版本信息�?
 * 日期�?013-3-29
 */

package com.lis99.mobile.json;

/**
 * 
 * 项目名称：framework 类名称：DataParse 类描述： 创建人：吴安�?创建时间�?013-3-29 上午9:33:01 修改人：吴安�?
 * 修改时间�?013-3-29 上午9:33:01 修改备注�?
 * 
 * @version
 * 
 */
public interface IDataParse<T> {
	T toBean(String data, Class<T> clazz);
}
