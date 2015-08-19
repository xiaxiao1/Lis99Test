package com.lis99.mobile.application.data;

import android.text.TextUtils;

import com.lis99.mobile.util.Common;

import java.net.URLDecoder;

public class UserBean {

	@Override
	public String toString() {
		return "UserBean [nickname=" + nickname + ", email=" + email
				+ ", user_id=" + user_id + "]";
	}

	private String nickname;
	private String email;
	private String sn;
	private String password;
	private String user_id;
	private String headicon;
	private String sex;
	private String point;
	private String province_id;
	private String city_id;
	private String province;
	private String city;
	private String is_vip;
	private String note;
	private String vtitle;
	private String mobile;

	private int follows;


	public int getFollows() {
		return follows;
	}

	public void setFollows(int follows) {
		this.follows = follows;
	}

	private boolean is_follows;

	public boolean isIs_follows() {
		return is_follows;
	}

	public void setIsFollowed(boolean isFollowed) {
		this.is_follows = isFollowed;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getVtitle() {
		return vtitle;
	}

	public void setVtitle(String vtitle) {
		this.vtitle = vtitle;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_id() {
		/*
		 * if(user_id==null||"".equals(user_id)){ return "0"; }else{
		 */
		return user_id;
		// }
	}

	public void setUser_id(String user_id) {
		Common.log("setUser_id="+user_id);
		this.user_id = user_id;
	}

	public String getHeadicon() {
		if (TextUtils.isEmpty(headicon))
			return null;
		headicon = URLDecoder.decode(headicon);
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSn() {
		return sn;
	}
}
