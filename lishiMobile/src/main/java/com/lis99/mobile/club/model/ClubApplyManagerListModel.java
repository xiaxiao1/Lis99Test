package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;
/***
 * topic_id	int	帖子id
title	string	帖子标题
club_id	int	聚乐部id
applyPass	int	通过人数
applyRefuse	int	被拒绝的人数
applyAudit	int	正在审核的人数
applyTotal	int	报名总数

 * @author yy
 *
 */
public class ClubApplyManagerListModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***
	 * 	name	string	真实姓名
		names	stirng	真实姓名(标签名)
		sex	string	性别
		sexs	string	性别(标签名)
		mobile	int	手机号码
		mobiles	int	手机号码(标签名)
		credentials	string	身份证号
		credentialss	string	身份证号(标签名)
		phone	int	紧急联系人电话
		phones	int	紧急联系人电话(标签名)
		qq	int	QQ号码
		qqs	int	QQ号码(标签名)
		address	string	居住城市
		addresss	string	居住城市(标签名)
		postaladdress	string	邮寄地址
		postaladdresss	string	邮寄地址(标签名)
		willnum	int	同行人数
		willnums	int	同行人数(标签名)
		const	int	应付费用
		consts	string	应付费用(标签名)
		is_vip	int	1:vip  0：不是vip
		nickname	string	昵称
		headicon	string	用户图像
		createdate	string	报名时间
		flag	int	1：已通过2：等待审核   -1：被拒绝

	 */
	public ArrayList<ApplyList> list;
	
	public String topic_id;
	public String title;
	public String club_id;
	public String applyPass;
	public String applyRefuse;
	public String applyAudit;
	public String applyTotal;
	
	public int totalNum;
	public int totPage;
	
	public class ApplyList
	{
		public String name;
		public String names;
		public String sex;
		public String sexs;
		public String mobile;
		public String mobiles;
		public String credentials;
		public String credentialss;
		public String phone;
		public String phones;
		public String qq;
		public String qqs;
		public String address;
		public String addresss;
		public String postaladdress;
		public String postaladdresss;
		public String willnum;
		public String willnums;
		public String cost;
		public String costs;
		public String is_vip;
		public String is_vips;
		public String nickname;
		public String headicon;
		public String createdate;
		public String flag;
		//申请人ID
		public String applyid;
	}
	
	
}
