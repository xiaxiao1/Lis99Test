package com.lis99.mobile.entity.item;

import java.io.Serializable;

public class Shop implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2954087669884738484L;
	private String oid;
	private String title;
	private String describe;
	private String phone;
	private String address;
	private String brand;
	private String product;
	private String firsta;
	private String city;
	private String ct;
	private String grade;
	private String state;
	private String mem;
	private String sorts;
	private String ctime;
	private String category;
	private String authmember;
	private String longitude;
	private String latitude;
	private String gaode_longitude;
	private String gaode_latitude;
	private String distance;
	private String img;
	private String star;
	private String click;
	private String type;
	private String goodsnum;
	private boolean discount;

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getFirsta() {
		return firsta;
	}

	public void setFirsta(String firsta) {
		this.firsta = firsta;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}

	public String getSorts() {
		return sorts;
	}

	public void setSorts(String sorts) {
		this.sorts = sorts;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuthmember() {
		return authmember;
	}

	public void setAuthmember(String authmember) {
		this.authmember = authmember;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getGaode_longitude() {
		return gaode_longitude;
	}

	public void setGaode_longitude(String gaode_longitude) {
		this.gaode_longitude = gaode_longitude;
	}

	public String getGaode_latitude() {
		return gaode_latitude;
	}

	public void setGaode_latitude(String gaode_latitude) {
		this.gaode_latitude = gaode_latitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(String goodsnum) {
		this.goodsnum = goodsnum;
	}

}
