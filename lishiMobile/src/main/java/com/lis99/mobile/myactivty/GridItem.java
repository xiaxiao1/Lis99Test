package com.lis99.mobile.myactivty;

public class GridItem {
	private String title;
	private int imageId;

	public GridItem() {
		super();
	}

	public GridItem(String title, int imageId, String time) {
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public String getTitle() {
		return title;
	}

	public int getImageId() {
		return imageId;
	}
}
