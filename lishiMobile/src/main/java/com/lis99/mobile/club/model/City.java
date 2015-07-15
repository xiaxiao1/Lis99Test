package com.lis99.mobile.club.model;

public class City {

	int id;
	String name;
	String pinyin;

	public City() {
		super();
	}
	
	public City(int id, String name, String pinyin) {
		super();
		this.id = id;
		this.name = name;
		this.pinyin = pinyin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
