package com.lis99.mobile.club.model;

import com.lis99.mobile.club.model.EquipAppraiseModel.Profilelist;
import com.lis99.mobile.club.model.NearbyModel.Shoplist;

import java.util.ArrayList;

/**
 * 			装备新加接口所有内容
 * @author yy
 *
 */
public class EquipBaseModel extends BaseModel
{
//	public Accesslist EquipTypeItem;
//	
//	public Zhuangbeilist RecommendItem;
	
	public Shoplist NearbyItem;
	
	public ArrayList<Profilelist> AppraiseItem;
	
//	public EquipAppraiseModel AppraiseItem;
	
	public EquipTypeModel EquipTypeItem;
	
	public EquipRecommendModel RecommendItem;
	
}
