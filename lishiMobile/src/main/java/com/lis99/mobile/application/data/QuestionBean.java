package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class QuestionBean {

	private String id;
	private String answernum;
	private AuthorBean author;
	private String likenum;
	private String title;
	private String desc;
	private String share_url;
	private List<AnswerBean> answers = new ArrayList<AnswerBean>();
	private List<String> images = new ArrayList<String>();
	private String like_status;
	private String date;
	private DarenBean daren;
	
	public DarenBean getDaren() {
		return daren;
	}
	public void setDaren(DarenBean daren) {
		this.daren = daren;
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
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public void setAnswernum(String answernum) {
		this.answernum = answernum;
	}
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public String getLikenum() {
		return likenum;
	}
	public void setLikenum(String likenum) {
		this.likenum = likenum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getShare_url() {
		return share_url;
	}
	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}
	public List<AnswerBean> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerBean> answers) {
		this.answers = answers;
	}
	public String getLike_status() {
		return like_status;
	}
	public void setLike_status(String like_status) {
		this.like_status = like_status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
