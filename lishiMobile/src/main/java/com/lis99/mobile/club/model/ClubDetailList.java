package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ClubDetailList implements Serializable{

	
	private static final long serialVersionUID = 1L;

	public ArrayList<Topiclist> topiclist;
	/**总页数*/
	public int totpage;
	public int topictot;
	/**
	 * topiclist	-------------------------------------------	帖子列表信息
		id	int	帖子id
		title	string	帖子标题
		stick	tinyint	帖子类型：0全站帖子，1站内置顶帖子，2普通
		category	tinyint	帖子类型：0话题贴，1线路活动帖
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
		public String category;
		public String is_image;
		public String createdate;
		public String user_id;
		public String nickname;
		public String replytot;
		public String catename;
		public String times;
		public ClubActiveImages image;
		
	}

}
