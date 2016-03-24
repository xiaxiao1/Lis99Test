package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ClubDetailList implements Serializable{

	
	private static final long serialVersionUID = 1L;
//	置顶
	public ArrayList<Toptopiclist> toptopiclist;
//	非置顶
	public ArrayList<Topiclist> topiclist;
	/**总页数*/
	public int totpage;
	public int topictot;

	/**
	 * 		id
	 int
	 帖子id
	 activity_code
	 varchar
	 帖子编码
	 title
	 string
	 帖子标题
	 category
	 tinyint
	 帖子类型：0旧版话题贴，1旧版线路活动帖, 2旧版线上活动
	 stick
	 tinyint
	 是否置顶：0全站置顶，1俱乐部置顶

	 */
	public static class Toptopiclist
	{
		public int id;

		public String activity_code;

		public String title;

		public String category;

		public String stick;


	}




	/**
	 * topiclist	-------------------------------------------	帖子列表信息
		id	int	帖子id
		title	string	帖子标题
		stick	tinyint	帖子类型：0全站帖子，1站内置顶帖子，2普通
		category	tinyint	帖子类型：0话题贴，1线路活动帖, 2线路活动
		is_image	tinyint	是否有图片：0无，1有
		createdate	string	创建日期
		user_id	int	用户id
		nickname	string	用户名
		replytot	int	回复数
		totpage	int	总页数
		topictot	int	总条数


	 */
	public class Topiclist
	{
		public int id;
		public String title;
		public String stick;
		/**
		 * 		帖子类型：0旧版话题贴，1旧版线路活动帖, 2旧版线上活动，3新版话题贴，4新版活动贴
		 */
		public String category;
		public String is_image;
		public String createdate;
		public String user_id;
		public String nickname;
		public String replytot;
		public String catename;
		public String times;
		public int is_hot;
		public String image;

		public int height;
		public int width;

		public String setaddress;
		public String starttime;

		public int is_vip;
		public int is_follow;
		public int likeNum;
		public int LikeStatus;
		public String headicon;
		/**
		 * 		判断是否为新版活动贴: 空为旧版活动贴，非空为新版活动贴
		 */
		public String activity_code;

		/**
		 * 		帖子编码
		 */
		public String topic_code;

	}

}
