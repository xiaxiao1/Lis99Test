package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class ZhuanjiCate {

	private String id;
	private String albumId;
	private String title;
	private String desc;
	private String sortNum;
	private String createTime;
	private List<ZhuanjiCateChild> childs = new ArrayList<ZhuanjiCateChild>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
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
	public String getSortNum() {
		return sortNum;
	}
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<ZhuanjiCateChild> getChilds() {
		return childs;
	}
	public void setChilds(List<ZhuanjiCateChild> childs) {
		this.childs = childs;
	}
	
}
