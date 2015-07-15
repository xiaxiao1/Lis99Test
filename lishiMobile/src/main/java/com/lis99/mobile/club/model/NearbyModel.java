package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			附近户外店
 * @author yy
 *
 */
public class NearbyModel extends BaseModel
{
	
	public ArrayList<Shoplist> shoplist;
	/**
	 * 			id	int	店铺ID
				title	string	店铺名称
				city	int	所在省份
				ct	int	所在城市
				address	string	详细地址
				longitude	float	精度
				latitude	float	纬度
				gaode_longitude	float	高德精度
				gaode_latitude	float	高德纬度
				goodsnum	int	折扣
				img	string	banner
				distance	float	距离
	 * @author yy
	 *
	 */
	public class Shoplist
	{
		public int id;
		public String title;
		public int city;
		public int ct;
		public String address;
		public float longitude;
		public float latitude;
		public float gaode_longitude;
		public float gaode_latitude;
		public String goodsnum;
		public String img;
		public float distance;
		// 下横线
		public boolean line_all;
		//最后一条， 显示全部
		public boolean isLast;
		//第一条， 有标题栏
		public boolean isfirst;
	}
}
