package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			装备评测
 * @author yy
 *
 */
public class EquipAppraiseModel extends BaseModel
{
	
	public ArrayList<Profilelist> profilelist;
	/**
	 * 			topic_id	int	帖子id
				title	string	帖子标题
				image	string	banner

	 * @author yy
	 *
	 */
	public class Profilelist
	{
		//是否是第一条， 显示标题栏
		public boolean first;
		
		public int topic_id;
		public String title;
		public String image;
	}
}
