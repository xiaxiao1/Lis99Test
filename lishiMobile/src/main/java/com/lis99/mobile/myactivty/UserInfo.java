package com.lis99.mobile.myactivty;

public class UserInfo {
	private String headicon;
	private String nickname;
	private String note;
	private String vip;

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	@Override
	public String toString() {
		return "UserInfo [headicon=" + headicon + ", nickname=" + nickname
				+ ", note=" + note + ", vip=" + vip + "]";
	}
}