package com.lis99.mobile.myactivty;


public class Comments {
	
	private String headicon ;
	private String commentid ;
	/**
	 * @return the commentid
	 */
	public String getCommentid() {
		return commentid;
	}
	/**
	 * @param commentid the commentid to set
	 */
	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}
	private String nickname ;
	private String createtime ;
	private String comment ;
	public String getHeadicon() {
		return headicon;
	}
	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
