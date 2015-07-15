package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class DarenDetailBean {

	private String huida_num;
	private String following_num;
	private String followers_num;
	//private String shaitu_list;
	private String shaitu_num;
	private String relation;
	private List<AnswerBean> answers = new ArrayList<AnswerBean>();
	private List<ShaiYearBean> shaiyears = new ArrayList<ShaiYearBean>();
	
	public List<AnswerBean> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswerBean> answers) {
		this.answers = answers;
	}
	public List<ShaiYearBean> getShaiyears() {
		return shaiyears;
	}
	public void setShaiyears(List<ShaiYearBean> shaiyears) {
		this.shaiyears = shaiyears;
	}
	private DarenBean user = new DarenBean();
	public String getHuida_num() {
		return huida_num;
	}
	public void setHuida_num(String huida_num) {
		this.huida_num = huida_num;
	}
	public String getFollowing_num() {
		return following_num;
	}
	public void setFollowing_num(String following_num) {
		this.following_num = following_num;
	}
	public String getFollowers_num() {
		return followers_num;
	}
	public void setFollowers_num(String followers_num) {
		this.followers_num = followers_num;
	}
	public String getShaitu_num() {
		return shaitu_num;
	}
	public void setShaitu_num(String shaitu_num) {
		this.shaitu_num = shaitu_num;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public DarenBean getUser() {
		return user;
	}
	public void setUser(DarenBean user) {
		this.user = user;
	}
}
