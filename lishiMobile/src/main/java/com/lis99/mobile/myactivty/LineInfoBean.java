package com.lis99.mobile.myactivty;

import java.util.ArrayList;
import java.util.List;

public class LineInfoBean {
	private int id;
	private int man;
	private int activeDiff;
	private int activeType;
	private int provinceid;
	private int cityid;
	private int total_showPic;
	private int areaid;
	private int activeCost;
	private int renqizhi;
	private int uid;
	private int vip;
	private int costFee;
	private String title;
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	private String thumb;
	private String catename;
	private String end_date;

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	private String address;
	private String startTimes;
	private String endTimes;
	private String cityname;
	private String placeIntro;
	private String activeIntro;
	private String start_date;
	private String travePlan;
	private String equipDiscuss;
	private String selection_end_date;
	private UserInfo userInfo;

	List<Comment> comment;
	List<Equipcate> Equipcate;

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public ArrayList<ShowPic> getShowPic() {
		return showPic;
	}

	public void setShowPic(ArrayList<ShowPic> showPic) {
		this.showPic = showPic;
	}

	ArrayList<ShowPic> showPic;

	public List<Equipcate> getEquipcate() {
		return Equipcate;
	}

	public void setEquipcate(List<Equipcate> equipcate) {
		Equipcate = equipcate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMan() {
		return man;
	}

	public void setMan(int man) {
		this.man = man;
	}

	public int getActiveDiff() {
		return activeDiff;
	}

	public void setActiveDiff(int activeDiff) {
		this.activeDiff = activeDiff;
	}

	public int getActiveType() {
		return activeType;
	}

	public void setActiveType(int activeType) {
		this.activeType = activeType;
	}

	public int getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(int provinceid) {
		this.provinceid = provinceid;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public int getTotal_showPic() {
		return total_showPic;
	}

	public void setTotal_showPic(int total_showPic) {
		this.total_showPic = total_showPic;
	}

	public int getAreaid() {
		return areaid;
	}

	public void setAreaid(int areaid) {
		this.areaid = areaid;
	}

	public int getActiveCost() {
		return activeCost;
	}

	public void setActiveCost(int activeCost) {
		this.activeCost = activeCost;
	}

	public int getRenqizhi() {
		return renqizhi;
	}

	public void setRenqizhi(int renqizhi) {
		this.renqizhi = renqizhi;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getCostFee() {
		return costFee;
	}

	public void setCostFee(int costFee) {
		this.costFee = costFee;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(String startTimes) {
		this.startTimes = startTimes;
	}

	public String getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(String endTimes) {
		this.endTimes = endTimes;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getPlaceIntro() {
		return placeIntro;
	}

	public void setPlaceIntro(String placeIntro) {
		this.placeIntro = placeIntro;
	}

	public String getActiveIntro() {
		return activeIntro;
	}

	public void setActiveIntro(String activeIntro) {
		this.activeIntro = activeIntro;
	}

	public String getTravePlan() {
		return travePlan;
	}

	public void setTravePlan(String travePlan) {
		this.travePlan = travePlan;
	}

	public String getEquipDiscuss() {
		return equipDiscuss;
	}

	public void setEquipDiscuss(String equipDiscuss) {
		this.equipDiscuss = equipDiscuss;
	}

	public String getSelection_end_date() {
		return selection_end_date;
	}

	public void setSelection_end_date(String selection_end_date) {
		this.selection_end_date = selection_end_date;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}