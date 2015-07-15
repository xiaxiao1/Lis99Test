package com.lis99.mobile.application.data;

public class AnswerDetailBean {
	
	private String id;
	private String answernum;
	private String likenum;
	private String title;
	private String desc;
	private String share_url;
	private AnswerBean answer;
	private String date;
	private String answer_like_status;
	private String question_like_status;
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
	public AnswerBean getAnswer() {
		return answer;
	}
	public void setAnswer(AnswerBean answer) {
		this.answer = answer;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAnswer_like_status() {
		return answer_like_status;
	}
	public void setAnswer_like_status(String answer_like_status) {
		this.answer_like_status = answer_like_status;
	}
	public String getQuestion_like_status() {
		return question_like_status;
	}
	public void setQuestion_like_status(String question_like_status) {
		this.question_like_status = question_like_status;
	}

}
