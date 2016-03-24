package com.lis99.mobile.newhome.activeline;

import java.util.ArrayList;

/**
 * Created by zhangjie on 3/24/16.
 */
public class LSLineCateListModel {

    public ArrayList<ActivityItem> activitylist;
    /**总页数*/
    public int totalpage;
    public int total;
    /**
     * topiclist	-------------------------------------------	帖子列表信息
     id	int	帖子id
     title	string	帖子标题
     stick	tinyint	帖子类型：0全站帖子，1站内置顶帖子，2普通
     category	tinyint	帖子类型：0话题贴，1线路活动帖, 2线路活动
     is_image	tinyint	是否有图片：0无，1有
     createdate	string	创建日期
     user_id	int	用户id
     nickname	string	用户名
     replytot	int	回复数
     totpage	int	总页数
     topictot	int	总条数


     */
    public class ActivityItem
    {
        public int id;
        public String title;
        public String startdate;
        public String catename;
        public String harddesc;
        public String images;
        public String activity_code;



        public int height;
        public int width;

    }
}
