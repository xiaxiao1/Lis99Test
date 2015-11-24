package com.lis99.mobile.club.model;

import java.io.Serializable;
/**
 * 	club_id	int	俱乐部id
	title	string	俱乐部标题
	images	string 	俱乐部图片
	members	int	俱乐部成员
	cityname	string	俱乐部城市信息
	catename	string	俱乐部类型
	is_jion	int	1创始人，2管理员，4成员,8网站编辑，-1无权限信息

 */
public class ClubDetailHead implements Serializable{

	
	private static final long serialVersionUID = 1L;

	public String club_id;
	public String title;
	public String images;
	public String members;
	public String cityname;
	public String catename;
	public String is_jion;
	public String topicnum;
	public int ui_levels;

}
