package com.lis99.mobile.entity.bean;

import java.util.List;

public class LSCollection {
	
	LSCollectionShopInfo shop_info;
	List<LSCollectionItemInfo> sale_item;
	public LSCollectionShopInfo getShop_info() {
		return shop_info;
	}
	public void setShop_info(LSCollectionShopInfo shop_info) {
		this.shop_info = shop_info;
	}
	public List<LSCollectionItemInfo> getSale_item() {
		return sale_item;
	}
	public void setSale_item(List<LSCollectionItemInfo> sale_item) {
		this.sale_item = sale_item;
	}
	
	

}
