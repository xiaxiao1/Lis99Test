package com.lis99.mobile.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.newhome.LSFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ParserUtil {

	public static Object getJacksonParser ( String result, Object o )
	{
		try {
			JsonNode root = LSFragment.mapper.readTree(result);
			String errCode = root.get("status").asText("");
			if (!"OK".equals(errCode)) {
				Common.toast(errCode);
				return null;
			}
			JsonNode data = root.get("data");
			o = LSFragment.mapper.readValue(data.traverse(),  o.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public static Object getGosnParser ( String result, Object o )
	{
//		Common.log("parserResult="+result);
		Gson gson = new Gson();
		o = gson.fromJson(result, o.getClass());
		return o;
	}
	
	public static Object getFastJson ( String result, Object o )
	{
		o = JSON.parseObject(result, o.getClass());
		return o;
	}
	
	public static String getGsonString ( Object o )
	{
		String result = null;
		Gson gson = new Gson();
		result = gson.toJson(o);
		return result;
	}
	
	public static String getJacksonString ( Object o )
	{
		String result = null;
		try {
			result = LSFragment.mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getFastJsonString ( Object o )
	{
		String result = null;
		result = JSON.toJSONString(o);
		return result;
	}
	/**
	 *  解析
	 * @param result	Json 数据
	 * @param o			数据实体类
	 * @return
	 */
	public static Object getParserResult ( String result, Object o, MyTask task )
	{
		//fastJson
//		JSONObject json1 = JSONObject.parseObject(result);
//		String ok1 = json1.getString("status");
		try {
			JsonObject json = (JsonObject) new JsonParser().parse(result);
			String errCode = json.get("status").getAsString();
		if (!"OK".equals(errCode)) {
			String date = json.get("data").toString();
			JsonObject dat = (JsonObject) new JsonParser().parse(date);
			String errorstr = dat.get("error").getAsString();
			if ( !TextUtils.isEmpty(errorstr) )
			{
				if ( task != null )
				{
					if ( task.isShowErrorTost() )
					{
						Common.toast(errorstr);
						Common.log("resultError="+errorstr);
					}
				}
			}
			return null;
		}
		String data = json.get("data").toString();
//		data = getUTF8(data);
		o = getGosnParser(data, o);
//			Common.log("Httpresult="+Common.convert(result));
			Common.log("Httpresult="+result);
		}
		catch ( JsonSyntaxException e )
		{
			Common.log("parser error ="+e.getMessage()+"\n"+result);
			return null;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			Common.log("parser error1 ="+e.getMessage()+"\n"+result);
			e.printStackTrace();
			return null;
		}
		return o;
	}
	
	private static String getUTF8 ( String str )
	{
		String result = null;
		try
		{
			result = new String(str.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Common.log("ParserUtil getUTF-8 ERROE=" + e.getMessage());
			return str;
		}
		return result;
	}

	/**
	 *		拼装JsonArray，
	 * @param name	表名称
	 * @param info	表数据
	 * @return
	 */
	public static String getJsonArrayWithName (String name, String info )
	{
		String result = "";
		try {
			JSONArray aj = new JSONArray(info);
			JSONObject jo = new JSONObject();
			jo.put(name, aj);
			result = jo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}
}
