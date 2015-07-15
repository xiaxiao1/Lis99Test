package com.lis99.mobile.application.data;

import java.util.ArrayList;
import java.util.List;

public class DarenHuidaBean {

	private DarenBean user;
	private List<HuidaBean> lists = new ArrayList<HuidaBean>();
	public DarenBean getUser() {
		return user;
	}
	public void setUser(DarenBean user) {
		this.user = user;
	}
	public List<HuidaBean> getLists() {
		return lists;
	}
	public void setLists(List<HuidaBean> lists) {
		this.lists = lists;
	}
	
	
}
