package com.lis99.mobile.application.data;


public class ActivityLeaderPicBean {
	private int id;
	String image_url;
	String title;
	String huodong_url;
	String startDate;
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	String endDate;

	@Override
	public String toString() {
		return "AvtivityLeaderPicBean [image_url=" + image_url + ", title="
				+ title + ", huodong_url=" + huodong_url + ", sequence="
				+ startDate + "-" + endDate + "]";
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHuodong_url() {
		return huodong_url;
	}

	public void setHuodong_url(String huodong_url) {
		this.huodong_url = huodong_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
