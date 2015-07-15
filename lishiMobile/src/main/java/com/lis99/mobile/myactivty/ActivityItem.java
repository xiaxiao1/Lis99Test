package com.lis99.mobile.myactivty;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityItem {
	private int id;
	private int renqizhi;
	private String title;
	private String uid;
	private String userInfo;
	private String headicon;
	private String nickname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRenqizhi() {
		return renqizhi;
	}

	public void setRenqizhi(int renqizhi) {
		this.renqizhi = renqizhi;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
	public static ActivityItem buildBean(JSONObject o) throws JSONException{
		
		ActivityItem item = new ActivityItem();
		item.setHeadicon(JSONHelper.getString(o, "headicon"));
		item.setId(Integer.valueOf(JSONHelper.getString(o, "id")));
		item.setRenqizhi(Integer.valueOf(JSONHelper.getString(o, "renqizhi")));
		item.setNickname(JSONHelper.getString(o, "nicekname"));
		item.setTitle(JSONHelper.getString(o, "tittle"));
		item.setUid(JSONHelper.getString(o, "uid"));
		item.setUserInfo(JSONHelper.getString(o, "userinfo"));
	return item;
	}
}
