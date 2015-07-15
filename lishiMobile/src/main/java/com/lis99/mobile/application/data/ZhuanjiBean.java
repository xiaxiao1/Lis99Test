package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class ZhuanjiBean {

	private String id;
	
	private String title;
	
	private String desc;
	
	private String image;
	
	private String thumb;
	
	private String shareUrl;
	private String wap_url;
	
	private List<ZhuanjiCate> listCates = new ArrayList<ZhuanjiCate>();
	
	

	public String getWap_url() {
		return wap_url;
	}

	public void setWap_url(String wap_url) {
		this.wap_url = wap_url;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public List<ZhuanjiCate> getListCates() {
		return listCates;
	}

	public void setListCates(List<ZhuanjiCate> listCates) {
		this.listCates = listCates;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}
