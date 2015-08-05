package com.lis99.mobile.club.model;

import java.util.ArrayList;

public class LSClubGroup {

	public int typeid;
	public String typename;
	public ArrayList<ClubMainListModel> typeranks;

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

	public ArrayList<ClubMainListModel> getTyperanks() {
		return typeranks;
	}

	public void setTyperanks(ArrayList<ClubMainListModel> typeranks) {
		this.typeranks = typeranks;
	}

}
