package com.lis99.mobile.club.model;

import java.util.ArrayList;
import java.util.List;

public class LSClub {

	int id;
	String title;
	String image;
	String initnickname;
	String initheadicon;
	String descript;
	String create_time;
	String province;
	String city;
	List<LSClubAdmin> adminlist;
	List<LSClubTag> clubsport;
	List<LSClubTopic> topiclist;
	
	int totalNum;
	
	int members;
	int huatiTot;
	int huodongTot;
	int is_jion = -1;
	
	String topic_title;
	
	boolean local;
	
	public boolean isLocal()
	{
		return local;
	}

	public void setLocal(boolean local)
	{
		this.local = local;
	}

	int is_samecity;
	

	public int getIs_samecity()
	{
		return is_samecity;
	}

	public void setIs_samecity(int is_samecity)
	{
		this.is_samecity = is_samecity;
	}

	public boolean isVip = true;
	public String creater_is_vip;
	
	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getHuatiTot() {
		return huatiTot;
	}

	public void setHuatiTot(int huatiTot) {
		this.huatiTot = huatiTot;
	}

	public String getTopic_title() {
		return topic_title;
	}

	public void setTopic_title(String topic_title) {
		this.topic_title = topic_title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getInitnickname() {
		return initnickname;
	}

	public void setInitnickname(String initnickname) {
		this.initnickname = initnickname;
	}

	public String getInitheadicon() {
		return initheadicon;
	}

	public void setInitheadicon(String initheadicon) {
		this.initheadicon = initheadicon;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public List<LSClubAdmin> getAdminlist() {
		return adminlist;
	}

	public void setAdminlist(List<LSClubAdmin> adminlist) {
		this.adminlist = adminlist;
	}

	public List<LSClubTag> getClubsport() {
		return clubsport;
	}

	public void setClubsport(List<LSClubTag> clubsport) {
		this.clubsport = clubsport;
	}

	public int getMembers() {
		return members;
	}

	public void setMembers(int members) {
		this.members = members;
	}


	public int getHuodongTot() {
		return huodongTot;
	}

	public void setHuodongTot(int huodongTot) {
		this.huodongTot = huodongTot;
	}

	public int getIs_jion() {
		return is_jion;
	}

	public void setIs_jion(int is_jion) {
		this.is_jion = is_jion;
	}

	public List<LSClubTopic> getTopiclist() {
		return topiclist;
	}

	public void setTopiclist(List<LSClubTopic> topiclist) {
		this.topiclist = topiclist;
	}

	public void addTopics(List<LSClubTopic> topics) {
		if (topiclist == null) {
			topiclist = new ArrayList<LSClubTopic>();
		}
		topiclist.addAll(topics);
	}

	
}
