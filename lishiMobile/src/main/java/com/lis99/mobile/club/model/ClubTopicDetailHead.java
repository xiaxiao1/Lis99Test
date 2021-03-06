package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 			status	string	状态:OK成功，ERR失败
			error	string	错误信息
			data	-------------------------------------------	数据信息
			info	-------------------------------------------	帖子信息
			topic_id	int	帖子id
			title	string	帖子标题
			floor	int	楼层
			content	string	帖子内容
			is_image	tinyint	是否有图片：0无，1有
			topic_image	-------------------------------------------	帖子图片信息
			image	string	帖子图片URL
			club_id	int	俱乐部id
			club_title	string	俱乐部标题
			user_id	int	用户id
			nickname	string	用户名称
			headicon	string	用户头像
			category	tinyint	帖子类型：0话题贴，1线路活动帖
			catename	string	帖子分类
			hardDegree	tinyint	活动难度：1轻松，2,一般，3艰难
			time_type	tinyint	活动时间：0当天结束，1连续多天
			startDate	string	开始日期
			endDate	string	结束日期
			startTime	string	开始时间
			endTime	string	结束时间
			aimaddress	string	活动地址
			const	string	费用
			deadline	string	截止日期
			setTime	string	集合时间
			setaddress	string	集合地址
			gaodeLongitude	string	经度
			gaodeLatitude	string	纬度
			leader	string	领队
			mobile	string	手机号
			qq	string	QQ
			qqgroup	string	QQ群
			activeDesc	string	活动介绍
			activeFileUrl	string	活动介绍图片
			detailTrip	string	具体行程
			equipAdvise	string	装备建议
			disclaimer	string	免责声明
			constDesc	string	费用说明
			cateMatter	string	注意事项
			reportInfo	string	报名信息
			replytopic	string	回复数
			likeNum	int	赞数
			LikeStatus	int	是否赞过
			baomingNum	int	报名人数
			passNum	int	通过人数
			createdate	string	添加时间

 * @author yy
 *
 */
public class ClubTopicDetailHead implements ShareInterface, LikeInterface, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String stick;
	public String shareTxt;
	public String topic_id;
	public String title;
	public String floor;
	public String is_image;
	public String club_id;
	public String club_title;
	public String user_id;
	public String nickname;
	public String headicon;
	public String category;
	public String content;
	public String replytopic;
	public String likeNum;
	public String LikeStatus;
	public String createdate;
	public String times;
	public String is_vip;
	public String is_jion;
	public ArrayList<Topic_image> topic_image;

	public int zhuangbei_id;
	public String zhuangbei_image;
	public String zhuangbei_title;
	public String zhuangbei_price;
	public int zhuangbei_star;

	//=====活动===========
	public int applyTimeStatus;
	public int applyStauts;
	public String catename;
	public String hardDegree;
	public String time_type;
	public String startDate;
	public String endDate;
	public String startTime;
	public String endTime;
	public String aimaddress;
	public String consts;
	public String deadline;
	public String setTime;
	public String setaddress;
	public String gaodeLongitude;
	public String gaodeLatitude;
	public String leader;
	public String mobile;
	public String qq;
	public String qqgroup;
	public String activeDesc;
	public String activeFileUrl;
	public String detailTrip;
	public String equipAdvise;
	public String disclaimer;
	public String constDesc;
	public String cateMatter;
	public String reportInfo;
	public String baomingNum;
	public String passNum;

	public int[] tags;

	public ArrayList<tagsName> tags_name;

	public class tagsName
	{
		public String title;
		public String tags;
	}

//	3.6.3===
//  0：为关注 1已关注
	public int attenStatus;

	//===3.5====
	public ArrayList<Taglist> taglist;
	//积分
	public ArrayList<Topicpoints> topicpoints;


//	====3.5.5=======赞列表=====
	public ArrayList<LikeList> lists;

//	====== 3.9.1===== 版主  版主（1：版主 0：非版主）
	public String moderator;
//4.0.2 阅读量
	public String visits;

	@Override
	public String getTopic_id() {
		return this.topic_id;
	}

	@Override
	public String getLikeNum() {
		return this.likeNum;
	}

	@Override
	public String getLikeStatus() {
		return this.LikeStatus;
	}

	@Override
	public void setLikeStatus(String status) {
		this.LikeStatus = status;
	}

	@Override
	public void setLikeNum(String likeNum) {
		this.likeNum = likeNum;
	}

	@Override
	public ArrayList<LikeListModel> getList() {
		ArrayList<LikeListModel> lsm = new ArrayList<LikeListModel>(lists);
//		for ( LikeList ll : lists )
//		{
//			lsm.add(ll);
//		}
//		return lists;
		return lsm;
	}

	@Override
	public String getStick() {
		return this.stick;
	}

	@Override
	public void setStick(String s) {
		this.stick = s;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getIsJoin() {
		return is_jion;
	}

	@Override
	public String getTopicId() {
		return topic_id;
	}

	/**
	 * 图片Url 地址
	 *
	 * @return
	 */
	@Override
	public String getImageUrl() {
		String imgUrl = "";
		if (topic_image != null && topic_image.size() >= 1 )
		{
			imgUrl = topic_image.get(0).image;
		}
		return imgUrl;
	}

	/**
	 * 分享显示标题
	 *
	 * @return
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * 分享内容
	 *
	 * @return
	 */
	@Override
	public String getShareTxt() {
		return "";
	}

	/**
	 * 分享连接地址
	 *
	 * @return
	 */
	@Override
	public String getShareUrl() {
		return "http://club.lis99.com/actives/detail/" + topic_id;
	}

	/**
	 * 帖子类型
	 *
	 * @return 0, 其他， 1， 线上
	 */
	@Override
	public int getType() {
		return 0;
	}

	public String active_code;

	@Override
	public String getNewActive() {
		return active_code;
	}

	/**
	 * 俱乐部Id
	 *
	 * @return
	 */
	@Override
	public String getClubId() {
		return club_id;
	}


	public class Topic_image
	{
		public String image;
	}

	public class Taglist
	{
		public int id;
		public String name;
	}

	public class Topicpoints
	{
		public int points;
		public int reason;
		public String content;
		public String createtime;
	}

	public static class LikeList extends LikeListModel
	{
		public int id;
		public String headicon;
		public int is_vip;


		@Override
		public int getId() {
			return id;
		}

		@Override
		public String getHeadIcon() {
			return headicon;
		}

		@Override
		public int getIsVip() {
			return is_vip;
		}
	}

}
