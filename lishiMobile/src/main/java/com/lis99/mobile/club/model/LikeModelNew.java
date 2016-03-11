package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/16.
 *
 *  status	string	状态:OK成功，ERR失败
 error	string	错误信息
 data	--------------------------------------------------	数据信息
 likeNum	int	当前帖子点赞的总数量
 lists	--------------------------------------------------	-------------------------------------
 id	int	用户id
 headicon	string	用户头像
 is_vip	int	0：非vip  1：vip

 *
 */
public class LikeModelNew extends BaseModel {

    public int likeNum;

    public ArrayList<ClubTopicDetailHead.LikeList> lists;


}
