package com.lis99.mobile.club.model;
/**
 * haveApplyInfo = data.get("is_baoming").asBoolean();
		haveApply = data.get("manage_baoming").asBoolean();
		haveReply = data.get("is_reply").asBoolean();
		isFounder = data.get("is_creater").asBoolean();
		isAdministrator = data.get("is_admin").asBoolean();
 */
public class RedDotModel extends BaseModel
{
	public int is_baoming;
	public int manage_baoming;
	public int is_reply;
	public int is_creater;
	public int is_admin;
	public int is_follow;
	//系统消息
	public int notice;
}
