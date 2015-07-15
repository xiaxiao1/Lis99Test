package com.lis99.mobile.myactivty;

import java.io.Serializable;

public class ShowPic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image_url;

	@Override
	public String toString() {
		return "ShowPic [image_url=" + image_url + "]";
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

}
