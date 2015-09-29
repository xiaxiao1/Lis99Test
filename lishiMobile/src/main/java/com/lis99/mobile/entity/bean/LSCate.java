package com.lis99.mobile.entity.bean;

import java.util.ArrayList;
import java.util.List;

public class LSCate {

	public final static int LSCATE_NORMAL = 0;
	public final static int LSCATE_VIRTUAL = 1;

	private String name;
	private int ID;
	private int parentID;

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	List<LSCate> subCates;
	
	public List<LSCate> getSubCates() {
		return subCates;
	}
	public void setSubCates(List<LSCate> subCates) {
		this.subCates = subCates;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getParentID() {
		return parentID;
	}
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}
	
	public void addSubCate(LSCate cate){
		if(subCates == null){
			subCates = new ArrayList<LSCate>();
		}
		subCates.add(cate);
	}
	
	
}
