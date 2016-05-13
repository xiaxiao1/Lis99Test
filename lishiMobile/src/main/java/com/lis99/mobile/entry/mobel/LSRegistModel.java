package com.lis99.mobile.entry.mobel;

import java.io.Serializable;
/**
 * 		user_id	int	用户id
		nickname	string	用户昵称
		headicon	string	头像
		is_vip	tinyint	是否加V：0不加，1加
		tags	string	标签
		token	string	Token信息

 * @author yy
 *
 */
public class LSRegistModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String user_id;
	public String nickname;
	public String headicon;
	public String is_vip;
	public String tags;
	public String token;


	@Override
	public String toString() {
		return "LSRegistModel{" +
				"user_id='" + user_id + '\'' +
				", nickname='" + nickname + '\'' +
				", headicon='" + headicon + '\'' +
				", is_vip='" + is_vip + '\'' +
				", tags='" + tags + '\'' +
				", token='" + token + '\'' +
				'}';
	}
}
