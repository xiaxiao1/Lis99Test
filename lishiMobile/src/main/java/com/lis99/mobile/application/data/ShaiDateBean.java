package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class ShaiDateBean {

	private String date;
	private List<ShaituDateBean> dateBean = new ArrayList<ShaituDateBean>();
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<ShaituDateBean> getDateBean() {
		return dateBean;
	}
	public void setDateBean(List<ShaituDateBean> dateBean) {
		this.dateBean = dateBean;
	}
}
