package com.lis99.mobile.club.model;

import java.util.List;

public class LSClubGroup {

	int typeid;
	String typename;
	List<LSClub> typeranks;

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public List<LSClub> getTyperanks() {
		return typeranks;
	}

	public void setTyperanks(List<LSClub> typeranks) {
		this.typeranks = typeranks;
	}

}
