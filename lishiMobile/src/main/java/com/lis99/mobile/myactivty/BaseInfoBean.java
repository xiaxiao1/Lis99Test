package com.lis99.mobile.myactivty;


public class BaseInfoBean {
	private int id;
	private String title;
	private String image_url;
	private String base_info;
	private String location;
	private String full_info;

	@Override
	public String toString() {
		return "BaseInfoBean [id=" + id + ", title=" + title + ", image_url="
				+ image_url + ", base_info=" + base_info + ", location="
				+ location + ", full_info=" + full_info + "]";
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

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getBase_info() {
		return base_info;
	}

	public void setBase_info(String base_info) {
		this.base_info = base_info;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFull_info() {
		return full_info;
	}

	public void setFull_info(String full_info) {
		this.full_info = full_info;
	}
}