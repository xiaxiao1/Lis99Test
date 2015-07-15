package com.lis99.mobile.entity.item;

import java.io.Serializable;
import java.util.ArrayList;

public class DPListItem implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private ArrayList<Shop> shop;
private ArrayList<Citys> citys;
private int count;
public ArrayList<Shop> getShop() {
	return shop;
}
public void setShop(ArrayList<Shop> shop) {
	this.shop = shop;
}
public ArrayList<Citys> getCitys() {
	return citys;
}
public void setCitys(ArrayList<Citys> citys) {
	this.citys = citys;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}

}
