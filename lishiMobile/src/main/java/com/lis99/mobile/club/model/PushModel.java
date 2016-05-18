package com.lis99.mobile.club.model;

import java.io.Serializable;
/**
 * 			1 => '启动App打开首页',
			2 => '启动App打开某个帖',
			3 => '个人中心里的报名通知',
			4 => '个人中心里的我发布的活动'
 			5	审核报名
 			6	订单详情	“topic_id” 和 “category” 都为0，order_id 有意义
			以下两个字段在类型是2的时候才有用，否则都是0
			"topid_id" = 0;
			"category" = 0
 * @author yy
 *
 */
public class PushModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int type = -1;
	public int topic_id = -1;
	public int category = -1;
//	只有当type=6的时候才会出现order_id
	public int order_id = -1;

	public String url;

	
}
