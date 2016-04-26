package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			totalpage	int	总页数
			topictotal	int	总条数
			specialinfo	------------------------------------------	---------------------------------
			id	int	帖子专辑id
			title	string	帖子专题标题
			subhead	string	帖子专题副标题
			image	string	banner
			describe	string	导语
			topiclist	------------------------------------------	列表信息
			topic_id	int	帖子id
			title	string	帖子标题
			image	string	帖子banner
			reply_num	int	回复数
			nickname	string	发帖人昵称
			headicon	string	发帖人头像
			is_vip	int	vip(1:vip 0:不是vip)
			createdate	string	帖子发布时间

 * @author yy
 *
 */
public class SubjectModel extends BaseModel
{
	public int totalpage;
	public int topictotal;
	public Specialinfo specialinfo;
	public ArrayList<Topiclist> topiclist;
	
	/**
	 * 		id	int	帖子专辑id
			title	string	帖子专题标题
			subhead	string	帖子专题副标题
			image	string	banner
			describe	string	导语
			topiclist	------------------------------------------	列表信息
			
	 * @author yy
	 *
	 */
	public class Specialinfo
	{
		public int id;
		public String title;
		public String subhead;
		public String image;
		public String describe;
	}
	/**
	 * 		topic_id	int	帖子id
			title	string	帖子标题
			image	string	帖子banner
			reply_num	int	回复数
			nickname	string	发帖人昵称
			headicon	string	发帖人头像
			is_vip	int	vip(1:vip 0:不是vip)
			createdate	string	帖子发布时间
	 * @author yy
	 *
	 */
	public class Topiclist
	{
		public int topic_id;
		public String title;
		public String image;
		public String reply_num;
		public String nickname;
		public String headicon;
		public int is_vip;
		public String createdate;
/**		type == 12  帖子类型（0:旧版话题帖子 3：新版话题帖子）*/
		public int category;
	}
	
	
}
