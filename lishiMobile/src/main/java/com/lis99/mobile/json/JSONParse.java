package com.lis99.mobile.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON Object生成工具�?
 * 
 * 项目名称：hangout
 * 
 * 类名称：JSONObjectUtils
 * 
 * 类描述：
 * 
 * 创建人：吴安�?
 * 
 * 创建时间�?013-3-25 上午9:01:44
 * 
 * @version
 * 
 */
public final class JSONParse<T> implements IDataParse<T> {

	public JSONParse() {
	}

	static ObjectMapper mObjectMapper = new ObjectMapper();

	static{
		mObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	
	@Override
	public T toBean(String data, Class<T> clazz) {

		try {
			return mObjectMapper.readValue(data, clazz); // mObjectMapper.readValue(data,
															// new
															// TypeReference<T>()
															// {
			// });
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
