package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class WendaNewBean {

	private String id;
	private String answernum;
	private String title;
	private String user_id;
	private String cate;
	private String date;
	private String cate_title;
	private String cate_id;
	
	public String getCate_title() {
		return cate_title;
	}
	public void setCate_title(String cate_title) {
		this.cate_title = cate_title;
	}
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	private List<String> images;
	
	
	
	public List<String> getImages() {
		if(images == null)
			images = new ArrayList<String>();
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnswernum() {
		return answernum;
	}
	public void setAnswernum(String answernum) {
		this.answernum = answernum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
