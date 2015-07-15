package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class ShaiYearBean {

	private String year;
	private List<ShaiDateBean> dateBean = new ArrayList<ShaiDateBean>();
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public List<ShaiDateBean> getDateBean() {
		return dateBean;
	}
	public void setDateBean(List<ShaiDateBean> dateBean) {
		this.dateBean = dateBean;
	}
	
}
