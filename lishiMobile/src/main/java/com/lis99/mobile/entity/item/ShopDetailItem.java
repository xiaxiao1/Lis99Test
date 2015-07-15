package com.lis99.mobile.entity.item;

import java.util.ArrayList;

public class ShopDetailItem {
	private Info info;
	private ArrayList<Goods> goods;
	private ArrayList<Brand> brand;
	private ArrayList<Pic> pic;
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public ArrayList<Goods> getGoods() {
		return goods;
	}
	public void setGoods(ArrayList<Goods> goods) {
		this.goods = goods;
	}
	public ArrayList<Brand> getBrand() {
		return brand;
	}
	public void setBrand(ArrayList<Brand> brand) {
		this.brand = brand;
	}
	public ArrayList<Pic> getPic() {
		return pic;
	}
	public void setPic(ArrayList<Pic> pic) {
		this.pic = pic;
	}
	
	
	
}
