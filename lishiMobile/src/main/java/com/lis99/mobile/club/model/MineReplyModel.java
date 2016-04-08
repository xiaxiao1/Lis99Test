package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			回复的我
 * 			totpage	int	总页数
			replytot	int	回复我的数量
			replylist	--------------------------	回复我的列表信息
			fromuser_id	int	用户ID
			nickname	string	用户名
			headicon	string	用户头像
			is_vip	tinyint	是否加V：0不加，1加
			topic_title	string	帖子标题
			topic_content	string	帖子内容
			floor	int	楼层
			createtime	string	时间
			likeNum	int	赞数量
			LikeStatus	tinyint	是否赞过：0未赞，1已赞
			is_image	tinyint	是否有图片：0无，1有
			topic_image	-------------------------------------------	图片列表
			image	string	图片URL
			reply_id	int	回复帖子信息
			reply_floor	int	回复帖子楼层
			reply_content	string	回复帖子内容
			reply_nickname	string	回复帖子昵称
 * @author yy
 *
 */
public class MineReplyModel extends BaseModel
{

	/**
	 * 		
	 */
	private static final long serialVersionUID = 1L;
	
	public int totpage;
	
	public int replytot;
	
	public ArrayList<Replylist> replylist;
	
	public class Replylist
	{
		public int fromuser_id;
		public String nickname;
		public String headicon;
		public String is_vip;
		public String topic_title;
		public String topic_content;
		public String floor;
		public String createtime;
		public int likeNum;
		public String LikeStatus;
		public String is_image;
		public ArrayList<Topic_image> topic_image;
		public int reply_id;
		public String reply_floor;
		public String reply_content;
		public String reply_nickname;
		public int topic_id;
		public int club_id;
		public int replytopicid;

		/**
		 * 帖子类型：0话题贴，1线路活动帖 ，2线上活动帖,3 新版话题帖, 4 新版活动帖子
		 */
		public int category;
		
		public class Topic_image
		{
			public String image;
		}
		
	}
	
	
}
