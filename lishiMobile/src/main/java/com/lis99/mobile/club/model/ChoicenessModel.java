package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			totalpage	int	总页数
			omnibustotal	int	总条数
			omnibuslist	------------------------------------------	列表信息
			id	int	列表id
			title	string	精选标题
			subhead	string	精选副标题
			image	string	banner
			url	string	跳转链接
			type	int	精选类型(1:活动帖，2：话题帖 3：URL 4：帖子专题)
			nickname	string	发帖者昵称
			headicon	string	发帖者头像
			is_vip	int	VIP(1:vip 0:不是vip)
			reply_num	int	帖子回复数
			topic_id	int	帖子id
			position	string	所在地
			club_logo	string	俱乐部logo
			club_title	string	俱乐部名称
 * @author yy
 *
 */
public class ChoicenessModel extends BaseModel
{
	public int totalpage;
	public int omnibustotal;
	public ArrayList<Omnibuslist> omnibuslist;
	
	public class Omnibuslist
	{
		public String id;
		public String title;
		public String subhead;
		public String image;
		public String url;
		public int type;
		public String nickname;
		public String headicon;
		public int is_vip;
		public String reply_num;
		public int topic_id;
		public String position;
		public String club_logo;
		public String club_title;
	}
		
}
