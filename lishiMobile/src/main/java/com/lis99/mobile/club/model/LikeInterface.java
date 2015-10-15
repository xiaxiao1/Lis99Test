package com.lis99.mobile.club.model;

import java.util.ArrayList;

/**
 * Created by yy on 15/10/14.
 */
public interface LikeInterface {

    //帖子ID
    public String getTopic_id ();
//    喜欢数量
    public String getLikeNum();
//  点赞状态
    public String getLikeStatus();

    //  点赞状态
    public void setLikeStatus(String status);

    //    喜欢数量
    public void setLikeNum(String likeNum);

//  点赞的列表
    public ArrayList<LikeListModel> getList ();


}
