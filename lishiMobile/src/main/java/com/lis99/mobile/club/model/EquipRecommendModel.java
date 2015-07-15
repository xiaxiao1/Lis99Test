package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			装备推荐
 * @author yy
 *
 */
public class EquipRecommendModel extends BaseModel
{
	public ArrayList<Zhuangbeilist> zhuangbeilist;
	/**
	 * 		catename: "羽绒服",
			id: "12",
			type: "2"
	 * @author yy
	 *
	 */
	public class Zhuangbeilist
	{
		public String catename;
		public int id;
		public int type;
	}
}
