package com.lis99.mobile.myactivty;

public class PhaseItem {

	private String stage;
	private int offset;

	public PhaseItem(String stage, int offset) {
		super();
		this.stage = stage;
		this.offset = offset;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
