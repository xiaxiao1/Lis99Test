package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;
/***
 * 		totpage	int	总页数
		topictot	int	总条数
		topiclist	-------------------------------------------	帖子回复信息
		replytopic_id	int	回复帖子id
		user_id	int	用户id
		nickname	string	用户名
		headicon	string	用户头像
		is_vip	int	是否加V：0不加，1加
		flag	tinying	标志位0：正常 -1：已删除
		is_image	tinyint	是否有图片：0无，1有
		topic_image	-------------------------------------------	图片列表
		image	string	图片URL
		reply_id	int	回复帖子信息
		reply_floor	int	回复帖子楼层
		reply_content	string	回复帖子内容
		reply_nickname	string	回复帖子昵称
		content	string	帖子内容
		floor	int	楼层
		createdate	string	时间
		likeNum	int	赞数量
		LikeStatus	tinyint	是否赞过：0未赞，1已赞
 * 
 * @author yy
 *
 */
public class ClubTopicReplyList implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int totpage;
	public int topictot;
	
	public ArrayList<Topiclist> topiclist;
	
	public class Topiclist
	{
		public String replytopic_id;
		public String user_id;
		public String nickname;
		public String headicon;
		public String is_vip;
		public String flag;
		public String is_image;
		public ArrayList<Topic_image> topic_image;
		public String reply_id;
		public String reply_floor;
		public String reply_content;
		public String reply_nickname;
		public String content;
		public String floor;
		public String createdate;
		public String likeNum;
		public String LikeStatus;
		public String is_lander;
		public String is_jion;

		public int zhuangbei_id;
		public String zhuangbei_image;
		public String zhuangbei_title;
		public String zhuangbei_price;
		public int zhuangbei_star;

		public class Topic_image
		{
			public String image;
		}
	}
	
	
}
