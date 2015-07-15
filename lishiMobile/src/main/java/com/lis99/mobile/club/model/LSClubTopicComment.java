package com.lis99.mobile.club.model;

import java.util.List;

public class LSClubTopicComment {

	int id;
	String nickname;
	String headicon;
	String content;
	String createdate;
	List<LSClubTopicImage> imagelist;
	int is_vip;
	
	


	public int getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public List<LSClubTopicImage> getImagelist() {
		return imagelist;
	}

	public void setImagelist(List<LSClubTopicImage> imagelist) {
		this.imagelist = imagelist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}
