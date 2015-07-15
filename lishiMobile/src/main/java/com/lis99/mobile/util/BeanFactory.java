/**
 * 
 */
package com.lis99.mobile.util;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工厂类
 * 
 * @author Administrator
 * 
 */
public class BeanFactory {

	private static Properties properties;

	static {
		properties = new Properties();
		InputStream in = BeanFactory.class.getClassLoader()
				.getResourceAsStream("bean.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			throw new RuntimeException("BeanFactory exception!");
		}
	}

	/**
	 * 获取具体业务实现类的对象
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getEngineImpl(Class<T> clazz) {
		String key = clazz.getSimpleName();

		String property = properties.getProperty(key);

		if (!StringUtils.isEmpty(property)) {
			try {
				return (T) Class.forName(property).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static <T> T getBean(Class<T> clazz) {
		String key = clazz.getSimpleName();

		String property = properties.getProperty(key);

		if (!StringUtils.isEmpty(property)) {
			try {
				return (T) Class.forName(property).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
