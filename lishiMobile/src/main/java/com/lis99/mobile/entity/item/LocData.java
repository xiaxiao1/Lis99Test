package com.lis99.mobile.entity.item;

import com.baidu.location.BDLocation;

import java.io.Serializable;

public class LocData implements Serializable{

	/**
	 * 定位数据
	 */
	private static final long serialVersionUID = 5633474767708823232L;
	private BDLocation location;
	public BDLocation getLocation() {
		return location;
	}
	public void setLocation(BDLocation location) {
		this.location = location;
	}

}
