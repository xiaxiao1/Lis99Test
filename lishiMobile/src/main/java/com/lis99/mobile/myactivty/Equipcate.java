package com.lis99.mobile.myactivty;


public class Equipcate {
	
	private String catname ;
	private String thumb ;
	public String getCatname() {
		return catname;
	}
	public void setCatname(String catname) {
		this.catname = catname;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	@Override
	public String toString() {
		return "Equipcate [catname=" + catname + ", thumb=" + thumb + "]";
	}
	
	
	
}
