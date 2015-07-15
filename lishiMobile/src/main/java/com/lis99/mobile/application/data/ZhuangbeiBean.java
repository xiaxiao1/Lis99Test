package com.lis99.mobile.application.data;

public class ZhuangbeiBean {
	
	private String id;
	
	private String title;
	
	private String thumb;
	
	private String score;
	
	private String star;
	
	private String image;
	private String like;
	private String content;
	
	private String share_url;
	private String wap_url;
	
	
	

	public String getWap_url() {
		return wap_url;
	}

	public void setWap_url(String wap_url) {
		this.wap_url = wap_url;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		if(like ==null)
			like = "0";
		this.like = like;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}
	
}
