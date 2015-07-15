package com.lis99.mobile.newhome;

import com.lis99.mobile.club.model.EquipBaseModel;

import java.util.List;

public class LSSelectContent extends EquipBaseModel{

	int content_type_id;
	String content_type_title;
	int template_id;
	String template_title;
	String title;
	String descript;
	List<LSSelectItem> items;
	

	boolean isBanner;
	int is_startbanner;
	String banner_image;
	String bannerUrl;
	int eventID;
	int weight;
	
//	新加的id
	int equipType = -1;
	
	

	public int getContent_type_id() {
		return content_type_id;
	}

	public void setContent_type_id(int content_type_id) {
		this.content_type_id = content_type_id;
	}

	public String getContent_type_title() {
		return content_type_title;
	}

	public void setContent_type_title(String content_type_title) {
		this.content_type_title = content_type_title;
	}

	public int getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(int template_id) {
		this.template_id = template_id;
	}

	public String getTemplate_title() {
		return template_title;
	}

	public void setTemplate_title(String template_title) {
		this.template_title = template_title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public List<LSSelectItem> getItems() {
		return items;
	}

	public void setItems(List<LSSelectItem> items) {
		this.items = items;
	}

	public boolean isBanner() {
		return isBanner;
	}

	public void setBanner(boolean isBanner) {
		this.isBanner = isBanner;
	}

	public int getIs_startbanner() {
		return is_startbanner;
	}

	public void setIs_startbanner(int is_startbanner) {
		this.is_startbanner = is_startbanner;
	}

	public String getBanner_image() {
		return banner_image;
	}

	public void setBanner_image(String banner_image) {
		this.banner_image = banner_image;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
