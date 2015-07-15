package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 			俱乐部排行
 * @author yy
 *
 */
public class ClubLevelModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<Hotclublist> hotclublist;
	
	
	public class Hotclublist
	{
		public String image;
		public String title;
		public String people;
		public String clubId;
		public String topicid;
		public String topictitle;
	}
	
}
