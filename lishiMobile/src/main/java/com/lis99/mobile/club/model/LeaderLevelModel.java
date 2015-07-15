package com.lis99.mobile.club.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LeaderLevelModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Leaderlist> leaderlist;
	/***
	 * @author yy
	 *
	 */
	public class Leaderlist
	{
		public String user_id;
		public String headicon;
		public String nickname;
		public String tottopic;
		public String club_id;
		public String club_title;
		public String is_vip;
	}
}
