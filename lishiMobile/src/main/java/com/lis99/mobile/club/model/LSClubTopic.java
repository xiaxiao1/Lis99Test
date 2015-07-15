package com.lis99.mobile.club.model;

import java.util.ArrayList;
import java.util.List;

public class LSClubTopic {

	int id;
	String title;
	int category;
	int stick;
	String content;
	String createdate;
	String nickname;
	String headicon;
	String clubname;
	int total;
	String club_title;

	int totalPage;

	int is_vip;

	int club_id;

	int is_jion = -1;

	int is_new;

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
	}

	public int getIs_new() {
		return is_new;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}

	List<LSClubTopicComment> commentlist;
	List<LSClubTopicImage> imagelist;

	int is_image;

	public int getIs_image() {
		return is_image;
	}

	public void setIs_image(int is_image) {
		this.is_image = is_image;
	}

	public String getClubname() {
		return clubname;
	}

	public void setClubname(String clubname) {
		this.clubname = clubname;
	}

	public String getClub_title() {
		return club_title;
	}

	public void setClub_title(String club_title) {
		this.club_title = club_title;
	}

	public int getClub_id() {
		return club_id;
	}

	public void setClub_id(int club_id) {
		this.club_id = club_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getStick() {
		return stick;
	}

	public void setStick(int stick) {
		this.stick = stick;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<LSClubTopicComment> getCommentlist() {
		return commentlist;
	}

	public void setCommentlist(List<LSClubTopicComment> commentlist) {
		this.commentlist = commentlist;
	}

	public List<LSClubTopicImage> getImagelist() {
		return imagelist;
	}

	public void setImagelist(List<LSClubTopicImage> imagelist) {
		this.imagelist = imagelist;
	}

	public int getIs_jion() {
		return is_jion;
	}

	public void setIs_jion(int is_jion) {
		this.is_jion = is_jion;
	}

	public void addComments(List<LSClubTopicComment> comments) {
		if (commentlist == null) {
			commentlist = new ArrayList<LSClubTopicComment>();
		}
		commentlist.addAll(comments);
	}

}
