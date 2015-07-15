package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * 			店铺
 * @author yy
 *
 */
public class EquipTypeModel extends BaseModel
{
	public ArrayList<Accesslist> accesslist;
	/**
	 * 			accesslist: [
				{
				title: "户外店",
				url: "outdoor_shop"
				},
				{
				title: "商场店",
				url: "outdoor_mall"
				},
				{
				title: "全部店铺",
				url: "outdoor_all"
				}
	 * @author yy
	 *
	 */
	public class Accesslist
	{
		public String title;
		public String url;
	}
}
