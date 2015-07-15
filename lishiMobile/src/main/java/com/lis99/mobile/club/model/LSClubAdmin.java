package com.lis99.mobile.club.model;

public class LSClubAdmin {
	
	int user_id;
	String nickname;
	String headicon;
	String is_vip;
	public String getIs_vip()
	{
		return is_vip;
	}
	public void setIs_vip(String is_vip)
	{
		this.is_vip = is_vip;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadicon() {
		return headicon;
	}
	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}
	
	

}
