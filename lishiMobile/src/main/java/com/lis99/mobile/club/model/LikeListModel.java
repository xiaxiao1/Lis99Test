package com.lis99.mobile.club.model;

import java.io.Serializable;

/**
 * Created by yy on 15/9/16.
 * id	int	用户id
 headicon	string	用户头像
 is_vip	int	0：非vip  1：vip

 */
public class LikeListModel implements Serializable {

    public int id;
    public String headicon;
    public int is_vip;

}
